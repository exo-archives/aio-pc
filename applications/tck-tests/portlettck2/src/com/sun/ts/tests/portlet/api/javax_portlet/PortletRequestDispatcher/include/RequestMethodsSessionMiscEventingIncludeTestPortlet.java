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

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsSessionMiscEventingIncludeTestPortlet 
	extends AbstractEventingIncludeTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsSessionMiscEventingIncludeTest";
	
	private final String SERVLET_PATH = 
		"/RequestMethodsSessionMiscTestServlet";
    
    
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
			EventRequest request, EventResponse response) {
		
		super.prepareInclude(session, request, response);
		
		session.setAttribute("test1",
				"test1",
				PortletSession.APPLICATION_SCOPE);

	}


	@Override
	protected void checkIncludeResults(PortletSession session, 
			EventRequest request) {
		
		String servletResult = 
			(String)session.getAttribute(RESULT_ATTR, 
					PortletSession.APPLICATION_SCOPE);
		
		if(servletResult == null){
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getSession():");
            resultWriter.addDetail("This method does not provide the functionality " +
            		"defined by the Servlet Specification or the provided " + 
            		"HttpSession instance is not accessible from the current "+ 
            		"PortletSession instance via PortletSession.APPLICATION_SCOPE");
            
            session.setAttribute(RESULT_ATTR, resultWriter.toString(), 
            		PortletSession.APPLICATION_SCOPE);
		}		
	}

}
