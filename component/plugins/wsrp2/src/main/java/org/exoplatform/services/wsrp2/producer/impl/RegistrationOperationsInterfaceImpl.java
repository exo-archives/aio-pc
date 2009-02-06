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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp2.producer.impl.helpers.Helper;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class RegistrationOperationsInterfaceImpl implements RegistrationOperationsInterface {

  private Log                    log;

  private PersistentStateManager stateManager;

  public RegistrationOperationsInterfaceImpl(PersistentStateManager stateManager) {
    this.stateManager = stateManager;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
  }

  public RegistrationContext register(RegistrationData data,
                                      UserContext userContext,
                                      Lifetime lifetime) throws OperationNotSupported,
                                                        MissingParameters,
                                                        OperationFailed,
                                                        WSRPException {

    whetherLifetimeNullOrExpired(lifetime);

    String owner = null;
    if (userContext != null) {
      owner = userContext.getUserContextKey();
      if (log.isDebugEnabled())
        log.debug("Register method entered for user:" + owner);
    }
    String registrationHandle = null;
    byte[] registrationState = null;
    try {
      validateRegistrationDatas(data);
      registrationHandle = IdentifierUtil.generateUUID(data);
      registrationState = stateManager.register(registrationHandle, data);
      stateManager.putRegistrationLifetime(registrationHandle, lifetime);
    } catch (WSRPException e) {
      e.printStackTrace();
      if (log.isDebugEnabled())
        log.debug("Registration failed", e);
      throw new WSRPException();
    }
    RegistrationContext rC = new RegistrationContext();
    rC.setRegistrationHandle(registrationHandle);
    rC.setRegistrationState(registrationState);
    rC.setScheduledDestruction(lifetime);
    if (log.isDebugEnabled()) {
      log.debug("Registration done with handle : " + registrationHandle + " for owner : " + owner);
    }
    return rC;
  }

  public RegistrationState modifyRegistration(RegistrationContext registrationContext,
                                              RegistrationData data,
                                              UserContext userContext) throws OperationNotSupported,
                                                                      ResourceSuspended,
                                                                      InvalidRegistration,
                                                                      MissingParameters,
                                                                      OperationFailed,
                                                                      WSRPException {

    if (userContext != null) {
      String owner = userContext.getUserContextKey();
      if (log.isDebugEnabled())
        log.debug("Modify registrion method entered for owner " + owner);
    }
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        throw new InvalidRegistration();
      }
    } catch (WSRPException e) {
      throw new WSRPException();
    }
    String registrationHandle = registrationContext.getRegistrationHandle();
    try {
      validateRegistrationDatas(data);
      byte[] registrationState = stateManager.register(registrationHandle, data);
    } catch (WSRPException e) {
      if (log.isDebugEnabled())
        log.debug("Registration failed", e);
      throw new WSRPException();
    }
    return new RegistrationState();//the state is kept in the producer (not send to the consumer)
  }

  public ReturnAny deregister(RegistrationContext registrationContext, UserContext userContext) throws OperationNotSupported,
                                                                                               ResourceSuspended,
                                                                                               InvalidRegistration,
                                                                                               OperationFailed,
                                                                                               WSRPException {
    if (userContext != null) {
      String owner = userContext.getUserContextKey();
      if (log.isDebugEnabled())
        log.debug("Deregister method entered for owner:" + owner);
    }
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        throw new InvalidRegistration();
      }
    } catch (WSRPException e) {
      throw new WSRPException();
    }
    try {
      stateManager.deregister(registrationContext);
    } catch (WSRPException e) {
      if (log.isDebugEnabled())
        log.debug("Registration failed", e);
      throw new WSRPException();
    }
    return new ReturnAny();
  }

  public Lifetime getRegistrationLifetime(RegistrationContext registrationContext,
                                          UserContext userContext) throws OperationNotSupported,
                                                                  AccessDenied,
                                                                  ResourceSuspended,
                                                                  InvalidRegistration,
                                                                  InvalidHandle,
                                                                  ModifyRegistrationRequired,
                                                                  OperationFailed,
                                                                  WSRPException {
    if (userContext != null) {
      String owner = userContext.getUserContextKey();
      if (log.isDebugEnabled())
        log.debug("getRegistrationLifetime method entered for owner:" + owner);
    }
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        throw new InvalidRegistration();
      }
    } catch (WSRPException e) {
      throw new WSRPException();
    }
    Lifetime lifetime = null;
    try {
      lifetime = stateManager.getRegistrationLifetime(registrationContext);
    } catch (WSRPException e) {
      if (log.isDebugEnabled())
        log.debug("Get registration lifetime failed", e);
      throw new WSRPException();
    }
    return lifetime;
  }

  public Lifetime setRegistrationLifetime(RegistrationContext registrationContext,
                                          UserContext userContext,
                                          Lifetime lifetime) throws OperationNotSupported,
                                                            AccessDenied,
                                                            ResourceSuspended,
                                                            InvalidRegistration,
                                                            InvalidHandle,
                                                            ModifyRegistrationRequired,
                                                            OperationFailed,
                                                            WSRPException {
    whetherLifetimeNullOrExpired(lifetime);

    if (userContext != null) {
      String owner = userContext.getUserContextKey();
      if (log.isDebugEnabled())
        log.debug("getRegistrationLifetime method entered for owner:" + owner);
    }
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        throw new InvalidRegistration();
      }
    } catch (WSRPException e) {
      throw new WSRPException();
    }
    Lifetime resultLifetime = null;
    try {
      resultLifetime = stateManager.putRegistrationLifetime(registrationContext.getRegistrationHandle(),
                                                            lifetime);
    } catch (WSRPException e) {
      if (log.isDebugEnabled())
        log.debug("Get registration lifetime failed", e);
      throw new WSRPException();
    }
    return resultLifetime;
  }

  private void whetherLifetimeNullOrExpired(Lifetime lifetime) throws OperationFailed {
    if (lifetime != null && Helper.lifetimeExpired(lifetime))
      throw new OperationFailed("The provided lifetime has expired");
  }

  private void validateRegistrationDatas(RegistrationData data) throws WSRPException {
    String consumerAgent = data.getConsumerAgent();
    String[] members = StringUtils.split(consumerAgent, ".");
    if (!StringUtils.isNumeric(members[1])) {
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
    if (!StringUtils.isNumeric(members[2])) {
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
  }

}
