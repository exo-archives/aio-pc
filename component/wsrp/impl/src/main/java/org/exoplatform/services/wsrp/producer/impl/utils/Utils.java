/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.producer.impl.utils;


import java.rmi.RemoteException;

import org.exoplatform.services.wsrp.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.type.RegistrationContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 11 févr. 2004
 * Time: 19:33:49
 */

public class Utils {
  public static void testRegistration(RegistrationContext registrationContext,
                                      org.exoplatform.services.wsrp.producer.PersistentStateManager stateManager)
      throws RemoteException {
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
  }
}