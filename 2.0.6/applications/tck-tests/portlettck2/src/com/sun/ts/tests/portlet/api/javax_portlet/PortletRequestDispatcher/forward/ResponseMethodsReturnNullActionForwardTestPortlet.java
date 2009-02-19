/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;



/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsReturnNullActionForwardTestPortlet 
	extends AbstractActionForwardTestPortlet {
	
	private final String TEST_NAME = 
		"ResponseMethodsReturnNullActionForwardTest";
	
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
