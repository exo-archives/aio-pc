/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;


/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsParameterMiscServeResourceIncludeTestPortlet 
	extends AbstractServeResourceIncludeTestPortlet {
	
    private final String TEST_NAME = 
    	"RequestMethodsParameterMiscServeResourceIncludeTest";
    
    private final String SERVLET_PATH = 
    	"/RequestMethodsParameterMiscTestServlet";


	@Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}


	@Override
	protected String getTestName() {
		return TEST_NAME;
	}
	

	@Override
	protected Map<String, String[]> getParametersMap() {
		Map<String, String[]> result = new HashMap<String, String[]>();
		
		result.put("numbers", new String[]{"One", "Two", "Three"});
		result.put("language", new String[]{"Java"});
		
		return result;
	}
   

	@Override
	protected void prepareInclude(PortletSession session, 
			ResourceRequest request, ResourceResponse response) {
		
		super.prepareInclude(session, request, response);
		
		session.setAttribute("getParameter",
                request.getParameter("language"),
                PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getParameterNames",
                request.getParameterNames(),
                PortletSession.APPLICATION_SCOPE);
		
		session.setAttribute("getParameterValues",
                request.getParameterValues("numbers"),
                PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getParameterMap",
                request.getParameterMap(),
                PortletSession.APPLICATION_SCOPE);
	}
}
