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

import javax.portlet.CacheControl;
import javax.portlet.RenderResponse;

/**
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko <roman.pedchenko@exoplatform.com.ua>
 */
public class CacheControlImp implements CacheControl {

  private MimeResponseImp resp;

  public CacheControlImp(MimeResponseImp resp) {
    this.resp = resp;
  }

  public int getExpirationTime() {
    return Integer.parseInt(resp.getProperty(RenderResponse.EXPIRATION_CACHE));
  }

  public void setExpirationTime(int time) {
    resp.setProperty(RenderResponse.EXPIRATION_CACHE, String.valueOf(time));
  }

  public boolean isPublicScope() {
    return resp.getProperty(RenderResponse.CACHE_SCOPE).equals(RenderResponse.PUBLIC_SCOPE);
  }

  public void setPublicScope(boolean publicScope) {
    if (publicScope)
      resp.setProperty(RenderResponse.CACHE_SCOPE, RenderResponse.PUBLIC_SCOPE);
    else
      resp.setProperty(RenderResponse.CACHE_SCOPE, RenderResponse.PRIVATE_SCOPE);
  }

  public String getETag() {
    return resp.getProperty(RenderResponse.ETAG);
  }

  public void setETag(String token) {
    resp.setProperty(RenderResponse.ETAG, token);
  }

  public boolean useCachedContent() {
    return resp.getProperty(RenderResponse.USE_CACHED_CONTENT) != null;
  }

  public void setUseCachedContent(boolean useCachedContent) {
    if (useCachedContent)
      resp.setProperty(RenderResponse.USE_CACHED_CONTENT, "");
    else
      resp.removeProperty(RenderResponse.USE_CACHED_CONTENT);
  }

}
