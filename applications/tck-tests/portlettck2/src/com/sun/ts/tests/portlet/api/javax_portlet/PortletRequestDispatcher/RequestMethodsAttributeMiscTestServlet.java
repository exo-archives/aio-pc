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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the following methods of the
 * HttpServletRequest are equivalent to the methods of the
 * PortletRequest of similar name: getAttribute, getAttributeNames,
 * setAttribute, removeAttribute.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
 
public class RequestMethodsAttributeMiscTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;
	

    @Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
    	
        ResultWriter resultWriter = new ResultWriter(testName);
        resultWriter.setStatus(ResultWriter.PASS);
        
        checkGetAttribute(request, resultWriter);
        checkGetAttributeNames(request, resultWriter);
        checkSetAttribute(request, resultWriter);
        checkRemoveAttribute(request, resultWriter);
        
        return resultWriter.toString();
	}

    private void checkGetAttribute(HttpServletRequest request,
                                   ResultWriter resultWriter) {

        String expectedResult = 
        	(String)getSessionAttribute(request, "getAttribute1");
        
        String result = (String)request.getAttribute(testName);

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getAttribute():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    @SuppressWarnings("unchecked")
    private void checkGetAttributeNames(HttpServletRequest request,
                                        ResultWriter resultWriter) {

    	String expectedResult1 = (String)getAndRemoveSessionAttribute(request, "getAttribute1");
    	boolean attribute1 = false;
    	String expectedResult2 = (String)getAndRemoveSessionAttribute(request, "getAttribute2");
    	boolean attribute2 = false;
    	Enumeration attributeNames = request.getAttributeNames();
    	while(attributeNames.hasMoreElements()&& !(attribute1 && attribute2)){
    		String name = (String)attributeNames.nextElement();
    		if (name.equals(expectedResult1))
    			attribute1 = true;
    		if (name.equals(expectedResult2))
    			attribute2 = true;
    	}
    	if (!attribute1 || !attribute2){
	          resultWriter.setStatus(ResultWriter.FAIL);
	          resultWriter.addDetail("getAttributeNames():");
	          resultWriter.addDetail(attributeNames.toString());
    	}
//        Enumeration<String> expectedResult =
//            (Enumeration<String>)getAndRemoveSessionAttribute(request, "getAttributeNames");
//
//        Enumeration<String> result = request.getAttributeNames();
//
//        ListCompare listCompare = new ListCompare(expectedResult,
//                                                  result,
//                                                  null,
//                                                  ListCompare.SUBSET_MATCH);
//
//        if (listCompare.misMatch()) {
//            resultWriter.setStatus(ResultWriter.FAIL);
//            resultWriter.addDetail("getAttributeNames():");
//            resultWriter.addDetail(listCompare.getMisMatchReason());
//        }
    }

    private void checkSetAttribute(HttpServletRequest request,
                                   ResultWriter resultWriter) {

        request.setAttribute("setAttribute", "setAttribute");
    }

    private void checkRemoveAttribute(HttpServletRequest request,
                                      ResultWriter resultWriter) {

        request.removeAttribute(testName);
    }
}
