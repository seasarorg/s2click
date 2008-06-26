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
	
	/** jTip�̃��\�[�X�iclick/jquery�Ƀf�v���C����܂��j */
    public static final String[] JQUERY_RESOURCES = {
        "/org/seasar/s2click/control/jquery/jquery.js",
        "/org/seasar/s2click/control/jquery/jtip.js",
        "/org/seasar/s2click/control/jquery/jtip.css",
        "/org/seasar/s2click/control/jquery/arrow_left.gif",
        "/org/seasar/s2click/control/jquery/arrow_right.gif",
        "/org/seasar/s2click/control/jquery/loader.gif",
        "/org/seasar/s2click/control/jquery/help.png",
    };
    
	/** HTML��head�v�f���ɏo�͂���C���|�[�g�X�e�[�g�����g */
    public static final String HTML_IMPORTS =
        "<link type=\"text/css\" rel=\"stylesheet\" href=\"{0}/click/jquery/jtip.css\"/>\n"
        + "<script type=\"text/javascript\" src=\"{0}/click/jquery/jquery.js\"></script>\n"
        + "<script type=\"text/javascript\" src=\"{0}/click/jquery/jtip.js\"></script>\n";
    
	private String contents;
	private int width = 300;
	private String title;
	private String label;
	
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
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.click.Control#onDeploy(javax.servlet.ServletContext)
	 */
    public void onDeploy(ServletContext servletContext) {
        ClickUtils.deployFiles(servletContext,
                               JQUERY_RESOURCES,
                               "click/jquery");
    }
	
    /*
     * (non-Javadoc)
     * @see net.sf.click.Control#getHtmlImports()
     */
	public String getHtmlImports() {
        return MessageFormat.format(HTML_IMPORTS, 
        		new Object[]{ getContext().getRequest().getContextPath() }
        );
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
			sb.append("<img src=\"").append(path).append("/click/jquery/help.png\" border=\"0\">");
		}
		sb.append("</a>");
		
		return sb.toString();
	}

	/**
	 * �c�[���`�b�v�ɕ\������HTML�̃p�X���擾���܂��B
	 * 
	 * @return �c�[���`�b�v�ɕ\������HTML�̃p�X
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * �c�[���`�b�v�ɕ\������HTML�̃p�X��ݒ肵�܂��B
	 * 
	 * @param contents �c�[���`�b�v�ɕ\������HTML�̃p�X
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * �c�[���`�b�v�̕����擾���܂��B
	 * 
	 * @return �c�[���`�b�v�̕��i�s�N�Z���P�ʁj
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * �c�[���`�b�v�̕���ݒ肵�܂��B
	 * 
	 * @param width �c�[���`�b�v�̕��i�s�N�Z���P�ʁj
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * �c�[���`�b�v�̃^�C�g�����擾���܂��B
	 * 
	 * @return �c�[���`�b�v�̃^�C�g��
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * �c�[���`�b�v�̃^�C�g����ݒ肵�܂��B
	 * 
	 * @param title �c�[���`�b�v�̃^�C�g��
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * �A�C�R���̑���ɕ\������e�L�X�g���擾���܂��B
	 * 
	 * @return �A�C�R���̑���ɕ\������e�L�X�g
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * �A�C�R���̑���ɕ\������e�L�X�g��ݒ肵�܂��B
	 * 
	 * @param label �A�C�R���̑���ɕ\������e�L�X�g
	 */
	public void setLabel(String label) {
		this.label = label;
	}

}
