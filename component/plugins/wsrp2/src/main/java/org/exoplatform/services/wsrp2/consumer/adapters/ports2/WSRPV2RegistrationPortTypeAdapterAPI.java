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
package org.exoplatform.services.wsrp2.consumer.adapters.ports2;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPRegistrationPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType;
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
public class WSRPV2RegistrationPortTypeAdapterAPI implements WSRPRegistrationPortTypeAdapterAPI {

  private WSRPV2RegistrationPortType registrationPort;

  private static final Log           LOG = ExoLogger.getLogger(WSRPV2RegistrationPortTypeAdapterAPI.class);

  public WSRPV2RegistrationPortTypeAdapterAPI(WSRPV2RegistrationPortType registrationPort) {
    this.registrationPort = registrationPort;
  }

  public RegistrationContext register(Register register) throws OperationNotSupported,
                                                        MissingParameters,
                                                        OperationFailed {
    System.out.println("Invoking register...");
    return registrationPort.register(register);
  }

  public Extension deregister(Deregister deregister) throws OperationNotSupported,
                                                    ResourceSuspended,
                                                    InvalidRegistration,
                                                    OperationFailed {
    System.out.println("Invoking deregister...");
    return registrationPort.deregister(deregister.getRegistrationContext(),
                                       deregister.getUserContext());
  }

  public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws OperationNotSupported,
                                                                                    ResourceSuspended,
                                                                                    InvalidRegistration,
                                                                                    MissingParameters,
                                                                                    OperationFailed {

    System.out.println("Invoking modifyRegistration...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _modifyRegistration_registrationContext = modifyRegistration.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.RegistrationData _modifyRegistration_registrationData = modifyRegistration.getRegistrationData();
    org.exoplatform.services.wsrp2.type.UserContext _modifyRegistration_userContext = modifyRegistration.getUserContext();
    javax.xml.ws.Holder<byte[]> _modifyRegistration_registrationState = new javax.xml.ws.Holder<byte[]>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> _modifyRegistration_scheduledDestruction = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _modifyRegistration_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    registrationPort.modifyRegistration(_modifyRegistration_registrationContext,
                                        _modifyRegistration_registrationData,
                                        _modifyRegistration_userContext,
                                        _modifyRegistration_registrationState,
                                        _modifyRegistration_scheduledDestruction,
                                        _modifyRegistration_extensions);

    System.out.println("modifyRegistration._modifyRegistration_registrationState="
        + _modifyRegistration_registrationState.value);
    System.out.println("modifyRegistration._modifyRegistration_scheduledDestruction="
        + _modifyRegistration_scheduledDestruction.value);
    System.out.println("modifyRegistration._modifyRegistration_extensions="
        + _modifyRegistration_extensions.value);

    RegistrationState registrationState = new RegistrationState();
    registrationState.setRegistrationState(_modifyRegistration_registrationState.value);
    registrationState.setScheduledDestruction(_modifyRegistration_scheduledDestruction.value);
    if (_modifyRegistration_extensions.value != null)
      registrationState.getExtensions().addAll(_modifyRegistration_extensions.value);
    return registrationState;
  }

  public Lifetime getRegistrationLifetime(GetRegistrationLifetime getRegistrationLifetime) throws OperationNotSupported,
                                                                                          AccessDenied,
                                                                                          ResourceSuspended,
                                                                                          InvalidRegistration,
                                                                                          InvalidHandle,
                                                                                          ModifyRegistrationRequired,
                                                                                          OperationFailed {
    System.out.println("Invoking getRegistrationLifetime...");
    return registrationPort.getRegistrationLifetime(getRegistrationLifetime);
  }

  public Lifetime setRegistrationLifetime(SetRegistrationLifetime setRegistrationLifetime) throws OperationNotSupported,
                                                                                          AccessDenied,
                                                                                          ResourceSuspended,
                                                                                          InvalidRegistration,
                                                                                          InvalidHandle,
                                                                                          ModifyRegistrationRequired,
                                                                                          OperationFailed {
    System.out.println("Invoking setRegistrationLifetime...");
    return registrationPort.setRegistrationLifetime(setRegistrationLifetime);
  }

}
