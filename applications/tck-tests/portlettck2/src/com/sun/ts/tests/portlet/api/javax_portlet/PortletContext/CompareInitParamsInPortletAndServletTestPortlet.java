/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2006 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletRequestDispatcher;
import java.util.Enumeration;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This portlet reads initialization parameter names from the descriptor 
 *  file and puts into the portlet session. It then invokes a servlet   
 *  through the PortletRequestDispatcher's include() method.
 *  
 *  @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *  @since v2.0
 */ 

public class CompareInitParamsInPortletAndServletTestPortlet extends GenericPortlet {

	static public String TEST_NAME = 
                                "CompareInitParamsInPortletAndServletTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        PortletContext context = getPortletContext();

        Enumeration enumeration = context.getInitParameterNames();
        
        PortletSession session = request.getPortletSession();
        session.setAttribute("initParameters", enumeration, PortletSession.APPLICATION_SCOPE);
        String servletName = 
                        "CompareInitParamsInPortletAndServletTest_1_Servlet";

        PortletRequestDispatcher dispatcher = 
                                 context.getNamedDispatcher(servletName);
        if (dispatcher != null) {
              dispatcher.include(request, response);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("The method getNamedDispatcher() returned "
                                 + "a null dispatcher object");
        }
        out.println(resultWriter.toString());
    }
}
