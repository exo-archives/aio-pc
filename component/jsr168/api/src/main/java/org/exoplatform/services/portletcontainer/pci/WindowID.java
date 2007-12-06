/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletcontainer.pci;

import java.io.Serializable;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public interface WindowID extends Serializable{

  public String getOwner();

  public String getPortletApplicationName();

  public String getPortletName();

  public String getUniqueID();

  public String generateKey();

}
