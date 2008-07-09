package org.seasar.s2click.example.page;

import org.seasar.s2click.control.ToolTip;

/**
 * {@link ToolTip}�R���g���[���̃T���v���y�[�W�B
 * 
 * @author Naoki Takezoe
 * @see ToolTip
 */
public class TooltipPage extends LayoutPage {
	
	public String title = "�c�[���`�b�v�w���v";
	
	public ToolTip tooltipIcon = new ToolTip("tooltipIcon", "help.html", "�w���v");
	
	public ToolTip tooltipText = new ToolTip("tooltipText", "help.html", "�w���v");
	
	public TooltipPage(){
		tooltipText.setLabel("�e�L�X�g");
	}
}
