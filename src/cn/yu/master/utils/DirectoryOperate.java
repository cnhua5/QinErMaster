package cn.yu.master.utils;

import java.io.File;
import java.io.FilenameFilter;

import android.util.Log;

import cn.yu.master.entries.FileObject;

public class DirectoryOperate {

	public static final String TAG = "DirectoryOperate";

	private static final int RECORD_DIRS = 2;

	public static final String INTERNAL_SDCARD = "storage/sdcard0";

	public static final String EXTERNAL_SDCARD = "storage/sdcard1";

	private boolean showHide = false;

	private String[] record_dirs = new String[RECORD_DIRS];

	public FileObject[] ls_al(String dir) {
		File[] fs = showHide(dir, showHide);
		Log.e(TAG, "list " + dir);

		FileObject[] files = new FileObject[fs.length];

		for (int i = 0; i < fs.length; i++) {
			FileObject obj = new FileObject();
			File f = fs[i];
			obj.name = f.getName();
			obj.path = f.getAbsolutePath();
			obj.type = f.isDirectory() ? 0 : 1;
			files[i] = obj;
			obj = null;
		}
		return files;
	}

	private File[] showHide(String dir, final boolean showHidee) {
		File file = new File(dir);
		File[] files = file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				if (filename.startsWith(".") && !showHidee) {
					return false;
				}
				return true;
			}
		});
		return files;
	}

	public void rm_rf(String dir, RecordDirChangeListener listener) {
		File f = new File(dir);
		if (f.exists() && delete(f)) {
			checkIsRecordChanged(dir, listener);
		}
	}

	public boolean delete(File file) {
		boolean isSuccess = false;
		Log.e(TAG, "start delete");
		if (file.isFile()) {
			isSuccess = file.delete();
			Log.e(TAG, "delete file " + isSuccess);
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				isSuccess = file.delete();
			}
			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			isSuccess = file.delete();
			Log.e(TAG, "delete dir " + isSuccess);
		}
		return isSuccess;
	}

	private void checkIsRecordChanged(String dir,
			RecordDirChangeListener listener) {
		if (dir.equals(record_dirs[0])) {
			listener.onRecordDirChanged(0);
		}
		if (dir.equals(record_dirs[1])) {
			listener.onRecordDirChanged(1);
		}
	}

	public interface RecordDirChangeListener {
		public void onRecordDirChanged(int recordId);
	}
}
