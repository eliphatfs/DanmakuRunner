package cn.BHR.danmakurunner.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ErrorDialog extends DialogFragment {
	private String mMessage;
	public ErrorDialog(String errorMessage)
	{
		super();
		mMessage = errorMessage;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("错误 Error");
		builder.setMessage(mMessage);
		builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
				}
			});
		return builder.create();
	}
}
