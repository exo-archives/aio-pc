/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * Abstract portlet which generates a resourceURL.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

abstract public class LogicInServeResourcePortlet extends GenericPortlet {
	
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
	
	abstract public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException;

}
