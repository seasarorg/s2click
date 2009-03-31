package org.seasar.s2click;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 
 * @author Naoki Takezoe
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Assert {
	
	Table[] tables();
	
	public @interface Table {
		String name();
		String[] includeColumns() default {};
		String[] excludeColumns() default {};
	}
	
}
