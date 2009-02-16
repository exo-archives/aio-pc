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
 *	This class uses getResourceBundle() method to read the resources when
 *  they are inlined in the descriptor file.
 */

public class GetResourceBundleOnlyInlineTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetResourceBundleOnlyInlineTest";

    /**
     *	Gets the resource bundle from the descriptor file.
     *  
     */
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException{

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortletConfig portletConfig = getPortletConfig();

		if (portletConfig != null) {
        	//get the resource bundle
			try {
                ResourceBundle resourceBundle = 
                        portletConfig.getResourceBundle(Locale.getDefault());
                if (resourceBundle == null) { 
                    resultWriter.setStatus(ResultWriter.FAIL); 
                    resultWriter.addDetail("getResourceBundle() should have "
                                         + "used the inline values defined "
                                         + "in the deployment descriptor to "
                                         + "return a descriptor.");
                }
                else {
                    String expectedValue = "GetResourceBundleOnlyInlineTestPortlet";
                    String actualValue = 
                                resourceBundle.getString("javax.portlet.title");
                    if (expectedValue != null && 
                                        expectedValue.equals(actualValue)) { 
                        resultWriter.setStatus(ResultWriter.PASS); 
                    }
                    else {
                        resultWriter.setStatus(ResultWriter.FAIL);
                        resultWriter.addDetail("For key:title");
                        resultWriter.addDetail("Expected value:"+expectedValue);
                        resultWriter.addDetail("Actual value:" + actualValue);
                    }
                }
            }
            catch(MissingResourceException ex) {
                    ex.printStackTrace();
                    resultWriter.setStatus(ResultWriter.FAIL); 
                    resultWriter.addDetail("getResourceBundle() threw a "
                                         + "MissingResourceBundle exception.");
                    resultWriter.addDetail("getResourceBundle() should have "
                                         + "used the inline values defined in " 
                                         + "the deployment descriptor to "
                                         + "return a descriptor.");
            }
    	} else {
            resultWriter.setStatus(ResultWriter.FAIL); 
			resultWriter.addDetail("getPortletConfig() returned null" );
		}
        out.println(resultWriter.toString());
	}
}
