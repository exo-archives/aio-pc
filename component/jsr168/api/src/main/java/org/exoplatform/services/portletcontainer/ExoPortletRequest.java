/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletcontainer;


import javax.portlet.PortletRequest;

import org.exoplatform.services.portletcontainer.pci.WindowID;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public interface ExoPortletRequest extends PortletRequest{

  public WindowID getWindowID();
  
}
