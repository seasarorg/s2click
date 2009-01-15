package org.seasar.s2click.control;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.seasar.s2click.control");
		//$JUnit-BEGIN$
		suite.addTestSuite(S2ClickFormTest.class);
		suite.addTestSuite(DateFieldYYYYMMDDTest.class);
		suite.addTestSuite(PublicFieldColumnTest.class);
		suite.addTestSuite(ToolTipTest.class);
		suite.addTestSuite(CodePrettifyTest.class);
		suite.addTestSuite(AjaxButtonTest.class);
		suite.addTestSuite(AjaxLinkTest.class);
		suite.addTestSuite(AjaxSubmitTest.class);
		suite.addTestSuite(GreyboxButtonTest.class);
		suite.addTestSuite(GreyboxLinkTest.class);
		//$JUnit-END$
		return suite;
	}

}
