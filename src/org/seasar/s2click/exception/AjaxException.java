package org.seasar.s2click.exception;

/**
 * Ajaxの処理でエラーが発生した場合にスローされます。
 * 
 * @author Naoki Takezoe
 */
public class AjaxException extends S2ClickException {

	private static final long serialVersionUID = 1L;

	public AjaxException() {
		super();
	}

	public AjaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public AjaxException(String message) {
		super(message);
	}

	public AjaxException(Throwable cause) {
		super(cause);
	}
	

}
