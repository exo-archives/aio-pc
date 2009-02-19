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

package org.exoplatform.services.wsrp.bind;

import java.rmi.RemoteException;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp.intf.WSRP_v1_Registration_PortType;
import org.exoplatform.services.wsrp.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp.type.MissingParametersFault;
import org.exoplatform.services.wsrp.type.ModifyRegistrationRequest;
import org.exoplatform.services.wsrp.type.OperationFailedFault;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.RegistrationData;
import org.exoplatform.services.wsrp.type.RegistrationState;
import org.exoplatform.services.wsrp.type.ReturnAny;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRP_v1_Registration_Binding_SOAPImpl implements WSRP_v1_Registration_PortType {

  private RegistrationOperationsInterface registrationOperationsInterface;

  public WSRP_v1_Registration_Binding_SOAPImpl() {
    registrationOperationsInterface = (RegistrationOperationsInterface) ExoContainerContext.getCurrentContainer()
                                                                                           .getComponentInstanceOfType(RegistrationOperationsInterface.class);
  }

  public RegistrationContext register(RegistrationData data) throws RemoteException,
                                                            OperationFailedFault,
                                                            MissingParametersFault {
    return registrationOperationsInterface.register(data);
  }

  public ReturnAny deregister(RegistrationContext context) throws RemoteException,
                                                          InvalidRegistrationFault,
                                                          OperationFailedFault {
    return registrationOperationsInterface.deregister(context);
  }

  public RegistrationState modifyRegistration(ModifyRegistrationRequest modifyRegistration) throws RemoteException,
                                                                                           InvalidRegistrationFault,
                                                                                           OperationFailedFault,
                                                                                           MissingParametersFault {
    return registrationOperationsInterface.modifyRegistration(modifyRegistration.getRegistrationContext(),
                                                              modifyRegistration.getRegistrationData());
  }

}
