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
package org.seasar.s2click;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.click.Page;
import org.seasar.framework.util.tiger.ReflectionUtil;
import org.seasar.s2click.annotation.Request;
import org.seasar.s2click.exception.RequestRequiredException;
import org.seasar.s2click.test.MockConfigService;
import org.seasar.s2click.test.S2ClickTestCase;

public class S2ClickPageTest extends S2ClickTestCase {

	public void testRenderJSON() throws Exception {
		@SuppressWarnings("serial")
		S2ClickPage page = new S2ClickPage(){};

		page.setHeaders(new HashMap<String, Object>());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Tarou");
		map.put("age", 20);

		page.renderJSON(map);

		assertEquals("application/x-javascript; charset=utf-8", response.getContentType());

		String value = new String(response.getBinaryContent(), "UTF-8");
		assertEquals("{\"age\":20,\"name\":\"Tarou\"}", value);
	}

	public void testRenderHTML() throws Exception {
		@SuppressWarnings("serial")
		S2ClickPage page = new S2ClickPage(){};

		page.setHeaders(new HashMap<String, Object>());

		page.renderHTML("<html>あああ</html>");

		assertEquals("text/html; charset=utf-8", response.getContentType());

		String value = new String(response.getBinaryContent(), "UTF-8");
		assertEquals("<html>あああ</html>", value);
	}

	public void testValidatePageFields_エラー(){
		setConfigService(new TestConfigService());
		request.setParameter("password", "password");

		TestPage page = new TestPage();

		try {
			page.bindPageFields();
			fail();
		} catch(RequestRequiredException ex){
			assertEquals("必須パラメータ userName が指定されていません。", ex.getMessage());
		}
	}

	public void testValidatePageFields_エラーなし(){
		request.setParameter("userName", "たけぞう");
		request.setParameter("password", "password");
		setConfigService(new TestConfigService());

		TestPage page = new TestPage();

		page.bindPageFields();
	}

	public static class TestConfigService extends MockConfigService {
		@Override
		@SuppressWarnings("rawtypes")
		public Field[] getPageFieldArray(Class pageClass) {
			return new Field[]{
				ReflectionUtil.getField(TestPage.class, "userId"),
				ReflectionUtil.getField(TestPage.class, "userName"),
				ReflectionUtil.getField(TestPage.class, "password")
			};
		}

		@Override
		public Map<String, Field> getPageFields(Class<? extends Page> pageClass) {
			Map<String, Field> fields = new HashMap<String, Field>();
			for(Field field: getPageFieldArray(pageClass)){
				fields.put(field.getName(), field);
			}
			return fields;
		}
	}

	@SuppressWarnings("serial")
	public static class TestPage extends S2ClickPage {

		@Request
		public String userId;

		@Request(required=true)
		public String userName;

		@Request(required=true)
		public String password;
	}

//
//	public void testRenderFile() {
//		fail("Not yet implemented");
//	}
//
//	public void testRenderResponse() {
//		fail("Not yet implemented");
//	}

}
