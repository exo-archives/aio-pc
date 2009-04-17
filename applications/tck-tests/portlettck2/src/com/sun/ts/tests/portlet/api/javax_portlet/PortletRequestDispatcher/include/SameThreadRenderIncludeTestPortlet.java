/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.SameThreadTestThreadLocalizer;



/**
 * This class tests that the portlet container ensures that the
 * servlet called through a PortletRequestDispatcher is called in the
 * same thread as the PortletRequestDispatcher include invocation.
 *
 *
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class SameThreadRenderIncludeTestPortlet 
	extends AbstractRenderIncludeTestPortlet {
    
	private final String TEST_NAME = "SameThreadRenderIncludeTest";
	
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
			RenderRequest request, RenderResponse response) {
		
		super.prepareInclude(session, request, response);
		
		SameThreadTestThreadLocalizer.set(TEST_NAME);
	}

	

}
