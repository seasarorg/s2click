package org.seasar.s2click.example.page;

import org.seasar.s2click.annotation.Path;
import org.seasar.s2click.example.form.ControlsForm;

@Path("/controls.htm")
public class ControlsPage extends LayoutPage {
	
	public String title = "Click標準のコントロール";
	public String template = "/form.htm";
	public ControlsForm form = new ControlsForm("form");

}
