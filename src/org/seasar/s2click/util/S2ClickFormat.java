package org.seasar.s2click.util;


import java.util.Date;

import net.arnx.jsonic.JSON;
import net.sf.click.util.Format;
import ognl.Ognl;
import ognl.OgnlException;

/**
 * 
 * @author Naoki Takezoe
 */
public class S2ClickFormat extends Format {
	
	/**
	 * 日付をyyyy/MM/dd形式でフォーマットします。
	 * 
	 * @param date 日付
	 * @return フォーマットされた文字列
	 */
	@Override
	public String date(Date date){
		return date(date, "yyyy/MM/dd");
	}
	
	/**
	 * 日付をyyyy/MM/dd HH:mm:ss形式でフォーマットします。
	 * 
	 * @param date 日付
	 * @return フォーマットされた文字列
	 */
	public String datetime(Date date){
		return date(date, "yyyy/MM/dd HH:mm:ss");
	}
	
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
	
	/**
	 * 引数の文字列をOGNLとして評価し、結果を返却します。
	 * 
	 * @param expression OGNL式
	 * @return OGML式を評価した結果
	 * @throws OgnlException OGNLの評価に失敗した場合
	 */
	public Object ognl(String expression) throws OgnlException {
		return Ognl.getValue(expression, null);
	}
	
	/**
	 * HTMLをエスケープし、改行を&lt;br&gt;タグに変換します。
	 * 
	 * @param value 文字列
	 * @return 変換後の文字列
	 */
	public String br(String value){
		String result = html(value);
		result = result.replaceAll("(\r\n|\r|\n)", "<br>$1");
		return result;
	}
	
}
