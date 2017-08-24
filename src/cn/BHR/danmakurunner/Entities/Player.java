package cn.BHR.danmakurunner.Entities;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.*;
import cn.BHR.danmakurunner.*;

public class Player extends Entity
{
	public int beforeDeath = -1;
	public int power = 0;
	public int graze = 0;
	public int lives = 0;
	public int spell = 0;
	public int invincible = 0;
	public int invincibleBlinkOpacity = 0;
	PlayerAnimation animation;
	@Override
	public void Init(int _type)
	{
		beforeDeath = -1;
		graze = 0;
		spell = 3;
		power = 100;
		active = true;
		existTime = 0;
		invincible = 90;
		invincibleBlinkOpacity = 0;
		scale = 0.6f;
		rotation = 0;
		type = _type;
		animation = new PlayerAnimation();
		texture = animation.GetCurrentTexture();
		setCenter(new Vector2(270, 80));
		// TODO: Implement this method
	}

	@Override
	public void AI()
	{
		if (!active) return;
		existTime++;
		if (RunnerActivity.configs.get(RunnerActivity.CONFIG_SELF_ACTIVE, 1) == 0) {
			active = false;
		}
		invincibleBlinkOpacity = (int)(Math.abs(Math.sin(existTime / 9f)) * 170 + 84);
		if (invincible > 0)
		{
			invincible--;
		}
		else invincibleBlinkOpacity = 255;
		/*if (beforeDeath > 0)
		{
			beforeDeath--;
			if (beforeDeath == 0) Loot();
			return;
		}*/
		
		
		/*if (existTime % 6 == 2)
			DRHelper.NewProj(1, getCenter().add(new Vector2(-5, 20)), new Vector2(0, 14), 1, 0);
		int autoPathTime = 3 * (11 - (int)Math.log(power - 145));
		if (existTime % autoPathTime == 1 && power >= 149)
			DRHelper.NewProj(2, getCenter(), new Vector2(0, 8), 1, 0);*/
		//setCenter(new Vector2(270, 50));
		// TODO: Implement this method
	}
	Vector2 newPos = new Vector2();
	@Override
	public void Draw(SpriteBatch batch)
	{
		if (!active) return;
		if (texture == null)
			texture = animation.GetCurrentTexture();
		//float scale = RunnerActivity.configs.get(RunnerActivity.CONFIG_SENSIBILITY, 100) / 100f;
		
		if (type > -1) {
			texture = Runner.projTextures.get(type);
		}
		else {
			animation.Update();
			texture = animation.GetCurrentTexture();
		}
		batch.setColor(1, 1, 1, invincibleBlinkOpacity / 255f);
		DrawHelper.DrawInFightArea(batch, texture, position, scale, rotation);
		batch.setColor(new Color(1, 1, 1, 1));
		//DrawHelper.DrawInFightArea(batch, Textures.button_start, getCenter(), scale, rotation);
		// TODO: Implement this method
	}

	@Override
	public void Loot()
	{
		invincible = 90;
		Runner.DeathTimes.add(Runner.absTicks);
		Runner.deathReadCross.Init(0);
		// TODO: Implement this method
	}
	
	public static class PlayerAnimation
	{
		Texture[] textures = new Texture[8];
		int stat = 0;
		PlayerAnimation()
		{
			for (int i=0; i<textures.length; i++)
			{
				Pixmap pixmap = new Pixmap(64, 64, Format.RGBA8888);
				pixmap.setColor(0, 0, 1, 1);
				pixmap.fillCircle(32, 32, 10);
				pixmap.setColor(1, 1, 1, 1);
				pixmap.fillCircle(32, 32, 5);
				pixmap.setColor(1, 1, 1, 0.5f);
				pixmap.drawCircle(32, 32, (int)(i * 3f) + 7);
				pixmap.setColor(1, 1, 1, 1f);
				pixmap.drawCircle(32, 32, (int)(i * 3f) + 8);
				pixmap.setColor(1, 1, 1, 0.5f);
				pixmap.drawCircle(32, 32, (int)(i * 3f) + 9);
				textures[i] = new Texture(pixmap);
				pixmap.dispose();
			}
		}
		Texture GetCurrentTexture()
		{
			return textures[stat % textures.length];
		}
		void Update()
		{
			stat++;
		}
	}
}
