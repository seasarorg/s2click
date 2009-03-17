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

import java.util.List;

import org.apache.click.control.Button;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Label;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * モバイル向けの<code>S2ClickForm</code>拡張です。
 * <p>
 * JavaScriptやテーブルによるレイアウト、CSSのための出力などを行わず、
 * なるべくシンプルかつ軽量なレンダリングを行います。
 * 
 * @author Naoki Takezoe
 */
public class MobileForm extends S2ClickForm {

	private static final long serialVersionUID = 1L;

	public MobileForm() {
		super();
	}

	public MobileForm(String name) {
		super(name);
	}
	
//	@Override
//	public void onInit() {
//		super.onInit();
//		setLabelRequiredSuffix("<font color=\"red\">*</font>");
//	}

	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void setJavaScriptValidation(boolean validate) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void addConfirmMessage(String action, String message) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void addNoJavaScriptValidateAction(String action) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void setButtonAlign(String align) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void setButtonStyle(String value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void setColumns(int columns) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void setDefaultFieldSize(int size) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void setErrorsStyle(String value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void setFieldStyle(String value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void setLabelAlign(String align) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * <code>MobileForm</code>ではこのメソッドはサポートされていません。
	 * 
	 * @throws UnsupportedOperationException このメソッドを呼び出すと常に発生します。
	 */
	@Override
	@Deprecated
	public void setLabelStyle(String value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
    public String toString() {
        final boolean process =
            getContext().getRequest().getMethod().equalsIgnoreCase(getMethod());

        List formFields = ClickUtils.getFormFields(this);

        int bufferSize = getFormSizeEst(formFields);

        HtmlStringBuffer buffer = new HtmlStringBuffer(bufferSize);

        renderHeader(buffer, formFields);

        // Render fields, errors and buttons
        if (POSITION_TOP.equals(getErrorsPosition())) {
            renderErrors(buffer, process);
            renderFields(buffer);
            renderButtons(buffer);

        } else if (POSITION_MIDDLE.equals(getErrorsPosition())) {
            renderFields(buffer);
            renderErrors(buffer, process);
            renderButtons(buffer);

        } else if (POSITION_BOTTOM.equals(getErrorsPosition())) {
            renderFields(buffer);
            renderButtons(buffer);
            renderErrors(buffer, process);

        } else {
            String msg = "Invalid errorsPositon:" + getErrorsPosition();
            throw new IllegalArgumentException(msg);
        }

        buffer.append(endTag());

        return buffer.toString();
    }
	
	@Override
	@SuppressWarnings("unchecked")
    protected void renderHeader(HtmlStringBuffer buffer, List formFields) {

        buffer.elementStart("form");

        buffer.appendAttribute("method", getMethod());
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("action", getActionURL());
        buffer.appendAttribute("enctype", getEnctype());

        appendAttributes(buffer);

        buffer.closeTag();
        buffer.append("\n");
    }
	
	@Override
	@SuppressWarnings("unchecked")
    public String startTag() {
        List formFields = ClickUtils.getFormFields(this);
        int bufferSize = getFormSizeEst(formFields);
		
		HtmlStringBuffer buffer = new HtmlStringBuffer(bufferSize);
		renderErrors(buffer, true);
		renderHeader(buffer, formFields);
        
		return super.startTag() + buffer.toString();		
    }

    @Override
    public String endTag() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();

        List<?> formFields = ClickUtils.getFormFields(this);
        renderHiddenFields(buffer, formFields);

        buffer.append("</form>\n");

        return buffer.toString();
    }
    
	@SuppressWarnings("unchecked")
    protected void renderErrors(HtmlStringBuffer buffer, boolean processed) {

        if (processed && !isValid()) {

            if (getError() != null) {
                buffer.append("<font color=\"red\">");
                buffer.append(getError());
                buffer.append("</font><br>\n");
            }

            List errorFieldList = getErrorFields();

            for (int i = 0, size = errorFieldList.size(); i < size; i++) {
                Field field = (Field) errorFieldList.get(i);
                
                buffer.append("<font color=\"red\">");
                buffer.append(field.getError());
                buffer.append("</font><br>\n");
            }

        }
    }
	
    protected void renderFields(HtmlStringBuffer buffer) {
        if (getFieldList().size() == 1
            && getFields().containsKey("form_name")) {
            return;
        }

        for (int i = 0, size = getFieldList().size(); i < size; i++) {

            Field field = (Field) getFieldList().get(i);

            if (!field.isHidden()) {

                // Field width
//                Integer width = (Integer) getFieldWidths().get(field.getName());

                if (field instanceof FieldSet) {
//                    buffer.append("<td class=\"fields\"");

//                    if (width != null) {
//                        int colspan = (width.intValue() * 2);
//                        buffer.appendAttribute("colspan", colspan);
//                    } else {
//                        buffer.appendAttribute("colspan", 2);
//                    }
//
//                    buffer.append(">\n");
                    buffer.append(field);

                } else if (field instanceof Label) {
//                    buffer.append("<td class=\"fields\" align=\"");
//                    buffer.append(getLabelAlign());
//                    buffer.append("\"");

//                    if (field.hasAttributes()) {
//                        //Temporarily remove the style attribute
//                        String tempStyle = null;
//                        if (field.hasAttribute("style")) {
//                            tempStyle = field.getAttribute("style");
//                            field.setAttribute("style", null);
//                        }
//                        buffer.appendAttributes(field.getAttributes());
//
//                        //Put style back in attribute map
//                        if (tempStyle != null) {
//                            field.setAttribute("style", tempStyle);
//                        }
//                    }
//                    buffer.append(">");
                    buffer.append(field);

                } else {
                    // Write out label
//                    if (POSITION_LEFT.equals(getLabelsPosition())) {
//                        buffer.append("<td class=\"fields\"");
//                        buffer.appendAttribute("align", getLabelAlign());
//                        buffer.appendAttribute("style", getLabelStyle());
//                        buffer.append(">");
//                    } else {
//                        buffer.append("<td class=\"fields\" valign=\"top\"");
//                        buffer.appendAttribute("style", getLabelStyle());
//                        buffer.append(">");
//                    }

//                    if (field.isRequired()) {
//                        buffer.append(getLabelRequiredPrefix());
//                    } else {
//                        buffer.append(getLabelNotRequiredPrefix());
//                    }
                    buffer.append("<label");
                    buffer.appendAttribute("for", field.getId());
                    if (field.isDisabled()) {
                        buffer.append(" disabled=\"disabled\"");
                    }
                    if (field.getError() != null) {
                        buffer.append(" class=\"error\"");
                    }
                    buffer.append(">");
                    buffer.append(field.getLabel());
                    buffer.append("</label>");
//                    if (field.isRequired()) {
//                        buffer.append(getLabelRequiredSuffix());
//                    } else {
//                        buffer.append(getLabelNotRequiredSuffix());
//                    }
                    buffer.append("<br>");

//                    if (POSITION_LEFT.equals(getLabelsPosition())) {
//                        buffer.append("</td>\n");
//                        buffer.append("<td align=\"left\"");
//                        buffer.appendAttribute("style", getFieldStyle());
//
//                        if (width != null) {
//                            int colspan = (width.intValue() * 2) + 1;
//                            buffer.appendAttribute("colspan", colspan);
//                        }
//
//                        buffer.append(">");
//                    } else {
//                        buffer.append("<br>");
//                    }

                    // Write out field
                    buffer.append(field);
                    buffer.append("<br>\n");
                }

            }
        }
    }

	
    protected void renderButtons(HtmlStringBuffer buffer) {
        if (!getButtonList().isEmpty()) {
            for (int i = 0, size = getButtonList().size(); i < size; i++) {
                Button button = (Button) getButtonList().get(i);
                buffer.append(button);
            }
        }
    }


	
}
