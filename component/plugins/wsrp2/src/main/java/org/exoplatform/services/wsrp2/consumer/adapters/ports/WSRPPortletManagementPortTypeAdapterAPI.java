/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.services.wsrp2.consumer.adapters.ports;

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
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.CopyPortlets;
import org.exoplatform.services.wsrp2.type.CopyPortletsResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportPortlets;
import org.exoplatform.services.wsrp2.type.ExportPortletsResponse;
import org.exoplatform.services.wsrp2.type.GetPortletDescription;
import org.exoplatform.services.wsrp2.type.GetPortletProperties;
import org.exoplatform.services.wsrp2.type.GetPortletPropertyDescription;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetime;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.ImportPortlets;
import org.exoplatform.services.wsrp2.type.ImportPortletsResponse;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.ReleaseExport;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.SetExportLifetime;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetime;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetimeResponse;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public interface WSRPPortletManagementPortTypeAdapterAPI {

  public PortletDescriptionResponse getPortletDescription(GetPortletDescription getPortletDescription) throws OperationNotSupported,
                                                                                                      AccessDenied,
                                                                                                      ResourceSuspended,
                                                                                                      InvalidRegistration,
                                                                                                      InvalidHandle,
                                                                                                      InvalidUserCategory,
                                                                                                      ModifyRegistrationRequired,
                                                                                                      MissingParameters,
                                                                                                      InconsistentParameters,
                                                                                                      OperationFailed;

  public PortletContext clonePortlet(ClonePortlet clonePortlet) throws OperationNotSupported,
                                                               AccessDenied,
                                                               ResourceSuspended,
                                                               InvalidRegistration,
                                                               InvalidHandle,
                                                               InvalidUserCategory,
                                                               ModifyRegistrationRequired,
                                                               MissingParameters,
                                                               InconsistentParameters,
                                                               OperationFailed;

  public DestroyPortletsResponse destroyPortlets(DestroyPortlets destroyPortlets) throws OperationNotSupported,
                                                                                 ResourceSuspended,
                                                                                 InvalidRegistration,
                                                                                 ModifyRegistrationRequired,
                                                                                 MissingParameters,
                                                                                 InconsistentParameters,
                                                                                 OperationFailed;

  public PortletContext setPortletProperties(SetPortletProperties setPortletProperties) throws OperationNotSupported,
                                                                                       AccessDenied,
                                                                                       ResourceSuspended,
                                                                                       InvalidRegistration,
                                                                                       InvalidHandle,
                                                                                       InvalidUserCategory,
                                                                                       ModifyRegistrationRequired,
                                                                                       MissingParameters,
                                                                                       InconsistentParameters,
                                                                                       OperationFailed;

  public PropertyList getPortletProperties(GetPortletProperties getPortletProperties) throws OperationNotSupported,
                                                                                     AccessDenied,
                                                                                     ResourceSuspended,
                                                                                     InvalidRegistration,
                                                                                     InvalidHandle,
                                                                                     InvalidUserCategory,
                                                                                     ModifyRegistrationRequired,
                                                                                     MissingParameters,
                                                                                     InconsistentParameters,
                                                                                     OperationFailed;

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(GetPortletPropertyDescription getPortletPropertyDescription) throws OperationNotSupported,
                                                                                                                                      AccessDenied,
                                                                                                                                      ResourceSuspended,
                                                                                                                                      InvalidRegistration,
                                                                                                                                      InvalidHandle,
                                                                                                                                      InvalidUserCategory,
                                                                                                                                      ModifyRegistrationRequired,
                                                                                                                                      MissingParameters,
                                                                                                                                      InconsistentParameters,
                                                                                                                                      OperationFailed;

  public CopyPortletsResponse copyPortlets(CopyPortlets copyPortlets) throws OperationNotSupported,
                                                                     AccessDenied,
                                                                     ResourceSuspended,
                                                                     InvalidRegistration,
                                                                     InvalidHandle,
                                                                     InvalidUserCategory,
                                                                     ModifyRegistrationRequired,
                                                                     MissingParameters,
                                                                     InconsistentParameters,
                                                                     OperationFailed;

  public ExportPortletsResponse exportPortlets(ExportPortlets exportPortlets) throws OperationNotSupported,
                                                                             ExportByValueNotSupported,
                                                                             AccessDenied,
                                                                             ResourceSuspended,
                                                                             InvalidRegistration,
                                                                             InvalidHandle,
                                                                             InvalidUserCategory,
                                                                             ModifyRegistrationRequired,
                                                                             MissingParameters,
                                                                             InconsistentParameters,
                                                                             OperationFailed;

  public ImportPortletsResponse importPortlets(ImportPortlets importPortlets) throws OperationNotSupported,
                                                                             ExportNoLongerValid,
                                                                             AccessDenied,
                                                                             ResourceSuspended,
                                                                             InvalidRegistration,
                                                                             InvalidUserCategory,
                                                                             ModifyRegistrationRequired,
                                                                             MissingParameters,
                                                                             InconsistentParameters,
                                                                             OperationFailed;

  public ReturnAny releaseExport(ReleaseExport releaseExport);

  public Lifetime setExportLifetime(SetExportLifetime setExportLifetime) throws OperationNotSupported,
                                                                        AccessDenied,
                                                                        ResourceSuspended,
                                                                        InvalidRegistration,
                                                                        InvalidHandle,
                                                                        ModifyRegistrationRequired,
                                                                        OperationFailed;

  public GetPortletsLifetimeResponse getPortletsLifetime(GetPortletsLifetime getPortletsLifetime) throws OperationNotSupported,
                                                                                                 AccessDenied,
                                                                                                 ResourceSuspended,
                                                                                                 InvalidRegistration,
                                                                                                 InvalidHandle,
                                                                                                 ModifyRegistrationRequired,
                                                                                                 InconsistentParameters,
                                                                                                 OperationFailed;

  public SetPortletsLifetimeResponse setPortletsLifetime(SetPortletsLifetime setPortletsLifetime) throws OperationNotSupported,
                                                                                                 AccessDenied,
                                                                                                 ResourceSuspended,
                                                                                                 InvalidRegistration,
                                                                                                 InvalidHandle,
                                                                                                 ModifyRegistrationRequired,
                                                                                                 InconsistentParameters,
                                                                                                 OperationFailed;

}
