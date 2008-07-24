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
public class IncludedRequestAttributesEventingTestPortlet 
	extends AbstractEventingIncludeTestPortlet {
    
	private final String TEST_NAME = 
		"IncludedRequestAttributesEventingTest";
	
	private final String SERVLET_PATH = 
		"/IncludedRequestAttributesTestServlet";
    

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
		
		request.setAttribute("config", getPortletConfig());
        request.setAttribute("request", request);
        request.setAttribute("response", response);
	}
	

}
