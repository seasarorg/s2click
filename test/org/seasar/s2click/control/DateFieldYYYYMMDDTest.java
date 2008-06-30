package org.seasar.s2click.control;

import java.text.SimpleDateFormat;

import net.sf.click.MockContext;

import junit.framework.TestCase;

public class DateFieldYYYYMMDDTest extends TestCase {

	/**
	 * <code>getValue()</code>���\�b�h�ł�yyyy/MM/dd�`���Ńt�H�[�}�b�g���ꂽ���t���擾�ł��邱�ƁB
	 */
	public void testDateFieldYYYYMMDD1() throws Exception {
		MockContext.initContext();
		DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateField.setDate(format.parse("2000/01/01"));
		
		assertEquals("2000/01/01", dateField.getValue());
	}

	/**
	 * <code>setValue()</code>���\�b�h��yyyy/MM/dd�`���̕�����Ƃ��ăZ�b�g�������t��
	 * <code>getDate()</code>���\�b�h�Ŏ擾�ł��邱�ƁB
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
		DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD("name", "���t");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateField.setDate(format.parse("2000/01/01"));
		
		assertEquals("2000/01/01", dateField.getValue());
		assertEquals("name", dateField.getName());
		assertEquals("���t", dateField.getLabel());
	}

	public void testDateFieldYYYYMMDD6() throws Exception {
		MockContext.initContext();
		DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD("name", "���t", true);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		dateField.setDate(format.parse("2000/01/01"));
		
		assertEquals("2000/01/01", dateField.getValue());
		assertEquals("name", dateField.getName());
		assertEquals("���t", dateField.getLabel());
		assertTrue(dateField.isRequired());
	}
}
