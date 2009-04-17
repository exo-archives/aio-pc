/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionPrivatescope;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class CheckAttributeNameInPrivateScopeTest_1_Servlet extends HttpServlet {
    public static final String TEST_NAME = CheckAttributeNameInPrivateScopeTestPortlet.TEST_NAME;

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        if (session != null) {
            Enumeration attrNames = session.getAttributeNames();
            boolean found = false;
            String sessionAttribute = TEST_NAME;

            if (attrNames.hasMoreElements()) {
                while (attrNames.hasMoreElements() && !found) {
                    String attrName = (String)attrNames.nextElement();

                    // Need to match the starting string and attribute name.
                    // Tokenize the string to extract the attribute name.

                    StringTokenizer st = new StringTokenizer(attrName,"?");

                    if (st.countTokens() < 2) {
                        continue;
                    }

                    if ((st.nextToken().startsWith("javax.portlet.p." + request.getAttribute("WindowID"))) &&
                                (st.nextToken().equals(sessionAttribute))) { 
                        found = true;
                        break;
                    }
                }

                if (found) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("Unable to find the attribute name"
                       + " set in the portlet session in the servlet session");
                }
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(
                    "Obtained an empty attribute names enumeration from"
                    + " session.getAttributeNames." );
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("The method Request.getSession() returned null");
        }

        out.println(resultWriter.toString());
    }
}
