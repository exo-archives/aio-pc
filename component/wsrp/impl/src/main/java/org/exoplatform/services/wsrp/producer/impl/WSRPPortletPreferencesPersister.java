/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.wsrp.producer.impl;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;


/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 8 juin 2004
 */
public class WSRPPortletPreferencesPersister implements PortletPreferencesPersister {

  private static WSRPPortletPreferencesPersister ourInstance = new WSRPPortletPreferencesPersister();

  public static WSRPPortletPreferencesPersister getInstance() {
    return ourInstance;
  }

  private WSRPPortletPreferencesPersister() {
  }

  private Map localMap = new HashMap();

  public ExoPortletPreferences getPortletPreferences(WindowID windowID) throws Exception {
    ExoPortletPreferences map = (ExoPortletPreferences) localMap.get(windowID.generateKey());
    if(map == null)
      return null;
    return map;
  }

  public void savePortletPreferences(WindowID windowID, ExoPortletPreferences preferences) throws Exception {
    localMap.put(windowID.generateKey(), preferences);
  }

}