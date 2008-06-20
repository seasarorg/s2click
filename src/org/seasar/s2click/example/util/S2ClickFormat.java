package org.seasar.s2click.example.util;

import org.seasar.s2click.S2ClickUtils;

import net.arnx.jsonic.JSON;
import net.sf.click.util.Format;

/**
 * 
 * @author Naoki Takezoe
 */
public class S2ClickFormat extends Format {
	
	/**
	 * 引数に渡された文字列をclick.xmlで指定された文字コードでURLエンコードします。
	 * 
	 * @param value 文字列
	 * @return URLエンコード後の文字列
	 */
	public String url(String value){
		return S2ClickUtils.urlEncode(value);
	}
	
    /**
     * JavaScriptの文字列をエスケープします。
     *
     * @param value 文字列
     * @return エスケープされた文字列
     */
	public String escapeJavaString(String value){
		return S2ClickUtils.escapeJavaScript(value);
	}
	
	/**
	 * JavaオブジェクトをJSONに変換します。
	 * 
	 * @param obj オブジェクト
	 * @return JSON
	 */
	public String json(Object obj){
		return JSON.encode(obj);
	}
	
}
