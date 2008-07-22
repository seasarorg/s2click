package org.seasar.s2click.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.s2click.S2ClickConfig;

/**
 * S2Click内で使用するユーティリティメソッドを提供します。
 * 
 * @author Naoki Takezoe
 */
public class S2ClickUtils {
	
	/**
	 * 引数に渡された文字列を<tt>s2click.dicon</tt>で指定された文字コードでURLエンコードします。
	 * 
	 * @param value 文字列
	 * @return URLエンコード後の文字列
	 */
	public static String urlEncode(String value){
		try {
			S2ClickConfig config = getComponent(S2ClickConfig.class);
			return URLEncoder.encode(value, config.charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 連続する半角スペースの2文字目以降をを&nbspに変換します。
	 * 
	 * @param value 文字列
	 * @return 変換後の文字列
	 */
	public static String convertNbsp(String value){
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for(int i=0;i<value.length();i++){
			char c = value.charAt(i);
			if(c == ' '){
				if(flag){
					sb.append("&nbsp;");
				} else {
					sb.append(c);
					flag = true;
				}
			} else {
				sb.append(c);
				flag = false;
			}
		}
		return sb.toString();
	}

	/**
	 * <code>SingletonS2ContainerFactory</code>からコンポーネントを取得します。
	 * 
	 * @param <T> 取得するコンポーネントの型
	 * @param clazz 取得するコンポーネントの型
	 * @return コンポーネント
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getComponent(Class<T> clazz){
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (T) container.getComponent(clazz);
	}
	
}
