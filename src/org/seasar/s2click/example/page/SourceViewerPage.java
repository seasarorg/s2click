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
package org.seasar.s2click.example.page;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.click.Page;
import org.apache.click.util.ClickUtils;
import org.apache.commons.io.IOUtils;
import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.annotation.Layout;
import org.seasar.s2click.control.CodePrettify;

/**
 * Provides a Java source code, HTML and XML examples rendering page.
 *
 * @author Malcolm Edgar
 * @author Naoki Takezoe
 */
@Layout
public class SourceViewerPage extends S2ClickPage {

	private static final long serialVersionUID = 1L;

	/**
     * @see Page#onGet()
     */
    public void onGet() {
        HttpServletRequest request = getContext().getRequest();

        String filename = request.getParameter("filename");

        if (filename != null) {
        	addModel("title", filename);
            loadFilename(filename);

        } else {
        	addModel("title", "エラー");
            addModel("error", "filename not defined");
        }
    }

    private void loadFilename(String filename) {
        ServletContext context = getContext().getServletContext();

        // Orion server requires '/' prefix to find resources
        String resourceFilename =
            (filename.charAt(0) != '/') ? "/" + filename : filename;

        InputStream in = null;
        try {
            in = context.getResourceAsStream(resourceFilename);

            if (in == null && filename.endsWith(".htm")) {
                resourceFilename =
                    resourceFilename.substring(0, resourceFilename.length() - 4)
                    + ".jsp";

                in = context.getResourceAsStream(resourceFilename);
            }

            if (in != null) {
            	String lang = filename.endsWith(".java") ?
            			CodePrettify.LANG_JAVA : CodePrettify.LANG_HTML;

            	String code = IOUtils.toString(in, "UTF-8");
            	CodePrettify codePrettify = new CodePrettify("codePrettify");
            	codePrettify.setCode(code);
            	codePrettify.setLang(lang);
            	addControl(codePrettify);

            } else {
                addModel("error", "File " + resourceFilename + " not found");
            }

        } catch (IOException e) {
            addModel("error", "Could not read " + resourceFilename);

        } finally {
            ClickUtils.close(in);
        }
    }

}
