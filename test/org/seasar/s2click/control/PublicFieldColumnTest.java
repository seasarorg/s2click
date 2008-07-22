package org.seasar.s2click.control;

import junit.framework.TestCase;

public class PublicFieldColumnTest extends TestCase {

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
