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
import java.util.Map;


/**
 * Test for EventRequest.getParameterMap() method.
 * First request to the portlet writes a
 * actionURL to the output stream.
 * The portlet URL string is extracted and used for second
 * request. In the second request, no parameters set on
 * ActionResponse and trigger an event. 
 * The portlet uses EventRequest.getParameterMap()
 * to see if the empty enumeration is returned.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */


import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetParameterMapEmptyTestPortlet extends GeneralEventing {

    static public final String TEST_NAME = 
    	"GetParameterMapEmptyTest";
    
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

            Map map = request.getParameterMap();
            
            if (map != null && map.size() == 0) {
                resultWriter.setStatus(ResultWriter.PASS);
            }
            else {
                resultWriter.setStatus(ResultWriter.FAIL);
                
                if ( map == null) {
                    resultWriter.addDetail("Expected an empty map.");
                    resultWriter.addDetail("Found null returned.");
                }
                else {
                    resultWriter.addDetail("Expected an empty map.");
                    resultWriter.addDetail(
                        "Found a map with size greater than 0.");
                }
            } 		
    		
        	request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
    	}
	}
}
