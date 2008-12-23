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

package org.exoplatform.services.wsrp2.producer;

import java.rmi.RemoteException;
import java.util.List;

import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.ExportByValueNotSupported;
import org.exoplatform.services.wsrp2.intf.ExportNoLongerValid;
import org.exoplatform.services.wsrp2.intf.InconsistentParameters;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.InvalidUserCategory;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.type.CopyPortletsResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportPortletsResponse;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.ImportPortlet;
import org.exoplatform.services.wsrp2.type.ImportPortletsResponse;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface PortletManagementOperationsInterface {

  public PortletDescriptionResponse getPortletDescription(RegistrationContext registrationContext,
                                                          PortletContext portletContext,
                                                          UserContext userContext,
                                                          List<String> desiredLocales) throws OperationNotSupported,
                                                                                      AccessDenied,
                                                                                      ResourceSuspended,
                                                                                      InvalidRegistration,
                                                                                      InvalidHandle,
                                                                                      InvalidUserCategory,
                                                                                      ModifyRegistrationRequired,
                                                                                      MissingParameters,
                                                                                      InconsistentParameters,
                                                                                      OperationFailed,
                                                                                      WSRPException;

  public DestroyPortletsResponse destroyPortlets(RegistrationContext registrationContext,
                                                 List<String> portletHandles,
                                                 UserContext userContext) throws OperationNotSupported,
                                                                         ResourceSuspended,
                                                                         InvalidRegistration,
                                                                         ModifyRegistrationRequired,
                                                                         MissingParameters,
                                                                         InconsistentParameters,
                                                                         OperationFailed,
                                                                         WSRPException;

  public PortletContext setPortletProperties(RegistrationContext registrationContext,
                                             PortletContext portletContext,
                                             UserContext userContext,
                                             PropertyList propertyList) throws OperationNotSupported,
                                                                       AccessDenied,
                                                                       ResourceSuspended,
                                                                       InvalidRegistration,
                                                                       InvalidHandle,
                                                                       InvalidUserCategory,
                                                                       ModifyRegistrationRequired,
                                                                       MissingParameters,
                                                                       InconsistentParameters,
                                                                       OperationFailed,
                                                                       WSRPException;

  public PropertyList getPortletProperties(RegistrationContext registrationContext,
                                           PortletContext portletContext,
                                           UserContext userContext,
                                           List<String> names) throws OperationNotSupported,
                                                              AccessDenied,
                                                              ResourceSuspended,
                                                              InvalidRegistration,
                                                              InvalidHandle,
                                                              InvalidUserCategory,
                                                              ModifyRegistrationRequired,
                                                              MissingParameters,
                                                              InconsistentParameters,
                                                              OperationFailed,
                                                              WSRPException;

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(RegistrationContext registrationContext,
                                                                          PortletContext portletContext,
                                                                          UserContext userContext,
                                                                          List<String> desiredLocales) throws OperationNotSupported,
                                                                                                      AccessDenied,
                                                                                                      ResourceSuspended,
                                                                                                      InvalidRegistration,
                                                                                                      InvalidHandle,
                                                                                                      InvalidUserCategory,
                                                                                                      ModifyRegistrationRequired,
                                                                                                      MissingParameters,
                                                                                                      InconsistentParameters,
                                                                                                      OperationFailed,
                                                                                                      WSRPException;

  public PortletContext clonePortlet(RegistrationContext registrationContext,
                                     PortletContext portletContext,
                                     UserContext userContext,
                                     Lifetime lifetime) throws OperationNotSupported,
                                                       AccessDenied,
                                                       ResourceSuspended,
                                                       InvalidRegistration,
                                                       InvalidHandle,
                                                       InvalidUserCategory,
                                                       ModifyRegistrationRequired,
                                                       MissingParameters,
                                                       InconsistentParameters,
                                                       OperationFailed,
                                                       WSRPException;

  public GetPortletsLifetimeResponse getPortletsLifetime(RegistrationContext registrationContext,
                                                         List<PortletContext> portletContext,
                                                         UserContext userContext) throws OperationNotSupported,
                                                                                 AccessDenied,
                                                                                 ResourceSuspended,
                                                                                 InvalidRegistration,
                                                                                 InvalidHandle,
                                                                                 ModifyRegistrationRequired,
                                                                                 InconsistentParameters,
                                                                                 OperationFailed,
                                                                                 WSRPException;

  public SetPortletsLifetimeResponse setPortletsLifetime(RegistrationContext registrationContext,
                                                         List<PortletContext> portletContext,
                                                         UserContext userContext,
                                                         Lifetime lifetime) throws OperationNotSupported,
                                                                           AccessDenied,
                                                                           ResourceSuspended,
                                                                           InvalidRegistration,
                                                                           InvalidHandle,
                                                                           ModifyRegistrationRequired,
                                                                           InconsistentParameters,
                                                                           OperationFailed,
                                                                           WSRPException;

  public CopyPortletsResponse copyPortlets(RegistrationContext toRegistrationContext,
                                           UserContext toUserContext,
                                           RegistrationContext fromRegistrationContext,
                                           UserContext UserContext,
                                           List<PortletContext> fromPortletContexts,
                                           Lifetime lifetime) throws OperationNotSupported,
                                                             AccessDenied,
                                                             ResourceSuspended,
                                                             InvalidRegistration,
                                                             InvalidHandle,
                                                             InvalidUserCategory,
                                                             ModifyRegistrationRequired,
                                                             MissingParameters,
                                                             InconsistentParameters,
                                                             OperationFailed,
                                                             WSRPException;

  public ExportPortletsResponse exportPortlets(RegistrationContext registrationContext,
                                               List<PortletContext> portletContext,
                                               UserContext userContext,
                                               Lifetime lifetime,
                                               boolean exportByValueRequired) throws OperationNotSupported,
                                                                             ExportByValueNotSupported,
                                                                             AccessDenied,
                                                                             ResourceSuspended,
                                                                             InvalidRegistration,
                                                                             InvalidHandle,
                                                                             InvalidUserCategory,
                                                                             ModifyRegistrationRequired,
                                                                             MissingParameters,
                                                                             InconsistentParameters,
                                                                             OperationFailed,
                                                                             WSRPException;

  public ImportPortletsResponse importPortlets(RegistrationContext registrationContext,
                                               byte[] importContext,
                                               List<ImportPortlet> importPortlet,
                                               UserContext userContext,
                                               Lifetime lifetime) throws OperationNotSupported,
                                                                 ExportNoLongerValid,
                                                                 AccessDenied,
                                                                 ResourceSuspended,
                                                                 InvalidRegistration,
                                                                 InvalidUserCategory,
                                                                 ModifyRegistrationRequired,
                                                                 MissingParameters,
                                                                 InconsistentParameters,
                                                                 OperationFailed,
                                                                 WSRPException;

  public ReturnAny releaseExport(byte[] exportContext,
                                 UserContext userContext,
                                 RegistrationContext registrationContext);

  public Lifetime setExportLifetime(RegistrationContext registrationContext,
                                    byte[] exportContext,
                                    UserContext userContext,
                                    Lifetime lifetime) throws OperationNotSupported,
                                                      AccessDenied,
                                                      ResourceSuspended,
                                                      InvalidRegistration,
                                                      InvalidHandle,
                                                      ModifyRegistrationRequired,
                                                      OperationFailed,
                                                      WSRPException;

}
