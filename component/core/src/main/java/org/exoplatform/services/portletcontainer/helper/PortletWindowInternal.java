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
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin .
 * benjmestrallet@users.sourceforge.net Date: Jul 29, 2003 Time: 2:10:13 AM
 */
public class PortletWindowInternal implements Serializable {

  /**
   * Preferences.
   */
  private PortletPreferences preferences;

  /**
   * Window id.
   */
  private WindowID windowID;

  /**
   * Simple constructor.
   */
  public PortletWindowInternal() {
  }

  /**
   * @param windowID windowID
   * @param preferences preferences
   */
  public PortletWindowInternal(final WindowID windowID, final PortletPreferences preferences) {
    this.windowID = windowID;
    this.preferences = preferences;
  }

  /**
   * @return window id
   */
  public final WindowID getWindowID() {
    return windowID;
  }

  /**
   * @return preferences
   */
  public final PortletPreferences getPreferences() {
    return preferences;
  }

  /**
   * @param preferences preferences
   */
  public final void setPreferences(final PortletPreferences preferences) {
    this.preferences = preferences;
  }
}
