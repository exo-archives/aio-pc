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

package org.exoplatform.services.wsrp2.producer.impl.utils;

import java.rmi.RemoteException;

import org.exoplatform.services.wsrp2.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.type.RegistrationContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 11 févr. 2004
 * Time: 19:33:49
 */

public class Utils {
  public static void checkRegistration(RegistrationContext registrationContext,
                                      org.exoplatform.services.wsrp2.producer.PersistentStateManager stateManager) throws RemoteException {
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
  }
}
