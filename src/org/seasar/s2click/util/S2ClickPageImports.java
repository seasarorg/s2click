/*
 * Copyright 2006-2009 the Seasar Foundation and the Others.
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
package org.seasar.s2click.util;

import java.util.List;

import net.sf.click.Control;
import net.sf.click.Page;
import net.sf.click.control.FieldSet;
import net.sf.click.control.Form;
import net.sf.click.control.Panel;
import net.sf.click.control.Table;
import net.sf.click.util.Format;
import net.sf.click.util.PageImports;

/**
 * Provides a utility object for rendering a Page's control HTML header imports.
 * <p/>
 * A <tt>PageImports</tt> instance is automatically added to the Velocity Context
 * for Velocity templates, or as a request attribute for JSP pages using the key
 * name "<span class="blue">imports</span>".
 * <p/>
 * To use the <tt>PageImports</tt> object simply reference it your page header
 * section. For example:
 * <pre class="codeHtml">
 * &lt;html&gt;
 *  &lt;head&gt;
 *   <span class="blue">$imports</span>
 *  &lt;/head&gt;
 *  &lt;body&gt;
 *   <span class="red">$form</span>
 *  &lt;body&gt;
 * &lt;/html&gt; </pre>
 *
 * You should also follow the performance best practice by importing CSS includes
 * in the head section, then include the JS imports after the html body.
 * For example:
 * <pre class="codeHtml">
 * &lt;html&gt;
 *  &lt;head&gt;
 *   <span class="blue">$cssImports</span>
 *  &lt;/head&gt;
 *  &lt;body&gt;
 *   <span class="red">$form</span>
 *   &lt;br/&gt;
 *   <span class="red">$table</span>
 *  &lt;body&gt;
 * &lt;/html&gt;
 * <span class="blue">$jsImports</span>
 * </pre>
 *
 * Please also see {@link Control#getHtmlImports()}.
 *
 * @see Format
 *
 * @author Malcolm Edgar
 */
public class S2ClickPageImports extends PageImports {

    public S2ClickPageImports(Page page) {
        super(page);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void processControl(Control control) {
        processLine(control.getHtmlImports());

        if (control instanceof Form) {
            Form form = (Form) control;
            List controls = form.getFieldList();
            for (int i = 0, size = controls.size(); i < size; i++) {
                processControl((Control) controls.get(i));
            }
            List buttons = form.getButtonList();
            for (int i = 0, size = buttons.size(); i < size; i++) {
                processControl((Control) buttons.get(i));
            }

        } else if (control instanceof FieldSet) {
            FieldSet fieldSet = (FieldSet) control;
            List controls = fieldSet.getFieldList();
            for (int i = 0, size = controls.size(); i < size; i++) {
                processControl((Control) controls.get(i));
            }

        } else if (control instanceof Panel) {
            Panel panel = (Panel) control;
            if (panel.hasControls()) {
                List controls = panel.getControls();
                for (int i = 0, size = controls.size(); i < size; i++) {
                    processControl((Control) controls.get(i));
                }
            }

        } else if (control instanceof Table) {
            Table table = (Table) control;
            if (table.hasControls()) {
                List controls = table.getControls();
                for (int i = 0, size = controls.size(); i < size; i++) {
                    processControl((Control) controls.get(i));
                }
            }
        }
    }

}
