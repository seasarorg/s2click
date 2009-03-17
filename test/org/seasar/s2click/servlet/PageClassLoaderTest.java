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
package org.seasar.s2click.servlet;

import java.util.List;

import org.seasar.s2click.S2ClickTestCase;

public class PageClassLoaderTest extends S2ClickTestCase {

	public void testPageClassLoader() {
		PageClassLoader loader = new PageClassLoader("sample");
		assertEquals("sample", getField(loader, "rootPackage"));
	}

	public void testProcessClass() {
		PageClassLoader loader = new PageClassLoader("sample");
		loader.processClass("sample", "IndexPage");
		
		@SuppressWarnings("unchecked")
		List<String> classes = (List<String>) getField(loader, "classes");
		assertEquals(1, classes.size());
		assertEquals("sample.IndexPage", classes.get(0));
	}

//	public void testGetPageClasses() {
//		PageClassLoader loader = new PageClassLoader(
//				"org.seasar.s2click.example.page");
//		List<String> classes = loader.getPageClasses();
//		
//		assertEquals(11, classes.size());
//		
//		for(String className: classes){
//			assertTrue(className.endsWith("Page"));
//		}
//	}

}
