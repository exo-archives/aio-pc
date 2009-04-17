/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;


/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsGetOutputStreamActionIncludeTestPortlet 
	extends AbstractActionIncludeTestPortlet {
	
	private final String TEST_NAME = 
		"ResponseMethodsGetOutputStreamActionIncludeTest";
	
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
	protected void prepareInclude(PortletSession session,
			ActionRequest request, ActionResponse response) {
		
		super.prepareInclude(session, request, response);
		
		session.setAttribute("getOutputStream",
				new Boolean(true),
				PortletSession.APPLICATION_SCOPE);
	}

	

}
