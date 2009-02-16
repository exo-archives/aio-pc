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
 * There is one object per portlet lifetime.
 */
public class PortletConfigImp implements PortletConfig {

  /**
   * Portlet datas.
   */
  private final Portlet hPortletDatas;

  /**
   * Parameters.
   */
  private final Map<String, InitParam> params = new HashMap<String, InitParam>();

  /**
   * Portlet context.
   */
  private final PortletContextImpl portletContext;

  /**
   * Security contraints.
   */
  private final List<SecurityConstraint> securityContraints;

  /**
   * User attributes.
   */
  private final List<UserAttribute> userAttributes;

  /**
   * Custom portlet modes.
   */
  private final List<CustomPortletMode> customPortletModes;

  /**
   * Custom window states.
   */
  private final List<CustomWindowState> customWindowStates;

  /**
   * Default namespace.
   */
  private final String defaultNamespace;

  /**
   * @param portletDatas portlet datas
   * @param portletContext portlet context
   * @param securityContraints security contraints
   * @param userAttributes user attributes
   * @param customPortletModes custom portlet modes
   * @param customWindowStates custom window states
   * @param defaultNamespace default namespace
   */
  public PortletConfigImp(final Portlet portletDatas,
      final PortletContext portletContext,
      final List<SecurityConstraint> securityContraints,
      final List<UserAttribute> userAttributes,
      final List<CustomPortletMode> customPortletModes,
      final List<CustomWindowState> customWindowStates,
      final String defaultNamespace) {
    this.hPortletDatas = portletDatas;
    this.defaultNamespace = defaultNamespace;
    this.portletContext = (PortletContextImpl) portletContext;
    this.securityContraints = securityContraints;
    this.userAttributes = userAttributes;
    this.customPortletModes = customPortletModes;
    this.customWindowStates = customWindowStates;

    // optimize the accesses to init paramters with Map
    List<InitParam> l = hPortletDatas.getInitParam();
    for (InitParam initParam : l)
      params.put(initParam.getName(), initParam);
  }

  /**
   * Overridden method.
   *
   * @return portlet name
   * @see javax.portlet.PortletConfig#getPortletName()
   */
  public final String getPortletName() {
    return hPortletDatas.getPortletName();
  }

  /**
   * Overridden method.
   *
   * @return portlet context
   * @see javax.portlet.PortletConfig#getPortletContext()
   */
  public final PortletContext getPortletContext() {
    return portletContext;
  }

  /**
   * Overridden method.
   *
   * @param locale locale
   * @return resource bundle
   * @see javax.portlet.PortletConfig#getResourceBundle(java.util.Locale)
   */
  public final ResourceBundle getResourceBundle(final Locale locale) {
    ResourceBundleManager manager = (ResourceBundleManager) portletContext.getContainer()
        .getComponentInstanceOfType(ResourceBundleManager.class);
    return manager.lookupBundle(hPortletDatas, locale);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return value
   * @see javax.portlet.PortletConfig#getInitParameter(java.lang.String)
   */
  public final String getInitParameter(final String name) {
    if (name == null)
      throw new IllegalArgumentException("You cannot have null as a paramter");
    InitParam initParam = params.get(name);
    if (initParam != null)
      return initParam.getValue();
    return null;
  }

  /**
   * Overridden method.
   *
   * @return init parameter names
   * @see javax.portlet.PortletConfig#getInitParameterNames()
   */
  public final Enumeration getInitParameterNames() {
    return new Vector(params.keySet()).elements();
  }

  /**
   * @return portlet datas
   */
  public final Portlet getPortletDatas() {
    return hPortletDatas;
  }

  /**
   * @param portletName portlet name
   * @return does it need security constraints
   */
  public final boolean needsSecurityContraints(final String portletName) {
    for (SecurityConstraint securityConstraint : securityContraints) {
      List<String> l = securityConstraint.getPortletCollection().getPortletName();
      for (String portlet : l)
        if (portlet.equals(portletName))
          return true;
    }
    return false;
  }

  /**
   * Overridden method.
   *
   * @return default namespace
   * @see javax.portlet.PortletConfig#getDefaultNamespace()
   */
  public final String getDefaultNamespace() {
    // normally the value is inherited from <default-namespace/> element of
    // portlet.xml
    return defaultNamespace;
  }

  /**
   * Overridden method.
   *
   * @return public render parameter names
   * @see javax.portlet.PortletConfig#getPublicRenderParameterNames()
   */
  public final Enumeration getPublicRenderParameterNames() {
    return notNullEnumeration(hPortletDatas.getSupportedPublicRenderParameter());
  }

  /**
   * Overridden method.
   *
   * @return publishing event qnames
   * @see javax.portlet.PortletConfig#getPublishingEventQNames()
   */
  public final Enumeration<QName> getPublishingEventQNames() {
    return notNullEnumeration(hPortletDatas.getSupportedPublishingEvent());
  }

  /**
   * Overridden method.
   *
   * @return processing event qnames
   * @see javax.portlet.PortletConfig#getProcessingEventQNames()
   */
  public final Enumeration<QName> getProcessingEventQNames() {
    return notNullEnumeration(hPortletDatas.getSupportedProcessingEvent());
  }

  /**
   * Overridden method.
   *
   * @return supported locales
   * @see javax.portlet.PortletConfig#getSupportedLocales()
   */
  public final Enumeration<Locale> getSupportedLocales() {
    return notNullEnumeration(hPortletDatas.getSupportedLocale());
  }

  /**
   * @param list list
   * @return not null enumeration of a list
   */
  protected final Enumeration notNullEnumeration(List list) {
    if (list == null)
      list = new ArrayList<String>();
    return Collections.enumeration(list);
  }

  /**
   * Overridden method.
   *
   * @return container runtime options
   * @see javax.portlet.PortletConfig#getContainerRuntimeOptions()
   */
  public final Map<String, String[]> getContainerRuntimeOptions() {
    Map<String, String[]> a = hPortletDatas.getContainerRuntimeOption();
    Map<String, String[]> b = hPortletDatas.getApplication().getContainerRuntimeOption();
    b.putAll(a);
    return b;
  }

}
