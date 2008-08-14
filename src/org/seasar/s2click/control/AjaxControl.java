package org.seasar.s2click.control;

import java.util.Map;

/**
 * Ajaxを実現するコントロールのインターフェースです。
 * <p>
 * <tt>prototype.js</tt>を使って非同期通信処理を呼び出すコントロールの共通的なメソッド、定数値を定義します。
 * 
 * @author Naoki Takezoe
 * @since 0.3.1
 */
public interface AjaxControl {
	
	public static final String ON_CREATE = "onCreate";
	
	public static final String ON_COMPLETE = "onComplete";
	
	public static final String ON_EXCEPTION = "onException";
	
	public static final String ON_FAILURE = "onFailure";
	
	public static final String ON_SUCCESS = "onSuccess";
	
	/**
	 * Ajax.Requestのイベントハンドラを設定します。
	 * 
	 * @param event イベント
	 * @param handler　イベントハンドラ（JavaScriptの関数名）
	 */
	public void addAjaxHandler(String event, String handler);
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getAjaxHandlers();
	
}
