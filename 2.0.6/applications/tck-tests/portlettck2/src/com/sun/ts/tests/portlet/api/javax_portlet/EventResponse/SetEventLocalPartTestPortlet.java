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
 * This class tests setEvent method with only the local part
 * of an event.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class SetEventLocalPartTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"SetEventLocalPartTest";

	@Override
	protected String getTestName() {
		return TEST_NAME;
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		response.setEvent(qname.getLocalPart(), TEST_NAME);
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		
		Event evt = request.getEvent();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		if (evt.getQName().toString().equals(qname.toString())) {
			resultWriter.setStatus(ResultWriter.PASS);
		} else {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Wrong event name received.");
			resultWriter.addDetail("Expected name: " + qname.toString());
			resultWriter.addDetail("Actual name  : " + evt.getQName().toString());
		
		}
		
		request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
	}
}
