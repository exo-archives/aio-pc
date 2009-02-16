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
 * This class tests that PortletContext.getNamedDispatcher() returns
 * <code>null</code> for a non-existing servlet.
 */
public class IncludeNonexistingServletTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "IncludeNonexistingServletTest";

    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String servletName = "NoSuchServlet";

        PortletRequestDispatcher dispatcher
            = getPortletContext().getNamedDispatcher(servletName);

        if (dispatcher == null) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("PortletContext.getNamedDispatcher() "
                                   + "doesn't return null for a "
                                   + "non-existing servlet.");
        }

        out.println(resultWriter.toString());
    }
}
