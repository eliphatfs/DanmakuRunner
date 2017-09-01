package cn.BHR.danmakurunner;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import android.annotation.SuppressLint;
import cn.BHR.danmakurunner.Entities.*;
import cn.BHR.danmakurunner.Scripting.*;
import cn.BHR.danmakurunner.Scripting.MarkupClassDefs.*;

import com.badlogic.gdx.utils.Align;

public class Runner implements ApplicationListener
{
	static SpriteBatch batch;
	public static int absTicks = 0;
	public static final int MAXPROJTYPE = 234;
	public static final int MAXPROJ = 1792;
	public static ArrayList<Texture> projTextures = new ArrayList<Texture>();
	public static Rectangle FIGHTAREA = new Rectangle(0, 0, 540, 540);
	public static Rectangle MAINRECT;
	public static Projectile[] proj = new Projectile[MAXPROJ];
	public static int screenWidth;
	public static int screenHeight;
	public static float screenSize;
	public static Timer updateTimer;
	public static boolean touching = false;
	public static Vector2 touchPos = new Vector2();
	public static Vector2 oldtouchPos = new Vector2();
	public static BitmapFont fontArial;
	public static boolean initialized;
	public static Player plr;
	public static StringBuilder msgPool = new StringBuilder();
	public static ArrayList<Integer> DeathTimes = new ArrayList<Integer>();
	public static DeathReadCross deathReadCross;
	public static LinkedBlockingQueue<String> toLoadTexturePool = new LinkedBlockingQueue<String>();
	public static ScriptThread scriptThread;
	@Override
	public void create()
	{
		initialized = false;
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		screenSize = screenWidth / 540.0f;
		MAINRECT = new Rectangle(0, screenHeight - screenWidth, screenWidth, screenWidth);
		batch = new SpriteBatch(4000);
		fontArial = new BitmapFont();
		projTextures.clear();
		for (int i = 0; i < MAXPROJTYPE; i++)
			projTextures.add(new Texture(Gdx.files.internal("proj"+i+".png")));
		deathReadCross = new DeathReadCross();
		deathReadCross.texture = new Texture(Gdx.files.internal("DeathRedCross.png"));
		reset();
		
		updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				deathReadCross.AI();
				GameObject.UpdateAll();
				for (int i=0; i<MAXPROJ; i++) proj[i].AI();
				plr.AI();
				absTicks++;
			}
		}, 0, 1000/90);
		//Test();
		MagicPixel.Init();
		scriptThread = new ScriptThread();
		scriptThread.worker.schedule(new TimerTask() {
			@Override
			public void run() {
	    		EngineMain.Run();
			}
		}, 0);
		initialized = true;
	}
	@SuppressLint("SdCardPath")
	static void Test()
	{
		StringBuffer testMarkup = new StringBuffer();
		try {
			FileInputStream fileInputStream = new FileInputStream("/sdcard/Download/testMarkup.dmm");
			while (fileInputStream.available() > 0) {
				testMarkup.append((char)fileInputStream.read());
			}
			fileInputStream.close();
			GameObject.markupIntepreter.Run(testMarkup.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//markupIntepreter.Run(MarkupIntepreter.toParse);
		/*final Bullet go = Bullet.Malloc();
		//go.Init(3);
		Emitter emitter = new Emitter();
		emitter.Ways=5;
		emitter.Direction=0;
		emitter.Cycle=10;
		emitter.BeginTick=100;
		emitter.Range=360;
		emitter.Radius=70;
		emitter.Factory = new Factory() {
			@Override
			public GameObject Create() {
				Bullet newObj = Bullet.Malloc();
				//newObj.Init(0);
				newObj.Speed.set(2, 0);
				//newObj.scale = 0.5f;
				newObj.Died = false;
				go.Speed.set(0,0);
				return newObj;
			}
		};
		go.Components.add(emitter);
		go.Speed.set(2, 2);
		go.Died = false;*/
	}
	static StringBuilder builder = new StringBuilder();
	static Vector2 plrNewPos = new Vector2();
	@Override
	public void render()
	{   
	    Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    int fightsize = RunnerActivity.configs.get(RunnerActivity.CONFIG_FIGHTAREA, 540);
	    fightsize = fightsize < 540 ? 540 : fightsize;
	    FIGHTAREA.set(270 - fightsize / 2, 270 - fightsize / 2, fightsize, fightsize);
	    if (Gdx.input.isTouched())
	    {
	    	touchPos.set(Gdx.input.getX(), screenHeight - Gdx.input.getY());
	    	if (!touching)
				oldtouchPos.set(touchPos);
	    	touching = true;
			plrNewPos.set(plr.position).add(plr.width() / 2, plr.height() / 2);
			plrNewPos.add(Gdx.input.getDeltaX(0) * SettingsActivity.Sensibility / 30, -Gdx.input.getDeltaY(0) * SettingsActivity.Sensibility / 30);
			if (plrNewPos.x <= 0) plrNewPos.x = 1;
			else if (plrNewPos.x >= 540) plrNewPos.x = 539;
			if (plrNewPos.y <= 0) plrNewPos.y = 1;
			else if (plrNewPos.y >= 540) plrNewPos.y = 539;
			plr.position.set(plrNewPos.sub(plr.width() / 2, plr.height() / 2));
	    }
	    else touching = false;
		batch.begin();
		String toLoad;
		while(!toLoadTexturePool.isEmpty())
		{
			toLoad = toLoadTexturePool.poll();
			projTextures.add(new Texture(Gdx.files.absolute(toLoad)));
		}
		deathReadCross.Draw(batch);
		plr.Draw(batch);
		for (int i=0; i<MAXPROJ; i++)
		{
			proj[i].Draw(batch);
		}
		GameObject.DrawAll(batch);
		MagicPixel.Draw(batch, new Rectangle(0, 0, screenWidth, screenHeight - screenWidth), Color.DARK_GRAY);
		builder.delete(0, builder.length());
		builder.append("Time: ").append(absTicks);
		builder.append(" Graze: ").append(plr.graze);
		builder.append("\n");
		builder.append(Gdx.graphics.getFramesPerSecond());
		builder.append("fps Died ");
		builder.append(DeathTimes.size());
		builder.append(" times at: ");
		for (int i=0; i<DeathTimes.size(); i++)
		{
			builder.append(DeathTimes.get(i));
			builder.append(" ");
		}
		fontArial.draw(batch, builder.toString(), 40, 80);
		float hgt;
		if ((hgt = fontArial.draw(batch, msgPool.toString(), 40, MAINRECT.getY() - 40, screenWidth - 80, Align.topLeft, true).height) > screenHeight - screenWidth - 140) {
			
			for (int i=0; i<hgt - (screenHeight - screenWidth - 140); i+=fontArial.getLineHeight())
				msgPool.delete(0, msgPool.indexOf("\n")+1);
		}
		batch.end();
		oldtouchPos.set(touchPos);
	}
	@Override
	public void dispose()
	{
		scriptThread.worker.cancel();
		updateTimer.cancel();
	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
		
	}
	
	public void reset()
	{
		DeathTimes.clear();
		msgPool.delete(0, msgPool.length());
		absTicks = 0;
		for (int i=0; i<MAXPROJ; i++)
		{
			proj[i] = new Projectile();
			proj[i].active = false;
		}
		GameObject.ResetAll();
		GameObject.markupIntepreter.FirstLayerObjects.clear();
		GameObject.markupIntepreter.BulletTypes.clear();
		GameObject.markupIntepreter.Emitters.clear();
		plr = new Player();
		plr.Init(-1);
	}
}
