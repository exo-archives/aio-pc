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

package org.exoplatform.services.wsrp2.producer;

import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * The Interface RegistrationOperationsInterface.
 * 
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface RegistrationOperationsInterface {

  /**
   * Register.
   * 
   * @param data the data
   * @param userContext the user context
   * @param lifetime the lifetime
   * 
   * @return the registration context
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws MissingParameters the missing parameters
   * @throws OperationFailed the operation failed
   * @throws WSRPException the WSRP exception
   */
  public RegistrationContext register(RegistrationData data,
                                      UserContext userContext,
                                      Lifetime lifetime) throws OperationNotSupported,
                                                        MissingParameters,
                                                        OperationFailed,
                                                        WSRPException;

  /**
   * Modify registration.
   * 
   * @param context the context
   * @param data the data
   * @param userContext the user context
   * 
   * @return the registration state
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws MissingParameters the missing parameters
   * @throws OperationFailed the operation failed
   * @throws WSRPException the WSRP exception
   */
  public RegistrationState modifyRegistration(RegistrationContext context,
                                              RegistrationData data,
                                              UserContext userContext) throws OperationNotSupported,
                                                                      ResourceSuspended,
                                                                      InvalidRegistration,
                                                                      MissingParameters,
                                                                      OperationFailed,
                                                                      WSRPException;

  /**
   * Deregister.
   * 
   * @param context the context
   * @param userContext the user context
   * 
   * @return the return any
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws OperationFailed the operation failed
   * @throws WSRPException the WSRP exception
   */
  public ReturnAny deregister(RegistrationContext context, UserContext userContext) throws OperationNotSupported,
                                                                                   ResourceSuspended,
                                                                                   InvalidRegistration,
                                                                                   OperationFailed,
                                                                                   WSRPException;

  /**
   * Gets the registration lifetime.
   * 
   * @param registrationContext the registration context
   * @param userContext the user context
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
   * @throws WSRPException the WSRP exception
   */
  public Lifetime getRegistrationLifetime(RegistrationContext registrationContext,
                                          UserContext userContext) throws OperationNotSupported,
                                                                  AccessDenied,
                                                                  ResourceSuspended,
                                                                  InvalidRegistration,
                                                                  InvalidHandle,
                                                                  ModifyRegistrationRequired,
                                                                  OperationFailed,
                                                                  WSRPException;

  /**
   * Sets the registration lifetime.
   * 
   * @param registrationContext the registration context
   * @param userContext the user context
   * @param lifetime the lifetime
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
   * @throws WSRPException the WSRP exception
   */
  public Lifetime setRegistrationLifetime(RegistrationContext registrationContext,
                                          UserContext userContext,
                                          Lifetime lifetime) throws OperationNotSupported,
                                                            AccessDenied,
                                                            ResourceSuspended,
                                                            InvalidRegistration,
                                                            InvalidHandle,
                                                            ModifyRegistrationRequired,
                                                            OperationFailed,
                                                            WSRPException;
}
