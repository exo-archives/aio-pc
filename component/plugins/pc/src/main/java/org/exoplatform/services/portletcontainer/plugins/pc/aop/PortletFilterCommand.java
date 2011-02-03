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
import javax.portlet.filter.FilterConfig;

import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterChainImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterConfigImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterWrapper;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomResponseWrapper;

/**
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 * @author: Roman Pedchenko
 */
public class PortletFilterCommand extends BaseCommandUnit {

  /**
   * Overridden method.
   * 
   * @param rcontext context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#render(org.exoplatform.services.portletcontainer.plugins.pc.aop.RenderExecutionContext)
   */
  protected final Object render(final RenderExecutionContext rcontext) throws Throwable {
    log.debug("--> render method, call portlet filter aspect");

    RenderRequestImp req = (RenderRequestImp) rcontext.getRequest();
    RenderResponseImp res = (RenderResponseImp) rcontext.getResponse();
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    PortletFilterChainImpl chain = (PortletFilterChainImpl) req.getPortletDatas().getFilterChain();
    for (Iterator iterator = chain.getFiltersIterator(); iterator.hasNext();) {
      PortletFilterWrapper portletFilter = (PortletFilterWrapper) iterator.next();
      portletFilter.init(new PortletFilterConfigImpl(portletFilter.getFilterName(),
                                                     portletFilter.getInitParam(),
                                                     portletContext));
    }
    chain.restart(rcontext);
    chain.doFilter(req, res);
    if (chain.isGoodFinished()) {
      return chain.getResult();
    } else {
      return null;
    }
  }

  /**
   * Overridden method.
   * 
   * @param acontext context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#processAction(org.exoplatform.services.portletcontainer.plugins.pc.aop.ActionExecutionContext)
   */
  protected final Object processAction(final ActionExecutionContext acontext) throws Throwable {
    log.debug("--> processAction method, call portlet filter aspect");

    ActionRequestImp req = (ActionRequestImp) acontext.getRequest();
    ActionResponseImp res = (ActionResponseImp) acontext.getResponse();
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    PortletFilterChainImpl chain = (PortletFilterChainImpl) req.getPortletDatas().getFilterChain();
    for (Iterator iterator = chain.getFiltersIterator(); iterator.hasNext();) {
      PortletFilterWrapper portletFilter = (PortletFilterWrapper) iterator.next();
      portletFilter.init(new PortletFilterConfigImpl(portletFilter.getFilterName(),
                                                     portletFilter.getInitParam(),
                                                     portletContext));
    }
    chain.restart(acontext);
    chain.doFilter(req, res);
    if (chain.isGoodFinished())
    	return chain.getResult();
    else
      return null;
  }

  /**
   * Overridden method.
   * 
   * @param rcontext context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#serveResource(org.exoplatform.services.portletcontainer.plugins.pc.aop.ResourceExecutionContext)
   */
  protected final Object serveResource(final ResourceExecutionContext rcontext) throws Throwable {
    log.debug("--> serveResource method, call portlet filter aspect");
    ResourceRequestImp req = (ResourceRequestImp) rcontext.getRequest();
    ResourceResponseImp res = (ResourceResponseImp) rcontext.getResponse();
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    PortletFilterChainImpl chain = (PortletFilterChainImpl) req.getPortletDatas().getFilterChain();
    for (Iterator iterator = chain.getFiltersIterator(); iterator.hasNext();) {
      PortletFilterWrapper portletFilter = (PortletFilterWrapper) iterator.next();
      portletFilter.init(new PortletFilterConfigImpl(portletFilter.getFilterName(),
                                                     portletFilter.getInitParam(),
                                                     portletContext));
    }
    chain.restart(rcontext);
    chain.doFilter(req, res);
    if (chain.isGoodFinished()) {
       return chain.getResult();
    } else {
      return null;
    }
  }

  /**
   * Overridden method.
   * 
   * @param econtext context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#processEvent(org.exoplatform.services.portletcontainer.plugins.pc.aop.EventExecutionContext)
   */
  protected final Object processEvent(final EventExecutionContext econtext) throws Throwable {
    log.debug("--> processEvent method, call portlet filter aspect");

    EventRequestImp req = (EventRequestImp) econtext.getRequest();
    EventResponseImp res = (EventResponseImp) econtext.getResponse();
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    PortletFilterChainImpl chain = (PortletFilterChainImpl) req.getPortletDatas().getFilterChain();
    for (Iterator<PortletFilterWrapper> iterator = chain.getFiltersIterator(); iterator.hasNext();) {
      PortletFilterWrapper portletFilter = iterator.next();
      FilterConfig fc = new PortletFilterConfigImpl(portletFilter.getFilterName(),
                                                    portletFilter.getInitParam(),
                                                    portletContext);
      portletFilter.init(fc);
    }
    chain.restart(econtext);
    chain.doFilter(req, res);
    if (chain.isGoodFinished())
    	return chain.getResult();
    else
      return null;
  }

}
