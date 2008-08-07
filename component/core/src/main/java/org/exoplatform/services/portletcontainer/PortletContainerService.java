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
package org.exoplatform.services.portletcontainer;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 10 nov. 2003 Time: 09:40:23
 */
public interface PortletContainerService {

  /**
   * @param plugin plugin object
   */
  void addPlugin(PortletContainerPlugin plugin);

  /**
   * @param name name
   */
  void setName(String name);

  /**
   * @param description description
   */
  void setDescription(String description);

  /**
   * @param majorVersion major version
   */
  void setMajorVersion(int majorVersion);

  /**
   * @param minorVersion minor version
   */
  void setMinorVersion(int minorVersion);

  /**
   * @param properties properties
   */
  void setProperties(Map<String, String> properties);

  /**
   * @return portlet modes
   */
  Collection<PortletMode> getSupportedPortletModes();

  /**
   * @return window states
   */
  Collection<WindowState> getSupportedWindowStates();

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup
   * @return portlet modes
   */
  Collection<PortletMode> getPortletModes(String portletAppName, String portletName, String markup);

  String[] getPortalManagedPortletModes(String portletAppName, String portletName);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup
   * @param mode mode
   * @return either it is supported
   */
  boolean isModeSuported(String portletAppName, String portletName, String markup, PortletMode mode);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup
   * @return window states
   */
  Collection<WindowState> getWindowStates(String portletAppName, String portletName, String markup);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup
   * @param state state
   * @return either it is supported
   */
  boolean isStateSupported(String portletAppName,
                           String portletName,
                           String markup,
                           WindowState state);

  /**
   * @return all portlet metadata
   */
  Map<String, PortletData> getAllPortletMetaData();

  /**
   * @param portletAppName app name
   * @return app object
   */
  PortletApp getPortletApp(String portletAppName);

  /**
   * @param request request
   * @param response response
   * @param portletAppName app name
   * @param portletName portlet name
   * @param locale locale
   * @return bundle
   * @throws PortletContainerException exception
   */
  ResourceBundle getBundle(HttpServletRequest request,
                           HttpServletResponse response,
                           String portletAppName,
                           String portletName,
                           Locale locale) throws PortletContainerException;

  /**
   * @param input input
   * @param preferences preferences
   * @throws PortletContainerException exception
   */
  @Deprecated
  void setPortletPreference(Input input, Map<String, String> preferences) throws PortletContainerException;

  /**
   * @param input input
   * @param preferences preferences
   * @throws PortletContainerException exception
   */
  @Deprecated
  void setPortletPreference2(Input input, Map<String, String[]> preferences) throws PortletContainerException;

  /**
   * @param input input
   * @return preferences map
   */
  @Deprecated
  Map<String, String[]> getPortletPreference(Input input);

  /**
   * @param input input
   * @param preferences preferences
   * @throws PortletContainerException exception
   */
  void setPortletPreferences(Input input, PortletPreferences preferences) throws PortletContainerException;

  /**
   * @param input input
   * @return portlet preferences
   */
  PortletPreferences getPortletPreferences(Input input);

  /**
   * @param request request
   * @param response response
   * @param input input
   * @return output
   * @throws PortletContainerException exception
   */
  ActionOutput processAction(HttpServletRequest request,
                             HttpServletResponse response,
                             ActionInput input) throws PortletContainerException;

  /**
   * @param request request
   * @param response response
   * @param input input
   * @return output
   * @throws PortletContainerException exception
   */
  RenderOutput render(HttpServletRequest request, HttpServletResponse response, RenderInput input) throws PortletContainerException;

  /**
   * @param request request
   * @param response response
   * @param input input
   * @return output
   * @throws PortletContainerException exception
   */
  ResourceOutput serveResource(HttpServletRequest request,
                               HttpServletResponse response,
                               ResourceInput input) throws PortletContainerException;

  /**
   * @param request request
   * @param response response
   * @param input input
   * @return output
   * @throws PortletContainerException exception
   */
  EventOutput processEvent(HttpServletRequest request,
                           HttpServletResponse response,
                           EventInput input) throws PortletContainerException;

  /**
   * @param request request
   * @param response response
   * @param attrs attrs
   * @param portletApplicationName app name
   * @throws PortletContainerException exception
   */
  void sendAttrs(HttpServletRequest request,
                 HttpServletResponse response,
                 Map<String, Object> attrs,
                 String portletApplicationName) throws PortletContainerException;

  /**
   * @param portletAppName app name
   * @param eventName event name
   * @param payload payload
   * @return either payload type matches
   * @throws PortletContainerException exception
   */
  boolean isEventPayloadTypeMatches(String portletAppName, QName eventName, Object payload) throws PortletContainerException;

}
