/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventRequest;

import java.io.IOException;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * A Test for PortletRequest.getResponseContentTypes()
 * Checks that return value of this method is not null.
 * And the first item in the list is same as returned by
 * getResponseContentType().
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetResponseContentTypesTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"GetResponseContentTypesTest";

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
	        
			Enumeration responseTypes = request.getResponseContentTypes();

			if (responseTypes != null && responseTypes.hasMoreElements()) {
				
				String firstElement = (String)responseTypes.nextElement();
				
				if (firstElement.equals(request.getResponseContentType())) {
					
	              	resultWriter.setStatus(ResultWriter.PASS);
	              	
				} else {
					
	                resultWriter.setStatus(ResultWriter.FAIL);
	                resultWriter.addDetail( "First element of " +
	                    " PortletRequest.getResponseContentTypes() must be equal"+ 
	                    " to value returned by getResponseContentType()" );
	                resultWriter.addDetail( "First element returned is:" + 
	                    firstElement);
	                resultWriter.addDetail( "First element expected is:" + 
	                    request.getResponseContentType());
	                
	            }

	        } else {
	        	
	            resultWriter.setStatus(ResultWriter.FAIL);
	            resultWriter.addDetail( 
	                "PortletRequest.getResponseContentTypes() returned a "
	                + " null result " );
	            
	        }
			
	        request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());			
			
		}
	}
}
