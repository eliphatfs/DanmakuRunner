package cn.BHR.danmakurunner.Scripting;
import java.io.File;

import com.badlogic.gdx.math.Vector2;
import com.eclipsesource.v8.V8Array;

import android.os.Environment;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import cn.BHR.danmakurunner.*;
import cn.BHR.danmakurunner.Dialogs.ErrorDialog;
import cn.BHR.danmakurunner.Dialogs.Msgbox;

public class DanmakuInterface
{
	public static DanmakuInterface Caller = new DanmakuInterface();
	@JavascriptInterface
	public void toast(Object t)
	{
		Toast.makeText(RunnerActivity.instance, String.valueOf(t), Toast.LENGTH_SHORT).show();
	}
	@JavascriptInterface
	public int NewProj(int id, int type, double positionx, double positiony, double velocityx, double velocityy, double scale)
	{
		return DRHelper.NewProj(id, type,
						 new Vector2((float)positionx, (float)positiony),
						 new Vector2((float)velocityx, (float)velocityy),
						 (float)scale);
	}
	
	/*@JavascriptInterface
	public int NewProjAng(int type, double positionx, double positiony, double angle, double velocity, double scale)
	{
		Vector2 _veloc = new Vector2((float)velocity, 0);
		_veloc.rotate((float)angle);
		return DRHelper.NewProj(type,
						 new Vector2((float)positionx, (float)positiony),
						 _veloc,
						 (float)scale);
	}
	
	@JavascriptInterface
	public int NewProjAngL(int type, double positionx, double positiony, double angle, double velocity, double scale, double scaleL)
	{
		Vector2 _veloc = new Vector2((float)velocity, 0);
		_veloc.rotate((float)angle);
		return DRHelper.NewProjL(type,
						 new Vector2((float)positionx, (float)positiony),
						 _veloc,
						 (float)scale, (float)scaleL);
	}*/
	
	@JavascriptInterface
	public int NewProjL(int id, int type, double positionx, double positiony, double velocityx, double velocityy, double scale, double scaleL)
	{
		return DRHelper.NewProjL(id, type,
									new Vector2((float)positionx, (float)positiony),
									new Vector2((float)velocityx, (float)velocityy),
									(float)scale, (float)scaleL);
	}
	
