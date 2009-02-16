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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.servlet.http.Cookie;

import org.exoplatform.services.portletcontainer.pci.ResourceInput;

/**
 * Created by The eXo Platform SAS.
 * Author : Roman Pedchenko roman.pedchenko@exoplatform.com.ua
 */
public class ResourceRequestImp extends ClientDataRequestImp implements ResourceRequest {

  /**
   * @param reqCtx request context
   */
  public ResourceRequestImp(final RequestContext reqCtx) {
    super(reqCtx);
  }

  /**
   * Overridden method.
   *
   * @return resource id
   * @see javax.portlet.ResourceRequest#getResourceID()
   */
  public final String getResourceID() {
    return ((ResourceInput) getInput()).getResourceID();
  }

  /**
   * Overridden method.
   *
   * @return lifecycle phase
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ClientDataRequestImp#getLifecyclePhase()
   */
  public final String getLifecyclePhase() {
    return RESOURCE_PHASE;
  }

  /**
   * Overridden method.
   *
   * @return cookies
   * @see javax.servlet.http.HttpServletRequestWrapper#getCookies()
   */
  @Override
  public final Cookie[] getCookies() {
    return super.getCookies();
  }

  /**
   * Overridden method.
   *
   * @return http method
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ClientDataRequestImp#getMethod()
   */
  public final String getMethod() {
    return super.getMethod();
  }

  /**
   * Overridden method.
   *
   * @return private render parameter map
   * @see javax.portlet.ResourceRequest#getPrivateRenderParameterMap()
   */
  public final Map<String, String[]> getPrivateRenderParameterMap() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Overridden method.
   *
   * @return ETag
   * @see javax.portlet.ResourceRequest#getETag()
   */
  public final String getETag() {
    return getProperty(RenderRequest.ETAG);
  }

  /**
   * Overridden method.
   *
   * @return cacheability
   * @see javax.portlet.ResourceRequest#getCacheability()
   */
  public final String getCacheability() {
    return ((ResourceInput) getInput()).getCacheability();
  }

}
