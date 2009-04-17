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
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the WindowStateException thrown by the
 * setWindowState() method when setting a window state that was not
 * declared in the deployment descriptor.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * 
 */

public class SetUndeclaredWindowStateTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"SetUndeclaredWindowStateTest";

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
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			
			try {
				
				response.setWindowState(new WindowState("INVALID"));
				
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("WindowStateException was not thrown "
                                 	+ "when setting a window state that "
                                 	+ "was not declared in the deployment "
                                 	+ "descriptor.");
				
			} catch (WindowStateException e) {
				
				resultWriter.setStatus(ResultWriter.PASS);
				
			} finally {
				
				request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
				
			}
		}
	}
}
