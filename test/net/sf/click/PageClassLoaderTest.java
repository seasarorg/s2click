package net.sf.click;

import java.util.List;

import org.seasar.s2click.S2ClickTestCase;

public class PageClassLoaderTest extends S2ClickTestCase {

	public void testPageClassLoader() {
		PageClassLoader loader = new PageClassLoader("sample");
		assertEquals("sample", getField(loader, "rootPackage"));
	}

	public void testProcessClass() {
		PageClassLoader loader = new PageClassLoader("sample");
		loader.processClass("sample", "IndexPage");
		
		@SuppressWarnings("unchecked")
		List<String> classes = (List<String>) getField(loader, "classes");
		assertEquals(1, classes.size());
		assertEquals("sample.IndexPage", classes.get(0));
	}

	public void testGetPageClasses() {
		PageClassLoader loader = new PageClassLoader(
				"org.seasar.s2click.example.page");
		List<String> classes = loader.getPageClasses();
		
		assertEquals(12, classes.size());
		
		for(String className: classes){
			assertTrue(className.endsWith("Page"));
		}
	}

}
