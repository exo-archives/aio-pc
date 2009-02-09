/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * A Test for syntax for path returned by GetContextPath() method.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetContextPathSyntaxTestPortlet extends LogicInServeResourcePortlet {
	
	public static final String TEST_NAME =
		"GetContextPathSyntaxTest";
	
	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String contextPath = request.getContextPath();
        
        if (contextPath != null) {
            if ((contextPath.length() == 0) || ((contextPath.startsWith("/")) &&
                (!contextPath.endsWith("/")))) {
            	
                resultWriter.setStatus(ResultWriter.PASS);
                
            } else {
            	
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getContextPath returned " + contextPath);
                
            }
        } else {
        	
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getContextPath returned null"); 
            
        }
        
        out.println(resultWriter.toString());
		
	}
}
