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
import com.sun.ts.tests.portlet.common.util.ListCompare;



/**
* First request to the portlet writes a
* action portletURL to the output stream.
* The portlet URL string is extracted and used for second
* request. In the second request, processAction() will set
* a parameter with multiply values in the response.
* In processEvent uses EventRequest.getParameterValues()
* to see if the expected parameter values are returned.
* 
* @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
* @since 2.0
*/



public class GetParameterValuesTestPortlet extends GeneralEventing {

	public static final String TEST_NAME = 
		"GetParameterValuesTest";
	
	protected static final String key = "LANGUAGES";
	protected static final String[] values = new String[] {"XML", "JAVA"};
	
	public String getTestName() {
		return TEST_NAME;
	}
	
	public void processAction(ActionRequest request, ActionResponse response)
		throws PortletException, IOException {
	
		response.setRenderParameter(key, values);
		response.setEvent(qname, TEST_NAME);
	}
	
	public void processEvent(EventRequest request, EventResponse response) 
		throws PortletException, IOException {
		
		Event evt = request.getEvent();
		
		if (evt.getQName().getLocalPart().equals(qname.getLocalPart())) {
		
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
	
	        String[] paramValues = request.getParameterValues(key);
	        String firstExpectedValue = request.getParameter(key);
	
	        ListCompare compare = new ListCompare(
	                                    values, 
	                                    paramValues,
	                                    firstExpectedValue,
	                                    ListCompare.ALL_ELEMENTS_MATCH);
	        if (compare.misMatch()) {
	            resultWriter.setStatus(ResultWriter.FAIL);
	            resultWriter.addDetail("EventRequest.getParameterValues("
	            		+ key + ") returned unexpected results.");
	            resultWriter.addDetail(compare.getMisMatchReason());
	        }
	        else {
	            resultWriter.setStatus(ResultWriter.PASS);
	        }
	        
	        request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
		}
	}
}
