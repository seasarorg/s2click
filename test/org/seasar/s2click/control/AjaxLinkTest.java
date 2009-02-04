package org.seasar.s2click.control;

import java.util.Map;

import junit.framework.TestCase;
import net.sf.click.MockContext;
import net.sf.click.MockRequest;

import org.seasar.s2click.util.AjaxUtils;

public class AjaxLinkTest extends TestCase {

	public void testGetHtmlImports() {
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		
		MockContext.initContext(request);
		
		AjaxLink link = new AjaxLink();
		assertEquals("<script type=\"text/javascript\" src=\"/sample/resources/prototype.js\"></script>\n", 
				link.getHtmlImports());
	}

	public void testGetAjaxHandlers() {
		AjaxLink link = new AjaxLink();
		link.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");
		link.addAjaxHandler(AjaxUtils.ON_EXCEPTION, "processException");
		
		Map<String, String> handlers = link.getAjaxHandlers();
		assertEquals(2, handlers.size());
		assertEquals("processCompleted", handlers.get(AjaxUtils.ON_COMPLETE));
		assertEquals("processException", handlers.get(AjaxUtils.ON_EXCEPTION));

	}

	public void testGetHref() {
		AjaxLink link = new AjaxLink("ajaxLink");
		assertEquals("javascript:void(0)", link.getHref());
	}

	public void testGetElementId() {
		AjaxLink link = new AjaxLink();
		link.setElementId("element-id");
		assertEquals("element-id", link.getElementId());
	}

	public void testToString1() {
		MockRequest request = new MockRequest();
		request.setRequestURI("http://localhost:8080/sample/sample.htm");

		MockContext.initContext(request);
		
		AjaxLink link = new AjaxLink("ajaxLink", "Ajax.Requestのテスト");
		link.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");
		
		// elementIdを指定しなかった場合はAjax.Requestで送信
		assertEquals("<a href=\"javascript:void(0)\" " + 
				"onclick=\"new Ajax.Request('http://localhost:8080/sample/sample.htm?actionLink=ajaxLink', " + 
				"{method: 'post', onComplete: processCompleted})\">Ajax.Requestのテスト</a>", 
				link.toString());
	}

	public void testToString2() {
		MockRequest request = new MockRequest();
		request.setRequestURI("http://localhost:8080/sample/sample.htm");

		MockContext.initContext(request);
		
		AjaxLink link = new AjaxLink("ajaxLink", "Ajax.Requestのテスト");
		link.setElementId("targetElement");
		
		// elementIdを指定した場合はAjax.Updaterで送信
		assertEquals("<a href=\"javascript:void(0)\" " + 
				"onclick=\"new Ajax.Updater('targetElement', 'http://localhost:8080/sample/sample.htm?actionLink=ajaxLink', " + 
				"{method: 'post'})\">Ajax.Requestのテスト</a>", 
				link.toString());
	}
}
