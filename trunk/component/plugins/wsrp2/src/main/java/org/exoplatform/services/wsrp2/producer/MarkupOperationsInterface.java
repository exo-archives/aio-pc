/*
 * Copyright (C) 2003-2009  eXo Platform SAS.
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
import org.exoplatform.services.wsrp2.intf.InconsistentParameters;
import org.exoplatform.services.wsrp2.intf.InvalidCookie;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.InvalidSession;
import org.exoplatform.services.wsrp2.intf.InvalidUserCategory;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.PortletStateChangeRequired;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.intf.UnsupportedLocale;
import org.exoplatform.services.wsrp2.intf.UnsupportedMimeType;
import org.exoplatform.services.wsrp2.intf.UnsupportedMode;
import org.exoplatform.services.wsrp2.intf.UnsupportedWindowState;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.EventParams;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.InteractionParams;
import org.exoplatform.services.wsrp2.type.MarkupParams;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ResourceParams;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface MarkupOperationsInterface {
  
  /**
   * Gets the markup.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param runtimeContext the runtime context
   * @param userContext the user context
   * @param markupParams the markup params
   * 
   * @return the markup
   * 
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws UnsupportedMimeType the unsupported mime type
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidCookie the invalid cookie
   * @throws UnsupportedWindowState the unsupported window state
   * @throws InvalidUserCategory the invalid user category
   * @throws UnsupportedMode the unsupported mode
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws InvalidSession the invalid session
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   * @throws UnsupportedLocale the unsupported locale
   * @throws WSRPException the WSRP exception
   */
  public MarkupResponse getMarkup(RegistrationContext registrationContext,
                                  PortletContext portletContext,
                                  RuntimeContext runtimeContext,
                                  UserContext userContext,
                                  MarkupParams markupParams) throws AccessDenied,
                                                            ResourceSuspended,
                                                            UnsupportedMimeType,
                                                            InvalidRegistration,
                                                            InvalidHandle,
                                                            InvalidCookie,
                                                            UnsupportedWindowState,
                                                            InvalidUserCategory,
                                                            UnsupportedMode,
                                                            ModifyRegistrationRequired,
                                                            InvalidSession,
                                                            MissingParameters,
                                                            InconsistentParameters,
                                                            OperationFailed,
                                                            UnsupportedLocale,
                                                            WSRPException;

  /**
   * Gets the resource.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param runtimeContext the runtime context
   * @param userContext the user context
   * @param resourceParams the resource params
   * 
   * @return the resource
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws UnsupportedMimeType the unsupported mime type
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidCookie the invalid cookie
   * @throws UnsupportedWindowState the unsupported window state
   * @throws InvalidUserCategory the invalid user category
   * @throws UnsupportedMode the unsupported mode
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws InvalidSession the invalid session
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   * @throws UnsupportedLocale the unsupported locale
   * @throws WSRPException the WSRP exception
   */
  public ResourceResponse getResource(RegistrationContext registrationContext,
                                      PortletContext portletContext,
                                      RuntimeContext runtimeContext,
                                      UserContext userContext,
                                      ResourceParams resourceParams) throws OperationNotSupported,
                                                                    AccessDenied,
                                                                    ResourceSuspended,
                                                                    UnsupportedMimeType,
                                                                    InvalidRegistration,
                                                                    InvalidHandle,
                                                                    InvalidCookie,
                                                                    UnsupportedWindowState,
                                                                    InvalidUserCategory,
                                                                    UnsupportedMode,
                                                                    ModifyRegistrationRequired,
                                                                    InvalidSession,
                                                                    MissingParameters,
                                                                    InconsistentParameters,
                                                                    OperationFailed,
                                                                    UnsupportedLocale,
                                                                    WSRPException;

  /**
   * Perform blocking interaction.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param runtimeContext the runtime context
   * @param userContext the user context
   * @param markupParams the markup params
   * @param interactionParams the interaction params
   * 
   * @return the blocking interaction response
   * 
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws UnsupportedMimeType the unsupported mime type
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidCookie the invalid cookie
   * @throws UnsupportedWindowState the unsupported window state
   * @throws InvalidUserCategory the invalid user category
   * @throws UnsupportedMode the unsupported mode
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws InvalidSession the invalid session
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   * @throws UnsupportedLocale the unsupported locale
   * @throws PortletStateChangeRequired the portlet state change required
   * @throws WSRPException the WSRP exception
   */
  public BlockingInteractionResponse performBlockingInteraction(RegistrationContext registrationContext,
                                                                PortletContext portletContext,
                                                                RuntimeContext runtimeContext,
                                                                UserContext userContext,
                                                                MarkupParams markupParams,
                                                                InteractionParams interactionParams) throws AccessDenied,
                                                                                                    ResourceSuspended,
                                                                                                    UnsupportedMimeType,
                                                                                                    InvalidRegistration,
                                                                                                    InvalidHandle,
                                                                                                    InvalidCookie,
                                                                                                    UnsupportedWindowState,
                                                                                                    InvalidUserCategory,
                                                                                                    UnsupportedMode,
                                                                                                    ModifyRegistrationRequired,
                                                                                                    InvalidSession,
                                                                                                    MissingParameters,
                                                                                                    InconsistentParameters,
                                                                                                    OperationFailed,
                                                                                                    UnsupportedLocale,
                                                                                                    PortletStateChangeRequired,
                                                                                                    WSRPException;

  /**
   * Handle events.
   * 
   * @param registrationContext the registration context
   * @param portletContext the portlet context
   * @param runtimeContext the runtime context
   * @param userContext the user context
   * @param markupParams the markup params
   * @param eventParams the event params
   * 
   * @return the handle events response
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws UnsupportedMimeType the unsupported mime type
   * @throws InvalidRegistration the invalid registration
   * @throws InvalidHandle the invalid handle
   * @throws InvalidCookie the invalid cookie
   * @throws UnsupportedWindowState the unsupported window state
   * @throws InvalidUserCategory the invalid user category
   * @throws UnsupportedMode the unsupported mode
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws InvalidSession the invalid session
   * @throws MissingParameters the missing parameters
   * @throws InconsistentParameters the inconsistent parameters
   * @throws OperationFailed the operation failed
   * @throws UnsupportedLocale the unsupported locale
   * @throws PortletStateChangeRequired the portlet state change required
   * @throws WSRPException the WSRP exception
   */
  public HandleEventsResponse handleEvents(RegistrationContext registrationContext,
                                           PortletContext portletContext,
                                           RuntimeContext runtimeContext,
                                           UserContext userContext,
                                           MarkupParams markupParams,
                                           EventParams eventParams) throws OperationNotSupported,
                                                                   AccessDenied,
                                                                   ResourceSuspended,
                                                                   UnsupportedMimeType,
                                                                   InvalidRegistration,
                                                                   InvalidHandle,
                                                                   InvalidCookie,
                                                                   UnsupportedWindowState,
                                                                   InvalidUserCategory,
                                                                   UnsupportedMode,
                                                                   ModifyRegistrationRequired,
                                                                   InvalidSession,
                                                                   MissingParameters,
                                                                   InconsistentParameters,
                                                                   OperationFailed,
                                                                   UnsupportedLocale,
                                                                   PortletStateChangeRequired,
                                                                   WSRPException;

  /**
   * Inits the cookie.
   * 
   * @param registrationContext the registration context
   * @param userContext the user context
   * 
   * @return the return any
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws OperationFailed the operation failed
   * @throws WSRPException the WSRP exception
   */
  public ReturnAny initCookie(RegistrationContext registrationContext, UserContext userContext) throws OperationNotSupported,
                                                                                               AccessDenied,
                                                                                               ResourceSuspended,
                                                                                               InvalidRegistration,
                                                                                               ModifyRegistrationRequired,
                                                                                               OperationFailed,
                                                                                               WSRPException;

  /**
   * Release sessions.
   * 
   * @param registrationContext the registration context
   * @param sessionIDs the session i ds
   * @param userContext the user context
   * 
   * @return the return any
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws OperationFailed the operation failed
   * @throws WSRPException the WSRP exception
   */
  public ReturnAny releaseSessions(RegistrationContext registrationContext,
                                   List<String> sessionIDs,
                                   UserContext userContext) throws OperationNotSupported,
                                                           AccessDenied,
                                                           ResourceSuspended,
                                                           InvalidRegistration,
                                                           ModifyRegistrationRequired,
                                                           MissingParameters,
                                                           OperationFailed,
                                                           WSRPException;

}
