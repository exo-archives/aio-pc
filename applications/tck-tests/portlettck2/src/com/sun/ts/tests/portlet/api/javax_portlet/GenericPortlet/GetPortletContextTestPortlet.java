/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.GenericPortlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the getPortletContext() method.
 */
public class GetPortletContextTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetPortletContextTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletContext portletContext = getPortletContext();

        if (portletContext != null) {
            if (portletContext.equals(getPortletConfig().getPortletContext())) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("getPortletContext() returns "
                                       + "a different value from "
                                       + "getPortletConfig().getPortletContext()"
                                       + ".");
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null.");
        }

        out.println(resultWriter.toString());
    }
}
