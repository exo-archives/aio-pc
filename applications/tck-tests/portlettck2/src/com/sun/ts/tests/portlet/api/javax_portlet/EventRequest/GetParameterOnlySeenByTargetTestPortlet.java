/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This test will check that parameters only seen
 * by the target Portlet.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetParameterOnlySeenByTargetTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"GetParameterOnlySeenByTargetTest";
	
	public static final String key = "LANGUAGES";
	protected static final String[] values = new String[] {"XML", "JAVA"};

	@Override
	protected String getTestName() {
		return TEST_NAME;
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		response.setRenderParameter(key, values);
		response.setEvent(qname, TEST_NAME);
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {

	}
	public void render(RenderRequest request, RenderResponse response)
		throws PortletException, IOException {
	
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		RequestCount requestCount = 
			new RequestCount(request, 
					response, 
					RequestCount.MANAGED_VIA_SESSION);
		
		if (requestCount.isFirstRequest()) {
	
			PortletURLTag urlTag = new PortletURLTag();
			//PortletURL actionURL = response.createActionURL();
			urlTag.setTagContent(createActionURL(response));
			out.println(urlTag.toString());
	
		}
		else
			out.println(request.getPortletSession(true).getAttribute(RESULT,PortletSession.APPLICATION_SCOPE));
	}
}
