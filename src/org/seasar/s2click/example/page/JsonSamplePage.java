package org.seasar.s2click.example.page;

import java.util.ArrayList;
import java.util.List;

import org.seasar.s2click.annotation.Path;
import org.seasar.s2click.page.AbstractJSONPage;

/**
 * JSON��ԋp����y�[�W�N���X�̃T���v���B
 * 
 * @author Naoki Takezoe
 * @see JsonPage
 */
@Path("/json-sample.htm")
public class JsonSamplePage extends AbstractJSONPage {
	
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
	
	public JsonSamplePage(){
		List<Book> books = new ArrayList<Book>();
		books.add(new Book("����Wiki", "�����R�~���j�P�[�V�����Y", "�|�Y ����", 1000));
		books.add(new Book("Eclipse�v���O�C���J���O��U��", "�����R�~���j�P�[�V�����Y", "�|�Y ����", 1000));
		books.add(new Book("Eclipse�t�����N�C�b�N���t�@�����X", "�����R�~���j�P�[�V�����Y", "�|�Y ����", 1000));
		books.add(new Book("�ƏKJavaScript", "�ĉj��", "�|�Y ����", 1000));
		
		setContents(books);
	}
	
}
