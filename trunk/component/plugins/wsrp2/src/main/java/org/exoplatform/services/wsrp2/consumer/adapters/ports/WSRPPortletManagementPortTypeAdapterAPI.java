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

  /**
   * Gets the portlet description.
   * 
   * @param getPortletDescription the get portlet description
   * 
   * @return the portlet description
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidUserCategory the invalid user category
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
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

  /**
   * Clone portlet.
   * 
   * @param clonePortlet the clone portlet
   * 
   * @return the portlet context
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidUserCategory the invalid user category
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
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

  /**
   * Destroy portlets.
   * 
   * @param destroyPortlets the destroy portlets
   * 
   * @return the destroy portlets response
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
  public DestroyPortletsResponse destroyPortlets(DestroyPortlets destroyPortlets) throws OperationNotSupported,
                                                                                 ResourceSuspended,
                                                                                 InvalidRegistration,
                                                                                 ModifyRegistrationRequired,
                                                                                 MissingParameters,
                                                                                 InconsistentParameters,
                                                                                 OperationFailed;

  /**
   * Sets the portlet properties.
   * 
   * @param setPortletProperties the set portlet properties
   * 
   * @return the portlet context
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidUserCategory the invalid user category
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
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

  /**
   * Gets the portlet properties.
   * 
   * @param getPortletProperties the get portlet properties
   * 
   * @return the portlet properties
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidUserCategory the invalid user category
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
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

  /**
   * Gets the portlet property description.
   * 
   * @param getPortletPropertyDescription the get portlet property description
   * 
   * @return the portlet property description
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidUserCategory the invalid user category
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
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

  /**
   * Copy portlets.
   * 
   * @param copyPortlets the copy portlets
   * 
   * @return the copy portlets response
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidUserCategory the invalid user category
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
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

  /**
   * Export portlets.
   * 
   * @param exportPortlets the export portlets
   * 
   * @return the export portlets response
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws ExportByValueNotSupported the export by value not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidUserCategory the invalid user category
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
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

  /**
   * Import portlets.
   * 
   * @param importPortlets the import portlets
   * 
   * @return the import portlets response
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws ExportNoLongerValid the export no longer valid
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidUserCategory the invalid user category
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
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

  /**
   * Release export.
   * 
   * @param releaseExport the release export
   * 
   * @return the return any
   */
  public ReturnAny releaseExport(ReleaseExport releaseExport);

  /**
   * Sets the export lifetime.
   * 
   * @param setExportLifetime the set export lifetime
   * 
   * @return the lifetime
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws OperationFailed the operation failed
   */
  public Lifetime setExportLifetime(SetExportLifetime setExportLifetime) throws OperationNotSupported,
                                                                        AccessDenied,
                                                                        ResourceSuspended,
                                                                        InvalidRegistration,
                                                                        InvalidHandle,
                                                                        ModifyRegistrationRequired,
                                                                        OperationFailed;

  /**
   * Gets the portlets lifetime.
   * 
   * @param getPortletsLifetime the get portlets lifetime
   * 
   * @return the portlets lifetime
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
  public GetPortletsLifetimeResponse getPortletsLifetime(GetPortletsLifetime getPortletsLifetime) throws OperationNotSupported,
                                                                                                 AccessDenied,
                                                                                                 ResourceSuspended,
                                                                                                 InvalidRegistration,
                                                                                                 InvalidHandle,
                                                                                                 ModifyRegistrationRequired,
                                                                                                 InconsistentParameters,
                                                                                                 OperationFailed;

  /**
   * Sets the portlets lifetime.
   * 
   * @param setPortletsLifetime the set portlets lifetime
   * 
   * @return the sets the portlets lifetime response
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   */
  public SetPortletsLifetimeResponse setPortletsLifetime(SetPortletsLifetime setPortletsLifetime) throws OperationNotSupported,
                                                                                                 AccessDenied,
                                                                                                 ResourceSuspended,
                                                                                                 InvalidRegistration,
                                                                                                 InvalidHandle,
                                                                                                 ModifyRegistrationRequired,
                                                                                                 InconsistentParameters,
                                                                                                 OperationFailed;

}
