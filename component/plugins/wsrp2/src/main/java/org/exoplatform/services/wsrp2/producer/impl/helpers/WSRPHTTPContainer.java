/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.producer.impl.helpers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WSRPHTTPContainer extends HashMap<Object, Object> {

  private static final long serialVersionUID = 1L;

  private static ThreadLocal<WSRPHTTPContainer> threadLocal = new ThreadLocal<WSRPHTTPContainer>();

  private HttpServletRequest                    request;

  private HttpServletResponse                   response;

  private int                                   version;

  public WSRPHTTPContainer(HttpServletRequest request, HttpServletResponse response) {
    this.request = new WSRPHttpServletRequest(request);
    this.response = new WSRPHttpServletResponse(request, response);
    this.version = 2;
  }

  public static WSRPHTTPContainer getInstance() {
    return (WSRPHTTPContainer) threadLocal.get();
  }

  public static void removeInstance() {
    threadLocal.set(null);
  }

  public static void createInstance(final HttpServletRequest request,
                                    final HttpServletResponse response) {
    threadLocal.set(new WSRPHTTPContainer(request, response));
  }

  public WSRPHttpServletRequest getRequest() {
    return (WSRPHttpServletRequest) request;
  }

  public WSRPHttpServletResponse getResponse() {
    return (WSRPHttpServletResponse) response;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

}
