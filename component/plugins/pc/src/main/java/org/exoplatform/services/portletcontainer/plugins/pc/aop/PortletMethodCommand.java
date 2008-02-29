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

import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceRequestImp;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
public class PortletMethodCommand extends BaseCommandUnit {

  protected Object render(RenderExecutionContext rcontext)  throws Throwable {
    RenderRequest request = rcontext.request_;
    RenderResponse response = rcontext.response_;
    request.setAttribute("javax.portlet.config",((RenderRequestImp)request).getPortletConfig());
    request.setAttribute("javax.portlet.request", request);
    request.setAttribute("javax.portlet.response", response);
    request.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.RENDER_PHASE);

    rcontext.portlet_.render(request, response) ;

    request.removeAttribute("javax.portlet.config");
    request.removeAttribute("javax.portlet.request");
    request.removeAttribute("javax.portlet.response");
    request.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
    return null ;
  }

  protected Object processAction(ActionExecutionContext acontext)  throws Throwable  {
    ActionRequest request = acontext.getRequest();
    ActionResponse response = acontext.getResponse();
    request.setAttribute("javax.portlet.config",((ActionRequestImp)request).getPortletConfig());
    request.setAttribute("javax.portlet.request", request);
    request.setAttribute("javax.portlet.response", response);
    request.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.ACTION_PHASE);

    acontext.getPortlet().processAction(acontext.getRequest(), acontext.getResponse()) ;

    request.removeAttribute("javax.portlet.config");
    request.removeAttribute("javax.portlet.request");
    request.removeAttribute("javax.portlet.response");
    request.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
    return null ;
  }

  protected Object serveResource(ResourceExecutionContext rcontext)  throws Throwable {
    if (rcontext.portlet_ instanceof ResourceServingPortlet) {
      ResourceRequest request = rcontext.request_;
      ResourceResponse response = rcontext.response_;
      request.setAttribute("javax.portlet.config",((ResourceRequestImp)request).getPortletConfig());
      request.setAttribute("javax.portlet.request", request);
      request.setAttribute("javax.portlet.response", response);
      request.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.RESOURCE_PHASE);

      ((ResourceServingPortlet) rcontext.portlet_).serveResource(rcontext.request_, rcontext.response_) ;

      request.removeAttribute("javax.portlet.config");
      request.removeAttribute("javax.portlet.request");
      request.removeAttribute("javax.portlet.response");
      request.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
    }
    return null ;
  }

  protected Object processEvent(EventExecutionContext econtext)  throws Throwable  {
    if (econtext.portlet_ instanceof EventPortlet) {
      EventRequest request = econtext.request_;
      EventResponse response = econtext.response_;
      request.setAttribute("javax.portlet.config",((EventRequestImp)request).getPortletConfig());
      request.setAttribute("javax.portlet.request", request);
      request.setAttribute("javax.portlet.response", response);
      request.setAttribute(PortletRequest.LIFECYCLE_PHASE, PortletRequest.EVENT_PHASE);

      ((EventPortlet) econtext.portlet_).processEvent(econtext.request_, econtext.response_) ;

      request.removeAttribute("javax.portlet.config");
      request.removeAttribute("javax.portlet.request");
      request.removeAttribute("javax.portlet.response");
      request.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
    }
    return null ;
  }

}