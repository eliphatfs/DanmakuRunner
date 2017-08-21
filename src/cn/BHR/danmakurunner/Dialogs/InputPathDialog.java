package cn.BHR.danmakurunner.Dialogs;

import java.io.File;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.widget.EditText;
import cn.BHR.danmakurunner.EditorActivity;
import cn.BHR.danmakurunner.Projecting.ProjectingMain;

public class InputPathDialog extends DialogFragment {
	public static String workingPath = "";
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final EditText editText = new EditText(getActivity());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("输入文件名").setView(editText);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					try {
						File dest = new File(EditorActivity.projectPath);
						if (!dest.exists()) {
							dest.mkdirs();
						}
						FileOutputStream stream = new FileOutputStream(EditorActivity.projectPath+editText.getText().toString());
						stream.write(EditorActivity.editorMain.getText().toString().getBytes());
						stream.close();
						if (EditorActivity.projecting) {
							ProjectingMain.fileName = editText.getText().toString();
							Message message = new Message();
							Bundle data = new Bundle();
							data.putCharSequence("title", "Danmaku Runner - " + ProjectingMain.projectName + " - " + (ProjectingMain.fileName.isEmpty() ? "Unsaved File" : ProjectingMain.fileName));
							message.setData(data);
							EditorActivity.updateTitleHandler.sendMessage(message);
						}
						} catch (Exception e) {
						e.printStackTrace();
					}
					workingPath = editText.getText().toString();
				}
			});
		return builder.create();
	}
}
