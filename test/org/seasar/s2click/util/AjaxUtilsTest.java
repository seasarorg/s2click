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
package org.seasar.s2click.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class AjaxUtilsTest extends TestCase {

	public void testCreateAjaxRequest() {
		Map<String, String> options = new HashMap<String, String>();
		options.put(AjaxUtils.ON_COMPLETE, "onCompleteFunction");
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", "001");
		
		String result = AjaxUtils.createAjaxRequest(
				"http://www.example.com", options, parameters);
		
		assertEquals("new Ajax.Request('http://www.example.com', "
				+ "{method: 'post', parameters: {'id': '001'}, "
				+ "onComplete: onCompleteFunction})", result);
	}

	public void testCreateAjaxUpdater() {
		Map<String, String> options = new HashMap<String, String>();
		options.put(AjaxUtils.ON_COMPLETE, "onCompleteFunction");
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", "001");
		
		String result = AjaxUtils.createAjaxUpdater("result",
				"http://www.example.com", options, parameters);
		
		assertEquals("new Ajax.Updater('result', 'http://www.example.com', "
				+ "{method: 'post', parameters: {'id': '001'}, "
				+ "onComplete: onCompleteFunction})", result);

	}

	/**
	 * {@link AjaxUtils#getOptions(Map)}のテスト。
	 */
	public void testGetOptions_1() {
		Map<String, String> options = new HashMap<String, String>();
		options.put(AjaxUtils.ON_COMPLETE, "onCompleteFunction");
		options.put(AjaxUtils.ON_FAILURE, "onFilureFunction");
		
		String result = AjaxUtils.getOptions(options);
		assertEquals("onComplete: onCompleteFunction, onFailure: onFilureFunction", result);
	}

	/**
	 * {@link AjaxUtils#getOptions(Map)}のテスト。
	 * <p>
	 * 引数で渡したマップが空の場合。
	 */
	public void testGetOptions_2() {
		Map<String, String> options = new HashMap<String, String>();
		
		String result = AjaxUtils.getOptions(options);
		assertEquals("", result);
	}
}
