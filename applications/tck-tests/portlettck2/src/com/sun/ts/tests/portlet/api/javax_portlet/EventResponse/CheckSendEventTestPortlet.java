/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventResponse;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the sending of events in the processAction and
 * processEvent methods.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class CheckSendEventTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"CheckSendEventTest";

	@Override
	protected String getTestName() {
		return TEST_NAME;
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		response.setEvent(qname, TEST_NAME);
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		
		Event evt = request.getEvent();
		
		if (evt.getName().equals(qname.getLocalPart())) {
			
			response.setEvent(qnameAddress, getAddress());
			
		} else if (evt.getName().equals(qnameAddress.getLocalPart())) {
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			resultWriter.setStatus(ResultWriter.PASS);
			
			request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
			
		}
	}
}
