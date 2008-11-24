package org.seasar.s2click.control;

import java.util.Map;

import junit.framework.TestCase;
import net.sf.click.MockContext;
import net.sf.click.MockRequest;

import org.seasar.s2click.util.AjaxUtils;

public class AjaxButtonTest extends TestCase {

	public void testGetHtmlImports() {
		MockRequest request = new MockRequest(){
			@Override public String getContextPath(){
				return "/sample";
			}
		};
		
		MockContext.initContext(request);
		AjaxButton button = new AjaxButton();
		
		assertEquals("<script type=\"text/javascript\" src=\"/sample/js/prototype.js\"></script>\n",
				button.getHtmlImports());
	}

	public void testGetAjaxHandlers() {
		AjaxButton button = new AjaxButton();
		button.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");
		button.addAjaxHandler(AjaxUtils.ON_EXCEPTION, "processException");
		
		Map<String, String> handlers = button.getAjaxHandlers();
		assertEquals(2, handlers.size());
		assertEquals("processCompleted", handlers.get(AjaxUtils.ON_COMPLETE));
		assertEquals("processException", handlers.get(AjaxUtils.ON_EXCEPTION));
	}

	public void testGetElementId() {
		AjaxButton button = new AjaxButton();
		button.setElementId("element-id");
		assertEquals("element-id", button.getElementId());
	}

	// TODO テストを未実装
//	public void testGetOnClick1() {
//		MockRequest request = new MockRequest(){
//			@Override public String getRequestURI(){
//				return "http://localhost:8080/sample/sample.htm";
//			}
//		};
//
//		MockContext.initContext(request);
//		
//		AjaxButton button = new AjaxButton("ajaxButton");
//		button.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");
//		
//		System.out.println(button.getOnClick());
//	}

}
