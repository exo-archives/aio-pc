package org.exoplatform.services.portletcontainer.monitor;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 4 mai 2004
 */
public interface CachedData {

  char[] getContent();

  long getLastAccessTime();

  String getTitle();

  PortletMode getMode();

  WindowState getWindowState();
    
}
