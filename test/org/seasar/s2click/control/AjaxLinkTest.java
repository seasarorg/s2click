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

public class AjaxLinkTest extends S2ClickTestCase {

	public void testGetHeadElements(){
		AjaxLink link = new AjaxLink();
		List<Element> elements = link.getHeadElements();
		assertEquals(1, elements.size());

		HtmlStringBuffer buffer = new HtmlStringBuffer();
		elements.get(0).render(buffer);

		assertEquals(
				"<script src=\"/sample/resources/prototype.js\" type=\"text/javascript\"></script>",
				buffer.toString());
	}


	public void testGetAjaxHandlers() {
		AjaxLink link = new AjaxLink();
		link.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");
		link.addAjaxHandler(AjaxUtils.ON_EXCEPTION, "processException");

		Map<String, String> handlers = link.getAjaxHandlers();
		assertEquals(2, handlers.size());
		assertEquals("processCompleted", handlers.get(AjaxUtils.ON_COMPLETE));
		assertEquals("processException", handlers.get(AjaxUtils.ON_EXCEPTION));

	}

	public void testGetHref() {
		AjaxLink link = new AjaxLink("ajaxLink");
		assertEquals("javascript:void(0)", link.getHref());
	}

	public void testGetElementId() {
		AjaxLink link = new AjaxLink();
		link.setElementId("element-id");
		assertEquals("element-id", link.getElementId());
	}

	public void testToString1() {
		AjaxLink link = new AjaxLink("ajaxLink", "Ajax.Requestのテスト");
		link.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");

		// elementIdを指定しなかった場合はAjax.Requestで送信
		assertEquals("<a href=\"javascript:void(0)\" " +
				"onclick=\"new Ajax.Request('/sample/sample.htm?actionLink=ajaxLink', " +
				"{method: 'post', onComplete: processCompleted})\">Ajax.Requestのテスト</a>",
				link.toString());
	}

	public void testToString2() {
		AjaxLink link = new AjaxLink("ajaxLink", "Ajax.Requestのテスト");
		link.setElementId("targetElement");

		// elementIdを指定した場合はAjax.Updaterで送信
		assertEquals("<a href=\"javascript:void(0)\" " +
				"onclick=\"new Ajax.Updater('targetElement', '/sample/sample.htm?actionLink=ajaxLink', " +
				"{method: 'post'})\">Ajax.Requestのテスト</a>",
				link.toString());
	}
}
