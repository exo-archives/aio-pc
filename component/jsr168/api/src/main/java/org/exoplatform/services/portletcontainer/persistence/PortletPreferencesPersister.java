package org.exoplatform.services.portletcontainer.persistence;

import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 31 mai 2004
 */
public interface PortletPreferencesPersister {

  public ExoPortletPreferences getPortletPreferences(WindowID windowID) throws Exception;

  public void savePortletPreferences(WindowID windowID, ExoPortletPreferences preferences) throws Exception;
  
}