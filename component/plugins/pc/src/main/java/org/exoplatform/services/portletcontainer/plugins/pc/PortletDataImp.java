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

  /**
   * User attributes.
   */
  protected List<UserAttribute> userAttributes;

  /**
   * Portlet object.
   */
  protected Portlet portlet;

  /**
   * User data constraint type.
   */
  protected UserDataConstraint userDataConstraintType;

  /**
   * Exo container.
   */
  protected ExoContainer cont;

  /**
   * @param cont exo container
   * @param portlet portlet object
   * @param userDataConstraintType user data constraint type
   * @param userAttributes user attributes
   */
  public PortletDataImp(final ExoContainer cont,
      final Portlet portlet,
      final UserDataConstraint userDataConstraintType,
      final List<UserAttribute> userAttributes) {
    this.cont = cont;
    this.portlet = portlet;
    this.userDataConstraintType = userDataConstraintType;
    this.userAttributes = userAttributes;
  }

  /**
   * @return portlet object
   */
  public final Portlet getWrappedPortletTyped() {
    return portlet;
  }

  /**
   * Overridden method.
   *
   * @return display names
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getDisplayName()
   */
  public final List<DisplayName> getDisplayName() {
    return portlet.getDisplayName();
  }

  /**
   * Overridden method.
   *
   * @return security role defs
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getSecurityRoleRef()
   */
  public final List<SecurityRoleRef> getSecurityRoleRef() {
    return portlet.getSecurityRoleRef();
  }

  /**
   * Overridden method.
   *
   * @return init params
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getInitParam()
   */
  public final List<InitParam> getInitParam() {
    return portlet.getInitParam();
  }

  /**
   * @param locale locale
   * @return portlet info
   */
  public final ResourceBundle getPortletInfo(final Locale locale) {
    ResourceBundleManager manager = (ResourceBundleManager) cont
        .getComponentInstanceOfType(ResourceBundleManager.class);
    try {
      return manager.lookupBundle(portlet, locale);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Overridden method.
   *
   * @return supports
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getSupports()
   */
  public final List<Supports> getSupports() {
    return portlet.getSupports();
  }

  /**
   * Overridden method.
   *
   * @return descriptions
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getDescription()
   */
  public final List<Description> getDescription() {
    return portlet.getDescription();
  }

  /**
   * Overridden method.
   *
   * @param lang language
   * @return description
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getDescription(java.lang.String)
   */
  public final String getDescription(final String lang) {
    return portlet.getDescription(lang);
  }

  /**
   * Overridden method.
   *
   * @return is cache global
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#isCacheGlobal()
   */
  public final boolean isCacheGlobal() {
    if ("true".equalsIgnoreCase(portlet.getGlobalCache()))
      return true;
    return false;
  }

  /**
   * Overridden method.
   *
   * @return expiration cache
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getExpirationCache()
   */
  public final String getExpirationCache() {
    Integer s = portlet.getCaching();
    if (s == null)
      return "0";
    return s.toString();
  }

  /**
   * Overridden method.
   *
   * @return portlet name
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getPortletName()
   */
  public final String getPortletName() {
    return portlet.getPortletName();
  }

  /**
   * Overridden method.
   *
   * @return supported locales
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getSupportedLocale()
   */
  public final List<Locale> getSupportedLocale() {
    return portlet.getSupportedLocale();
  }

  /**
   * Overridden method.
   *
   * @return portlet preferences
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getPortletPreferences()
   */
  public final ExoPortletPreferences getPortletPreferences() {
    return portlet.getPortletPreferences();
  }

  /**
   * Overridden method.
   *
   * @return is secure
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#isSecure()
   */
  public final boolean isSecure() {
    if (userDataConstraintType != null)
      return true;
    return false;
  }

  /**
   * Overridden method.
   *
   * @return user attributes
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getUserAttributes()
   */
  public final List<UserAttribute> getUserAttributes() {
    return userAttributes;
  }

  /**
   * Overridden method.
   *
   * @return supported processing events
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getSupportedProcessingEvent()
   */
  public final List<QName> getSupportedProcessingEvent() {
    if (portlet == null)
      return null;
    return portlet.getSupportedProcessingEvent();
  }

  /**
   * Overridden method.
   *
   * @return supported publishing events
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getSupportedPublishingEvent()
   */
  public final List<QName> getSupportedPublishingEvent() {
    return portlet.getSupportedPublishingEvent();
  }

  /**
   * Overridden method.
   *
   * @return supported public render parameters
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getSupportedPublicRenderParameter()
   */
  public final List<String> getSupportedPublicRenderParameter() {
    if (portlet == null)
      return null;
    return portlet.getSupportedPublicRenderParameter();
  }

  /**
   * Overridden method.
   *
   * @return container runtime options
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getContainerRuntimeOption()
   */
  public final Map<String, String[]> getContainerRuntimeOption() {
    return portlet.getContainerRuntimeOption();
  }

  /**
   * Overridden method.
   *
   * @return escape xml
   * @see org.exoplatform.services.portletcontainer.pci.PortletData#getEscapeXml()
   */
  public boolean getEscapeXml() {
    if (portlet == null)
      return true;
    return portlet.getEscapeXml();
  }

}
