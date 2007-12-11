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

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 10 nov. 2003 Time: 09:40:23
 */
public interface PortletContainerService {

  public void addPlugin(PortletContainerPlugin plugin);

  public void setName(String name);

  public void setDescription(String description);

  public void setMajorVersion(int majorVersion);

  public void setMinorVersion(int minorVersion);

  public void setProperties(Map<String, String> properties);

  // public void setSupportedPortletModesWithDescriptions(Collection
  // portletModes);

  // public void setSupportedWindowStatesWithDescriptions(Collection
  // windowStates);

  public Collection<PortletMode> getSupportedPortletModes();

  public Collection<WindowState> getSupportedWindowStates();

  // public Collection getSupportedPortletModesWithDescriptions();

  // public Collection getSupportedWindowStatesWithDescriptions();

  public Collection<PortletMode> getPortletModes(String portletAppName,
                                                 String portletName,
                                                 String markup);

  public boolean isModeSuported(String portletAppName,
                                String portletName,
                                String markup,
                                PortletMode mode);

  public Collection<WindowState> getWindowStates(String portletAppName,
                                                 String portletName,
                                                 String markup);

  public boolean isStateSupported(String portletAppName,
                                  String portletName,
                                  String markup,
                                  WindowState state);

  public Map<String, PortletData> getAllPortletMetaData();

  public ResourceBundle getBundle(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String portletAppName,
                                  String portletName,
                                  Locale locale) throws PortletContainerException;

  public void setPortletPreference(Input input,
                                   Map<String, String> preferences) throws PortletContainerException;

  public Map<String, String[]> getPortletPreference(Input input);

  public ActionOutput processAction(HttpServletRequest request,
                                    HttpServletResponse response,
                                    ActionInput input) throws PortletContainerException;

  public RenderOutput render(HttpServletRequest request,
                             HttpServletResponse response,
                             RenderInput input) throws PortletContainerException;

  public ResourceOutput serveResource(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ResourceInput input) throws PortletContainerException;

  public EventOutput processEvent(HttpServletRequest request,
                                  HttpServletResponse response,
                                  EventInput input) throws PortletContainerException;

  public void sendAttrs(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> attrs,
                        String portletApplicationName) throws PortletContainerException;

  public boolean isEventPayloadTypeMatches(String portletAppName,
                                           QName eventName,
                                           Object payload) throws PortletContainerException;

}
