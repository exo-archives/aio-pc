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
 * This class tests that the receiving event value is the
 * correct type.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class CheckEventPayloadTypeTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME = 
		"CheckEventPayloadTypeTest";
	
	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		response.setEvent(qnameAddress, getAddress());
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
		throws PortletException, IOException {
		
		Event evt = request.getEvent();
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		if (evt.getQName().getLocalPart().equals(qnameAddress.getLocalPart())) {
			
			Object value = evt.getValue();
			
			if (value == null) {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("event value is null");
				
			} else {
				
				if (!(value instanceof Address)) {
					
					resultWriter.setStatus(ResultWriter.FAIL);
					resultWriter.addDetail("event value is not of type " +
						"com.sun.ts.tests.portlet.api.javax_portlet.EventRequest.Address");
					
				} else
					resultWriter.setStatus(ResultWriter.PASS);
			}
		} else {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Event name was not set or is wrong.");
			
		}
		
		request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
	}

	@Override
	protected String getTestName() {
		return TEST_NAME;
	}
}
