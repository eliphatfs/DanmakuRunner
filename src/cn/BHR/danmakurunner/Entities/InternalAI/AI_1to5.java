package cn.BHR.danmakurunner.Entities.InternalAI;

import com.badlogic.gdx.math.*;
import cn.BHR.danmakurunner.Entities.*;

public class AI_1to5
{
	private static Vector2 _velocAI1 = new Vector2();
	/*
	 * AI1: 每秒旋转指定角度
	 */
	public static void AI1(Projectile proj, float degrees)
	{
		if (proj.extraStorage[0] < 1) {
			proj.extraStorage[0] = 10;
			proj.extraStorage[1] = proj.velocity.x;
			proj.extraStorage[2] = proj.velocity.y;
		}
		else
		{
			_velocAI1.set(proj.extraStorage[1], proj.extraStorage[2]);
			_velocAI1.rotate(degrees / 90f);
			proj.extraStorage[1] = _velocAI1.x;
			proj.extraStorage[2] = _velocAI1.y;
			proj.velocity.set(proj.extraStorage[1], proj.extraStorage[2]);
			Vector2 radius = proj.getCenter().cpy().sub(proj.createCenter);
			Vector2 norH = radius.cpy().rotate90(1).nor();
			norH.scl((float)Math.tan(degrees / 90 * Math.PI / 180) * radius.len());
			proj.velocity.add(norH);
		}
	}
	/*
	 * AI2: 每秒绕定点旋转指定角度
	 */
	public static void AI2(Projectile proj, float degrees, float x, float y)
	{
		if (proj.extraStorage[0] < 1) {
			proj.extraStorage[0] = 10;
			proj.extraStorage[1] = proj.velocity.x;
			proj.extraStorage[2] = proj.velocity.y;
		}
		else
		{
			_velocAI1.set(proj.extraStorage[1], proj.extraStorage[2]);
			_velocAI1.rotate(degrees / 90f);
			proj.extraStorage[1] = _velocAI1.x;
			proj.extraStorage[2] = _velocAI1.y;
			proj.velocity.set(proj.extraStorage[1], proj.extraStorage[2]);
			Vector2 radius = proj.getCenter().cpy().sub(x, y);
			Vector2 norH = radius.cpy().rotate90(1).nor();
			norH.scl((float)Math.tan(degrees / 90 * Math.PI / 180) * radius.len());
			proj.velocity.add(norH);
		}
	}
}
