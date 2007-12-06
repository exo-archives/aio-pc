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

import java.rmi.RemoteException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.UserContext;

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
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
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

  
  public RegistrationContext register(RegistrationData data, Lifetime lifetime, UserContext userContext)
                                      throws RemoteException {

    // necessaire pour la verification de l'agent, pourquoi ?
    data.setConsumerAgent("exoplatform.1.0");
    String owner = userContext.getUserContextKey();
    log.debug("Register method entered for user:"+ owner);
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
    rC.setScheduledDestruction(lifetime);
    log.debug("Registration done with handle : " + registrationHandle +" for owner : " + owner);
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

  public RegistrationState modifyRegistration(RegistrationContext registrationContext,
                                              RegistrationData data,
                                              UserContext userContext)
  throws RemoteException {
    String owner = userContext.getUserContextKey();
    log.debug("Modify registrion method entered for owner " + owner);
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
    return new ReturnAny();
  }

  public ReturnAny deregister(RegistrationContext registrationContext, UserContext userContext)
                    throws RemoteException {
    String owner = userContext.getUserContextKey();
    log.debug("Deregister method entered for owner:" + owner);
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
    return new ReturnAny();
  }
  
  
  public Lifetime getRegistrationLifetime (RegistrationContext registrationContext,
                                           UserContext userContext)  
  throws RemoteException {

    try {
      if (!stateManager.isRegistered(registrationContext)) {
      Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    } catch (WSRPException e) {
    Exception2Fault.handleException(e);
    }
    String owner = userContext.getUserContextKey();
    log.debug("getRegistrationLifetime method entered for owner:" + owner);
    Lifetime lifetime =  registrationContext.getScheduledDestruction();
    return lifetime;
  }
  
    public Lifetime setRegistrationLifetime (RegistrationContext registrationContext,
                                           UserContext userContext, Lifetime lifetime)  
        throws RemoteException {
      try {
           if (!stateManager.isRegistered(registrationContext)) {
           Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
           }
       } catch (WSRPException e) {
         Exception2Fault.handleException(e);
       }
    
      String owner = userContext.getUserContextKey();
      log.debug("setRegistrationLifetime method entered for owner:" + owner);
      
//      if (lifetime == null)
//      {
//       log.debug("getRegistrationLifetime method failed entered for owner:" + owner + ", now deregistering");
//       deregister(registrationContext, userContext);
//      } else {
      registrationContext.setScheduledDestruction(lifetime);  
//      }
      return lifetime;

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
