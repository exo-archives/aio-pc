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

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *  This class will test the setValues() method. 
 */

public class SetValuesTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetValuesTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		String expectedValues[] = new String[] {"Java", "Oracle", "Perl"};

		PortletPreferences preferences = request.getPreferences();

		if (preferences != null) {
			// Set an array of values
			preferences.setValues("preferredLanguages", expectedValues);

			// Try reading back all the new values
			String[] actualValues = 
                            preferences.getValues("preferredLanguages", null);

            ListCompare listCompare = new ListCompare(expectedValues,
                                               actualValues,
                                               null,
                                               ListCompare.ALL_ELEMENTS_MATCH);

			if (!listCompare.misMatch()) {
				resultWriter.setStatus(ResultWriter.PASS);
			} else {
				resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("PortletPreference.getValues() did not"
                                     + "return expected results");
				resultWriter.addDetail(listCompare.getMisMatchReason());
			}
		} else {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("The method request.getPreferences() "
                                 + "returned a null value");
		}
		out.println(resultWriter.toString());
    }
}
