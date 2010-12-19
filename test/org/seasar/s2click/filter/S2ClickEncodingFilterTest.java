package org.seasar.s2click.filter;

import org.apache.click.servlet.MockRequest;
import org.apache.click.servlet.MockResponse;
import org.seasar.extension.unit.S2TestCase;

public class S2ClickEncodingFilterTest extends S2TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        include("app_ut.dicon");
    }

	public void testDoFilter1() throws Exception {
		MockRequest request = new MockRequest();
		MockResponse response = new MockResponse();
		MockFilterChain chain = new MockFilterChain();

		S2ClickEncodingFilter filter = new S2ClickEncodingFilter();
		filter.doFilter(request, response, chain);

		assertTrue(chain.isInvoked());
		assertEquals("UTF-8", request.getCharacterEncoding());
	}

	public void testDoFilter2() throws Exception {
		MockRequest request = new MockRequest();
		request.setCharacterEncoding("Windows-31J");

		MockResponse response = new MockResponse();
		MockFilterChain chain = new MockFilterChain();

		S2ClickEncodingFilter filter = new S2ClickEncodingFilter();
		filter.doFilter(request, response, chain);

		assertTrue(chain.isInvoked());
		assertEquals("Windows-31J", request.getCharacterEncoding());
	}

}
