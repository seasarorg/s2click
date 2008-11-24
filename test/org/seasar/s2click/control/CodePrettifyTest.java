package org.seasar.s2click.control;

import junit.framework.TestCase;
import net.sf.click.Page;

public class CodePrettifyTest extends TestCase {

	public void testToString() {
		Page page = new Page();
		
		CodePrettify codePrettify1 = new CodePrettify("codePrettify1");
		codePrettify1.setCode("<html>");
		codePrettify1.setLang(CodePrettify.LANG_HTML);
		page.addControl(codePrettify1);
		
		CodePrettify codePrettify2 = new CodePrettify("codePrettify2");
		codePrettify2.setCode("<html>");
		codePrettify2.setLang(CodePrettify.LANG_HTML);
		page.addControl(codePrettify2);
		
		assertEquals("<pre class=\"prettyprint lang-html\">&lt;html&gt;</pre><script type=\"text/javascript\">\n" + 
				"window.onload = function(){ prettyPrint(); }\n"+
				"</script>\n", codePrettify1.toString());
		
		// prettyPrint()関数の呼び出しは1回目だけしかレンダリングされないこと
		assertEquals("<pre class=\"prettyprint lang-html\">&lt;html&gt;</pre>", codePrettify2.toString());
	}
}
