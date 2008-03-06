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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.persistenceImp;

import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationProxy;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletPreferencesImp;

/**
 * Created by The eXo Platform SAS . Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Jun 14, 2003 Time: 1:12:22 PM
 */
public class DefaultPersistenceManager implements PersistenceManager {

  private Log            log_;

  protected ExoContainer cont;

  public DefaultPersistenceManager(ExoContainerContext context) throws Exception {
    log_ = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    cont = context.getContainer();
  }

  public PortletWindowInternal getWindow(Input input,
                                         ExoPortletPreferences defaultPrefs) {
    WindowID windowID = input.getInternalWindowID();
    PortletApplicationProxy proxy = (PortletApplicationProxy) cont.getComponentInstance(windowID.getPortletApplicationName());
    String validatorClassName = null;
    PreferencesValidator validator = null;
    if (defaultPrefs != null) {
      validatorClassName = defaultPrefs.getPreferencesValidator();
      if (validatorClassName != null) {
        validator = proxy.getValidator(validatorClassName, windowID.getPortletName());
      }
    }
    PortletPreferencesPersister currentPersister = null;
    if (input.getPortletPreferencesPersister() != null) {
      currentPersister = input.getPortletPreferencesPersister();
    } else {
      ExoContainer container = cont;
      currentPersister = (PortletPreferencesPersister) container.getComponentInstanceOfType(PortletPreferencesPersister.class);
    }
    PortletPreferencesImp prefsImp = null;
    if (!input.isStateSaveOnClient()) {
      try {
        ExoPortletPreferences preferences = currentPersister.getPortletPreferences(windowID);
        if (preferences != null) {
          prefsImp = new PortletPreferencesImp(validator, defaultPrefs, windowID, currentPersister);
          prefsImp.setCurrentPreferences(preferences);
          return new PortletWindowInternal(windowID, prefsImp);
        }
      } catch (Exception ex) {
        log_.error("Error: ", ex);
      }
    } else {
      byte[] portletState = input.getPortletState();
      if (portletState != null) {
        try {
          return new PortletWindowInternal(windowID, (PortletPreferences) IOUtil.deserialize(portletState));
        } catch (Exception e) {
          log_.error("Error: ", e);
        }
      }
    }
    prefsImp = new PortletPreferencesImp(validator, defaultPrefs, windowID, currentPersister);
    return new PortletWindowInternal(windowID, prefsImp);
  }
}
