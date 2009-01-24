package org.seasar.s2click.example.page;

import java.text.SimpleDateFormat;
import java.util.List;

import junit.framework.TestCase;
import net.sf.click.MockContext;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Submit;

public class ControlsPageTest extends TestCase {
	
	public void testDoSubmit() throws Exception {
		MockContext.initContext();
		
		ControlsPage page = new ControlsPage();
		page.form.onInit();
		page.form.dateField.setValue("2009/01/01");
		page.form.fckEditor.setValue("<html>");
		
		page.doSubmit();
		
		// DateFieldYYYYMMDDがHiddenFieldに変換されていること
		HiddenField dateField = (HiddenField) page.form.getField("date");
		assertEquals(new SimpleDateFormat("yyyy/MM/dd").parseObject("2009/01/01").toString(), 
				dateField.getValue());
		
		// FCKEditorがHiddenFieldに変換されていること
		HiddenField fckEditor = (HiddenField) page.form.getField("fckEditor");
		assertEquals("<html>", fckEditor.getValue());
		
		// 送信ボタンが削除されて戻るボタンが追加されていること
		List<?> buttons = page.form.getButtonList();
		assertEquals(1, buttons.size());
		assertEquals("戻る", ((Submit) buttons.get(0)).getLabel());
	}

}
