package cn.BHR.danmakurunner.Entities;
import com.badlogic.gdx.math.*;
import cn.BHR.danmakurunner.Runner;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public abstract class Entity
{
	public int existTime = 0;
	public boolean active = false;
	public Vector2 position = new Vector2();
	public Vector2 velocity = new Vector2();
	public float rotation = 0;
	public Texture texture = Runner.projTextures.get(0);
	public int type = -1;
	public float scale = 1;
	public abstract void Init(int _type);
	public abstract void AI();
	public abstract void Draw(SpriteBatch batch);
	public abstract void Loot();
	public float width()
	{
		return (texture.getWidth()*scale);
	}
	public float height()
	{
		return (texture.getHeight()*scale);
	}
	private Vector2 rot = new Vector2();
	protected Vector2 _center = new Vector2();
	private Vector2 pos = new Vector2();
	public Vector2 getCenter()
	{
		pos.set(position);
		rot.set(texture.getWidth()*scale/2, texture.getHeight()*scale/2);
		rot.rotate(rotation);
		_center.set(pos.x+rot.x, pos.y+rot.y);
		return _center;
	}
	public void setCenter(Vector2 center)
	{
		rot.set(texture.getWidth() * scale /2, texture.getHeight()* scale/2);
		rot.rotate(rotation);
		position.set(center.x-rot.x, center.y-rot.y);
	}
	protected Circle _hitCir = new Circle();
	public Circle hitBox()
	{
		_hitCir.set(getCenter(), Math.min(width(), height())*0.5f);
		return _hitCir;
	}
}
