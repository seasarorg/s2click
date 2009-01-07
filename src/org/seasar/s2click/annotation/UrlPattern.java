package org.seasar.s2click.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SAStrutsと同様のURLパターンを設定するためのアノテーションです。
 * ページクラスに対して付与します。
 * 
 * @author Naoki Takezoe
 * @since 0.4.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UrlPattern {

	public String value() default "";
	
}
