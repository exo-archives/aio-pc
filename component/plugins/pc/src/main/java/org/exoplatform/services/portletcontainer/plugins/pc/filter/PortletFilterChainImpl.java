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
import java.util.Collection;
import java.util.Iterator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;

import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 17 nov. 2003
 * Time: 21:21:35
 */
public class PortletFilterChainImpl implements FilterChain {

  private Collection filters;
	private Iterator iterator;
	private Portlet portlet;

	public PortletFilterChainImpl(Collection filters, Portlet p) {
	  this.filters = filters;
		this.portlet = p;
	}

	public Iterator getFiltersIterator() {
		return filters.iterator();
	}

	public void restart() {
		this.iterator = filters.iterator();
	}

  public void doFilter(ActionRequest request, ActionResponse response) throws IOException, PortletException {
    if (iterator.hasNext()) {
      ActionFilter portletFilter = (ActionFilter) iterator.next();
      portletFilter.doFilter(request, response, this);
    }
  }

  public void doFilter(EventRequest request, EventResponse response) throws IOException, PortletException {
    if (iterator.hasNext()) {
      EventFilter portletFilter = (EventFilter) iterator.next();
      portletFilter.doFilter(request, response, this);
    }
  }

  public void doFilter(RenderRequest request, RenderResponse response) throws IOException, PortletException {
    if (iterator.hasNext()) {
      RenderFilter portletFilter = (RenderFilter) iterator.next();
      portletFilter.doFilter(request, response, this);
    }
  }

  public void doFilter(ResourceRequest request, ResourceResponse response) throws IOException, PortletException {
    if (iterator.hasNext()) {
      ResourceFilter portletFilter = (ResourceFilter) iterator.next();
      portletFilter.doFilter(request, response, this);
    }
  }

}
