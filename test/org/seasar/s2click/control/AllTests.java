/*
 * Copyright 2006-2011 the Seasar Foundation and the Others.
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
		suite.addTestSuite(PaginateListTest.class);
		//$JUnit-END$
		return suite;
	}

}
