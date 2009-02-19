/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the PortletException thrown by the
 * include(PortletRequest, RenderResponse) method.
 */
public class IncludePortletExceptionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "IncludePortletExceptionTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String servletName = "IncludePortletExceptionTestServlet";

        PortletRequestDispatcher dispatcher
            = getPortletContext().getNamedDispatcher(servletName);

        if (dispatcher == null) {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                                   + servletName);
        } else {
            try {
                dispatcher.include(request, response);
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("Included servlet doesn't throw "
                                       + "PortletException");
            } catch (PortletException e) {
                Throwable cause = e.getCause();
                String expectedResult = TEST_NAME;
                String result = (cause == null) ? null : cause.getMessage();

                if ((cause != null) && (cause instanceof ServletException)
                    && (result != null) && result.equals(expectedResult)) {

                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);

                    resultWriter.addDetail("The root cause isn't set to "
                                           + "the original exception.");
                }
            }
        }

        out.println(resultWriter.toString());
    }
}
