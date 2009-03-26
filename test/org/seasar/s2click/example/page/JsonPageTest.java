package org.seasar.s2click.example.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.seasar.s2click.S2ClickPageTestCase;
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
