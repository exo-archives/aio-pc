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

public class DestroyFilterTestFilter implements RenderFilter {
	public static final String TEST_NAME = "DestroyFilterTest";
	private boolean sawDestroy = false;
	
	public void destroy() {
		sawDestroy = true;
		
	}

	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    ResultWriter resultWriter = new ResultWriter(TEST_NAME);

	    if (sawDestroy) {
	        resultWriter.setStatus(ResultWriter.FAIL);
	        resultWriter.addDetail("Portlet filter life cycle is incorrect.");
	        resultWriter.addDetail("destroy() is called before doFilter().");
	    } else {
	        resultWriter.setStatus(ResultWriter.PASS);
	    }

	    out.println(resultWriter.toString());
	    chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws PortletException {
		
	}

	
}
