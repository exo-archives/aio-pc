/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.xml.namespace.QName;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class tests that all operation maked on EventResponse
 * are ignored if processEvent throws a PortletException.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class CheckOpIgnoredTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"CheckOpIgnoredTest";
	
	private final QName qname =
		new QName("http://acme.com/events", TEST_NAME + "_String");
	
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		response.setEvent(qname, TEST_NAME);
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
			
			/*
			 * create an actionURL in which we set our event
			 */
			PortletURLTag urlTag = new PortletURLTag();
			PortletURL actionURL = response.createActionURL();
			urlTag.setTagContent(actionURL.toString());
			out.println(urlTag.toString());
	
		}
		else {
						
			ResultWriter writer = new ResultWriter(TEST_NAME);
			
			String param = request.getParameter(TEST_NAME);
			String property = request.getProperty(TEST_NAME);
			
			if (request.getPortletMode().equals(PortletMode.EDIT)
				|| request.getWindowState().equals(WindowState.MINIMIZED)
				|| param != null
				|| property != null) {
				
				writer.setStatus(ResultWriter.FAIL);
				writer.addDetail("Some operations set on EventResponse are not ignored!");
				writer.addDetail("PortletMode expected: PortletMode.VIEW");
				writer.addDetail("PortletMode actual  : " + request.getPortletMode());
				writer.addDetail("WindowState expected: WindowState.Normal");
				writer.addDetail("WindowState actual  : " + request.getWindowState());
				writer.addDetail("Parameter for key " + TEST_NAME + " expected: null");
				writer.addDetail("Parameter for key " + TEST_NAME + " actual  : " + param);
				writer.addDetail("Property for key " + TEST_NAME + " expected: null");
				writer.addDetail("Property for key " + TEST_NAME + " actual  : " + property);
				
			} else
				writer.setStatus(ResultWriter.PASS);
			
			out.println(writer.toString());
		}
	}
	
	public void processEvent(EventRequest request, EventResponse response) 
			throws PortletException, IOException {
		
		response.setRenderParameter(TEST_NAME, TEST_NAME);
		response.setProperty(TEST_NAME, TEST_NAME);
		response.setPortletMode(PortletMode.EDIT);
		response.setWindowState(WindowState.MINIMIZED);
		
		throw new PortletException();
	}
}
