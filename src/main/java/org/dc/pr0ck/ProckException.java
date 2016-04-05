package org.dc.pr0ck;

public class ProckException extends Exception {

	private static final long serialVersionUID = 6690263171751616402L;

	public ProckException() { }

	public ProckException(String message) {
		super(message);
	}

	public ProckException(Throwable cause) {
		super(cause);
	}

	public ProckException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
