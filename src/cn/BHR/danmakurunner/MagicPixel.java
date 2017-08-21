package cn.BHR.danmakurunner;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class MagicPixel
{
	private static Texture _tex;
	private static Pixmap _pixmap;
	public static void Init()
	{
		_pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		_tex = new Texture(_pixmap);
	}
	public static void Draw(SpriteBatch batch, Rectangle rect, Color color)
	{
		batch.setColor(1, 1, 1, 1);
		_pixmap.setColor(color);
		_pixmap.fill();
		_tex.draw(_pixmap, 0, 0);
		DrawHelper.Draw(batch, _tex, rect);
		batch.flush();
	}
	public static void DrawShadeGray(SpriteBatch batch, Rectangle rect)
	{
		Draw(batch, rect, new Color(0.2f, 0.2f, 0.2f, 0.401f));
	}
}
