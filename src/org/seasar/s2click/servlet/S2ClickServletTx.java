/*
 * Copyright 2006-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.s2click.servlet;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.click.Page;
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
		
		UserTransaction tx = SingletonS2Container.getComponent(UserTransaction.class);
		tx.begin();
		
		try {
			super.processPage(page);
			
		} catch(Exception ex){
			tx.setRollbackOnly();
			throw ex;
			
		} finally {
			if (tx.getStatus() == Status.STATUS_ACTIVE) {
				// 問題なければコミット
				tx.commit();
			} else {
				// 問題があればロールバック
				tx.rollback();
			}
		}
	}
}
