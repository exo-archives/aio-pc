/*
 * Copyright 2006 IBM Corporation.
 */

package com.sun.ts.tests.portlet.ee.taglib;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.sun.ts.tests.portlet.common.util.MapCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;


/**
 * This class tests that if Parameters are added by including the param tag between 
 * the resourceURL start and end tags and if such a parameter has the same name 
 * as a render parameter in this URL, the render parameter value must be the last 
 * value in the attribute value array.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public class ResourceURLAppendParameterTestPortlet extends GenericPortlet {
	
	private final String TEST_NAME = "ResourceURLAppendParameterTest";
	
	private final String SERVLET_NAME = "ResourceURLAppendParameterTestServlet";

	@Override
	public void render(RenderRequest request, RenderResponse response)
    	throws PortletException, IOException {
		
		// parameter map
	    Map<String,String[]> parameterMap = new HashMap<String,String[]>(1);
	    parameterMap.put(TEST_NAME, new String[] {"Two", "Three"});
		
        response.setContentType("text/html");
        
        RequestCount requestCount = 
        	new RequestCount(request, response,
                           RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {//create rende PortletURL with parameters
        	PrintWriter out = response.getWriter();
        	
        	PortletURLTag renderTag = new PortletURLTag();
            PortletURL renderUrl = response.createRenderURL();
            renderUrl.setParameters(parameterMap);
            renderTag.setTagContent(renderUrl.toString());
            out.println(renderTag.toString());        	
        }
        else{//include servlet
            PrintWriter out = response.getWriter();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        	PortletRequestDispatcher dispatcher = 
        		getPortletContext().getNamedDispatcher(SERVLET_NAME);
        
        	if (dispatcher == null) {
        		resultWriter.setStatus(ResultWriter.FAIL);
        		resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
        					+ SERVLET_NAME);
        		out.println(resultWriter.toString());
        	} 
        	else {
        		dispatcher.include(request, response);
        	}        	
        }
	}
	
	
	@Override
    public void serveResource(ResourceRequest request, ResourceResponse response)
		throws PortletException, IOException {
    	
        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();
        
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.PASS);
        
        Map<String,String[]> expectedParameterMap = new HashMap<String,String[]>(1);
	    expectedParameterMap.put(TEST_NAME, new String[] {"One", "Two", "Three"});
	    
	    Map<String,String[]> parameterMap = request.getParameterMap();
	    
        MapCompare mapCompare = new MapCompare(expectedParameterMap, parameterMap);
        
        if(mapCompare.misMatch()){
        	resultWriter.setStatus(ResultWriter.FAIL);
        	resultWriter.addDetail(mapCompare.getMisMatchReason());
        }
    	
    	out.println(resultWriter.toString());
	}
}

