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
  public static final String PORTLET_TITLE       = "javax.portlet.title";

  public static final String PORTLET_SHORT_TITLE = "javax.portlet.short-title";

  public static final String KEYWORDS            = "javax.portlet.keywords";

  public List<DisplayName> getDisplayName();

  public List<SecurityRoleRef> getSecurityRoleRef();

  public List<InitParam> getInitParam();

  // public java.util.ResourceBundle getPortletInfo(Locale locale);
  public List<Supports> getSupports();

  public List<Description> getDescription();

  public String getDescription(String lang);

  public boolean isCacheGlobal();

  public String getExpirationCache();

  public String getPortletName();

  public List<Locale> getSupportedLocale();

  public ExoPortletPreferences getPortletPreferences();

  public boolean isSecure();

  public List<UserAttribute> getUserAttributes();

  public List<QName> getSupportedProcessingEvent();

  public List<QName> getSupportedPublishingEvent();

  public List<String> getSupportedPublicRenderParameter();

  public Map<String, String[]> getContainerRuntimeOption();

  public boolean getEscapeXml();

}
