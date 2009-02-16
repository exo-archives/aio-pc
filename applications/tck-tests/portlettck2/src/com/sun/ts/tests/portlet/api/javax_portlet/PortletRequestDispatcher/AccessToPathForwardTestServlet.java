/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that a servlet being used from within an forward
 * has access to the path used to obtain the PortletRequestDispatcher.
 * The following request attributes are set:
 * javax.servlet.forward.request_uri,
 * javax.servlet.forward.context_path,
 * javax.servlet.forward.servlet_path,
 * javax.servlet.forward.path_info,
 * javax.servlet.forward.query_string.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class AccessToPathForwardTestServlet extends AbstractTestServlet {
	
	static final long serialVersionUID = 286L;
	
	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
		ResultWriter resultWriter = new ResultWriter(testName);
	    resultWriter.setStatus(ResultWriter.PASS);
	    checkRequestUri(request, resultWriter);
	    checkContextPath(request, resultWriter);
	    checkServletPath(request, resultWriter);
	    checkPathInfo(request, resultWriter);
	    checkQueryString(request, resultWriter);
	    
	    return resultWriter.toString();
	}

    private void checkRequestUri(ServletRequest request,
                                 ResultWriter resultWriter) {

        String expectedResult = (String)request.getAttribute("request_uri");

        String result
            = (String)request.getAttribute("javax.servlet.forward.request_uri");

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("javax.servlet.forward.request_uri:");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkContextPath(ServletRequest request,
                                  ResultWriter resultWriter) {

        String expectedResult = (String)request.getAttribute("context_path");

        String result
            = (String)request.getAttribute("javax.servlet.forward.context_path");

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("javax.servlet.forward.context_path:");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkServletPath(ServletRequest request,
                                  ResultWriter resultWriter) {

        String expectedResult = (String)request.getAttribute("servlet_path");
        String result
            = (String)request.getAttribute("javax.servlet.forward.servlet_path");

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("javax.servlet.forward.servlet_path:");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkPathInfo(ServletRequest request,
                               ResultWriter resultWriter) {

        String expectedResult = (String)request.getAttribute("path_info");

        String result
            = (String)request.getAttribute("javax.servlet.forward.path_info");

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("javax.servlet.forward.path_info:");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    private void checkQueryString(ServletRequest request,
                                  ResultWriter resultWriter) {

        String expectedResult = (String)request.getAttribute("query_string");

        String result
            = (String)request.getAttribute("javax.servlet.forward.query_string");

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("javax.servlet.forward.query_string:");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }
}
