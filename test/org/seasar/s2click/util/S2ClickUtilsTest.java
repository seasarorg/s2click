package org.seasar.s2click.util;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.s2click.S2ClickConfig;

public class S2ClickUtilsTest extends S2TestCase {

	@Override
	protected String getRootDicon() throws Throwable {
		return "s2click.dicon";
	}

	public void testUrlEncode() {
		assertEquals("ABCDEFG", 
				S2ClickUtils.urlEncode("ABCDEFG"));
		assertEquals("Click+Framework", 
				S2ClickUtils.urlEncode("Click Framework"));
		assertEquals("%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A", 
				S2ClickUtils.urlEncode("‚ ‚¢‚¤‚¦‚¨"));
	}

	public void testConvertNbsp() {
		assertEquals(" &nbsp;&nbsp;&nbsp;", S2ClickUtils.convertNbsp("    "));
		assertEquals("a &nbsp;&nbsp;&nbsp;b", S2ClickUtils.convertNbsp("a    b"));
		assertEquals("a b", S2ClickUtils.convertNbsp("a b"));
		assertEquals("a \n b", S2ClickUtils.convertNbsp("a \n b"));
		assertEquals("a \n &nbsp;b", S2ClickUtils.convertNbsp("a \n  b"));
	}

	public void testGetComponent() {
		S2ClickConfig config = S2ClickUtils.getComponent(S2ClickConfig.class);
		assertNotNull(config);
	}

}
