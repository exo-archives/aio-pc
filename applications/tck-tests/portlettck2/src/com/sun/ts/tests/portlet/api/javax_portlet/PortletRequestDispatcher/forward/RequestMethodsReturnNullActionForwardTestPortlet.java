/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;



/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsReturnNullActionForwardTestPortlet 
	extends AbstractActionForwardTestPortlet {
	
	private final String TEST_NAME = 
		"RequestMethodsReturnNullActionForwardTest";
	
	private final String SERVLET_PATH = 
		"/RequestMethodsReturnNullTestServlet";
    
    
    @Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}


	@Override
	protected String getTestName() {
		return TEST_NAME;
	}


}
