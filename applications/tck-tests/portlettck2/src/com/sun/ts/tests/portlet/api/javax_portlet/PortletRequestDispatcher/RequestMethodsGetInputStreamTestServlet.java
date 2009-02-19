/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the following methods of the
 * HttpServletRequest return the expected results: 
 * getInputStream
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsGetInputStreamTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;
	

	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
		ResultWriter resultWriter = new ResultWriter(testName);
		
        resultWriter.setStatus(ResultWriter.PASS);
        
        checkGetInputStream(request, resultWriter);
        
        return resultWriter.toString();
	}


	private void checkGetInputStream(HttpServletRequest request,
			ResultWriter resultWriter) {

		Boolean expectedResultNotNull = 
			(Boolean)getAndRemoveSessionAttribute(request, "getInputStream");
		
		ServletInputStream result = null;
		try{ 
			result = request.getInputStream();
		}
		catch(IOException e){
			result = null;
		}
		if(!expectedResultNotNull && (result != null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getInputStream():");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
		else if (expectedResultNotNull && (result == null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getInputStream():");
			resultWriter.addDetail("Expected result = non-null value");
			resultWriter.addDetail("Actual result = null");
		}
	}
}
