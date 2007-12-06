/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomResponseWrapper;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */

public class PortletContentCommand extends BaseCommandUnit {

  public Object render(RenderExecutionContext rcontext)  throws Throwable {
    log_.debug("--> render method, call content ");
    Object result =  rcontext.executeNextUnit() ;
    PortletResponseImp rimpl = (PortletResponseImp) rcontext.response_ ;
    CustomResponseWrapper responseWrapper = (CustomResponseWrapper)  rimpl.getResponse() ;
    responseWrapper.flushBuffer();
    RenderOutput routput = (RenderOutput) rimpl.getOutput() ;
    routput.setContent(responseWrapper.getPortletContent());
    routput.setCacheHit(false);
    return result ;
  }
}