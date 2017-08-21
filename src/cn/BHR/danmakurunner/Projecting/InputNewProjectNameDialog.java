package cn.BHR.danmakurunner.Projecting;

import java.io.File;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.EditText;
import cn.BHR.danmakurunner.EditorActivity;

public class InputNewProjectNameDialog
{
	public static Dialog create()
	{
		final EditText editText = new EditText(EditorActivity.instance);
		AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.instance);
		builder.setTitle("输入工程名").setView(editText);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					File dest = new File(EditorActivity.projectPath + editText.getText().toString());
					if (!dest.exists()) {
						dest.mkdirs();
					}
					try {
						FileOutputStream outputStream = new FileOutputStream(EditorActivity.projectPath + editText.getText().toString() + "/Describer");
						outputStream.write("# Describe The Script Files In The Project Here".getBytes());
						outputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		return builder.create();
	}
	public static void show()
	{
		create().show();
	}
}
