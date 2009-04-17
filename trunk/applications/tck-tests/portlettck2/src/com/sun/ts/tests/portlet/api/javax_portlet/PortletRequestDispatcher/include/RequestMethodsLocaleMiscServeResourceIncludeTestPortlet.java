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
public class RequestMethodsLocaleMiscServeResourceIncludeTestPortlet 
	extends AbstractServeResourceIncludeTestPortlet {

    private final String TEST_NAME = 
    	"RequestMethodsLocaleMiscServeResourceIncludeTest";
    
    private final String SERVLET_PATH = 
    	"/RequestMethodsLocaleMiscTestServlet";
    
    
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
		session.setAttribute("getLocale",
                request.getLocale(),
                PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getLocales",
                request.getLocales(),
                PortletSession.APPLICATION_SCOPE);
	}
}
