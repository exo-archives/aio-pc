/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.plugins.pc;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.model.Description;
import org.exoplatform.services.portletcontainer.pci.model.DisplayName;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.InitParam;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.SecurityRoleRef;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;
import org.exoplatform.services.portletcontainer.pci.model.UserDataConstraint;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.bundle.ResourceBundleManager;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class PortletDataImp implements PortletData {

  protected List<UserAttribute> userAttributes_;

  protected Portlet             portlet_;

  protected UserDataConstraint  userDataConstraintType_;

  protected ExoContainer        cont;

  public PortletDataImp(ExoContainer cont,
                        Portlet portlet,
                        UserDataConstraint userDataConstraintType,
                        List<UserAttribute> userAttributes) {
    this.cont = cont;
    this.portlet_ = portlet;
    this.userDataConstraintType_ = userDataConstraintType;
    this.userAttributes_ = userAttributes;
  }

  public Portlet getWrappedPortletTyped() {
    return portlet_;
  }

  public List<DisplayName> getDisplayName() {
    return portlet_.getDisplayName();
  }

  public List<SecurityRoleRef> getSecurityRoleRef() {
    return portlet_.getSecurityRoleRef();
  }

  public List<InitParam> getInitParam() {
    return portlet_.getInitParam();
  }

  public ResourceBundle getPortletInfo(Locale locale) {
    ResourceBundleManager manager = (ResourceBundleManager) cont.getComponentInstanceOfType(ResourceBundleManager.class);
    try {
      return manager.lookupBundle(portlet_, locale);
    } catch (Exception e) {
      return null;
    }
  }

  public List<Supports> getSupports() {
    return portlet_.getSupports();
  }

  public List<Description> getDescription() {
    return portlet_.getDescription();
  }

  public String getDescription(String lang) {
    return portlet_.getDescription(lang);
  }

  public boolean isCacheGlobal() {
    if ("true".equalsIgnoreCase(portlet_.getGlobalCache())) {
      return true;
    }
    return false;
  }

  public String getExpirationCache() {
    Integer s = portlet_.getCaching();
    if (s == null)
      return "0";
    return s.toString();
  }

  public String getPortletName() {
    return portlet_.getPortletName();
  }

  public List<Locale> getSupportedLocale() {
    return portlet_.getSupportedLocale();
  }

  public ExoPortletPreferences getPortletPreferences() {
    return portlet_.getPortletPreferences();
  }

  public boolean isSecure() {
    if (userDataConstraintType_ != null)
      return true;
    return false;
  }

  public List<UserAttribute> getUserAttributes() {
    return userAttributes_;
  }

  public List<QName> getSupportedProcessingEvent() {
    if (portlet_ == null)
      return null; // DONE for avoid exc for WSRPAdminPortlet
    return portlet_.getSupportedProcessingEvent();
  }

  public List<QName> getSupportedPublishingEvent() {
    return portlet_.getSupportedPublishingEvent();
  }

  public List<String> getSupportedPublicRenderParameter() {
    if (portlet_ == null)
      return null; // DONE for avoid exc for WSRPAdminPortlet
    return portlet_.getSupportedPublicRenderParameter();
  }

  public Map<String, String[]> getContainerRuntimeOption() {
    return portlet_.getContainerRuntimeOption();
  }

  public boolean getEscapeXml() {
    if (portlet_ == null)
      return true; // DONE for avoid exc for WSRPAdminPortlet
    return portlet_.getEscapeXml();
  }

}
