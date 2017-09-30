package cn.BHR.danmakurunner.Scripting;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;

import cn.BHR.danmakurunner.*;

public class ScriptThread {
	public Timer worker;
	public ArrayBlockingQueue<V8Function> functionsQueue;
	public ArrayBlockingQueue<String> scriptsQueue;
	
	long timeend;
	V8Function nowFunc;
	String nowStr;
	public ScriptThread()
	{
		functionsQueue = new ArrayBlockingQueue<V8Function>(3072);
		scriptsQueue = new ArrayBlockingQueue<String>(3072);
		worker = new Timer();
		//Init
		worker.schedule(new TimerTask() {
			@Override
			public void run() {
				EngineMain.Init();
			}
		}, 0);
		worker.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				timeend = System.currentTimeMillis() + 7;
				try {
					while((System.currentTimeMillis() <= timeend)
							&& ((nowFunc = functionsQueue.poll()) != null))
					{
						try {
							nowFunc.call(null, null);
						} catch (Exception e) {
							DRHelper.ScriptErr(e);
						}
					}
				} catch (Exception e) {
				}
				try {
					while((System.currentTimeMillis() <= timeend)
							&& ((nowStr = scriptsQueue.poll()) != null))
					{
						try {
							EngineMain.v8.executeVoidScript(nowStr);
						} catch (Exception e) {
							DRHelper.ScriptErr(e);
						}
					}
				} catch (Exception e) {
				}
				V8Array pi = EngineMain.v8.executeArrayScript("var tmp = command_pool_integers || []; command_pool_integers = []; tmp;");
				V8Array pd = EngineMain.v8.executeArrayScript("var tmp = command_pool_doubles || []; command_pool_doubles = []; tmp;");
				try {
					ScriptBufferIntepreter.ProcessCommand(pi.getIntegers(0, pi.length()), pd.getDoubles(0, pd.length()));
				} catch (Exception e) {
					DRHelper.ScriptErr(e);
				}
			}
		}, 0, 8);
		RunnerActivity.loadSTL();
	}
}
