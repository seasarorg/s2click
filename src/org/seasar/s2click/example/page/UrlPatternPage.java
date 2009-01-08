package org.seasar.s2click.example.page;

import org.seasar.s2click.annotation.Request;
import org.seasar.s2click.annotation.UrlPattern;

/**
 * URLパターンのテストページ。
 * 
 * @author Naoki Takezoe
 */
@UrlPattern("/user/select/{userId}")
public class UrlPatternPage extends LayoutPage {
	
	public String title = "URLパターン";
	
	@Request
	public String userId;
	
}
