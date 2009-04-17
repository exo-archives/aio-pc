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
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the PortletModeException thrown by the
 * setPortletMode() method when setting a portlet mode that was not
 * declared in the deployment descriptor.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * 
 */

public class SetUndeclaredPortletModeTestPortlet extends GeneralEventing {

	public static final String TEST_NAME =
		"SetUndeclaredPortletModeTest";
	
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
	        	response.setPortletMode(PortletMode.EDIT);
	        	
	        	resultWriter.setStatus(ResultWriter.FAIL);
	        	resultWriter.addDetail("PortletModeException was not thrown "
	                                 + "when setting a portlet mode that "
	                                 + "was not declared in the deployment "
	                                 + "descriptor.");
	        	
	        } catch (PortletModeException e) {
	        	
	        	resultWriter.setStatus(ResultWriter.PASS);
	        	
	        } finally {
	        	
	        	request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
	        	
	        }		
		}
	}
}
