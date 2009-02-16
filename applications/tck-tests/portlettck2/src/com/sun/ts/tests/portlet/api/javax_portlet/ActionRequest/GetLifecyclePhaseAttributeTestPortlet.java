/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
	
	private static final String RESULT = "result";
	
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		String phase = (String)request.getAttribute(PortletRequest.LIFECYCLE_PHASE);
		
		if (phase != null && phase.equals(PortletRequest.ACTION_PHASE))
			resultWriter.setStatus(ResultWriter.PASS);
		else {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			
			resultWriter.addDetail("PortletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE)");
			resultWriter.addDetail("not returned PortletRequest.ACTION_PHASE");
			
		}
		
		request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
		
	}
	
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
					response.createActionURL().toString());
			
			out.println(portletURLTag.toString());
			
		} else {
			
			out.println(request.getPortletSession(true).getAttribute(RESULT));
			
		}
	}
}
