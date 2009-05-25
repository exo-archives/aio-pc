/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

import java.util.List;

import javax.xml.datatype.DatatypeConstants;

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
import org.exoplatform.services.wsrp2.producer.impl.utils.CalendarUtils;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletLifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.picocontainer.Startable;

/**
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
 */
public class LifetimeHelper implements Startable {

  private static RegistrationOperationsInterface      roi;

  private static PortletManagementOperationsInterface pmoi;

  public LifetimeHelper(RegistrationOperationsInterface roi,
                        PortletManagementOperationsInterface pmoi) {
    LifetimeHelper.roi = roi;
    LifetimeHelper.pmoi = pmoi;
  }

  /**
   * Throws InvalidRegistration when unregistered. Return false when lifetime is
   * expired.
   */
  public static boolean checkRegistrationLifetime(RegistrationContext registrationContext,
                                                  UserContext userContext) throws OperationNotSupported,
                                                                          AccessDenied,
                                                                          ResourceSuspended,
                                                                          InvalidRegistration,
                                                                          ModifyRegistrationRequired,
                                                                          OperationFailed,
                                                                          WSRPException {
    if (registrationContext == null)
      return true;
    try {

      Lifetime lf = roi.getRegistrationLifetime(registrationContext, userContext);
      if (lf != null && registrationContext.getScheduledDestruction() != null) {
        lf.setCurrentTime(CalendarUtils.getNow());
        if (lifetimeExpired(lf)) {
         roi.deregister(registrationContext, userContext);
          return false;
        }
      }
    } catch (InvalidHandle ih) {
      throw new OperationFailed(ih.getMessage(), ih);
    }

    return true;
  }

  public static boolean lifetimeExpired(Lifetime lf) {
    if (lf != null)
      return DatatypeConstants.LESSER == lf.getTerminationTime().compare(lf.getCurrentTime());
    else
      return false;
  }

  /**
   * @param registrationContext
   * @param portletContexts
   * @param userContext
   * @return
   * @throws OperationNotSupported
   * @throws AccessDenied
   * @throws ResourceSuspended
   * @throws InvalidRegistration
   * @throws InvalidHandle
   * @throws ModifyRegistrationRequired
   * @throws InconsistentParameters
   * @throws OperationFailed
   * @throws MissingParameters
   * @throws WSRPException
   */
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
    if (registrationContext == null)
      return true;
    GetPortletsLifetimeResponse resp = pmoi.getPortletsLifetime(registrationContext,
                                                                portletContexts,
                                                                userContext);
    if (resp != null && resp.getPortletLifetime() != null && resp.getPortletLifetime().size() != 0) {
      PortletLifetime plf = resp.getPortletLifetime().get(0);
      Lifetime lf = plf.getScheduledDestruction();
      if (lf != null && registrationContext.getScheduledDestruction() != null) {
        lf.setCurrentTime(CalendarUtils.getNow());
        if (lifetimeExpired(lf)) {
//            String portletHandle = portletContexts.get(0).getPortletHandle();
//            pmoi.destroyPortlets(registrationContext,
//                                 Arrays.asList(new String[] { portletHandle }),
//                                 userContext);
          return false;
        }
      }
    }
    return true;
  }

  public void start() {
  }

  public void stop() {
  }
}
