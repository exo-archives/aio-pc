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
 * HttpServletRequest return 0: getRemotePort, getLocalPort.
 *
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */

public class RequestMethodsReturn0TestServlet extends AbstractTestServlet {
    
	private static final long serialVersionUID = 286L;
	
  
    @Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
    	
        ResultWriter resultWriter = new ResultWriter(testName);
        resultWriter.setStatus(ResultWriter.PASS);
        
        checkGetRemotePort(request, resultWriter);
        checkGetLocalPort(request, resultWriter);
        
        return resultWriter.toString();
	}

    
    private void checkGetRemotePort(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	int result = request.getRemotePort();

    	if (result != 0) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getRemotePort():");
    		resultWriter.addDetail("Expected result = 0");
    		resultWriter.addDetail("Actual result = " + result);
    	}
    }
    
    
    private void checkGetLocalPort(HttpServletRequest request,
    		ResultWriter resultWriter) {

    	int result = request.getLocalPort();

    	if (result != 0) {
    		resultWriter.setStatus(ResultWriter.FAIL);
    		resultWriter.addDetail("getLocalPort():");
    		resultWriter.addDetail("Expected result = 0");
    		resultWriter.addDetail("Actual result = " + result);
    	}
    }
   
}
