package org.seasar.s2click.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.click.MockContext;
import ognl.OgnlException;

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
				format.url("あいうえお"));
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

	public void testOgnl() throws OgnlException {
		S2ClickFormat format = new S2ClickFormat();
		assertEquals("test", format.ognl("'test'"));
		assertEquals(S2ClickFormat.class,
				format.ognl("@org.seasar.s2click.util.S2ClickFormat@class"));
	}

	public void testHtml2(){
		S2ClickFormat format = new S2ClickFormat();
		assertEquals("&lt;br&gt;aaaa<br>\nbbbb<br>\r\ncccc",
				format.br("<br>aaaa\nbbbb\r\ncccc"));
	}

	public void testDate() throws Exception {
		MockContext.initContext();

		S2ClickFormat format = new S2ClickFormat();
		Date date = new SimpleDateFormat("yyyy/MM/dd").parse("2009/01/01");


		assertEquals("2009/01/01", format.date(date));
	}

	public void testDatetime() throws Exception {
		MockContext.initContext();

		S2ClickFormat format = new S2ClickFormat();
		Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2009/01/01 22:01:59");

		assertEquals("2009/01/01 22:01:59", format.datetime(date));
	}


	public void testMask(){
		S2ClickFormat format = new S2ClickFormat();
		assertEquals("****", format.mask("pass"));
	}

}
