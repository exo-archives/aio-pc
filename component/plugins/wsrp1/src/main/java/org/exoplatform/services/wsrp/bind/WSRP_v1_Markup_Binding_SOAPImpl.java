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
import org.exoplatform.services.wsrp.intf.WSRP_v1_Markup_PortType;
import org.exoplatform.services.wsrp.producer.MarkupOperationsInterface;
import org.exoplatform.services.wsrp.type.AccessDeniedFault;
import org.exoplatform.services.wsrp.type.BlockingInteractionRequest;
import org.exoplatform.services.wsrp.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp.type.InconsistentParametersFault;
import org.exoplatform.services.wsrp.type.InitCookieRequest;
import org.exoplatform.services.wsrp.type.InvalidCookieFault;
import org.exoplatform.services.wsrp.type.InvalidHandleFault;
import org.exoplatform.services.wsrp.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp.type.InvalidSessionFault;
import org.exoplatform.services.wsrp.type.InvalidUserCategoryFault;
import org.exoplatform.services.wsrp.type.MarkupRequest;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.MissingParametersFault;
import org.exoplatform.services.wsrp.type.OperationFailedFault;
import org.exoplatform.services.wsrp.type.PortletStateChangeRequiredFault;
import org.exoplatform.services.wsrp.type.ReleaseSessionsRequest;
import org.exoplatform.services.wsrp.type.ReturnAny;
import org.exoplatform.services.wsrp.type.UnsupportedLocaleFault;
import org.exoplatform.services.wsrp.type.UnsupportedMimeTypeFault;
import org.exoplatform.services.wsrp.type.UnsupportedModeFault;
import org.exoplatform.services.wsrp.type.UnsupportedWindowStateFault;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRP_v1_Markup_Binding_SOAPImpl implements WSRP_v1_Markup_PortType {

  private MarkupOperationsInterface markupOperationsInterface;

  public WSRP_v1_Markup_Binding_SOAPImpl() {
    markupOperationsInterface = (MarkupOperationsInterface) ExoContainerContext.getCurrentContainer()
                                                                               .getComponentInstanceOfType(MarkupOperationsInterface.class);
  }

  public MarkupResponse getMarkup(MarkupRequest markupRequest) throws RemoteException,
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

  public BlockingInteractionResponse performBlockingInteraction(BlockingInteractionRequest blockingInteractionRequest) throws RemoteException,
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

  public ReturnAny releaseSessions(ReleaseSessionsRequest releaseSessionsRequest) throws RemoteException,
                                                                                 InvalidRegistrationFault,
                                                                                 OperationFailedFault,
                                                                                 MissingParametersFault,
                                                                                 AccessDeniedFault {
    return markupOperationsInterface.releaseSessions(releaseSessionsRequest.getRegistrationContext(),
                                                     releaseSessionsRequest.getSessionIDs());
  }

  public ReturnAny initCookie(InitCookieRequest initCookie) throws RemoteException,
                                                           InvalidRegistrationFault,
                                                           OperationFailedFault,
                                                           AccessDeniedFault {
    return markupOperationsInterface.initCookie(initCookie.getRegistrationContext());
  }

}
