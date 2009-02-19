/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the following methods of the
 * HttpServletResponse return the expected results: 
 * encodeURL, encodeUrl, getBufferSize, getCharacterEncoding, 
 * getContentType, getLocale and isCommitted.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsMiscMiscTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;
	

	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
        ResultWriter resultWriter = new ResultWriter(testName);
        resultWriter.setStatus(ResultWriter.PASS);
        
        checkEncodeURL(request, response, resultWriter);
        checkEncodeUrl(request, response, resultWriter);
        checkGetBufferSize(request, response, resultWriter);
        checkGetCharacterEncoding(request, response, resultWriter);     
        checkGetContentType(request, response, resultWriter);    
        checkGetLocale(request, response, resultWriter);
        checkIsCommitted(request, response, resultWriter);
        
        return resultWriter.toString();
	}

	
	private void checkEncodeURL(HttpServletRequest request,
			HttpServletResponse response, ResultWriter resultWriter) {
	
	    String expectedResult = 
	    	(String)getAndRemoveSessionAttribute(request, "encodeURL");
	    String path = 
	    	(String)getSessionAttribute(request, "encodeUrlPath");
	    String result = response.encodeURL(path);
	
	    if ((result == null) || !result.equals(expectedResult)) {
	        resultWriter.setStatus(ResultWriter.FAIL);
	        resultWriter.addDetail("encodeURL():");
	        resultWriter.addDetail("Expected result = " + expectedResult);
	        resultWriter.addDetail("Actual result = " + result);
	    }
	}


	@SuppressWarnings("deprecation")
	private void checkEncodeUrl(HttpServletRequest request,
			HttpServletResponse response, ResultWriter resultWriter) {
	
	    String expectedResult = 
	    	(String)getAndRemoveSessionAttribute(request, "encodeUrl");
	    String path = 
	    	(String)getAndRemoveSessionAttribute(request, "encodeUrlPath");
	    String result = response.encodeUrl(path);
	
	    if ((result == null) || !result.equals(expectedResult)) {
	        resultWriter.setStatus(ResultWriter.FAIL);
	        resultWriter.addDetail("encodeUrl():");
	        resultWriter.addDetail("Expected result = " + expectedResult);
	        resultWriter.addDetail("Actual result = " + result);
	    }
	}


	private void checkGetBufferSize(HttpServletRequest request,
			HttpServletResponse response, ResultWriter resultWriter) {
	
	    Integer expectedResult = 
	    	(Integer)getAndRemoveSessionAttribute(request, "getBufferSize");
	    int result = response.getBufferSize();
	
	    if (result != expectedResult.intValue()) {
	        resultWriter.setStatus(ResultWriter.FAIL);
	        resultWriter.addDetail("getBufferSize():");
	        resultWriter.addDetail("Expected result = " + expectedResult);
	        resultWriter.addDetail("Actual result = " + result);
	    }
	}


	private void checkGetCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response, ResultWriter resultWriter) {
	
	    String expectedResult = 
	    	(String)getAndRemoveSessionAttribute(request, "getCharacterEncoding");
	    String result = response.getCharacterEncoding();
	
	    if((expectedResult == null) && (result != null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getCharacterEncoding():");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
		else if ((expectedResult != null) && 
				((result == null) || !result.equals(expectedResult))) {
	        resultWriter.setStatus(ResultWriter.FAIL);
	        resultWriter.addDetail("getCharacterEncoding():");
	        resultWriter.addDetail("Expected result = " + expectedResult);
	        resultWriter.addDetail("Actual result = " + result);
	    }
	}


	private void checkGetContentType(HttpServletRequest request,
			HttpServletResponse response, ResultWriter resultWriter) {

		String expectedResult = 
			(String)getAndRemoveSessionAttribute(request, "getContentType");
		String result = response.getContentType();

		if((expectedResult == null) && (result != null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getContentType():");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
		else if ((expectedResult != null) && 
				((result == null) || !result.equals(expectedResult))) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getContentType():");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}
	
	private void checkGetLocale(HttpServletRequest request,
			HttpServletResponse response, ResultWriter resultWriter) {
	
	    Locale expectedResult = 
	    	(Locale)getAndRemoveSessionAttribute(request, "getLocale");
	    Locale result = response.getLocale();
	
	    if((expectedResult == null) && (result != null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getLocale():");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
		else if ((expectedResult != null) && 
				((result == null) || !result.equals(expectedResult))) {
	        resultWriter.setStatus(ResultWriter.FAIL);
	        resultWriter.addDetail("getLocale():");
	        resultWriter.addDetail("Expected result = " + expectedResult);
	        resultWriter.addDetail("Actual result = " + result);
	    }
	}


	private void checkIsCommitted(HttpServletRequest request,
			HttpServletResponse response, ResultWriter resultWriter) {

        Boolean expectedResult = 
        	(Boolean)getAndRemoveSessionAttribute(request, "isCommitted");
        boolean result = response.isCommitted();

        if (result != expectedResult.booleanValue()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("isCommitted():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }
}
