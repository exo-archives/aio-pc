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
 *  This class expects a IllegalArgumentException() to be thrown when getvalue()
 *  is invoked with a null key as argument.
 */

public class GetValueIllegalArgumentExceptionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetValueIllegalArgumentExceptionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		String defaultValue = "Java";
		String actualValue = null;

		PortletPreferences preferences = request.getPreferences();

		if (preferences != null) {
			try {
				actualValue = preferences.getValue(null,defaultValue);
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("Expected a "
                                     + "IllegalArgumentException() to be "
                                     + "thrown as the key was null");
				resultWriter.addDetail("Actual value = " + actualValue);
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
