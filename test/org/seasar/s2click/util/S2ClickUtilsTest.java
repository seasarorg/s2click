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
package org.seasar.s2click.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.click.Context;
import org.apache.click.MockContext;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.PasswordField;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.CheckList;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.service.ConfigService;
import org.seasar.s2click.S2ClickConfig;
import org.seasar.s2click.S2ClickTestCase;
import org.seasar.s2click.control.DateFieldYYYYMMDD;
import org.seasar.s2click.control.HiddenList;
import org.seasar.s2click.servlet.S2ClickConfigService;

public class S2ClickUtilsTest extends S2ClickTestCase {
	
	@Override
	protected String getRootDicon() throws Throwable {
		return "s2click.dicon";
	}
	
	public void testGetConfigService(){
		ConfigService configService = new S2ClickConfigService();
		
		MockContext.initContext();
		Context context = Context.getThreadLocalContext();
		context.getServletContext().setAttribute(ConfigService.CONTEXT_NAME, configService);
		
		assertSame(configService, S2ClickUtils.getConfigService());
	}

	public void testUrlEncode() {
		assertEquals("ABCDEFG", 
				S2ClickUtils.urlEncode("ABCDEFG"));
		assertEquals("Click+Framework", 
				S2ClickUtils.urlEncode("Click Framework"));
		assertEquals("%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A", 
				S2ClickUtils.urlEncode("あいうえお"));
	}

	public void testConvertNbsp() {
		assertEquals(" &nbsp;&nbsp;&nbsp;", S2ClickUtils.convertNbsp("    "));
		assertEquals("a &nbsp;&nbsp;&nbsp;b", S2ClickUtils.convertNbsp("a    b"));
		assertEquals("a b", S2ClickUtils.convertNbsp("a b"));
		assertEquals("a \n b", S2ClickUtils.convertNbsp("a \n b"));
		assertEquals("a \n &nbsp;b", S2ClickUtils.convertNbsp("a \n  b"));
	}

	public void testGetComponent() {
		S2ClickConfig config = S2ClickUtils.getComponent(S2ClickConfig.class);
		assertNotNull(config);
	}
	
	/**
	 * publicフィールドを使用したDTOの場合の<code>copyFormToObject()</code>のテスト。
	 */
	public void testCopyFormToObject1(){
		Form form = new Form("form");
		
		TextField name = new TextField("name");
		name.setValue("Name");
		
		PasswordField pass = new PasswordField("pass");
		pass.setValue("password");
		
		IntegerField age = new IntegerField("age");
		age.setInteger(20);
		
		form.add(name);
		form.add(pass);
		form.add(age);
		
		SampleDto1 dto = new SampleDto1();
		S2ClickUtils.copyFormToObject(form, dto, true);
		
		assertEquals("Name", dto.name);
		assertEquals("password", dto.pass);
		assertEquals(20, dto.age.intValue());
	}
	
	/**
	 * アクセサメソッドを使用したDTOの場合の<code>copyFormToObject()</code>のテスト。
	 */
	public void testCopyFormToObject2(){
		Form form = new Form("form");
		
		TextField name = new TextField("name");
		name.setValue("Name");
		
		PasswordField pass = new PasswordField("pass");
		pass.setValue("password");
		
		IntegerField age = new IntegerField("age");
		age.setInteger(20);
		
		form.add(name);
		form.add(pass);
		form.add(age);
		
		SampleDto2 dto = new SampleDto2();
		S2ClickUtils.copyFormToObject(form, dto, true);
		
		assertEquals("Name", dto.getName());
		assertEquals("password", dto.getPass());
		assertEquals(20, dto.getAge().intValue());
	}
	
	/**
	 * publicフィールドを使用したDTOの場合の<code>copyObjecyToForm()</code>のテスト。
	 */
	public void testCopyObjectToForm1() throws Exception {
		MockContext.initContext();
		
		Form form = new Form("form");
		TextField name = new TextField("name");
		PasswordField pass = new PasswordField("pass");
		IntegerField age = new IntegerField("age");
		DateField date = new DateFieldYYYYMMDD("date");
		form.add(name);
		form.add(pass);
		form.add(age);
		form.add(date);
		
		SampleDto1 dto = new SampleDto1();
		dto.name = "Taro";
		dto.pass = "secret";
		dto.age = 15;
		dto.date = new SimpleDateFormat("yyyy/MM/dd").parse("2009/01/01");
		S2ClickUtils.copyObjectToForm(dto, form, true);
		
		assertEquals("Taro", name.getValue());
		assertEquals("secret", pass.getValue());
		assertEquals(15, age.getInteger().intValue());
		assertEquals("2009/01/01", date.getValue());
	}
	
	/**
	 * アクセサメソッドを使用したDTOの場合の<code>copyObjecyToForm()</code>のテスト。
	 */
	public void testCopyObjectToForm2() throws Exception {
		MockContext.initContext();
		
		Form form = new Form("form");
		TextField name = new TextField("name");
		PasswordField pass = new PasswordField("pass");
		IntegerField age = new IntegerField("age");
		DateField date = new DateFieldYYYYMMDD("date");
		form.add(name);
		form.add(pass);
		form.add(age);
		form.add(date);
		
		SampleDto2 dto = new SampleDto2();
		dto.setName("Taro");
		dto.setPass("secret");
		dto.setAge(15);
		dto.setDate(new SimpleDateFormat("yyyy/MM/dd").parse("2009/01/01"));
		S2ClickUtils.copyObjectToForm(dto, form, true);
		
		assertEquals("Taro", name.getValue());
		assertEquals("secret", pass.getValue());
		assertEquals(15, age.getInteger().intValue());
		assertEquals("2009/01/01", date.getValue());
	}
	
	/**
	 * publicフィールドを使用したDTO
	 */
	public static class SampleDto1 {
		public String name;
		public String pass;
		public Integer age;
		public Date date;
	}
	
	/**
	 * アクセサメソッドを使用したDTO
	 */
	public static class SampleDto2 {
		private String name;
		private String pass;
		private Integer age;
		private Date date;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPass() {
			return pass;
		}
		public void setPass(String pass) {
			this.pass = pass;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
	}
	
	public void testConvertToHidden(){
		Form form = new Form("form");
		
		TextField text = new TextField("text");
		text.setValue("テキストフィールド");
		form.add(text);
		
		Checkbox checkbox = new Checkbox("checkbox");
		checkbox.setChecked(true);
		form.add(checkbox);
		
		CheckList checkList = new CheckList("checkList");
		checkList.add("A");
		checkList.add("B");
		checkList.add("C");
		
		List<String> values = new ArrayList<String>();
		values.add("B");
		values.add("C");
		checkList.setSelectedValues(values);
		
		form.add(checkList);
		
		S2ClickUtils.convertToHidden(form);
		
		HiddenField hiddenText = (HiddenField) form.getField("text");
		assertEquals("テキストフィールド", hiddenText.getValue());
		
		HiddenField hiddenCheckbox = (HiddenField) form.getField("checkbox");
		assertEquals("true", hiddenCheckbox.getValue());
		
		HiddenList hiddenCheckList = (HiddenList) form.getField("checkList");
		assertEquals(2, hiddenCheckList.getValues().size());
		assertEquals("B", hiddenCheckList.getValues().get(0));
		assertEquals("C", hiddenCheckList.getValues().get(1));
	}

}
