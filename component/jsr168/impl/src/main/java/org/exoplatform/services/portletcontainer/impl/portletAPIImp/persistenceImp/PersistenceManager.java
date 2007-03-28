package org.exoplatform.services.portletcontainer.impl.portletAPIImp.persistenceImp;


import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 31 mai 2004
 */
public interface PersistenceManager {
  public PortletWindowInternal getWindow(Input input, ExoPortletPreferences defaultPrefs); 
}
