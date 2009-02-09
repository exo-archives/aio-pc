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
import javax.xml.namespace.QName;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This portlet verify that the parameters set on ActionResponse
 * and received in EventRequest will not propagated to render().
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */

public class CheckEventRequestParameterRenderTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"CheckEventRequestParameterRenderTest";
	
	private static final String key = "LANGUAGES";
	private static final String value = "Java";
	
	protected final QName qname = new QName("http://acme.com/events",
			TEST_NAME + "_String");
	
	public void render(RenderRequest request, RenderResponse response )
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		RequestCount requestCount = 
			new RequestCount(request, 
					response, 
					RequestCount.MANAGED_VIA_SESSION);
		
		if (requestCount.isFirstRequest()) {
	
			PortletURLTag urlTag = new PortletURLTag();
			PortletURL actionURL = response.createActionURL();
			actionURL.setParameter(key, value);
			urlTag.setTagContent(actionURL.toString());
			out.println(urlTag.toString());
	
		} else {
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			
			String actualValue = request.getParameter(key);
			
			if (actualValue == null) {
				resultWriter.setStatus(ResultWriter.PASS);
			} else {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("RenderRequest.getParameter("
							+ key + ") returned an incorrect result" );
				resultWriter.addDetail("Expected result = null");
				resultWriter.addDetail("Actual result = " + actualValue);

			}
            out.println(resultWriter.toString());
		}
	}
	
	public void processAction(ActionRequest request, ActionResponse response)
		throws PortletException, IOException {
		
		response.setEvent(qname, TEST_NAME);
	}
	
	public void processEvent(EventRequest request, EventResponse response) 
		throws PortletException, IOException {
	}
}
