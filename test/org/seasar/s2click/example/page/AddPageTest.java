package org.seasar.s2click.example.page;

import org.seasar.s2click.S2ClickPageTestCase;

public class AddPageTest extends S2ClickPageTestCase<AddPage> {

	public void testDoAdd() {
		page.form.num1.setValueObject(1);
		page.form.num2.setValueObject(2);
		
		assertTrue(page.doAdd());
		assertEquals(3, page.form.result.getInteger().intValue());
	}

}
