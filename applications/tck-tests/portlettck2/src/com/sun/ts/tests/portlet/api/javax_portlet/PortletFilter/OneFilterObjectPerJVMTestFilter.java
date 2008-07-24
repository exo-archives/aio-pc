/**
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletFilter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class OneFilterObjectPerJVMTestFilter implements RenderFilter{
	public static final String TEST_NAME = "OneFilterObjectPerJVMTest";
    private static int onePerJVMCounter = 0;
    private int onePerFilterCounter = 0;
    
	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
		RequestCount requestCount
        = new RequestCount(request, response,
                           RequestCount.MANAGED_VIA_SESSION);

		if (requestCount.isFirstRequest()){
			response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        PortletURLTag urlTag = new PortletURLTag();
	        urlTag.setTagContent(response.createRenderURL().toString());        
	        out.println(urlTag.toString());
		}
	    if (!requestCount.isFirstRequest()) {
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
	
	        if (++onePerJVMCounter == ++onePerFilterCounter) {
	            resultWriter.setStatus(ResultWriter.PASS);
	        } else {
	            resultWriter.setStatus(ResultWriter.FAIL);
	            resultWriter.addDetail("The two counter values are different:");
	            resultWriter.addDetail("onePerJVMCounter = " + onePerJVMCounter);
	            resultWriter.addDetail("onePerFilterCounter = " + onePerFilterCounter);
	        }
	
	        out.println(resultWriter.toString());
	    }
	    chain.doFilter(request, response);
		
	}

	public void destroy() {
		
	}

	public void init(FilterConfig arg0) throws PortletException {
		
		
	}
}
