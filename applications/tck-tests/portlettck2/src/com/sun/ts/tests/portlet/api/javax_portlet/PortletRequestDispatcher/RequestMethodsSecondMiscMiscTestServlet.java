/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the following methods of the
 * HttpServletRequest return the expected results: 
 * getCharacterEncoding, getContentLength, getContentType,
 * getMethod and getProtocol.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsSecondMiscMiscTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;
	

	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) {
		
		ResultWriter resultWriter = new ResultWriter(testName);
		
        resultWriter.setStatus(ResultWriter.PASS);
        
        checkGetCharacterEncoding(request, resultWriter);
        checkGetContentLength(request, resultWriter);
        checkGetContentType(request, resultWriter);
        checkGetMethod(request, resultWriter);
        checkGetProtocol(request, resultWriter);
        
        return resultWriter.toString();
	}


	private void checkGetCharacterEncoding(HttpServletRequest request,
			ResultWriter resultWriter) {

		String expectedResult = 
			(String)getAndRemoveSessionAttribute(request, "getCharacterEncoding");

		String result = request.getCharacterEncoding();

		if((expectedResult == null) && (result == null)){
			return;
		}
		if ((result == null) || !result.equals(expectedResult)) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getCharacterEncoding():");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}
    
	private void checkGetContentLength(HttpServletRequest request,
			ResultWriter resultWriter) {

		int expectedResult = 
			(Integer)getAndRemoveSessionAttribute(request, "getContentLength");

		int result = request.getContentLength();

		if (expectedResult != result) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getContentLength():");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}
	
	private void checkGetContentType(HttpServletRequest request,
			ResultWriter resultWriter) {

		String expectedResult = 
			(String)getAndRemoveSessionAttribute(request, "getContentType");

		String result = request.getContentType();

		if((expectedResult == null) && (result == null)){
			return;
		}
		if ((result == null) || !result.equals(expectedResult)) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getContentType():");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}
	
	private void checkGetMethod(HttpServletRequest request,
			ResultWriter resultWriter) {

		String expectedResult = 
			(String)getAndRemoveSessionAttribute(request, "getMethod");

		String result = request.getMethod();

		if((expectedResult == null) && (result == null)){
			return;
		}
		if ((result == null) || !result.equals(expectedResult)) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getMethod():");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}
	
	private void checkGetProtocol(HttpServletRequest request,
			ResultWriter resultWriter) {

		String expectedResult = 
			(String)getAndRemoveSessionAttribute(request, "getProtocol");
		
		String result = request.getProtocol();

		if((expectedResult == null) && (result == null)){
			return;
		}
		if ((result == null) || !result.equals(expectedResult)) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getProtocol():");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

}
