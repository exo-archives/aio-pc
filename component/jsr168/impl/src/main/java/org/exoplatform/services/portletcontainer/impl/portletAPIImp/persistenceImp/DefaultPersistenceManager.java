/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.persistenceImp;

import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationProxy;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletPreferencesImp;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;


/**
 * Created by The eXo Platform SARL.
 * Author : Tuan Nguyen
 * tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class DefaultPersistenceManager implements PersistenceManager {

  private Log log_;
  protected ExoContainer cont;

  public DefaultPersistenceManager(LogService lservice, ExoContainerContext context) throws Exception {
    log_ = lservice.getLog("org.exoplatform.services.portletcontainer");
    cont = context.getContainer();
  }

  public PortletWindowInternal getWindow(Input input, ExoPortletPreferences defaultPrefs) {
    WindowID windowID = input.getWindowID();
    ExoContainer manager = cont;
    PortletApplicationProxy proxy = (PortletApplicationProxy) manager.getComponentInstance(
        windowID.getPortletApplicationName());
    String validatorClassName = null;
    PreferencesValidator validator = null;
    if (defaultPrefs != null) {
      validatorClassName = defaultPrefs.getPreferencesValidator();
      if (validatorClassName != null) {
        validator = validator = proxy.getValidator(validatorClassName, windowID.getPortletName());
      }
    }
    PortletPreferencesPersister currentPersister = null;
    if (input.getPortletPreferencesPersister() != null) {
      currentPersister = input.getPortletPreferencesPersister();
    } else {
      ExoContainer container = cont;
      currentPersister = 
        (PortletPreferencesPersister) container.getComponentInstanceOfType(PortletPreferencesPersister.class);
    }
    PortletPreferencesImp prefsImp = null;
    if (!input.isStateSaveOnClient()) {
      try {
        ExoPortletPreferences preferences = 
        	currentPersister.getPortletPreferences(windowID);
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