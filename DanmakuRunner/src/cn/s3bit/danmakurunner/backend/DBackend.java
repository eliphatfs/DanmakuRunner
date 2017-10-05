package cn.s3bit.danmakurunner.backend;

import java.io.OutputStream;

/**
 * 
 * @author 北海若
 * <p>
 * Backend interface of Danmaku Runner.
 * </p>
 */
public interface DBackend {
	/**
	 * Get a {@link OutputStream} to which the logger writes Logs.
	 */
	public OutputStream getLogStream();
}
