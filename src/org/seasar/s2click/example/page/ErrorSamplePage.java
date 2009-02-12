package org.seasar.s2click.example.page;

import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.annotation.Path;

/**
 * わざと例外を発生させ、カスタムエラーページを表示するためのページクラスです。
 * 
 * @author Naoki Takezoe
 */
@Path("/error-sample.htm")
public class ErrorSamplePage extends S2ClickPage {

	/**
	 * {@link RuntimeException}をスローします。
	 */
	@Override
	public void onInit(){
		throw new RuntimeException("カスタムエラーページのテストです。");
	}
	
}
