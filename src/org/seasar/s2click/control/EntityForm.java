package org.seasar.s2click.control;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.click.control.Field;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.IntegerField;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

public class EntityForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;

	protected Class<?> entityClass;
	protected EntityFormMode mode;

	public EntityForm(String name, Class<?> entityClass, EntityFormMode mode){
		super(name);
		this.entityClass = entityClass;
		this.mode = mode;
	}

	@Override
	public void onInit() {
		super.onInit();
		createFields();
		createButtons();
	}

	protected void createFields(){
		BeanDesc beanDesc = BeanDescFactory.getBeanDesc(entityClass);
		int size = beanDesc.getPropertyDescSize();
		for(int i=0; i < size; i++){
			PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
			Field field = createField(propertyDesc);
			if(field != null){
				add(field);
			}
		}
	}

	protected void createButtons(){
		Submit submit = null;
		if(mode == EntityFormMode.REGISTER){
			submit = new Submit("submit", "Register");
		} else if(mode == EntityFormMode.EDIT){
			submit = new Submit("submit", "Update");
		} else if(mode == EntityFormMode.DELETE){
			submit = new Submit("submit", "Delete");
		}
		add(submit);
	}

	public Submit getSubmit(){
		return (Submit) getField("submit");
	}

	// TODO 別クラスにしてDIして使うようにしたほうがいいかも
	protected Field createField(PropertyDesc propertyDesc){
		String name = propertyDesc.getPropertyName();
		Class<?> type = propertyDesc.getPropertyType();
		Field field = null;

		Id id = getAnnotation(propertyDesc, Id.class);
		if(id != null){
			// 削除モード時はIDをHiddenFieldとして生成
			if(mode == EntityFormMode.DELETE){
				field = new HiddenField(name, "");
				return field;
			}
			// 更新モード時かつIDが自動採番の場合はIDをHiddenFieldとして生成
			if(mode == EntityFormMode.EDIT){
				GeneratedValue generatedValue = getAnnotation(propertyDesc, GeneratedValue.class);
				if(generatedValue != null){
					field = new HiddenField(name, "");
					return field;
				}
			}
		}

		// 削除モード時はIDのHiddenField以外は作成しない
		if(mode == EntityFormMode.DELETE){
			return null;
		}

		// プロパティの型に応じたフィールドを生成
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

	// TODO ユーティリティにしたほうがいいかも
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
