package cn.s3bit.danmakurunner.scripting;

import com.eclipsesource.v8.V8;

/**
 * 
 * @author 北海若
 * <p>
 * The Danmaku Runner Script engine, based on V8.
 * </p>
 */
public class DRSEngine {
	/**
	 * Factory method
	 * @return A new DRSEngine
	 */
	public static DRSEngine create() {
		DRSEngine engine = new DRSEngine();
		engine.initialize();
		return engine;
	}
	
	public final V8 v8;
	
	private DRSEngine() {
		v8 = V8.createV8Runtime();
	}
	
	protected void initialize() {
		
	}
}
