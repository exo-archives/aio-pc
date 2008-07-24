/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/*
 * A servlet that checks for an attribute in the session that was inserted by
 * portlet from the same application.
 */

public class ComparePortletAndHttpSessionTest_1_Servlet extends HttpServlet {


    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(
                ComparePortletAndHttpSessionTestPortlet.TEST_NAME);
		HttpSession session = request.getSession(true);

		String attrValue = (String)session.getAttribute("testName");
        String expectedValue = ComparePortletAndHttpSessionTestPortlet.TEST_NAME;
		if (attrValue != null) {
            if (attrValue.equals(expectedValue)) {
               resultWriter.setStatus(ResultWriter.PASS);
            }
            else {
               resultWriter.setStatus(ResultWriter.FAIL);
               resultWriter.addDetail(
                    "A portlet appscope session attribute \"testName\" " + 
                    " when found in the " +
                    " servlet session expected to have value :" +  expectedValue);
                resultWriter.addDetail("actual value:" + attrValue);
            }
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail("A appscope portlet session attribute "
            + " \"testName\" not found in the servlet session." );
        }
        out.println(resultWriter.toString());
    }
}
