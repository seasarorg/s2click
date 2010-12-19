package org.seasar.s2click.servlet;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.apache.click.servlet.MockRequest;
import org.apache.click.servlet.MockResponse;
import org.seasar.framework.mock.servlet.MockServletConfigImpl;
import org.seasar.framework.util.tiger.ReflectionUtil;

public class ResourceServletTest extends TestCase {

	public void testService_正常() throws Exception {
		MockServletConfigImpl config = new MockServletConfigImpl();
		config.setInitParameter("rootPackage", "org.seasar.s2click.servlet");

		ResourceServlet servlet = new TestResourceServlet();
		servlet.init(config);

		MockRequest request = new MockRequest();
		request.setPathInfo("/ResourceServletTest.txt");

		MockResponse response = new MockResponse();

		servlet.service(request, response);

		String content = new String(response.getBinaryContent());
		assertEquals("test!!", content);
	}

	public void testService_パスが不正な場合() throws Exception {
		MockServletConfigImpl config = new MockServletConfigImpl();
		config.setInitParameter("rootPackage", "org.seasar.s2click.servlet");

		ResourceServlet servlet = new TestResourceServlet();
		servlet.init(config);

		MockRequest request = new MockRequest();
		request.setPathInfo("../test.txt");

		MockResponse response = new MockResponse();

		try {
			servlet.service(request, response);
			fail();
		} catch(IOException ex){
			assertEquals("パスが不正です。", ex.getMessage());
		}
	}

	public void testService_リソースが存在しない場合() throws Exception {
		MockServletConfigImpl config = new MockServletConfigImpl();
		config.setInitParameter("rootPackage", "org.seasar.s2click.servlet");

		ResourceServlet servlet = new TestResourceServlet();
		servlet.init(config);

		MockRequest request = new MockRequest();
		request.setPathInfo("/test.txt");

		MockResponse response = new MockResponse();

		try {
			servlet.service(request, response);
			fail();
		} catch(IOException ex){
			assertEquals("リソースが存在しません。", ex.getMessage());
		}
	}

	public void testInit_単一のrootPackageを指定した場合() throws Exception {
		MockServletConfigImpl config = new MockServletConfigImpl();
		config.setInitParameter("rootPackage", "org.seasar.s2click.example.page");

		ResourceServlet servlet = new TestResourceServlet();
		servlet.init(config);

		Field field = ReflectionUtil.getDeclaredField(ResourceServlet.class, "rootPathArray");
		field.setAccessible(true);
		String[] rootPathArray = ReflectionUtil.getValue(field, servlet);

		assertEquals(1, rootPathArray.length);
		assertEquals("/org/seasar/s2click/example/page", rootPathArray[0]);
	}

	public void testInit_複数のrootPackageを指定した場合() throws Exception {
		MockServletConfigImpl config = new MockServletConfigImpl();
		config.setInitParameter("rootPackage",
			"org.seasar.s2click.example.page,org.seasar.s2click.example.resource");

		ResourceServlet servlet = new TestResourceServlet();
		servlet.init(config);

		Field field = ReflectionUtil.getDeclaredField(ResourceServlet.class, "rootPathArray");
		field.setAccessible(true);
		String[] rootPathArray = ReflectionUtil.getValue(field, servlet);

		assertEquals(2, rootPathArray.length);
		assertEquals("/org/seasar/s2click/example/page", rootPathArray[0]);
		assertEquals("/org/seasar/s2click/example/resource", rootPathArray[1]);
	}

	public void testInit_rootPackageが指定されていない場合() throws Exception {
		MockServletConfigImpl config = new MockServletConfigImpl();

		ResourceServlet servlet = new TestResourceServlet();
		try {
			servlet.init(config);
			fail();
		} catch(ServletException ex){
			assertEquals("初期化パラメータ 'rootPackage' が指定されていません。", ex.getMessage());
		}
	}

	public void testInit_rootPackageが空の場合() throws Exception {
		MockServletConfigImpl config = new MockServletConfigImpl();
		config.setInitParameter("rootPackage", ",");

		ResourceServlet servlet = new TestResourceServlet();
		try {
			servlet.init(config);
			fail();
		} catch(ServletException ex){
			assertEquals("初期化パラメータ 'rootPackage' が指定されていません。", ex.getMessage());
		}
	}

	private class TestResourceServlet extends ResourceServlet {

		private static final long serialVersionUID = 1L;

		@Override
		public void log(String message, Throwable t) {
		}

		@Override
		public void log(String msg) {
		}
	}
}
