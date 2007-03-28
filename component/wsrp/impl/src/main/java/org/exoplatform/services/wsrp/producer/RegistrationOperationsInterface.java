/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 19 d�c. 2003
 */
 
package org.exoplatform.services.wsrp.producer;

import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.RegistrationData;
import org.exoplatform.services.wsrp.type.RegistrationState;
import org.exoplatform.services.wsrp.type.ReturnAny;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public interface RegistrationOperationsInterface {
  public RegistrationContext register(RegistrationData data) 
    throws java.rmi.RemoteException;
    
  public RegistrationState modifyRegistration(RegistrationContext context, 
                                              RegistrationData data) 
    throws java.rmi.RemoteException;
    
  public ReturnAny deregister(RegistrationContext context) 
    throws java.rmi.RemoteException;
}
