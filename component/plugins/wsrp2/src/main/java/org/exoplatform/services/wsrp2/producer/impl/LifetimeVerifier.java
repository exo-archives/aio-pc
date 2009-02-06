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
package org.exoplatform.services.wsrp2.producer.impl;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
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
import org.exoplatform.services.wsrp2.producer.impl.helpers.LifetimeHelper;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $
 */
public class LifetimeVerifier {

  private final Log LOG = ExoLogger.getLogger(LifetimeVerifier.class);

  /**
   * Whether registration lifetime valid.
   * 
   * @param registrationContext
   * @param userContext
   * @throws InvalidRegistration
   * @throws ModifyRegistrationRequired
   * @throws OperationFailed
   * @throws ResourceSuspended
   * @throws OperationNotSupported
   * @throws AccessDenied
   * @throws WSRPException
   */
  public static void checkRegistrationLifetime(RegistrationContext registrationContext,
                                               UserContext userContext) throws InvalidRegistration,
                                                                       ModifyRegistrationRequired,
                                                                       OperationFailed,
                                                                       ResourceSuspended,
                                                                       OperationNotSupported,
                                                                       AccessDenied,
                                                                       WSRPException {
    if (!LifetimeHelper.checkRegistrationLifetime(registrationContext, userContext)) {
      throw new InvalidRegistration();
    }
  }

  /**
   * Whether portlet lifetime valid.
   * 
   * @param registrationContext
   * @param portletContext
   * @param userContext
   * @throws InvalidRegistration
   * @throws ModifyRegistrationRequired
   * @throws OperationFailed
   * @throws ResourceSuspended
   * @throws AccessDenied
   * @throws InconsistentParameters
   * @throws MissingParameters
   * @throws WSRPException
   */
  public static void checkPortletLifetime(RegistrationContext registrationContext,
                                          PortletContext portletContext,
                                          UserContext userContext) throws InvalidRegistration,
                                                                  ModifyRegistrationRequired,
                                                                  OperationFailed,
                                                                  ResourceSuspended,
                                                                  AccessDenied,
                                                                  InconsistentParameters,
                                                                  MissingParameters,
                                                                  WSRPException {
    try {
      if (!LifetimeHelper.checkPortletLifetime(registrationContext,
                                               Arrays.asList(new PortletContext[] { portletContext }),
                                               userContext)) {
        throw new InvalidRegistration();
      }
    } catch (InvalidRegistration ir) {
      throw ir;
    } catch (InvalidHandle ih) {
      throw new InvalidRegistration(ih.getMessage(), ih);
    } catch (OperationNotSupported ons) {
      throw new OperationFailed(ons.getMessage(), ons);
    }
  }

}
