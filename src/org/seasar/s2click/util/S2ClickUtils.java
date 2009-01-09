package org.seasar.s2click.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.click.control.Field;
import net.sf.click.control.FileField;
import net.sf.click.control.Form;
import net.sf.click.control.HiddenField;
import net.sf.click.util.ClickUtils;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Copy;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.s2click.S2ClickConfig;
import org.seasar.s2click.control.HiddenList;

/**
 * S2Click内で使用するユーティリティメソッドを提供します。
 * 
 * @author Naoki Takezoe
 */
public class S2ClickUtils {
	
	/**
	 * フォームのコントロールを<code>HiddenField</code>（もしくは<code>HiddenList</code>）に変換します。
	 * 
	 * @param form 変換するフォーム
	 * @throws IllegalArgumentException hiddenに変換できないコントロールがフォームに含まれていた場合
	 */
	public static void convertToHidden(Form form) throws IllegalArgumentException {
		for(Object obj: form.getFieldList().toArray()){
			Field field = (Field) obj;
			
			// もともとhiddenの場合はなにもしない
			if(field.isHidden()){
				continue;
			}
			
			// FileFieldの場合は変換できない
			if(field instanceof FileField){
				throw new IllegalArgumentException("FileFieldはHiddenFieldに変換できません。");
			}
			
			Object value = field.getValueObject();
			if(value == null){
				value = "";
			}
			
			form.remove(field);
			
			if(value instanceof List){
				// 値がListの場合はHiddenListを使用
				HiddenList hidden = new HiddenList(field.getName());
				for(Object valueItem: List.class.cast(value)){
					hidden.addValue(valueItem.toString());
				}
				form.add(hidden);
				
			} else {
				// List以外の場合はHiddenFieldに文字列として格納
				HiddenField hidden = new HiddenField(field.getName(), value.toString());
				form.add(hidden);
			}
		}
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
	
	/**
	 * publicフィールドに対応した{@link ClickUtils#copyObjectToForm(Object, Form, boolean)}です。
	 * 
	 * @param object コピー元のオブジェクト
	 * @param form コピー先のフォーム
	 * @param debug デバッグログを出力するかどうか
	 */
    public static void copyObjectToForm(Object object, Form form,
            boolean debug) {

        if (object == null) {
            throw new IllegalArgumentException("Null object parameter");
        }
        if (form == null) {
            throw new IllegalArgumentException("Null form parameter");
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        new S2ClickCopy(object, map).execute();
        ClickUtils.copyObjectToForm(map, form, debug);
    }
    
    /**
     * publicフィールドに対応した{@link ClickUtils#copyFormToObject(Form, Object, boolean)}です。
     * 
     * @param form コピー元のフォーム
     * @param object コピー先のオブジェクト
     * @param debug デバッグログを出力するかどうか
     */
    @SuppressWarnings("unchecked")
    public static void copyFormToObject(Form form, Object object,
            boolean debug) {
        if (form == null) {
            throw new IllegalArgumentException("Null form parameter");
        }
        if (object == null) {
            throw new IllegalArgumentException("Null object parameter");
        }
    	
        Map map = new HashMap();
        for(Field field: (List<Field>) form.getFieldList()){
        	map.put(field.getName(), "");
        }
        ClickUtils.copyFormToObject(form, map, debug);
        new S2ClickCopy(map, object).execute();
    }
    
    /**
     * 日付にデフォルトのコンバータを適用しない<code>Beans.copy()</code>の拡張。
     */
    private static class S2ClickCopy extends Copy {

    	/**
    	 * コンストラクタ。
    	 * 
    	 * @param src コピー元
    	 * @param dest コピー先
    	 * @throws NullPointerException
    	 */
		public S2ClickCopy(Object src, Object dest) throws NullPointerException {
			super(src, dest);
		}
		
		/**
		 * 常にnullを返すことで日付にデフォルトのコンバータを適用しません。
		 */
		@Override
		protected Converter findDefaultConverter(Class<?> clazz) {
			return null;
		}
    }
    
}
