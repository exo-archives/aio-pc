package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;
import javax.xml.namespace.QName;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class ProcessEventUnavailableExceptionTestPortlet extends GenericPortlet {
	public static final String TEST_NAME = "ProcessEventUnavailableExceptionTest";
	
	@Override
	public void processEvent(EventRequest request, EventResponse response) throws PortletException, IOException {
		Event event = request.getEvent();
		if (event.getQName().toString().equals("{http://www.event.de}ProcessEventUnavailableException")) {
			throw new UnavailableException(TEST_NAME);
		}
	}

	
	@Override
	public void processAction(ActionRequest request, ActionResponse response)
    	throws PortletException, IOException {
		//set Event
		//setEvent
		QName qname = new QName("http://www.event.de", "ProcessEventUnavailableException");
		response.setEvent(qname, "dummy");
	}
	
	@Override
	public void render(RenderRequest request, RenderResponse response)
		throws PortletException, IOException {
	
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	
	    RequestCount requestCount
	        = new RequestCount(request, response,
	                           RequestCount.MANAGED_VIA_SESSION);
	
	    if (requestCount.isFirstRequest()) {
	        PortletURL url = response.createActionURL();
	        PortletURLTag urlTag = new PortletURLTag();
	        urlTag.setTagContent(url.toString());        
	        out.println(urlTag.toString());
	    } else {
	        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
	        resultWriter.setStatus(ResultWriter.FAIL);
	
	        resultWriter.addDetail("render() is called after processAction()"
	                               + " throws UnavailableException.");
	
	        out.println(resultWriter.toString());            
	    }
	}
}
