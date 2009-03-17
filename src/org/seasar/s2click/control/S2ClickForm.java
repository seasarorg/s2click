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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.click.control.ActionButton;
import org.apache.click.control.Field;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;
import org.seasar.s2click.util.S2ClickUtils;

/**
 * publicフィールドを自動的にコントロールとして登録してくれる<code>Form</code>拡張クラスです。
 * <code>Submit</code>コントロールによってJavaScriptバリデーションを行うかどうかを制御する機能も備えています。
 *
 * <h2>publicフィールドの自動登録</h2>
 * <p>
 *   このクラスのサブクラスで{@link #setFieldAutoRegisteration(boolean)}に<code>true</code>が設定された場合、
 *   <code>S2ClickForm</code>はサブクラスのpublicフィールドとして宣言されたコントロール群を自動的に{@link #add(Field)}します。
 *   初期化コードで{@link #add(Field)}を呼び出す必要はありません。
 * </p>
 * <pre>
 * public SampleForm extends S2ClickForm {
 *   private TextField userId = new TextField("userId");
 *   private PasswordField password = new PasswordField("password");
 *   private Submit submit = new Submit("submit");
 *
 *   public SampleForm(){
 *     setFieldAutoRegisteration(true);
 *   }
 *
 *   ...
 * } </pre>
 *
 * <h2>送信前に確認ダイアログを表示する</h2>
 * <p>
 *   {@link #addConfirmMessage(String, String)}を使用することで
 *   フォームの送信前にJavaScriptによる確認ダイアログを表示することができます。
 * </p>
 * <pre>
 * public SampleForm extends S2ClickForm {
 *   ...
 *   private Submit register = new Submit("register");
 *   private Submit cancel = new Submit("cancel");
 *
 *   public SampleForm(){
 *     addConfirmMessage("register", "登録します。よろしいですか？");
 *   }
 *
 *   ...
 * </pre>
 *
 * <h2>JavaScriptバリデーションを行わないSubmitコントロール</h2>
 * <p>
 *   {@link #addNoJavaScriptValidateAction(String)}を使用することで、
 *   JavaScriptバリデーションを行わない<code>Submit</code>コントロールを指定することができます。
 * </p>
 * <pre>
 * public SampleForm extends S2ClickForm {
 *   ...
 *   private Submit register = new Submit("register");
 *   private Submit cancel = new Submit("cancel");
 *
 *   public SampleForm(){
 *     addNoJavaScriptValidateAction("register");
 *   }
 *
 *   ...
 * } </pre>
 *
 * <h2>copyTo()、copyFrom()のpublicフィールド対応</h2>
 * <p>
 *   Clickの<code>Form</code>クラスはJavaBeanとフォームの値を相互変換するために
 *   <code>copyTo()</code>メソッド、<code>copyFrom()</code>メソッドを備えています。
 *   <code>S2ClickForm</code>ではこれらのメソッドをpublicフィールドに対応させています。
 *   なお、{@link S2ClickUtils}を使用することでもpublicフィールドを使用したJavaBeanと
 *   フォームの値を相互変換することができます。
 * </p>
 *
 * @author Naoki Takezoe
 */
public abstract class S2ClickForm extends Form {

	private static final long serialVersionUID = 1L;

	protected Map<String, String> confirmMessages = new HashMap<String, String>();
	protected List<String> noJavaScriptValidateActions = new ArrayList<String>();
	protected boolean fieldAutoRegisteration = false;

	/**
	 * Create a Form with no name defined.
	 */
	public S2ClickForm() {
		super();
	}

	/**
	 * Construct the Form with the given name.
	 *
	 * @param name the form name
	 */
	public S2ClickForm(String name) {
		super(name);
	}

	public void setFieldAutoRegisteration(boolean fieldAutoRegisteration){
		this.fieldAutoRegisteration = fieldAutoRegisteration;
	}

	public boolean isFieldAutoRegistration(){
		return this.fieldAutoRegisteration;
	}

	public void addConfirmMessage(String action, String message){
		confirmMessages.put(action, message);
	}

	public void addNoJavaScriptValidateAction(String action){
		noJavaScriptValidateActions.add(action);
	}

