package org.seasar.s2click.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link S2ClickServiceTestCase}を継承したテストケースのテストメソッドに付与することで、
 * 指定したテーブルの内容とExcelの内容を比較することができます。
 * <p>
 * また、テストケース実行時に期待値を記述したExcelが存在しない場合は自動的に生成されます。
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
	
}
