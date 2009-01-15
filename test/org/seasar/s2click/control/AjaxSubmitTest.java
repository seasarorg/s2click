package org.seasar.s2click.control;

import java.util.Map;

import org.seasar.s2click.util.AjaxUtils;

import net.sf.click.MockContext;
import net.sf.click.MockRequest;
import junit.framework.TestCase;

public class AjaxSubmitTest extends TestCase {

	public void testGetHtmlImports() {
		MockRequest request = new MockRequest();
		request.setContextPath("/sample");
		
		MockContext.initContext(request);
		
		AjaxSubmit submit = new AjaxSubmit();
		assertEquals("<script type=\"text/javascript\" src=\"/sample/js/prototype.js\"></script>\n", 
				submit.getHtmlImports());
	}

	public void testGetAjaxHandlers() {
		AjaxSubmit submit = new AjaxSubmit();
		submit.addAjaxHandler(AjaxUtils.ON_COMPLETE, "processCompleted");
		submit.addAjaxHandler(AjaxUtils.ON_EXCEPTION, "processException");
		
		Map<String, String> handlers = submit.getAjaxHandlers();
		assertEquals(2, handlers.size());
		assertEquals("processCompleted", handlers.get(AjaxUtils.ON_COMPLETE));
		assertEquals("processException", handlers.get(AjaxUtils.ON_EXCEPTION));
	}

	public void testToString() {
		MockRequest request = new MockRequest();
		request.setRequestURI("http://localhost:8080/sample/sample.htm");

		MockContext.initContext(request);
		
		@SuppressWarnings("serial")
		S2ClickForm form = new S2ClickForm("form"){};
		
		AjaxSubmit submit = new AjaxSubmit("submit");
		form.add(submit);
		
		assertEquals("<input type=\"submit\" name=\"submit\" id=\"form_submit\" value=\"Submit\"/>" +
				"<script type=\"text/javascript\">\n" +
				"$('form').onsubmit = function(){\n" +
				"  this.request({ method: 'post', }); return false;}\n" +
				"</script>\n", submit.toString());
	}

}
