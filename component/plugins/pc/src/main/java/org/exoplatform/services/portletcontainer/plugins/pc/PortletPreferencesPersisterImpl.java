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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;

/**
 * Portlet preferences simple memory persister.
 */
public class PortletPreferencesPersisterImpl implements PortletPreferencesPersister, Serializable {

  /**
   * Preferences.
   */
  private final Map<String, ExoPortletPreferences> prefs = new HashMap<String, ExoPortletPreferences>();

  /**
   * Logger.
   */
  private final transient Log log;

  /**
   * Simple constructor.
   */
  public PortletPreferencesPersisterImpl() {
    log = ExoLogger.getLogger(getClass());
  }

  /**
   * Overridden method.
   *
   * @param windowID window id
   * @return exo prefs
   * @throws Exception exception
   * @see org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister#getPortletPreferences(org.exoplatform.services.portletcontainer.pci.WindowID)
   */
  public final ExoPortletPreferences getPortletPreferences(final WindowID windowID) throws Exception {
    return prefs.get(windowID.generateKey());
  }

  /**
   * Overridden method.
   *
   * @param windowID window id
   * @param exoPref exo prefs
   * @throws Exception exception
   * @see org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister#savePortletPreferences(org.exoplatform.services.portletcontainer.pci.WindowID, org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences)
   */
  public final void savePortletPreferences(final WindowID windowID, final ExoPortletPreferences exoPref) throws Exception {
    prefs.put(windowID.generateKey(), exoPref);
  }

}
