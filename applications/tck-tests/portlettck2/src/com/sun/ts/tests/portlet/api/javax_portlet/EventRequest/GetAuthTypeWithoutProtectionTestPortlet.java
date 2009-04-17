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
 * A Negative Test for getAuthType method.
 *
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */

public class GetAuthTypeWithoutProtectionTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"GetAuthTypeWithoutProtectionTest";

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
	        
	        String result = request.getAuthType();

	        if(result == null )
	            resultWriter.setStatus(ResultWriter.PASS);
	        else {
	        	
	            resultWriter.setStatus(ResultWriter.FAIL);
	            resultWriter.addDetail( 
	                "PortletRequest.getAuthType() returned a non-null result, "
	                + " even though Portlet is not protected" );
	            resultWriter.addDetail( "     Actual result = |" + result + "|" );
	            
	        }

	        request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());	
			
		}
	}
}
