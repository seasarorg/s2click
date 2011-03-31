/*
 * Copyright 2006-2011 the Seasar Foundation and the Others.
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

import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.control.PaginateList;

/**
 * PaginateListのサンプルページです。
 *
 * @author Naoki Takezoe
 */
@Layout
public class PaginateListPage extends S2ClickPage {

	private static final long serialVersionUID = 1L;

	public String title = "PaginateList";

	public List<String> list = new ArrayList<String>();
	public PaginateList paginateList = new PaginateList("paginateList");

	public PaginateListPage(){
		list.add("Java");
		list.add("C#");
		list.add("Perl");
		list.add("Ruby");
		list.add("Python");

		paginateList.setRowList(list);
		paginateList.setPageSize(3);
		paginateList.setTemplatePath("/paginate.htm");
	}

}