	public static final int POS_X = 0;
	public static final int POS_Y = 1;
	public static final int VEL_X = 2;
	public static final int VEL_Y = 3;
	public static final int CENTER_X = 4;
	public static final int CENTER_Y = 5;
	public static final int ACCEL_X = 6;
	public static final int ACCEL_Y = 7;
	public static final int VALID = 8;
	public static final int PARENT = 9;
	public static final int POS = 16;
	public static final int VEL = 17;
	public static final int CENTER = 18;
	public static final int ACCEL = 19;
	@JavascriptInterface
	public float GetProj(int id, int type)
	{
		switch (type) {
		case POS_X:
			return (Runner.proj[id].position.x);
			
		case POS_Y:
			return (Runner.proj[id].position.y);
			
		case VEL_X:
			return (Runner.proj[id].velocity.x);
			
		case VEL_Y:
			return (Runner.proj[id].velocity.y);
			
		case CENTER_X:
			return (Runner.proj[id].getCenter().x);
			
		case CENTER_Y:
			return (Runner.proj[id].getCenter().y);
			
		case ACCEL_X:
			return (Runner.proj[id].accel.x);
		
		case ACCEL_Y:
			return (Runner.proj[id].accel.y);
			
		case VALID:
			return (Runner.proj[id].active ? Runner.proj[id].existTime : -1);
			
		default:
			return 0;
		}
	}
	@JavascriptInterface
	public float GetSelf(int type)
	{
		switch (type) {
		case POS_X:
			return (Runner.plr.position.x);
			
		case POS_Y:
			return (Runner.plr.position.y);
			
		case CENTER_X:
			return (Runner.plr.getCenter().x);
		
		case CENTER_Y:
			return (Runner.plr.getCenter().y);

		default:
			return 0;
		}
	}
	@JavascriptInterface
	public void SetProj(int id, int type, double value1d, double value2d)
	{
		float value = (float)value1d;
		float value2 = (float)value2d;
		switch (type) {
		case POS_X:
			Runner.proj[id].position.x = value;
			break;
			
		case POS_Y:
			Runner.proj[id].position.y = value;
			break;
			
		case VEL_X:
			Runner.proj[id].velocity.x = value;
			break;
			
		case VEL_Y:
			Runner.proj[id].velocity.y = value;
			break;
		
		case CENTER_X:
			Runner.proj[id].setCenter(new Vector2(value, Runner.proj[id].getCenter().y));
			break;
			
		case CENTER_Y:
			Runner.proj[id].setCenter(new Vector2(Runner.proj[id].getCenter().x, value));
			break;
			
		case ACCEL_X:
			Runner.proj[id].accel.x = value;
			break;
			
		case ACCEL_Y:
			Runner.proj[id].accel.y = value;
			break;
			
		case POS:
			Runner.proj[id].position.set(value, value2);
			break;
			
		case VEL:
			Runner.proj[id].velocity.set(value, value2);
			break;
		
		case CENTER:
			Runner.proj[id].setCenter(new Vector2(value, value2));
			break;
			
		case ACCEL:
			Runner.proj[id].accel.set(value, value2);
			break;
			
		case VALID:
			Runner.proj[id].judging = (value > 0);
			break;
			
		case PARENT:
			Runner.proj[id].parent = (int)(value+0.001f);
			Runner.proj[Runner.proj[id].parent].childs++;
			break;
		}
	}
	@JavascriptInterface
	public void SetProjAI(int id, int type, double arg0, double arg1, double arg2)
	{
		Runner.proj[id].internalAIType = type;
		Runner.proj[id].ai[0] = (float)arg0;
		Runner.proj[id].ai[1] = (float)arg1;
		Runner.proj[id].ai[2] = (float)arg2;
	}
	private static String textureDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DanmakuRunner/ExtraTextures/";
	private int queries=0;
	@JavascriptInterface
	public void ClearProjs()
	{
		for (int i=0; i<Runner.MAXPROJ; i++)
		{
			if (Runner.proj[i] != null)
				if (Runner.proj[i].active)
					Runner.proj[i].Loot();
		}
	}
	@JavascriptInterface
	public String AddTexture(String filename)
	{
		queries++;
		try {
			if (new File(EditorActivity.projectPath + filename).exists()) {
				Runner.toLoadTexturePool.put(EditorActivity.projectPath + filename);
			}
			else Runner.toLoadTexturePool.put(textureDir + filename);
			//Log.i("TexDir", textureDir + filename);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return String.valueOf(Runner.MAXPROJTYPE + queries - 1);
	}
	@JavascriptInterface
	public void SetSelfTexture(int tindex)
	{
		Runner.plr.type = tindex;
	}
	@JavascriptInterface
	public void Config(int key, int value)
	{
		RunnerActivity.configs.put(key, value);
	}
	@JavascriptInterface
	public void ProjJudgeSize(int key, double value)
	{
		RunnerActivity.ProjJudges.put(key, (float)(value * value));
	}
	Vector2 plrJudgeNow = new Vector2();
	@JavascriptInterface
	public int ShootPlayer(int id, int type, double posx, double posy, double velocity, double scale, double deltaDeg)
	{
		plrJudgeNow.set(Runner.plr.getCenter());
		double angle = plrJudgeNow.sub((float)posx, (float)posy).angle() + deltaDeg;
		Vector2 _veloc = new Vector2((float)velocity, 0);
		_veloc.rotate((float)angle);
		return NewProj(id, type, posx, posy, _veloc.x, _veloc.y, scale);
	}
	@JavascriptInterface
	public int ShootPlayerL(int id, int type, double posx, double posy, double velocity, double sclL, double scale, double deltaDeg)
	{
		plrJudgeNow.set(Runner.plr.getCenter());
		double angle = plrJudgeNow.sub((float)posx, (float)posy).angle() + deltaDeg;
		Vector2 _veloc = new Vector2((float)velocity, 0);
		_veloc.rotate((float)angle);
		return NewProjL(id, type, posx, posy, _veloc.x, _veloc.y, scale, sclL);
	}
	@JavascriptInterface
	public void NewText(Object text)
	{
		Runner.msgPool.append(text.toString());
		Runner.msgPool.append('\n');
	}
	@JavascriptInterface
	public void DelProj(int id)
	{
		Runner.proj[id].Loot();
	}
	@JavascriptInterface
	public void SetProjShader(int id, double r, double g, double b, double a)
	{
		Runner.proj[id].shader.set((float)r, (float)g, (float)b, (float)a);
	}
	@JavascriptInterface
	public void KillPlayer()
	{
		Runner.plr.Loot();
	}
	@JavascriptInterface
	public void SetProjLifeTime(int id, int time)
	{
		Runner.proj[id].lifetime = time;
	}
	public static final int DIE = 1;
	@JavascriptInterface
	public void SetProjEvent(int id, int type, String function)
	{
		switch (type) {
		case DIE:
			Runner.proj[id].LootCallbackFunction = function;
			break;
		}
	}
	static StringBuilder MessageString = new StringBuilder();
	@JavascriptInterface
	public void AddText(Object string)
	{
		Runner.msgPool.append(string.toString());
	}
	@JavascriptInterface
	public void log(Object str)
	{
		MessageString.append(String.valueOf(str)).append('\n');
	}
	@JavascriptInterface
	public void logFlush(String Type)
	{
		if (Type.equals("ERR")) {
			new ErrorDialog(MessageString.toString()).show(RunnerActivity.instance.getFragmentManager(), "ScriptError");
			MessageString.delete(0, MessageString.length());
		}
		else if (Type.equals("MSG")) {
			new Msgbox(MessageString.toString()).show(RunnerActivity.instance.getFragmentManager(), "ScriptLog");
			MessageString.delete(0, MessageString.length());
		}
		else {
			new ErrorDialog("控制台错误：无法解析的显示模式 - " + Type).show(RunnerActivity.instance.getFragmentManager(), "ConsoleError");
		}
	}
	@JavascriptInterface
	public V8Array GetLowLevelInitData()
	{
		V8Array v8Array = new V8Array(EngineMain.v8);
		v8Array.push(Runner.screenWidth);
		v8Array.push(Runner.screenHeight);
		return v8Array;
	}
}
