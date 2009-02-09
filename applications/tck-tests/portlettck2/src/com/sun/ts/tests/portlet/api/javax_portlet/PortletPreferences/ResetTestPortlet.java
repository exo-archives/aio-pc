/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletPreferences;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *  This class will test the reset() method.
 */

public class ResetTestPortlet extends GenericPortlet {

    public static String TEST_NAME="ResetTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		String expectedValue = "Java";

		PortletPreferences preferences = request.getPreferences();

		if (preferences != null) {
			String actualValue = 
                                preferences.getValue("preferredLanguage", null);
			if (actualValue != null) {

                preferences.setValue("preferredLanguage", "XML");

				// Reset the value for this preference key
				preferences.reset("preferredLanguage");

				// Try reading back the value
				actualValue = preferences.getValue("preferredLanguage", null);

                if (actualValue != null) {
				    if (actualValue.equals(expectedValue)) { 
					    resultWriter.setStatus(ResultWriter.PASS);
				    } else {
					    resultWriter.setStatus(ResultWriter.FAIL);
					    resultWriter.addDetail("Expected preference value = "
                                                            + expectedValue);
					    resultWriter.addDetail("Actual preference value = " 
                                                            + actualValue);
				    }
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("Expected preference value = "
                                                            + expectedValue);
                    resultWriter.addDetail("Actual preference value = null"); 
                }
		    } else {
			    	resultWriter.setStatus(ResultWriter.FAIL);
			    	resultWriter.addDetail("The preference value returned for " 
                         + "preferredLanguage is null. Expected default: Java");
			  }
		} else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("The method request.getPreferences() "
                                 + "returned a null value.");
		}
		out.println(resultWriter.toString());
    }
}
