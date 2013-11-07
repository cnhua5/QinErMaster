package cn.yu.master.viewers;

import java.io.File;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.yu.master.R;
import cn.yu.master.entries.FileObject;
import cn.yu.master.utils.DirectoryOperate;
import cn.yu.master.utils.MimeUtils;

public class FileViewerFragment extends ListFragment {

	private static final String TAG = "FileViewerFragment";

	private FileObject[] list_dirs;

	private String currDir;

	private ItemAdapter mAdapter;
	private ListView mListView;

	private DirectoryOperate mDirectoryOperate;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mDirectoryOperate = new DirectoryOperate();
		currDir = getArguments().getString("file_dir");
		list_dirs = mDirectoryOperate.ls_al(currDir);
		Log.e(TAG, "" + currDir);

		mAdapter = new ItemAdapter();
		setListAdapter(mAdapter);
		mListView = getListView();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				FileObject obj = mAdapter.getItem(position);
				if (obj.type == 0) {
					currDir = obj.path;
					list_dirs = mDirectoryOperate.ls_al(currDir);
					mAdapter.notifyDataSetChanged();
					return;
				}
				MimeUtils.openFile(getActivity(), new File(obj.path));
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				FileObject obj = mAdapter.getItem(position);
				if (mDirectoryOperate.delete(new File(obj.path))) {
					list_dirs = mDirectoryOperate.ls_al(currDir);
					mAdapter.notifyDataSetChanged();
					Toast.makeText(getActivity(), "Delete finish....", 1000)
							.show();
					return true;
				}
				return true;
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	class ItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list_dirs.length;
		}

		@Override
		public FileObject getItem(int position) {
			return list_dirs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public int getType(int position) {
			return getItem(position).type;
		}

		class ViewHolder {
			TextView textView;
			ImageView imageView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (null == convertView) {
				view = LayoutInflater.from(
						FileViewerFragment.this.getActivity()).inflate(
						R.layout.row, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) view.findViewById(R.id.row_icon);
				holder.textView = (TextView) view.findViewById(R.id.row_title);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.textView.setText(getItem(position).name);
			holder.imageView.setImageResource(R.id.row_icon);
			return view;
		}
	}
}
