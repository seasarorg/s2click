package org.seasar.s2click;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.util.tiger.ReflectionUtil;
import org.seasar.s2click.annotation.Request;

import junit.framework.TestCase;
import net.sf.click.ClickApp;
import net.sf.click.Context;
import net.sf.click.MockContext;
import net.sf.click.MockResponse;
import net.sf.click.MockResponse.ServletOutputStreamImpl;

public class S2ClickPageTest extends TestCase {

	public void testRenderJSON() throws Exception {
		MockResponse res = new MockResponse();
		MockContext.initContext(res);
		
		S2ClickPage page = new S2ClickPage();
		page.setHeaders(new HashMap<Object, Object>());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Tarou");
		map.put("age", 20);
		
		page.renderJSON(map);
		
		assertEquals("application/x-javascript; charset=utf-8", res.getContentType());
		
		String value = new String(
				((ServletOutputStreamImpl) res.getOutputStream()).getContents(), "UTF-8");
		assertEquals("{\"age\":20,\"name\":\"Tarou\"}", value);
	}

	public void testRenderHTML() throws Exception {
		MockResponse res = new MockResponse();
		MockContext.initContext(res);
		
		S2ClickPage page = new S2ClickPage();
		page.setHeaders(new HashMap<Object, Object>());
		
		page.renderHTML("<html>あああ</html>");
		
		assertEquals("text/html; charset=UTF-8", res.getContentType());
		
		String value = new String(
				((ServletOutputStreamImpl) res.getOutputStream()).getContents(), "UTF-8");
		assertEquals("<html>あああ</html>", value);
	}
	
	public void testValidatePageFields_単一エラー(){
		MockContext.initContext();
		Context.getThreadLocalContext().setRequestAttribute(
				ClickApp.class.getName(), new TestClickApp());
		
		TestPage page = new TestPage();
		page.password = "password";
		String result = page.validatePageFields();
		
		assertEquals("必須パラメータ userName が指定されていません。\n", result);
	}
	
	public void testValidatePageFields_複数エラー(){
		MockContext.initContext();
		Context.getThreadLocalContext().setRequestAttribute(
				ClickApp.class.getName(), new TestClickApp());
		
		TestPage page = new TestPage();
		String result = page.validatePageFields();
		
		assertEquals("必須パラメータ userName が指定されていません。\n" + 
				"必須パラメータ password が指定されていません。\n", result);
	}
	
	public void testValidatePageFields_エラーなし(){
		MockContext.initContext();
		Context.getThreadLocalContext().setRequestAttribute(
				ClickApp.class.getName(), new TestClickApp());
		
		TestPage page = new TestPage();
		page.userName = "たけぞう";
		page.password = "password";
		String result = page.validatePageFields();
		
		assertNull(result);
	}
	
	public static class TestClickApp extends ClickApp {
		@Override
		@SuppressWarnings("unchecked")
		public Field[] getPageFieldArray(Class pageClass) {
			return new Field[]{
				ReflectionUtil.getField(TestPage.class, "userId"),
				ReflectionUtil.getField(TestPage.class, "userName"),
				ReflectionUtil.getField(TestPage.class, "password")
			};
		}
	}
	
	public static class TestPage extends S2ClickPage {
		
		@Request
		public String userId;
		
		@Request(required=true)
		public String userName;
		
		@Request(required=true)
		public String password;
	}
	
//
//	public void testRenderFile() {
//		fail("Not yet implemented");
//	}
//
//	public void testRenderResponse() {
//		fail("Not yet implemented");
//	}

}
