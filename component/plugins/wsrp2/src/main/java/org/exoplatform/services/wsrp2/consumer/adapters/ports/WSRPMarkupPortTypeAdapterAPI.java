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

import java.util.List;

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
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetResource;
import org.exoplatform.services.wsrp2.type.HandleEvents;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.InitCookie;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.PerformBlockingInteraction;
import org.exoplatform.services.wsrp2.type.ReleaseSessions;
import org.exoplatform.services.wsrp2.type.ResourceResponse;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public interface WSRPMarkupPortTypeAdapterAPI {

  /**
   * Gets the markup.
   * 
   * @param getMarkup the get markup
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
   */
  public MarkupResponse getMarkup(GetMarkup getMarkup) throws AccessDenied,
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
                                                      UnsupportedLocale;

  /**
   * Gets the resource.
   * 
   * @param getResource the get resource
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
   */
  public ResourceResponse getResource(GetResource getResource) throws OperationNotSupported,
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
                                                              UnsupportedLocale;

  /**
   * Perform blocking interaction.
   * 
   * @param performBlockingInteraction the perform blocking interaction
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
   */
  public BlockingInteractionResponse performBlockingInteraction(PerformBlockingInteraction performBlockingInteraction) throws AccessDenied,
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
                                                                                                                      PortletStateChangeRequired;

  /**
   * Handle events.
   * 
   * @param handleEvents the handle events
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
   */
  public HandleEventsResponse handleEvents(HandleEvents handleEvents) throws OperationNotSupported,
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
                                                                     PortletStateChangeRequired;

  /**
   * Inits the cookie.
   * 
   * @param initCookie the init cookie
   * 
   * @return the list< extension>
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws OperationFailed the operation failed
   */
  public List<Extension> initCookie(InitCookie initCookie) throws OperationNotSupported,
                                                    AccessDenied,
                                                    ResourceSuspended,
                                                    InvalidRegistration,
                                                    ModifyRegistrationRequired,
                                                    OperationFailed;

  /**
   * Release sessions.
   * 
   * @param releaseSessions the release sessions
   * 
   * @return the list< extension>
   * 
   * @throws OperationNotSupported the operation not supported
   * @throws AccessDenied the access denied
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws MissingParameters the missing parameters
   * @throws OperationFailed the operation failed
   */
  public List<Extension> releaseSessions(ReleaseSessions releaseSessions) throws OperationNotSupported,
                                                                   AccessDenied,
                                                                   ResourceSuspended,
                                                                   InvalidRegistration,
                                                                   ModifyRegistrationRequired,
                                                                   MissingParameters,
                                                                   OperationFailed;

}
