package org.seasar.s2click.control;

import org.apache.click.ClickServlet;
import org.apache.click.MockContext;
import org.apache.click.servlet.MockRequest;
import org.apache.click.servlet.MockResponse;
import org.apache.click.servlet.MockServletConfig;
import org.apache.click.servlet.MockServletContext;

import junit.framework.TestCase;

public abstract class S2ClickControlTestCase extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
		
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		request.setServletPath("/sample.htm");
		
		MockContext.initContext(
				new MockServletConfig("click-servlet", new MockServletContext()), 
				request, 
				new MockResponse(), 
				new ClickServlet());
	}
	
}
