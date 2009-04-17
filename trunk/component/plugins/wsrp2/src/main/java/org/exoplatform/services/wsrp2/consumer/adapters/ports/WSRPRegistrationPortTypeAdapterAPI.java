/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.services.wsrp2.consumer.adapters.ports;

import java.util.List;

import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.GetRegistrationLifetime;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.SetRegistrationLifetime;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public interface WSRPRegistrationPortTypeAdapterAPI {

  /**
   * Register.
   * 
   * @param register the register
   * 
   * @return the registration context
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws MissingParameters the missing parameters
   * @throws OperationFailed the operation failed
   */
  public RegistrationContext register(Register register) throws OperationNotSupported,
                                                        MissingParameters,
                                                        OperationFailed;

  /**
   * Deregister.
   * 
   * @param deregister the deregister
   * 
   * @return the list< extension>
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws OperationFailed the operation failed
   */
  public List<Extension> deregister(Deregister deregister) throws OperationNotSupported,
                                                    ResourceSuspended,
                                                    InvalidRegistration,
                                                    OperationFailed;

  /**
   * Modify registration.
   * 
   * @param modifyRegistration the modify registration
   * 
   * @return the registration state
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws MissingParameters the missing parameters
   * @throws OperationFailed the operation failed
   */
  public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws OperationNotSupported,
                                                                                    ResourceSuspended,
                                                                                    InvalidRegistration,
                                                                                    MissingParameters,
                                                                                    OperationFailed;

  /**
   * Gets the registration lifetime.
   * 
   * @param getRegistrationLifetime the get registration lifetime
   * 
   * @return the registration lifetime
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws OperationFailed the operation failed
   */
  public Lifetime getRegistrationLifetime(GetRegistrationLifetime getRegistrationLifetime) throws OperationNotSupported,
                                                                                          AccessDenied,
                                                                                          ResourceSuspended,
                                                                                          InvalidRegistration,
                                                                                          InvalidHandle,
                                                                                          ModifyRegistrationRequired,
                                                                                          OperationFailed;

  /**
   * Sets the registration lifetime.
   * 
   * @param setRegistrationLifetime the set registration lifetime
   * 
   * @return the lifetime
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws OperationFailed the operation failed
   */
  public Lifetime setRegistrationLifetime(SetRegistrationLifetime setRegistrationLifetime) throws OperationNotSupported,
                                                                                          AccessDenied,
                                                                                          ResourceSuspended,
                                                                                          InvalidRegistration,
                                                                                          InvalidHandle,
                                                                                          ModifyRegistrationRequired,
                                                                                          OperationFailed;
}
