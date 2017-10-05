package cn.s3bit.danmakurunner.threading;

/**
 * @author 北海若
 * <p>
 * Thread wrapper that has try_catch function.
 * <br />
 * Notice that it is recommended to watch the return value of Thread.interrupted()
 * in the DTryCatchRunnable to avoid unwanted results when interrupted.
 * </p>
 * <p>
 * @see {@link DTryCatchRunnable}
 * </p>
 */
public class DTryCatchThread {
	private Thread mThread = null;
	
	/**
	 * The constructor.
	 * <br />
	 * The effect is same as DTryCatchThread(runnable, name, false).
	 *
	 * @param  name
	 *		 The name of the new thread
	 *
	 * @param  runnable
	 *		 The try_catch to run
	 */
	public DTryCatchThread(final DTryCatchRunnable runnable, final String name) {
		this(runnable, name, false);
	}
	
	/**
	 * The constructor.
	 *
	 * @param  name
	 *		 The name of the new thread
	 *
	 * @param  runnable
	 *		 The try_catch to run
	 *
	 * @param  autoStart
	 *		 If true, the thread will start automatically.
	 */
	public DTryCatchThread(final DTryCatchRunnable runnable, final String name, final boolean autoStart) {
		mThread = new Thread(DSubThreadGroup.getInstance(), new Runnable() {
			@Override
			public void run() {
				try {
					runnable.tryDo();
				} catch (Exception e) {
					runnable.catchDo(e);
				}
			}
		}, name);
		if (autoStart)
			mThread.start();
	}
	
	/**
	 * This method starts the thread.
	 * <br />
	 * Remember that if the thread has already started, it will throw an Exception.
	 * 
	 * @exception  IllegalThreadStateException  if the thread was already
	 *			 started.
	 */
	public void start() {
		mThread.start();
	}
	
	/**
	 * This method interrupts this thread.
	 */
	public void interrupt() {
		mThread.interrupt();
	}
	
	/**
	 * Get the inner {@link java.lang.Thread}.
	 * @return The inner Thread
	 */
	public Thread getThread() {
		return mThread;
	}
}