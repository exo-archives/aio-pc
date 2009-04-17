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
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.namespace.QName;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This portlet tests the defineObjects tag with a JSP included within 
 * the processEvent method.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */

public class DefineObjectsProcessEventTestPortlet extends GenericPortlet {

	private final String TEST_NAME = "DefineObjectsProcessEventTest";
	private final String SERVLET_NAME = "DefineObjectsProcessEventTestServlet";
	
	private final String RESULT = "result";
	
	//event
	private QName testEvent = new QName("http://acme.com/events", TEST_NAME);

	
	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		response.setEvent(testEvent, TEST_NAME);
	}


	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		
		Event evt = request.getEvent();

		if(evt.getValue().equals(TEST_NAME)){
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
