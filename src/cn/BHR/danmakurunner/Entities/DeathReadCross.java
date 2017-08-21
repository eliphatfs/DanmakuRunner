package cn.BHR.danmakurunner.Entities;

import com.badlogic.gdx.graphics.g2d.*;

import cn.BHR.danmakurunner.*;

public class DeathReadCross extends Entity {
	@Override
	public void Init(int _type) {
		scale = 1;
		rotation = 0;
		existTime = 0;
		active = true;
		type = _type;
		setCenter(Runner.plr.getCenter());
		position = position.cpy();
		velocity.set(0, 0);
	}

	@Override
	public void AI() {
		if (active) {
			existTime++;
			if (existTime > 75) {
				active = false;
			}
		}
	}

	@Override
	public void Draw(SpriteBatch batch) {
		if (active) {
			batch.setColor(1, 1, 1, 1 - existTime / 77f);
			DrawHelper.DrawInFightArea(batch, texture, position, scale, rotation);
			batch.setColor(1, 1, 1, 1);
		}
	}

	@Override
	public void Loot() {
		
	}

}
