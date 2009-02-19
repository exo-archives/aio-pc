/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionPrivatescope;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class CheckAttributeNameInPrivateScopeTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "CheckAttributeNameInPrivateScopeTest";
    public static final String PATH = "/CheckAttributeNameInPrivateScopeTest_1_Servlet";

    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        PortletRequestDispatcher dispatcher
            = getPortletContext().getRequestDispatcher(PATH);

        if (dispatcher == null) {
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                                   + PATH);

            out.println(resultWriter.toString());
        } else {
            PortletSession session = request.getPortletSession();
            request.setAttribute("WindowID", request.getWindowID());
            session.setAttribute(TEST_NAME, TEST_NAME,
                                 PortletSession.PORTLET_SCOPE);

            dispatcher.include(request, response);
        }
    }
}
