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
package org.exoplatform.services.portletcontainer.plugins.pc.aop;

import java.util.Iterator;

import javax.portlet.PortletContext;

import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterChainImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterConfigImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterWrapper;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceResponseImp;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 * @author: Roman Pedchenko
 */
public class PortletFilterCommand extends BaseCommandUnit {

  protected Object render(RenderExecutionContext rcontext)  throws Throwable {
    log_.debug("--> render method, call portlet filter aspect");

    RenderRequestImp req = (RenderRequestImp) rcontext.request_;
    RenderResponseImp res = (RenderResponseImp) rcontext.response_;
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    PortletFilterChainImpl chain = (PortletFilterChainImpl) req.getPortletDatas().getFilterChain();
    for (Iterator iterator = chain.getFiltersIterator(); iterator.hasNext();) {
      PortletFilterWrapper portletFilter = (PortletFilterWrapper) iterator.next();
      portletFilter.init(new PortletFilterConfigImpl(portletFilter.getFilterName(), portletFilter.getInitParam(), portletContext));
    }
    chain.restart();
    chain.doFilter(req, res);
    return rcontext.executeNextUnit();
  }

  protected Object processAction(ActionExecutionContext acontext)  throws Throwable {
    log_.debug("--> processAction method, call portlet filter aspect");

    ActionRequestImp req = (ActionRequestImp) acontext.request_;
    ActionResponseImp res = (ActionResponseImp) acontext.response_;
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    PortletFilterChainImpl chain = (PortletFilterChainImpl) req.getPortletDatas().getFilterChain();
    for (Iterator iterator = chain.getFiltersIterator(); iterator.hasNext();) {
      PortletFilterWrapper portletFilter = (PortletFilterWrapper) iterator.next();
      portletFilter.init(new PortletFilterConfigImpl(portletFilter.getFilterName(), portletFilter.getInitParam(), portletContext));
    }
    chain.restart();
    chain.doFilter(req, res);
    return acontext.executeNextUnit();
  }

  protected Object serveResource(ResourceExecutionContext rcontext)  throws Throwable {
    log_.debug("--> serveResource method, call portlet filter aspect");
    ResourceRequestImp req = (ResourceRequestImp) rcontext.request_;
    ResourceResponseImp res = (ResourceResponseImp) rcontext.response_;
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    PortletFilterChainImpl chain = (PortletFilterChainImpl) req.getPortletDatas().getFilterChain();
    for (Iterator iterator = chain.getFiltersIterator(); iterator.hasNext();) {
      PortletFilterWrapper portletFilter = (PortletFilterWrapper) iterator.next();
      portletFilter.init(new PortletFilterConfigImpl(portletFilter.getFilterName(), portletFilter.getInitParam(), portletContext));
    }
    chain.restart();
    chain.doFilter(req, res);
    return rcontext.executeNextUnit();
  }

  protected Object processEvent(EventExecutionContext econtext)  throws Throwable {
    log_.debug("--> processEvent method, call portlet filter aspect");

    EventRequestImp req = (EventRequestImp) econtext.request_;
    EventResponseImp res = (EventResponseImp) econtext.response_;
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    PortletFilterChainImpl chain = (PortletFilterChainImpl) req.getPortletDatas().getFilterChain();
    for (Iterator iterator = chain.getFiltersIterator(); iterator.hasNext();) {
      PortletFilterWrapper portletFilter = (PortletFilterWrapper) iterator.next();
      portletFilter.init(new PortletFilterConfigImpl(portletFilter.getFilterName(), portletFilter.getInitParam(), portletContext));
    }
    chain.restart();
    chain.doFilter(req, res);
    return econtext.executeNextUnit();
  }

}
