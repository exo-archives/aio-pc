/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */

public class PortletMethodCommand extends BaseCommandUnit {

  protected Object render(RenderExecutionContext rcontext)  throws Throwable {
    rcontext.portlet_.render(rcontext.request_, rcontext.response_) ;
    return null ;
  }
  
  protected Object processAction(ActionExecutionContext acontext)  throws Throwable  {
    acontext.portlet_.processAction(acontext.request_, acontext.response_) ;
    return null ;
  }
}