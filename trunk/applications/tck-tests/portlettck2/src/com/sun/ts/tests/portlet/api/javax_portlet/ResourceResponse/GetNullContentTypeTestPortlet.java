/*
 * Copyright 2007 IBM Corporation
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that getContentType() returns null
 * without setting the content type.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since 2.0
 */

public class GetNullContentTypeTestPortlet extends LogicInServeResourcePortlet {
	
	public static final String TEST_NAME =
		"GetNullContentTypeTest";

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String result = response.getContentType();

        if (result == null) {
        	
            resultWriter.setStatus(ResultWriter.PASS);
            
        } else {
        	
            resultWriter.setStatus(ResultWriter.FAIL);
            
            resultWriter.addDetail("Expected result = null");
            resultWriter.addDetail("Actual result = " + result);
            
        }
        
        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();
        
        out.println(resultWriter.toString());
	}
}
