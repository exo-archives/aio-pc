/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 19 d�c. 2003
 */
 
package org.exoplatform.services.wsrp.producer;

import java.rmi.RemoteException;

import org.exoplatform.services.wsrp.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp.type.PropertyList;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.UserContext;


/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public interface PortletManagementOperationsInterface {
  
  public PortletDescriptionResponse getPortletDescription(RegistrationContext registrationContext,
                                                          PortletContext portletContext,
                                                          UserContext userContext,
                                                          String[] desiredLocales) 
    throws RemoteException;

  public DestroyPortletsResponse destroyPortlets(RegistrationContext registrationContext,
                                                 String[] portletHandles) 
    throws RemoteException;

  public PortletContext setPortletProperties(RegistrationContext registrationContext,
                                             PortletContext portletContext,
                                             UserContext userContext,
                                             PropertyList propertyList) 
    throws RemoteException;

  public PropertyList getPortletProperties(RegistrationContext registrationContext,
                                           PortletContext portletContext,
                                           UserContext userContext,
                                           String[] names) 
    throws RemoteException;

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(RegistrationContext registrationContext,
                                                                          PortletContext portletContext,
                                                                          UserContext userContext,
                                                                          String[] desiredLocales) 
    throws RemoteException;


  public PortletContext clonePortlet(RegistrationContext registrationContext, 
                                     PortletContext portletContext, 
                                     UserContext userContext)
    throws RemoteException;                                 

}
