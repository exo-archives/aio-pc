/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.GenericPortlet;
import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;


/*
 * A portlet that first includes a servlet that would invalidate the session.
 * After the include, this portlet would check if the session is really 
 * invalidated.
 */

public class InvalidateServletAndPortletSessionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "InvalidateServletAndPortletSessionTest";
    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String servletName = 
                "InvalidateServletAndPortletSessionTest_1_Servlet";
        PortletContext context = getPortletContext();
        PortletRequestDispatcher dispatcher = 
                context.getNamedDispatcher(servletName);
        if (dispatcher != null) {
            try {
                // Just invoke the servlet which will invalidate the servlet
                // session
                 dispatcher.include(request, response);
            } catch (PortletException e) { 
                out.println("PortletException() thrown while invoking " + 
                    " the include() method");
            } catch (IOException e) { 
                out.println("IOException() thrown while invoking " +
                    " the include() method");
            }
        }
        PortletSession session = request.getPortletSession(false);
        if (session == null) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Portlet session object is still alive " + 
                " even after invalidating servlet session");
        }
       out.println(resultWriter.toString());
    }
}
