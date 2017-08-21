package cn.BHR.danmakurunner.UI;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.*;

import cn.BHR.danmakurunner.DrawHelper;
import cn.BHR.danmakurunner.Runner;

public class UIBase
{
	public boolean pressing = false;
	public Texture _tex = null;
	public iEvent onClick = new iEvent();
	public Rectangle drawRect = new Rectangle();
	public void Init()
	{
		_tex = new Texture(new Pixmap(1, 1, Pixmap.Format.RGBA8888));
	}
	public void Init(Rectangle drwRect)
	{
		drawRect = new Rectangle(drwRect);
	}
	public void Draw(SpriteBatch batch)
	{		
		if (_tex == null)
			_tex = new Texture(new Pixmap(1, 1, Pixmap.Format.RGBA8888));
		if (drawRect.width == 0 || drawRect.height == 0)
			batch.draw(_tex, drawRect.x, drawRect.y);
		else DrawHelper.Draw(batch, _tex, drawRect);
		if (drawRect.width == 0) drawRect.width = _tex.getWidth() * Runner.screenSize;
		if (drawRect.height == 0) drawRect.height = _tex.getHeight() * Runner.screenSize;
	}
	public void Draw(SpriteBatch batch, Rectangle newDrawRect)
	{
		drawRect = newDrawRect;
		if (drawRect.width == 0) drawRect.width = _tex.getWidth() * Runner.screenSize;
		if (drawRect.height == 0) drawRect.height = _tex.getHeight() * Runner.screenSize;
		Draw(batch);
	}
	public void Update()
	{
		if (drawRect.contains(Runner.touchPos))
		{
			if (Runner.touching)
				pressing = true;
			else if (pressing)
			{
				pressing = false;
				onClick.happen();
			}
		}
		else pressing = false;
	}
}
