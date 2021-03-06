/*
 * Copyright 2006-2011 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.s2click.filter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.click.servlet.MockRequest;
import org.apache.click.servlet.MockResponse;
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
		request.setServletPath("/diary/20090101");

		MockResponse response = new MockResponse();

		MockFilterChain chain = new MockFilterChain();

		UrlPatternFilter filter = new UrlPatternFilter();
		filter.doFilter(request, response, chain);

		assertFalse(chain.isInvoked());
		assertTrue(dispatcher.fowarded);
		assertEquals("/diary.htm?date=20090101", dispatcher.targetPath);
	}

	public void testDoFilter_リダイレクトしない場合() throws Exception {
		MockRequest request = new MockRequest();
		request.setContextPath("/test");
		request.setServletPath("/diary.htm");

		MockResponse response = new MockResponse();

		MockFilterChain chain = new MockFilterChain();

		UrlPatternFilter filter = new UrlPatternFilter();
		filter.doFilter(request, response, chain);

		assertTrue(chain.isInvoked());
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

}
