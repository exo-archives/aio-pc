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
 * This class tests the getContentType() method.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since 2.0
 */

public class GetContentTypeTestPortlet extends LogicInServeResourcePortlet {
	
	public static final String TEST_NAME =
		"GetContentTypeTest";

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String expectedResult = "text/html";

        try {
        	
            response.setContentType(expectedResult);
            String result = response.getContentType();

            if ((result != null) && result.equalsIgnoreCase(expectedResult)) {
            	
                resultWriter.setStatus(ResultWriter.PASS);
                
            } else {
            	
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected result = " + expectedResult);
                resultWriter.addDetail("Actual result = " + result);
                
            }
        } catch (IllegalArgumentException e) {
        	
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(expectedResult + " isn't allowed!");
            
        }

        PrintWriter out = response.getWriter();
        
        out.println(resultWriter.toString());
	}

}
