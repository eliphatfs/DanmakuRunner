package cn.s3bit.danmakurunner.threading;

/**
 * @author 北海若
 * <p>
 * A class that manages a ThreadGroup singleton,
 * which will contain all of the threads in Danmaku Runner,
 * except the UI Thread and Main Thread, which don't origin from inside this app.
 * </p>
 */
public class DSubThreadGroup
{
	private static ThreadGroup mThreadGroup = null;
	private static int mResetCount = 0;
	
	/**
	 * Get the ThreadGroup.
	 */
	public static ThreadGroup getInstance()
	{
		if (mThreadGroup == null)
		{
			mThreadGroup = new ThreadGroup("DSubThreadGroup-" + mResetCount);
			mThreadGroup.setDaemon(true);
		}
		return mThreadGroup;
	}
	
	/**
	 * Reset the ThreadGroup.
	 */
	public static void reset()
	{
		mThreadGroup.interrupt();
		++mResetCount;
		mThreadGroup = null;
	}
}