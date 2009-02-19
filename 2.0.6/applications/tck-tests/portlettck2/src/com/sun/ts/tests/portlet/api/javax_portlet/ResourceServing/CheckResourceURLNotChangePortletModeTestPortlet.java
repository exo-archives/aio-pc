/*
 * Copyright 2006 IBM Corporation
 */

/**
 * @author Kay Schieck <schieck@inf.uni-jena.de>
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceServing;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class CheckResourceURLNotChangePortletModeTestPortlet extends GenericPortlet {
	public static String TEST_NAME = "CheckResourceURLNotChangePortletModeTest";
	public static String RENDER_PARAMETER_NAME = "renderParameterName";
	public static String RENDER_PARAMETER_VALUE_FIRST ="renderParameterValueFirst";
	public static String RENDER_PARAMETER_VALUE_SECOND ="renderParameterValueSecond";
	
	public void render(RenderRequest request, RenderResponse response )
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		RequestCount requestCount
	        = new RequestCount(request, response,
	                           RequestCount.MANAGED_VIA_SESSION);

	    if (requestCount.isFirstRequest()) {
	        PortletURL url = response.createRenderURL();
	        url.setParameter(RENDER_PARAMETER_NAME, RENDER_PARAMETER_VALUE_FIRST);
	        PortletURLTag urlTag = new PortletURLTag();
	        urlTag.setTagContent(url.toString());        
	        out.println(urlTag.toString());
	    }
	    else{
	    	PortletURLTag urlTag = new PortletURLTag();
	    	ResourceURL resourceURL = response.createResourceURL();
			
			resourceURL.setParameter(RENDER_PARAMETER_NAME, RENDER_PARAMETER_VALUE_SECOND);
			urlTag.setTagContent(resourceURL.toString());
			
			out.println(urlTag.toString());
	    }
	}

	public void serveResource(ResourceRequest request, ResourceResponse response) 
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out =  response.getWriter();
		
		ResultWriter writer = new ResultWriter(TEST_NAME);
		
		String[] parameter = request.getParameterValues(RENDER_PARAMETER_NAME);
		
		if (parameter == null) {
			writer.setStatus(ResultWriter.FAIL);
			writer.addDetail("No Parameter set vor key: " + TEST_NAME);
			writer.addDetail("Expected value: " + TEST_NAME);
		}
		else if (parameter.length!=2){
			writer.setStatus(ResultWriter.FAIL);
			writer.addDetail("Wrong parameter length: " + parameter.length);
			writer.addDetail("Expected value: 2");
		}
		else if (request.getPortletMode() != PortletMode.VIEW
				|| request.getWindowState() != WindowState.NORMAL
				|| parameter[0] == null
				|| parameter[1] == null ) {
			
			writer.setStatus(ResultWriter.FAIL);
			writer.addDetail("Wrong PortletMode or WindowState or Parameter set.");
		} else{
			if (parameter[0].equals(RENDER_PARAMETER_VALUE_SECOND) && parameter[1].equals(RENDER_PARAMETER_VALUE_FIRST))
				writer.setStatus(ResultWriter.PASS);
			else{
				writer.setStatus(ResultWriter.FAIL);
				writer.addDetail("Wrong Parameter set.");
			}
		}
		out.println(writer.toString());
	}
}
