package org.seasar.s2click;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * S2Click内で使用するユーティリティメソッドを提供します。
 * 
 * @author Naoki Takezoe
 */
public class S2ClickUtils {
	
    /**
     * JavaScriptの文字列をエスケープします。
     *
     * @param value 文字列
     * @return エスケープされた文字列
     */
    public static String escapeJavaScript(String value) {
        if (value == null) {
            return "";
        }
        value = value.replaceAll("\\\\", "\\\\\\\\");
        value = value.replaceAll("'", "\\\\'");
        return value;
    }
    
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
