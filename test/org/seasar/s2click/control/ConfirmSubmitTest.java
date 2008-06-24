package org.seasar.s2click.control;

import junit.framework.TestCase;

public class ConfirmSubmitTest extends TestCase {

	/**
	 * コンストラクタで与えたメッセージを用いて確認ダイアログを表示するJavaScriptが生成されること。
	 */
	public void testConfirmSubmitStringStringObjectStringString() {
		ConfirmSubmit submit = new ConfirmSubmit("submit", "送信", this, "onSubmit",
				"送信してよろしいですか？");
		
		assertEquals("return confirm('\\u9001\\u4FE1\\u3057\\u3066\\u3088\\u308D\\u3057\\u3044\\u3067\\u3059\\u304B\\uFF1F');",
				submit.getOnClick());
		assertEquals("return confirm('\\u9001\\u4FE1\\u3057\\u3066\\u3088\\u308D\\u3057\\u3044\\u3067\\u3059\\u304B\\uFF1F');",
				submit.getAttribute("onclick"));
	}

	/**
	 * <code>setConfirmMessage()</code>メソッドで与えたメッセージを用いて
	 * 確認ダイアログを表示するJavaScriptが生成されること。
	 */
	public void testSetConfirmMessage() {
		ConfirmSubmit submit = new ConfirmSubmit("submit", "送信", this, "onSubmit");
		submit.setConfirmMessage("送信してよろしいですか？");

		assertEquals("return confirm('\\u9001\\u4FE1\\u3057\\u3066\\u3088\\u308D\\u3057\\u3044\\u3067\\u3059\\u304B\\uFF1F');",
				submit.getOnClick());
		assertEquals("return confirm('\\u9001\\u4FE1\\u3057\\u3066\\u3088\\u308D\\u3057\\u3044\\u3067\\u3059\\u304B\\uFF1F');",
				submit.getAttribute("onclick"));
	}

}
