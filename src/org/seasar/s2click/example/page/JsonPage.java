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
package org.seasar.s2click.example.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.seasar.s2click.annotation.Ajax;
import org.seasar.s2click.control.AjaxButton;
import org.seasar.s2click.example.form.JsonForm;
import org.seasar.s2click.util.AjaxUtils;

/**
 * JSONを使用したAjaxのサンプルページ。
 *
 * @author Naoki Takezoe
 * @see JsonSamplePage
 */
public class JsonPage extends LayoutPage {

	private static final long serialVersionUID = 1L;

	public String title = "JSONを使用したAjaxアプリケーション";

	public JsonForm form = new JsonForm("form");
	public AjaxButton button = new AjaxButton(
			"button", "Ajax.Updaterのテスト", this, "onAjaxUpdater");

	public void onInit(){
		super.onInit();
		
		form.search.setActionListener(new ActionListener(){
			private static final long serialVersionUID = 1L;

			public boolean onAction(Control source) {
				return onSearch();
			}
		});
		form.searchAll.setActionListener(new ActionListener(){
			private static final long serialVersionUID = 1L;

			public boolean onAction(Control source) {
				return onSearchAll();
			}
		});

		button.setElementId("target");
		button.addAjaxHandler(AjaxUtils.ON_CREATE, "startProgress");
		button.addAjaxHandler(AjaxUtils.ON_SUCCESS, "stopProgress");
	}

	private List<Book> getBookList(){
		List<Book> books = new ArrayList<Book>();
		books.add(new Book("入門Wiki", "毎日コミュニケーションズ", "竹添 直樹", 1000));
		books.add(new Book("Eclipseプラグイン開発徹底攻略", "毎日コミュニケーションズ", "竹添 直樹", 1000));
		books.add(new Book("Eclipse逆引きクイックリファレンス", "毎日コミュニケーションズ", "竹添 直樹", 1000));
		books.add(new Book("独習JavaScript", "翔泳社", "竹添 直樹", 1000));
		return books;
	}

	protected boolean onSearch(){
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

	protected boolean onSearchAll(){
		renderJSON(getBookList());
		return false;
	}

	public boolean onAjaxUpdater(){
		renderHTML("<h2>Ajaxでコンテンツを置換しました</h2>");
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
	
	@Ajax
	public String hello(String name){
		return "こんにちは、" + name + "さん！";
	}


}
