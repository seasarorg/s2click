package org.seasar.s2click.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ページクラスにこのアノテーションを付与することで、
 * 任意のパスにページクラスをマッピングすることができます。
 * 
 * @author Naoki Takezoe
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {
	
	/**
	 * ページのパス
	 * @return　ページのパス
	 */
	String value() default "";
	
}
