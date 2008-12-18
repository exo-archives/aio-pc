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

package org.exoplatform.services.wsrp1.bind;

import java.rmi.RemoteException;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp.MarkupOperationsInterface;
import org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType;
import org.exoplatform.services.wsrp1.type.AccessDeniedFault;
import org.exoplatform.services.wsrp1.type.BlockingInteractionRequest;
import org.exoplatform.services.wsrp1.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp1.type.InconsistentParametersFault;
import org.exoplatform.services.wsrp1.type.InitCookieRequest;
import org.exoplatform.services.wsrp1.type.InvalidCookieFault;
import org.exoplatform.services.wsrp1.type.InvalidHandleFault;
import org.exoplatform.services.wsrp1.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp1.type.InvalidSessionFault;
import org.exoplatform.services.wsrp1.type.InvalidUserCategoryFault;
import org.exoplatform.services.wsrp1.type.MarkupRequest;
import org.exoplatform.services.wsrp1.type.MarkupResponse;
import org.exoplatform.services.wsrp1.type.MissingParametersFault;
import org.exoplatform.services.wsrp1.type.OperationFailedFault;
import org.exoplatform.services.wsrp1.type.PortletStateChangeRequiredFault;
import org.exoplatform.services.wsrp1.type.ReleaseSessionsRequest;
import org.exoplatform.services.wsrp1.type.ReturnAny;
import org.exoplatform.services.wsrp1.type.UnsupportedLocaleFault;
import org.exoplatform.services.wsrp1.type.UnsupportedMimeTypeFault;
import org.exoplatform.services.wsrp1.type.UnsupportedModeFault;
import org.exoplatform.services.wsrp1.type.UnsupportedWindowStateFault;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPV1MarkupPortTypeImpl implements WSRPV1MarkupPortType {

  private MarkupOperationsInterface markupOperationsInterface;

  public WSRPV1MarkupPortTypeImpl() {
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
