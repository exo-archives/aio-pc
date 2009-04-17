/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

// This servlet would be called by the URLClient by constructing
// a request using PortletRequest.getPortletContext(), 
// Test passes is the servlet is called successfully.

public class GetContextPathTestServlet extends GenericServlet {
    public static final String TEST_NAME = GetContextPathTestPortlet.TEST_NAME;


    public void service(ServletRequest request, ServletResponse response)
        throws ServletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.PASS);
        response.getWriter().println(resultWriter.toString());
    }
}
