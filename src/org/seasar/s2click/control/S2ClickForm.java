package org.seasar.s2click.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.seasar.s2click.util.S2ClickUtils;

import net.arnx.jsonic.JSON;
import net.sf.click.control.Field;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Submit;
import net.sf.click.util.HtmlStringBuffer;

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
public abstract class S2ClickForm extends net.sf.click.control.Form {
	
	private static final long serialVersionUID = 1L;
	
//	protected Map<String, String> confirmMessages = new HashMap<String, String>();
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

//	public void addMessage(String action, String message){
//		confirmMessages.put(action, message);
//	}
//	
	public void addNoJavaScriptValidateAction(String action){
		noJavaScriptValidateActions.add(action);
	}
	
	/**
	 * Initializes this form.
	 * <p>
	 * This method is called from {@link #onProcess()}.
	 */
	protected void init(){
		if(getValidate() && getJavaScriptValidation()){
			add(new HiddenField("action", ""));
		}
		if(isFieldAutoRegistration()){
	    	for(java.lang.reflect.Field field: getClass().getDeclaredFields()){
	       		if(Field.class.isAssignableFrom(field.getType())){
	       			try {
	       				add((Field) field.get(this));
	       			} catch(IllegalAccessException ex){
	       				ex.printStackTrace();
	       			}
	    		}
	    	}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#add(net.sf.click.control.Field)
	 */
	@Override public void add(Field field) {
		super.add(field);
		if(field instanceof Submit){
			if(getValidate() && getJavaScriptValidation()){
				field.setAttribute("onclick", 
						getName() + ".action.value='" + field.getName() + "'");
			}
		}
//		return field;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#startTag()
	 */
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

	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#renderValidationJavaScript(net.sf.click.util.HtmlStringBuffer, java.util.List)
	 */
	@SuppressWarnings("unchecked")
    protected void renderValidationJavaScript(HtmlStringBuffer buffer, List formFields) {

        // Render JavaScript form validation code
        if (getValidate() && getJavaScriptValidation()) {
            List functionNames = new ArrayList();

            buffer.append("<script type=\"text/javascript\"><!--\n");

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

            if (!functionNames.isEmpty()) {
                buffer.append("function on_");
                buffer.append(getId());
                buffer.append("_submit() {\n");
                
                buffer.append("   var noValidateActions = ");
                buffer.append(JSON.encode(noJavaScriptValidateActions));
                buffer.append(";\n");
                buffer.append("   for(var i=0;i<noValidateActions.length;i++){\n");
                buffer.append("      if(document." + getName() + ".action.value == noValidateActions[i]){\n");
                buffer.append("         return true;\n");
                buffer.append("      }\n");
                buffer.append("   }\n");
                
                buffer.append("   var msgs = new Array(");
                buffer.append(functionNames.size());
                buffer.append(");\n");
                for (int i = 0; i < functionNames.size(); i++) {
                    buffer.append("   msgs[");
                    buffer.append(i);
                    buffer.append("] = ");
                    buffer.append(functionNames.get(i).toString());
                    buffer.append(";\n");
                }
                buffer.append("   return validateForm(msgs, '");
                buffer.append(getId());
                buffer.append("', '");
                buffer.append(getErrorsAlign());
                buffer.append("', ");
                if (getErrorsStyle() == null) {
                    buffer.append("null");
                } else {
                    buffer.append("'" + getErrorsStyle() + "'");
                }
                buffer.append(");\n");
                buffer.append("}\n");

            } else {
                buffer.append("function on_");
                buffer.append(getId());
                buffer.append("_submit() { return true; }\n");
            }
            buffer.append("//--></script>\n");
        }
    }
	
	@Override public boolean onProcess() {
		init();
		return super.onProcess();
	}

	@Override
	public void copyFrom(Object object, boolean debug) {
		S2ClickUtils.copyObjectToForm(object, this, debug);
	}

	@Override
	public void copyFrom(Object object) {
		S2ClickUtils.copyObjectToForm(object, this, false);
	}

	@Override
	public void copyTo(Object object, boolean debug) {
		S2ClickUtils.copyFormToObject(this, object, debug);
	}

	@Override
	public void copyTo(Object object) {
		S2ClickUtils.copyFormToObject(this, object, false);
	}

}