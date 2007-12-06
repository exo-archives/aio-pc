/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 7 janv. 2004
 */
package org.exoplatform.services.portletcontainer.pci;

import java.util.List;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class CustomModeWithDescription {
  
  private javax.portlet.PortletMode portletMode;
  private List descriptions;
  
  public CustomModeWithDescription(javax.portlet.PortletMode portletMode, 
                                   List descriptions){
    this.portletMode = portletMode;
    this.descriptions = descriptions;
  }
  
  /**
   * @return a List of LocalisedDescription objects
   */
  public List getDescriptions() {
    return descriptions;
  }

  public javax.portlet.PortletMode getPortletMode() {
    return portletMode;
  }    
  


}
