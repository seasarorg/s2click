package org.seasar.s2click.page;

import junit.framework.TestCase;
import net.sf.click.MockContext;

public class AbstractJSONPageTest extends TestCase {

//	public void testOnRender() {
//		MockContext.initContext();
//		JSONPage page = new JSONPage();
//		page.setContents("data");
//		page.onRender();
//	}

	public void testSetContents1() {
		JSONPage page = new JSONPage();
		page.setContents("data");
		assertEquals("data", page.contents);
	}

	public void testSetContents2() {
		JSONPage page = new JSONPage();
		try {
			page.setContents(null);
			fail();
		} catch(IllegalArgumentException ex){
			assertEquals("contents shouldn't be null.", ex.getMessage());
		}
	}
	
	public void testSetContentType1() {
		MockContext.initContext();
		
		JSONPage page = new JSONPage();
		assertEquals("application/x-javascript; charset=utf-8", page.getContentType());
		
		page.setContentType("application/octet-stream");
		assertEquals("application/octet-stream", page.getContentType());
	}

	public void testSetContentType2() {
		MockContext.initContext();
		
		JSONPage page = new JSONPage();
		try {
			page.setContentType(null);
			fail();
		} catch(IllegalArgumentException ex){
			assertEquals("contentType shouldn't be null.", ex.getMessage());
		}
	}
	
	public void testGetPath() {
		JSONPage page = new JSONPage();
		assertNull(page.getPath());
	}

	private class JSONPage extends AbstractJSONPage {
	}
}
