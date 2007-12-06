/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 16 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.exoplatform.services.wsrp.type.RegistrationData;


/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class ConsumerContext implements Serializable{

  private String registrationHandle;
  private RegistrationData datas;
  private Collection clonedPortletHandles;

  public ConsumerContext(String registrationHandle, RegistrationData datas){
    this.registrationHandle = registrationHandle;
    this.datas = datas;    
    clonedPortletHandles = new ArrayList();    
  }
  
  public void addPortletHandle(String portletHandle){
    clonedPortletHandles.add(portletHandle);
  }
  
  public void removePortletHandle(String portletHandle){
    clonedPortletHandles.remove(portletHandle);
  }

  public String getRegistrationHandle() {
    return registrationHandle;
  }

  public RegistrationData getRegistationData() {
    return datas;
  }
  
  public boolean isPortletHandleRegistered(String portletHandle){
    return clonedPortletHandles.contains(portletHandle);
  }

}
