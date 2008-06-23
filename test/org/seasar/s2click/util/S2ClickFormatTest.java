package org.seasar.s2click.util;

import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.unit.S2TestCase;

public class S2ClickFormatTest extends S2TestCase {

	@Override
	protected String getRootDicon() throws Throwable {
		return "s2click.dicon";
	}
	
	public void testUrl() {
		S2ClickFormat format = new S2ClickFormat();
		assertEquals("ABCDEFG", format.url("ABCDEFG"));
		assertEquals("Click+Framework", format.url("Click Framework"));
		assertEquals("%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A", 
				format.url("‚ ‚¢‚¤‚¦‚¨"));
	}

	public void testJson() {
		S2ClickFormat format = new S2ClickFormat();
		Map<String, String> map = new HashMap<String, String>();
		map.put("Language", "Java");
		map.put("Framework", "S2Click");
		map.put("IDE", "Eclipse");
		
		assertEquals("{\"IDE\":\"Eclipse\",\"Framework\":\"S2Click\",\"Language\":\"Java\"}", 
				format.json(map));
	}

	public void testNbsp() {
		S2ClickFormat format = new S2ClickFormat();
		assertEquals(" &nbsp;&nbsp;&nbsp;", format.nbsp("    "));
		assertEquals("a &nbsp;&nbsp;&nbsp;b", format.nbsp("a    b"));
		assertEquals("a b", format.nbsp("a b"));
		assertEquals("a \n b", format.nbsp("a \n b"));
		assertEquals("a \n &nbsp;b", format.nbsp("a \n  b"));
	}

}
