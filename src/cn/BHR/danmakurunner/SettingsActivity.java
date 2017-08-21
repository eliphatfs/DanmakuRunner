package cn.BHR.danmakurunner;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import java.io.*;

public class SettingsActivity extends Activity
{
	EditText sensibilityText;
	SeekBar sensibilitySeekBar;
	
	public static int Sensibility = 30;
	static
	{
		try{
			FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/Settings.dat");
			Sensibility = fis.read();
			fis.close();
		}
		catch(Exception e){}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
		sensibilityText = (EditText)findViewById(R.id.sensibilityText);
		sensibilitySeekBar = (SeekBar)findViewById(R.id.sensibilitySeekBar);
		sensibilitySeekBar.setProgress(30);
		//sensibilityText.setText(.toString());
		sensibilityText.setOnEditorActionListener(new TextView.OnEditorActionListener(){

				@Override
				public boolean onEditorAction(TextView p1, int p2, KeyEvent p3)
				{
					Sensibility = Integer.parseInt(p1.getText().toString());
					sensibilitySeekBar.setProgress(Sensibility);
					return true;
				}

			
		});
		sensibilitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar p1, int p2, boolean p3)
				{
					// TODO: Implement this method
					Sensibility = p1.getProgress();
					sensibilityText.setText(String.valueOf(p1.getProgress()));
				}

				@Override
				public void onStartTrackingTouch(SeekBar p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onStopTrackingTouch(SeekBar p1)
				{
					// TODO: Implement this method
				}
				
			
		});
	}

	@Override
	public void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		File settingsDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/");
		if (!settingsDir.exists())
		{
			settingsDir.mkdirs();
		}
		try{
			FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/Settings.dat");
			Sensibility = fis.read();
			fis.close();
		}
		catch(Exception e){}
		sensibilitySeekBar.setProgress(Sensibility);
		sensibilityText.setText(String.valueOf(Sensibility));
	}

	@Override
	public void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		File settingsDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/");
		if (!settingsDir.exists())
		{
			settingsDir.mkdirs();
		}
		try{
			FileOutputStream fis = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/Settings.dat");
			fis.write(new byte[]{(byte)Sensibility});
			fis.close();
		}
		catch(Exception e){}
	}
}
