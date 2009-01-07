package org.seasar.s2click.control;

import java.text.MessageFormat;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringEscapeUtils;

import net.sf.click.Page;
import net.sf.click.control.AbstractLink;
import net.sf.click.util.ClickUtils;

/**
 * <a href="http://orangoo.com/labs/GreyBox/">GreyBox</a>を使用して指定したページを
 * モーダルダイアログとして表示するリンクです。
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxLink extends AbstractLink {
	
	private static final long serialVersionUID = 1L;

	public static final String HTML_IMPORTS =
//		"<script type=\"text/javascript\">var GB_ROOT_DIR = \"{0}/js/greybox/\";</script>\n" +
		"<script type=\"text/javascript\" src=\"{0}/click/greybox/AJS.js\"></script>\n" +
		"<script type=\"text/javascript\" src=\"{0}/click/greybox/AJS_fx.js\"></script>\n" +
		"<script type=\"text/javascript\" src=\"{0}/click/greybox/gb_scripts.js\"></script>\n" +
		"<link href=\"{0}/click/greybox/gb_styles.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\" />\n" +
		"<script type=\"text/javascript\">function S2Click_GB_SetResult(data, id)'{ AJS.$(id).value = data; }'</script>\n";

	protected String dialogTitle = "";
	protected int width = 400;
	protected int height = 300;
	protected Class<? extends Page> pageClass;

	/** greyboxのリソース（click/greyboxにデプロイされます） */
    public static final String[] GREYBOX_RESOURCES = {
        "/org/seasar/s2click/control/greybox/AJS_fx.js",
        "/org/seasar/s2click/control/greybox/AJS.js",
        "/org/seasar/s2click/control/greybox/g_close.gif",
        "/org/seasar/s2click/control/greybox/gb_scripts.js",
        "/org/seasar/s2click/control/greybox/gb_styles.css",
        "/org/seasar/s2click/control/greybox/header_bg.gif",
        "/org/seasar/s2click/control/greybox/indicator.gif",
        "/org/seasar/s2click/control/greybox/loader_frame.html",
        "/org/seasar/s2click/control/greybox/next.gif",
        "/org/seasar/s2click/control/greybox/prev.gif",
        "/org/seasar/s2click/control/greybox/w_close.gif",
    };
    
	public GreyboxLink() {
		super();
	}

	public GreyboxLink(String name) {
		super(name);
	}

	public GreyboxLink(String name, String label) {
		super(name);
		setLabel(label);
	}

	public GreyboxLink(String name, String label, String title, Class<? extends Page> pageClass) {
		super(name);
		setLabel(label);
		setPageClass(pageClass);
		setTitle(title);
	}
    
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void onDeploy(ServletContext servletContext) {
        ClickUtils.deployFiles(servletContext,
                               GREYBOX_RESOURCES,
                               "click/greybox");
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHtmlImports() {
        return MessageFormat.format(HTML_IMPORTS,
        		new Object[]{ getContext().getRequest().getContextPath() }
        );
	}

	/**
	 * greyboxによるモーダルダイアログとして表示するページクラスを設定します。
	 *
	 * @param pageClass 表示するページクラス
	 */
	public void setPageClass(Class<? extends Page> pageClass){
		this.pageClass = pageClass;
	}

	/**
	 * greyboxによるモーダルダイアログとして表示するページクラスを設定します。
	 *
	 * @return 表示するページクラス
	 */
	public Class<? extends Page> getPageClass(){
		return this.pageClass;
	}

	/**
	 * greyboxによるモーダルダイアログのタイトルを設定します。
	 *
	 * @param dialogTitle ダイアログのタイトル
	 */
	public void setDialogTitle(String dialogTitle){
		this.dialogTitle = dialogTitle;
	}

	/**
	 * greyboxによるモーダルダイアログのタイトルを取得します。
	 *
	 * @return ダイアログのタイトル
	 */
	public String getDialogTitle(){
		if(this.dialogTitle == null){
			return "";
		}
		return this.dialogTitle;
	}

	/**
	 * ダイアログの幅を設定します。
	 *
	 * @param width ダイアログの幅
	 */
	public void setDialogWidth(int width){
		this.width = width;
	}

	/**
	 * ダイアログの幅を取得します。
	 *
	 * @return ダイアログの幅
	 */
	public int getDialogWidth(){
		return this.width;
	}

	/**
	 * ダイアログの高さを設定します。
	 *
	 * @param height ダイアログの高さ
	 */
	public void setDialogHeight(int height){
		this.height = height;
	}

	/**
	 * ダイアログの高さを取得します。
	 *
	 * @return ダイアログの高さ
	 */
	public int getDialogHeight(){
		return this.height;
	}
    
	public String getHref() {
		// TODO voidにする
		return "#";
	}

	public boolean onProcess() {
		return true;
	}

	public void setListener(Object listener, String method) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString(){
		// ページクラスのパスを取得
		Class<? extends Page> pageClass = getPageClass();
		String pagePath = getContext().getPagePath(pageClass);

		// onClick属性を設定
		StringBuilder sb = new StringBuilder();

		sb.append("GB_showCenter('").append(StringEscapeUtils.escapeJavaScript(getTitle()));
		sb.append("'").append(", '../..").append(pagePath).append("'");

		if(getDialogHeight() > 0){
			sb.append(", ").append(getDialogHeight());
		}

		if(getDialogWidth() > 0){
			sb.append(", ").append(getDialogWidth());
		}

		sb.append(")");

		setAttribute("onclick", sb.toString());

		return super.toString();
	}

}
