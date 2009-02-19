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
import java.util.HashMap;

import com.sun.ts.tests.portlet.common.util.MapCompare;

/**
 * Test for EventRequest.getParameterMap() method.
 * First request to the portlet writes a
 * actionURL to the output stream.
 * The portlet URL string is extracted and used for second
 * request. In the second request, few parameters set on
 * ActionResponse and trigger an event. 
 * The portlet uses EventRequest.getParameterMap()
 * to see if the expected parameter map is returned and
 * unmodifiable.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */


import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetParameterMapTestPortlet extends GeneralEventing {

    static public final String TEST_NAME = 
    	"GetParameterMapTest";
    
    static Map<String, String[]> map = new HashMap<String, String[]>(2);
    
    static {
    	map.put("LANGUAGES", new String[] {"XML", "JAVA"});
    	map.put("BestLanguage", new String[] {"C"});
    }
    
    @Override
    protected String getTestName() {
    	return TEST_NAME;
    }
    
    @Override
	public void processAction(ActionRequest request, ActionResponse response)
		throws PortletException, IOException {
		
    	response.setRenderParameters(map);
    	response.setEvent(qname, TEST_NAME);
	}
	
    @Override
	public void processEvent(EventRequest request, EventResponse response) 
		throws PortletException, IOException {
		
    	Event evt = request.getEvent();
    	
    	if (evt.getQName().getLocalPart().equals(qname.getLocalPart())) {
    		
    		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
    		Map<String, String[]> actual = request.getParameterMap();
    		
        	if (actual != null) {
        		
        		try {
        			actual.put(TEST_NAME, new String[] {TEST_NAME});
        		} catch (Exception e) {}
        		
        		MapCompare compare = new MapCompare(map, actual);
        		
        		if (!compare.misMatch())
        			resultWriter.setStatus(ResultWriter.PASS);
        		else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail(compare.getMisMatchReason());   			
        		}
        	} else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( "EventRequest.getParameterMap() "
                                        + " returned an empty map." );    		
        	}   		
    		
        	request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
    	}
	}
}
