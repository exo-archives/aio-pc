/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that data stored in the ServletContext by servlets
 * are accessible to portlets through the PortletContext.
 */

public class CompareContextAttrInServletAndPortletTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "CompareContextAttrInServletAndPortletTest";
    public static final String PATH = "/CompareContextAttrInServletAndPortletTest_1_Servlet";

    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletRequestDispatcher dispatcher
            = getPortletContext().getRequestDispatcher(PATH);

        if (dispatcher == null) {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                                   + PATH);
        } else {
            dispatcher.include(request, response);
            PortletContext context = getPortletContext();
            String expectedResult = TEST_NAME;
            String result = (String)context.getAttribute(TEST_NAME);

            if ((result != null) && result.equals(expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected result = " + expectedResult);
                resultWriter.addDetail("Actual result = " + result);
            }
        }

        out.println(resultWriter.toString());
    }
}
