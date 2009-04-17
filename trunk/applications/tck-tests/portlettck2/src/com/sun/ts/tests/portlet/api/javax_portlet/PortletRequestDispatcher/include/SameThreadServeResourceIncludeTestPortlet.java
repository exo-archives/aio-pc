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

import com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.SameThreadTestThreadLocalizer;



/**
 * This class tests that the portlet container ensures that the
 * servlet called through a PortletRequestDispatcher is called in the
 * same thread as the PortletRequestDispatcher include invocation.
 *
 *
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class SameThreadServeResourceIncludeTestPortlet 
	extends AbstractServeResourceIncludeTestPortlet {
    
	private final String TEST_NAME = "SameThreadServeResourceIncludeTest";
	
	private final String SERVLET_PATH = "/SameThreadTestServlet";
    
    
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
		
		SameThreadTestThreadLocalizer.set(TEST_NAME);
	}

	

}
