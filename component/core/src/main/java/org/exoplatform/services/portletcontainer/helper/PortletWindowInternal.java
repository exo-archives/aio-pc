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
package org.exoplatform.services.portletcontainer.helper;

import java.io.Serializable;

import javax.portlet.PortletPreferences;

import org.exoplatform.services.portletcontainer.pci.WindowID;

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 2:10:13 AM
 */
public class PortletWindowInternal implements Serializable {
  private PortletPreferences preferences;

  private WindowID           windowID;

  public PortletWindowInternal() {
  }

  public PortletWindowInternal(WindowID windowID,
                               PortletPreferences preferences) {
    this.windowID = windowID;
    this.preferences = preferences;
  }

  public WindowID getWindowID() {
    return windowID;
  }

  public PortletPreferences getPreferences() {
    return preferences;
  }

  public void setPreferences(PortletPreferences preferences) {
    this.preferences = preferences;
  }
}
