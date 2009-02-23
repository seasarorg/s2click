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
package org.seasar.s2click.control;

import java.util.Map;

import org.seasar.s2click.util.AjaxUtils;

import net.sf.click.MockContext;
import net.sf.click.MockRequest;
import junit.framework.TestCase;

public class AjaxSubmitTest extends TestCase {

	public void testGetHtmlImports() {
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		
		MockContext.initContext(request);
		
		AjaxSubmit submit = new AjaxSubmit();
		assertEquals("<script type=\"text/javascript\" src=\"/sample/resources/prototype.js\"></script>\n", 
				submit.getHtmlImports());
	}

	public void testGetAjaxHandlers() {
		AjaxSubmit submit = new AjaxSubmit();
		submit.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");
		submit.addAjaxHandler(AjaxUtils.ON_EXCEPTION, "processException");
		
		Map<String, String> handlers = submit.getAjaxHandlers();
		assertEquals(2, handlers.size());
		assertEquals("processCompleted", handlers.get(AjaxUtils.ON_COMPLETE));
		assertEquals("processException", handlers.get(AjaxUtils.ON_EXCEPTION));
	}

	public void testToString() {
		MockRequest request = new MockRequest();
		request.setRequestURI("http://localhost:8080/sample/sample.htm");

		MockContext.initContext(request);
		
		@SuppressWarnings("serial")
		S2ClickForm form = new S2ClickForm("form"){};
		
		AjaxSubmit submit = new AjaxSubmit("submit");
		form.add(submit);
		
		assertEquals("<input type=\"submit\" name=\"submit\" id=\"form_submit\" value=\"Submit\"/>" +
				"<script type=\"text/javascript\">\n" +
				"$('form').onsubmit = function(){\n" +
				"  this.request({ method: 'post', }); return false;}\n" +
				"</script>\n", submit.toString());
	}

}
