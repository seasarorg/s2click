package org.seasar.s2click.example.page;

import java.util.ArrayList;
import java.util.List;

import org.seasar.s2click.control.AjaxRequestLink;

/**
 * JSONを使用したAjaxのサンプルページ。
 * 
 * @author Naoki Takezoe
 * @see JsonSamplePage
 */
public class JsonPage extends LayoutPage {
	
	public String title = "JSONを使用したAjaxアプリケーション";
	public AjaxRequestLink link = new AjaxRequestLink("link", "Ajaxのテスト");
	
	public JsonPage(){
		link.addAjaxHandler("onComplete", "displayResult");
		link.setListener(this, "onAjaxLink");
	}
	
	public boolean onAjaxLink(){
		List<Book> books = new ArrayList<Book>();
		books.add(new Book("入門Wiki", "毎日コミュニケーションズ", "竹添 直樹", 1000));
		books.add(new Book("Eclipseプラグイン開発徹底攻略", "毎日コミュニケーションズ", "竹添 直樹", 1000));
		books.add(new Book("Eclipse逆引きクイックリファレンス", "毎日コミュニケーションズ", "竹添 直樹", 1000));
		books.add(new Book("独習JavaScript", "翔泳社", "竹添 直樹", 1000));
		
		renderJSON(books);
		return false;
	}
	
	public static class Book {
		public String title;
		public String publisher;
		public String author;
		public int price;
		
		public Book(String title, String publisher, String author, int price){
			this.title = title;
			this.publisher = publisher;
			this.author = author;
			this.price = price;
		}
	}

	
}
