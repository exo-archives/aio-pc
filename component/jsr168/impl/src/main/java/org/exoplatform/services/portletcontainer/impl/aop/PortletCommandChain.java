/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Sep 17, 2005
 */
public class PortletCommandChain {
  private BaseCommandUnit commands_ ;
   
  public void addCommand(BaseCommandUnit  command) {
    if(commands_ == null) commands_ = command ;
    else commands_.addExecutionUnit(command) ;
  } 
  
  public  void doRender(Portlet portlet, RenderRequest req, RenderResponse res) throws Throwable {
    RenderExecutionContext context = new RenderExecutionContext(portlet, req, res) ;
    context.setCurrentExecutionUnit(commands_) ;
    context.execute() ;
  }
  
  public  void doProcessAction(Portlet portlet, ActionRequest req, ActionResponse res) throws Throwable {
    ActionExecutionContext context = new ActionExecutionContext(portlet, req, res) ;
    context.setCurrentExecutionUnit(commands_) ;
    context.execute() ;
  }
}
