/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventRequest;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;

import java.io.IOException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * Test for EventRequest.getParameterValues() method.
 * First request to the portlet writes a
 * action portletURL to the output stream.
 * The portlet URL string is extracted and used for second
 * request. In the second request, processAction() will set
 * a parameter with one value in the response. The portlet uses
 * EventRequest.getParameterValues() to check the returned 
 * array.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since 2.0
 */

public class CheckSingleParameterValuesTestPortlet extends GeneralEventing {

	public static final String TEST_NAME =
		"CheckSingleParameterValuesTest";
	
	static final String key = "LANGUAGES";
	static final String value = "Java";
	
	public String getTestName() {
		return TEST_NAME;
	}
	
	public void processAction(ActionRequest request, ActionResponse response)
		throws PortletException, IOException {

		response.setRenderParameter(key, value);
		response.setEvent(qname, TEST_NAME);
	}
	
	public void processEvent(EventRequest request, EventResponse response) 
		throws PortletException, IOException {
		
		Event evt = request.getEvent();
		
		if (evt.getQName().getLocalPart().equals(qname.getLocalPart())) {
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);

	        String[] paramValues = request.getParameterValues(key);

	        if ((paramValues != null) &&
	        	(paramValues.length == 1) &&
	            (paramValues[0] != null) &&
	            (paramValues[0].equals(value))) {
	                resultWriter.setStatus(ResultWriter.PASS);
	        } else {
	            resultWriter.setStatus(ResultWriter.FAIL);
	            resultWriter.addDetail("Expected a array of size one with: "
	            		+ value);
	        }
	        
	        request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
		}
	}
}
