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
 * Created by The eXo Platform SAS Author : Roman Pedchenko
 * <roman.pedchenko@exoplatform.com.ua>
 */
public class ResourceRequestImp extends ClientDataRequestImp implements ResourceRequest {

  public ResourceRequestImp(final RequestContext reqCtx) {
    super(reqCtx);
  }

  public String getResourceID() {
    return ((ResourceInput) getInput()).getResourceID();
  }

  public String getLifecyclePhase() {
    return RESOURCE_PHASE;
  }

  @Override
  public Cookie[] getCookies() {
    return super.getCookies();
  }

  public String getMethod() {
    return super.getMethod();
  }

  public Map<String, String[]> getPrivateRenderParameterMap() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getETag() {
    return getProperty(RenderRequest.ETAG);
  }

  public String getCacheability() {
    return ((ResourceInput) getInput()).getCacheability();
  }

}
