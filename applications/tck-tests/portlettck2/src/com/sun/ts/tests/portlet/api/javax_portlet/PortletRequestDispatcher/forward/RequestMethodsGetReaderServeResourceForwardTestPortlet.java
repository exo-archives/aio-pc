/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.PortletSession;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsGetReaderServeResourceForwardTestPortlet 
	extends AbstractServeResourceForwardTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsGetReaderServeResourceForwardTest";
	
	private final String SERVLET_PATH = 
		"/RequestMethodsGetReaderTestServlet";
    
    
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
			ResourceRequest request, ResourceResponse response) throws IOException{
		
		super.prepareForward(session, request, response);

		session.setAttribute("getReader",
					new Boolean(true),
					PortletSession.APPLICATION_SCOPE);

	}	

}
