/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class verify that only valid public-render-parameter send by the
 * container and only valid targets receive that parameters.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetPublicParameterOnlySeenByTargetTest_2_Portlet extends
		GenericPortlet {
	
	public static final String TEST_NAME =
		GetPublicParameterOnlySeenByTargetTestPortlet.TEST_NAME;
	
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		ResultWriter writer = new ResultWriter(TEST_NAME);
		
		RequestCount reqCount = new RequestCount(request,
									response,
									RequestCount.MANAGED_VIA_SESSION);
		
		if (!reqCount.isFirstRequest()) {
			
			Map<String, String[]> param = request.getParameterMap();
			
			if (param.size() != 0) {
				writer.setStatus(ResultWriter.FAIL);
				writer.addDetail("Portlet received unrequested parameter.");
			} else
				writer.setStatus(ResultWriter.PASS);
			
			out.println(writer.toString());			
		}
		
	}

}
