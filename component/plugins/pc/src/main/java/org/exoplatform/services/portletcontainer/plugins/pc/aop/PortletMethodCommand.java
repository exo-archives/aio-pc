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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventPortlet;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceServingPortlet;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceRequestImp;

/**
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
public class PortletMethodCommand extends BaseCommandUnit {

  protected static Log log = ExoLogger.getLogger("pc.monitor");
  
  /**
   * Overridden method.
   *
   * @param rcontext context
   * @return result
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#render(org.exoplatform.services.portletcontainer.plugins.pc.aop.RenderExecutionContext)
   */
  protected final Object render(final RenderExecutionContext rcontext) throws Throwable {
    RenderRequest request = rcontext.getRequest();
    RenderResponse response = rcontext.getResponse();
    request.setAttribute("javax.portlet.config", ((RenderRequestImp) request).getPortletConfig());
    request.setAttribute("javax.portlet.request", request);
    request.setAttribute("javax.portlet.response", response);
    request.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.RENDER_PHASE);
    long start = System.currentTimeMillis();
    rcontext.getPortlet().render(request, response);
    if (log.isDebugEnabled()) {
      log.debug("render() on " + request.getWindowID() +" executed in " + (System.currentTimeMillis()-start) +"ms");
     }
    request.removeAttribute("javax.portlet.config");
    request.removeAttribute("javax.portlet.request");
    request.removeAttribute("javax.portlet.response");
    request.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
    return null;
  }

  /**
   * Overridden method.
   *
   * @param acontext context
   * @return result
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#processAction(org.exoplatform.services.portletcontainer.plugins.pc.aop.ActionExecutionContext)
   */
  protected final Object processAction(final ActionExecutionContext acontext) throws Throwable {
    ActionRequest request = acontext.getRequest();
    ActionResponse response = acontext.getResponse();
    request.setAttribute("javax.portlet.config", ((ActionRequestImp) request).getPortletConfig());
    request.setAttribute("javax.portlet.request", request);
    request.setAttribute("javax.portlet.response", response);
    request.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.ACTION_PHASE);
    long start = System.currentTimeMillis();
    acontext.getPortlet().processAction(acontext.getRequest(), acontext.getResponse());
    if (log.isDebugEnabled()) {
      log.debug("processAction() on " + request.getWindowID() +" executed in " + (System.currentTimeMillis()-start) +"ms");
     }
    request.removeAttribute("javax.portlet.config");
    request.removeAttribute("javax.portlet.request");
    request.removeAttribute("javax.portlet.response");
    request.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
    return null;
  }

  /**
   * Overridden method.
   *
   * @param rcontext context
   * @return result
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#serveResource(org.exoplatform.services.portletcontainer.plugins.pc.aop.ResourceExecutionContext)
   */
  protected final Object serveResource(final ResourceExecutionContext rcontext) throws Throwable {
    if (rcontext.getPortlet() instanceof ResourceServingPortlet) {
      ResourceRequest request = rcontext.getRequest();
      ResourceResponse response = rcontext.getResponse();
      request.setAttribute("javax.portlet.config", ((ResourceRequestImp) request)
          .getPortletConfig());
      request.setAttribute("javax.portlet.request", request);
      request.setAttribute("javax.portlet.response", response);
      request.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.RESOURCE_PHASE);
      long start = System.currentTimeMillis();
      ((ResourceServingPortlet) rcontext.getPortlet()).serveResource(rcontext.getRequest(),
          rcontext.getResponse());
      if (log.isDebugEnabled()) {
        log.debug("serveResource() on " + request.getWindowID() +" executed in " + (System.currentTimeMillis()-start) +"ms");
       }
      request.removeAttribute("javax.portlet.config");
      request.removeAttribute("javax.portlet.request");
      request.removeAttribute("javax.portlet.response");
      request.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
    }
    return null;
  }

  /**
   * Overridden method.
   *
   * @param econtext event context
   * @return result
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#processEvent(org.exoplatform.services.portletcontainer.plugins.pc.aop.EventExecutionContext)
   */
  protected final Object processEvent(final EventExecutionContext econtext) throws Throwable {
    if (econtext.getPortlet() instanceof EventPortlet) {
      EventRequest request = econtext.getRequest();
      EventResponse response = econtext.getResponse();
      request.setAttribute("javax.portlet.config", ((EventRequestImp) request).getPortletConfig());
      request.setAttribute("javax.portlet.request", request);
      request.setAttribute("javax.portlet.response", response);
      request.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.EVENT_PHASE);
      long start = System.currentTimeMillis();
      ((EventPortlet) econtext.getPortlet()).processEvent(econtext.getRequest(), econtext
          .getResponse());
      if (log.isDebugEnabled()) {
        log.debug("processEvent() on " + request.getWindowID() +" executed in " + (System.currentTimeMillis()-start) +"ms");
       }
      request.removeAttribute("javax.portlet.config");
      request.removeAttribute("javax.portlet.request");
      request.removeAttribute("javax.portlet.response");
      request.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
    }
    return null;
  }

}
