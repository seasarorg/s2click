package org.seasar.s2click.exception;

public class S2ClickException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public S2ClickException() {
		super();
	}

	public S2ClickException(String message, Throwable cause) {
		super(message, cause);
	}

	public S2ClickException(String message) {
		super(message);
	}

	public S2ClickException(Throwable cause) {
		super(cause);
	}
	
}
