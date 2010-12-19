package org.seasar.s2click.servlet;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ResourceServletTest.class);
		//$JUnit-END$
		return suite;
	}

}
