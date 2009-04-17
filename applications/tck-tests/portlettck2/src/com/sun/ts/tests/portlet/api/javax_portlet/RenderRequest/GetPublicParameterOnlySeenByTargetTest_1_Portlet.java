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

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class verify that only valid public-render-parameter send by the
 * container and only valid targets receive that parameters.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetPublicParameterOnlySeenByTargetTest_1_Portlet extends
		GenericPortlet {
	
	public static final String TEST_NAME =
		GetPublicParameterOnlySeenByTargetTestPortlet.TEST_NAME;
	
	public void render(RenderRequest request, RenderResponse response) 
			throws PortletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		RequestCount requestCount = new RequestCount(request,
										response,
										RequestCount.MANAGED_VIA_SESSION);

		if (!requestCount.isFirstRequest()) {
			
			ResultWriter writer = new ResultWriter(TEST_NAME);
			
			String[] actualPublic = request.getParameterValues(
								GetPublicParameterOnlySeenByTargetTestPortlet
									.PUBLIC_PARAMETER);
				
			String[] expected = GetPublicParameterOnlySeenByTargetTestPortlet
									.map.get(
											GetPublicParameterOnlySeenByTargetTestPortlet
												.PUBLIC_PARAMETER);
			
			ListCompare listCompare = new ListCompare(expected,
												actualPublic,
												null,
												ListCompare.ALL_ELEMENTS_MATCH);
			
			if (listCompare.misMatch()) {
				
				writer.setStatus(ResultWriter.FAIL);
				writer.addDetail(listCompare.getMisMatchReason());
				
			} else
				writer.setStatus(ResultWriter.PASS);
			
	
			out.println(writer.toString());
		}
	}
}
