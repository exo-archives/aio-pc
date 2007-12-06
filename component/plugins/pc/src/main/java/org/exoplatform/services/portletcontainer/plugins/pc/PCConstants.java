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
package org.exoplatform.services.portletcontainer.plugins.pc;

/**
 * Created by The eXo Platform SAS .
 * Author : Roman Pedchenko <roman.pedchenko@exoplatform.com.ua>
 */
public class PCConstants {
  
  public static final String XHTML_MIME_TYPE = "text/html";
  
  public static final String renderString   = "render";

  public static final String actionString   = "action";

  public static final String eventString    = "event";

  public static final String resourceString = "resource";

  public static final int    renderInt      = 0;

  public static final int    actionInt      = 1;

  public static final int    eventInt       = 2;

  public static final int    resourceInt    = 3;

  public static String actionToString(int action) {
    switch (action) {
    case actionInt:
      return actionString;
    case eventInt:
      return eventString;
    case resourceInt:
      return resourceString;
    default:
      return renderString;
    }
  }

  public static int actionToInt(String action) {
    if (action == null)
      return renderInt;
    if (action.equals(actionString))
      return actionInt;
    if (action.equals(eventString))
      return eventInt;
    if (action.equals(resourceString))
      return resourceInt;
    return renderInt;
  }
}
