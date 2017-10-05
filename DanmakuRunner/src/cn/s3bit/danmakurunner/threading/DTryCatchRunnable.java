package cn.s3bit.danmakurunner.threading;

/**
 * @author 北海若
 * <p>
 * A runnable with a catch method when meeting an exception.
 * </p>
 */
public interface DTryCatchRunnable {
	/**
	 * Code to run in try block.
	 */
	public abstract void tryDo();
	/**
	 * Code to run when met with an Exception.
	 */
	public abstract void catchDo(Exception e);
}