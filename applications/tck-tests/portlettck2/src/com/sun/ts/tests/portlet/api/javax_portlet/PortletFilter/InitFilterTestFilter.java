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

public class InitFilterTestFilter implements RenderFilter {
	public static final String TEST_NAME = "InitFilterTest";
    private boolean sawInit = false;
	
	public void destroy() {
		
		
	}

	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        if (sawInit) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Portlet life cycle is incorrect.");
            resultWriter.addDetail("init() not called before render().");
        }
        out.println(resultWriter.toString());
        chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws PortletException {
		sawInit = true;
		
	}

}
