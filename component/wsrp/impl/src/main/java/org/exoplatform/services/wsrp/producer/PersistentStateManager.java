/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 14 janv. 2004
 */
package org.exoplatform.services.wsrp.producer;


import java.util.Map;

import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.RegistrationData;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public interface PersistentStateManager {

  public byte[] register(String registrationHandle, RegistrationData data) throws WSRPException;
  public RegistrationData getRegistrationData(RegistrationContext registrationContext) throws WSRPException;
  public void deregister(RegistrationContext registrationContext) throws WSRPException;
  public boolean isRegistered(RegistrationContext registrationContext) throws WSRPException;
  
  public boolean isConsumerConfiguredPortlet(String portletHandle, RegistrationContext registrationContext) throws WSRPException;
  public void addConsumerConfiguredPortletHandle(String portletHandle, RegistrationContext registrationContext) throws WSRPException;
  public void removeConsumerConfiguredPortletHandle(String portletHandle, RegistrationContext registrationContext) throws WSRPException;

  public Map getNavigationalSate(String navigationalState) throws WSRPException;
  public void putNavigationalState(String ns, Map renderParameters) throws WSRPException;


}
