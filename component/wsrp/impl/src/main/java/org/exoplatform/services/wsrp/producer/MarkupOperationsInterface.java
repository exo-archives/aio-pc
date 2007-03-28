/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 19 d�c. 2003
 */
 
package org.exoplatform.services.wsrp.producer;

import org.exoplatform.services.wsrp.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp.type.InteractionParams;
import org.exoplatform.services.wsrp.type.MarkupParams;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ReturnAny;
import org.exoplatform.services.wsrp.type.RuntimeContext;
import org.exoplatform.services.wsrp.type.UserContext;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public interface MarkupOperationsInterface {
  public MarkupResponse getMarkup(RegistrationContext registrationContext, 
                                  PortletContext portletContext, 
                                  RuntimeContext runtimeContext, 
                                  UserContext userContext, 
                                  MarkupParams markupParams) 
    throws java.rmi.RemoteException;
    
  public BlockingInteractionResponse performBlockingInteraction(RegistrationContext registrationContext, 
                                                                PortletContext portletContext, 
                                                                RuntimeContext runtimeContext, 
                                                                UserContext userContext, 
                                                                MarkupParams markupParams, 
                                                                InteractionParams interactionParams) 
    throws java.rmi.RemoteException;
    
  public ReturnAny initCookie(RegistrationContext registrationContext) 
    throws java.rmi.RemoteException;
    
  public ReturnAny releaseSessions(RegistrationContext registrationContext, 
                                   String[] sessionIDs) 
    throws java.rmi.RemoteException;
}
