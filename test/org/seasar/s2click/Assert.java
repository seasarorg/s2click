package org.seasar.s2click;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link S2ClickServiceTestCase}を継承したテストケースのテストメソッドに付与することで、
 * 指定したテーブルの内容とExcelの内容を比較することができます。
 * <p>
 * また、テストケース実行時にExcelが存在しない場合は自動的に生成されます。
 * 
 * @author Naoki Takezoe
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Assert {
	
	/**
	 * 比較対象のテーブル。
	 */
	Table[] tables();
	
	/**
	 * {@link Assert}アノテーションで比較するテーブルの情報を指定するアノテーションです。
	 * 
	 * @author Naoki Takezoe
	 */
	public @interface Table {
		
		/**
		 * テーブル名。
		 */
		String name();
		
		/**
		 * 比較対象のカラム名。
		 * この属性を省略し、なおかつ<code>excludeColumns</code>を指定しなかった場合はすべてのカラムが比較対象になります。
		 */
		String[] includeColumns() default {};
		
		/**
		 * 比較対象外のカラム名。
		 * この属性を省略し、なおかつ<code>includeColumns</code>を指定しなかった場合はすべてのカラムが比較対象になります。
		 */
		String[] excludeColumns() default {};
	}
	
}
