package org.seasar.s2click.example.page;

import java.util.ArrayList;
import java.util.List;

import org.seasar.s2click.control.AjaxUpdaterLink;
import org.seasar.s2click.example.form.JsonForm;

/**
 * JSONを使用したAjaxのサンプルページ。
 * 
 * @author Naoki Takezoe
 * @see JsonSamplePage
 */
public class JsonPage extends LayoutPage {
	
	public String title = "JSONを使用したAjaxアプリケーション";
	
	public JsonForm form = new JsonForm("form");
	public AjaxUpdaterLink link = new AjaxUpdaterLink(
			"link", "Ajax.Updaterのテスト", this, "onAjaxUpdater");
	
	public JsonPage(){
		form.search.setListener(this, "onSearch");
		form.searchAll.setListener(this, "onSearchAll");
		addControl(form.searchAll);
		
		link.setElementId("target");
	}
	
	private List<Book> getBookList(){
		List<Book> books = new ArrayList<Book>();
		books.add(new Book("入門Wiki", "毎日コミュニケーションズ", "竹添 直樹", 1000));
		books.add(new Book("Eclipseプラグイン開発徹底攻略", "毎日コミュニケーションズ", "竹添 直樹", 1000));
		books.add(new Book("Eclipse逆引きクイックリファレンス", "毎日コミュニケーションズ", "竹添 直樹", 1000));
		books.add(new Book("独習JavaScript", "翔泳社", "竹添 直樹", 1000));
		return books;
	}
	
	public boolean onSearch(){
		String keyword = form.keyword.getValue();
		
		if(keyword.length() != 0){
			List<Book> bookList = new ArrayList<Book>();
			for(Book book: getBookList()){
				if(book.title.indexOf(keyword) >= 0){
					bookList.add(book);
				}
			}
			renderJSON(bookList);
		} else {
			renderJSON(getBookList());
		}
		return false;
	}
	
	public boolean onSearchAll(){
		renderJSON(getBookList());
		return false;
	}
	
	public boolean onAjaxUpdater(){
		renderHTML("<strong>Ajaxでコンテンツを置換します</strong>");
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
