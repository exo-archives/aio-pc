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

package org.exoplatform.services.wsrp1.bind;

import java.rmi.RemoteException;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType;
import org.exoplatform.services.wsrp1.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp1.type.MissingParametersFault;
import org.exoplatform.services.wsrp1.type.ModifyRegistrationRequest;
import org.exoplatform.services.wsrp1.type.OperationFailedFault;
import org.exoplatform.services.wsrp1.type.RegistrationContext;
import org.exoplatform.services.wsrp1.type.RegistrationData;
import org.exoplatform.services.wsrp1.type.RegistrationState;
import org.exoplatform.services.wsrp1.type.ReturnAny;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPV1RegistrationPortTypeImpl implements WSRPV1RegistrationPortType {

  private RegistrationOperationsInterface registrationOperationsInterface;

  public WSRPV1RegistrationPortTypeImpl() {
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
