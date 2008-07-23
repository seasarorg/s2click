package org.seasar.s2click.util;


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
	 * JavaオブジェクトをJSONに変換します。
	 * 
	 * @param obj オブジェクト
	 * @return JSON
	 */
	public String json(Object obj){
		return JSON.encode(obj);
	}
	
	/**
	 * 連続する半角スペースの2文字目以降を&nbsp;に変換します。
	 * 
	 * @param value 文字列
	 * @return 変換後の文字列
	 */
	public String nbsp(String value){
		return S2ClickUtils.convertNbsp(value);
	}
	
}