package cn.BHR.danmakurunner;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity {
	public static MainActivity instance;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		//BmobMain.initialize(this);
		File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DanmakuRunner/ExtraTextures/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File sDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/");
		if (!sDir.exists())
		{
			sDir.mkdirs();
		}
		File tmpFileOld = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/danmakurunner.tmp");
		if (tmpFileOld.exists()) {
			tmpFileOld.delete();
		}
		setContentView(R.layout.main);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	}
	public void OnButtonClick_Doc(View view)
	{
		Intent runner = new Intent(this, DocumentActivity.class);
		startActivity(runner);
	}
	public void OnButtonClick_Net(View view)
	{
		Intent runner = new Intent(this, NetActivity.class);
		startActivity(runner);
	}
	public void OnButtonClick_Edit(View view)
	{
		Intent runner = new Intent(this, EditorActivity.class);
		startActivity(runner);
	}
	public void OnButtonClick_Settings(View view)
	{
		Intent runner = new Intent(this, SettingsActivity.class);
		startActivity(runner);
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		new Msgbox("").show(getFragmentManager(), "info");
		return false;
	}*/
}
