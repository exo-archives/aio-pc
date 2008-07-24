/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.namespace.QName;

import com.sun.ts.tests.portlet.common.util.MapCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class verify that a parameter is replaced from another
 * parameter with the same name.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class SetRenderParametersReplaceTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"SetRenderParametersReplaceTest";

	private final QName qname = new QName("http://acme.com/events",
			TEST_NAME + "_String");
	
	private static final String KEY = "Languages";
	
    static Map<String, String[]> map = new HashMap<String, String[]>(2);
    
    static {
    	map.put(KEY, new String[] {"XML", "JAVA"});
    	map.put("BestLanguage", new String[] {"C"});
    }
	
    @Override
	public void render(RenderRequest request, RenderResponse response )
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		RequestCount requestCount = 
			new RequestCount(request, 
					response, 
					RequestCount.MANAGED_VIA_SESSION);
		
		if (requestCount.isFirstRequest()) {
	
			PortletURLTag urlTag = new PortletURLTag();
			PortletURL actionURL = response.createActionURL();
			urlTag.setTagContent(actionURL.toString());
			out.println(urlTag.toString());
	
		} else {
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			
			Map<String, String[]> actualMap = request.getParameterMap();
			
			MapCompare mapCompare = new MapCompare(map, actualMap);
			
			if (mapCompare.misMatch()) {
				
				resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(mapCompare.getMisMatchReason());
				
			} else {
				resultWriter.setStatus(ResultWriter.PASS);
			}
			
            out.println(resultWriter.toString());
		}
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
		
		response.setRenderParameter(KEY, TEST_NAME);
		
		if (evt.getQName().getLocalPart().equals(qname.getLocalPart())) {
			
			for (String key : map.keySet())
				response.setRenderParameter(key, map.get(key));
		}
	}
}
