/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include.IncludeServletTestPortlet;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the include(PortletRequest, RenderResponse)
 * method to include a servlet's output.
 */
public class IncludeServletTestServlet extends GenericServlet {
	
	static final long serialVersionUID=168L;
	
    public static final String TEST_NAME = IncludeServletTestPortlet.TEST_NAME;

    public void service(ServletRequest request, ServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String expectedResult = TEST_NAME;
        String result = (String)request.getAttribute(TEST_NAME);

        if ((result != null) && result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }

        out.println(resultWriter.toString());
    }
}
