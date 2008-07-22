package org.seasar.s2click.control;

import java.text.SimpleDateFormat;

import net.sf.click.MockContext;

import junit.framework.TestCase;

public class DateFieldYYYYMMDDTest extends TestCase {

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
