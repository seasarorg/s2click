package org.seasar.s2click.example.page;

import org.seasar.s2click.control.ToolTip;

public class TooltipPage extends LayoutPage {
	
	public ToolTip tooltipIcon = new ToolTip("tooltipIcon", "help.html", "�w���v");
	
	public ToolTip tooltipText = new ToolTip("tooltipText", "help.html", "�w���v");
	
	public TooltipPage(){
		tooltipText.setLabel("�e�L�X�g");
	}
}
