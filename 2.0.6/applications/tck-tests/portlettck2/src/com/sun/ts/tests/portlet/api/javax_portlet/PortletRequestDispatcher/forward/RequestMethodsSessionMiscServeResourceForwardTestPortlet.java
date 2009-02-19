/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;

import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsSessionMiscServeResourceForwardTestPortlet 
	extends AbstractServeResourceForwardTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsSessionMiscServeResourceForwardTest";
	
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
	protected void prepareForward(PortletSession session, 
			ResourceRequest request, ResourceResponse response) throws IOException{
		super.prepareForward(session, request, response);
		
		session.setAttribute("test1",
				"test1",
				PortletSession.APPLICATION_SCOPE);

	}


	@Override
	protected void checkForwardResults(PortletSession session, 
			ResourceRequest request) {
		
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


	@Override
	protected boolean isServletPrintingResults() {
		return false;
	}	
	
	

}
