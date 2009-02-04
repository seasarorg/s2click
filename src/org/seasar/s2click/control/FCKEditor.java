package org.seasar.s2click.control;

import java.text.MessageFormat;

import net.sf.click.control.Field;
import net.sf.click.util.ClickUtils;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * <a href="http://www.fckeditor.net/">FCKeditor</a>を使用してHTMLをWYSIWYG編集するためのコントロールです。
 * 
 * @author Naoki Takezoe
 */
public class FCKEditor extends Field {

	private static final long serialVersionUID = 1L;
	
	protected int width = 800;
	protected int height = 450;
	protected int minLength = 0;
	protected int maxLength = 0;
	protected boolean required;
	
	public static final String HTML_IMPORTS = 
		"<script type=\"text/javascript\" src=\"{0}/r/fckeditor/fckeditor.js\"></script>\n";
	
	public FCKEditor() {
		super();
	}

	public FCKEditor(String name, boolean required) {
		super(name);
		this.required = required;
	}

	public FCKEditor(String name, int width, int height) {
		super(name);
		this.width = width;
		this.height = height;
	}

	public FCKEditor(String name, String label, boolean required) {
		super(name, label);
		this.required = required;
	}

	public FCKEditor(String name, String label, int width, int height) {
		super(name, label);
		this.width = width;
		this.height = height;
	}

	public FCKEditor(String name, String label) {
		super(name, label);
	}

	public FCKEditor(String name) {
		super(name);
	}
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHtmlImports() {
		Object[] args = {
				getContext().getRequest().getContextPath(),
				ClickUtils.getResourceVersionIndicator(getContext()),
		};
		
		return MessageFormat.format(HTML_IMPORTS, args);
	}
	
	/**
	 * FCKEditorの幅（ピクセル）を取得します。
	 * 
	 * @return FCKEditorの幅（ピクセル）
	 */
	@Override
	public String getWidth() {
		return String.valueOf(width);
	}

	/**
	 * FCKEditorの幅（ピクセル）を設定します。
	 * 
	 * @param value FCKEditorの幅（ピクセル）
	 */
	@Override
	public void setWidth(String value) {
		setWidth(Integer.parseInt(value));
	}
	
	/**
	 * FCKEditorの幅（ピクセル）を設定します。
	 * 
	 * @param width FCKEditorの幅（ピクセル）
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * FCKEditorの高さ（ピクセル）を取得します。
	 * 
	 * @return FCKEditorの高さ（ピクセル）
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * FCKEditorの高さ（ピクセル）を設定します。
	 * 
	 * @param height FCKEditorの高さ（ピクセル）
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 入力必須かどうかを取得します。
	 * 
	 * @return 入力必須かどうか
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * 入力必須かどうかを設定します。
	 * 
	 * @param required 入力必須かどうか
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	/**
	 * 最小文字数を取得します。
	 * 
	 * @return 最小文字数
	 */
	public int getMinLength() {
		return minLength;
	}

	/**
	 * 最小文字数を設定します。
	 * 
	 * @param minLength 最小文字数
	 */
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	
	/**
	 * 最大文字数を取得します。
	 * 
	 * @return 最大文字数
	 */
	public int getMaxLength() {
		return maxLength;
	}
	
	/**
	 * 最大文字数を設定します。
	 * 
	 * @param maxLength 最大文字数
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	/**
	 * 以下の入力チェックを行ないます。
	 * <ul>
	 *   <li>必須チェック（<code>setRequired()</code>に<code>true</code>が設定された場合）</li>
	 *   <li>最小文字数チェック（<code>setMinLength()</code>にゼロ以上の値が設定された場合）</li>
	 *   <li>最大文字数チェック（<code>setMaxLength()</code>にゼロ以上の値が設定された場合）</li>
	 * </ul>
	 */
    public void validate() {
        setError(null);

        String value = getValue();

        int length = value.length();
        if (length > 0) {
            if (getMinLength() > 0 && length < getMinLength()) {
                setErrorMessage("field-minlength-error", getMinLength());
                return;
            }

            if (getMaxLength() > 0 && length > getMaxLength()) {
                setErrorMessage("field-maxlength-error", getMaxLength());
                return;
            }

        } else {
            if (isRequired()) {
                setErrorMessage("field-required-error");
            }
        }
    }


	@Override
	public String toString(){
		String contextPath = getContext().getRequest().getContextPath();
		
		StringBuilder sb = new StringBuilder();
		sb.append("<script type=\"text/javascript\">\n");
		sb.append("var sBasePath = '").append(contextPath).append("/click/fckeditor/';\n");
		sb.append("var oFCKeditor = new FCKeditor('").append(getName()).append("');\n");
		sb.append("oFCKeditor.BasePath = sBasePath;\n");
		sb.append("oFCKeditor.Width = ").append(getWidth()).append(";\n");
		sb.append("oFCKeditor.Height = ").append(getHeight()).append(";\n");
		sb.append("oFCKeditor.Value = '").append(
				StringEscapeUtils.escapeJavaScript(getValue()).replaceAll("(</)(script>)", "$1' + '$2")).append("';\n");
		sb.append("oFCKeditor.Create();\n");
		sb.append("</script>\n");
		
		return sb.toString();
	}
	
}
