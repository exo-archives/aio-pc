/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsGetInputStreamActionForwardTestPortlet 
	extends AbstractActionForwardTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsGetInputStreamActionForwardTest";
	
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
	protected void prepareForward(PortletSession session, 
			ActionRequest request, ActionResponse response) {
		
		super.prepareForward(session, request, response);

		session.setAttribute("getInputStream",
				new Boolean(true),
				PortletSession.APPLICATION_SCOPE);
	}	

}
