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

import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public abstract class GeneralEventing extends GenericPortlet {

	protected static final String RESULT = "result";
	protected static final String GLOBAL_PART = "http://acme.com/events";
	protected final QName qname = new QName(GLOBAL_PART,
											getTestName() + "_String");
	
	protected final QName qnameAddress = new QName(GLOBAL_PART,
											getTestName() + "_Address");
	
	protected Address getAddress() {
		Address address = new Address();
		address.setCity("Rio de Janeiro");
		address.setStreet("Av. Atlantica");
		
		return address;
	}
	
	public abstract void processEvent(EventRequest request, EventResponse response) 
		throws PortletException, IOException;
	
	public abstract void processAction(ActionRequest request, ActionResponse response)
		throws PortletException, IOException;
	
	protected abstract String getTestName();
	
	protected String createActionURL(RenderResponse response) {
		PortletURL actionURL = response.createActionURL();
		return actionURL.toString();
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
			out.println(request.getPortletSession(true).getAttribute(RESULT));
	}
}
