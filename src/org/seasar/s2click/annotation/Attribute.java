package org.seasar.s2click.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.apache.click.control.Field;

/**
 * {@link Attributes}アノテーションと組み合わせることで
 * フォームクラスのpublicフィールドとして宣言した{@link Field}コントロールの
 * 属性を指定することのできるアノテーションです。
 *
 * @author Naoki Takezoe
 * @since 1.0.5
 * @see Attributes
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Attribute {

	String name();

	String value();

}
