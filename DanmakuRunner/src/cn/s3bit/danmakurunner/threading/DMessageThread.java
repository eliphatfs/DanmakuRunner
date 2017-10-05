package cn.s3bit.danmakurunner.threading;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 北海若
 * <p>
 * A {@link DTryCatchThread} wrapped together with a message queue.
 * <br/>
 * Uses {@link java.lang.Runnable} as the message.
 * <br/>
 * The thread will continuously consume the Runnable objects in the queue.
 * </p>
 */
public class DMessageThread {
	private LinkedBlockingQueue<Runnable> mMessageQueue = new LinkedBlockingQueue<Runnable>();
	private DTryCatchThread mThread;
	
	/**
	 * The constructor.
	 *
	 * @param  name
	 *		 The name of the new thread
	 */
	public DMessageThread(final String name) {
		mThread = new DTryCatchThread(new DTryCatchRunnable() {
			public void tryDo() {
				try {
					while (true)
						mMessageQueue.take().run();
				} catch (InterruptedException irq) { // Usually when thread.interrupt() called
					return;
				}
			}
			public void catchDo(Exception e) {
				
			}
		}, name, true);
	}
	
	/**
	 * This method puts a Runnable object into the Message queue.
	 * 
	 * @param  runnable
	 *		 {@link java.lang.Runnable} to be run when the queue is consumed up to it
	 */
	public void queue(Runnable runnable) {
		mMessageQueue.offer(runnable);
		/* Use offer because it does not involve waiting
		 * It can be changed to add since the Linked Queue will never be full
		 *
		 * However if changed to put, a catch InterruptedException block is needed
		 * In fact it is unnecessary because the queue will never be full
		 * So using put is not good here
		 */
	}
	
	/**
	 * Get inner {@link DTryCatchThread}.
	 * @return  The inner DTryCatchThread
	 */
	public DTryCatchThread getTryCatchThread() {
		return mThread;
	}
}