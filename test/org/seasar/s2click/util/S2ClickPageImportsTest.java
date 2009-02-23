/*
 * Copyright 2006-2009 the Seasar Foundation and the Others.
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
package org.seasar.s2click.util;

import junit.framework.TestCase;
import net.sf.click.MockContext;
import net.sf.click.MockRequest;
import net.sf.click.Page;
import net.sf.click.control.Form;

import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.control.AjaxSubmit;

public class S2ClickPageImportsTest extends TestCase {

	public void testProcessControlControl() {
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		MockContext.initContext(request);
		
		TestS2ClickPageImports imports = new TestS2ClickPageImports(new S2ClickPage(){});
		
		Form form = new Form("form");
		form.add(new AjaxSubmit("submit"));
		
		imports.processControl(form);
		
		// ボタンのgetHtmlImports()の内容が出力されていること
		assertTrue(imports.sb.toString().endsWith(
				"<script type=\"text/javascript\" src=\"/sample/resources/prototype.js\"></script>\n"));
	}
	
	private class TestS2ClickPageImports extends S2ClickPageImports {
		
		private StringBuilder sb = new StringBuilder();
		
		public TestS2ClickPageImports(Page page) {
			super(page);
		}

		@Override
		protected void processLine(String value) {
			if(value != null){
				sb.append(value);
			}
		}
	}

}
