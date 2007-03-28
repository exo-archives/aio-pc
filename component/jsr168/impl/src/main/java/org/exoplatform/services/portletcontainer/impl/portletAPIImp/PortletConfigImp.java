/*
Copyright 2001-2003 The eXo Platform SARL
All rights reserved
*/

/**
 * Created by The eXo Platform SARL
 * Date: Jul 25, 2003
 * Time: 6:11:15 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.bundle.ResourceBundleManager;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletContextImpl;
import org.exoplatform.services.portletcontainer.pci.model.*;



/**
 * There is one object per portlet Lifetime.
 * It can be pooled
 */
public class PortletConfigImp implements PortletConfig {

  private Portlet portletDatas_;
  private Map params_ = new HashMap();
  private PortletContextImpl portletContext;
  private List securityContraints;
  private List userAttributes_;
  private List customPortletModes_;
  private List customWindowStates_;

  public PortletConfigImp(Portlet portletDatas,
                          PortletContext portletContext,
                          List securityContraints,
                          List userAttributes,
                          List customPortletModes,
                          List customWindowStates) {
    this.portletDatas_ = portletDatas;
    this.portletContext = (PortletContextImpl) portletContext;
    this.securityContraints = securityContraints;
    this.userAttributes_ = userAttributes;
    this. customPortletModes_ = customPortletModes;
    this.customWindowStates_ = customWindowStates;

    //optimize the accesses to init paramters with Map
    List l = portletDatas.getInitParam();
    for (Iterator iterator = l.iterator(); iterator.hasNext();) {
      InitParam initParam = (InitParam) iterator.next();
      params_.put(initParam.getName(), initParam);
    }
  }

  public String getPortletName() { return portletDatas_.getPortletName();}

  public PortletContext getPortletContext() {
    return portletContext;
  }

  public ResourceBundle getResourceBundle(Locale locale) {
    ResourceBundleManager manager = (ResourceBundleManager) portletContext.getContainer().
      getComponentInstanceOfType(ResourceBundleManager.class);    
    return manager.lookupBundle(portletDatas_ , locale);
  }

  public String getInitParameter(String name) {
    if (name == null ) {
      throw new IllegalArgumentException("You cannot have null as a paramter") ;
    }
    InitParam initParam = (InitParam) params_.get(name);
    if (initParam != null)  return initParam.getValue();
    return null;
  }

  public Enumeration getInitParameterNames() {
    return new Vector(params_.keySet()).elements();
  }

  public Portlet getPortletDatas() {  return portletDatas_; }

  public boolean needsSecurityContraints(String portletName) {
    for (Iterator iterator = securityContraints.iterator(); iterator.hasNext();) {
      SecurityConstraint securityConstraint = (SecurityConstraint) iterator.next();
      List l = securityConstraint.getPortletCollection().getPortletName();
      for (Iterator iterator2 = l.iterator(); iterator2.hasNext();) {
        String portlet = (String) iterator2.next();
        if(portlet.equals(portletName)) return true;
      }
    }
    return false;
  }
}