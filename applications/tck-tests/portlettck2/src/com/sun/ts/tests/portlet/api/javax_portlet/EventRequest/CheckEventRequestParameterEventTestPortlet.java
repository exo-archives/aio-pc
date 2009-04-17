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
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This portlet verify that the parameters set on actionURL
 * and received in processAction() will not propagated to processEvent().
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */

public class CheckEventRequestParameterEventTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"CheckEventRequestParameterEventTest";
	
	private static final String key = "LANGUAGES";
	private static final String value = "Java";

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
		
		if (evt.getQName().getLocalPart().equals(qname.getLocalPart())) {
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);

			String actualValue = request.getParameter(key);
			
			if (actualValue == null) {
				resultWriter.setStatus(ResultWriter.PASS);
			} else {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("EventRequest.getParameter("
							+ key + ") returned an incorrect result" );
				resultWriter.addDetail("Expected result = null");
				resultWriter.addDetail("Actual result = " + actualValue);

			}
	        
	        request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
		}

	}
	
	@Override
	protected String createActionURL(RenderResponse response) {
		
		PortletURL actionURL = response.createActionURL();
		actionURL.setParameter(key, value);
		
		return actionURL.toString();
	}

}
