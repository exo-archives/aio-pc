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
import javax.xml.namespace.QName;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class CheckEventNameInDDTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME = 
		"CheckEventNameInDDTest";
	
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
		QName name = evt.getQName();
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		if (qname.toString().equals(name.toString()))
			resultWriter.setStatus(ResultWriter.PASS);
		else {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Wrong event name submitted.");
			resultWriter.addDetail("Expected name: '" + qname.toString() + "'");
			resultWriter.addDetail("Received name: '" + name.toString() + "'");
		}
		
		request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
	}
}
