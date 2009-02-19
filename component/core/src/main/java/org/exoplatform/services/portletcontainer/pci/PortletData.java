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
package org.exoplatform.services.portletcontainer.pci;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;

import org.exoplatform.services.portletcontainer.pci.model.Description;
import org.exoplatform.services.portletcontainer.pci.model.DisplayName;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.InitParam;
import org.exoplatform.services.portletcontainer.pci.model.SecurityRoleRef;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface PortletData {

  // elements that must be found in the resource bundle

  /**
   * Constant for 'title' key.
   */
  String PORTLET_TITLE = "javax.portlet.title";

  /**
   * Constant for 'short-title' key.
   */
  String PORTLET_SHORT_TITLE = "javax.portlet.short-title";

  /**
   * Constant for 'keywords' key.
   */
  String KEYWORDS = "javax.portlet.keywords";

  /**
   * @return display names
   */
  List<DisplayName> getDisplayName();

  /**
   * @return security role refs
   */
  List<SecurityRoleRef> getSecurityRoleRef();

  /**
   * @return init params
   */
  List<InitParam> getInitParam();

  /**
   * @return supports
   */
  List<Supports> getSupports();

  /**
   * @return descriptions
   */
  List<Description> getDescription();

  /**
   * @param lang language
   * @return description
   */
  String getDescription(String lang);

  /**
   * @return is cache global
   */
  boolean isCacheGlobal();

  /**
   * @return expiration cache
   */
  String getExpirationCache();

  /**
   * @return portlet name
   */
  String getPortletName();

  /**
   * @return supported locales
   */
  List<Locale> getSupportedLocale();

  /**
   * @return portlet preferences
   */
  ExoPortletPreferences getPortletPreferences();

  /**
   * @return is secure
   */
  boolean isSecure();

  /**
   * @return user attributes
   */
  List<UserAttribute> getUserAttributes();

  /**
   * @return supported processing events
   */
  List<QName> getSupportedProcessingEvent();

  /**
   * @return supported publishing events
   */
  List<QName> getSupportedPublishingEvent();

  /**
   * @return supported public render parameters
   */
  List<String> getSupportedPublicRenderParameter();

  /**
   * @return container runtime options
   */
  Map<String, String[]> getContainerRuntimeOption();

  /**
   * @return escapeXml
   */
  boolean getEscapeXml();

}
