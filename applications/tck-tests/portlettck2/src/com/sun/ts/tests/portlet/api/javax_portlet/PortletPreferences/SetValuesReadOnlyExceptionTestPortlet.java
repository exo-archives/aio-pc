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
import javax.portlet.ReadOnlyException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *  This class expects ReadOnlyException() to be thrown when the method
 *  setValues() is invoked for a read-only preference entity.
 */

public class SetValuesReadOnlyExceptionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetValuesReadOnlyExceptionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		String newValues[] = new String[] {"Java", "Oracle", "Perl"};

		PortletPreferences preferences = request.getPreferences();

		if (preferences != null) {
			try {
				preferences.setValues("preferredLanguages", newValues);
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("Expected a ReadOnlyException() as "
                                     + "the setValues() was invoked for a "
                                     + "read-only preference entity");
			} catch (ReadOnlyException ume) {
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
