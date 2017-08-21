package cn.BHR.danmakurunner;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class DrawHelper
{
	public static void Draw(SpriteBatch batch, Texture tex, Rectangle rect)
	{
		if (rect.width == 0) rect.width = tex.getWidth() * Runner.screenSize;
		if (rect.height == 0) rect.height = tex.getHeight() * Runner.screenSize;
		batch.draw(tex, rect.x, rect.y, rect.width, rect.height);
	}
	public static void Draw(SpriteBatch batch, TextureRegion tex, Rectangle rect)
	{
		if (rect.width == 0) rect.width = tex.getRegionWidth() * Runner.screenSize;
		if (rect.height == 0) rect.height = tex.getRegionHeight() * Runner.screenSize;
		batch.draw(tex, rect.x, rect.y, rect.width, rect.height);
	}
	public static void Draw(SpriteBatch batch, Texture tex, Rectangle drawRect, Rectangle srcRectangle)
	{
		batch.draw(tex, drawRect.x, drawRect.y, drawRect.width, drawRect.height, (int)srcRectangle.x, (int)srcRectangle.y, (int)srcRectangle.width, (int)srcRectangle.height, false, false);
	}
	public static void Draw(SpriteBatch batch, Texture tex, Rectangle drawRect, float srcX, float srcY, float srcW, float srcH)
	{
		Draw(batch, tex, drawRect, new Rectangle(srcX, srcY, srcW, srcH));
	}
	static Vector2 pos = new Vector2();
	public static void DrawInFightArea(SpriteBatch batch, Texture tex, Vector2 position, float scale, float rotation)
	{
		pos.x = position.x * Runner.screenSize;
		pos.y = position.y * Runner.screenSize + Runner.MAINRECT.y;
		batch.draw(tex, pos.x, pos.y, 0, 0, tex.getWidth()*scale*Runner.screenSize, tex.getHeight()*scale*Runner.screenSize, 1, 1, rotation, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
	}
	public static void DrawInFightArea(SpriteBatch batch, Texture tex, Vector2 position, float scaleX, float scaleY, float rotation)
	{
		pos.x = position.x * Runner.screenSize;
		pos.y = position.y * Runner.screenSize + Runner.MAINRECT.y;
		batch.draw(tex, pos.x, pos.y, 0, 0, tex.getWidth()*scaleX*Runner.screenSize, tex.getHeight()*scaleY*Runner.screenSize, 1, 1, rotation, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
	}
	public static void DrawInFightAreaCentered(SpriteBatch batch, Texture tex, Vector2 position, float scaleX, float scaleY, float rotation)
	{
		pos.x = position.x * Runner.screenSize;
		pos.y = position.y * Runner.screenSize + Runner.MAINRECT.y;
		batch.draw(tex, pos.x, pos.y, tex.getWidth() / 2, tex.getHeight() / 2, tex.getWidth(), tex.getHeight(), scaleX*Runner.screenSize, scaleY*Runner.screenSize, rotation, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
	}
}
