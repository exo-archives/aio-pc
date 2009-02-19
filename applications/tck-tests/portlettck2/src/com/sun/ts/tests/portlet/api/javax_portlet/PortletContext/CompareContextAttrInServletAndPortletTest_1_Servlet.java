/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class tests that data stored in the ServletContext by servlets
 * are accessible to portlets through the PortletContext.
 */

public class CompareContextAttrInServletAndPortletTest_1_Servlet extends HttpServlet {
    public static final String TEST_NAME = CompareContextAttrInServletAndPortletTestPortlet.TEST_NAME;

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        getServletContext().setAttribute(TEST_NAME, TEST_NAME);
    }
}
