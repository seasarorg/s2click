package org.seasar.s2click.page;

import junit.framework.TestCase;

public class AbstractDownloadPageTest extends TestCase {

	public void testSetContentType1() {
		DownloadPage page = new DownloadPage();
		assertEquals("application/octet-stream", page.getContentType());
		
		page.setContentType("text/plain");
		assertEquals("text/plain", page.getContentType());
	}

	public void testSetContentType2() {
		DownloadPage page = new DownloadPage();
		try {
			page.setContentType(null);
			fail();
		} catch(IllegalArgumentException ex){
			assertEquals("contentType shouldn't be null.", ex.getMessage());
		}
	}
	
	public void testSetFileName() {
		DownloadPage page = new DownloadPage();
		assertNull(page.fileName);
		
		page.setFileName("readme.txt");
		assertEquals("readme.txt", page.fileName);
	}

	public void testSetContents1() {
		DownloadPage page = new DownloadPage();
		try {
			page.setContents(null);
			fail();
		} catch(IllegalArgumentException ex){
			assertEquals("contents shouldn't be null.", ex.getMessage());
		}
	}

	public void testSetContentDisposition() {
		DownloadPage page = new DownloadPage();
		assertEquals("attachment", page.contentDisposition);
		
		page.setContentDisposition("inline");
		assertEquals("inline", page.contentDisposition);
	}

	public void testGetPath() {
		DownloadPage page = new DownloadPage();
		assertNull(page.getPath());
	}
	
	private class DownloadPage extends AbstractDownloadPage {
	}

}
