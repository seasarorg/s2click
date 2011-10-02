package org.seasar.s2click.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 実験的なクラスやメソッドに付与するアノテーションです。
 * このアノテーションが付与されているクラスやメソッドは将来のリリースで削除されたり、
 * 大きく変更される可能性があります。
 *
 * @author Naoki Takezoe
 * @since 1.0.6
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface Experimental {

	String value() default "";

}
