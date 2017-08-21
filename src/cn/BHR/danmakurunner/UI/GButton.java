package cn.BHR.danmakurunner.UI;
import com.badlogic.gdx.graphics.g2d.*;

import cn.BHR.danmakurunner.MagicPixel;

public class GButton extends UIBase
{
	@Override
	public void Draw(SpriteBatch batch)
	{
		super.Draw(batch);
		if (pressing)
			MagicPixel.DrawShadeGray(batch, drawRect);
	}

	@Override
	public void Update()
	{
		super.Update();
	}
}
