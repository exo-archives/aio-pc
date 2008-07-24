/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class InvalidatePortletAndServletSessionTest_1_Servlet extends HttpServlet {

    public static final String TEST_NAME = "InvalidatePortletAndServletSessionTest";

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(false);
		
        if (session == null) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected the servlet session object to " + 
                " be null as the portlet session object was invalidated"); 
        }
        out.println(resultWriter.toString());
    }
}
