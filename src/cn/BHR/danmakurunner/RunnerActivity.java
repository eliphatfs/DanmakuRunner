package cn.BHR.danmakurunner;

import android.annotation.SuppressLint;
import android.os.*;
import android.util.*;
import cn.BHR.danmakurunner.Dialogs.*;
import com.badlogic.gdx.backends.android.*;

import java.io.*;
import com.badlogic.gdx.*;

public class RunnerActivity extends AndroidApplication {
	public static boolean loadedSTL = false;
	public static StringBuilder scriptSTL = new StringBuilder(1024);
	public static Runner runner = new Runner();
	public static RunnerActivity instance;
	
	public static final int CONFIG_SELF_ACTIVE = 64;
	public static final int CONFIG_SENSIBILITY = 65;
	public static final int CONFIG_FIGHTAREA = 66;
	public static SparseIntArray configs = new SparseIntArray();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        //hdl = new RunCodeHandler();
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        
		runner = new Runner();
		setContentView(initializeForView(runner, cfg));
		//loadSTL();
    }
	
	@SuppressLint("SetJavaScriptEnabled")
	public static void loadSTL()
	{
		if (!loadedSTL)
		{
			InputStream stlstream = Gdx.files.internal("scriptSTL.js").read();
			try
			{
				while (!(loadedSTL = !(stlstream.available() > 0)))
					scriptSTL.append((char)stlstream.read());
			}
			catch (IOException e)
			{
				new ErrorDialog("加载标准库失败%>_<%\n" + e.getLocalizedMessage());
			}
		}
	}
    
}

