package org.seasar.s2click.example.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
}
