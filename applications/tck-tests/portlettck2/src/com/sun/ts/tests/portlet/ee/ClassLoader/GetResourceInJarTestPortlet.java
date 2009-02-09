/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.ee.ClassLoader;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletRequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;


import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetResourceInJarTestPortlet extends GenericPortlet {

	static public String TEST_NAME="GetResourceInJarTest";

    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        response.setContentType("text/html");
        PortletContext context = getPortletContext();

        String servletName = "GetResourceInJarTest_1_Servlet";

        PortletRequestDispatcher dispatcher = context.getNamedDispatcher(servletName);

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
