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

import javax.portlet.Portlet;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import org.exoplatform.container.component.ExecutionContext;

/**
 * Created by The eXo Platform SAS.
 * Author : Roman Pedchenko roman.pedchenko@exoplatform.com.ua
 */
public class ResourceExecutionContext extends ExecutionContext {

  /**
   * Request.
   */
  private ResourceRequest request;

  /**
   * Response.
   */
  private ResourceResponse response;

  /**
   * Portlet object.
   */
  private Portlet portlet;

  /**
   * @param p portlet object
   * @param req request
   * @param res response
   */
  public ResourceExecutionContext(final Portlet p,
      final ResourceRequest req,
      final ResourceResponse res) {
    setPortlet(p);
    setRequest(req);
    setResponse(res);
  }

  /**
   * @param portlet the portlet to set
   */
  void setPortlet(Portlet portlet) {
    this.portlet = portlet;
  }

  /**
   * @return the portlet
   */
  Portlet getPortlet() {
    return portlet;
  }

  /**
   * @param response the response to set
   */
  void setResponse(ResourceResponse response) {
    this.response = response;
  }

  /**
   * @return the response
   */
  public ResourceResponse getResponse() {
    return response;
  }

  /**
   * @param request the request to set
   */
  void setRequest(ResourceRequest request) {
    this.request = request;
  }

  /**
   * @return the request
   */
  ResourceRequest getRequest() {
    return request;
  }

}
