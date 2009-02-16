/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsReturnNullRenderForwardTestPortlet 
	extends AbstractRenderForwardTestPortlet {
    
	private final String TEST_NAME = 
		"ResponseMethodsReturnNullRenderForwardTest";
	
	private final String SERVLET_PATH = 
		"/ResponseMethodsReturnNullTestServlet";
    
    
    @Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}


	@Override
	protected String getTestName() {
		return TEST_NAME;
	}


}
