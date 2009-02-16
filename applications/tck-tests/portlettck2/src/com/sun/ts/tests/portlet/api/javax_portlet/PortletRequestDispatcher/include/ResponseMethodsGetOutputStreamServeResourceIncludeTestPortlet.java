/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;


/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsGetOutputStreamServeResourceIncludeTestPortlet 
	extends AbstractServeResourceIncludeTestPortlet {
    
	private final String TEST_NAME = 
		"ResponseMethodsGetOutputStreamServeResourceIncludeTest";
	
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
			ResourceRequest request, ResourceResponse response) {
		
		super.prepareInclude(session, request, response);
		
		session.setAttribute("getOutputStream",
				new Boolean(true),
				PortletSession.APPLICATION_SCOPE);
	}

	

}
