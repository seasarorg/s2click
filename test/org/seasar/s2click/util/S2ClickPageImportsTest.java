package org.seasar.s2click.util;

import junit.framework.TestCase;
import net.sf.click.MockContext;
import net.sf.click.MockRequest;
import net.sf.click.Page;
import net.sf.click.control.Form;

import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.control.AjaxSubmit;

public class S2ClickPageImportsTest extends TestCase {

	public void testProcessControlControl() {
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		MockContext.initContext(request);
		
		TestS2ClickPageImports imports = new TestS2ClickPageImports(new S2ClickPage(){});
		
		Form form = new Form("form");
		form.add(new AjaxSubmit("submit"));
		
		imports.processControl(form);
		
		// ボタンのgetHtmlImports()の内容が出力されていること
		assertTrue(imports.sb.toString().endsWith(
				"<script type=\"text/javascript\" src=\"/sample/js/prototype.js\"></script>\n"));
	}
	
	private class TestS2ClickPageImports extends S2ClickPageImports {
		
		private StringBuilder sb = new StringBuilder();
		
		public TestS2ClickPageImports(Page page) {
			super(page);
		}

		@Override
		protected void processLine(String value) {
			if(value != null){
				sb.append(value);
			}
		}
	}

}
