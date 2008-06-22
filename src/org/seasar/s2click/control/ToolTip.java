package org.seasar.s2click.control;

import java.text.MessageFormat;

import javax.servlet.ServletContext;

import net.sf.click.control.AbstractControl;
import net.sf.click.util.ClickUtils;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Naoki Takezoe
 */
public class ToolTip extends AbstractControl {
	
	private static final long serialVersionUID = 1L;
	
	private String contents;
	private int width = 300;
	private String title;
	private String label;
	
    public static final String HTML_IMPORTS =
        "<link type=\"text/css\" rel=\"stylesheet\" href=\"{0}/css/jtip.css\"/>\n"
        + "<script type=\"text/javascript\" src=\"{0}/js/jquery.js\"></script>\n"
        + "<script type=\"text/javascript\" src=\"{0}/js/jtip.js\"></script>\n";
	
	public ToolTip() {
		super();
	}

	public ToolTip(String name) {
		super();
		setName(name);
	}
	
	public ToolTip(String name, String contents) {
		super();
		setName(name);
		setContents(contents);
	}
	
	public ToolTip(String name, String contents, int width) {
		super();
		setName(name);
		setContents(contents);
		setWidth(width);
	}
	
	public ToolTip(String name, String contents, String title) {
		super();
		setName(name);
		setContents(contents);
		setTitle(title);
	}
	
	public ToolTip(String name, String contents, String title, int width) {
		super();
		setName(name);
		setContents(contents);
		setTitle(title);
		setWidth(width);
	}
	
	public String getHtmlImports() {
        return MessageFormat.format(HTML_IMPORTS, 
        		new Object[]{ getContext().getRequest().getContextPath() }
        );
    }

	public void onDeploy(ServletContext servletContext) {
	}

	public void onDestroy() {
	}

	public void onInit() {
	}

	public boolean onProcess() {
		return true;
	}

	public void onRender() {
	}

	public void setListener(Object listener, String method) {
	}
	
	@Override public String toString(){
		String path = getContext().getRequest().getContextPath();
		
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"").append(getContents()).append("?width=").append(getWidth()).append("\" ");
		sb.append("class=\"jTip\" id=\"").append(getId()).append("\" ");
		
		if(StringUtils.isNotEmpty(getTitle())){
			sb.append("name=\"").append(ClickUtils.escapeHtml(getTitle())).append("\"");
		}
		
		sb.append(">");
		if(StringUtils.isNotEmpty(getLabel())){
			sb.append(ClickUtils.escapeHtml(getLabel()));
		} else {
			sb.append("<img src=\"").append(path).append("/images/help.png\" border=\"0\">");
		}
		sb.append("</a>");
		
		return sb.toString();
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
