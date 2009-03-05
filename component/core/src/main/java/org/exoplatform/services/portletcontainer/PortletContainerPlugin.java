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

import java.io.Serializable;
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

import org.exoplatform.container.component.ComponentPlugin;
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
 * Created by The eXo Platform SAS. Author : Roman Pedchenko
 * roman.pedchenko@exoplatform.com.ua
 */
public interface PortletContainerPlugin extends ComponentPlugin {
  
  public static final String LOCAL = "jsr";
  
  public static final String REMOTE = "wsrp";

  /**
   * Overridden method.
   * 
   * @param name name
   * @see org.exoplatform.container.component.ComponentPlugin#setName(java.lang.String)
   */
  void setName(String name);

  /**
   * Overridden method.
   * 
   * @param description description
   * @see org.exoplatform.container.component.ComponentPlugin#setDescription(java.lang.String)
   */
  void setDescription(String description);

  /**
   * Set major version.
   * 
   * @param majorVersion major version
   */
  void setMajorVersion(int majorVersion);

  /**
   * Set minor version.
   * 
   * @param minorVersion minor vesrion
   */
  void setMinorVersion(int minorVersion);

  /**
   * Set properties.
   * 
   * @param properties properties
   */
  void setProperties(Map<String, String> properties);

  /**
   * Get supported portlet modes.
   * 
   * @return app supported portlet modes
   */
  Collection<PortletMode> getSupportedPortletModes();

  /**
   * Get supported window states.
   * 
   * @return all supported window states
   */
  Collection<WindowState> getSupportedWindowStates();

  /**
   * Get portlet modes.
   * 
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup
   * @return supported portlet modes
   */
  Collection<PortletMode> getPortletModes(String portletAppName, String portletName, String markup);

  /**
   * Is mode suported.
   * 
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup
   * @param mode portlet mode
   * @return either the mode supported
   */
  boolean isModeSuported(String portletAppName, String portletName, String markup, PortletMode mode);

  /**
   * Get portal managed portlet modes.
   * 
   * @param portletAppName
   * @param portletName
   * @return
   */
  String[] getPortalManagedPortletModes(final String portletAppName, final String portletName);

  /**
   * Get window states.
   * 
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup
   * @return supported window states
   */
  Collection<WindowState> getWindowStates(String portletAppName, String portletName, String markup);

  /**
   * Is state supported.
   * 
   * @param portletAppName app name
   * @param portletName portlet name
   * @param markup markup
   * @param state window state
   * @return either the state supported
   */
  boolean isStateSupported(String portletAppName,
                           String portletName,
                           String markup,
                           WindowState state);

  /**
   * Get all portlet metadata.
   * 
   * @return all portlet metadata
   */
  Map<String, PortletData> getAllPortletMetaData();

  /**
   * Get portlet app.
   * 
   * @param portletAppName app name
   * @return app object
   */
  PortletApp getPortletApp(String portletAppName);

  /**
   * Get bundle.
   * 
   * @param request request
   * @param response response
   * @param portletAppName app name
   * @param portletName portlet name
   * @param locale locale
   * @return resource bundle
   * @throws PortletContainerException exception
   */
  ResourceBundle getBundle(HttpServletRequest request,
                           HttpServletResponse response,
                           String portletAppName,
                           String portletName,
                           Locale locale) throws PortletContainerException;

  /**
   * Set portlet preference.
   * 
   * @param input input
   * @param preferences preferences
   * @throws PortletContainerException exception
   */
  @Deprecated
  void setPortletPreference(Input input, Map<String, String> preferences) throws PortletContainerException;

  /**
   * Set portlet preference with string array.
   * 
   * @param input input
   * @param preferences preferences
   * @throws PortletContainerException exception
   */
  @Deprecated
  void setPortletPreference2(Input input, Map<String, String[]> preferences) throws PortletContainerException;

  /**
   * Get portlet preference.
   * 
   * @param input input
   * @return portlet preferences
   */
  @Deprecated
  Map<String, String[]> getPortletPreference(Input input);

  /**
   * Set portlet preferences.
   * 
   * @param input input
   * @param preferences preferences
   * @throws PortletContainerException exception
   */
  void setPortletPreferences(Input input, PortletPreferences preferences) throws PortletContainerException;

  /**
   * Get portlet preferences.
   * 
   * @param input input
   * @return portlet preferences
   */
  PortletPreferences getPortletPreferences(Input input);

  /**
   * Process action.
   * 
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
   * Process event.
   * 
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
   * Serve resource.
   * 
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
   * Render.
   * 
   * @param request request
   * @param response response
   * @param input input
   * @return output
   * @throws PortletContainerException exception
   */
  RenderOutput render(HttpServletRequest request, HttpServletResponse response, RenderInput input) throws PortletContainerException;

  /**
   * Send attrs.
   * 
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
   * Is event payload type matches.
   * 
   * @param portletAppName app name
   * @param eventName event name
   * @param payload payload
   * @return either payload is of correct type
   * @throws PortletContainerException exception
   */
  boolean isEventPayloadTypeMatches(String portletAppName, QName eventName, Serializable payload) throws PortletContainerException;
  
  /**
   * Get portlet app names.
   * 
   * @return Collection of string
   */
  Collection<String> getPortletAppNames();

}
