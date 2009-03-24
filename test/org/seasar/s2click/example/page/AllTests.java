package org.seasar.s2click.example.page;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.seasar.s2click.example.page");
		//$JUnit-BEGIN$
		suite.addTestSuite(AddPageTest.class);
		suite.addTestSuite(JdbcPageTest.class);
		//$JUnit-END$
		return suite;
	}

}
