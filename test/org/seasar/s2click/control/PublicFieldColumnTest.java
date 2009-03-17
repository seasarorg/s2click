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
package org.seasar.s2click.control;


public class PublicFieldColumnTest extends S2ClickControlTestCase {

	/**
	 * {@link PublicFieldColumn#getProperty(String, Object)}でpublicフィールドの値を取得できること。
	 */
	public void testGetPropertyStringObject() {
		PublicFieldColumn column = new PublicFieldColumn();
		
		String name = (String) column.getProperty("name", new SampleBean());
		assertEquals("Naoki Takezoe", name);
		
		String project = (String) column.getProperty("project.name", new SampleBean());
		assertEquals("Amateras", project);
	}
	
	public static class SampleBean {
		public String name = "Naoki Takezoe";
		public Project project = new Project();
	}
	
	public static class Project {
		public String name = "Amateras";
	}

}
