package org.seasar.s2click.exception;

/**
 * 
 * @author Naoki Takezoe
 */
public class RequestConversionException extends S2ClickException {

	private static final long serialVersionUID = 1L;

	public RequestConversionException(Throwable cause){
		super("リクエストパラメータの型変換に失敗しました。", cause);
	}
	
}
