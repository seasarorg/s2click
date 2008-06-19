package org.seasar.s2click.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �y�[�W�N���X�ɂ��̃A�m�e�[�V������t�^���邱�ƂŁA
 * �C�ӂ̃p�X�Ƀy�[�W�N���X���}�b�s���O���邱�Ƃ��ł��܂��B
 * 
 * @author Naoki Takezoe
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {
	
	/**
	 * �y�[�W�̃p�X
	 * @return�@�y�[�W�̃p�X
	 */
	String value() default "";
	
}
