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
import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * Created by The eXo Platform SAS
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

  public  void doProcessEvent(Portlet portlet, EventRequest req, EventResponse res) throws Throwable {
    EventExecutionContext context = new EventExecutionContext(portlet, req, res) ;
    context.setCurrentExecutionUnit(commands_) ;
    context.execute() ;
  }

  public  void doServeResource(Portlet portlet, ResourceRequest req, ResourceResponse res) throws Throwable {
    ResourceExecutionContext context = new ResourceExecutionContext(portlet, req, res) ;
    context.setCurrentExecutionUnit(commands_) ;
    context.execute() ;
  }

//  public  void doServeFragment(Portlet portlet, FragmentRequest req, FragmentResponse res) throws Throwable {
//    FragmentExecutionContext context = new FragmentExecutionContext(portlet, req, res) ;
//    context.setCurrentExecutionUnit(commands_) ;
//    context.execute() ;
//  }

}
