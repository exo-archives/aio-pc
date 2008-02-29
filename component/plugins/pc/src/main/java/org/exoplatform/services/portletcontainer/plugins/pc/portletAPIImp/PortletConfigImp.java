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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.xml.namespace.QName;

import org.exoplatform.services.portletcontainer.pci.model.CustomPortletMode;
import org.exoplatform.services.portletcontainer.pci.model.CustomWindowState;
import org.exoplatform.services.portletcontainer.pci.model.InitParam;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.SecurityConstraint;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.bundle.ResourceBundleManager;

/**
 * There is one object per portlet Lifetime. It can be pooled
 */
public class PortletConfigImp implements PortletConfig {

  private final Portlet portletDatas_;

  private final Map<String, InitParam> params_ = new HashMap<String, InitParam>();

  private final PortletContextImpl portletContext;

  private final List<SecurityConstraint> securityContraints;

  private final List<UserAttribute> userAttributes_;

  private final List<CustomPortletMode> customPortletModes_;

  private final List<CustomWindowState> customWindowStates_;

  private final String defaultNamespace;

  public PortletConfigImp(final Portlet portletDatas,
      final PortletContext portletContext,
      final List<SecurityConstraint> securityContraints,
      final List<UserAttribute> userAttributes,
      final List<CustomPortletMode> customPortletModes,
      final List<CustomWindowState> customWindowStates,
      final String defaultNamespace) {
    this.portletDatas_ = portletDatas;
    this.defaultNamespace = defaultNamespace;
    this.portletContext = (PortletContextImpl) portletContext;
    this.securityContraints = securityContraints;
    this.userAttributes_ = userAttributes;
    this.customPortletModes_ = customPortletModes;
    this.customWindowStates_ = customWindowStates;

    // optimize the accesses to init paramters with Map
    List<InitParam> l = portletDatas_.getInitParam();
    for (InitParam initParam : l) {
      params_.put(initParam.getName(), initParam);
    }
  }

  public String getPortletName() {
    return portletDatas_.getPortletName();
  }

  public PortletContext getPortletContext() {
    return portletContext;
  }

  public ResourceBundle getResourceBundle(final Locale locale) {
    ResourceBundleManager manager = (ResourceBundleManager) portletContext.getContainer()
        .getComponentInstanceOfType(ResourceBundleManager.class);
    return manager.lookupBundle(portletDatas_, locale);
  }

  public String getInitParameter(final String name) {
    if (name == null)
      throw new IllegalArgumentException("You cannot have null as a paramter");
    InitParam initParam = params_.get(name);
    if (initParam != null)
      return initParam.getValue();
    return null;
  }

  public Enumeration getInitParameterNames() {
    return new Vector(params_.keySet()).elements();
  }

  public Portlet getPortletDatas() {
    return portletDatas_;
  }

  public boolean needsSecurityContraints(final String portletName) {
    for (SecurityConstraint securityConstraint : securityContraints) {
      List<String> l = securityConstraint.getPortletCollection().getPortletName();
      for (String portlet : l) {
        if (portlet.equals(portletName))
          return true;
      }
    }
    return false;
  }

  public String getDefaultNamespace() {
    // normally the value is inherited from <default-namespace/> element of
    // portlet.xml
    return defaultNamespace;
  }

  public Enumeration getPublicRenderParameterNames() {
    return notNullEnumeration(portletDatas_.getSupportedPublicRenderParameter());
  }

  public Enumeration<QName> getPublishingEventQNames() {
    return notNullEnumeration(portletDatas_.getSupportedPublishingEvent());
  }

  public Enumeration<QName> getProcessingEventQNames() {
    return notNullEnumeration(portletDatas_.getSupportedProcessingEvent());
  }

  public Enumeration<Locale> getSupportedLocales() {
    return notNullEnumeration(portletDatas_.getSupportedLocale());
  }

  protected Enumeration notNullEnumeration(List list) {
    if (list == null)
      list = new ArrayList<String>();
    return Collections.enumeration(list);
  }

  public Map<String, String[]> getContainerRuntimeOptions() {
    Map<String, String[]> a = portletDatas_.getContainerRuntimeOption();
    Map<String, String[]> b = portletDatas_.getApplication().getContainerRuntimeOption();
    b.putAll(a);
    return b;
  }

}
