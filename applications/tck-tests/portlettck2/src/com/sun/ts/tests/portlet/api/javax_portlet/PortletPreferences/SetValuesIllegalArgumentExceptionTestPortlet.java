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
 *  This class expects IllegalArgumentException() to be thrown when the method
 *  setValues() is invoked with a null preference key.
 */

public class SetValuesIllegalArgumentExceptionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetValuesIllegalArgumentExceptionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		String newValues[] = new String[] {"Java", "Oracle", "Perl"};

		PortletPreferences preferences = request.getPreferences();

		if (preferences != null) {
			try {
				// Set an array of values to an empty preference key
				preferences.setValues(null, newValues);

				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("Expected a IllegalArgumentException() " 
                                    + "as the setValues() was invoked for a "
                                    + "null preference key");
			} catch (IllegalArgumentException iae) {
				resultWriter.setStatus(ResultWriter.PASS);
			}
		} else {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("The method request.getPreferences() "
                                 + "returned a null value");
		}
		out.println(resultWriter.toString());
    }
}
