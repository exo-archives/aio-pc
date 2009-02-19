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
 *  This class will set a new preference attribute and checks for the value
 *  of the method isReadOnly(). 
 */

public class SetValueThruProgramTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetValueThruProgramTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		PortletPreferences preferences = request.getPreferences();

		if (preferences != null) {
			// Set a new preference attribute
			preferences.setValue("preferredEditor", "VI");

            if (!preferences.isReadOnly("preferredEditor")) {
				resultWriter.setStatus(ResultWriter.PASS);
			} else {
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("Expected value of isReadOnly()=false");
				resultWriter.addDetail("Actual value of isReadOnly()=true");
			}
		} else {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("The method request.getPreferences() "
                                 + "returned a null value");
		}
		out.println(resultWriter.toString());
    }
}
