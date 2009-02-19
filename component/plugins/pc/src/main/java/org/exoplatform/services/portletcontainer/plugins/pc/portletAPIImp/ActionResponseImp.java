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

import java.io.IOException;
import java.net.URLEncoder;

import javax.portlet.ActionResponse;
import javax.portlet.PortletModeException;
import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 2:44:18 PM
 */
public class ActionResponseImp extends StateAwareResponseImp implements ActionResponse {

  /**
   * Location.
   */
  private String location;

  /**
   * @param resCtx response context
   */
  public ActionResponseImp(final ResponseContext resCtx) {
    super(resCtx);
  }

  /**
   * Overridden method.
   *
   * @param location1 location
   * @throws IOException exception
   * @see javax.servlet.http.HttpServletResponseWrapper#sendRedirect(java.lang.String)
   */
  public final void sendRedirect(final String location1) throws IOException {
    if (!isRedirectionPossible())
      throw new IllegalStateException(" The sendRedirect method can not be invoked "
          + "after any of the following methods of the ActionResponse interface has "
          + "been called: setPortletMode, setWindowState, setRenderParameter, "
          + "setRenderParameters");
    if (location1.startsWith("/") || location1.startsWith("http://")
        || location1.startsWith("https://")) {
      this.setSendRedirectAlreadyOccured(true);
      this.location = location1;
    } else
      throw new IllegalArgumentException("a relative or incorrect path URL is given");
  }

  /**
   * Overridden method.
   *
   * @param location1 location
   * @param renderUrlParamName render url param name
   * @throws IOException exception
   * @see javax.portlet.ActionResponse#sendRedirect(java.lang.String, java.lang.String)
   */
  public final void sendRedirect(String location1, String renderUrlParamName) throws IOException {
    PortletURL url = this.createRenderURL();
    try {
      url.setPortletMode(getPortletMode());
    } catch (PortletModeException e) {
    }
    try {
      url.setWindowState(getWindowState());
    } catch (WindowStateException e) {
    }
    url.setParameters(getResCtx().getPortletRequest().getParameterMap());
    String paramForRedirect = url.toString();
    try {
      renderUrlParamName = URLEncoder.encode(renderUrlParamName, "utf-8");
      paramForRedirect = URLEncoder.encode(paramForRedirect, "utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
    }
    if (location1.indexOf('?') < 0)
      location1 = location1 + "?";
    else
      location1 = location1 + "&";
    location1 = location1 + renderUrlParamName + "=" + paramForRedirect;
    sendRedirect(location1);
  }

  /**
   * @return location
   */
  public final String getLocation() {
    return location;
  }

}
