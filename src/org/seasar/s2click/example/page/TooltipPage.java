package org.seasar.s2click.example.page;

import org.seasar.s2click.control.ToolTip;

public class TooltipPage extends LayoutPage {
	
	public ToolTip tooltipIcon = new ToolTip("tooltipIcon", "help.html", "ヘルプ");
	
	public ToolTip tooltipText = new ToolTip("tooltipText", "help.html", "ヘルプ");
	
	public TooltipPage(){
		tooltipText.setLabel("テキスト");
	}
}
