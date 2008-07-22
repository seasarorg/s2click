package org.seasar.s2click;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.apache.commons.io.IOUtils;
import org.seasar.extension.unit.S2TestCase;

/**
 * 
 * @author Naoki Takezoe
 */
public class S2ClickTestCase extends S2TestCase {
	
	protected Object getField(Object obj, String fieldName){
		try {
			Class<?> clazz = obj.getClass();
			while(clazz != null){
				for(Field field: clazz.getDeclaredFields()){
					if(field.getName().equals(fieldName)){
						field.setAccessible(true);
						return field.get(obj);
					}
				}
				clazz = clazz.getSuperclass();
			}
			throw new RuntimeException("フィールドが存在しません。");
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * テストケースと同じパッケージにあるテキストファイルを読み込み、文字列として返却します。
	 * テキストファイルの文字コードはUTF-8である必要があります。
	 * また、改行コードはLFに統一されます。
	 * 
	 * @param fileName ファイル名
	 * @return ファイルの内容
	 * @throws RuntimeException ファイルの読み込みに失敗した場合
	 */
	protected String load(String fileName){
		try {
			InputStream in = getClass().getResourceAsStream(fileName);
			String text = IOUtils.toString(in, "UTF-8");
			text = text.replaceAll("\r\n", "\n");
			text = text.replaceAll("\r", "\n");
			return text;
		} catch(IOException ex){
			throw new RuntimeException(ex);
		}
	}
	
}
