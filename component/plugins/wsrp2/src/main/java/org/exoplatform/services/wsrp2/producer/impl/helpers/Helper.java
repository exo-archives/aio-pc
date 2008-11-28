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
package org.exoplatform.services.wsrp2.producer.impl.helpers;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp2.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp2.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp2.producer.impl.WSRPConfiguration;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletLifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
 */
public class Helper {

  static private ExoContainer      cont = ExoContainerContext.getCurrentContainer();

  static private WSRPConfiguration conf = (WSRPConfiguration) cont.getComponentInstanceOfType(WSRPConfiguration.class);

  public static boolean checkLifetime(RegistrationContext registrationContext,
                                      UserContext userContext) {
    if (registrationContext == null)
      return true;
    RegistrationOperationsInterface roi = (RegistrationOperationsInterface) cont.getComponentInstanceOfType(RegistrationOperationsInterface.class);
    try {
      Lifetime lf = roi.getRegistrationLifetime(registrationContext, userContext);
      if (lf != null) {
        if (lifetimeExpired(lf)) {
          roi.deregister(registrationContext, userContext);
          return false;
        }
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return true;
  }

  public static boolean lifetimeExpired(Lifetime lf) {
    if (lf != null)
      return lf.getTerminationTime().getMillisecond() < lf.getCurrentTime().getMillisecond();
    else
      return false;
  }

  public static boolean checkPortletLifetime(RegistrationContext registrationContext,
                                             List<PortletContext> portletContexts,
                                             UserContext userContext,
                                             PortletManagementOperationsInterface pmoi) {
    //ExoContainer cont = ExoContainerContext.getCurrentContainer();
    // PortletManagementOperationsInterface poi = (PortletManagementOperationsInterface) cont.getComponentInstanceOfType(PortletManagementOperationsInterface.class);

    if (registrationContext == null)
      return true;
    try {
      GetPortletsLifetimeResponse resp = pmoi.getPortletsLifetime(registrationContext,
                                                                  portletContexts,
                                                                  userContext);
      if (resp != null) {
        if (resp.getPortletLifetime() != null && resp.getPortletLifetime().size() != 0) {
          PortletLifetime plf = resp.getPortletLifetime().get(0);
          Lifetime lf = plf.getScheduledDestruction();
          if (lf != null) {
            if (lifetimeExpired(lf)) {
              String portletHandle = portletContexts.get(0).getPortletHandle();
              pmoi.destroyPortlets(registrationContext, Arrays.asList(new String[] { portletHandle }), userContext);
              return false;
            }
          }
        }
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return true;
  }

  private void checkRegistrationContext(RegistrationContext registrationContext) throws RemoteException {
    if (conf.isRegistrationRequired()) {
      if (registrationContext == null) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    }
  }

}
