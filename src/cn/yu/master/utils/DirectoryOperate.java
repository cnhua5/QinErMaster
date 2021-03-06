package cn.yu.master.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.util.Log;
import cn.yu.master.entries.FileObject;

public class DirectoryOperate {

	public static final String TAG = "DirectoryOperate";

	public static final String INTERNAL_SDCARD = "/storage/sdcard0";

	public static final String EXTERNAL_SDCARD = "/storage/sdcard1";

	private boolean showHide = false;

	private SuperStack<HashMap<String, FileObject[]>> dirPool;

	public DirectoryOperate() {
		dirPool = new SuperStack<HashMap<String, FileObject[]>>();
	}

	public String backToParentDir(String dir) {
		if ("/".equals(dir)) {
			return "/";
		}
		File file = new File(dir);
		return file.getParent();
	}

	public void forwardToSubDir() {

	}

	public boolean isSDRootDir(String dir) {
		if (dir.equals(INTERNAL_SDCARD) || dir.equals(EXTERNAL_SDCARD)) {
			return true;
		}
		return false;
	}

	public FileObject[] ls_al(String dir) {
		File[] fs = showDir(dir, showHide);
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
		setDirPoolStack(dir, files);
		return files;
	}

	private File[] showDir(String dir, final boolean showHidee) {
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
		files = (files == null) ? new File[0] : files;
		return files;
	}

	private void setDirPoolStack(String dir, FileObject[] fs) {
		// if (fs.length < 5) {
		// return;
		// }
		for (int i = 0; i < SuperStack.INIT_SIZE; i++) {
			HashMap<String, FileObject[]> one = dirPool.get(i);
			if (one == null) {
				Log.e(TAG, i + "  of pool is null!!!!!");
			} else {
				Set<String> set = one.keySet();
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					String string = (String) iterator.next();
					Log.e(TAG, i + "  of pool is " + string);
				}
				if (null != one && set.contains(dir)) {
					return;
				}
			}
		}
		HashMap<String, FileObject[]> dirlist = new HashMap<String, FileObject[]>();
		dirlist.put(dir, fs);
		dirPool.push(dirlist);
	}

	public void rm_rf(String dir, RecordDirChangeListener listener) {
		File f = new File(dir);
		if (f.exists()) {
			if (delete(f)) {
				checkIsRecordChanged(dir, listener);
			} else {
				Log.e(TAG, "Delete File Failed!!!!!");
			}
		} else {
			Log.e(TAG, "Delete File Not Found!!!!!");
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
		listener.onRecordDirChanged();
	}

	public interface RecordDirChangeListener {
		public void onRecordDirChanged();
	}
}
