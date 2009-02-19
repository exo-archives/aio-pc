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
 *  they are not defined in the resource bundle file but are defined as inline.
 */

public class GetResourceBundleBothTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetResourceBundleBothTest";

    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		String errMsg = "The container failed to return the value of the "
						+ "property which was defined as inline but was not "
						+ "defined in the resource bundle file";

        try {
            ResourceBundle resourceBundle = 
                        getPortletConfig().getResourceBundle(Locale.ENGLISH);

            if (resourceBundle != null) {
                String expectedValue = "Unix";
				Object actualValue =
							resourceBundle.getObject("javax.portlet.keywords");
				if (actualValue != null) {
					if (actualValue instanceof String) {
                		if (expectedValue.equals(actualValue)) {
                      		resultWriter.setStatus(ResultWriter.PASS);
						} else  {
                    		resultWriter.setStatus(ResultWriter.FAIL);
                    		resultWriter.addDetail(errMsg);
                    		resultWriter.addDetail("Expected value:"
																+ expectedValue);
                    		resultWriter.addDetail("Actual value:" 
																+ actualValue);
                		}
                	} else if (actualValue instanceof String[]) {
						String[] valueArray = (String[])actualValue;
						if ((valueArray.length > 0) && 
					   		(expectedValue.equals(valueArray[0]))) {
                      	 	resultWriter.setStatus(ResultWriter.PASS);
						} else {
							resultWriter.setStatus(ResultWriter.FAIL);
                    		resultWriter.addDetail(errMsg);
                    		resultWriter.addDetail("Expected value:"
																+ expectedValue);
							if (valueArray.length > 0) {
                    	  		resultWriter.addDetail("Actual value:"
																+ valueArray[0]);
							} else {
                    	  		resultWriter.addDetail("Actual value: null");
							}
                		}
					} else {
						resultWriter.setStatus(ResultWriter.FAIL);
                    	resultWriter.addDetail(errMsg);
                    	resultWriter.addDetail("The expected object type was "
											 + "neither String or String[]");
					}
				} else {
					resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("The value of the returned property "
										 + "was null");
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
            resultWriter.addDetail("MissingResourceException() thrown "
                                   + "when resource bundle for the "
                                   + "given locale was defined in the "
                                   + "deployment descriptor with "
                                   + "resource-bundle tag.");
        }
        out.println(resultWriter.toString());
    }
}
