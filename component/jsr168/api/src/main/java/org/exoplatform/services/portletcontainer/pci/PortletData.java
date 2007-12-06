/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.portletcontainer.pci;

import java.util.List;
import org.exoplatform.services.portletcontainer.pci.model.*;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public interface PortletData {    
  //elements that must be found in the resource bundle
  public static final String PORTLET_TITLE = "javax.portlet.title";
  public static final String PORTLET_SHORT_TITLE = "javax.portlet.short-title";
  public static final String KEYWORDS = "javax.portlet.keywords";  
  
  public List getDisplayName();
  public List getSecurityRoleRef();
  public List getInitParam();
  //public java.util.ResourceBundle getPortletInfo(Locale locale);
  public List getSupports();
  public List getDescription();
  public String getDescription(String lang);
  public boolean isCacheGlobal();
  public String getExpirationCache();
  public String getPortletName();
  public List getSupportedLocale();
  public ExoPortletPreferences getPortletPreferences();   
  public boolean isSecure();
  public List getUserAttributes();
}
