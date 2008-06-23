package org.seasar.s2click.util;


import net.arnx.jsonic.JSON;
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
		return S2ClickUtils.urlEncode(value);
	}
	
	/**
	 * Java�I�u�W�F�N�g��JSON�ɕϊ����܂��B
	 * 
	 * @param obj �I�u�W�F�N�g
	 * @return JSON
	 */
	public String json(Object obj){
		return JSON.encode(obj);
	}
	
	/**
	 * �A�����锼�p�X�y�[�X��2�����ڈȍ~��&nbsp;�ɕϊ����܂��B
	 * 
	 * @param value ������
	 * @return �ϊ���̕�����
	 */
	public String nbsp(String value){
		return S2ClickUtils.convertNbsp(value);
	}
	
}
