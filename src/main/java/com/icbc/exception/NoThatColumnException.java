package com.icbc.exception;

/**
 *  当找不到指定的column时，就可能抛出该异常
 * @author yzh
 * @date 2020年5月19日
 */
public class NoThatColumnException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoThatColumnException() {
		super();
	}

	public NoThatColumnException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoThatColumnException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoThatColumnException(String message) {
		super(message);
	}

	public NoThatColumnException(Throwable cause) {
		super(cause);
	}

}
