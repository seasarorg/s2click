package org.seasar.s2click.page;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.seasar.s2click.page");
		//$JUnit-BEGIN$
		suite.addTestSuite(AbstractDownloadPageTest.class);
		suite.addTestSuite(AbstractJSONPageTest.class);
		//$JUnit-END$
		return suite;
	}

}
