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

import org.apache.click.element.Element;
import org.apache.click.util.HtmlStringBuffer;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.test.S2ClickTestCase;

public class GreyboxButtonTest extends S2ClickTestCase {

	public void testGetHeadElements(){
		GreyboxButton button = new GreyboxButton();
		List<Element> elements = button.getHeadElements();
		assertEquals(5, elements.size());

		HtmlStringBuffer buffer = new HtmlStringBuffer();
		elements.get(0).render(buffer);
		elements.get(1).render(buffer);
		elements.get(2).render(buffer);
		elements.get(3).render(buffer);
		elements.get(4).render(buffer);

		assertEquals(
				"<script src=\"/sample/resources/greybox/AJS.js\" type=\"text/javascript\"></script>" +
				"<script src=\"/sample/resources/greybox/AJS_fx.js\" type=\"text/javascript\"></script>" +
				"<script src=\"/sample/resources/greybox/gb_scripts.js\" type=\"text/javascript\"></script>" +
				"<link href=\"/sample/click/greybox/gb_styles.css\" rel=\"stylesheet\" type=\"text/css\"/>" +
				"<script type=\"text/javascript\">\n" +
				"function S2Click_GB_SetResult(data, id)'{ AJS.$(id).value = data; }'\n" +
				"</script>",
				buffer.toString());
	}

	public void testSetPageClass() {
		GreyboxButton button = new GreyboxButton();
		button.setPageClass(S2ClickPage.class);

		assertSame(S2ClickPage.class, button.getPageClass());
	}

	public void testSetDialogTitle() {
		GreyboxButton button = new GreyboxButton();
		button.setDialogTitle("タイトル");

		assertEquals("タイトル", button.getDialogTitle());
	}

	public void testSetDialogWidth() {
		GreyboxButton button = new GreyboxButton();
		assertEquals(400, button.getDialogWidth());

		button.setDialogWidth(600);
		assertEquals(600, button.getDialogWidth());
	}

	public void testSetDialogHeight() {
		GreyboxButton button = new GreyboxButton();
		assertEquals(300, button.getDialogHeight());

		button.setDialogHeight(400);
		assertEquals(400, button.getDialogHeight());
	}

	public void testToString() {
		configService.setPagePath(S2ClickPage.class, "/test.htm");

		GreyboxButton button = new GreyboxButton("button", "ボタン", "title", S2ClickPage.class);

		assertEquals("<input type=\"button\" name=\"button\" id=\"button\" " +
				"value=\"ボタン\" onclick=\"GB_showCenter('title', '../../test.htm', 300, 400)\"/>",
				button.toString());
	}

}
