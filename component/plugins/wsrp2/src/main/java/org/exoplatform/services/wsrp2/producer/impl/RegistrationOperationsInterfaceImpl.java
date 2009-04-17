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
import org.exoplatform.services.wsrp2.producer.impl.helpers.LifetimeHelper;
import org.exoplatform.services.wsrp2.producer.impl.utils.CalendarUtils;
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

  private static final Log       LOG = ExoLogger.getLogger(RegistrationOperationsInterfaceImpl.class);

  private PersistentStateManager stateManager;

  public RegistrationOperationsInterfaceImpl(PersistentStateManager stateManager) {
    this.stateManager = stateManager;
  }

  public RegistrationContext register(RegistrationData data,
                                      UserContext userContext,
                                      Lifetime lifetime) throws OperationNotSupported,
                                                        MissingParameters,
                                                        OperationFailed,
                                                        WSRPException {

    whetherLifetimeIsExpired(lifetime);

    String owner = null;
    if (userContext != null) {
      owner = userContext.getUserContextKey();
      if (LOG.isDebugEnabled())
        LOG.debug("Register method entered for user:" + owner);
    }
    String registrationHandle = null;
    byte[] registrationState = null;
    Lifetime returnedLifetime = null;

    validateRegistrationDatas(data);

    // Below we are creating registrationHandle
    registrationHandle = createRegistrationHandle(data);
    registrationState = stateManager.register(registrationHandle, data);
    returnedLifetime = stateManager.putRegistrationLifetime(registrationHandle, lifetime);

    RegistrationContext rC = new RegistrationContext();
    rC.setRegistrationHandle(registrationHandle);
    rC.setRegistrationState(registrationState);
    rC.setScheduledDestruction(returnedLifetime);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Registration done with handle : '" + registrationHandle + "' for owner : '"
          + owner + "'");
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
      if (LOG.isDebugEnabled())
        LOG.debug("Modify registrion method entered for owner " + owner);
    }

    verifyRegistration(registrationContext);

    String registrationHandle = registrationContext.getRegistrationHandle();

    validateRegistrationDatas(data);
    byte[] regState = stateManager.register(registrationHandle, data);

    Lifetime lifetime = stateManager.getRegistrationLifetime(registrationContext);

    RegistrationState registrationState = new RegistrationState();
    registrationState.setRegistrationState(regState);
    registrationState.setScheduledDestruction(lifetime);
    return registrationState;
  }

  public ReturnAny deregister(RegistrationContext registrationContext, UserContext userContext) throws OperationNotSupported,
                                                                                               ResourceSuspended,
                                                                                               InvalidRegistration,
                                                                                               OperationFailed,
                                                                                               WSRPException {
    if (userContext != null) {
      String owner = userContext.getUserContextKey();
      if (LOG.isDebugEnabled())
        LOG.debug("Deregister method entered for owner:" + owner);
    }

    verifyRegistration(registrationContext);

    stateManager.deregister(registrationContext);

    return new ReturnAny();
  }

  /**
   * Throws InvalidRegistration when unregistered. Lifetime is null when it
   * doesn't provided for registration.
   */
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
      if (LOG.isDebugEnabled())
        LOG.debug("getRegistrationLifetime method entered for owner:" + owner);
    }

    verifyRegistration(registrationContext);

    Lifetime lifetime = null;
    try {
      lifetime = stateManager.getRegistrationLifetime(registrationContext);
      if (lifetime != null)
        lifetime.setCurrentTime(CalendarUtils.getNow());
    } catch (WSRPException e) {
      if (LOG.isDebugEnabled())
        LOG.debug("Get registration lifetime failed", e);
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
    whetherLifetimeIsExpired(lifetime);

    if (userContext != null) {
      String owner = userContext.getUserContextKey();
      if (LOG.isDebugEnabled())
        LOG.debug("getRegistrationLifetime method entered for owner:" + owner);
    }

    verifyRegistration(registrationContext);

    Lifetime resultLifetime = null;
    try {
      resultLifetime = stateManager.putRegistrationLifetime(registrationContext.getRegistrationHandle(),
                                                            lifetime);
    } catch (WSRPException e) {
      if (LOG.isDebugEnabled())
        LOG.debug("Get registration lifetime failed", e);
      throw new WSRPException();
    }
    return resultLifetime;
  }

  private void verifyRegistration(RegistrationContext registrationContext) throws InvalidRegistration,
                                                                          WSRPException {
    if (registrationContext == null || !stateManager.isRegistered(registrationContext)) {
      throw new InvalidRegistration("Producer with this registrationContext = '"
          + registrationContext + "' doesn't registered");
    }
  }

  private void validateRegistrationDatas(RegistrationData data) throws WSRPException {
    String consumerAgent = data.getConsumerAgent();
    if (consumerAgent == null || consumerAgent.length() == 0) {
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
  }

  private void validateConsumerAgent(String consumerAgent) throws WSRPException {
    String[] members = StringUtils.split(consumerAgent, ".");
    if (!StringUtils.isNumeric(members[1])) {
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
    if (!StringUtils.isNumeric(members[2])) {
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
  }

  /**
   * If lifetime is expired throws OperationFailed. For the methods 'register'
   * and 'setRegistrationLifetime'.
   * 
   * @param lifetime
   * @throws OperationFailed
   */
  private void whetherLifetimeIsExpired(Lifetime lifetime) throws OperationFailed {
    if (lifetime != null && LifetimeHelper.lifetimeExpired(lifetime))
      throw new OperationFailed("The provided lifetime has expired");
  }

  private String createRegistrationHandle(RegistrationData data) {
    return IdentifierUtil.generateUUID(data);
  }

}
