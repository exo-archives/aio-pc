/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This servlet is invoked by CompareContextAttrInPortletAndServletTestPortlet.
 *  This servlet reads the context attribute set in the portlet and compares
 *  it with the expected value.
 */

public class CompareContextAttrInPortletAndServletTest_1_Servlet extends HttpServlet {

    public static final String TEST_NAME = 
                                "CompareContextAttrInPortletAndServletTest";

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PrintWriter out = response.getWriter();
        String expectedPreferredLanguage = "Java";

        ServletContext context = getServletContext();

        if (context != null) {
            String preferredLanguage = 
                        (String)context.getAttribute("preferredLanguage");

            if (expectedPreferredLanguage.equals(preferredLanguage)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected preferred Language = " 
                                                + expectedPreferredLanguage);
                resultWriter.addDetail("Actual preferred Language = " 
                                                + preferredLanguage);
            }
        } else {
          resultWriter.setStatus(ResultWriter.FAIL);
          resultWriter.addDetail("getServletContext() returned null");
        }
        out.println(resultWriter.toString());
    }
}
