/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.GenericPortlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the getResourceBundle(Locale) method.
 */
public class GetResourceBundleTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetResourceBundleTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            ResourceBundle resourceBundle = getResourceBundle(Locale.ENGLISH);

            if (resourceBundle != null) {
                String expectedResult = "Java";
                String result = resourceBundle.getString("preferredLanguage");

                if ((result != null) && result.equals(expectedResult)) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("For key: preferredLanguage");
                    resultWriter.addDetail("Expected result = " + expectedResult);
                    resultWriter.addDetail("Actual result = " + result);
                }
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("getResourceBundle() returns "
                                       + "null when resource bundle "
                                       + "for the given locale was "
                                       + "defined in the deployment "
                                       + "descriptor with "
                                       + "resource-bundle tag.");
            }
        } catch (MissingResourceException e) {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("MissingResourceException is thrown "
                                   + "when resource bundle for the "
                                   + "given locale was defined in the "
                                   + "deployment descriptor with "
                                   + "resource-bundle tag.");
        }

        out.println(resultWriter.toString());
    }
}
