/*
 * Copyright 2006-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
		suite.addTest(org.seasar.s2click.control.AllTests.suite());
		suite.addTest(org.seasar.s2click.filter.AllTests.suite());
		suite.addTest(org.seasar.s2click.util.AllTests.suite());
		suite.addTest(org.seasar.s2click.example.page.AllTests.suite());
		suite.addTest(org.seasar.s2click.example.service.AllTests.suite());
		suite.addTestSuite(S2ClickPageTest.class);
		suite.addTestSuite(PageClassLoaderTest.class);
		//$JUnit-END$
		return suite;
	}

}
