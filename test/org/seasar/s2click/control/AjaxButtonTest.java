/*
 * Copyright 2006-2010 the Seasar Foundation and the Others.
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

public class AjaxButtonTest extends S2ClickTestCase {

//	public void testGetHtmlImports() {
//		AjaxButton button = new AjaxButton();
//
//		assertEquals("<script type=\"text/javascript\" src=\"/sample/resources/prototype.js\"></script>\n",
//				button.getHtmlImports());
//	}

	public void testGetHeadElements(){
		AjaxButton button = new AjaxButton();
		List<Element> elements = button.getHeadElements();
		assertEquals(1, elements.size());

		HtmlStringBuffer buffer = new HtmlStringBuffer();
		elements.get(0).render(buffer);

		assertEquals(
				"<script src=\"/sample/resources/prototype.js\" type=\"text/javascript\"></script>",
				buffer.toString());
	}

	public void testGetAjaxHandlers() {
		AjaxButton button = new AjaxButton();
		button.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");
		button.addAjaxHandler(AjaxUtils.ON_EXCEPTION, "processException");

		Map<String, String> handlers = button.getAjaxHandlers();
		assertEquals(2, handlers.size());
		assertEquals("processCompleted", handlers.get(AjaxUtils.ON_COMPLETE));
		assertEquals("processException", handlers.get(AjaxUtils.ON_EXCEPTION));
	}

	public void testGetElementId() {
		AjaxButton button = new AjaxButton();
		button.setElementId("element-id");
		assertEquals("element-id", button.getElementId());
	}

	public void testGetOnClick1() {
		AjaxButton button = new AjaxButton("ajaxButton");
		button.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");

		// elementIdを指定しなかった場合はAjax.Requestで送信
		assertEquals("new Ajax.Request('/sample/sample.htm?actionButton=ajaxButton', "+
				"{method: 'post', onComplete: processCompleted})",
				button.getOnClick());
	}

	public void testGetOnClick2() {
		AjaxButton button = new AjaxButton("ajaxButton");
		button.setElementId("targetElement");

		// elementIdを指定した場合はAjax.Updaterで送信
		assertEquals("new Ajax.Updater('targetElement', " +
				"'/sample/sample.htm?actionButton=ajaxButton', " +
				"{method: 'post'})",
				button.getOnClick());
	}
}
