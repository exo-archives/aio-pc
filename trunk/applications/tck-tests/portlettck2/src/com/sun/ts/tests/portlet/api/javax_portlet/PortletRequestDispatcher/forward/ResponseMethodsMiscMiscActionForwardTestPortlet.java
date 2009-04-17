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
public class ResponseMethodsMiscMiscActionForwardTestPortlet 
	extends AbstractActionForwardTestPortlet {
	
	private final String TEST_NAME = 
		"ResponseMethodsMiscMiscActionForwardTest";
	
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
	protected void prepareForward(PortletSession session,
			ActionRequest request, ActionResponse response) {
		
		super.prepareForward(session, request, response);

		session.setAttribute("getBufferSize",
				new Integer(0),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("isCommitted",
				new Boolean(false),
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
