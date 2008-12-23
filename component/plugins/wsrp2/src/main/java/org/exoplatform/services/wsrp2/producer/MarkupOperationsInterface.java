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

  public ReturnAny initCookie(RegistrationContext registrationContext, UserContext userContext) throws OperationNotSupported,
                                                                                               AccessDenied,
                                                                                               ResourceSuspended,
                                                                                               InvalidRegistration,
                                                                                               ModifyRegistrationRequired,
                                                                                               OperationFailed,
                                                                                               WSRPException;

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
