/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import java.security.Principal;
import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the following methods of the
 * HttpServletRequest are equivalent to the methods of the
 * PortletRequest of similar name: getScheme, getServerName,
 * getServerPort, isSecure, getAuthType, getContextPath,
 * getRemoteUser, getUserPrincipal, getRequestedSessionId,
 * isRequestedSessionIdValid and getCookies.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsMiscMiscTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;
	
	
	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
		ResultWriter resultWriter = new ResultWriter(testName);
		
        resultWriter.setStatus(ResultWriter.PASS);
        
        checkGetScheme(request, resultWriter);
        checkGetServerName(request, resultWriter);
        checkGetServerPort(request, resultWriter);
        checkIsSecure(request, resultWriter);
        checkGetAuthType(request, resultWriter);
        checkGetContextPath(request, resultWriter);
        checkGetRemoteUser(request, resultWriter);
        checkGetUserPrincipal(request, resultWriter);
        checkGetRequestedSessionId(request, resultWriter);
        checkIsRequestedSessionIdValid(request, resultWriter);
        checkGetCookies(request, resultWriter);
        
        return resultWriter.toString();
	}


    private void checkGetScheme(HttpServletRequest request,
                                ResultWriter resultWriter) {

        String expectedResult = 
        	(String)getAndRemoveSessionAttribute(request, "getScheme");
        
        String result = request.getScheme();

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getScheme():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkGetServerName(HttpServletRequest request,
                                    ResultWriter resultWriter) {

        String expectedResult = 
        	(String)getAndRemoveSessionAttribute(request, "getServerName");
        
        String result = request.getServerName();

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getServerName():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkGetServerPort(HttpServletRequest request,
                                    ResultWriter resultWriter) {

        Integer expectedResult = 
        	(Integer)getAndRemoveSessionAttribute(request, "getServerPort");
        
        int result = request.getServerPort();

        if (result != expectedResult.intValue()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getServerPort():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkIsSecure(HttpServletRequest request,
                               ResultWriter resultWriter) {

        Boolean expectedResult = 
        	(Boolean)getAndRemoveSessionAttribute(request, "isSecure");
        
        boolean result = request.isSecure();

        if (result != expectedResult.booleanValue()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("isSecure():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkGetAuthType(HttpServletRequest request,
                                  ResultWriter resultWriter) {

        String expectedResult = 
        	(String)getAndRemoveSessionAttribute(request, "getAuthType");
        
        String result = request.getAuthType();

        if (((result == null) && (expectedResult != null))
            || ((result != null) && !result.equals(expectedResult))) {

            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getAuthType():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkGetContextPath(HttpServletRequest request,
                                     ResultWriter resultWriter) {

        String expectedResult = 
        	(String)getAndRemoveSessionAttribute(request, "getContextPath");
        
        String result = request.getContextPath();

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getContextPath():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkGetRemoteUser(HttpServletRequest request,
                                    ResultWriter resultWriter) {

        String expectedResult = 
        	(String)getAndRemoveSessionAttribute(request, "getRemoteUser");
        
        String result = request.getRemoteUser();

        if (((result == null) && (expectedResult != null))
            || ((result != null) && !result.equals(expectedResult))) {

            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getRemoteUser():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkGetUserPrincipal(HttpServletRequest request,
                                       ResultWriter resultWriter) {

        Principal expectedResult = 
        	(Principal)getAndRemoveSessionAttribute(request, "getUserPrincipal");
        
        Principal result = request.getUserPrincipal();

        if (((result == null) && (expectedResult != null))
            || ((result != null) && !result.equals(expectedResult))) {

            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getUserPrincipal():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkGetRequestedSessionId(HttpServletRequest request,
                                            ResultWriter resultWriter) {

        String expectedResult = 
        	(String)getAndRemoveSessionAttribute(request, "getRequestedSessionId");
        
        String result = request.getRequestedSessionId();

        if (((result == null) && (expectedResult != null))
            || ((result != null) && !result.equals(expectedResult))) {

            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getRequestedSessionId():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkIsRequestedSessionIdValid(HttpServletRequest request,
                                                ResultWriter resultWriter) {

        Boolean expectedResult = 
        	(Boolean)getAndRemoveSessionAttribute(request, "isRequestedSessionIdValid");
        
        boolean result = request.isRequestedSessionIdValid();

        if (result != expectedResult.booleanValue()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("isRequestedSessionIdValid():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }
    
    private void checkGetCookies(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	Cookie[] expectedResult = 
    		(Cookie[])getAndRemoveSessionAttribute(request, "getCookies");

    	Cookie[] result = request.getCookies();
    	
    	if((result != null && expectedResult == null) || 
    			(result == null && expectedResult != null) ||
    			((result != null && expectedResult != null) && 
    					!Arrays.equals(result, expectedResult))){
    		
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getCookies():");
    		resultWriter.addDetail("Expected result = " + expectedResult);
    		resultWriter.addDetail("Actual result = " + result);
    	}
}
}
