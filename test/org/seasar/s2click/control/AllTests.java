package org.seasar.s2click.control;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.seasar.s2click.control");
		//$JUnit-BEGIN$
		suite.addTestSuite(ConfirmSubmitTest.class);
		suite.addTestSuite(AutoFormTest.class);
		suite.addTestSuite(DateFieldYYYYMMDDTest.class);
		suite.addTestSuite(PublicFieldColumnTest.class);
		suite.addTestSuite(ToolTipTest.class);
		//$JUnit-END$
		return suite;
	}

}
