package org.seasar.s2click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.click.Control;
import net.sf.click.util.Format;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;

/**
 * diconファイルでClickの設定を行うためのクラス。
 *
 * @author Naoki Takezoe
 */
public class S2ClickConfig {

	/**
	 * 文字コード。デフォルトはUTF-8です。
	 */
	public String charset = "UTF-8";

	/**
	 * ページテンプレートで利用可能なフォーマットクラス。
	 * デフォルトは<code>net.sf.click.util.Format</code>です。
	 */
	public Class<? extends Format> formatClass = Format.class;

	/**
	 * Clickの動作モード。以下の値が指定可能です。デフォルトはdevelopmentです。
	 * <ul>
	 *   <li>production</li>
	 *   <li>profile</li>
	 *   <li>development</li>
	 *   <li>debug</li>
	 *   <li>trace</li>
	 * </ul>
	 * モードによってClickの動作（HTMLテンプレートのリロードを行うかどうか等）やログに出力される内容が変化します。
	 * 詳細については<a href="http://click.sourceforge.net/docs/configuration.html#application-configuration">Clickのドキュメント</a>を参照してください。
	 */
	public String mode = "development";

	/**
	 * 共通レスポンスヘッダ。
	 */
	public Map<String, String> headers = new HashMap<String, String>();

	/**
	 * ロケール。
	 */
	public String locale;

//	/**
//	 * ページクラスにリクエストパラメータを自動バインドするかどうか。
//	 * デフォルトは<code>true</code>です。
//	 */
//	public boolean autoBinding = true;

	/**
	 * コントロールセットを定義した設定ファイル群のパス。
	 */
	public List<String> controlSets = new ArrayList<String>();

	/**
	 * デプロイ（ファイルの展開）が必要なコントロールクラス群。
	 */
	public List<Class<? extends Control>> controls = new ArrayList<Class<? extends Control>>();

	/**
	 * Clickの<code>FileField</code>コントロールが使用するCommons FileUploadの<code>FileItemFactory</code>のインスタンス。
	 * デフォルトは<code>org.apache.commons.fileupload.disk.DiskFileItemFactory</code>です。
	 */
	@Binding(bindingType = BindingType.NONE)
	public FileItemFactory fileItemFactory = new DiskFileItemFactory();

	public S2ClickConfig(){
		controlSets.add("org/seasar/s2click/s2click-controls.xml");
//		controls.add(ToolTip.class);
//		controls.add(CodePrettify.class);
	}

}
