package org.seasar.s2click.control;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.click.control.Field;
import org.apache.click.control.HiddenField;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.IntegerField;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.factory.aspect.MetaAnnotationAspectDefBuilder;
import org.seasar.framework.util.tiger.AnnotationUtil;

public class EntityForm extends S2ClickForm {

	protected Class<?> entityClass;
	protected EntityFormMode mode;

	public EntityForm(Class<?> entityClass, EntityFormMode mode){
		this.entityClass = entityClass;
		this.mode = mode;
	}

	@Override
	public void onInit() {
		super.onInit();

		// フィールドの作成
		BeanDesc beanDesc = BeanDescFactory.getBeanDesc(entityClass);
		int size = beanDesc.getPropertyDescSize();
		for(int i=0; i < size; i++){
			PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
			Field field = createField(propertyDesc);
			if(field != null){
				add(field);
			}
		}

		// ボタンの作成
		if(mode == EntityFormMode.REGISTER){

		} else if(mode == EntityFormMode.EDIT){

		} else if(mode == EntityFormMode.DELETE){

		}
	}

	protected Field createField(PropertyDesc propertyDesc){
		String name = propertyDesc.getPropertyName();
		Class<?> type = propertyDesc.getPropertyType();
		Field field = null;

		Id id = getAnnotation(propertyDesc, Id.class);
		if(id != null){
			GeneratedValue generatedValue = getAnnotation(propertyDesc, GeneratedValue.class);
			if(generatedValue != null){
				if(mode == EntityFormMode.EDIT || mode == EntityFormMode.DELETE){
					field = new HiddenField(name, "");
					return field;
				}
				return null;
			}
		}

		if(type == String.class){
			field = new TextField();
		} else if(type == Integer.class){
			field = new IntegerField();
		}

		if(field != null){
			field.setName(name);

			Column column = getAnnotation(propertyDesc, Column.class);
			if(column != null){
				if(column.nullable() == false){
					field.setRequired(true);
				}
			}
		}

		return field;
	}

	protected <T extends Annotation> T getAnnotation(PropertyDesc propertyDesc, Class<T> type){
		java.lang.reflect.Field field = propertyDesc.getField();
		if(field != null){
			T ann = field.getAnnotation(type);
			if(ann != null){
				return ann;
			}
		}
		Method getter = propertyDesc.getReadMethod();
		if(getter != null){
			T ann = getter.getAnnotation(type);
			if(ann != null){
				return ann;
			}
		}
		Method setter = propertyDesc.getReadMethod();
		if(setter != null){
			T ann = setter.getAnnotation(type);
			if(ann != null){
				return ann;
			}
		}
		return null;
	}

	public static enum EntityFormMode {
		REGISTER, EDIT, DELETE
	}

}
