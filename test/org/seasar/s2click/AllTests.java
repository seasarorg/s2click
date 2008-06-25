package org.seasar.s2click;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.seasar.s2click");
		//$JUnit-BEGIN$
		suite.addTest(org.seasar.s2click.control.AllTests.suite());
		suite.addTest(org.seasar.s2click.util.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
