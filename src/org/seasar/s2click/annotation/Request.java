package org.seasar.s2click.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ���N�G�X�g�p�����[�^���o�C���h����y�[�W�N���X��public�t�B�[���h�ɕt�^���܂��B
 * 
 * @author Naoki Takezoe
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Request {

	String value() default "";
	
}
