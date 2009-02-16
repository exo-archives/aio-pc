/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * TODO
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsGetWriterTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;
	

	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
        ResultWriter resultWriter = new ResultWriter(testName);
        resultWriter.setStatus(ResultWriter.PASS);

        checkGetWriter(request, response, resultWriter);
        
        return resultWriter.toString();
	}


    private void checkGetWriter(HttpServletRequest request,
    		HttpServletResponse response, ResultWriter resultWriter)
        throws IOException {

    	Boolean expectedResultNotNull = 
    		(Boolean)getAndRemoveSessionAttribute(request,"getWriter");
    	
    	PrintWriter result = response.getWriter();
    	
    	if (!expectedResultNotNull && (result != null)) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getWriter():");
    		resultWriter.addDetail("Expected result = null");
    		resultWriter.addDetail("Actual result = " + result);
    	}
    	else if(expectedResultNotNull && (result == null)){
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getWriter():");
    		resultWriter.addDetail("Expected result = non-null value");
    		resultWriter.addDetail("Actual result = null");
    	}
    }


}
