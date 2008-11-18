package org.seasar.s2click;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import net.sf.click.Context;
import net.sf.click.MockContext;
import net.sf.click.MockResponse.ServletOutputStreamImpl;

public class S2ClickPageTest extends TestCase {

	public void testRenderJSON() throws Exception {
		MockContext.initContext();
		
		S2ClickPage page = new S2ClickPage();
		page.setHeaders(new HashMap<Object, Object>());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Tarou");
		map.put("age", 20);
		
		page.renderJSON(map);
		
		Context context = Context.getThreadLocalContext();
		HttpServletResponse res = context.getResponse();
		
		assertEquals("application/x-javascript; charset=utf-8", res.getContentType());
		
		String value = new String(
				((ServletOutputStreamImpl) res.getOutputStream()).getContents(), "UTF-8");
		assertEquals("{\"age\":20,\"name\":\"Tarou\"}", value);
	}

//	public void testRenderHTML() {
//		fail("Not yet implemented");
//	}
//
//	public void testRenderFile() {
//		fail("Not yet implemented");
//	}
//
//	public void testRenderResponse() {
//		fail("Not yet implemented");
//	}

}
