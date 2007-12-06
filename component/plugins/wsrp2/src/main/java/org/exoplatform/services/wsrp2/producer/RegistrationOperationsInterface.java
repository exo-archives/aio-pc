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

import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public interface RegistrationOperationsInterface {
  public RegistrationContext register(RegistrationData data,
                                      Lifetime lifetime,
                                      UserContext userContext) 
    throws java.rmi.RemoteException;
  
  
  public RegistrationContext register(RegistrationData data) 
  throws java.rmi.RemoteException;
  
    
  public RegistrationState modifyRegistration(RegistrationContext context, 
                                              RegistrationData data,
                                              UserContext userContext) 
    throws java.rmi.RemoteException;
  
  
  public RegistrationState modifyRegistration(RegistrationContext context, 
                                              RegistrationData data)
  
    throws java.rmi.RemoteException;
  
    
  public ReturnAny deregister(RegistrationContext context,
                              UserContext userContext) 
    throws java.rmi.RemoteException;
  
  public ReturnAny deregister(RegistrationContext context)
  
    throws java.rmi.RemoteException;
  
  
  public Lifetime getRegistrationLifetime (RegistrationContext registrationContext, 
                                           UserContext userContext)
  throws java.rmi.RemoteException;
  
  public Lifetime setRegistrationLifetime (RegistrationContext registrationContext, 
                                             UserContext userContext, 
                                             Lifetime lifetime)
   throws java.rmi.RemoteException;
}
