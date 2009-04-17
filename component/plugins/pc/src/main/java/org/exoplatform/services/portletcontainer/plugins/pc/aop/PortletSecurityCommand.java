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

/**
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
public class PortletSecurityCommand extends BaseCommandUnit {

  /**
   * Simple constructor.
   */
  public PortletSecurityCommand() {
  }

  /**
   * Overridden method.
   *
   * @param rcontext context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#render(org.exoplatform.services.portletcontainer.plugins.pc.aop.RenderExecutionContext)
   */
  protected Object render(final RenderExecutionContext rcontext) throws Throwable {
    log.debug("--> render method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) rcontext.getRequest();
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure)
      if (!req.isSecure())
        throw new Throwable("Need a secure transport layer");
    return rcontext.executeNextUnit();
  }

  /**
   * Overridden method.
   *
   * @param acontext context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#processAction(org.exoplatform.services.portletcontainer.plugins.pc.aop.ActionExecutionContext)
   */
  protected Object processAction(final ActionExecutionContext acontext) throws Throwable {
    log.debug("--> processAction method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) acontext.getRequest();
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure)
      if (!req.isSecure())
        throw new Throwable();
    return acontext.executeNextUnit();
  }

  /**
   * Overridden method.
   *
   * @param rcontext context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#serveResource(org.exoplatform.services.portletcontainer.plugins.pc.aop.ResourceExecutionContext)
   */
  protected Object serveResource(final ResourceExecutionContext rcontext) throws Throwable {
    log.debug("--> render method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) rcontext.getRequest();
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure)
      if (!req.isSecure())
        throw new Throwable("Need a secure transport layer");
    return rcontext.executeNextUnit();
  }

  /**
   * Overridden method.
   *
   * @param econtext context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#processEvent(org.exoplatform.services.portletcontainer.plugins.pc.aop.EventExecutionContext)
   */
  protected Object processEvent(final EventExecutionContext econtext) throws Throwable {
    log.debug("--> processAction method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) econtext.getRequest();
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure)
      if (!req.isSecure())
        throw new Throwable();
    return econtext.executeNextUnit();
  }

}
