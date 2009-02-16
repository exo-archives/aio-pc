/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletSession;


/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsGetOutputStreamEventingForwardTestPortlet 
	extends AbstractEventingForwardTestPortlet {
	
    private final String TEST_NAME = 
    	"ResponseMethodsGetOutputStreamEventingForwardTest";
    
	private final String SERVLET_PATH = 
		"/ResponseMethodsGetOutputStreamTestServlet";
    
    
    @Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}


	@Override
	protected String getTestName() {
		return TEST_NAME;
	}



	@Override
	protected void prepareForward(PortletSession session, EventRequest request,
			EventResponse response) {

		super.prepareForward(session, request, response);
		
		session.setAttribute("getOutputStream",
				new Boolean(true),
				PortletSession.APPLICATION_SCOPE);
	}

	
	

}
