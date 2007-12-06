/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 8 janv. 2004
 */
package org.exoplatform.services.portletcontainer.pci;

import java.util.Locale;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class LocalisedDescription {

  private Locale locale;
  private String description;
    
  public LocalisedDescription (Locale locale, String description){
   this.locale = locale;
   this.description = description;
  }
     
  public String getDescription() {
   return description;
  }

  public Locale getLocale() {
   return locale;
  }
 
}
