package cn.s3bit.danmakurunner;

import cn.s3bit.danmakurunner.backend.DBackend;

/**
 * 
 * @author 北海若
 * <p>
 * A singleton class, containing globals of the app.
 * </p>
 */
public final class GlobalManager {
	private static GlobalManager mInstance = null;
	
	/**
	 * Initialize the GlobalManager with the {@link DBackend}.
	 * 
	 * @param backend  The backend instance.
	 * 
	 * @throws RuntimeException When trying to initialize the GlobalManager twice.
	 */
	public static void initialize(DBackend backend) {
		if (mInstance != null) {
			throw new RuntimeException("Global Manager cannot be initialized twice!");
		}
		mInstance = new GlobalManager(backend);
	}
	
	/**
	 * Get the GlobalManager Instance.
	 * 
	 * @throws RuntimeException When trying to get the GlobalManager before initialization.
	 */
	public static GlobalManager getInstance() {
		if (mInstance == null) {
			throw new RuntimeException("Global Manager getInstance() called before initialize!");
		}
		return mInstance;
	}
	
	private final DBackend mBackend;
	private final DLogger mLogger;
	
	
	private GlobalManager(DBackend backend) {
		mBackend = backend;
		mLogger = new DLogger(backend);
	}

	/**
	 * Get the backend interface of Danmaku Runner
	 * @return {@link DBackend}
	 */
	public DBackend getBackend() {
		return mBackend;
	}

	/**
	 * Get the logger of Danmaku Runner
	 * @return {@link DLogger}
	 */
	public DLogger getmLogger() {
		return mLogger;
	}
	
}
