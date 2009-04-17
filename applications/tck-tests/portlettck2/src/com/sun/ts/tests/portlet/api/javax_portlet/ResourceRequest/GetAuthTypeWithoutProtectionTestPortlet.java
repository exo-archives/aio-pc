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
 * A Negative Test for getAuthType method.
 *
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */

public class GetAuthTypeWithoutProtectionTestPortlet extends
		LogicInServeResourcePortlet {
	
	public static final String TEST_NAME =
		"GetAuthTypeWithoutProtectionTest";

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        
        String result = request.getAuthType();

        if(result == null )
            resultWriter.setStatus(ResultWriter.PASS);
        else {
        	
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "PortletRequest.getAuthType() returned a non-null result, "
                + " even though Portlet is not protected" );
            resultWriter.addDetail( "     Actual result = |" + result + "|" );
            
        }
        
        out.println(resultWriter.toString());

	}
}
