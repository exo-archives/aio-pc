/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class tests the send and receive of public render parameter.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetPublicParameterTest_1_Portlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		GetPublicParameterTestPortlet.TEST_NAME;
	
	private static final String PUBLIC_PARAMETER =
		GetPublicParameterTestPortlet.PUBLIC_PARAMETER;
	
	public void render(RenderRequest request, RenderResponse response)
			throws IOException, PortletException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		RequestCount reqCount = new RequestCount(request,
												response,
												RequestCount.MANAGED_VIA_SESSION);
		
		if (!reqCount.isFirstRequest()) {
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			
			String result = request.getParameter(PUBLIC_PARAMETER);
			
			if (result != null && result.equals(TEST_NAME))
				resultWriter.setStatus(ResultWriter.PASS);
			else {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				
				resultWriter.addDetail("Get wrong parameter.");
				resultWriter.addDetail("Expected parameter value for key " + PUBLIC_PARAMETER
						+ " is " + TEST_NAME);
				resultWriter.addDetail("Actual value is " + result);
				
			}
			
			out.println(resultWriter.toString());
			
		}
		
	}

}
