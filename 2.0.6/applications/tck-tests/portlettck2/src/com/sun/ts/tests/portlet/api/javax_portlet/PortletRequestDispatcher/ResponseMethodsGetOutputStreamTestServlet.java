/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * TODO:
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsGetOutputStreamTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;
	

	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
        ResultWriter resultWriter = new ResultWriter(testName);
        resultWriter.setStatus(ResultWriter.PASS);
        
        Boolean expectedResultNotNull = (Boolean)getAndRemoveSessionAttribute(request,"getOutputStream");
    	
    	ServletOutputStream outputStream = response.getOutputStream();

    	if (!expectedResultNotNull && (outputStream != null)) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getOutputStream():");
    		resultWriter.addDetail("Expected result = null");
    		resultWriter.addDetail("Actual result = " + outputStream);
    	}
    	else if(expectedResultNotNull && (outputStream == null)){
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getOutputStream():");
    		resultWriter.addDetail("Expected result = non-null value");
    		resultWriter.addDetail("Actual result = null");
    	}
    	if((outputStream != null) && (writeResult)){
    		outputStream.println(resultWriter.toString());
    		return null;//avoid getWriter in AbstractTestServlet
    	}
    	
    	return resultWriter.toString();	
    	
	}    
}
