/*
 * Copyright 2006-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
