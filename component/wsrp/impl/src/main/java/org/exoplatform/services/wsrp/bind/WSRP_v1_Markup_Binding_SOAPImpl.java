/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 15 janv. 2004
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
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRP_v1_Markup_Binding_SOAPImpl implements WSRP_v1_Markup_PortType{
  
  private MarkupOperationsInterface markupOperationsInterface;
  
  public WSRP_v1_Markup_Binding_SOAPImpl() {
    markupOperationsInterface = (MarkupOperationsInterface) ExoContainerContext.getCurrentContainer().
        getComponentInstanceOfType(MarkupOperationsInterface.class);
  }  

  public MarkupResponse getMarkup(MarkupRequest markupRequest)
    throws RemoteException, InconsistentParametersFault, InvalidRegistrationFault, 
           MissingParametersFault, OperationFailedFault, UnsupportedMimeTypeFault, 
           UnsupportedModeFault, UnsupportedLocaleFault, InvalidUserCategoryFault, 
           InvalidSessionFault, InvalidCookieFault, AccessDeniedFault, InvalidHandleFault, 
           UnsupportedWindowStateFault {
    return markupOperationsInterface.getMarkup(markupRequest.getRegistrationContext(),
                                               markupRequest.getPortletContext(),
                                               markupRequest.getRuntimeContext(),
                                               markupRequest.getUserContext(),
                                               markupRequest.getMarkupParams());
  }

  public BlockingInteractionResponse performBlockingInteraction(BlockingInteractionRequest blockingInteractionRequest)
    throws RemoteException, InconsistentParametersFault, 
           InvalidRegistrationFault, MissingParametersFault, 
           OperationFailedFault, UnsupportedMimeTypeFault, 
           UnsupportedModeFault, UnsupportedLocaleFault, 
           InvalidUserCategoryFault, InvalidSessionFault, 
           InvalidCookieFault, PortletStateChangeRequiredFault, 
           AccessDeniedFault, InvalidHandleFault, UnsupportedWindowStateFault {
    return markupOperationsInterface.performBlockingInteraction(blockingInteractionRequest.getRegistrationContext(),
                                                                blockingInteractionRequest.getPortletContext(),
                                                                blockingInteractionRequest.getRuntimeContext(),
                                                                blockingInteractionRequest.getUserContext(),
                                                                blockingInteractionRequest.getMarkupParams(),
                                                                blockingInteractionRequest.getInteractionParams());
  }

  public ReturnAny releaseSessions(ReleaseSessionsRequest releaseSessionsRequest)
    throws RemoteException, InvalidRegistrationFault, OperationFailedFault, 
           MissingParametersFault, AccessDeniedFault {
    return markupOperationsInterface.releaseSessions(releaseSessionsRequest.getRegistrationContext(),
                                                     releaseSessionsRequest.getSessionIDs());
  }

  public ReturnAny initCookie(InitCookieRequest initCookie) 
    throws RemoteException, InvalidRegistrationFault, 
           OperationFailedFault, AccessDeniedFault {             
    return markupOperationsInterface.initCookie(initCookie.getRegistrationContext());
  }

}
