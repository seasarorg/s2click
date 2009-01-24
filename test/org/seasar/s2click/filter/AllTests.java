package org.seasar.s2click.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.seasar.s2click.filter");
		//$JUnit-BEGIN$
		suite.addTestSuite(UrlPatternFilterTest.class);
		suite.addTestSuite(UrlPatternManagerTest.class);
		//$JUnit-END$
		return suite;
	}

}
