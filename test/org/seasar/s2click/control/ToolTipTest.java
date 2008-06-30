package org.seasar.s2click.control;

import junit.framework.TestCase;

public class ToolTipTest extends TestCase {

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
		ToolTip tooltip = new ToolTip("tooltip", "index.html", "�^�C�g��");
		assertEquals("tooltip", tooltip.getName());
		assertEquals("index.html", tooltip.getContents());
		assertEquals("�^�C�g��", tooltip.getTitle());
	}
	
	public void testToolTip6() {
		ToolTip tooltip = new ToolTip("tooltip", "index.html", "�^�C�g��", 999);
		assertEquals("tooltip", tooltip.getName());
		assertEquals("index.html", tooltip.getContents());
		assertEquals("�^�C�g��", tooltip.getTitle());
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
		tooltip.setTitle("�^�C�g��");
		assertEquals("�^�C�g��", tooltip.getTitle());
	}

	public void testGetLabel() {
		ToolTip tooltip = new ToolTip();
		assertNull(tooltip.getLabel());
		tooltip.setLabel("���x��");
		assertEquals("���x��", tooltip.getLabel());
	}

}
