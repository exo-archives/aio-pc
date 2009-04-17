/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include.ServletCannotChangeStatusCodeTestPortlet;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the included servlet cannot change the
 * response status code; any attempt to make a change is ignored.
 */
public class ServletCannotChangeStatusCodeTestServlet extends HttpServlet {
	
	static final long serialVersionUID=286L;
	
    public static final String TEST_NAME = ServletCannotChangeStatusCodeTestPortlet.TEST_NAME;

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // try to set status code to 202 instead of the usual 200
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.PASS);
        out.println(resultWriter.toString());
    }
}
