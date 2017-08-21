package cn.BHR.danmakurunner.Scripting;

import com.badlogic.gdx.utils.reflect.*;
import com.eclipsesource.v8.*;
import cn.BHR.danmakurunner.*;
import cn.BHR.danmakurunner.Dialogs.*;
import cn.BHR.danmakurunner.Projecting.ProjectingMain;

public class EngineMain {
	public static V8 v8;
	@SuppressWarnings("rawtypes")
	public static void Init()
	{
		v8 = V8.createV8Runtime();
		try {
			Class dminterface = ClassReflection.forName("cn.BHR.danmakurunner.Scripting.DanmakuInterface");
			DanmakuInterface dm = new DanmakuInterface();
			V8Object V8Interface = new V8Object(v8);
			v8.add("dm", V8Interface);
			
			Method[] methods = ClassReflection.getMethods(dminterface);
			for (Method method : methods) {
				V8Interface.registerJavaMethod(dm, method.getName(), method.getName(), method.getParameterTypes());
			}
			
			methods = ClassReflection.getMethods(ClassReflection.forName("cn.BHR.danmakurunner.Scripting.OtherSTL"));
			OtherSTL stlInstance = new OtherSTL();
			for (Method method : methods) {
				v8.registerJavaMethod(stlInstance, method.getName(), method.getName(), method.getParameterTypes());
			}
			v8.executeVoidScript(RunnerActivity.scriptSTL.toString(), "Danmaku API", 0);
		} catch (Exception e) {
			new ErrorDialog("加载引擎失败%>_<%\n"+e.getMessage()).show(RunnerActivity.instance.getFragmentManager(), "EngineInitErr");
		}
	}
	public static void Run()
	{
		OtherSTL.Intervals.clear();
		OtherSTL.Timeouts.clear();
		OtherSTL.timer.clear();
		OtherSTL.tasks.clear();
		OtherSTL.timer.start();
		if (!EditorActivity.projecting) {
			try {
				v8.executeVoidScript(EditorActivity.codeInside);
			} catch (Exception e) {
				DRHelper.ScriptErr(e);
			}
		}
		else
		{
			ProjectingMain.RunProject();
		}
	}
}
