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
package org.exoplatform.services.portletcontainer.persistence;

import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;

/**
 * Created y the eXo platform team.
 * User: Benjamin Mestrallet Date: 31 mai 2004
 */
public interface PortletPreferencesPersister {

  /**
   * @param windowID window id
   * @return preferences
   * @throws Exception exception
   */
  ExoPortletPreferences getPortletPreferences(WindowID windowID) throws Exception;

  /**
   * @param windowID window id
   * @param preferences preferences
   * @throws Exception exception
   */
  void savePortletPreferences(WindowID windowID, ExoPortletPreferences preferences) throws Exception;

}
