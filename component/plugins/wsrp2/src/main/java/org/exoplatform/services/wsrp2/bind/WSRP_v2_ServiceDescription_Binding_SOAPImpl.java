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
import org.exoplatform.services.wsrp2.intf.WSRP_v2_ServiceDescription_PortType;
import org.exoplatform.services.wsrp2.producer.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp2.type.ModifyRegistrationRequiredFault;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.ResourceSuspendedFault;
import org.exoplatform.services.wsrp2.type.ServiceDescription;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */

public class WSRP_v2_ServiceDescription_Binding_SOAPImpl implements
    WSRP_v2_ServiceDescription_PortType {

  private ServiceDescriptionInterface serviceDescriptionInterface;

  public WSRP_v2_ServiceDescription_Binding_SOAPImpl() {
    serviceDescriptionInterface = (ServiceDescriptionInterface) ExoContainerContext.getCurrentContainer()
                                                                                   .getComponentInstanceOfType(ServiceDescriptionInterface.class);
  }

  public ServiceDescription getServiceDescription(GetServiceDescription getServiceDescription) throws RemoteException,
                                                                                              ResourceSuspendedFault,
                                                                                              ModifyRegistrationRequiredFault,
                                                                                              InvalidRegistrationFault,
                                                                                              OperationFailedFault {
    return serviceDescriptionInterface.getServiceDescription(getServiceDescription.getRegistrationContext(),
                                                             getServiceDescription.getDesiredLocales());
  }

}
