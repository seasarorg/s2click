package org.seasar.s2click.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.click.MockRequest;
import net.sf.click.MockResponse;

import org.seasar.extension.unit.S2TestCase;

public class UrlPatternFilterTest extends S2TestCase {

	public void setUp() throws Exception {
		super.setUp();
		UrlPatternManager.add("/diary/{date}", "/diary.htm");
	}
	
	public void tearDown() throws Exception {
		UrlPatternManager.getAll().clear();
		super.tearDown();
	}
	
	public void testDoFilter_リダイレクトする場合() throws Exception {
		final TestRequestDispatcher dispatcher = new TestRequestDispatcher();
		
		MockRequest request = new MockRequest(){
			public RequestDispatcher getRequestDispatcher(String path){
				dispatcher.setTargetPath(path);
				return dispatcher;
			}
		};
		request.setContextPath("/test");
		request.setRequestURI("/test/diary/20090101");
				
		MockResponse response = new MockResponse();
		
		TestFilterChain chain = new TestFilterChain();
		
		UrlPatternFilter filter = new UrlPatternFilter();
		filter.doFilter(request, response, chain);
		
		assertFalse(chain.invoked);
		assertTrue(dispatcher.fowarded);
		assertEquals("/diary.htm?date=20090101", dispatcher.targetPath);
	}
	
	public void testDoFilter_リダイレクトしない場合() throws Exception {
		MockRequest request = new MockRequest();
		request.setContextPath("/test");
		request.setRequestURI("/test/diary.htm");
		
		MockResponse response = new MockResponse();
		
		TestFilterChain chain = new TestFilterChain();
		
		UrlPatternFilter filter = new UrlPatternFilter();
		filter.doFilter(request, response, chain);
		
		assertTrue(chain.invoked);
	}
	
	private static class TestRequestDispatcher implements RequestDispatcher {

		public boolean fowarded = false;
		public String targetPath = null;
		
		public void setTargetPath(String targetPath){
			this.targetPath = targetPath;
		}
		
		public void forward(ServletRequest arg0, ServletResponse arg1)
				throws ServletException, IOException {
			this.fowarded = true;
		}

		public void include(ServletRequest arg0, ServletResponse arg1)
				throws ServletException, IOException {
		}
		
	}
	
	private static class TestFilterChain implements FilterChain {
		
		public boolean invoked = false;
		
		public void doFilter(ServletRequest arg0, ServletResponse arg1)
				throws IOException, ServletException {
			this.invoked = true;
		}
	}

}
