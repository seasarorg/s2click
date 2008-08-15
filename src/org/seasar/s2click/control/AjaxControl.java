package org.seasar.s2click.control;

import java.util.Map;

/**
 * Ajaxを実現するコントロールのインターフェースです。
 * <p>
 * <tt>prototype.js</tt>を使って非同期通信処理を呼び出すコントロールの共通的なメソッド、定数値を定義します。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public interface AjaxControl {
	
	public static final String ON_CREATE = "onCreate";
	
	public static final String ON_COMPLETE = "onComplete";
	
	public static final String ON_EXCEPTION = "onException";
	
	public static final String ON_FAILURE = "onFailure";
	
	public static final String ON_SUCCESS = "onSuccess";
	
//    /** The Prototype resource file names. */
//    public static final String[] PROTOTYPE_RESOURCES = {
//    	"/net/sf/click/extras/control/prototype/prototype.js"
//    };

//    public static final String HTML_IMPORTS =
//        "<script type=\"text/javascript\" src=\"{0}/click/prototype/prototype{1}.js\"></script>\n";
	// TODO Click付属のprototype.jsはバージョンが古い…
    public static final String HTML_IMPORTS =
        "<script type=\"text/javascript\" src=\"{0}/js/prototype.js\"></script>\n";
	
	/**
	 * Ajax.Requestのイベントハンドラを設定します。
	 * 
	 * @param event イベント
	 * @param handler　イベントハンドラ（JavaScriptの関数名）
	 */
	public void addAjaxHandler(String event, String handler);
	
	/**
	 * Ajax.Requestのイベントハンドラを取得します。
	 * 
	 * @return イベント名をキーにJavaScriptの関数名を格納したマップ
	 */
	public Map<String, String> getAjaxHandlers();
	
}
