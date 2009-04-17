/*
 * Copyright 2006 IBM Corporation
 */

/**
 * @author Kay Schieck <schieck@inf.uni-jena.de>
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceServing;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class CheckProcessActionNotCalledForResourceURLTestPortlet extends
		GenericPortlet {

	public static String TEST_NAME = "CheckProcessActionNotCalledForResourceURLTest";
	
	public void processAction(ActionRequest request, ActionResponse response )
		throws PortletException, IOException {
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		resultWriter.setStatus(ResultWriter.FAIL);
		resultWriter.addDetail("processAction() method called");
		
		request.getPortletSession(true).setAttribute("resultProcessActionCall", resultWriter.toString(), PortletSession.PORTLET_SCOPE);
	}
	public void processEvent(EventRequest request, EventResponse response )
		throws PortletException, IOException {
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		resultWriter.setStatus(ResultWriter.FAIL);
		resultWriter.addDetail("processAction() method called");
		
		request.getPortletSession(true).setAttribute("resultProcessActionCall", resultWriter.toString(), PortletSession.PORTLET_SCOPE);
	}
	
	public void render(RenderRequest request, RenderResponse response )
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		PortletURLTag urlTag = new PortletURLTag();
		ResourceURL resourceURL = response.createResourceURL();
		urlTag.setTagContent(resourceURL.toString());
		
		out.println(urlTag.toString());
	}
	
    public void serveResource(ResourceRequest request, ResourceResponse response)
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		String resultProcessAction = (String)request.getPortletSession(true).getAttribute("resultProcessActionCall", PortletSession.PORTLET_SCOPE);
		
		if (resultProcessAction != null)
			out.println(resultProcessAction);
		else {
		
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			resultWriter.setStatus(ResultWriter.PASS);
			
			out.println(resultWriter.toString());
		}
	}
}
