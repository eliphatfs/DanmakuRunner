package cn.BHR.danmakurunner.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import cn.BHR.danmakurunner.*;
import cn.BHR.danmakurunner.Entities.InternalAI.AI_1to5;

import com.badlogic.gdx.math.*;

public class Projectile extends Entity
{
	public Vector2 accel = new Vector2();
	public int parent;
	public boolean grazed;
	public boolean judging;
	public boolean hitPlr = false;
	public float[] ai = new float[4];
	public float[] extraStorage = new float[8];
	public int internalAIType = 0;
	public int lifetime;
	public int childs;
	public Vector2 createCenter = new Vector2();
	public Color shader = new Color(1, 1, 1, 1);
	public boolean laserlike = false;
	public float laserlikescale;
	public String LootCallbackFunction;
	public Projectile()
	{
		parent = -1;
		Loot();
	}
	@Override
	public void Init(int _type)
	{
		hitPlr = false;
		grazed = false;
		laserlike = false;
		judging = true;
		//rotation = laserlike ? velocity.angle() : 0;
		type = _type;
		texture = Runner.projTextures.get(type);
	}
	protected Rectangle hitRect = new Rectangle();
	//private Vector2 realVel = new Vector2();
	//private Vector2 mcenter = new Vector2();
	protected Vector2 oldCenter = new Vector2();
	protected void Move()
	{
		if (laserlike)
		{
			if (existTime * velocity.len() < extraStorage[7] * texture.getWidth())
			{
				scale = existTime * velocity.len() / texture.getWidth();
				float w = scale * texture.getWidth();
				float h = laserlikescale * texture.getHeight();
				hitRect.set(0, 0, w, h);
			}
			else{
				if (scale != extraStorage[7])
				{
					scale = extraStorage[7];
					float w = scale * texture.getWidth();
					float h = laserlikescale * texture.getHeight();
					hitRect.set(0, 0, w, h);
				}
				position.add(velocity);
			}
			rotation = velocity.angle();
			
			if (parent == -1) {
				hitRect.setPosition(position.x, position.y);
			}
			else {
				hitRect.setPosition(Runner.proj[parent].position.x + position.x, Runner.proj[parent].position.y + position.y);
			}
			if (existTime > 1)
			{
				float larger = hitRect.width > hitRect.height ? hitRect.width : hitRect.height;
				rect_judgeFightArea.set(hitRect.x + hitRect.width / 2 - larger, hitRect.y + hitRect.height / 2 - larger, larger * 2, larger * 2);
				if (!rect_judgeFightArea.overlaps(Runner.FIGHTAREA))
					this.Loot();
				else if (existTime > 1800)
					this.Loot();
			}
		}
		else
		{
			setCenter(oldCenter.add(velocity));
			hitBox();
			if (parent == -1) {
				hitRect.set(position.x, position.y, 2*_hitCir.radius, 2*_hitCir.radius);
			}
			else hitRect.set(Runner.proj[parent].position.x + position.x, Runner.proj[parent].position.y + position.y, 2*_hitCir.radius, 2*_hitCir.radius);
			if ((!hitRect.overlaps(Runner.FIGHTAREA)) && (childs == 0))
			{
				this.Loot();
			}
		}
	}
	static Vector2 vct_rotatetmp = new Vector2();
	static Rectangle rect_judgeFightArea = new Rectangle();
	protected void Judge()
	{
		if (Runner.plr.active && this.judging)
		{
			if (laserlike)
			{
				vct_rotatetmp.set(Runner.plr.getCenter()).sub(hitRect.x, hitRect.y);
				
				vct_rotatetmp.rotate(-rotation);
				vct_rotatetmp.add(hitRect.x, hitRect.y);
				if (Runner.plr.invincible <=0 && hitRect.contains(vct_rotatetmp))
				{
					Runner.plr.Loot();
					hitPlr = true;
				}
			}
			else {
				float distance;
				if (parent == -1) {
					distance = Runner.plr.getCenter().sub(getCenter()).len2();
				}
				else distance = Runner.plr.getCenter().sub(Runner.proj[parent].position.x + getCenter().x, Runner.proj[parent].position.y + _center.y).len2();
				if (!grazed && distance < 4000)
				{
					grazed = true;
					Runner.plr.graze++;
				}
				if (Runner.plr.invincible <= 0 && distance < _hitCir.radius * _hitCir.radius * 0.6f)
				{
					Runner.plr.Loot();
					hitPlr = true;
				}
			}
		}
	}
	protected void internalAI() {
		switch (internalAIType) {
		case 1:
			AI_1to5.AI1(this, ai[0]);
			break;
		case 2:
			AI_1to5.AI2(this, ai[0], ai[1], ai[2]);
			break;
		default:
			break;
		}
	}
	@Override
	public void AI()
	{
		if (!active) return;
		//realVel.set(velocity);
		//realVel.scl(1.5f);
		
		//mcenter.set(getCenter());
		//setCenter(mcenter.add(velocity));
		velocity.add(accel);
		oldCenter.set(getCenter());
		existTime++;
		if (lifetime > 0) {
			lifetime--;
			if (lifetime == 0) Loot();
		}
		if (existTime <= 1) {
			createCenter.set(getCenter());
			if (laserlike)
			{
				extraStorage[7] = scale;
			}
		}
		this.internalAI();
		this.Move();
		this.Judge();
	}
	public static Vector2 drawPos = new Vector2();
	@Override
	public void Draw(SpriteBatch batch)
	{
		if (!active) return;
		if (hitPlr) batch.setColor(shader.r, 0.5f*shader.g, 0.5f*shader.b, shader.a);
		else batch.setColor(shader);
		if (texture == null)
			texture = Runner.projTextures.get(type);
		if (parent == -1) {
			drawPos.set(position);
		}
		else drawPos.set(Runner.proj[parent].position.x + position.x, Runner.proj[parent].position.y + position.y);
		if (laserlike)
		{
			if (existTime <= 0) return;
			DrawHelper.DrawInFightArea(batch, texture, drawPos, scale, laserlikescale, rotation);
		}
		else DrawHelper.DrawInFightArea(batch, texture, drawPos, scale, rotation);
		//batch.setColor(1, 1, 1, 1);
	}
	public int whoAmI;
	@Override
	public void Loot()
	{
		if (parent > 0)
			Runner.proj[parent].childs--;
		if (LootCallbackFunction != null)
			Runner.scriptThread.scriptsQueue.add(new StringBuilder().append(LootCallbackFunction).append('(').append(whoAmI).append(',').append(System.currentTimeMillis()).append(");").toString());
		if (Runner.initialized)
			Runner.scriptThread.scriptsQueue.add("available_ids.push("+ whoAmI +");");
		for (int i=0; i<4; i++)
			ai[i] = 0;
		for (int i=0; i<8; i++)
			extraStorage[i] = 0;
		accel.set(0, 0);
		shader.set(1, 1, 1, 1);
		internalAIType = 0;
		LootCallbackFunction = null;
		parent = -1;
		childs = 0;
		laserlikescale = 1;
		existTime = 0;
		lifetime = -1;
		rotation = 0;
		active = false;
	}
}
