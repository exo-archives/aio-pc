/*
 * Copyright 2007 IBM Corporation.
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

import com.sun.ts.tests.portlet.common.util.ParameterCheck;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;


/**
 * This class tests whether the resourceURL tag preserves the current render parameters.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public class ResourceURLPreservesRenderParametersTestPortlet extends GenericPortlet {
	
	private final String TEST_NAME = "ResourceURLPreservesRenderParametersTest";
	
	private final String SERVLET_NAME = "ResourceURLPreservesRenderParametersTestServlet";

	
	@Override
	public void render(RenderRequest request, RenderResponse response)
    	throws PortletException, IOException {
		
	    Map<String,String[]> parameterMap = new HashMap<String,String[]>(2);
	    parameterMap.put(TEST_NAME, new String[] {"One", "Two","Three"});
		
        response.setContentType("text/html");
        
        RequestCount requestCount = 
        	new RequestCount(request, response,
                           RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
        	PrintWriter out = response.getWriter();
        	
        	PortletURLTag renderTag = new PortletURLTag();
            PortletURL renderUrl = response.createRenderURL();
            renderUrl.setParameters(parameterMap);
            renderTag.setTagContent(renderUrl.toString());
            out.println(renderTag.toString());        	
        }
        else{
            PrintWriter out = response.getWriter();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        	PortletRequestDispatcher dispatcher
        		= getPortletContext().getNamedDispatcher(SERVLET_NAME);
        
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
        
        String[] result=request.getParameterValues(TEST_NAME);
        ParameterCheck check = new ParameterCheck(TEST_NAME, resultWriter);
        check.checkParameter(result);
    	
    	out.println(resultWriter.toString());
	}
}

