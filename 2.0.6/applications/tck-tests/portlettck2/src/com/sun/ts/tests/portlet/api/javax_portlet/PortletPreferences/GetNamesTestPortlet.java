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
import java.util.Enumeration;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.ListCompare;

/**
 *  This class will test the getNames() method.
 */

public class GetNamesTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetNamesTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		String[] expectedPreferenceKeys = 
         new String[] {"preferredLanguage", "preferredOS", "preferredSystem"};

		PortletPreferences preferences = request.getPreferences();

		if (preferences != null) {
            Enumeration actualKeys = preferences.getNames();
            ListCompare listCompare = new ListCompare(expectedPreferenceKeys,
                                              actualKeys,
                                              null,
                                              ListCompare.ALL_ELEMENTS_MATCH);
            if (!listCompare.misMatch()) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("PortletPreference.getNames() did not"
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
