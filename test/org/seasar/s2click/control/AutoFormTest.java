package org.seasar.s2click.control;

import java.util.List;

import junit.framework.TestCase;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;

public class AutoFormTest extends TestCase {

	/**
	 * <code>onProcess()</code>から<code>init()</code>が呼び出されること。
	 */
	public void testOnProcess() {
//		@SuppressWarnings("unused")
//		AutoForm form = new AutoForm() {
//			private static final long serialVersionUID = 1L;
//			{
//				setFieldAutoRegisteration(true);
//				setJavaScriptValidation(false);
//			}
//			public TextField text1 = new TextField("text1");
//			public TextField text2 = new TextField("text2");
//			public Submit submit = new Submit("submit");
//		};
//		form.onProcess();
//		
//		List<?> fields = form.getFieldList();
//		assertEquals(2, fields.size());
//		assertEquals("text1", ((TextField) fields.get(0)).getName());
//		assertEquals("text2", ((TextField) fields.get(1)).getName());
//		
//		List<?> buttons = form.getButtonList();
//		assertEquals(1, buttons.size());
//		assertEquals("submit", ((Submit) buttons.get(0)).getName());
	}

	/**
	 * <code>isFieldAutoRegistration()</code>の初期値が<code>false</code>であること、
	 * <code>setFieldAutoRegisteration()</code>でセットした値が<code>isFieldAutoRegistration()</code>で取得できること。
	 */
	public void testSetFieldAutoRegisteration() {
		AutoForm form = new AutoForm(){
			private static final long serialVersionUID = 1L;
		};
		
		assertFalse(form.isFieldAutoRegistration());
		form.setFieldAutoRegisteration(true);
		assertTrue(form.isFieldAutoRegistration());
	}

	public void testAddNoJavaScriptValidateAction() {
//		AutoForm form = new AutoForm("form"){
//			private static final long serialVersionUID = 1L;
//		};
//		
//		form.add(new TextField("text", true));
//		form.setValidate(true);
//		form.setJavaScriptValidation(true);
//		form.addNoJavaScriptValidateAction("submit");
//		
//		HtmlStringBuffer buffer = new HtmlStringBuffer();
//		form.renderValidationJavaScript(buffer, form.getFieldList());
//		
//		System.out.println(buffer.toString());
	}

	/**
	 * フィールドの自動登録が行われること。
	 */
	public void testInit1() {
		@SuppressWarnings("unused")
		AutoForm form = new AutoForm() {
			private static final long serialVersionUID = 1L;
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
		assertEquals(2, fields.size());
		assertEquals("text1", ((TextField) fields.get(0)).getName());
		assertEquals("text2", ((TextField) fields.get(1)).getName());
		
		List<?> buttons = form.getButtonList();
		assertEquals(1, buttons.size());
		assertEquals("submit", ((Submit) buttons.get(0)).getName());
	}

	/**
	 * 
	 */
	public void testInit2() {
		@SuppressWarnings("unused")
		AutoForm form = new AutoForm() {
			private static final long serialVersionUID = 1L;
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
		assertTrue(fields.isEmpty());
		
		List<?> buttons = form.getButtonList();
		assertTrue(buttons.isEmpty());
	}
	
	public void testInit3() {
		@SuppressWarnings("unused")
		AutoForm form = new AutoForm() {
			private static final long serialVersionUID = 1L;
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
		assertEquals(3, fields.size());
		assertEquals("action", ((HiddenField) fields.get(0)).getName());
		assertEquals("text1", ((TextField) fields.get(1)).getName());
		assertEquals("text2", ((TextField) fields.get(2)).getName());
		
		List<?> buttons = form.getButtonList();
		assertEquals(1, buttons.size());
		assertEquals("submit", ((Submit) buttons.get(0)).getName());
	}
	
	public void testAddField1() {
		@SuppressWarnings("unused")
		AutoForm form = new AutoForm("form") {
			private static final long serialVersionUID = 1L;
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
		@SuppressWarnings("unused")
		AutoForm form = new AutoForm("form") {
			private static final long serialVersionUID = 1L;
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
	
	public void testStartTag() {
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
	}

}
