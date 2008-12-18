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
import org.exoplatform.services.wsrp.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp1.intf.WSRPV1ServiceDescriptionPortType;
import org.exoplatform.services.wsrp1.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp1.type.OperationFailedFault;
import org.exoplatform.services.wsrp1.type.ServiceDescription;
import org.exoplatform.services.wsrp1.type.ServiceDescriptionRequest;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPV1ServiceDescriptionPortTypeImpl implements
    WSRPV1ServiceDescriptionPortType {

  private ServiceDescriptionInterface serviceDescriptionInterface;

  public WSRPV1ServiceDescriptionPortTypeImpl() {
    serviceDescriptionInterface = (ServiceDescriptionInterface) ExoContainerContext.getCurrentContainer()
                                                                                   .getComponentInstanceOfType(ServiceDescriptionInterface.class);
  }

  public ServiceDescription getServiceDescription(ServiceDescriptionRequest getServiceDescription) throws RemoteException,
                                                                                                  InvalidRegistrationFault,
                                                                                                  OperationFailedFault {
    return serviceDescriptionInterface.getServiceDescription(getServiceDescription.getRegistrationContext(),
                                                             getServiceDescription.getDesiredLocales());
  }

}
