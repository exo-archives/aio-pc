/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;

import javax.portlet.RenderRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsAttributeMiscRenderForwardTestPortlet 
	extends AbstractRenderForwardTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsAttributeMiscRenderForwardTest";
	
	private final String SERVLET_PATH = 
		"/RequestMethodsAttributeMiscTestServlet";
    

    @Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}

    
	@Override
	protected String getTestName() {
		return TEST_NAME;
	}
	
	
	@Override
	protected boolean isServletPrintingResults() {
		return false;
	}

	
	@Override
	protected void prepareForward(PortletSession session, 
			RenderRequest request, RenderResponse response) throws IOException {
		
		super.prepareForward(session, request, response);
		
		request.setAttribute(TEST_NAME, TEST_NAME);
        request.setAttribute(TEST_NAME + SERVLET_PATH, TEST_NAME 
        		+ SERVLET_PATH);

        session.setAttribute("getAttribute1",
                             request.getAttribute(TEST_NAME),
                             PortletSession.APPLICATION_SCOPE);
        
        session.setAttribute("getAttribute2",
			                 request.getAttribute(TEST_NAME + SERVLET_PATH),
			                 PortletSession.APPLICATION_SCOPE);
	}
	
	
	@Override
	protected void checkForwardResults(PortletSession session, 
			RenderRequest request) {
		
		ResultWriter resultWriter = null;
		
		//check if attribute was set successfully
		String expectedResult = "setAttribute";
        String result = (String)request.getAttribute("setAttribute");

        if ((result == null) || !result.equals(expectedResult)) {
        	resultWriter = new ResultWriter(TEST_NAME);
        	
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("setAttribute():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }

        //check if attribute was removed successfully
        result = (String)request.getAttribute(TEST_NAME);

        if (result != null) {
        	resultWriter = new ResultWriter(TEST_NAME);
        	
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("removeAttribute():");
            resultWriter.addDetail("Expected result = null");
            resultWriter.addDetail("Actual result = " + result);      
        }
		        
        
        if(resultWriter != null){       
        	session.setAttribute(RESULT_ATTR, resultWriter.toString(),
        			PortletSession.APPLICATION_SCOPE);
        }
	}



	

}
