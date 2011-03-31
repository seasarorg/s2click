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
package org.seasar.s2click.control;

import java.util.List;
import java.util.Map;

import org.apache.click.element.Element;
import org.apache.click.util.HtmlStringBuffer;
import org.seasar.s2click.test.S2ClickTestCase;
import org.seasar.s2click.util.AjaxUtils;

public class AjaxSubmitTest extends S2ClickTestCase {

	public void testGetHeadElements(){
		AjaxSubmit submit = new AjaxSubmit();
		List<Element> elements = submit.getHeadElements();
		assertEquals(1, elements.size());

		HtmlStringBuffer buffer = new HtmlStringBuffer();
		elements.get(0).render(buffer);

		assertEquals(
				"<script src=\"/sample/resources/prototype.js\" type=\"text/javascript\"></script>",
				buffer.toString());
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
