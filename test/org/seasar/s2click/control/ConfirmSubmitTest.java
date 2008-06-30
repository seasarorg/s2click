package org.seasar.s2click.control;

import junit.framework.TestCase;

public class ConfirmSubmitTest extends TestCase {

	public void testConfirmSubmit1() {
		ConfirmSubmit submit = new ConfirmSubmit();
		assertNull(submit.getName());
		assertNull(submit.getOnClick());
		assertNull(submit.getAttribute("onclick"));
	}
	
	public void testConfirmSubmit2() {
		ConfirmSubmit submit = new ConfirmSubmit("submit");
		assertEquals("submit", submit.getName());
		assertNull(submit.getOnClick());
		assertNull(submit.getAttribute("onclick"));
	}
	
	public void testConfirmSubmit3() {
		ConfirmSubmit submit = new ConfirmSubmit("submit", "���M�{�^��");
		assertEquals("submit", submit.getName());
		assertEquals("���M�{�^��", submit.getLabel());
		assertNull(submit.getOnClick());
		assertNull(submit.getAttribute("onclick"));
	}
	
	public void testConfirmSubmit4() {
		ConfirmSubmit submit = new ConfirmSubmit("submit", this, "onSubmit");
		assertEquals("submit", submit.getName());
		assertNull(submit.getOnClick());
		assertNull(submit.getAttribute("onclick"));
	}
	
	/**
	 * �R���X�g���N�^�ŗ^�������b�Z�[�W��p���Ċm�F�_�C�A���O��\������JavaScript����������邱�ƁB
	 */
	public void testConfirmSubmit5() {
		ConfirmSubmit submit = new ConfirmSubmit("submit", "���M", this, "onSubmit",
				"���M���Ă�낵���ł����H");
		
		assertEquals("return confirm('\\u9001\\u4FE1\\u3057\\u3066\\u3088\\u308D\\u3057\\u3044\\u3067\\u3059\\u304B\\uFF1F');",
				submit.getOnClick());
		assertEquals("return confirm('\\u9001\\u4FE1\\u3057\\u3066\\u3088\\u308D\\u3057\\u3044\\u3067\\u3059\\u304B\\uFF1F');",
				submit.getAttribute("onclick"));
	}

	/**
	 * <code>setConfirmMessage()</code>���\�b�h�ŗ^�������b�Z�[�W��p����
	 * �m�F�_�C�A���O��\������JavaScript����������邱�ƁB
	 */
	public void testSetConfirmMessage() {
		ConfirmSubmit submit = new ConfirmSubmit("submit", "���M", this, "onSubmit");
		submit.setConfirmMessage("���M���Ă�낵���ł����H");

		assertEquals("return confirm('\\u9001\\u4FE1\\u3057\\u3066\\u3088\\u308D\\u3057\\u3044\\u3067\\u3059\\u304B\\uFF1F');",
				submit.getOnClick());
		assertEquals("return confirm('\\u9001\\u4FE1\\u3057\\u3066\\u3088\\u308D\\u3057\\u3044\\u3067\\u3059\\u304B\\uFF1F');",
				submit.getAttribute("onclick"));
	}

}
