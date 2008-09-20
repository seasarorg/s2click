package org.seasar.s2click.control;

import java.util.List;

import net.sf.click.MockContext;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;
import net.sf.click.util.HtmlStringBuffer;

import org.seasar.s2click.S2ClickTestCase;

public class S2ClickFormTest extends S2ClickTestCase {

	/**
	 * <code>onProcess()</code>から<code>init()</code>が呼び出されること。
	 */
	public void testOnProcess() {
		MockContext.initContext();
		
		@SuppressWarnings({"unused", "serial"})
		S2ClickForm form = new S2ClickForm("form") {
			{
				setFieldAutoRegisteration(true);
				setJavaScriptValidation(false);
			}
			public TextField text1 = new TextField("text1");
			public TextField text2 = new TextField("text2");
			public Submit submit = new Submit("submit");
		};
		form.onProcess();
		
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
			}
			public TextField text1 = new TextField("text1");
			public TextField text2 = new TextField("text2");
			public Submit submit = new Submit("submit");
		};
		form.init();
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
			}
			public TextField text1 = new TextField("text1");
			public TextField text2 = new TextField("text2");
			public Submit submit = new Submit("submit");
		};
		form.init();
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
			}
			public TextField text1 = new TextField("text1");
			public TextField text2 = new TextField("text2");
			public Submit submit = new Submit("submit");
		};
		form.init();
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
			}
			public Submit submit = new Submit("submit");
		};
		form.init();
		
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
			}
			public Submit submit = new Submit("submit");
		};
		form.init();
		
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
//		form.init();
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
		
		form.init();
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
		
		form.init();
		
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
