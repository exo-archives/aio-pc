/**
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletFilter;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;

public class InterfaceFilterTestFilter
	implements ActionFilter,EventFilter,RenderFilter,ResourceFilter {
	private static final String TEST_FILTER = "filter";
	private static final String RENDER_FILTER = "render_filter";
	private static final String ACTION_FILTER = "action_filter";
	private static final String EVENT_FILTER = "event_filter";
	private static final String RESOURCE_FILTER = "resource_filter";

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws PortletException {
	}
	
	public void doFilter(ActionRequest request, ActionResponse response, FilterChain chain) throws IOException, PortletException {
		PortletSession session = request.getPortletSession();
		if (session.getAttribute(TEST_FILTER)== null)
			session.setAttribute(TEST_FILTER, ACTION_FILTER);
		chain.doFilter(request, response);
	}

	public void doFilter(EventRequest request, EventResponse response, FilterChain chain) throws IOException, PortletException {
		PortletSession session = request.getPortletSession();
		if (session.getAttribute(TEST_FILTER)== null)
			session.setAttribute(TEST_FILTER, EVENT_FILTER);
		chain.doFilter(request, response);
	}

	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
		PortletSession session = request.getPortletSession();
		if (session.getAttribute(TEST_FILTER)== null)
			session.setAttribute(TEST_FILTER, RENDER_FILTER);
		chain.doFilter(request, response);
	}

	public void doFilter(ResourceRequest request, ResourceResponse response, FilterChain chain) throws IOException, PortletException {
		PortletSession session = request.getPortletSession();
		if (session.getAttribute(TEST_FILTER)== null)
			session.setAttribute(TEST_FILTER, RESOURCE_FILTER);
		chain.doFilter(request, response);
	}
}
