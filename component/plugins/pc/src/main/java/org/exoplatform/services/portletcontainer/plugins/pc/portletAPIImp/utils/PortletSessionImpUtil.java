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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.utils;

import javax.portlet.PortletSession;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: Jul 26, 2003
 * Time: 4:54:01 PM
 *
 * The attribute name may be encoded according to its scope (application or portlet)
 * using javax.portlet.p.{ID}?attributeName (for portlet scope)
 */
public final class PortletSessionImpUtil {

  /**
   * Attribute name prefix.
   */
  private static final String PORTLET_SCOPE_NAMESPACE = "javax.portlet.p.";

  /**
   * Private constructor.
   */
  private PortletSessionImpUtil() {
  }

  /**
   * @param windowId id
   * @param attributeName attr name
   * @param scope scope
   * @return encoded attr name
   */
  public static String encodePortletSessionAttribute(final String windowId,
      final String attributeName,
      final int scope) {
    StringBuffer sB = new StringBuffer();
    if (PortletSession.APPLICATION_SCOPE == scope) {
      sB.append(attributeName);
      return sB.toString();
    } else if (PortletSession.PORTLET_SCOPE == scope) {
      sB.append(PORTLET_SCOPE_NAMESPACE);
      sB.append(windowId);
      sB.append("?");
      sB.append(attributeName);
      return sB.toString();
    } else
      return null;
  }

}
