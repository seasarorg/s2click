package org.seasar.s2click.example.form;

import net.sf.click.control.Option;
import net.sf.click.extras.control.CheckList;
import net.sf.click.extras.control.PickList;

import org.seasar.s2click.control.S2ClickForm;
import org.seasar.s2click.control.DateFieldYYYYMMDD;

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
	}
	
	public DateFieldYYYYMMDD dateField = new DateFieldYYYYMMDD("date", "日付");
	public CheckList checkList = new CheckList("checkList", "チェックリスト");
	public PickList pickList = new PickList("pickList", "ピックリスト");
	
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
