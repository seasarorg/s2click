package org.seasar.s2click.exception;

/**
 * 
 * 
 * @author Naoki Takezoe
 */
public class RequestRequiredException extends S2ClickException {

	private static final long serialVersionUID = 1L;
	
	public RequestRequiredException(String fieldName){
		super("必須パラメータ " + fieldName + " が指定されていません。");
	}
	
}
