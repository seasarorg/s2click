package org.seasar.s2click;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * S2Clickのすべてのテストケースを実行します。
 * 
 * @author Naoki Takezoe
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.seasar.s2click");
		//$JUnit-BEGIN$
		suite.addTest(net.sf.click.AllTests.suite());
		suite.addTest(org.seasar.s2click.control.AllTests.suite());
		suite.addTest(org.seasar.s2click.util.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
