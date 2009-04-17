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
public class ResponseMethodsMiscMiscEventingIncludeTestPortlet 
	extends AbstractEventingIncludeTestPortlet {
	
    private final String TEST_NAME = 
    	"ResponseMethodsMiscMiscEventingIncludeTest";
    
	private final String SERVLET_PATH = 
		"/ResponseMethodsMiscMiscTestServlet";
    
    
    @Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}


	@Override
	protected String getTestName() {
		return TEST_NAME;
	}



	@Override
	protected void prepareInclude(PortletSession session, EventRequest request,
			EventResponse response) {

		super.prepareInclude(session, request, response);
		
		session.setAttribute("getBufferSize",
				new Integer(0),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("isCommitted",
				new Boolean(true),
				PortletSession.APPLICATION_SCOPE);
		
		session.setAttribute("encodeURL",
				response.encodeURL(getServletPath()),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("encodeUrl",
				response.encodeURL(getServletPath()),
				PortletSession.APPLICATION_SCOPE);
		
		session.setAttribute("encodeUrlPath",
				getServletPath(),
				PortletSession.APPLICATION_SCOPE);
	}

	
	

}
