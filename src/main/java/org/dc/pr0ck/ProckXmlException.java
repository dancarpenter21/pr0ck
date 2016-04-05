package org.dc.pr0ck;

public class ProckXmlException extends ProckException {

	private static final long serialVersionUID = 1L;

	public ProckXmlException() { }

	public ProckXmlException(String message) {
		super(message);
	}

	public ProckXmlException(Throwable cause) {
		super(cause);
	}

	public ProckXmlException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProckXmlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
