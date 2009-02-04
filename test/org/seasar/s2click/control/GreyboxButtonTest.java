package org.seasar.s2click.control;

import junit.framework.TestCase;
import net.sf.click.Context;
import net.sf.click.MockContext;
import net.sf.click.MockRequest;

import org.seasar.s2click.S2ClickPage;

public class GreyboxButtonTest extends TestCase {

	public void testGetHtmlImports() {
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		
		MockContext.initContext(request);
		GreyboxButton button = new GreyboxButton();
		
		assertEquals("<script type=\"text/javascript\" src=\"/sample/resources/greybox/AJS.js\"></script>\n" + 
				"<script type=\"text/javascript\" src=\"/sample/resources/greybox/AJS_fx.js\"></script>\n" + 
				"<script type=\"text/javascript\" src=\"/sample/resources/greybox/gb_scripts.js\"></script>\n" + 
				"<link href=\"/sample/resources/greybox/gb_styles.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\" />\n" + 
				"<script type=\"text/javascript\">function S2Click_GB_SetResult(data, id){ AJS.$(id).value = data; }</script>\n",
				button.getHtmlImports());
	}

	public void testSetPageClass() {
		GreyboxButton button = new GreyboxButton();
		button.setPageClass(S2ClickPage.class);
		
		assertSame(S2ClickPage.class, button.getPageClass());
	}

	public void testSetDialogTitle() {
		GreyboxButton button = new GreyboxButton();
		button.setDialogTitle("タイトル");
		
		assertEquals("タイトル", button.getDialogTitle());
	}

	public void testSetDialogWidth() {
		GreyboxButton button = new GreyboxButton();
		assertEquals(400, button.getDialogWidth());
		
		button.setDialogWidth(600);
		assertEquals(600, button.getDialogWidth());
	}

	public void testSetDialogHeight() {
		GreyboxButton button = new GreyboxButton();
		assertEquals(300, button.getDialogHeight());
		
		button.setDialogHeight(400);
		assertEquals(400, button.getDialogHeight());
	}

	public void testToString() {
		MockContext.initContext();
		MockContext context = (MockContext) Context.getThreadLocalContext();
		context.setPagePath(S2ClickPage.class, "/test.htm");
		
		GreyboxButton button = new GreyboxButton("button", "ボタン", "title", S2ClickPage.class);
		
		assertEquals("<input type=\"button\" name=\"button\" id=\"button\" " + 
				"value=\"ボタン\" onclick=\"GB_showCenter('title', '../../test.htm', 300, 400)\"/>", 
				button.toString());
	}

}
