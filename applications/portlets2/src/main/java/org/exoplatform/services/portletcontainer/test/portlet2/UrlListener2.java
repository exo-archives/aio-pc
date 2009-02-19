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
package org.exoplatform.services.portletcontainer.test.portlet2;

import javax.portlet.PortletURL;
import javax.portlet.PortletURLGenerationListener;
import javax.portlet.ResourceURL;

/**
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
 */
public class UrlListener2 implements PortletURLGenerationListener {

  /**
   * Overridden method.
   *
   * @param url url object
   * @see javax.portlet.PortletURLGenerationListener#filterActionURL(javax.portlet.PortletURL)
   */
  public final void filterActionURL(final PortletURL url) {
    url.setParameter("paramFromListener2", "val");
  }

  /**
   * Overridden method.
   *
   * @param url url object
   * @see javax.portlet.PortletURLGenerationListener#filterRenderURL(javax.portlet.PortletURL)
   */
  public final void filterRenderURL(final PortletURL url) {
    url.setParameter("paramFromListener2", "val");
  }

  /**
   * Overridden method.
   *
   * @param url url object
   * @see javax.portlet.PortletURLGenerationListener#filterResourceURL(javax.portlet.ResourceURL)
   */
  public final void filterResourceURL(final ResourceURL url) {
  }

}
