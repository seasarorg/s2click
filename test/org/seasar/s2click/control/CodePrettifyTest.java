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

import org.apache.click.Page;
import org.seasar.s2click.S2ClickTestCase;

public class CodePrettifyTest extends S2ClickTestCase {

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
