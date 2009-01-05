package org.seasar.s2click.example.form;

import net.sf.click.control.Option;
import net.sf.click.control.Submit;
import net.sf.click.extras.control.CheckList;
import net.sf.click.extras.control.PickList;

import org.seasar.s2click.control.DateFieldYYYYMMDD;
import org.seasar.s2click.control.FCKEditor;
import org.seasar.s2click.control.HiddenList;
import org.seasar.s2click.control.S2ClickForm;

public class ControlsForm extends S2ClickForm {
	
	private static final long serialVersionUID = 1L;
	
	public ControlsForm(String name){
		super(name);
		setFieldAutoRegisteration(true);
		
		checkList.add("Click Framework");
		checkList.add("Wicket");
		checkList.add("Grails");
		
		pickList.add(new Option("Java"));
		pickList.add(new Option("C#"));
		pickList.add(new Option("Ruby"));
		pickList.add(new Option("Perl"));
		pickList.add(new Option("Python"));
		
		hiddenList.addValue("Eclipse");
		hiddenList.addValue("NetBeans");
	}
	
	public DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD("date", "日付");
	public CheckList checkList = new CheckList("checkList", "チェックリスト");
	public PickList pickList = new PickList("pickList", "ピックリスト");
	public FCKEditor fckEditor = new FCKEditor("fckEditor", "FCKEditor");
	public HiddenList hiddenList = new HiddenList("hiddenList");
	public Submit submit = new Submit("submit", "送信");
	
//	public AutoCompleteTextField completion 
//		= new AutoCompleteTextField("completion", "入力補完"){
//
//		private static final long serialVersionUID = 1L;
//		
//		@SuppressWarnings("unchecked")
//		@Override public List getAutoCompleteList(String criteria) {
//			List<String> list = new ArrayList<String>();
//			list.add("Java");
//			list.add("JavaScript");
//			list.add("Perl");
//			list.add("Python");
//			list.add("Ruby");
//			return list;
//		}
//	};
	
}
