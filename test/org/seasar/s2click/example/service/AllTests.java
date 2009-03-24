package org.seasar.s2click.example.service;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.seasar.s2click.example.service");
		//$JUnit-BEGIN$
		suite.addTestSuite(MessageServiceTest.class);
		//$JUnit-END$
		return suite;
	}

}
