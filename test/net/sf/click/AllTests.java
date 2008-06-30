package net.sf.click;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for net.sf.click");
		//$JUnit-BEGIN$
		suite.addTestSuite(PageClassLoaderTest.class);
		//$JUnit-END$
		return suite;
	}

}
