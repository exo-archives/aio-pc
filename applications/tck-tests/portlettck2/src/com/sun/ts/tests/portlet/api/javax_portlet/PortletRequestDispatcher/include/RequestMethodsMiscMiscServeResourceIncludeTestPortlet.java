/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

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
public class RequestMethodsMiscMiscServeResourceIncludeTestPortlet 
	extends AbstractServeResourceIncludeTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsMiscMiscServeResourceIncludeTest";
	
	private final String SERVLET_PATH = 
		"/RequestMethodsMiscMiscTestServlet";
    
    
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

		session.setAttribute("getScheme",
				request.getScheme(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getServerName",
				request.getServerName(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getServerPort",
				new Integer(request.getServerPort()),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("isSecure",
				new Boolean(request.isSecure()),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getAuthType",
				request.getAuthType(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getContextPath",
				request.getContextPath(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getRemoteUser",
				request.getRemoteUser(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getUserPrincipal",
				request.getUserPrincipal(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getRequestedSessionId",
				request.getRequestedSessionId(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("isRequestedSessionIdValid",
				new Boolean(request.isRequestedSessionIdValid()),
				PortletSession.APPLICATION_SCOPE);
		
		session.setAttribute("getCookies",
				request.getCookies(),
				PortletSession.APPLICATION_SCOPE);
	}

}
