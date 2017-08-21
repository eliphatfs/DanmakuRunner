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
					ProcessCommand(pi.getIntegers(0, pi.length()), pd.getDoubles(0, pd.length()));
				} catch (Exception e) {
				}
			}
		}, 0, 1000/135);
		RunnerActivity.loadSTL();
	}
	public static final int NEWPROJ = 1;
	public static final int NEWPROJL = 2;
	public static final int SHOOTPLR = 3;
	public static final int SHOOTPLRL = 4;
	public static final int SET = 16;
	public static final int SETO = 17;
	
	public static final int AI = 1;
	public static final int LIFE = 2;
	public static final int SHADER = 3;
	public static void ProcessCommand(int[] poolI, double[] poolD)
	{
		int nowptI = 0;
		int nowptD = 0;
		while (nowptI < poolI.length) {
			switch (poolI[nowptI++]) {
			case NEWPROJ:
				DanmakuInterface.Caller.NewProj(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case NEWPROJL:
				DanmakuInterface.Caller.NewProjL(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case SHOOTPLR:
				DanmakuInterface.Caller.ShootPlayer(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case SHOOTPLRL:
				DanmakuInterface.Caller.ShootPlayerL(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case SET:
				DanmakuInterface.Caller.SetProj(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case SETO:
				switch (poolI[nowptI++]) {
				case AI:
					DanmakuInterface.Caller.SetProjAI(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
					break;
				case LIFE:
					DanmakuInterface.Caller.SetProjLifeTime(poolI[nowptI++], poolI[nowptI++]);
					break;
				case SHADER:
					DanmakuInterface.Caller.SetProjShader(poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}
		}
	}
}
