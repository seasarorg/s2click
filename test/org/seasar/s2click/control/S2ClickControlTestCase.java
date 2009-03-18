package org.seasar.s2click.control;

import org.apache.click.ClickServlet;
import org.apache.click.MockContext;
import org.apache.click.service.ConfigService;
import org.apache.click.servlet.MockRequest;
import org.apache.click.servlet.MockResponse;
import org.apache.click.servlet.MockServletConfig;
import org.apache.click.servlet.MockServletContext;
import org.seasar.s2click.MockConfigService;

import junit.framework.TestCase;

public abstract class S2ClickControlTestCase extends TestCase {

	protected MockConfigService configService;
	
	public void setUp() throws Exception {
		super.setUp();
		
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		request.setServletPath("/sample.htm");
		
		configService = new MockConfigService();
		
		@SuppressWarnings("serial")
		ClickServlet servlet = new ClickServlet(){
			@Override
			protected ConfigService getConfigService() {
				return S2ClickControlTestCase.this.configService;
			}
		};
		
		MockContext.initContext(
				new MockServletConfig("click-servlet", new MockServletContext()), 
				request, 
				new MockResponse(), 
				servlet);
	}
	
}
