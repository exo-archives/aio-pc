/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class participates in testing that destroy() is called if a
 * permanent unavailability is indicated by the UnavailableException.
 * It looks for a portlet context attribute in render() that was set
 * in the destroy() method of another portlet that threw
 * UnavailableException in render().
 */
public class PermanentlyUnavailableTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "PermanentlyUnavailableTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (!requestCount.isFirstRequest()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            String expectedResult = TEST_NAME;
            PortletContext portletContext = getPortletContext();
            String result = (String)portletContext.getAttribute(TEST_NAME);

            if (result != null) {
                portletContext.removeAttribute(TEST_NAME); // cleanup
            }

            if ((result != null) && result.equals(expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected result = " + expectedResult);
                resultWriter.addDetail("Actual result = " + result);
            }

            out.println(resultWriter.toString());
        }
    }
}
