/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventRequest;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class verify that an event has always a name.
 * 
 * @author Fred Thiele <ferdy@inf.uni-jena.de>
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */

public class GetEventNameTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"GetEventNameTest";
	
	public void processAction(ActionRequest request, ActionResponse response)
		throws PortletException, IOException {
		
		response.setEvent(qname, null);
	}
	
	public void processEvent(EventRequest request, EventResponse response)
		throws PortletException, IOException {		
		
		Event evt = request.getEvent();
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		if (evt.getQName().getLocalPart().equals(qname.getLocalPart())) {
			resultWriter.setStatus(ResultWriter.PASS);
		}
		else {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Event name is wrong.");
			resultWriter.addDetail("expected name " + qname.toString());
		}
		
		request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
	}
	
	protected String getTestName() {
		return TEST_NAME;
	}
}