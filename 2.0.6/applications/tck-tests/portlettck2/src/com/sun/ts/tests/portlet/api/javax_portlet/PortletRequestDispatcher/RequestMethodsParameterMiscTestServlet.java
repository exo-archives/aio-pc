/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.MapCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the following methods of the
 * HttpServletRequest are equivalent to the methods of the
 * PortletRequest of similar name: getParameter, getParameterNames,
 * getParameterValues, getParameterMap.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsParameterMiscTestServlet extends AbstractTestServlet { 
    
	private static final long serialVersionUID = 286L;
	

	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
		ResultWriter resultWriter = new ResultWriter(testName);
        
		resultWriter.setStatus(ResultWriter.PASS);
		
        checkGetParameter(request, resultWriter);
        checkGetParameterNames(request, resultWriter);
        checkGetParameterValues(request, resultWriter);
        checkGetParameterMap(request, resultWriter);
        
        return resultWriter.toString();
	}

    private void checkGetParameter(HttpServletRequest request,
                                   ResultWriter resultWriter) {

        String expectedResult = 
        	(String)getAndRemoveSessionAttribute(request, "getParameter");
        
        String result = (String)request.getParameter("language");

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getParameter():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    @SuppressWarnings("unchecked")
    private void checkGetParameterNames(HttpServletRequest request,
                                        ResultWriter resultWriter) {

        Enumeration expectedResult = 
        	(Enumeration)getAndRemoveSessionAttribute(request, "getParameterNames");

        Enumeration result = request.getParameterNames();

        ListCompare listCompare = new ListCompare(expectedResult,
                                                  result,
                                                  null,
                                                  ListCompare.SUBSET_MATCH);

        if (listCompare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getParameterNames():");
            resultWriter.addDetail(listCompare.getMisMatchReason());
        }
    }

    private void checkGetParameterValues(HttpServletRequest request,
                                         ResultWriter resultWriter) {

        String[] expectedResult = 
        	(String[])getAndRemoveSessionAttribute(request, "getParameterValues");

        String[] result = request.getParameterValues("numbers");

        ListCompare listCompare = new ListCompare(expectedResult,
                                                  result,
                                                  null,
                                                  ListCompare.SUBSET_MATCH);

        if (listCompare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getParameterValues():");
            resultWriter.addDetail(listCompare.getMisMatchReason());
        }
    }

    @SuppressWarnings("unchecked")
    private void checkGetParameterMap(HttpServletRequest request,
                                      ResultWriter resultWriter) {

        Map expectedResult = 
        	(Map)getAndRemoveSessionAttribute(request, "getParameterMap");
        
        Map result = request.getParameterMap();
        MapCompare mapCompare = new MapCompare(expectedResult, result);

        if (mapCompare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getParameterMap():");
            resultWriter.addDetail(mapCompare.getMisMatchReason());
        }
    }
}
