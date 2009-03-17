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

import java.text.SimpleDateFormat;

import org.apache.click.MockContext;

public class DateFieldYYYYMMDDTest extends S2ClickControlTestCase {

	/**
	 * <code>getValue()</code>メソッドででyyyy/MM/dd形式でフォーマットされた日付が取得できること。
	 */
	public void testDateFieldYYYYMMDD1() throws Exception {
		MockContext.initContext();
		DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateField.setDate(format.parse("2000/01/01"));
		
		assertEquals("2000/01/01", dateField.getValue());
	}

	/**
	 * <code>setValue()</code>メソッドでyyyy/MM/dd形式の文字列としてセットした日付が
	 * <code>getDate()</code>メソッドで取得できること。
	 */
	public void testDateFieldYYYYMMDD2() throws Exception {
		MockContext.initContext();
		DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD();
		
		dateField.setValue("2000/01/01");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		assertEquals(format.parse("2000/01/01"), dateField.getDate());
	}
	
	public void testDateFieldYYYYMMDD3() throws Exception {
		MockContext.initContext();
		DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD("name");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateField.setDate(format.parse("2000/01/01"));
		
		assertEquals("2000/01/01", dateField.getValue());
		assertEquals("name", dateField.getName());
	}

	public void testDateFieldYYYYMMDD4() throws Exception {
		MockContext.initContext();
		DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD("name", true);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateField.setDate(format.parse("2000/01/01"));
		
		assertEquals("2000/01/01", dateField.getValue());
		assertEquals("name", dateField.getName());
		assertTrue(dateField.isRequired());
	}

	public void testDateFieldYYYYMMDD5() throws Exception {
		MockContext.initContext();
		DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD("name", "日付");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateField.setDate(format.parse("2000/01/01"));
		
		assertEquals("2000/01/01", dateField.getValue());
		assertEquals("name", dateField.getName());
		assertEquals("日付", dateField.getLabel());
	}

	public void testDateFieldYYYYMMDD6() throws Exception {
		MockContext.initContext();
		DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD("name", "日付", true);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateField.setDate(format.parse("2000/01/01"));
		
		assertEquals("2000/01/01", dateField.getValue());
		assertEquals("name", dateField.getName());
		assertEquals("日付", dateField.getLabel());
		assertTrue(dateField.isRequired());
	}
}
