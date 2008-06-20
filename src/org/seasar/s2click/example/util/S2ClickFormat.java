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
	 * �����ɓn���ꂽ�������click.xml�Ŏw�肳�ꂽ�����R�[�h��URL�G���R�[�h���܂��B
	 * 
	 * @param value ������
	 * @return URL�G���R�[�h��̕�����
	 */
	public String url(String value){
		return S2ClickUtils.urlEncode(value);
	}
	
    /**
     * JavaScript�̕�������G�X�P�[�v���܂��B
     *
     * @param value ������
     * @return �G�X�P�[�v���ꂽ������
     */
	public String escapeJavaString(String value){
		return S2ClickUtils.escapeJavaScript(value);
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
	
}
