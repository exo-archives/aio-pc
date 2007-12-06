/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.plugins.pc.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.PortletFilter;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;

import org.exoplatform.services.portletcontainer.pci.model.Filter;

/**
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 */
public class PortletFilterWrapper implements ActionFilter, EventFilter, RenderFilter, ResourceFilter {

  private String filterName;
  private String filterClass;
  private List initParams;
  private PortletFilter filter;
  private List lifecycle;

  public PortletFilterWrapper(String filterName, String filterClass, List initParams, List lifecycle) {
    this.filterName = filterName;
    this.filterClass = filterClass;
    this.initParams = initParams;
    this.lifecycle = lifecycle;
    filter = null;
  }

  public String getFilterName() {
    return filterName;
  }

  public List getInitParam() {
    return initParams;
  }

  public void init(FilterConfig config) throws PortletException {
    if (filter == null) {
      try {
        filter = (PortletFilter) Thread.currentThread().getContextClassLoader().loadClass(filterClass).newInstance();
      } catch (Exception e) {
        throw new PortletException(e);
      }
      filter.init(config);
    }
  }

  public void doFilter(ActionRequest request, ActionResponse response, FilterChain chain) throws IOException, PortletException {
    if (isLifecycleSupported(request))
      ((ActionFilter) filter).doFilter(request, response, chain);
    else
    	chain.doFilter(request, response);
  }

  public void doFilter(EventRequest request, EventResponse response, FilterChain chain) throws IOException, PortletException {
    if (isLifecycleSupported(request))
      ((EventFilter) filter).doFilter(request, response, chain);
    else
    	chain.doFilter(request, response);
  }

  public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
    if (isLifecycleSupported(request))
      ((RenderFilter) filter).doFilter(request, response, chain);
    else
    	chain.doFilter(request, response);
  }

  public void doFilter(ResourceRequest request, ResourceResponse response, FilterChain chain) throws IOException, PortletException {
    if (isLifecycleSupported(request))
      ((ResourceFilter) filter).doFilter(request, response, chain);
    else
    	chain.doFilter(request, response);
  }

  private boolean isLifecycleSupported(PortletRequest request) {
    Iterator cycles = lifecycle.iterator();
    while (cycles.hasNext()) {
      int cycle = ((Integer) cycles.next()).intValue();
      if ((cycle == Filter.ALL) ||
        ((request instanceof ActionRequest) && (cycle == Filter.ACTION)) ||
        ((request instanceof EventRequest) && (cycle == Filter.EVENT)) ||
        ((request instanceof RenderRequest) && (cycle == Filter.RENDER)) ||
        ((request instanceof ResourceRequest) && (cycle == Filter.RESOURCE))
      ) return true;
    }
    return false;
  }

  public void destroy() {
    if (filter != null) {
      filter.destroy();
      filter = null;
    }
  }

}
