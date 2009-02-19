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
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class uses getResourceBundle() method to read the resources when
 *  they are defined inside the "resource-bundle" tag.
 */

public class GetResourceBundleSameNameTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetResourceBundleSameNameTest";

    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            ResourceBundle resourceBundle = 
                        getPortletConfig().getResourceBundle(Locale.ENGLISH);

            if (resourceBundle != null) {
                String expectedValue = "GetResourceBundleSameNameTestPortlet";
				String inLineExpectedValue = 
										"InLineGetResourceBundleSameNameTest";
                String actualValue = 
                        resourceBundle.getString("javax.portlet.title");
                if (expectedValue.equals(actualValue)) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else if (expectedValue.equals(inLineExpectedValue)) {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("Picked up the property value "
								 		  + "from inline instead from the "
										  + "resource bundle file");
                    resultWriter.addDetail("Expected value:" + expectedValue);
                    resultWriter.addDetail("Actual value:" + actualValue);
				}
                else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("For key:preferredLanguage");
                    resultWriter.addDetail("Expected value:" + expectedValue);
                    resultWriter.addDetail("Actual value:" + actualValue);
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
            resultWriter.addDetail("MissingResourceException() is thrown "
                                   + "when resource bundle for the "
                                   + "given locale was defined in the "
                                   + "deployment descriptor with "
                                   + "resource-bundle tag.");
        }
        out.println(resultWriter.toString());
    }
}
