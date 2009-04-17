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
import javax.portlet.PortletRequest;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class verify that the PortletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE)
 * method returned the correct value.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetLifecyclePhaseAttributeTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"GetLifecyclePhaseAttributeTest";

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
			
			String phase = (String)request.getAttribute(PortletRequest.LIFECYCLE_PHASE);
			
			if (phase != null && phase.equals(PortletRequest.EVENT_PHASE))
				resultWriter.setStatus(ResultWriter.PASS);
			else {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				
				resultWriter.addDetail("PortletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE)");
				resultWriter.addDetail("not returned PortletRequest.EVENT_PHASE");
				
			}
			
			request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());		
		}
	}
}
