package cn.yu.master.services;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class QAlarmProvider extends ContentProvider {

	private static final String TAG = "QAlarmProvider";

	private static final int Q_ALARM = 1;
	private static final int Q_ALARM_ID = 2;

	private static final String Q_ID = "_id=";

	private static final UriMatcher mUriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	private QAlarmDatabaseHelper mQAlarmDatabaseHelper;

	static {
		mUriMatcher.addURI("cn.yu.master", "alarm", Q_ALARM);
		mUriMatcher.addURI("cn.yu.master", "alarm/#", Q_ALARM_ID);
	}

	@Override
	public boolean onCreate() {
		mQAlarmDatabaseHelper = new QAlarmDatabaseHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mQAlarmDatabaseHelper.getWritableDatabase();
		int count;
		long rowId = 0;
		switch (mUriMatcher.match(uri)) {
		case Q_ALARM:
			count = db.delete("alarms", selection, selectionArgs);
			break;
		case Q_ALARM_ID:
			String segment = uri.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			if (TextUtils.isEmpty(selection)) {
				selection = Q_ID + segment;
			} else {
				selection = Q_ID + segment + " AND (" + selection + ")";
			}
			count = db.delete("alarms", selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Cannot delete from URL: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		int match = mUriMatcher.match(uri);
		switch (match) {
		case Q_ALARM:
			return "vnd.master.cursor.dir/alarms";
		case Q_ALARM_ID:
			return "vnd.master.cursor.item/alarms";
		default:
			throw new IllegalArgumentException("Unknown URL");
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (mUriMatcher.match(uri) != Q_ALARM) {
			throw new IllegalArgumentException("Cannot insert into URL: " + uri);
		}

		Uri newUrl = commonInsert(uri, values);
		getContext().getContentResolver().notifyChange(newUrl, null);
		return newUrl;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.e(TAG, "Provider......query.....");
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		int match = mUriMatcher.match(uri);
		switch (match) {
		case Q_ALARM:
			builder.setTables(QAlarmDatabaseHelper.TABLE_NAME);
			break;
		case Q_ALARM_ID:
			builder.setTables(QAlarmDatabaseHelper.TABLE_NAME);
			builder.appendWhere(Q_ID);
			builder.appendWhere(uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		SQLiteDatabase db = mQAlarmDatabaseHelper.getReadableDatabase();
		Cursor c = builder.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);
		if (null == c) {
			Log.e(TAG, "query null cursor...........");
		} else {
			c.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count;
		long rowId = 0;
		int match = mUriMatcher.match(uri);
		SQLiteDatabase db = mQAlarmDatabaseHelper.getWritableDatabase();
		switch (match) {
		case Q_ALARM_ID: {
			String segment = uri.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update("alarms", values, Q_ID + rowId, null);
			break;
		}
		default: {
			throw new UnsupportedOperationException("Cannot update URL: " + uri);
		}
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	Uri commonInsert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mQAlarmDatabaseHelper.getWritableDatabase();
		db.beginTransaction();
		long rowId = -1;
		try {
			Object value = values.get("_id");
			if (value != null) {
				int id = (Integer) value;
				if (id > -1) {
					final Cursor cursor = db.query("alarms",
							new String[] { "_id" }, "_id = ?",
							new String[] { id + "" }, null, null, null);
					if (cursor.moveToFirst()) {
						values.putNull("_id");
					}
				}
			}

			rowId = db.insert("alarms", null, values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		if (rowId < 0) {
			throw new SQLException("Failed to insert row");
		}
		return ContentUris.withAppendedId(uri, rowId);
	}
}
