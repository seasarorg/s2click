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
package org.seasar.s2click.example.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.seasar.s2click.test.S2ClickPageTestCase;
import org.seasar.s2click.util.AjaxUtils;

public class JsonPageTest extends S2ClickPageTestCase<JsonPage> {

	public void testOnInit() {
		page.onInit();
		
		assertEquals("target", page.button.getElementId());
		assertEquals("startProgress", page.button.getAjaxHandlers().get(AjaxUtils.ON_CREATE));
		assertEquals("stopProgress", page.button.getAjaxHandlers().get(AjaxUtils.ON_SUCCESS));
	}

	public void testOnSearch() throws Exception {
		page.setHeaders(new HashMap<String, String>());
		page.form.keyword.setValue("Wiki");
		page.onSearch();
		
		assertEquals("application/x-javascript; charset=utf-8", response.getContentType());
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> books = List.class.cast(
				JSON.decode(new String(response.getBinaryContent(), "UTF-8")));
		
		assertEquals(1, books.size());
		assertEquals("入門Wiki", books.get(0).get("title"));
	}

	public void testOnSearchAll() throws Exception {
		page.setHeaders(new HashMap<String, String>());
		page.onSearchAll();
		
		assertEquals("application/x-javascript; charset=utf-8", response.getContentType());
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> books = List.class.cast(
				JSON.decode(new String(response.getBinaryContent(), "UTF-8")));
		
		assertEquals(4, books.size());
		assertEquals("入門Wiki", books.get(0).get("title"));
		assertEquals("Eclipseプラグイン開発徹底攻略", books.get(1).get("title"));
		assertEquals("Eclipse逆引きクイックリファレンス", books.get(2).get("title"));
		assertEquals("独習JavaScript", books.get(3).get("title"));
	}

	public void testOnAjaxUpdater() throws Exception {
		page.setHeaders(new HashMap<String, String>());
		page.onAjaxUpdater();
		
		assertEquals("text/html; charset=UTF-8", response.getContentType());
		assertEquals("<h2>Ajaxでコンテンツを置換しました</h2>", 
				new String(response.getBinaryContent(), "UTF-8"));
	}

}
