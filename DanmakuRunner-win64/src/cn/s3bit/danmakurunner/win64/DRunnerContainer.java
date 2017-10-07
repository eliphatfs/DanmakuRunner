package cn.s3bit.danmakurunner.win64;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import cn.s3bit.danmakurunner.runner.DRunner;

/**
 * 
 * @author 北海若
 * <p>
 * Contains a LwjglApplication, which contains a {@link DRunner}.
 * </p>
 */
public class DRunnerContainer {
	/**
	 * The {@link LwjglApplication}, an OpenGL surface full-screen or in a lightweight window.
	 */
	public final LwjglApplication application;
	
	private DRunnerContainer() {
		application = new LwjglApplication(new DRunner(), "Danmaku Runner - Playback", 540, 960);
	}
	
	/**
	 * Factory method.
	 * @return a new {@link DRunnerContainer}
	 */
	public static DRunnerContainer instanciate() {
		DRunnerContainer instance = new DRunnerContainer();
		return instance;
	}
}
