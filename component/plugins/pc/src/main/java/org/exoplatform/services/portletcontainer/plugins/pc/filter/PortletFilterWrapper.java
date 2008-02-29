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
import org.exoplatform.services.portletcontainer.pci.model.InitParam;

/**
 * Created by The eXo Platform SAS.
 * Author : Roman Pedchenko roman.pedchenko@exoplatform.com.ua
 */
public class PortletFilterWrapper implements ActionFilter, EventFilter, RenderFilter, ResourceFilter {

  /**
   * Filter name.
   */
  private final String filterName;

  /**
   * Filter class.
   */
  private final String filterClass;

  /**
   * Init params.
   */
  private final List<InitParam> initParams;

  /**
   * Filter.
   */
  private PortletFilter filter;

  /**
   * Lifecycle.
   */
  private final List<Integer> lifecycle;

  /**
   * @param filterName filter name
   * @param filterClass filter class
   * @param initParams init params
   * @param lifecycle lifecycle
   */
  public PortletFilterWrapper(final String filterName,
      final String filterClass,
      final List<InitParam> initParams,
      final List<Integer> lifecycle) {
    this.filterName = filterName;
    this.filterClass = filterClass;
    this.initParams = initParams;
    this.lifecycle = lifecycle;
    filter = null;
  }

  /**
   * @return filter name
   */
  public final String getFilterName() {
    return filterName;
  }

  /**
   * @return init params
   */
  public final List<InitParam> getInitParam() {
    return initParams;
  }

  /**
   * Overridden method.
   *
   * @param config config
   * @throws PortletException exception
   * @see javax.portlet.filter.PortletFilter#init(javax.portlet.filter.FilterConfig)
   */
  public final void init(final FilterConfig config) throws PortletException {
    if (filter == null) {
      try {
        filter = (PortletFilter) Thread.currentThread().getContextClassLoader().loadClass(
            filterClass).newInstance();
      } catch (Exception e) {
        throw new PortletException(e);
      }
      filter.init(config);
    }
  }

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param chain chain
   * @throws IOException exception
   * @throws PortletException exception
   * @see javax.portlet.filter.ActionFilter#doFilter(javax.portlet.ActionRequest, javax.portlet.ActionResponse, javax.portlet.filter.FilterChain)
   */
  public final void doFilter(final ActionRequest request,
      final ActionResponse response,
      final FilterChain chain) throws IOException, PortletException {
    if (isLifecycleSupported(request))
      ((ActionFilter) filter).doFilter(request, response, chain);
    else
      chain.doFilter(request, response);
  }

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param chain chain
   * @throws IOException exception
   * @throws PortletException exception
   * @see javax.portlet.filter.EventFilter#doFilter(javax.portlet.EventRequest, javax.portlet.EventResponse, javax.portlet.filter.FilterChain)
   */
  public final void doFilter(final EventRequest request,
      final EventResponse response,
      final FilterChain chain) throws IOException, PortletException {
    if (isLifecycleSupported(request))
      ((EventFilter) filter).doFilter(request, response, chain);
    else
      chain.doFilter(request, response);
  }

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param chain chain
   * @throws IOException exception
   * @throws PortletException exception
   * @see javax.portlet.filter.RenderFilter#doFilter(javax.portlet.RenderRequest, javax.portlet.RenderResponse, javax.portlet.filter.FilterChain)
   */
  public final void doFilter(final RenderRequest request,
      final RenderResponse response,
      final FilterChain chain) throws IOException, PortletException {
    if (isLifecycleSupported(request))
      ((RenderFilter) filter).doFilter(request, response, chain);
    else
      chain.doFilter(request, response);
  }

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @param chain chain
   * @throws IOException exception
   * @throws PortletException exception
   * @see javax.portlet.filter.ResourceFilter#doFilter(javax.portlet.ResourceRequest, javax.portlet.ResourceResponse, javax.portlet.filter.FilterChain)
   */
  public final void doFilter(final ResourceRequest request,
      final ResourceResponse response,
      final FilterChain chain) throws IOException, PortletException {
    if (isLifecycleSupported(request))
      ((ResourceFilter) filter).doFilter(request, response, chain);
    else
      chain.doFilter(request, response);
  }

  /**
   * @param request request
   * @return is lifecycle supported
   */
  private boolean isLifecycleSupported(final PortletRequest request) {
    Iterator cycles = lifecycle.iterator();
    while (cycles.hasNext()) {
      int cycle = ((Integer) cycles.next()).intValue();
      if ((cycle == Filter.ALL) || ((request instanceof ActionRequest) && (cycle == Filter.ACTION))
          || ((request instanceof EventRequest) && (cycle == Filter.EVENT))
          || ((request instanceof RenderRequest) && (cycle == Filter.RENDER))
          || ((request instanceof ResourceRequest) && (cycle == Filter.RESOURCE)))
        return true;
    }
    return false;
  }

  /**
   * Overridden method.
   *
   * @see javax.portlet.filter.PortletFilter#destroy()
   */
  public final void destroy() {
    if (filter != null) {
      filter.destroy();
      filter = null;
    }
  }

}
