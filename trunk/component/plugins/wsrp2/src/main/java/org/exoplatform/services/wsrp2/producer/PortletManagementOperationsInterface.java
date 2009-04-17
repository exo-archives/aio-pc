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

package org.exoplatform.services.wsrp2.producer;

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

  /**
   * Gets the portlet description.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param userContext the user context
   * @param desiredLocales the desired locales
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Destroy portlets.
   * 
   * @param registrationContext the registration context
   * @param portletHandles the portlet handles
   * @param userContext the user context
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Sets the portlet properties.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param userContext the user context
   * @param propertyList the property list
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Gets the portlet properties.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param userContext the user context
   * @param names the names
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Gets the portlet property description.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param userContext the user context
   * @param desiredLocales the desired locales
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Clone portlet.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param userContext the user context
   * @param lifetime the lifetime
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Gets the portlets lifetime.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param userContext the user context
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Sets the portlets lifetime.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param userContext the user context
   * @param lifetime the lifetime
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Copy portlets.
   * 
   * @param toRegistrationContext the to registration context
   * @param toUserContext the to user context
   * @param fromRegistrationContext the from registration context
   * @param UserContext the user context
   * @param fromPortletContexts the from portlet contexts
   * @param lifetime the lifetime
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Export portlets.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param userContext the user context
   * @param lifetime the lifetime
   * @param exportByValueRequired the export by value required
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Import portlets.
   * 
   * @param registrationContext the registration context
   * @param importContext the import context
   * @param importPortlet the import portlet
   * @param userContext the user context
   * @param lifetime the lifetime
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
   * @throws WSRPException the WSRP exception
   */
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

  /**
   * Release export.
   * 
   * @param exportContext the export context
   * @param userContext the user context
   * @param registrationContext the registration context
   * 
   * @return the return any
   */
  public ReturnAny releaseExport(byte[] exportContext,
                                 UserContext userContext,
                                 RegistrationContext registrationContext);

  /**
   * Sets the export lifetime.
   * 
   * @param registrationContext the registration context
   * @param exportContext the export context
   * @param userContext the user context
   * @param lifetime the lifetime
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
   * @throws WSRPException the WSRP exception
   */
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
