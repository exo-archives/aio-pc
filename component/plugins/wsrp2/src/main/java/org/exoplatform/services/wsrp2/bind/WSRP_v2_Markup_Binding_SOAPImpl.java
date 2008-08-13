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
import org.exoplatform.services.wsrp2.intf.WSRP_v2_Markup_PortType;
import org.exoplatform.services.wsrp2.producer.MarkupOperationsInterface;
import org.exoplatform.services.wsrp2.type.AccessDeniedFault;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetResource;
import org.exoplatform.services.wsrp2.type.HandleEvents;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.InconsistentParametersFault;
import org.exoplatform.services.wsrp2.type.InitCookie;
import org.exoplatform.services.wsrp2.type.InvalidCookieFault;
import org.exoplatform.services.wsrp2.type.InvalidHandleFault;
import org.exoplatform.services.wsrp2.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp2.type.InvalidSessionFault;
import org.exoplatform.services.wsrp2.type.InvalidUserCategoryFault;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.MissingParametersFault;
import org.exoplatform.services.wsrp2.type.ModifyRegistrationRequiredFault;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.OperationNotSupportedFault;
import org.exoplatform.services.wsrp2.type.PerformBlockingInteraction;
import org.exoplatform.services.wsrp2.type.PortletStateChangeRequiredFault;
import org.exoplatform.services.wsrp2.type.ReleaseSessions;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.ResourceSuspendedFault;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.UnsupportedLocaleFault;
import org.exoplatform.services.wsrp2.type.UnsupportedMimeTypeFault;
import org.exoplatform.services.wsrp2.type.UnsupportedModeFault;
import org.exoplatform.services.wsrp2.type.UnsupportedWindowStateFault;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRP_v2_Markup_Binding_SOAPImpl implements WSRP_v2_Markup_PortType {

  private MarkupOperationsInterface markupOperationsInterface;

  public WSRP_v2_Markup_Binding_SOAPImpl() {
    markupOperationsInterface = (MarkupOperationsInterface) ExoContainerContext.getCurrentContainer()
                                                                               .getComponentInstanceOfType(MarkupOperationsInterface.class);
  }

  public MarkupResponse getMarkup(GetMarkup markupRequest) throws RemoteException,
                                                          InconsistentParametersFault,
                                                          InvalidRegistrationFault,
                                                          MissingParametersFault,
                                                          OperationFailedFault,
                                                          UnsupportedMimeTypeFault,
                                                          UnsupportedModeFault,
                                                          UnsupportedLocaleFault,
                                                          InvalidUserCategoryFault,
                                                          InvalidSessionFault,
                                                          InvalidCookieFault,
                                                          AccessDeniedFault,
                                                          InvalidHandleFault,
                                                          UnsupportedWindowStateFault {
    return markupOperationsInterface.getMarkup(markupRequest.getRegistrationContext(),
                                               markupRequest.getPortletContext(),
                                               markupRequest.getRuntimeContext(),
                                               markupRequest.getUserContext(),
                                               markupRequest.getMarkupParams());
  }

  public ResourceResponse getResource(GetResource getResource) throws RemoteException,
                                                              InconsistentParametersFault,
                                                              ResourceSuspendedFault,
                                                              InvalidRegistrationFault,
                                                              MissingParametersFault,
                                                              OperationFailedFault,
                                                              UnsupportedMimeTypeFault,
                                                              UnsupportedModeFault,
                                                              OperationNotSupportedFault,
                                                              UnsupportedLocaleFault,
                                                              InvalidUserCategoryFault,
                                                              InvalidSessionFault,
                                                              ModifyRegistrationRequiredFault,
                                                              InvalidCookieFault,
                                                              AccessDeniedFault,
                                                              InvalidHandleFault,
                                                              UnsupportedWindowStateFault {
    return markupOperationsInterface.getResource(getResource.getRegistrationContext(),
                                                 getResource.getPortletContext(),
                                                 getResource.getRuntimeContext(),
                                                 getResource.getUserContext(),
                                                 getResource.getResourceParams());

  }

  public BlockingInteractionResponse performBlockingInteraction(PerformBlockingInteraction blockingInteractionRequest) throws RemoteException,
                                                                                                                      InconsistentParametersFault,
                                                                                                                      InvalidRegistrationFault,
                                                                                                                      MissingParametersFault,
                                                                                                                      OperationFailedFault,
                                                                                                                      UnsupportedMimeTypeFault,
                                                                                                                      UnsupportedModeFault,
                                                                                                                      UnsupportedLocaleFault,
                                                                                                                      InvalidUserCategoryFault,
                                                                                                                      InvalidSessionFault,
                                                                                                                      InvalidCookieFault,
                                                                                                                      PortletStateChangeRequiredFault,
                                                                                                                      AccessDeniedFault,
                                                                                                                      InvalidHandleFault,
                                                                                                                      UnsupportedWindowStateFault {
    return markupOperationsInterface.performBlockingInteraction(blockingInteractionRequest.getRegistrationContext(),
                                                                blockingInteractionRequest.getPortletContext(),
                                                                blockingInteractionRequest.getRuntimeContext(),
                                                                blockingInteractionRequest.getUserContext(),
                                                                blockingInteractionRequest.getMarkupParams(),
                                                                blockingInteractionRequest.getInteractionParams());
  }

  public ReturnAny releaseSessions(ReleaseSessions releaseSessionsRequest) throws RemoteException,
                                                                          InvalidRegistrationFault,
                                                                          OperationFailedFault,
                                                                          MissingParametersFault,
                                                                          AccessDeniedFault {
    return markupOperationsInterface.releaseSessions(releaseSessionsRequest.getRegistrationContext(),
                                                     releaseSessionsRequest.getSessionIDs());
  }

  public ReturnAny initCookie(InitCookie initCookie) throws RemoteException,
                                                    InvalidRegistrationFault,
                                                    OperationFailedFault,
                                                    AccessDeniedFault {
    return markupOperationsInterface.initCookie(initCookie.getRegistrationContext());
  }

  public HandleEventsResponse handleEvents(HandleEvents handleEvents) throws java.rmi.RemoteException,
                                                                     InconsistentParametersFault,
                                                                     ResourceSuspendedFault,
                                                                     InvalidRegistrationFault,
                                                                     MissingParametersFault,
                                                                     OperationFailedFault,
                                                                     UnsupportedMimeTypeFault,
                                                                     UnsupportedModeFault,
                                                                     UnsupportedLocaleFault,
                                                                     OperationNotSupportedFault,
                                                                     InvalidUserCategoryFault,
                                                                     InvalidSessionFault,
                                                                     ModifyRegistrationRequiredFault,
                                                                     InvalidCookieFault,
                                                                     PortletStateChangeRequiredFault,
                                                                     AccessDeniedFault,
                                                                     InvalidHandleFault,
                                                                     UnsupportedWindowStateFault {
    return markupOperationsInterface.handleEvents(handleEvents.getRegistrationContext(),
                                                  handleEvents.getPortletContext(),
                                                  handleEvents.getRuntimeContext(),
                                                  handleEvents.getUserContext(),
                                                  handleEvents.getMarkupParams(),
                                                  handleEvents.getEventParams());
  }

}
