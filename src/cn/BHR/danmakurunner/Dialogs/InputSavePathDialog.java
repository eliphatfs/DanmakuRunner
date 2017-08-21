package cn.BHR.danmakurunner.Dialogs;

import java.io.*;
import java.util.ArrayList;

import android.app.*;
import android.content.DialogInterface;
import android.os.*;
import cn.BHR.danmakurunner.EditorActivity;
import cn.BHR.danmakurunner.Projecting.InputNewProjectNameDialog;

public class InputSavePathDialog extends DialogFragment {
	static ArrayList<String> items = new ArrayList<String>();
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		File dir = new File(EditorActivity.projectPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		final File[] files = dir.listFiles();
		items.clear();
		items.add("新建文件...");
		items.add("新建工程...");
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				items.add(files[i].getName());
			}
		}
	    AlertDialog.Builder builder = 
	        new AlertDialog.Builder(EditorActivity.instance);
	    builder.setTitle("选择要保存到的代码");
	    builder.setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            // which 下标从0开始
	        	final int wh_In = which;
	        	if (which == 0) {
					InputPathDialog dialog2 = new InputPathDialog();
					dialog2.show(getFragmentManager(), "inputNewFilename");
				}
	        	else if (which == 1) {
					InputNewProjectNameDialog.show();
				}
	        	else {
					try {
						File dest = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DanmakuRunner/");
						if (!dest.exists()) {
							dest.mkdirs();
						}
						new ConfirmDialog("确认覆盖 Override", "注意："+items.get(wh_In)+"中原有内容会被覆盖") {
							@Override
							public void OnOKPressed() {
								FileOutputStream stream;
								try {
									stream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DanmakuRunner/" + items.get(wh_In));
									stream.write(EditorActivity.editorMain.getText().toString().getBytes());
									stream.close();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}.show();
						} catch (Exception e) {
						e.printStackTrace();
					}
					InputPathDialog.workingPath = files[which-1].getName().toString();
				}
	        }
	    });
		return builder.create();
	}
}
