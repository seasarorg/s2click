package org.seasar.s2click.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.arnx.jsonic.JSON;
import net.sf.click.Control;
import net.sf.click.control.Field;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Submit;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.HtmlStringBuffer;

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
public abstract class S2ClickForm extends net.sf.click.control.Form {

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
	 * Initializes this form.
	 * <p>
	 * This method is called from {@link #onProcess()}.
	 */
	protected void init(){
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

		for(Object button: getButtonList()){
			getPage().addControl(Control.class.cast(button));
		}
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
	@Override public void add(Field field) {
		super.add(field);
		if(field instanceof Submit){
			if(requiresJavaScript()){
				field.setAttribute("onclick",
						getName() + ".action.value='" + field.getName() + "'");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#startTag()
	 */
	@Override public String startTag(){
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
	 * @see net.sf.click.control.Form#endTag()
	 */
	@Override public String endTag() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();

        List<?> formFields = ClickUtils.getFormFields(this);
        renderHiddenFields(buffer, formFields);

        buffer.append("</form>\n");

        renderFocusJavaScript(buffer, formFields);

        renderValidationJavaScript(buffer, formFields);

        return buffer.toString();
    }

	/*
	 * (non-Javadoc)
	 * @see net.sf.click.control.Form#renderValidationJavaScript(net.sf.click.util.HtmlStringBuffer, java.util.List)
	 */
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
		init();
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

}
