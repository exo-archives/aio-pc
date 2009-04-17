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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.ListCompare;

/**
 *	This servlet is invoked by CompareInitParamsInPortletAndServletTestPortlet.
 *  This servlet reads the initialization parameters set by the portlet from
 *  the HttpSession and compares it with the expected values.
 */ 
public class CompareInitParamsInPortletAndServletTest_1_Servlet extends HttpServlet {

    public static final String TEST_NAME = 
                                "CompareInitParamsInPortletAndServletTest";

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PrintWriter out = response.getWriter();
        Enumeration portletContextInitParams = null;
        Enumeration servletContextInitParams = null;
        int count=0;

        ServletContext context = getServletContext();

        servletContextInitParams = context.getInitParameterNames();

		HttpSession session = request.getSession();

        if (session != null) {
            portletContextInitParams = 
                            (Enumeration)session.getAttribute("initParameters");

            ListCompare compare = new ListCompare(
                                      servletContextInitParams,
                                      portletContextInitParams,
                                      null,
                                      ListCompare.ALL_ELEMENTS_MATCH);
            if (compare.misMatch()) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Init params obtained thru Servlet " 
                                        + "Context and Portlet Context are "
                                        + "different. Expected them to be the "
                                        + "same");
                resultWriter.addDetail("Init Params obtained through "
                                                        + "ServletContext: ");
                while (servletContextInitParams.hasMoreElements()) {
                  resultWriter.addDetail((String)servletContextInitParams.nextElement());
               }
                resultWriter.addDetail("Init Params obtained through "
                                                       + "Portlet Context: ");
                while (portletContextInitParams.hasMoreElements()) {
                   resultWriter.addDetail((String)portletContextInitParams.nextElement());
                }
            } else {
                   resultWriter.setStatus(ResultWriter.PASS);
              }
        } else {
          resultWriter.setStatus(ResultWriter.FAIL);
          resultWriter.addDetail("Obtained an empty servlet context init "
                                   + "params enumeration");
        }
      out.println(resultWriter.toString());
    }
}
