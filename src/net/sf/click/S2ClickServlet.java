package net.sf.click;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sf.click.util.ErrorPage;
import ognl.OgnlException;

import org.apache.velocity.VelocityContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.util.SmartDeployUtil;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.filter.UrlPatternFilter;
import org.seasar.s2click.util.S2ClickPageImports;

/**
 * Seasar2とClick Frameworkを連携させるためのサーブレット。
 *
 * @author Naoki Takezoe
 */
public class S2ClickServlet extends ClickServlet {

	private static final long serialVersionUID = 1L;
	private boolean initialized = false;

	/**
	 * HOT deployではない場合、このメソッドでClick Frameworkの初期化を行います。
	 */
	@Override
	public void init() throws ServletException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		if(!SmartDeployUtil.isHotdeployMode(container)){
			super.init();
			initialized = true;
		}
	}

	/**
	 * HOT deployの場合、リクエスト毎にClick Frameworkの初期化を行います。
	 */
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		
		String hotDeployInitStatus = (String) req.getAttribute(UrlPatternFilter.HOTDEPLOY_INIT_KEY);
		
		if(initialized == false && !"initialized".equals(hotDeployInitStatus)){
			super.init();
			req.setAttribute(UrlPatternFilter.HOTDEPLOY_INIT_KEY, "initialized");
			
			if("initialize".equals(hotDeployInitStatus)){
				return;
			}
		}
		
		req.setAttribute(ClickApp.class.getName(), clickApp);

		super.service(new S2ClickRequestWrapper((HttpServletRequest) req), res);
	}

	/**
	 * S2Containerからページクラスのインスタンスを取得します。
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Page newPageInstance(String path, Class pageClass,
			HttpServletRequest request) throws Exception {
		if(pageClass == Page.class || pageClass == ErrorPage.class){
			return (Page) pageClass.newInstance();
		}
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (Page) container.getComponent(pageClass);
	}

	/**
	 * ページクラスのフィールドへのリクエストパラメータのバインドは{@link S2ClickPage}で行うため、
	 * このメソッドでは何も行いません。
	 */
	@Override
	protected void processPageRequestParams(Page page) throws OgnlException {
		// なにもしない
	}

	@Override
	protected void renderTemplate(Page page) throws Exception {
		String skipRendering = (String) page.getContext().getRequestAttribute(
				S2ClickPage.SKIP_RENDERING);
		if(!"true".equals(skipRendering)){
			super.renderTemplate(page);
		}
	}

	@Override
	protected VelocityContext createVelocityContext(Page page) {
		VelocityContext context = super.createVelocityContext(page);
		S2ClickPageImports pageImports = new S2ClickPageImports(page);
		
		context.put("imports", pageImports.getAllIncludes());
		context.put("cssImports", pageImports.getCssImports());
		context.put("jsImports", pageImports.getJsImports());
		
		return context;
	}
	
    protected void setRequestAttributes(Page page) {
    	super.setRequestAttributes(page);
    	
        HttpServletRequest request = page.getContext().getRequest();
		S2ClickPageImports pageImports = new S2ClickPageImports(page);
        
		request.setAttribute("imports", pageImports.getAllIncludes());
        request.setAttribute("cssImports", pageImports.getCssImports());
        request.setAttribute("jsImports", pageImports.getJsImports());
    }

}
