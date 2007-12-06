/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 19 d�c. 2003
 */
 
package org.exoplatform.services.wsrp.producer;

import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ServiceDescription;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public interface ServiceDescriptionInterface {
  public ServiceDescription getServiceDescription(RegistrationContext registrationContext, 
                                                  String[] desiredLocale) 
    throws java.rmi.RemoteException;  
}
