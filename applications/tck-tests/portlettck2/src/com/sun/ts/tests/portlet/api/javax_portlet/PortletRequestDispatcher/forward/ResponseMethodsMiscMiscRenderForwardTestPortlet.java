/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsMiscMiscRenderForwardTestPortlet 
	extends AbstractRenderForwardTestPortlet {
    
	private final String TEST_NAME = 
		"ResponseMethodsMiscMiscRenderForwardTest";
	
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
			RenderRequest request, RenderResponse response) throws IOException {

		super.prepareForward(session, request, response);
		
		session.setAttribute("encodeURL",
				response.encodeURL(getServletPath()),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("encodeUrl",
				response.encodeURL(getServletPath()),
				PortletSession.APPLICATION_SCOPE);
		
		session.setAttribute("encodeUrlPath",
				getServletPath(),
				PortletSession.APPLICATION_SCOPE);
		
		session.setAttribute("getBufferSize",
				new Integer(response.getBufferSize()),
				PortletSession.APPLICATION_SCOPE);
		
		session.setAttribute("getCharacterEncoding",
				response.getCharacterEncoding(),
				PortletSession.APPLICATION_SCOPE);
		
		session.setAttribute("getContentType",
				response.getContentType(),
				PortletSession.APPLICATION_SCOPE);	
		
		session.setAttribute("getLocale",
				response.getLocale(),
				PortletSession.APPLICATION_SCOPE);		

		session.setAttribute("isCommitted",
				new Boolean(response.isCommitted()),
				PortletSession.APPLICATION_SCOPE);
	}

	
	

}
