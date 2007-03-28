/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 6 janv. 2004
 */
package org.exoplatform.services.portletcontainer.impl;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.bundle.ResourceBundleManager;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.model.*;


/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class PortletDataImp implements PortletData {

  private List userAttributes_;
  private Portlet portlet_;
  private UserDataConstraint userDataConstraintType_;
  protected ExoContainer cont;

  public PortletDataImp(ExoContainer cont, Portlet portlet,
                        UserDataConstraint userDataConstraintType,
                        List userAttributes) {
    this.portlet_ = portlet;
    this.userDataConstraintType_ = userDataConstraintType;
    this.userAttributes_ = userAttributes;
    this.cont = cont;
  }

  public Portlet getWrappedPortletTyped() { return portlet_; }

  public List getDisplayName() { return portlet_.getDisplayName(); }

  public List getSecurityRoleRef() { return portlet_.getSecurityRoleRef(); }

  public List getInitParam() {  return portlet_.getInitParam(); }

  public ResourceBundle getPortletInfo(Locale locale) {
    ResourceBundleManager manager = 
      (ResourceBundleManager)cont.getComponentInstanceOfType(ResourceBundleManager.class);
    try {
      return manager.lookupBundle(portlet_, locale);
    } catch (Exception e) {
      return null;
    }
  }

  public List getSupports() { return portlet_.getSupports(); }

  public List getDescription() { return portlet_.getDescription(); }
  
  public String getDescription(String lang) { return portlet_.getDescription(lang) ; }

  public boolean isCacheGlobal() {
    if ("true".equalsIgnoreCase(portlet_.getGlobalCache())) {
      return true;
    }     
    return false;
  }

  public String getExpirationCache() {
    String s = portlet_.getExpirationCache(); ;
    if(s == null) return "" ;
    return s ;
  }

  public String getPortletName() {  return portlet_.getPortletName(); }

  public List getSupportedLocale() { return portlet_.getSupportedLocale(); }

  public ExoPortletPreferences getPortletPreferences() {
    return portlet_.getPortletPreferences();
  }

  public boolean isSecure() {
    if (userDataConstraintType_ != null)  return true;
    return false;
  }

  public List getUserAttributes() { return userAttributes_; }
}