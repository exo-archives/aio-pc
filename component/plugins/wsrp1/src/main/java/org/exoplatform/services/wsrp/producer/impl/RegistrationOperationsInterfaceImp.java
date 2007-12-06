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

package org.exoplatform.services.wsrp.producer.impl;

import java.rmi.RemoteException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;
import org.exoplatform.services.wsrp.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.RegistrationData;
import org.exoplatform.services.wsrp.type.RegistrationState;
import org.exoplatform.services.wsrp.type.ReturnAny;

/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class RegistrationOperationsInterfaceImp
    implements RegistrationOperationsInterface {

  private Log log;

  private PersistentStateManager stateManager;

  public RegistrationOperationsInterfaceImp(PersistentStateManager stateManager) {
    this.stateManager = stateManager;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp");
  }

  public RegistrationContext register(RegistrationData data)
      throws RemoteException {
    
	  //necessaire pour la verification de l'agent, pourquoi ?
	  data.setConsumerAgent("exoplatform.1.0");
	  
	  
	log.debug("Register method entered");
    String registrationHandle = null;
    byte[] registrationState = null;
    try {
      validateRegistrationDatas(data);
      registrationHandle = IdentifierUtil.generateUUID(data);
      registrationState = stateManager.register(registrationHandle, data);//may be null
    } catch (WSRPException e) {
      log.debug("Registration failed", e);
      Exception2Fault.handleException(e);
    }
    RegistrationContext rC = new RegistrationContext();
    rC.setRegistrationHandle(registrationHandle);
    rC.setRegistrationState(registrationState);
    log.debug("Registration done with handle : " + registrationHandle);
    return rC;
  }

  public RegistrationState modifyRegistration(RegistrationContext registrationContext,
                                              RegistrationData data)
      throws RemoteException {
    log.debug("Modify registrion method entered");
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    String registrationHandle = registrationContext.getRegistrationHandle();
    try {
      validateRegistrationDatas(data);
      stateManager.register(registrationHandle, data);
    } catch (WSRPException e) {
      log.debug("Registration failed", e);
      Exception2Fault.handleException(e);
    }

    return new RegistrationState();//the state is kept in the producer (not send to the consumer)
  }

  public ReturnAny deregister(RegistrationContext registrationContext)
      throws RemoteException {
    log.debug("Deregister method entered");
    ReturnAny any = null;
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    try {
      stateManager.deregister(registrationContext);
    } catch (WSRPException e) {
      log.debug("Registration failed", e);
      Exception2Fault.handleException(e);
    }
    return any;
  }

  private void validateRegistrationDatas(RegistrationData data)
      throws WSRPException {
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
