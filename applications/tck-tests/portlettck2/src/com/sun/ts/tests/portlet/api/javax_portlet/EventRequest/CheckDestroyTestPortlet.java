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
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;
import javax.xml.namespace.QName;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class CheckDestroyTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"CheckDestroyTest";
	
	private boolean destroyed = false;
	
	private final QName qname =
		new QName("http://acme.com/events", TEST_NAME + "_String");
	
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {

		response.setEvent(qname, TEST_NAME);
	}
	
	public void destroy() {
		destroyed = true;
	}
	
	public void render(RenderRequest request, RenderResponse response )
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
	
		} else {
			ResultWriter writer = new ResultWriter(TEST_NAME);
			
			if (destroyed)
				writer.setStatus(ResultWriter.PASS);
			else {
				writer.setStatus(ResultWriter.FAIL);
				writer.addDetail("destroy() method not called.");
			}
			
			out.println(writer.toString());
		}
	}
	
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {

		throw new UnavailableException(TEST_NAME);
	}
}
