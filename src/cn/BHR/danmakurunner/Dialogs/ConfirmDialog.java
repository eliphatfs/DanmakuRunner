package cn.BHR.danmakurunner.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import cn.BHR.danmakurunner.EditorActivity;

public abstract class ConfirmDialog extends DialogFragment {
	private String mMessage;
	private String mTitle;
	public abstract void OnOKPressed();
	public void show()
	{
		this.show(
				EditorActivity.instance.getFragmentManager(), 
				mTitle);
	}
	public ConfirmDialog(String title, String Message)
	{
		super();
		mMessage = Message;
		mTitle = title;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(mTitle);
		builder.setMessage(mMessage);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p1, int p2)
			{
				OnOKPressed();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface p1, int p2)
			{
			}
		});
		return builder.create();
	}
}
