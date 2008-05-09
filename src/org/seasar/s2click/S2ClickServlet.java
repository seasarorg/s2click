package org.seasar.s2click;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sf.click.ClickServlet;
import net.sf.click.Page;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.util.SmartDeployUtil;

/**
 * Seasar2とClick Frameworkを連携させるためのサーブレット。
 * 
 * @author Naoki Takezoe
 */
public class S2ClickServlet extends ClickServlet {

	private static final long serialVersionUID = 1L;
	private boolean initialized = false;

	/**
	 * HOT deployではない場合、このメソッドでClickの初期化を行います。
	 */
	@Override public void init() throws ServletException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		if(!SmartDeployUtil.isHotdeployMode(container)){
			super.init();
		}
	}
	
	/**
	 * HOT deployの場合、初回リクエストの受付時にClickの初期化を行います。
	 */
	@Override public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		if(SmartDeployUtil.isHotdeployMode(container)){
			synchronized(this){
				if(!initialized){
					super.init();
					initialized = true;
				}
			}
		}
		
		super.service(req, res);
	}

	/**
	 * S2Containerからページクラスのインスタンスを取得します。
	 */
	@SuppressWarnings("unchecked")
	@Override protected Page newPageInstance(String path, Class pageClass,
			HttpServletRequest request) throws Exception {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (Page) container.getComponent(pageClass);
	}
	
}
