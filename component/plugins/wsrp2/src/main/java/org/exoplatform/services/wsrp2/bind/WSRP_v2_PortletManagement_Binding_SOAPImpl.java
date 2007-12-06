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
import org.exoplatform.services.wsrp2.intf.WSRP_v2_PortletManagement_PortType;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp2.type.AccessDeniedFault;
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.CopyPortlets;
import org.exoplatform.services.wsrp2.type.CopyPortletsResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportByValueNotSupportedFault;
import org.exoplatform.services.wsrp2.type.ExportNoLongerValidFault;
import org.exoplatform.services.wsrp2.type.ExportPortlets;
import org.exoplatform.services.wsrp2.type.ExportPortletsResponse;
import org.exoplatform.services.wsrp2.type.GetPortletDescription;
import org.exoplatform.services.wsrp2.type.GetPortletProperties;
import org.exoplatform.services.wsrp2.type.GetPortletPropertyDescription;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetime;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.ImportPortlets;
import org.exoplatform.services.wsrp2.type.ImportPortletsResponse;
import org.exoplatform.services.wsrp2.type.InconsistentParametersFault;
import org.exoplatform.services.wsrp2.type.InvalidHandleFault;
import org.exoplatform.services.wsrp2.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp2.type.InvalidUserCategoryFault;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.MissingParametersFault;
import org.exoplatform.services.wsrp2.type.ModifyRegistrationRequiredFault;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.OperationNotSupportedFault;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.ReleaseExport;
import org.exoplatform.services.wsrp2.type.ResourceSuspendedFault;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.SetExportLifetime;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetime;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetimeResponse;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRP_v2_PortletManagement_Binding_SOAPImpl implements WSRP_v2_PortletManagement_PortType {

  private PortletManagementOperationsInterface portletManagementOperationsInterface;

  public WSRP_v2_PortletManagement_Binding_SOAPImpl() {
    portletManagementOperationsInterface = (PortletManagementOperationsInterface) ExoContainerContext.getCurrentContainer()
                                                                                                     .getComponentInstanceOfType(PortletManagementOperationsInterface.class);
  }

  public PortletDescriptionResponse getPortletDescription(GetPortletDescription getPortletDescription) throws RemoteException,
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

  public PortletContext clonePortlet(ClonePortlet clonePortlet) throws RemoteException,
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

  public DestroyPortletsResponse destroyPortlets(DestroyPortlets destroyPortlets) throws RemoteException,
                                                                                 InconsistentParametersFault,
                                                                                 InvalidRegistrationFault,
                                                                                 OperationFailedFault,
                                                                                 MissingParametersFault {
    return portletManagementOperationsInterface.destroyPortlets(destroyPortlets.getRegistrationContext(), destroyPortlets.getPortletHandles());
  }

  public PortletContext setPortletProperties(SetPortletProperties setPortletProperties) throws RemoteException,
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

  public PropertyList getPortletProperties(GetPortletProperties getPortletProperties) throws RemoteException,
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

  public ImportPortletsResponse importPortlets(ImportPortlets importPortlets) throws RemoteException,
                                                                             ExportNoLongerValidFault,
                                                                             InvalidUserCategoryFault,
                                                                             ResourceSuspendedFault,
                                                                             ModifyRegistrationRequiredFault,
                                                                             InconsistentParametersFault,
                                                                             InvalidRegistrationFault,
                                                                             OperationFailedFault,
                                                                             MissingParametersFault,
                                                                             OperationNotSupportedFault,
                                                                             AccessDeniedFault {
    return portletManagementOperationsInterface.importPortlets(importPortlets.getRegistrationContext(),
                                                               importPortlets.getImportContext(),
                                                               importPortlets.getImportPortlet(),
                                                               importPortlets.getUserContext(),
                                                               importPortlets.getLifetime());
  }

  public ExportPortletsResponse exportPortlets(ExportPortlets exportPortlets) throws java.rmi.RemoteException,
                                                                             InvalidUserCategoryFault,
                                                                             ResourceSuspendedFault,
                                                                             ModifyRegistrationRequiredFault,
                                                                             InconsistentParametersFault,
                                                                             InvalidRegistrationFault,
                                                                             OperationFailedFault,
                                                                             MissingParametersFault,
                                                                             ExportByValueNotSupportedFault,
                                                                             OperationNotSupportedFault,
                                                                             AccessDeniedFault,
                                                                             InvalidHandleFault {

    return portletManagementOperationsInterface.exportPortlets(exportPortlets.getRegistrationContext(),
                                                               exportPortlets.getPortletContext(),
                                                               exportPortlets.getUserContext(),
                                                               exportPortlets.getLifetime(),
                                                               exportPortlets.getExportByValueRequired());

  }

  public ReturnAny releaseExport(ReleaseExport releaseExport) throws java.rmi.RemoteException {
    return portletManagementOperationsInterface.releaseExport(releaseExport.getExportContext(), releaseExport.getUserContext());

  }

  public GetPortletsLifetimeResponse getPortletsLifetime(GetPortletsLifetime getPortletsLifetime) throws java.rmi.RemoteException,
                                                                                                 ResourceSuspendedFault,
                                                                                                 ModifyRegistrationRequiredFault,
                                                                                                 InconsistentParametersFault,
                                                                                                 InvalidRegistrationFault,
                                                                                                 OperationFailedFault,
                                                                                                 OperationNotSupportedFault,
                                                                                                 AccessDeniedFault,
                                                                                                 InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletsLifetime(getPortletsLifetime.getRegistrationContext(),
                                                                    getPortletsLifetime.getPortletContext(),
                                                                    getPortletsLifetime.getUserContext());
  }

  public SetPortletsLifetimeResponse setPortletsLifetime(SetPortletsLifetime setPortletsLifetime) throws java.rmi.RemoteException,
                                                                                                 ResourceSuspendedFault,
                                                                                                 ModifyRegistrationRequiredFault,
                                                                                                 InconsistentParametersFault,
                                                                                                 InvalidRegistrationFault,
                                                                                                 OperationFailedFault,
                                                                                                 OperationNotSupportedFault,
                                                                                                 AccessDeniedFault,
                                                                                                 InvalidHandleFault {
    return portletManagementOperationsInterface.setPortletsLifetime(setPortletsLifetime.getRegistrationContext(),
                                                                    setPortletsLifetime.getPortletContext(),
                                                                    setPortletsLifetime.getUserContext(),
                                                                    setPortletsLifetime.getLifetime());
  }

  public Lifetime setExportLifetime(SetExportLifetime setExportLifetime) throws java.rmi.RemoteException,
                                                                        ResourceSuspendedFault,
                                                                        ModifyRegistrationRequiredFault,
                                                                        InvalidRegistrationFault,
                                                                        OperationFailedFault,
                                                                        OperationNotSupportedFault,
                                                                        AccessDeniedFault,
                                                                        InvalidHandleFault {
    return portletManagementOperationsInterface.setExportLifetime(setExportLifetime.getRegistrationContext(),
                                                                  setExportLifetime.getExportContext(),
                                                                  setExportLifetime.getUserContext(),
                                                                  setExportLifetime.getLifetime());
  }

  public CopyPortletsResponse copyPortlets(CopyPortlets copyPortlets) throws java.rmi.RemoteException,
                                                                     InvalidUserCategoryFault,
                                                                     ResourceSuspendedFault,
                                                                     ModifyRegistrationRequiredFault,
                                                                     InconsistentParametersFault,
                                                                     InvalidRegistrationFault,
                                                                     OperationFailedFault,
                                                                     MissingParametersFault,
                                                                     OperationNotSupportedFault,
                                                                     AccessDeniedFault,
                                                                     InvalidHandleFault {
    return portletManagementOperationsInterface.copyPortlets(copyPortlets.getToRegistrationContext(),
                                                             copyPortlets.getToUserContext(),
                                                             copyPortlets.getFromRegistrationContext(),
                                                             copyPortlets.getFromUserContext(),
                                                             copyPortlets.getFromPortletContexts(),
                                                             copyPortlets.getLifetime());
  }

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(GetPortletPropertyDescription getPortletPropertyDescription) throws java.rmi.RemoteException,
                                                                                                                                      InvalidUserCategoryFault,
                                                                                                                                      ResourceSuspendedFault,
                                                                                                                                      ModifyRegistrationRequiredFault,
                                                                                                                                      InconsistentParametersFault,
                                                                                                                                      InvalidRegistrationFault,
                                                                                                                                      OperationFailedFault,
                                                                                                                                      MissingParametersFault,
                                                                                                                                      OperationNotSupportedFault,
                                                                                                                                      AccessDeniedFault,
                                                                                                                                      InvalidHandleFault {

    return portletManagementOperationsInterface.getPortletPropertyDescription(getPortletPropertyDescription.getRegistrationContext(),
                                                                              getPortletPropertyDescription.getPortletContext(),
                                                                              getPortletPropertyDescription.getUserContext(),
                                                                              getPortletPropertyDescription.getDesiredLocales());
  }

}
