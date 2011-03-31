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

import org.apache.click.element.Element;
import org.apache.click.util.HtmlStringBuffer;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.test.S2ClickTestCase;

public class GreyboxLinkTest extends S2ClickTestCase {

	public void testGetHeadElements(){
		GreyboxLink link = new GreyboxLink();
		List<Element> elements = link.getHeadElements();
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
		GreyboxLink link = new GreyboxLink();
		link.setPageClass(S2ClickPage.class);

		assertSame(S2ClickPage.class, link.getPageClass());
	}

	public void testSetDialogTitle() {
		GreyboxLink link = new GreyboxLink();
		link.setDialogTitle("タイトル");

		assertEquals("タイトル", link.getDialogTitle());
	}

	public void testSetDialogWidth() {
		GreyboxLink link = new GreyboxLink();
		assertEquals(400, link.getDialogWidth());

		link.setDialogWidth(600);
		assertEquals(600, link.getDialogWidth());
	}

	public void testSetDialogHeight() {
		GreyboxLink link = new GreyboxLink();
		assertEquals(300, link.getDialogHeight());

		link.setDialogHeight(400);
		assertEquals(400, link.getDialogHeight());
	}

	public void testGetHref() {
		GreyboxLink link = new GreyboxLink("link", "リンク");
		assertEquals("javascript:void(0);", link.getHref());
	}

	public void testOnProcess() {
		GreyboxLink link = new GreyboxLink();
		assertTrue(link.onProcess());
	}

	public void testSetListener() {
		GreyboxLink link = new GreyboxLink();
		try {
			link.setListener(null, null);
			fail();
		} catch(UnsupportedOperationException ex){
		}
	}

	public void testToString() {
		configService.setPagePath(S2ClickPage.class, "/test.htm");

		GreyboxLink link = new GreyboxLink("link", "リンク", "title", S2ClickPage.class);

		assertEquals("<a href=\"javascript:void(0);\" title=\"title\" " +
				"onclick=\"GB_showCenter('title', '../../test.htm', 300, 400)\">リンク</a>",
				link.toString());
	}

}
