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

import java.util.List;

import org.apache.click.MockContext;
import org.apache.click.Page;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.util.HtmlStringBuffer;
import org.seasar.s2click.test.S2ClickTestCase;

public class S2ClickFormTest extends S2ClickTestCase {

	/**
	 * <code>isFieldAutoRegistration()</code>の初期値が<code>false</code>であること、
	 * <code>setFieldAutoRegisteration()</code>でセットした値が<code>isFieldAutoRegistration()</code>で取得できること。
	 */
	public void testSetFieldAutoRegisteration() {
		@SuppressWarnings("serial")
		S2ClickForm form = new S2ClickForm(){ };

		assertFalse(form.isFieldAutoRegistration());
		form.setFieldAutoRegisteration(true);
		assertTrue(form.isFieldAutoRegistration());
	}

	public void testAddNoJavaScriptValidateAction() {
		MockContext.initContext();

		S2ClickForm form = new S2ClickForm("form"){
			private static final long serialVersionUID = 1L;
		};

		form.add(new TextField("text", true));
		form.setValidate(true);
		form.setJavaScriptValidation(true);
		form.addNoJavaScriptValidateAction("submit");

		HtmlStringBuffer buffer = new HtmlStringBuffer();
		form.renderValidationJavaScript(buffer, form.getFieldList());

		assertEquals(load("S2ClickFormTest_testAddNoJavaScriptValidateAction.js"),
				buffer.toString());
	}

	/**
	 * フィールドの自動登録が行われること。
	 */
	public void testInit1() {
		@SuppressWarnings({"serial", "unused"})
		S2ClickForm form = new S2ClickForm("form") {
			{
				setFieldAutoRegisteration(true);
				setJavaScriptValidation(false);
				setParent(new Page());
			}
			public TextField text1 = new TextField("text1");
			public TextField text2 = new TextField("text2");
			public Submit submit = new Submit("submit");
		};

		form.onInit();

		List<?> fields = form.getFieldList();
		assertEquals(3, fields.size());
		assertEquals(S2ClickForm.FORM_NAME, ((HiddenField) fields.get(0)).getName());
		assertEquals("text1", ((TextField) fields.get(1)).getName());
		assertEquals("text2", ((TextField) fields.get(2)).getName());

		List<?> buttons = form.getButtonList();
		assertEquals(1, buttons.size());
		assertEquals("submit", ((Submit) buttons.get(0)).getName());
	}

	/**
	 *
	 */
	public void testInit2() {
		@SuppressWarnings({"serial", "unused"})
		S2ClickForm form = new S2ClickForm("form") {
			{
				setFieldAutoRegisteration(false);
				setJavaScriptValidation(false);
				setParent(new Page());
			}
			public TextField text1 = new TextField("text1");
			public TextField text2 = new TextField("text2");
			public Submit submit = new Submit("submit");
		};
		form.onInit();
		List<?> fields = form.getFieldList();
		assertEquals(1, fields.size());
		assertEquals(S2ClickForm.FORM_NAME, ((HiddenField) fields.get(0)).getName());

		List<?> buttons = form.getButtonList();
		assertTrue(buttons.isEmpty());
	}

	public void testInit3() {
		@SuppressWarnings({"serial", "unused"})
		S2ClickForm form = new S2ClickForm("form") {
			{
				setFieldAutoRegisteration(true);
				setJavaScriptValidation(true);
				setParent(new Page());
			}
			public TextField text1 = new TextField("text1");
			public TextField text2 = new TextField("text2");
			public Submit submit = new Submit("submit");
		};
		form.onInit();
		List<?> fields = form.getFieldList();
		assertEquals(4, fields.size());
		assertEquals(S2ClickForm.FORM_NAME, ((HiddenField) fields.get(0)).getName());
		assertEquals("action", ((HiddenField) fields.get(1)).getName());
		assertEquals("text1", ((TextField) fields.get(2)).getName());
		assertEquals("text2", ((TextField) fields.get(3)).getName());

		List<?> buttons = form.getButtonList();
		assertEquals(1, buttons.size());
		assertEquals("submit", ((Submit) buttons.get(0)).getName());
	}

	public void testAddField1() {
		@SuppressWarnings({"serial", "unused"})
		S2ClickForm form = new S2ClickForm("form") {
			{
				setFieldAutoRegisteration(true);
				setJavaScriptValidation(true);
				setParent(new Page());
			}
			public Submit submit = new Submit("submit");
		};
		form.onInit();

		List<?> buttons = form.getButtonList();
		assertEquals(1, buttons.size());
		Submit submit = (Submit) buttons.get(0);

		assertEquals("form.action.value='submit'", submit.getOnClick());
	}

	public void testAddField2() {
		@SuppressWarnings({"serial", "unused"})
		S2ClickForm form = new S2ClickForm("form") {
			{
				setFieldAutoRegisteration(true);
				setJavaScriptValidation(false);
				setParent(new Page());
			}
			public Submit submit = new Submit("submit");
		};
		form.onInit();

		List<?> buttons = form.getButtonList();
		assertEquals(1, buttons.size());
		Submit submit = (Submit) buttons.get(0);

		assertNull(submit.getOnClick());
	}

//	public void testStartTag() {
//		MockRequest request = new MockRequest();
//		MockContext.initContext(request);
//
//		@SuppressWarnings("unused")
//		AutoForm form = new AutoForm("form") {
//			private static final long serialVersionUID = 1L;
//			{
//				setFieldAutoRegisteration(true);
//				setJavaScriptValidation(false);
//			}
//		};
//		form.onInit();
//
//		System.out.println(form.startTag());
//
//		fail("Not yet implemented");
//	}

	public void testCopyTo(){
		@SuppressWarnings({"serial", "unused"})
		S2ClickForm form = new S2ClickForm("form"){
			{
				setFieldAutoRegisteration(true);
				setJavaScriptValidation(false);
			}
			public TextField name = new TextField("name");
		};

		form.onInit();
		TextField name = (TextField) form.getField("name");
		name.setValue("Naoki Takezoe");

		SampleBean bean = new SampleBean();
		form.copyTo(bean);

		assertEquals("Naoki Takezoe", bean.name);
	}

	public void testCopyFrom(){
		@SuppressWarnings({"serial", "unused"})
		S2ClickForm form = new S2ClickForm("form"){
			{
				setFieldAutoRegisteration(true);
				setJavaScriptValidation(false);
			}
			public TextField name = new TextField("name");
		};

		form.onInit();

		SampleBean bean = new SampleBean();
		bean.name = "Naoki Takezoe";

		form.copyFrom(bean);

		TextField name = (TextField) form.getField("name");
		assertEquals("Naoki Takezoe", name.getValue());
	}

	private class SampleBean {
		public String name;
	}

}
