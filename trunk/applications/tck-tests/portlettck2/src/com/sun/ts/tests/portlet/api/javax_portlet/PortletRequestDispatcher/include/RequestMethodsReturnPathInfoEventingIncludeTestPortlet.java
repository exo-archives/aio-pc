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
public class RequestMethodsReturnPathInfoEventingIncludeTestPortlet 
	extends AbstractEventingIncludeTestPortlet {
	
    private final String TEST_NAME = 
    	"RequestMethodsReturnPathInfoEventingIncludeTest";
    
    private final String SIMPLE_SERVLET_PATH = 
    	"/RequestMethodsReturnPathInfoTestServlet";

  
    @Override
	protected String getServletPath() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(SIMPLE_SERVLET_PATH);
    	sb.append(getPathInfo());
    	sb.append("?");
    	sb.append(getQueryString());
		return sb.toString();
	}


	@Override
	protected String getTestName() {
		return TEST_NAME;
	}


	@Override
	protected void prepareInclude(PortletSession session, 
			EventRequest request, EventResponse response) {
		
		super.prepareInclude(session, request, response);
		
		String contextPath = request.getContextPath();
		
		StringBuffer sb = new StringBuffer();
		sb.append(contextPath);
		sb.append(SIMPLE_SERVLET_PATH);
		sb.append(getPathInfo());
		
		String requestUri = sb.toString();
		
		session.setAttribute("getPathInfo", getPathInfo(), 
				PortletSession.APPLICATION_SCOPE);
		session.setAttribute("getQueryString", getQueryString(), 
				PortletSession.APPLICATION_SCOPE);
		session.setAttribute("getRequestURI", requestUri, 
				PortletSession.APPLICATION_SCOPE);
		session.setAttribute("getServletPath", SIMPLE_SERVLET_PATH, 
				PortletSession.APPLICATION_SCOPE);
	}
	
	
    /** 
     * Returns the path info string.
     * 
     * @return the path info string.
     */
    private String getPathInfo(){
    	StringBuffer sb = new StringBuffer();
    	sb.append("/");
    	sb.append(TEST_NAME);
		return sb.toString();
    }

    
    /**
     * Returns the query string.
     * 
     * @return the query string.
     */
    private String getQueryString(){
    	StringBuffer sb = new StringBuffer();
    	sb.append(TEST_NAME);
    	sb.append("=");
    	sb.append(TEST_NAME);
		return sb.toString();
    }
	
}
