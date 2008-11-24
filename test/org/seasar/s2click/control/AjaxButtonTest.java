package org.seasar.s2click.control;

import java.util.Map;

import junit.framework.TestCase;
import net.sf.click.MockContext;
import net.sf.click.MockRequest;

import org.seasar.s2click.util.AjaxUtils;

public class AjaxButtonTest extends TestCase {

	public void testGetHtmlImports() {
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		
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

	public void testGetOnClick1() {
		MockRequest request = new MockRequest();
		request.setRequestURI("http://localhost:8080/sample/sample.htm");

		MockContext.initContext(request);
		
		AjaxButton button = new AjaxButton("ajaxButton");
		button.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");
		
		// elementIdを指定しなかった場合はAjax.Requestで送信
		assertEquals("new Ajax.Request('http://localhost:8080/sample/sample.htm?actionButton=ajaxButton', "+
				"{method: 'post', onComplete: processCompleted})",
				button.getOnClick());
	}

	public void testGetOnClick2() {
		MockRequest request = new MockRequest();
		request.setRequestURI("http://localhost:8080/sample/sample.htm");

		MockContext.initContext(request);
		
		AjaxButton button = new AjaxButton("ajaxButton");
		button.setElementId("targetElement");
		
		// elementIdを指定した場合はAjax.Updaterで送信
		assertEquals("new Ajax.Updater('targetElement', " +
				"'http://localhost:8080/sample/sample.htm?actionButton=ajaxButton', " +
				"{method: 'post'})",
				button.getOnClick());
	}
}
