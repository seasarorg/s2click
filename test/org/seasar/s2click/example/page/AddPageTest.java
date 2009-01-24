package org.seasar.s2click.example.page;

import junit.framework.TestCase;

public class AddPageTest extends TestCase {

	public void testDoAdd() {
		AddPage page = new AddPage();
		page.form.num1.setInteger(10);
		page.form.num2.setInteger(20);
		
		page.doAdd();
		
		assertEquals(30, page.form.result.getInteger().intValue());
	}

}
