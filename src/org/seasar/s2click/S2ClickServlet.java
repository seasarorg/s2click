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
 * Seasar2��Click Framework��A�g�����邽�߂̃T�[�u���b�g�B
 * 
 * @author Naoki Takezoe
 */
public class S2ClickServlet extends ClickServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * HOT deploy�ł͂Ȃ��ꍇ�A���̃��\�b�h��Click Framework�̏��������s���܂��B
	 */
	@Override public void init() throws ServletException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		if(!SmartDeployUtil.isHotdeployMode(container)){
			super.init();
		}
	}
	
	/**
	 * HOT deploy�̏ꍇ�A���N�G�X�g����Click Framework�̏��������s���܂��B
	 * <p>
	 * TODO �y�[�W���������Ȃ�ƒx���Ȃ邩�c�H
	 */
	@Override public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		if(SmartDeployUtil.isHotdeployMode(container)){
			synchronized(this){
				super.init();
			}
		}
		
		super.service(req, res);
	}

	/**
	 * S2Container����y�[�W�N���X�̃C���X�^���X���擾���܂��B
	 */
	@SuppressWarnings("unchecked")
	@Override protected Page newPageInstance(String path, Class pageClass,
			HttpServletRequest request) throws Exception {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (Page) container.getComponent(pageClass);
	}
	
}
