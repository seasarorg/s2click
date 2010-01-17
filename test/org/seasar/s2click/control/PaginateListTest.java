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
package org.seasar.s2click.control;

import java.util.ArrayList;
import java.util.List;

import org.seasar.s2click.test.S2ClickTestCase;

public class PaginateListTest extends S2ClickTestCase {

	public void testOnProcess() {
		request.setParameter("actionLink", "list-controlLink");
		request.setParameter("page", "10");
		
		PaginateList paginateList = new PaginateList("list");
		
		paginateList.onProcess();
		assertEquals(10, paginateList.getPageNumber());
	}

	public void testPaginateList() {
		PaginateList paginateList = new PaginateList();
		assertNull(paginateList.getName());
	}

	public void testPaginateListString() {
		PaginateList paginateList = new PaginateList("list");
		assertEquals("list", paginateList.getName());
	}

	public void testPaginateListStringString() {
		PaginateList paginateList = new PaginateList("list", "/paginateList.htm");
		assertEquals("list", paginateList.getName());
		assertEquals("/paginateList.htm", paginateList.getTemplatePath());
	}

	public void testSetNameString() {
		PaginateList paginateList = new PaginateList();
		paginateList.setName("list");
		
		assertEquals("list", paginateList.getName());
		assertEquals("list-controlLink", paginateList.controlLink.getName());
	}

	public void testSetPageNumber() {
		PaginateList paginateList = new PaginateList();
		
		assertEquals(0, paginateList.getPageNumber());
		
		paginateList.setPageNumber(1);
		assertEquals(1, paginateList.getPageNumber());
	}

	public void testSetPageSize() {
		PaginateList paginateList = new PaginateList();
		
		assertEquals(0, paginateList.getPageSize());
		
		paginateList.setPageSize(1);
		assertEquals(1, paginateList.getPageSize());
	}

	public void testSetRowList() {
		PaginateList paginateList = new PaginateList();
		
		assertTrue(paginateList.getRowList().isEmpty());
		
		List<Object> rowList = new ArrayList<Object>();
		paginateList.setRowList(rowList);
		
		assertSame(rowList, paginateList.getRowList());
	}

	public void testSetTemplatePath() {
		PaginateList paginateList = new PaginateList();
		paginateList.setTemplatePath("/paginateList.htm");
		
		assertEquals("/paginateList.htm", paginateList.getTemplatePath());
	}

	public void testGetPager_1() {
		PaginateList paginateList = new PaginateList("list");
		
		List<String> rowList = new ArrayList<String>();
		rowList.add("1");
		rowList.add("2");
		rowList.add("3");
		rowList.add("4");
		rowList.add("5");
		
		paginateList.setRowList(rowList);
		paginateList.setPageNumber(0);
		paginateList.setPageSize(2);
		
		assertEquals("<span class=\"disabled\">&lt;前へ</span>&nbsp;"
				+ "<a href=\"/sample/sample.htm?actionLink=list-controlLink&amp;page=1\" class=\"disabled\">次へ&gt;</a>",
			paginateList.getPager());
	}

	public void testGetPager_2() {
		PaginateList paginateList = new PaginateList("list");
		
		List<String> rowList = new ArrayList<String>();
		rowList.add("1");
		rowList.add("2");
		rowList.add("3");
		rowList.add("4");
		rowList.add("5");
		
		paginateList.setRowList(rowList);
		paginateList.setPageNumber(1);
		paginateList.setPageSize(2);
		
		assertEquals("<a href=\"/sample/sample.htm?actionLink=list-controlLink&amp;page=0\">&lt;前へ</a>&nbsp;"
				+ "<a href=\"/sample/sample.htm?actionLink=list-controlLink&amp;page=2\">次へ&gt;</a>",
			paginateList.getPager());
	}
	
	public void testGetPager_3() {
		PaginateList paginateList = new PaginateList("list");
		
		List<String> rowList = new ArrayList<String>();
		rowList.add("1");
		rowList.add("2");
		rowList.add("3");
		rowList.add("4");
		rowList.add("5");
		
		paginateList.setRowList(rowList);
		paginateList.setPageNumber(2);
		paginateList.setPageSize(2);
		
		assertEquals("<a href=\"/sample/sample.htm?actionLink=list-controlLink&amp;page=1\">&lt;前へ</a>&nbsp;"
				+ "<span class=\"disabled\">次へ&gt;</span>",
			paginateList.getPager());
	}
	
	public void testGetDisplayList_1() {
		PaginateList paginateList = new PaginateList();
		
		List<String> rowList = new ArrayList<String>();
		rowList.add("1");
		rowList.add("2");
		rowList.add("3");
		
		paginateList.setRowList(rowList);
		paginateList.setPageNumber(0);
		paginateList.setPageSize(5);
		
		List<?> displayList = paginateList.getDisplayList();
		assertEquals(3, displayList.size());
		assertEquals("1", displayList.get(0));
		assertEquals("2", displayList.get(1));
		assertEquals("3", displayList.get(2));
	}

	public void testGetDisplayList_2() {
		PaginateList paginateList = new PaginateList();
		
		List<String> rowList = new ArrayList<String>();
		rowList.add("1");
		rowList.add("2");
		rowList.add("3");
		rowList.add("4");
		rowList.add("5");
		
		paginateList.setRowList(rowList);
		paginateList.setPageNumber(1);
		paginateList.setPageSize(2);
		
		List<?> displayList = paginateList.getDisplayList();
		assertEquals(2, displayList.size());
		assertEquals("3", displayList.get(0));
		assertEquals("4", displayList.get(1));
	}
	
	public void testGetDisplayList_3() {
		PaginateList paginateList = new PaginateList();
		
		List<String> rowList = new ArrayList<String>();
		rowList.add("1");
		rowList.add("2");
		rowList.add("3");
		rowList.add("4");
		rowList.add("5");
		
		paginateList.setRowList(rowList);
		paginateList.setPageNumber(2);
		paginateList.setPageSize(2);
		
		List<?> displayList = paginateList.getDisplayList();
		assertEquals(1, displayList.size());
		assertEquals("5", displayList.get(0));
	}
	
	/**
	 * テンプレートを指定していない場合
	 */
	public void testToString_1() {
		PaginateList paginateList = new PaginateList("list");
		try {
			paginateList.toString();
			fail();
		} catch(RuntimeException ex){
			assertEquals("テンプレートが指定されていません。", ex.getMessage());
		}
	}
	
	public void testToString_2() {
		PaginateList paginateList = new PaginateList("list", "/paginateList.htm");
		
		List<String> rowList = new ArrayList<String>();
		rowList.add("1");
		rowList.add("2");
		rowList.add("3");
		rowList.add("4");
		rowList.add("5");
		
		paginateList.setRowList(rowList);
		paginateList.setPageNumber(0);
		paginateList.setPageSize(2);
		
		paginateList.toString();
		
		assertEquals("/paginateList.htm", templateService.getTemplatePath());
		assertEquals(0, templateService.getModel().get("pageNumber"));
		assertEquals(5, templateService.getModel().get("totalRows"));
		assertEquals(2, templateService.getModel().get("totalPages"));
		
		assertEquals(0, templateService.getModel().get("beginIndex"));
		assertEquals(1, templateService.getModel().get("endIndex"));
		
		List<?> displayList = (List<?>) templateService.getModel().get("list");
		assertEquals(2, displayList.size());
		assertEquals("1", displayList.get(0));
		assertEquals("2", displayList.get(1));
	}

}
