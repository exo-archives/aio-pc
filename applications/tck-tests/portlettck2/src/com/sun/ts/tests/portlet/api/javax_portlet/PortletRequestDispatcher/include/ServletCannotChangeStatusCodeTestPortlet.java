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
 * This class tests that the included servlet cannot change the
 * response status code; any attempt to make a change is ignored.
 */
public class ServletCannotChangeStatusCodeTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "ServletCannotChangeStatusCodeTest";

    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        String path = "/ServletCannotChangeStatusCodeTestServlet";

        PortletRequestDispatcher dispatcher
            = getPortletContext().getRequestDispatcher(path);

        if (dispatcher == null) {
            PrintWriter out = response.getWriter();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                                   + path);

            out.println(resultWriter.toString());
        } else {
            dispatcher.include(request, response);
        }
    }
}
