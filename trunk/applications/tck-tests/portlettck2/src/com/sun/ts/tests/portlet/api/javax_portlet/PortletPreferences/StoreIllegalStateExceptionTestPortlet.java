/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletPreferences;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletURL;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletException;
import javax.portlet.ValidatorException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 *  This class expects IllegalStateException() to be thrown when the  
 *  the store() method is invoked inside the render() method.
 */

public class StoreIllegalStateExceptionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="StoreIllegalStateExceptionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		PortletPreferences preferences = request.getPreferences();

        try {
            // Set the value to XML
            preferences.setValue("preferredLanguage", "XML");

            preferences.store();

			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected IllegalStateException()"
                                 + "as the store() method was invoked from the"
                                 + "render() method");
        } catch (IllegalStateException  ise) {
			resultWriter.setStatus(ResultWriter.PASS);
        }
        out.println(resultWriter.toString());
    }
}
