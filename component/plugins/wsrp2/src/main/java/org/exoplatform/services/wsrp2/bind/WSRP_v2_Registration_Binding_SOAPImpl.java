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

package org.exoplatform.services.wsrp2.bind;

import java.rmi.RemoteException;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_Registration_PortType;
import org.exoplatform.services.wsrp2.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.type.GetRegistrationLifetime;
import org.exoplatform.services.wsrp2.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.MissingParametersFault;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.OperationNotSupportedFault;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ResourceSuspendedFault;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.SetRegistrationLifetime;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRP_v2_Registration_Binding_SOAPImpl implements WSRP_v2_Registration_PortType {

  private RegistrationOperationsInterface registrationOperationsInterface;

  public WSRP_v2_Registration_Binding_SOAPImpl() {
    registrationOperationsInterface = (RegistrationOperationsInterface) ExoContainerContext.getCurrentContainer()
                                                                                           .getComponentInstanceOfType(RegistrationOperationsInterface.class);
  }

  public RegistrationContext register(Register register) throws RemoteException,
                                                        OperationFailedFault,
                                                        MissingParametersFault {
    return registrationOperationsInterface.register(register.getRegistrationData(),
                                                    register.getUserContext(),
                                                    register.getLifetime());
  }

  public ReturnAny deregister(Deregister deregister) throws RemoteException,
                                                    InvalidRegistrationFault,
                                                    OperationFailedFault {
    return registrationOperationsInterface.deregister(deregister.getRegistrationContext(),
                                                      deregister.getUserContext());
  }

  public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws RemoteException,
                                                                                    ResourceSuspendedFault,
                                                                                    InvalidRegistrationFault,
                                                                                    OperationFailedFault,
                                                                                    MissingParametersFault,
                                                                                    OperationNotSupportedFault {
    return registrationOperationsInterface.modifyRegistration(modifyRegistration.getRegistrationContext(),
                                                              modifyRegistration.getRegistrationData(),
                                                              modifyRegistration.getUserContext());
  }

  public Lifetime getRegistrationLifetime(GetRegistrationLifetime getRegistrationLifetime) throws RemoteException,
                                                                                          InvalidRegistrationFault,
                                                                                          OperationFailedFault {
    return registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime.getRegistrationContext(),
                                                                   getRegistrationLifetime.getUserContext());
  }

  public Lifetime setRegistrationLifetime(SetRegistrationLifetime setRegistrationLifetime) throws RemoteException,
                                                                                          InvalidRegistrationFault,
                                                                                          OperationFailedFault {
    return registrationOperationsInterface.setRegistrationLifetime(setRegistrationLifetime.getRegistrationContext(),
                                                                   setRegistrationLifetime.getUserContext(),
                                                                   setRegistrationLifetime.getLifetime());
  }

}
