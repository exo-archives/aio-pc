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
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This portlet tests the defineObjects tag with a JSP included within 
 * the serveResource method.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */

public class DefineObjectsServeResourceTestPortlet extends GenericPortlet {

	private final String TEST_NAME = "DefineObjectsServeResourceTest";
	private final String SERVLET_NAME = "DefineObjectsServeResourceTestServlet";

	
	@Override
	public void render(RenderRequest request, RenderResponse response) 
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		PortletURLTag urlTag = new PortletURLTag();
		ResourceURL resourceURL = response.createResourceURL();
		urlTag.setTagContent(resourceURL.toString());
			
		out.println(urlTag.toString());
	}
	
	
	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
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
