/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.portletcontainer.test.filters;

import java.io.*;
import java.util.*;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id$
 */

public class WindowID2 extends ExoWindowID {
  private PortletMode portletMode;
  private WindowState windowState;

  public PortletMode getPortletMode() {
    return portletMode;
  }

  public void setPortletMode(PortletMode portletMode) {
    this.portletMode = portletMode;
  }

  public WindowState getWindowState() {
    return windowState;
  }

  public void setWindowState(WindowState windowState) {
    this.windowState = windowState;
  }
}
