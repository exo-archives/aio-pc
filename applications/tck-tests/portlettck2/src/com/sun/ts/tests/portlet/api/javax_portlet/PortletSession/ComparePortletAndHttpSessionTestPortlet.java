/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletTCKCustomTag;


// Serves the first request for the test and puts a attribute in the 
// session that will be read by a servlet in the next request.

public class ComparePortletAndHttpSessionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="ComparePortletAndHttpSessionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		PortletSession session = request.getPortletSession();

		// Put an attribute in the PortletSession to be read in the HttpSession
		session.setAttribute("testName",TEST_NAME, 
                        PortletSession.APPLICATION_SCOPE);

		// Send the contextPath to be used by the servlet.
		String contextPath = request.getContextPath();
		PortletTCKCustomTag customTag = 
            new PortletTCKCustomTag(CommonConstants.PATH_TO_SERVLET_TAG);
        String pathToServlet = response.encodeURL(contextPath + 
                "/ComparePortletAndHttpSessionTest_1_Servlet");

		customTag.setTagContent(pathToServlet);
		out.println(customTag.toString());
    }
}
