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
public class CustomWindowStateWithDescription {
  
  private javax.portlet.WindowState windowState;
  private List descriptions;
  
  public CustomWindowStateWithDescription(javax.portlet.WindowState windowState, 
                                          List descriptions){
    this.windowState = windowState;
    this.descriptions = descriptions;
  }
  
  /**
   * @return a List of LocalisedDescription objects
   */  
  public List getDescriptions() {
    return descriptions;
  }

  public javax.portlet.WindowState getWindowState() {
    return windowState;
  }    

  public class Description {
    private String lang;
    private String description;
     
    public Description(String lang, String description){
       this.lang = lang;
       this.description = description;
    }
     
    public String getDescription() {
      return description;
    }

    public String getLang() {
      return lang;
    }

  }

}
