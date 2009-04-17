/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import javax.portlet.RenderRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderResponse;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class AccessToPathRenderIncludeTestPortlet 
	extends AbstractRenderIncludeTestPortlet {
    
	private final String TEST_NAME = "AccessToPathRenderIncludeTest";
	private final String SIMPLE_SERVLET_PATH = "/AccessToPathTestServlet";
    

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
			RenderRequest request, RenderResponse response) {
		
		super.prepareInclude(session, request, response);
		
		String contextPath = request.getContextPath();
        String requestUri = contextPath + SIMPLE_SERVLET_PATH +getPathInfo();
		
		request.setAttribute("request_uri", requestUri);
        request.setAttribute("context_path", contextPath);
        request.setAttribute("servlet_path", SIMPLE_SERVLET_PATH);
        request.setAttribute("path_info", getPathInfo());
        request.setAttribute("query_string", getQueryString());
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
