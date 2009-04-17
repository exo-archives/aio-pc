/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This test will check that parameters only seen
 * by the target Portlet.
 *  
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetParameterOnlySeenByTargetTest_1_Portlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		GetParameterOnlySeenByTargetTestPortlet.TEST_NAME;

	@Override
	protected String getTestName() {
		return TEST_NAME;
	}
	
	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		RequestCount requestCount = 
			new RequestCount(request, 
					response, 
					RequestCount.MANAGED_VIA_SESSION);
		
		if (!requestCount.isFirstRequest()) 
			out.println(request.getPortletSession(true).getAttribute(RESULT));
		
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {

	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		
		Event evt = request.getEvent();
		
		if (evt.getQName().getLocalPart().equals(qname.getLocalPart())) {
			
			ResultWriter writer = new ResultWriter(TEST_NAME);
			
			String[] actualValues = request.getParameterValues(GetParameterOnlySeenByTargetTestPortlet.key);
			
			if (actualValues == null)
				writer.setStatus(ResultWriter.PASS);
			else {
				writer.setStatus(ResultWriter.FAIL);
				writer.addDetail("Untargeted portlet can see the parameters set by"
	                    + " targeted portlets.");
			}
			
			request.getPortletSession(true).setAttribute(RESULT, writer.toString(), PortletSession.APPLICATION_SCOPE);
		}
	}
}
