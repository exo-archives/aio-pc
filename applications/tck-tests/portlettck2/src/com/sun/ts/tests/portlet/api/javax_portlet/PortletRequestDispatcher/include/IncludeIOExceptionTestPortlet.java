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

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the IOException thrown by the
 * include(PortletRequest, RenderResponse) method.
 */
public class IncludeIOExceptionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "IncludeIOExceptionTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String servletName = "IncludeIOExceptionTestServlet";

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
                                       + "IOException.");
            } catch (IOException e) {
                resultWriter.setStatus(ResultWriter.PASS);
            }
        }

        out.println(resultWriter.toString());
    }
}
