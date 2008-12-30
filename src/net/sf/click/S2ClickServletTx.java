package net.sf.click;

import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.container.SingletonS2Container;

/**
 * リクエスト毎にトランザクションを制御する{@link S2ClickServlet}拡張サーブレットです。
 *
 * @author Naoki Takezoe
 */
public class S2ClickServletTx extends S2ClickServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPage(final Page page) throws Exception {
		TransactionManagerAdapter manager
			= SingletonS2Container.getComponent(TransactionManagerAdapter.class);
		try {
			manager.requiresNew(new TransactionCallback(){
				public Object execute(TransactionManagerAdapter adapter) throws Throwable {
					try {
						S2ClickServletTx.super.processPage(page);
						return null;
					} catch(Throwable ex){
						adapter.setRollbackOnly();
						throw ex;
					}
				}
			});
		} catch(Throwable ex){
			if(ex instanceof Exception){
				throw (Exception) ex;
			} else {
				// TODO Exceptionでいいのかなぁ。
				throw new Exception(ex);
			}
		}
	}
}
