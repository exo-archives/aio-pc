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
 * Created by The eXo Platform SAS.
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Sep 17, 2005
 */
public class PortletCommandChain {

  /**
   * Command unit.
   */
  private BaseCommandUnit commands;

  /**
   * @param command command unit
   */
  public final void addCommand(final BaseCommandUnit command) {
    if (this.commands == null)
      this.commands = command;
    else
      this.commands.addExecutionUnit(command);
  }

  /**
   * @param portlet portlet object
   * @param req request
   * @param res response
   * @throws Throwable throwable
   */
  public final void doRender(final Portlet portlet, final RenderRequest req, final RenderResponse res) throws Throwable {
    RenderExecutionContext context = new RenderExecutionContext(portlet, req, res);
    context.setCurrentExecutionUnit(commands);
    context.execute();
  }

  /**
   * @param portlet portlet object
   * @param req request
   * @param res response
   * @throws Throwable throwable
   */
  public final void doProcessAction(final Portlet portlet,
      final ActionRequest req,
      final ActionResponse res) throws Throwable {
    ActionExecutionContext context = new ActionExecutionContext(portlet, req, res);
    context.setCurrentExecutionUnit(commands);
    context.execute();
  }

  /**
   * @param portlet portlet object
   * @param req request
   * @param res response
   * @throws Throwable throwable
   */
  public final void doProcessEvent(final Portlet portlet, final EventRequest req, final EventResponse res) throws Throwable {
    EventExecutionContext context = new EventExecutionContext(portlet, req, res);
    context.setCurrentExecutionUnit(commands);
    context.execute();
  }

  /**
   * @param portlet portlet object
   * @param req request
   * @param res response
   * @throws Throwable throwable
   */
  public final void doServeResource(final Portlet portlet,
      final ResourceRequest req,
      final ResourceResponse res) throws Throwable {
    ResourceExecutionContext context = new ResourceExecutionContext(portlet, req, res);
    context.setCurrentExecutionUnit(commands);
    context.execute();
  }

}
