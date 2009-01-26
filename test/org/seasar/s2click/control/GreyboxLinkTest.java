package org.seasar.s2click.control;

import junit.framework.TestCase;
import net.sf.click.Context;
import net.sf.click.MockContext;
import net.sf.click.MockRequest;

import org.seasar.s2click.S2ClickPage;

public class GreyboxLinkTest extends TestCase {

	public void testGetHtmlImports() {
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		
		MockContext.initContext(request);
		GreyboxLink link = new GreyboxLink();
		
		assertEquals("<script type=\"text/javascript\" src=\"/sample/click/greybox/AJS.js\"></script>\n" + 
				"<script type=\"text/javascript\" src=\"/sample/click/greybox/AJS_fx.js\"></script>\n" + 
				"<script type=\"text/javascript\" src=\"/sample/click/greybox/gb_scripts.js\"></script>\n" + 
				"<link href=\"/sample/click/greybox/gb_styles.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\" />\n" + 
				"<script type=\"text/javascript\">function S2Click_GB_SetResult(data, id){ AJS.$(id).value = data; }</script>\n",
				link.getHtmlImports());
	}

	public void testSetPageClass() {
		GreyboxLink link = new GreyboxLink();
		link.setPageClass(S2ClickPage.class);
		
		assertSame(S2ClickPage.class, link.getPageClass());
	}

	public void testSetDialogTitle() {
		GreyboxLink link = new GreyboxLink();
		link.setDialogTitle("タイトル");
		
		assertEquals("タイトル", link.getDialogTitle());
	}

	public void testSetDialogWidth() {
		GreyboxLink link = new GreyboxLink();
		assertEquals(400, link.getDialogWidth());
		
		link.setDialogWidth(600);
		assertEquals(600, link.getDialogWidth());
	}

	public void testSetDialogHeight() {
		GreyboxLink link = new GreyboxLink();
		assertEquals(300, link.getDialogHeight());
		
		link.setDialogHeight(400);
		assertEquals(400, link.getDialogHeight());
	}

	public void testGetHref() {
		GreyboxLink link = new GreyboxLink("link", "リンク");
		assertEquals("#", link.getHref());
	}

	public void testOnProcess() {
		GreyboxLink link = new GreyboxLink();
		assertTrue(link.onProcess());
	}

	public void testSetListener() {
		GreyboxLink link = new GreyboxLink();
		try {
			link.setListener(null, null);
			fail();
		} catch(UnsupportedOperationException ex){
		}
	}

	public void testToString() {
		MockContext.initContext();
		MockContext context = (MockContext) Context.getThreadLocalContext();
		context.setPagePath(S2ClickPage.class, "/test.htm");
		
		GreyboxLink link = new GreyboxLink("link", "リンク", "title", S2ClickPage.class);
		
		assertEquals("<a href=\"#\" title=\"title\" " +
				"onclick=\"GB_showCenter('title', '../../test.htm', 300, 400)\">リンク</a>", 
				link.toString());
	}

}