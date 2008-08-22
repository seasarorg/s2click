package org.seasar.s2click.example.page;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.click.Page;
import net.sf.click.util.ClickUtils;

import org.apache.commons.io.IOUtils;
import org.seasar.s2click.control.CodePrettify;

/**
 * Provides a Java source code, HTML and XML examples rendering page.
 *
 * @author Malcolm Edgar
 * @author Naoki Takezoe
 */
public class SourceViewerPage extends LayoutPage {
	
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
