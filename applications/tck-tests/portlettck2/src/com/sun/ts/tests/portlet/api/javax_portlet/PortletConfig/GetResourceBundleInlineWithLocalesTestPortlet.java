/**
 * Copyright 2007 IBM Corporation.
 */
/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class uses getResourceBundle() to read resources for multiple locales
 *  when the resources are actually defined inline in the descriptor file.
 */

public class GetResourceBundleInlineWithLocalesTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetResourceBundleInlineWithLocalesTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortletConfig portletConfig = getPortletConfig();

        Locale locales[] = new Locale[] {
                            new Locale("en", "US"),
                            new Locale("fr", "FR")
                            };

        resultWriter.setStatus(ResultWriter.PASS);

		if(portletConfig != null) {
            for(int i = 0; i < locales.length; i++) {
                     validateTitleString(resultWriter, 
                                        portletConfig, 
                                        locales[i]);
            }
        }
        out.println(resultWriter.toString());
    }

    /**
     * The method will validate the the title string
     * read from the resource
     */
    private void validateTitleString(ResultWriter resultWriter, 
                                     PortletConfig config, 
                                     Locale locale) { 
        String expected = "GetResourceBundleInlineWithLocalesTestPortlet";
        try {
            ResourceBundle resourceBundle = 
                    config.getResourceBundle(locale);
            if (resourceBundle == null) { 
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(
                        "Obtained a empty resource bundle object for the " 
                       + locale + " locale");
            }
            else {
                String resourceTitle = 
                    resourceBundle.getString("javax.portlet.title");
                if ((resourceTitle == null) || 
                            (!resourceTitle.equals(expected))) {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail(
                            "Expected the value for key title:" + expected
                            + "\n Actual value for locale " + locale
                            + ":" + resourceTitle);
                }
            } 
        }
        catch(MissingResourceException ex) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(
                "getResourceBundle threw a MissingResourceException "
                + " for locale " + locale);
        }
    }
}
