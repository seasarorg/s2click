package org.seasar.s2click.control;

import java.lang.reflect.Field;

import net.sf.click.control.Column;

/**
 * publicフィールドをテーブル列として扱うための<code>Column</code>拡張クラスです。
 * 
 * @author Naoki Takezoe
 */
public class PublicFieldColumn extends Column {

	private static final long serialVersionUID = 1L;

	public PublicFieldColumn() {
		super();
	}

	public PublicFieldColumn(String name, String title) {
		super(name, title);
	}

	public PublicFieldColumn(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public Object getProperty(String name, Object row) {
		try {
			String[] names = new String[]{name};
			if(name.indexOf('.') >= 0){
				names = name.split("\\.");
			}
			Class<?> target = row.getClass();
			Object value = row;
			for(String propertyName: names){
				Field field = target.getField(propertyName);
				value = field.get(value);
				target = field.getType();
			}
			return value;
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

}

