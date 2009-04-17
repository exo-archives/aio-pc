/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the following methods of the
 * HttpServletRequest return null: getRemoteAddr, getRemoteHost, 
 * getLocalAddr, getLocalName, getRealPath, getRequestURL.
 *
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */

public class RequestMethodsReturnNullTestServlet extends AbstractTestServlet {
    
	private static final long serialVersionUID = 286L;
  
	
    @Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
    	
        ResultWriter resultWriter = new ResultWriter(testName);
        resultWriter.setStatus(ResultWriter.PASS);
        
        checkGetRemoteAddr(request, resultWriter);
        checkGetRemoteHost(request, resultWriter);
        checkGetLocalAddr(request, resultWriter);
        checkGetLocalName(request, resultWriter);
        checkGetRealPath(request, resultWriter);                
        checkGetRequestURL(request, resultWriter);
        
        return resultWriter.toString();
	}

    
    private void checkGetRemoteAddr(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	String result = request.getRemoteAddr();
    	if (result != null) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getRemoteAddr():");
    		resultWriter.addDetail("Expected result = null");
    		resultWriter.addDetail("Actual result = " + result);
    	}
    }

    
    private void checkGetRemoteHost(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	String result = request.getRemoteHost();

    	if (result != null) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getRemoteHost():");
    		resultWriter.addDetail("Expected result = null");
    		resultWriter.addDetail("Actual result = " + result);
    	}
    }

    
    @SuppressWarnings("deprecation")
    private void checkGetRealPath(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	String result = request.getRealPath(null);

    	if (result != null) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getRealPath(null):");
    		resultWriter.addDetail("Expected result = null");
    		resultWriter.addDetail("Actual result = " + result);
    	}
    }

    
    private void checkGetRequestURL(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	StringBuffer result = request.getRequestURL();

    	if (result != null) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getRequestURL():");
    		resultWriter.addDetail("Expected result = null");
    		resultWriter.addDetail("Actual result = " + result);
    	}
    }

    
    private void checkGetLocalAddr(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	String result = request.getLocalAddr();

    	if (result != null) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getLocalAddr():");
    		resultWriter.addDetail("Expected result = null");
    		resultWriter.addDetail("Actual result = " + result);
    	}
    }

    
    private void checkGetLocalName(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	String result = request.getLocalName();

    	if (result != null) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getLocalName():");
    		resultWriter.addDetail("Expected result = null");
    		resultWriter.addDetail("Actual result = " + result);
    	}
    }


}
