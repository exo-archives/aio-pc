/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class tests the send and receive of public render parameter.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetPublicParameterTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"GetPublicParameterTest";
	
	public static final String PUBLIC_PARAMETER = "public-parameter";
	
	public void render(RenderRequest request, RenderResponse response)
			throws IOException, PortletException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		RequestCount reqCount = new RequestCount(request,
												response,
												RequestCount.MANAGED_VIA_SESSION);
		
		if (reqCount.isFirstRequest()) {
			
			PortletURL portletUrl = response.createRenderURL();
			portletUrl.setParameter(PUBLIC_PARAMETER, TEST_NAME);
			
			PortletURLTag urlTag = new PortletURLTag();
			urlTag.setTagContent(portletUrl.toString());
			
			out.println(urlTag.toString());
			
		}
	}
}
