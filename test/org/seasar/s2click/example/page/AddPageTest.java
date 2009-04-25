package org.seasar.s2click.example.page;

import org.apache.click.ActionListener;
import org.seasar.s2click.test.S2ClickPageTestCase;

public class AddPageTest extends S2ClickPageTestCase<AddPage> {

	public void testAddPage(){
		page.form.num1.setValueObject(1);
		page.form.num2.setValueObject(2);
		
		ActionListener listener = page.form.submit.getActionListener();
		listener.onAction(page.form.submit);
		
		assertEquals(3, page.form.result.getInteger().intValue());
	}

}
