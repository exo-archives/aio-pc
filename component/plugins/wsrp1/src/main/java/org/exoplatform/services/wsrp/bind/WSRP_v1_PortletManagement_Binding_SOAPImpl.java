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
import org.exoplatform.services.wsrp.intf.WSRP_v1_PortletManagement_PortType;
import org.exoplatform.services.wsrp.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp.type.AccessDeniedFault;
import org.exoplatform.services.wsrp.type.ClonePortletRequest;
import org.exoplatform.services.wsrp.type.DestroyPortletsRequest;
import org.exoplatform.services.wsrp.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp.type.GetPortletPropertiesRequest;
import org.exoplatform.services.wsrp.type.InconsistentParametersFault;
import org.exoplatform.services.wsrp.type.InvalidHandleFault;
import org.exoplatform.services.wsrp.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp.type.InvalidUserCategoryFault;
import org.exoplatform.services.wsrp.type.MissingParametersFault;
import org.exoplatform.services.wsrp.type.OperationFailedFault;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.PortletDescriptionRequest;
import org.exoplatform.services.wsrp.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp.type.PortletPropertyDescriptionRequest;
import org.exoplatform.services.wsrp.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp.type.PropertyList;
import org.exoplatform.services.wsrp.type.SetPortletPropertiesRequest;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRP_v1_PortletManagement_Binding_SOAPImpl 
    implements WSRP_v1_PortletManagement_PortType{

  private PortletManagementOperationsInterface portletManagementOperationsInterface;
  
  public WSRP_v1_PortletManagement_Binding_SOAPImpl() {
    portletManagementOperationsInterface = (PortletManagementOperationsInterface) ExoContainerContext.getCurrentContainer().
        getComponentInstanceOfType(PortletManagementOperationsInterface.class);
  }

  public PortletDescriptionResponse getPortletDescription(PortletDescriptionRequest getPortletDescription)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletDescription(getPortletDescription.getRegistrationContext(),
                                                                      getPortletDescription.getPortletContext(),
                                                                      getPortletDescription.getUserContext(),
                                                                      getPortletDescription.getDesiredLocales());
  }

  public PortletContext clonePortlet(ClonePortletRequest clonePortlet)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.clonePortlet(clonePortlet.getRegistrationContext(),
                                                             clonePortlet.getPortletContext(),
                                                             clonePortlet.getUserContext());
  }

  public DestroyPortletsResponse destroyPortlets(DestroyPortletsRequest destroyPortlets)
    throws RemoteException, InconsistentParametersFault, InvalidRegistrationFault, 
           OperationFailedFault, MissingParametersFault {
    return portletManagementOperationsInterface.destroyPortlets(destroyPortlets.getRegistrationContext(),
                                                                destroyPortlets.getPortletHandles());
  }

  public PortletContext setPortletProperties(SetPortletPropertiesRequest setPortletProperties)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.setPortletProperties(setPortletProperties.getRegistrationContext(),
                                                                     setPortletProperties.getPortletContext(),
                                                                     setPortletProperties.getUserContext(),
                                                                     setPortletProperties.getPropertyList());
  }

  public PropertyList getPortletProperties(GetPortletPropertiesRequest getPortletProperties)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletProperties(getPortletProperties.getRegistrationContext(),
                                                                     getPortletProperties.getPortletContext(),
                                                                     getPortletProperties.getUserContext(),
                                                                     getPortletProperties.getNames());
  }

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(PortletPropertyDescriptionRequest getPortletPropertyDescription)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletPropertyDescription(getPortletPropertyDescription.getRegistrationContext(),
                                                                              getPortletPropertyDescription.getPortletContext(),
                                                                              getPortletPropertyDescription.getUserContext(),
                                                                              getPortletPropertyDescription.getDesiredLocales());
  }

}
