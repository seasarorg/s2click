package org.seasar.s2click.example.page;

import net.sf.click.Page;

/**
 * ���ʃ��C�A�E�g����������y�[�W�N���X�̒��ۊ��N���X�B
 * 
 * @author Naoki Takezoe
 */
public abstract class LayoutPage extends Page {

	@Override public String getTemplate() {
        return "/layout.htm";
    }
	
}
