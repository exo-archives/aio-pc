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
 * This class tests the include(PortletRequest, RenderResponse)
 * method to include a JSP page's content.
 */
public class IncludeJSPTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "IncludeJSPTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        request.setAttribute(TEST_NAME, TEST_NAME);
        String servletName = "IncludedJSP";

        PortletRequestDispatcher dispatcher
            = getPortletContext().getNamedDispatcher(servletName);

        if (dispatcher == null) {
            PrintWriter out = response.getWriter();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                                   + servletName);

            out.println(resultWriter.toString());
        } else {
            dispatcher.include(request, response);
        }
    }
}
