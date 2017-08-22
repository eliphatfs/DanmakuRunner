package cn.BHR.danmakurunner.Scripting;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.eclipsesource.v8.*;

import cn.BHR.danmakurunner.Runner;

public class OtherSTL {
	static ArrayList<V8Function> Intervals = new ArrayList<V8Function>();
	static ArrayList<V8Function> Timeouts = new ArrayList<V8Function>();
	static ArrayList<Timer.Task> tasks = new ArrayList<Timer.Task>();
	public static ArrayList<Timer.Task> willClearTasks = new ArrayList<Timer.Task>();
	public static boolean WillClear = false;
	
	public static Timer timer = new Timer();
	static
	{
		timer.start();
	}
	public int setInterval(final V8Function function, double milliseconds)
	{
		final int id = Intervals.size();
		Intervals.add(function.twin());
		int tid = tasks.size();
		tasks.add(new Task() {
			@Override
			public void run() {
				Runner.scriptThread.functionsQueue.add(Intervals.get(id));
			}
		});
		if (WillClear) {
			willClearTasks.add(tasks.get(tid));
		}
		timer.scheduleTask(tasks.get(tid), 0, (float)(milliseconds / 1000));
		return tid;
	}
	public void setIntervalEx(final V8Function function, double timeout, double milliseconds, double timelast)
	{
		final int id = Intervals.size();
		Intervals.add(function.twin());
		final int tid = tasks.size();
		tasks.add(new Task() {
			@Override
			public void run() {
				Runner.scriptThread.functionsQueue.add(Intervals.get(id));
			}
		});
		if (WillClear) {
			willClearTasks.add(tasks.get(tid));
		}
		timer.scheduleTask(tasks.get(tid), (float)(timeout / 1000), (float)(milliseconds / 1000));
		timer.scheduleTask(new Task() {
			@Override
			public void run() {
				tasks.get(tid).cancel();
			}
		}, (float)((timeout + timelast) / 1000));
	}
	public void clearInterval(int tid)
	{
		tasks.get(tid).cancel();
	}
	public void clearTimeout(int tid)
	{
		tasks.get(tid).cancel();
	}
	public int setTimeout(V8Function function, double milliseconds)
	{
		final int id = Timeouts.size();
		Timeouts.add(function.twin());
		int tid = tasks.size();
		tasks.add(new Task() {
			@Override
			public void run() {
				Runner.scriptThread.functionsQueue.add(Timeouts.get(id));
			}
		});
		if (WillClear) {
			willClearTasks.add(tasks.get(tid));
		}
		timer.scheduleTask(tasks.get(tid), (float)(milliseconds / 1000));
		return tid;
	}
}
