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

import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletRequestImp;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
public class PortletSecurityCommand extends BaseCommandUnit {

  public PortletSecurityCommand() {

  }

  protected Object render(RenderExecutionContext rcontext)  throws Throwable  {
    log_.debug("--> render method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) rcontext.request_ ;
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure) {
      if(!req.isSecure())
        throw new Throwable("Need a secure transport layer");
    }
    return rcontext.executeNextUnit();
  }

  protected Object processAction(ActionExecutionContext acontext)  throws  Throwable {
    log_.debug("--> processAction method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) acontext.request_;
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure) {
      if(!req.isSecure())
        throw new Throwable();
    }
    return acontext.executeNextUnit() ;
  }

  protected Object serveResource(ResourceExecutionContext rcontext)  throws Throwable  {
    log_.debug("--> render method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) rcontext.request_ ;
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure) {
      if(!req.isSecure())
        throw new Throwable("Need a secure transport layer");
    }
    return rcontext.executeNextUnit();
  }

  protected Object processEvent(EventExecutionContext econtext)  throws  Throwable {
    log_.debug("--> processAction method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) econtext.request_;
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure) {
      if(!req.isSecure())
        throw new Throwable();
    }
    return econtext.executeNextUnit() ;
  }
}
