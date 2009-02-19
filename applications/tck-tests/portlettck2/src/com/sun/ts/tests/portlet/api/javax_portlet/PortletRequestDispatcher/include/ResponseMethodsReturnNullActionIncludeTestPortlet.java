/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;



/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsReturnNullActionIncludeTestPortlet 
	extends AbstractActionIncludeTestPortlet {
	
	private final String TEST_NAME = 
		"ResponseMethodsReturnNullActionIncludeTest";
	
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
