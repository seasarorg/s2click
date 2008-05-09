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
	 * �����ɓn���ꂽ�������click.xml�Ŏw�肳�ꂽ�����R�[�h��URL�G���R�[�h���܂��B
	 * 
	 * @param value ������
	 * @return URL�G���R�[�h��̕�����
	 */
	public String url(String value){
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
}
