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
 * This class tests that some request attributes are the same portlet
 * API objects accessible to the portlet doing the include call.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class IncludedRequestAttributesTestServlet extends AbstractTestServlet {
	
	static final long serialVersionUID=286L;
	
	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
		ResultWriter resultWriter = new ResultWriter(testName);
		resultWriter.setStatus(ResultWriter.PASS);
		
		checkPortletConfigAttribute(request, resultWriter);
		checkRequestAttribute(request, resultWriter);
		checkResponseAttribute(request, resultWriter);
		
		return resultWriter.toString();
	}

	private void checkPortletConfigAttribute(HttpServletRequest request, ResultWriter resultWriter){

		Object expectedResult = request.getAttribute("config");
        Object result = request.getAttribute("javax.portlet.config");

        if ((result == null) || (!result.equals(expectedResult))) {

            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("javax.portlet.config:");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
	}
	
	private void checkRequestAttribute(HttpServletRequest request, ResultWriter resultWriter){

		Object expectedResult = request.getAttribute("request");
        Object result = request.getAttribute("javax.portlet.request");

        if ((result == null) || (!result.equals(expectedResult))) {

            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("javax.portlet.request:");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
	}
	
	private void checkResponseAttribute(HttpServletRequest request, ResultWriter resultWriter){

		Object expectedResult = request.getAttribute("response");
        Object result = request.getAttribute("javax.portlet.response");

        if ((result == null) || (!result.equals(expectedResult))) {

            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("javax.portlet.response:");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
	}

}
