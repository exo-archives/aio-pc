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
import javax.xml.XMLConstants;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class test container used the XMLConstants.NULL_NS_URI
 * namespace if no default namespace provided.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class SetEventLocalPartUseXmlDefaultTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"SetEventLocalPartUseXmlDefaultTest";
	
	private static final String LOCAL_PART = TEST_NAME + "_String";
	
	@Override
	protected String getTestName() {
		return TEST_NAME;
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		response.setEvent(LOCAL_PART, TEST_NAME);
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		
		Event evt = request.getEvent();
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		if (evt.getName().equals(LOCAL_PART) 
				&& evt.getQName().getNamespaceURI()
						.equals(XMLConstants.NULL_NS_URI)) {
			
			resultWriter.setStatus(ResultWriter.PASS);
			
		} else {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Wrong event name global part received.");
			resultWriter.addDetail("Expected global part name: " + XMLConstants.NULL_NS_URI);
			resultWriter.addDetail("Actual global part name  : " + evt.getQName().getNamespaceURI());
			
		}
		
		request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
	}
}
