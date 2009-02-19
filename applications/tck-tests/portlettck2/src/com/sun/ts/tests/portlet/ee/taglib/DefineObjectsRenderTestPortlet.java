/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.ee.taglib;

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
 * This portlet tests the defineObjects tag with a JSP included within 
 * the render method.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */

public class DefineObjectsRenderTestPortlet extends GenericPortlet {

	private final String TEST_NAME = "DefineObjectsRenderTest";
	private final String SERVLET_NAME = "DefineObjectsRenderTestServlet";


	@Override
	public void render(RenderRequest request, RenderResponse response) 
			throws PortletException, IOException {
		
		response.setContentType("text/html");  
		
		PortletRequestDispatcher dispatcher = 
			getPortletContext().getNamedDispatcher(SERVLET_NAME);
		
		PortletSession portletSession = request.getPortletSession();
		portletSession.setAttribute("one", "one");
		portletSession.setAttribute("two", "two");
		
		if (dispatcher == null) {
			PrintWriter out = response.getWriter();
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
					+ SERVLET_NAME);
			out.println(resultWriter.toString());
		} 
		else {
			dispatcher.include(request, response);
		}
	}

	

	
    
}
