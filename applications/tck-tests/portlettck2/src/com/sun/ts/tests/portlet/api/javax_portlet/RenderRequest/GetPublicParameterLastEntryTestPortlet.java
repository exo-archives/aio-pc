/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class that public render parameter with the same name like
 * action parameter are the last entry in the returned list array.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetPublicParameterLastEntryTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"GetPublicParameterLastEntryTest";
	
	private static final String PUBLIC_PARAMETER =
		"public-parameter";
	
	private static final String RESULT = "result";
	
	public void processAction(ActionRequest request, ActionResponse response)
		throws IOException, PortletException {
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		String[] result = request.getParameterValues(PUBLIC_PARAMETER);
		String[] expected = {"foobar", TEST_NAME};
		
		ListCompare listCompare = new ListCompare(expected,
												result,
												null,
												ListCompare.ALL_ELEMENTS_AND_ORDER_MATCH);
		
		if (listCompare.misMatch()) {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail(listCompare.getMisMatchReason());
			
		} else
			resultWriter.setStatus(ResultWriter.PASS);
		
		request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
		
	}
	
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
			String portletURLstr = portletUrl.toString();
			urlTag.setTagContent(portletURLstr);
			out.println(urlTag.toString());
		} else if (reqCount.getRequestNumber() == 1){
			PortletURL portletUrl = response.createActionURL();
			portletUrl.setParameter(PUBLIC_PARAMETER, "foobar");
			PortletURLTag urlTag = new PortletURLTag();
			String portletURLstr = portletUrl.toString();
			urlTag.setTagContent(portletURLstr);
			out.println(urlTag.toString());	
		}
		else{
			out.println(request.getPortletSession(true).getAttribute(RESULT));
		}
	}
}
