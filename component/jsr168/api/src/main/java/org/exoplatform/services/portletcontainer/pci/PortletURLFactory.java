/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 13 janv. 2004
 */
package org.exoplatform.services.portletcontainer.pci;

import javax.portlet.PortletURL;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public interface PortletURLFactory {
  
  public static final String RENDER = "render";
  public static final String ACTION = "action";    

  public PortletURL createPortletURL(String Type);

}
