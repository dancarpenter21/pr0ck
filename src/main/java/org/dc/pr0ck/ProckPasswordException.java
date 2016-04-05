package org.dc.pr0ck;

public class ProckPasswordException extends ProckException {
	private static final long serialVersionUID = 119949422010830533L;

	public ProckPasswordException() {
	}

	public ProckPasswordException(String message) {
		super(message);
	}

	public ProckPasswordException(Throwable cause) {
		super(cause);
	}

	public ProckPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProckPasswordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
