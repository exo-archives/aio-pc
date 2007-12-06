/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 2:10:13 AM
 */
package org.exoplatform.services.portletcontainer.helper;


import java.io.Serializable;
import javax.portlet.PortletPreferences;
import org.exoplatform.services.portletcontainer.pci.WindowID;

public class PortletWindowInternal implements Serializable {
  private PortletPreferences preferences;
  private WindowID windowID;

  public PortletWindowInternal() {
  }

  public PortletWindowInternal(WindowID windowID, PortletPreferences preferences) {
    this.windowID = windowID;
    this.preferences = preferences;
  }

  public WindowID getWindowID() {
    return windowID;
  }

  public PortletPreferences getPreferences() {
    return preferences;
  }

  public void setPreferences(PortletPreferences preferences) {
    this.preferences = preferences;
  }
}
