package org.seasar.s2click.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.seasar.s2click.util");
		//$JUnit-BEGIN$
		suite.addTestSuite(S2ClickFormatTest.class);
		suite.addTestSuite(S2ClickUtilsTest.class);
		//$JUnit-END$
		return suite;
	}

}
