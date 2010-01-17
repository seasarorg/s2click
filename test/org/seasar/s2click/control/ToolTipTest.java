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

import org.seasar.s2click.test.S2ClickTestCase;


public class ToolTipTest extends S2ClickTestCase {

	public void testToolTip1() {
		ToolTip tooltip = new ToolTip();
		assertNull(tooltip.getName());
		assertNull(tooltip.getContents());
	}

	public void testToolTip2() {
		ToolTip tooltip = new ToolTip("tooltip");
		assertEquals("tooltip", tooltip.getName());
		assertNull(tooltip.getContents());
	}
	
	public void testToolTip3() {
		ToolTip tooltip = new ToolTip("tooltip", "index.html");
		assertEquals("tooltip", tooltip.getName());
		assertEquals("index.html", tooltip.getContents());
	}
	
	public void testToolTip4() {
		ToolTip tooltip = new ToolTip("tooltip", "index.html", 999);
		assertEquals("tooltip", tooltip.getName());
		assertEquals("index.html", tooltip.getContents());
		assertEquals(999, tooltip.getWidth());
	}
	
	public void testToolTip5() {
		ToolTip tooltip = new ToolTip("tooltip", "index.html", "タイトル");
		assertEquals("tooltip", tooltip.getName());
		assertEquals("index.html", tooltip.getContents());
		assertEquals("タイトル", tooltip.getTitle());
	}
	
	public void testToolTip6() {
		ToolTip tooltip = new ToolTip("tooltip", "index.html", "タイトル", 999);
		assertEquals("tooltip", tooltip.getName());
		assertEquals("index.html", tooltip.getContents());
		assertEquals("タイトル", tooltip.getTitle());
		assertEquals(999, tooltip.getWidth());
	}
	
	public void testGetContents() {
		ToolTip tooltip = new ToolTip();
		assertNull(tooltip.getContents());
		tooltip.setContents("index.html");
		assertEquals("index.html", tooltip.getContents());
	}

	public void testGetWidth() {
		ToolTip tooltip = new ToolTip();
		assertEquals(300, tooltip.getWidth());
		tooltip.setWidth(999);
		assertEquals(999, tooltip.getWidth());
	}

	public void testGetTitle() {
		ToolTip tooltip = new ToolTip();
		assertNull(tooltip.getTitle());
		tooltip.setTitle("タイトル");
		assertEquals("タイトル", tooltip.getTitle());
	}

	public void testGetLabel() {
		ToolTip tooltip = new ToolTip();
		assertNull(tooltip.getLabel());
		tooltip.setLabel("ラベル");
		assertEquals("ラベル", tooltip.getLabel());
	}

}
