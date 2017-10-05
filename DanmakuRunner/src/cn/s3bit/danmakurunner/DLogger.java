package cn.s3bit.danmakurunner;

import java.io.PrintWriter;

import cn.s3bit.danmakurunner.backend.DBackend;

/**
 * @author 北海若
 * <p>
 * Log Manager of Danmaku Runner.
 * <br />
 * Writes HTML into the {@link DBackend}'s {@link java.io.OutputStream};
 * </p>
 */
public class DLogger {
	/**
	 * Log Level of Danmaku Runner.
	 * <p>
	 * 4: Full<br />
	 * 3: Errors, Warnings and MessageA <br />
	 * 2: Errors and Warnings <br />
	 * 1: Errors Only <br />
	 * 0: None
	 * </p>
	 */
	protected int logLevel = 4;
	
	protected final DBackend mBackend;
	protected final PrintWriter mPrintWriter;
	
	public DLogger(DBackend backend) {
		mBackend = backend;
		mPrintWriter = new PrintWriter(mBackend.getLogStream());
	}
	
	public void logException(Exception e) {
		if (logLevel > 0) {
			
		}
	}
	
	public void error(String str) {
		if (logLevel > 0) {
			
		}
	}
	
	public void warning(String str) {
		if (logLevel > 1) {
			
		}
	}
	
	public void messageA(String str) {
		if (logLevel > 2) {
			
		}
	}
	
	public void messageB(String str) {
		if (logLevel > 3) {
			
		}
	}

	/**
	 * Get the Log Level of DLogger.
	 * 
	 * @return The Log Level.
	 * 
	 * @see DLogger#logLevel
	 */
	public int getLogLevel() {
		return logLevel;
	}

	/**
	 * Set the Log Level of DLogger.
	 * 
	 * @param logLevel the Log Level to set.
	 * 
	 * @throws IllegalArgumentException When the logLevel is not valid.
	 * 
	 * @see DLogger#logLevel
	 */
	public void setLogLevel(int logLevel) {
		if (logLevel > 4 || logLevel < 0) {
			throw new IllegalArgumentException("Log Level can only be in [0, 4]!");
		}
		this.logLevel = logLevel;
	}
}