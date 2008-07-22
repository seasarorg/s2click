package org.seasar.s2click.control;

import net.sf.click.extras.control.DateField;

/**
 * YYYY/MM/DD形式で日付を表示する<code>DateField</code>拡張コントロール。
 * 
 * @author Naoki Takezoe
 */
public class DateFieldYYYYMMDD extends DateField {

	private static final long serialVersionUID = 1L;
	
	private static final String FORMAT_PATTERN = "yyyy/MM/dd";
	
	public DateFieldYYYYMMDD() {
		super();
		setFormatPattern(FORMAT_PATTERN);
	}

	public DateFieldYYYYMMDD(String name, boolean required) {
		super(name, required);
		setFormatPattern(FORMAT_PATTERN);
	}

	public DateFieldYYYYMMDD(String name, String label, boolean required) {
		super(name, label, required);
		setFormatPattern(FORMAT_PATTERN);
	}

	public DateFieldYYYYMMDD(String name, String label) {
		super(name, label);
		setFormatPattern(FORMAT_PATTERN);
	}

	public DateFieldYYYYMMDD(String name) {
		super(name);
		setFormatPattern(FORMAT_PATTERN);
	}
	
}
