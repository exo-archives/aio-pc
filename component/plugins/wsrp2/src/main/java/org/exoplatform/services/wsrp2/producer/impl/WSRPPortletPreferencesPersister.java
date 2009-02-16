/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;

/**
 * User: Benjamin Mestrallet Date: 8 juin 2004
 */
public class WSRPPortletPreferencesPersister implements PortletPreferencesPersister, Serializable {

  private static WSRPPortletPreferencesPersister ourInstance = new WSRPPortletPreferencesPersister();

  private Map<String, ExoPortletPreferences>     prefs       = new HashMap<String, ExoPortletPreferences>();

  private transient Log                          log;

  private WSRPPortletPreferencesPersister() {
    this.log = ExoLogger.getLogger(getClass());
  }

  public static WSRPPortletPreferencesPersister getInstance() {
    return ourInstance;
  }

  public ExoPortletPreferences getPortletPreferences(WindowID windowID) throws Exception {
    return (ExoPortletPreferences) prefs.get(windowID.getUniqueID());
  }

  public void savePortletPreferences(WindowID windowID, ExoPortletPreferences preferences) throws Exception {
    prefs.put(windowID.getUniqueID(), preferences);
  }

}
