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

import org.exoplatform.container.component.ExecutionContext;

/**
 * Created by The eXo Platform SAS. Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Sep 17, 2005
 */
public class ActionExecutionContext extends ExecutionContext {

  /**
   * Request.
   */
  private ActionRequest request;

  /**
   * Response.
   */
  private ActionResponse response;

  /**
   * Portlet object.
   */
  private Portlet portlet;

  /**
   * @param p portlet object
   * @param req request
   * @param res response
   */
  public ActionExecutionContext(final Portlet p, final ActionRequest req, final ActionResponse res) {
    setPortlet(p);
    setRequest(req);
    setResponse(res);
  }

  /**
   * @param portlet the portlet to set
   */
  final void setPortlet(final Portlet portlet) {
    this.portlet = portlet;
  }

  /**
   * @return the portlet
   */
  final Portlet getPortlet() {
    return portlet;
  }

  /**
   * @param response the response to set
   */
  final void setResponse(final ActionResponse response) {
    this.response = response;
  }

  /**
   * @return the response
   */
  final ActionResponse getResponse() {
    return response;
  }

  /**
   * @param request the request to set
   */
  final void setRequest(final ActionRequest request) {
    this.request = request;
  }

  /**
   * @return the request
   */
  final ActionRequest getRequest() {
    return request;
  }

}
