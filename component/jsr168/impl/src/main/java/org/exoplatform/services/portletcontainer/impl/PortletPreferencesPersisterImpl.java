package org.exoplatform.services.portletcontainer.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;

public class PortletPreferencesPersisterImpl implements
		PortletPreferencesPersister, Serializable {

  private Map prefs = new HashMap();
  private transient Log log_;

  public PortletPreferencesPersisterImpl(LogService lservice) {
    log_ = lservice.getLog(getClass()); 
  }

  public ExoPortletPreferences getPortletPreferences(WindowID windowID) throws Exception {
    return (ExoPortletPreferences) prefs.get(windowID.generateKey());       
  }

  public void savePortletPreferences(WindowID windowID, 
                                     ExoPortletPreferences exoPref) throws Exception {
    prefs.put(windowID.generateKey(), exoPref);        
  }

}
