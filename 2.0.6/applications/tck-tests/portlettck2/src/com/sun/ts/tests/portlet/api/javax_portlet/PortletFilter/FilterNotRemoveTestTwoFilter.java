/**
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletFilter;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

public class FilterNotRemoveTestTwoFilter implements RenderFilter{
	public static final String TEST_NAME = "FilterNotRemoveTest";
	
	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
		PortletSession session = request.getPortletSession();
		session.setAttribute("FilterNotRemoveTestTwoFilter", TEST_NAME);
		chain.doFilter(request, response);
	}

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws PortletException {
	}
}
