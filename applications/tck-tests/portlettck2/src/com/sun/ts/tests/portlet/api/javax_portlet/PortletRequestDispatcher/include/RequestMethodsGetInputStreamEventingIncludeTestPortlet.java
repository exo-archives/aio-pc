/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletSession;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsGetInputStreamEventingIncludeTestPortlet 
	extends AbstractEventingIncludeTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsGetInputStreamEventingIncludeTest";
	
	private final String SERVLET_PATH = 
		"/RequestMethodsGetInputStreamTestServlet";
    
    
	@Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}


	@Override
	protected String getTestName() {
		return TEST_NAME;
	}


	@Override
	protected void prepareInclude(PortletSession session, 
			EventRequest request, EventResponse response) {
		
		super.prepareInclude(session, request, response);

		session.setAttribute("getInputStream",
				new Boolean(false),
				PortletSession.APPLICATION_SCOPE);


	}	

}
