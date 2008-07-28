package org.seasar.s2click.util;

import net.sf.click.control.Form;
import net.sf.click.control.PasswordField;
import net.sf.click.control.TextField;
import net.sf.click.extras.control.IntegerField;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.s2click.S2ClickConfig;

public class S2ClickUtilsTest extends S2TestCase {

	@Override
	protected String getRootDicon() throws Throwable {
		return "s2click.dicon";
	}

	public void testUrlEncode() {
		assertEquals("ABCDEFG", 
				S2ClickUtils.urlEncode("ABCDEFG"));
		assertEquals("Click+Framework", 
				S2ClickUtils.urlEncode("Click Framework"));
		assertEquals("%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A", 
				S2ClickUtils.urlEncode("あいうえお"));
	}

	public void testConvertNbsp() {
		assertEquals(" &nbsp;&nbsp;&nbsp;", S2ClickUtils.convertNbsp("    "));
		assertEquals("a &nbsp;&nbsp;&nbsp;b", S2ClickUtils.convertNbsp("a    b"));
		assertEquals("a b", S2ClickUtils.convertNbsp("a b"));
		assertEquals("a \n b", S2ClickUtils.convertNbsp("a \n b"));
		assertEquals("a \n &nbsp;b", S2ClickUtils.convertNbsp("a \n  b"));
	}

	public void testGetComponent() {
		S2ClickConfig config = S2ClickUtils.getComponent(S2ClickConfig.class);
		assertNotNull(config);
	}
	
	/**
	 * publicフィールドを使用したDTOの場合の<code>copyFormToObject()</code>のテスト。
	 */
	public void testCopyFormToObject1(){
		Form form = new Form("form");
		
		TextField name = new TextField("name");
		name.setValue("Name");
		
		PasswordField pass = new PasswordField("pass");
		pass.setValue("password");
		
		IntegerField age = new IntegerField("age");
		age.setInteger(20);
		
		form.add(name);
		form.add(pass);
		form.add(age);
		
		SampleDto1 dto = new SampleDto1();
		S2ClickUtils.copyFormToObject(form, dto, true);
		
		assertEquals("Name", dto.name);
		assertEquals("password", dto.pass);
		assertEquals(20, dto.age.intValue());
	}
	
	/**
	 * アクセサメソッドを使用したDTOの場合の<code>copyFormToObject()</code>のテスト。
	 */
	public void testCopyFormToObject2(){
		Form form = new Form("form");
		
		TextField name = new TextField("name");
		name.setValue("Name");
		
		PasswordField pass = new PasswordField("pass");
		pass.setValue("password");
		
		IntegerField age = new IntegerField("age");
		age.setInteger(20);
		
		form.add(name);
		form.add(pass);
		form.add(age);
		
		SampleDto2 dto = new SampleDto2();
		S2ClickUtils.copyFormToObject(form, dto, true);
		
		assertEquals("Name", dto.getName());
		assertEquals("password", dto.getPass());
		assertEquals(20, dto.getAge().intValue());
	}
	
	/**
	 * publicフィールドを使用したDTOの場合の<code>copyObjecyToForm()</code>のテスト。
	 */
	public void testCopyObjectToForm1(){
		Form form = new Form("form");
		TextField name = new TextField("name");
		PasswordField pass = new PasswordField("pass");
		IntegerField age = new IntegerField("age");
		form.add(name);
		form.add(pass);
		form.add(age);
		
		SampleDto1 dto = new SampleDto1();
		dto.name = "Taro";
		dto.pass = "secret";
		dto.age = 15;
		S2ClickUtils.copyObjectToForm(dto, form, true);
		
		assertEquals("Taro", name.getValue());
		assertEquals("secret", pass.getValue());
		assertEquals(15, age.getInteger().intValue());
	}
	
	/**
	 * アクセサメソッドを使用したDTOの場合の<code>copyObjecyToForm()</code>のテスト。
	 */
	public void testCopyObjectToForm2(){
		Form form = new Form("form");
		TextField name = new TextField("name");
		PasswordField pass = new PasswordField("pass");
		IntegerField age = new IntegerField("age");
		form.add(name);
		form.add(pass);
		form.add(age);
		
		SampleDto2 dto = new SampleDto2();
		dto.setName("Taro");
		dto.setPass("secret");
		dto.setAge(15);
		S2ClickUtils.copyObjectToForm(dto, form, true);
		
		assertEquals("Taro", name.getValue());
		assertEquals("secret", pass.getValue());
		assertEquals(15, age.getInteger().intValue());
	}
	
	/**
	 * publicフィールドを使用したDTO
	 */
	public static class SampleDto1 {
		public String name;
		public String pass;
		public Integer age;
	}
	
	/**
	 * アクセサメソッドを使用したDTO
	 */
	public static class SampleDto2 {
		private String name;
		private String pass;
		private Integer age;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPass() {
			return pass;
		}
		public void setPass(String pass) {
			this.pass = pass;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
	}

}
