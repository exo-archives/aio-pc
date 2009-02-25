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
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.helper.IOUtil;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationProxy;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletPreferencesImp;

/**
 * Created by The eXo Platform SAS. Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Jun 14, 2003 Time: 1:12:22 PM
 */
public class DefaultPersistenceManager implements PersistenceManager {

  /**
   * Logger.
   */
  private final Log      log;

  /**
   * Exo container.
   */
  protected ExoContainer cont;

  /**
   * @param context context
   * @throws Exception exception
   */
  public DefaultPersistenceManager(final ExoContainerContext context) throws Exception {
    log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    cont = context.getContainer();
  }

  /**
   * Overridden method.
   * 
   * @param input input
   * @param defaultPrefs prefs
   * @return portlet window internal object
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.persistenceImp.PersistenceManager#getWindow(org.exoplatform.services.portletcontainer.pci.Input,
   *     
   *     
   *      org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences)
   */
  public final PortletWindowInternal getWindow(final Input input,
                                               final ExoPortletPreferences defaultPrefs) {
    WindowID windowID = input.getInternalWindowID();

    // getting Preferences Validator
    String validatorClassName = null;
    PreferencesValidator validator = null;
    if (defaultPrefs != null) {
      validatorClassName = defaultPrefs.getPreferencesValidator();
      if (validatorClassName != null) {
        PortletApplicationProxy proxy = (PortletApplicationProxy) cont.getComponentInstance(windowID.getPortletApplicationName()
            + PCConstants.PORTLET_APP_ENCODER);
        validator = proxy.getValidator(validatorClassName, windowID.getPortletName());
      }
    }

    // going to get PortletPreferences for PortletWindowInternal
    PortletPreferences prefsImp = null;
    if (!input.isStateSaveOnClient()) { // state save on the server
      PortletPreferencesPersister persister = null;
      if (input.getPortletPreferencesPersister() != null)
        persister = input.getPortletPreferencesPersister();
      else {
        persister = (PortletPreferencesPersister) cont.getComponentInstanceOfType(PortletPreferencesPersister.class);
      }
      try {
        ExoPortletPreferences preferences = persister.getPortletPreferences(windowID);

        prefsImp = new PortletPreferencesImp(validator, defaultPrefs, windowID, persister);
        if (preferences != null) {
          ((PortletPreferencesImp) prefsImp).setCurrentPreferences(preferences);
        }
      } catch (Exception ex) {
        log.error("Error: ", ex);
      }
    } else { // state change kept on the client (for example consumer in WSRP)
      byte[] portletState = input.getPortletState();
      if (portletState != null) {
        try {
          prefsImp = (PortletPreferences) IOUtil.deserialize(portletState);
        } catch (Exception e) {
          log.error("Error: ", e);
        }
      }
    }
    return new PortletWindowInternal(windowID, prefsImp);
  }

}
