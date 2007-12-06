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

import javax.portlet.RenderRequest;
import javax.servlet.http.Cookie;

/**
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 5:50:44 PM
 */
public class RenderRequestImp extends PortletRequestImp implements RenderRequest {

  public RenderRequestImp(RequestContext reqCtx) {
    super(reqCtx);
  }

  public String getETag() {
    return getProperty(RenderRequest.ETAG);
  }

  public String getLifecyclePhase() {
    return RENDER_PHASE;
  }

  @Override
  public Cookie[] getCookies() {
    // TODO Auto-generated method stub
    return super.getCookies();
  }

}
