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

import java.util.Arrays;
import java.util.List;

import javax.xml.datatype.DatatypeConstants;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InconsistentParameters;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp2.producer.RegistrationOperationsInterface;
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

  public static boolean checkLifetime(RegistrationContext registrationContext,
                                      UserContext userContext) throws OperationNotSupported,
                                                              AccessDenied,
                                                              ResourceSuspended,
                                                              InvalidRegistration,
                                                              InvalidHandle,
                                                              ModifyRegistrationRequired,
                                                              OperationFailed,
                                                              WSRPException {
    ExoContainer cont = ExoContainerContext.getCurrentContainer();
    if (registrationContext == null)
      return true;
    RegistrationOperationsInterface roi = (RegistrationOperationsInterface) cont.getComponentInstanceOfType(RegistrationOperationsInterface.class);
    Lifetime lf = roi.getRegistrationLifetime(registrationContext, userContext);
    if (lf != null) {
      if (lifetimeExpired(lf)) {
        roi.deregister(registrationContext, userContext);
        return false;
      }
    }
    return true;
  }

  public static boolean lifetimeExpired(Lifetime lf) {
    if (lf != null)
      return DatatypeConstants.LESSER == lf.getTerminationTime().compare(lf.getCurrentTime());
    else
      return false;
  }

  public static boolean checkPortletLifetime(RegistrationContext registrationContext,
                                             List<PortletContext> portletContexts,
                                             UserContext userContext) throws OperationNotSupported,
                                                                     AccessDenied,
                                                                     ResourceSuspended,
                                                                     InvalidRegistration,
                                                                     InvalidHandle,
                                                                     ModifyRegistrationRequired,
                                                                     InconsistentParameters,
                                                                     OperationFailed,
                                                                     MissingParameters,
                                                                     WSRPException {
    ExoContainer cont = ExoContainerContext.getCurrentContainer();
    PortletManagementOperationsInterface pmoi = (PortletManagementOperationsInterface) cont.getComponentInstanceOfType(PortletManagementOperationsInterface.class);

    if (registrationContext == null)
      return true;
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
            pmoi.destroyPortlets(registrationContext,
                                 Arrays.asList(new String[] { portletHandle }),
                                 userContext);
            return false;
          }
        }
      }
    }
    return true;
  }

//  private void checkRegistrationContext(RegistrationContext registrationContext) throws InvalidRegistration {
//    ExoContainer cont = ExoContainerContext.getCurrentContainer();
//    WSRPConfiguration conf = (WSRPConfiguration) cont.getComponentInstanceOfType(WSRPConfiguration.class);
//    if (conf.isRegistrationRequired()) {
//      if (registrationContext == null) {
//        throw new InvalidRegistration();
//      }
//    }
//  }

}
