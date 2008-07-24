/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class verify that the PortletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE)
 * method returned the correct value.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetLifecyclePhaseAttributeTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"GetLifecyclePhaseAttributeTest";
	
	
	public void render(RenderRequest request, RenderResponse response)
		throws PortletException, IOException {

		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		RequestCount reqCount = new RequestCount(request,
										response,
										RequestCount.MANAGED_VIA_SESSION);
		
		if (reqCount.isFirstRequest()) {
			
			PortletURLTag portletURLTag = new PortletURLTag();
			portletURLTag.appendTagContent(
					response.createResourceURL().toString());
			
			out.println(portletURLTag.toString());
			
		}
	}
	
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		String phase = (String)request.getAttribute(PortletRequest.LIFECYCLE_PHASE);
		
		if (phase != null && phase.equals(PortletRequest.RESOURCE_PHASE))
			resultWriter.setStatus(ResultWriter.PASS);
		else {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			
			resultWriter.addDetail("PortletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE)");
			resultWriter.addDetail("not returned PortletRequest.RESOURCE_PHASE");
			
		}
		
		out.println(resultWriter.toString());
		
	}
}
