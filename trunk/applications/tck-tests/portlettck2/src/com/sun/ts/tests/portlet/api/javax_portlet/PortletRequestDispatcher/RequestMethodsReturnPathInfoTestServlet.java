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
 * HttpServletRequest return the path and query string information
 * used to obtain the PortletRequestDispatcher object: getPathInfo,
 * getQueryString, getRequestURI and getServletPath.
 *
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsReturnPathInfoTestServlet extends AbstractTestServlet {
    
	private static final long serialVersionUID = 286L;
	
	
    @Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
        ResultWriter resultWriter = new ResultWriter(testName);
        resultWriter.setStatus(ResultWriter.PASS);
        
        checkGetPathInfo(request, resultWriter);
        checkGetQueryString(request, resultWriter);
        checkGetRequestURI(request, resultWriter);
        checkGetServletPath(request, resultWriter);
        
        return resultWriter.toString();
	}

    private void checkGetPathInfo(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	String expectedResult = 
    		(String)getAndRemoveSessionAttribute(request, "getPathInfo");
    	
        String result = request.getPathInfo();

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPathInfo():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }
    
    private void checkGetQueryString(HttpServletRequest request,
                                     ResultWriter resultWriter) {

    	String expectedResult = 
    		(String)getAndRemoveSessionAttribute(request, "getQueryString");
    	
        String result = request.getQueryString();

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getQueryString():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkGetRequestURI(HttpServletRequest request,
                                    ResultWriter resultWriter) {

    	String expectedResult = 
    		(String)getAndRemoveSessionAttribute(request, "getRequestURI");
    	
        String result = request.getRequestURI();

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getRequestURI():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkGetServletPath(HttpServletRequest request,
                                     ResultWriter resultWriter) {

    	String expectedResult = 
    		(String)getAndRemoveSessionAttribute(request, "getServletPath");
    	
        String result = request.getServletPath();

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getServletPath():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }
}
