/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

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
public class RequestMethodsSecondMiscMiscEventingIncludeTestPortlet 
	extends AbstractEventingIncludeTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsSecondMiscMiscEventingIncludeTest";
	
	private final String SERVLET_PATH = 
		"/RequestMethodsSecondMiscMiscTestServlet";
    
    
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
		
		session.setAttribute("getContentLength",
				new Integer(0),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getMethod",
				request.getMethod(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getProtocol",
				"HTTP/1.1",
				PortletSession.APPLICATION_SCOPE);
		
		session.setAttribute("setCharacterEncoding",
				new Boolean(false),//no-op
				PortletSession.APPLICATION_SCOPE);

	}	

}
