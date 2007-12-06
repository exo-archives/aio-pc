/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
 
package org.exoplatform.services.wsrp2.producer.impl;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;


/**
 * User: Benjamin Mestrallet
 * Date: 8 juin 2004
 */
public class WSRPPortletPreferencesPersister implements PortletPreferencesPersister {

  private static WSRPPortletPreferencesPersister ourInstance = new WSRPPortletPreferencesPersister();

  public static WSRPPortletPreferencesPersister getInstance() {
    return ourInstance;
  }

  private WSRPPortletPreferencesPersister() {
  }

  private Map localMap = new HashMap();

  public ExoPortletPreferences getPortletPreferences(WindowID windowID) throws Exception {
    ExoPortletPreferences map = (ExoPortletPreferences) localMap.get(windowID.generateKey());
    if(map == null)
      return null;
    return map;
  }

  public void savePortletPreferences(WindowID windowID, ExoPortletPreferences preferences) throws Exception {
    localMap.put(windowID.generateKey(), preferences);
  }

}