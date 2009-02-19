package com.sun.ts.tests.portlet.Signature;

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

public class Filter implements ActionFilter, RenderFilter, ResourceFilter, EventFilter {

	public void doFilter(ActionRequest request, ActionResponse response, FilterChain chain) throws IOException, PortletException {
		chain.doFilter(request, response);
	}

	public void destroy() {
	}

	public void init(FilterConfig conf) throws PortletException {
	}

	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
		PortletSession portletSession = request.getPortletSession();
		int length = this.getClass().getInterfaces().length;
		for (int i = 0; i < length; i++){
			if (this.getClass().getInterfaces()[i].getName().equals("javax.portlet.filter.RenderFilter")){
				portletSession.setAttribute("RenderFilter", this.getClass().getInterfaces()[i]);
				portletSession.setAttribute("PortletFilter", this.getClass().getInterfaces()[i].getInterfaces()[0]);
			}
			else if (this.getClass().getInterfaces()[i].getName().equals("javax.portlet.filter.ActionFilter"))
				portletSession.setAttribute("ActionFilter", this.getClass().getInterfaces()[i]);
			else if (this.getClass().getInterfaces()[i].getName().equals("javax.portlet.filter.ResourceFilter"))
				portletSession.setAttribute("ResourceFilter", this.getClass().getInterfaces()[i]);
			else if (this.getClass().getInterfaces()[i].getName().equals("javax.portlet.filter.EventFilter"))
				portletSession.setAttribute("EventFilter", this.getClass().getInterfaces()[i]);
		}
		portletSession.setAttribute("FilterChain", chain.getClass());
		chain.doFilter(request, response);
	}

	public void doFilter(ResourceRequest request, ResourceResponse response, FilterChain chain) throws IOException, PortletException {
		chain.doFilter(request, response);
	}

	public void doFilter(EventRequest request, EventResponse response, FilterChain chain) throws IOException, PortletException {
		
	}
}
