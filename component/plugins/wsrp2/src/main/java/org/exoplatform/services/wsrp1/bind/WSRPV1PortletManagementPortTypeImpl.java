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
import org.exoplatform.services.wsrp.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType;
import org.exoplatform.services.wsrp1.type.AccessDeniedFault;
import org.exoplatform.services.wsrp1.type.ClonePortletRequest;
import org.exoplatform.services.wsrp1.type.DestroyPortletsRequest;
import org.exoplatform.services.wsrp1.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp1.type.GetPortletPropertiesRequest;
import org.exoplatform.services.wsrp1.type.InconsistentParametersFault;
import org.exoplatform.services.wsrp1.type.InvalidHandleFault;
import org.exoplatform.services.wsrp1.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp1.type.InvalidUserCategoryFault;
import org.exoplatform.services.wsrp1.type.MissingParametersFault;
import org.exoplatform.services.wsrp1.type.OperationFailedFault;
import org.exoplatform.services.wsrp1.type.PortletContext;
import org.exoplatform.services.wsrp1.type.PortletDescriptionRequest;
import org.exoplatform.services.wsrp1.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp1.type.PortletPropertyDescriptionRequest;
import org.exoplatform.services.wsrp1.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp1.type.PropertyList;
import org.exoplatform.services.wsrp1.type.SetPortletPropertiesRequest;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPV1PortletManagementPortTypeImpl implements
    WSRPV1PortletManagementPortType {

  private PortletManagementOperationsInterface portletManagementOperationsInterface;

  public WSRPV1PortletManagementPortTypeImpl() {
    portletManagementOperationsInterface = (PortletManagementOperationsInterface) ExoContainerContext.getCurrentContainer()
                                                                                                     .getComponentInstanceOfType(PortletManagementOperationsInterface.class);
  }

  public PortletDescriptionResponse getPortletDescription(PortletDescriptionRequest getPortletDescription) throws RemoteException,
                                                                                                          InvalidUserCategoryFault,
                                                                                                          InconsistentParametersFault,
                                                                                                          InvalidRegistrationFault,
                                                                                                          OperationFailedFault,
                                                                                                          MissingParametersFault,
                                                                                                          AccessDeniedFault,
                                                                                                          InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletDescription(getPortletDescription.getRegistrationContext(),
                                                                      getPortletDescription.getPortletContext(),
                                                                      getPortletDescription.getUserContext(),
                                                                      getPortletDescription.getDesiredLocales());
  }

  public PortletContext clonePortlet(ClonePortletRequest clonePortlet) throws RemoteException,
                                                                      InvalidUserCategoryFault,
                                                                      InconsistentParametersFault,
                                                                      InvalidRegistrationFault,
                                                                      OperationFailedFault,
                                                                      MissingParametersFault,
                                                                      AccessDeniedFault,
                                                                      InvalidHandleFault {
    return portletManagementOperationsInterface.clonePortlet(clonePortlet.getRegistrationContext(),
                                                             clonePortlet.getPortletContext(),
                                                             clonePortlet.getUserContext());
  }

  public DestroyPortletsResponse destroyPortlets(DestroyPortletsRequest destroyPortlets) throws RemoteException,
                                                                                        InconsistentParametersFault,
                                                                                        InvalidRegistrationFault,
                                                                                        OperationFailedFault,
                                                                                        MissingParametersFault {
    return portletManagementOperationsInterface.destroyPortlets(destroyPortlets.getRegistrationContext(),
                                                                destroyPortlets.getPortletHandles());
  }

  public PortletContext setPortletProperties(SetPortletPropertiesRequest setPortletProperties) throws RemoteException,
                                                                                              InvalidUserCategoryFault,
                                                                                              InconsistentParametersFault,
                                                                                              InvalidRegistrationFault,
                                                                                              OperationFailedFault,
                                                                                              MissingParametersFault,
                                                                                              AccessDeniedFault,
                                                                                              InvalidHandleFault {
    return portletManagementOperationsInterface.setPortletProperties(setPortletProperties.getRegistrationContext(),
                                                                     setPortletProperties.getPortletContext(),
                                                                     setPortletProperties.getUserContext(),
                                                                     setPortletProperties.getPropertyList());
  }

  public PropertyList getPortletProperties(GetPortletPropertiesRequest getPortletProperties) throws RemoteException,
                                                                                            InvalidUserCategoryFault,
                                                                                            InconsistentParametersFault,
                                                                                            InvalidRegistrationFault,
                                                                                            OperationFailedFault,
                                                                                            MissingParametersFault,
                                                                                            AccessDeniedFault,
                                                                                            InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletProperties(getPortletProperties.getRegistrationContext(),
                                                                     getPortletProperties.getPortletContext(),
                                                                     getPortletProperties.getUserContext(),
                                                                     getPortletProperties.getNames());
  }

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(PortletPropertyDescriptionRequest getPortletPropertyDescription) throws RemoteException,
                                                                                                                                          InvalidUserCategoryFault,
                                                                                                                                          InconsistentParametersFault,
                                                                                                                                          InvalidRegistrationFault,
                                                                                                                                          OperationFailedFault,
                                                                                                                                          MissingParametersFault,
                                                                                                                                          AccessDeniedFault,
                                                                                                                                          InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletPropertyDescription(getPortletPropertyDescription.getRegistrationContext(),
                                                                              getPortletPropertyDescription.getPortletContext(),
                                                                              getPortletPropertyDescription.getUserContext(),
                                                                              getPortletPropertyDescription.getDesiredLocales());
  }

}
