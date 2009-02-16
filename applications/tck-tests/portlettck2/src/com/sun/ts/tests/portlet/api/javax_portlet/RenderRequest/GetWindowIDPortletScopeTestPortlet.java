/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the attribute an checks that the getWindowID return
 * value is the same as that one used by the container for scoping
 * session attributes.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetWindowIDPortletScopeTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"GetWindowIDPortletScopeTest";
	
	public static final String SERVLET_PATH = "/GetWindowIDPortletScopeTest_1_Servlet";
	
	
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PortletRequestDispatcher dispatcher
        	= getPortletContext().getRequestDispatcher(SERVLET_PATH);
		
		if (dispatcher == null) {
			
			PrintWriter out = response.getWriter();
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                    + SERVLET_PATH);
			
			out.println(resultWriter.toString());
			
		} else {
			
			request.getPortletSession().setAttribute(TEST_NAME,
											request.getWindowID(),
											PortletSession.PORTLET_SCOPE);
			
			dispatcher.include(request, response);
			
		}	
	}
}
