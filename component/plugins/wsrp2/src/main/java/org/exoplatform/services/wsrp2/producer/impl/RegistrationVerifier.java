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

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.picocontainer.Startable;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Feb 6, 2009
 */
public class RegistrationVerifier implements Startable {

  private static final Log              LOG = ExoLogger.getLogger(RegistrationVerifier.class);

  private static WSRPConfiguration      conf;

  private static PersistentStateManager persistentStateManager;

  public RegistrationVerifier(WSRPConfiguration conf, PersistentStateManager persitentStateManager) {
    RegistrationVerifier.conf = conf;
    RegistrationVerifier.persistentStateManager = persitentStateManager;
  }

  /**
   * Whether registrationHandle is valid if it presents within
   * RegistrationContext. And whether it is required. Required access to
   * PersistentStateManager and WSRPConfiguration.
   * 
   * @param registrationContext
   * @return
   * @throws InvalidRegistration
   */
  public static boolean checkRegistrationContext(RegistrationContext registrationContext) throws InvalidRegistration,
                                                                                         InvalidHandle {
    if (registrationContext != null && registrationContext.getRegistrationHandle() != null
        && registrationContext.getRegistrationHandle().length() != 0) {
      // present registrationHandle within RegistrationContext, so whether it is valid
      try {
        // does registered this registrationHandle
        boolean isRegistered = persistentStateManager.isRegistered(registrationContext);
        if (LOG.isDebugEnabled())
          LOG.debug(" isRegistered = " + isRegistered);
        if (!isRegistered) {
          throw new InvalidRegistration("Provided '" + registrationContext.getRegistrationHandle()
              + "' registrationHandle is unregistered.");
        }
        return true;
      } catch (WSRPException e) {
        // unknown registrationHandle or something else
        throw new InvalidRegistration(e.getMessage(), e);
      }
    } else {
      // haven't registrationHandle within RegistrationContext, so we have to check whether it is required 
      if (conf.isRegistrationRequired()) {
        LOG.debug("Registration required");
        throw new InvalidRegistration("Registration required, but haven't registrationHandle");
      } else {
        LOG.debug("Registration non required");
        return false;
      }
    }
  }

  public void start() {
  }

  public void stop() {
  }
}
