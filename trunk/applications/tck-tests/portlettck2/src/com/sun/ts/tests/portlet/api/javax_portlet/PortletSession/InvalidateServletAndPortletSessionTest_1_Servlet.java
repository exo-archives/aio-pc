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

/*
 * Servlet that is going to invalidate the session.
 */
public class InvalidateServletAndPortletSessionTest_1_Servlet extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();
        if (session != null) {
            try {
                //invalidate the session
                session.invalidate();
            } catch(IllegalStateException ise ) {
              out.println("IllegalStatException() thrown while invalidating the  servlet session");
            }
        }
    }
}
