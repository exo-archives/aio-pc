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

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 11:49:38 AM
 */
public class CustomRequestWrapperUtil {

  /**
   * Separator.
   */
  private static final char SEPARATOR = '$';

  /**
   * @param windowId window id
   * @param attributeName attribute name
   * @return decoded attribute
   */
  public static final String decodeRequestAttribute(final String windowId, String attributeName) {
    if (attributeName.startsWith(windowId + SEPARATOR)) {
      int index = attributeName.indexOf(SEPARATOR);
      if (index > -1)
        attributeName = attributeName.substring(index + 1);
    }
    return attributeName;
  }

  /**
   * @param windowId window id
   * @param attributeName attribbute name
   * @return encoded attribute
   */
  public static final String encodeAttribute(final String windowId, final String attributeName) {
    if (attributeName.startsWith("javax.") || attributeName.startsWith("org.apache"))
      return attributeName;
    StringBuffer sB = new StringBuffer();
    sB.append(windowId);
    sB.append(SEPARATOR);
    sB.append(attributeName);
    return sB.toString();
  }

}
