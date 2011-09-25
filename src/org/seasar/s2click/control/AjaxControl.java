package org.seasar.s2click.control;

import java.util.Map;

/**
 * Ajaxリクエストを送信するコントロールのインターフェースです。
 *
 * @author Naoki Takezoe
 * @since 1.0.5
 */
public interface AjaxControl {

	public void addAjaxHandler(String event, String handler);

	public Map<String, String> getAjaxHandlers();

}
