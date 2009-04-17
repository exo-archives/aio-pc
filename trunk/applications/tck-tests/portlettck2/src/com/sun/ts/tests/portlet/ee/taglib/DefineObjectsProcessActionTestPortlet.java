/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.ee.taglib;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This portlet tests the defineObjects tag with a JSP included within 
 * the processAction method.
 *  
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */

public class DefineObjectsProcessActionTestPortlet extends GenericPortlet {

	private final String TEST_NAME = "DefineObjectsProcessActionTest";
	private final String SERVLET_NAME = "DefineObjectsProcessActionTestServlet";
	
	private final String RESULT = "result";

	
	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		PortletRequestDispatcher dispatcher = 
			getPortletContext().getNamedDispatcher(SERVLET_NAME);
		
		PortletSession portletSession = request.getPortletSession();
		
		portletSession.setAttribute("one", "one");
		portletSession.setAttribute("two", "two");
		
		if (dispatcher == null) {
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			resultWriter.setStatus(ResultWriter.FAIL);

			resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
					+ SERVLET_NAME);

			portletSession.setAttribute(RESULT, resultWriter.toString(), 
					PortletSession.APPLICATION_SCOPE);
		} 
		else {
			dispatcher.include(request, response);
		}
	}
	
	
	@Override
	public void render(RenderRequest request, RenderResponse response) 
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		PortletSession portletSession = request.getPortletSession();
		
		String resultString = 
			(String) portletSession.getAttribute(RESULT, 
					PortletSession.APPLICATION_SCOPE); 
		
		if(resultString != null){
			out.println(resultString);
		}
		else{
			PortletURLTag urlTag = new PortletURLTag();
			PortletURL actionURL = response.createActionURL();
			urlTag.setTagContent(actionURL.toString());
			
			out.println(urlTag.toString());
		}
		
	}
 
}
