/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletRequestImp;
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
}