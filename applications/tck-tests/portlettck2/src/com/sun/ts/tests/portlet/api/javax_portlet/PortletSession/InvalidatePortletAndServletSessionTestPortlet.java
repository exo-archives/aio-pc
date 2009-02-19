/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.GenericPortlet;
import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletTCKCustomTag;


/*
 * Portlet that is going to invalidate a session. It also writes the 
 * context-path so that client can construct a second request to servlet.
 * This servlet would check for the invalidated session.
 */

public class InvalidatePortletAndServletSessionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "InvalidatePortletAndServletSessionTest";
    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        PortletSession session = request.getPortletSession();
        if (session != null) {
            try {
                //invalidate the session
                session.invalidate();
            } catch(IllegalStateException ise ) {
		       out.println("IllegalStateException() thrown while " +
                " invalidating the portlet session");
            }
        } 

		// Send the contextPath to be used by the servlet.
		String contextPath = request.getContextPath();
		PortletTCKCustomTag customTag = 
            new PortletTCKCustomTag(CommonConstants.PATH_TO_SERVLET_TAG);
        String pathToServlet = response.encodeURL(
            contextPath + "/InvalidatePortletAndServletSessionTest_1_Servlet");
        
		customTag.setTagContent(pathToServlet);
		out.println(customTag.toString());
    }
}
