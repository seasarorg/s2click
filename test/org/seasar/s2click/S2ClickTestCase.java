package org.seasar.s2click;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.seasar.extension.unit.S2TestCase;

/**
 * 
 * @author Naoki Takezoe
 */
public class S2ClickTestCase extends S2TestCase {
	
	/**
	 * �e�X�g�P�[�X�Ɠ����p�b�P�[�W�ɂ���e�L�X�g�t�@�C����ǂݍ��݁A������Ƃ��ĕԋp���܂��B
	 * �e�L�X�g�t�@�C���̕����R�[�h��UTF-8�ł���K�v������܂��B
	 * �܂��A���s�R�[�h��LF�ɓ��ꂳ��܂��B
	 * 
	 * @param fileName �t�@�C����
	 * @return �t�@�C���̓��e
	 * @throws RuntimeException �t�@�C���̓ǂݍ��݂Ɏ��s�����ꍇ
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