	/**
	 * JavaScriptを使用する必要があるかどうかを判定します。
	 *
	 * @return JavaScriptを使用する必要がある場合true、必要ない場合false
	 */
	protected boolean requiresJavaScript(){
		return (getValidate() && getJavaScriptValidation()) || !confirmMessages.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#add(net.sf.click.control.Field)
	 */
	@Override
	public Field add(Field field) {
		Field result = super.add(field);
		if(field instanceof Submit){
			if(requiresJavaScript()){
				field.setAttribute("onclick",
						getName() + ".action.value='" + field.getName() + "'");
			}
		}
		if(field instanceof ActionButton){
			getPage().addControl(field);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#startTag()
	 */
	@Override
	public String startTag(){
		HtmlStringBuffer buffer = new HtmlStringBuffer();
		buffer.append("<table width=\"100%\">");
		renderErrors(buffer, true);
		buffer.append("</table>");

		if(getJavaScriptValidation()){
			String script = "on_" + getId() + "_submit()";
			setAttribute("onsubmit", "return " + script + ";");
		}

		return super.startTag() + buffer.toString();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void renderTagEnd(List formFields, HtmlStringBuffer buffer) {
		renderHiddenFields(buffer, formFields);
		super.renderTagEnd(formFields, buffer);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#renderValidationJavaScript(net.sf.click.util.HtmlStringBuffer, java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
    protected void renderValidationJavaScript(HtmlStringBuffer buffer, List formFields) {

        // Render JavaScript form validation code
        if (requiresJavaScript()) {
            List functionNames = new ArrayList();

            buffer.append("<script type=\"text/javascript\"><!--\n");

            if(getValidate() && getJavaScriptValidation()){
	            // Render field validation functions & build list of function names
	            for (Iterator i = formFields.iterator(); i.hasNext();) {
	                Field field = (Field) i.next();
	                String fieldJS = field.getValidationJavaScript();
	                if (fieldJS != null) {
	                    buffer.append(fieldJS);

	                    StringTokenizer tokenizer = new StringTokenizer(fieldJS);
	                    tokenizer.nextToken();
	                    functionNames.add(tokenizer.nextToken());
	                }
	            }
            }

            buffer.append("function on_");
            buffer.append(getId());
            buffer.append("_submit() {\n");
            buffer.append("  var actionName = document." + getName() + ".action.value;\n");

            if (!functionNames.isEmpty()) {
                buffer.append("  var noValidateActions = ");
                buffer.append(JSON.encode(noJavaScriptValidateActions));
                buffer.append(";\n");
                buffer.append("  var skipValidation = false;\n");
                buffer.append("  for(var i=0;i<noValidateActions.length;i++){\n");
                buffer.append("    if(actionName == noValidateActions[i]){\n");
                buffer.append("      skipValidation = true;\n");
                buffer.append("      break;\n");
                buffer.append("    }\n");
                buffer.append("  }\n");

                buffer.append("  if(skipValidation == false){\n");

                buffer.append("    var msgs = new Array(");
                buffer.append(functionNames.size());
                buffer.append(");\n");
                for (int i = 0; i < functionNames.size(); i++) {
                    buffer.append("    msgs[");
                    buffer.append(i);
                    buffer.append("] = ");
                    buffer.append(functionNames.get(i).toString());
                    buffer.append(";\n");
                }
                buffer.append("    if(!validateForm(msgs, '");
                buffer.append(getId());
                buffer.append("', '");
                buffer.append(getErrorsAlign());
                buffer.append("', ");
                if (getErrorsStyle() == null) {
                    buffer.append("null");
                } else {
                    buffer.append("'" + getErrorsStyle() + "'");
                }
                buffer.append(")){\n");
                buffer.append("      return false;\n");
                buffer.append("    }\n");

                buffer.append("  }\n");
            }

            if(!confirmMessages.isEmpty()){
                buffer.append("  var confirmMessages = ");
                buffer.append(JSON.encode(confirmMessages));
                buffer.append(";\n");

                buffer.append("  var message = confirmMessages[actionName];\n");
                buffer.append("  if(message){\n");
                buffer.append("    if(!confirm(message)){ return false; }\n");
                buffer.append("  }\n");
            }

            buffer.append("  return true;\n");
            buffer.append("}\n");
            buffer.append("//--></script>\n");
        }
    }

	@Override
	public void onInit() {
		if(requiresJavaScript()){
			add(new HiddenField("action", ""));
		}
		
		if(isFieldAutoRegistration()){
	    	for(java.lang.reflect.Field field: getClass().getFields()){
	       		if(Field.class.isAssignableFrom(field.getType())){
	       			try {
	       				add((Field) field.get(this));
	       			} catch(IllegalAccessException ex){
	       				ex.printStackTrace();
	       			}
	    		}
	    	}
		}
		
		super.onInit();
	}

	@Override
	public void copyFrom(Object object, boolean debug) {
		S2ClickUtils.copyObjectToForm(object, this, debug);
	}

	@Override
	public void copyFrom(Object object) {
		copyFrom(object, false);
	}

	@Override
	public void copyTo(Object object, boolean debug) {
		S2ClickUtils.copyFormToObject(this, object, debug);
	}

	@Override
	public void copyTo(Object object) {
		copyTo(object, false);
	}
	
	/**
	 * このフォームのフィールドをすべてhiddenフィールドに変換します。
	 */
	public void toHidden(){
		S2ClickUtils.convertToHidden(this);
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

        if (requiresJavaScript()) {
            String javaScript = "return on_" + getId() + "_submit();";
            buffer.appendAttribute("onsubmit", javaScript);
        }
        buffer.closeTag();
        buffer.append("\n");
    }

	@SuppressWarnings("unchecked")
	protected void renderHiddenFields(HtmlStringBuffer buffer, List formFields) {
      // Render hidden fields
      for (int i = 0, size = formFields.size(); i < size; i++) {
          Field field = (Field) formFields.get(i);
          if (field.isHidden()) {
              buffer.append(field);
              buffer.append("\n");
          }
      }
    }
    
    @Override
    public String getActionURL() {
        HttpServletRequest request = getContext().getRequest();
        HttpServletResponse response = getContext().getResponse();
        
        String requestURI = request.getRequestURI();
        
        // JSPの場合
        if(requestURI.endsWith(".jsp")){
        	// TODO c:importしている場合はこれだけじゃダメかも…
        	String forwardURI = (String) request.getAttribute("javax.servlet.forward.request_uri");
        	
        	if(StringUtils.isNotEmpty(forwardURI)){
        		requestURI = forwardURI;
        		
        	} else {
        		requestURI = requestURI.replaceFirst("\\.jsp$", ".htm");
        	}
        }
        
        return response.encodeURL(requestURI);
    }

}
