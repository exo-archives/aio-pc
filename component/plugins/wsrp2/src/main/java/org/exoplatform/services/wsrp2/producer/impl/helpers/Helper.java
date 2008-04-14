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

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp2.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletLifetime;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;

/**
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
 */
public class Helper {

  public static boolean checkLifetime(RegistrationContext registrationContext,
                                      UserContext userContext) {
    ExoContainer cont = ExoContainerContext.getCurrentContainer();
    RegistrationOperationsInterface roi = (RegistrationOperationsInterface) cont.getComponentInstanceOfType(RegistrationOperationsInterface.class);
    try {
      Lifetime lf = roi.getRegistrationLifetime(registrationContext, userContext);
      if (lf != null) {
        if (lf.getTerminationTime().getTimeInMillis() > lf.getCurrentTime().getTimeInMillis()) {
          roi.deregister(registrationContext, userContext);
          return false;
        }
      }
    } catch (RemoteException e) {
    }
    return true;
  }
  
  
  public static boolean checkPortletLifetime(RegistrationContext registrationContext,
                                             PortletContext[] portletContexts,
                                             UserContext userContext,
                                             PortletManagementOperationsInterface poi) {
    //ExoContainer cont = ExoContainerContext.getCurrentContainer();
    
   // PortletManagementOperationsInterface poi = (PortletManagementOperationsInterface) cont.getComponentInstanceOfType(PortletManagementOperationsInterface.class);
    try {
      GetPortletsLifetimeResponse resp = poi.getPortletsLifetime(registrationContext, portletContexts, userContext);
      PortletLifetime plf = resp.getPortletLifetime(0);
      Lifetime lf = plf.getScheduledDestruction();
      if (lf != null) {
        if (lf.getTerminationTime().getTimeInMillis() > lf.getCurrentTime().getTimeInMillis()) {
          String portletHandle = portletContexts[0].getPortletHandle();
          poi.destroyPortlets(registrationContext, new String[]{portletHandle});
          return false;
        }
      }
    } catch (RemoteException e) {
    }
    return true;
}

}
