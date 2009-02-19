/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletRequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class uses sets a context attribute and then calls a servlet through
 *  the PortletRequestDispatcher's include() method
 */

public class CompareContextAttrInPortletAndServletTestPortlet extends GenericPortlet {

	static public String TEST_NAME="CompareContextAttrInPortletAndServletTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        PortletContext context = getPortletContext();

        if (context != null) {
            context.setAttribute("preferredLanguage", "Java");

            String servletName = 
                        "CompareContextAttrInPortletAndServletTest_1_Servlet";
            PortletRequestDispatcher dispatcher = 
                                    context.getNamedDispatcher(servletName);
            if (dispatcher != null) {
                 dispatcher.include(request, response);
            } else {
			    resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getNamedDispatcher() returned a null "
                                     + "dispatcher object");
            }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
    }
}
