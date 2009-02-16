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
import javax.portlet.filter.RenderRequestWrapper;
import javax.portlet.filter.RenderResponseWrapper;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class WrapperTestFilter implements RenderFilter {
	public static final String TEST_NAME = "WrapperTest";
	
	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		RenderResponseWrapper resWrapper = new RenderResponseWrapper(response);
		RenderRequestWrapper reqWrapper = new RenderRequestWrapper(request);
		if (resWrapper==null || reqWrapper==null){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("RenderResponseWrapper or RenderRequestWrapper is null, this isn't allowed.");
		}
		chain.doFilter(request, resWrapper);
		
        resultWriter.setStatus(ResultWriter.PASS);
        out.println(resultWriter.toString());
	}

	public void destroy() {
		
		
	}

	public void init(FilterConfig arg0) throws PortletException {
		
		
	}

}
