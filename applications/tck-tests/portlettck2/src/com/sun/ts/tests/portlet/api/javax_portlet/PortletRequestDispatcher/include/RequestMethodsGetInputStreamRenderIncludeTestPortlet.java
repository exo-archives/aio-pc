/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletSession;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsGetInputStreamRenderIncludeTestPortlet 
	extends AbstractRenderIncludeTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsGetInputStreamRenderIncludeTest";
	
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
			RenderRequest request, RenderResponse response) {
		
		super.prepareInclude(session, request, response);

		session.setAttribute("getInputStream",
				new Boolean(false),
				PortletSession.APPLICATION_SCOPE);


	}	

}
