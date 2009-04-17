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
 * This class test that the processEvent method finished
 * before the render method is started.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class CheckFinishedBeforRenderTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"CheckFinishedBeforRenderTest";

	@Override
	protected String getTestName() {
		return TEST_NAME;
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		response.setEvent(qname, TEST_NAME);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		
		Event evt = request.getEvent();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		if (evt.getQName().getLocalPart().equals(qname.getLocalPart())) {
			resultWriter.setStatus(ResultWriter.PASS);
			
		} else {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Event name is wrong.");
			resultWriter.addDetail("expected name " + qname.toString());			
		}
		
		request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
		
	}
}
