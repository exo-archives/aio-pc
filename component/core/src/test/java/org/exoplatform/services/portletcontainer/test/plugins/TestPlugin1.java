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
package org.exoplatform.services.portletcontainer.test.plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerPlugin;
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

public class TestPlugin1 implements PortletContainerPlugin {

  private String                      name;

  private String                      description;

  private HashMap<String, PortletApp> portletApp = new HashMap<String, PortletApp>();

  private Map<String, String>         portletprefs;

  public Map<String, PortletData> getAllPortletMetaData() {
    HashMap<String, PortletData> all = new HashMap<String, PortletData>();
    PortletData pd1 = new PortletDataTestImpl();
    all.put("Test/plug1", pd1);
    return all;
  }

  public ResourceBundle getBundle(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String portletAppName,
                                  String portletName,
                                  Locale locale) throws PortletContainerException {
    return null;
  }

  public PortletApp getPortletApp(String portletAppName) {
    return portletApp.get(portletAppName);
  }

  public void addPortletApp(String portletAppName, PortletApp portletApp) {
    this.portletApp.put(portletAppName, portletApp);
  }

  public Collection<PortletMode> getPortletModes(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    return null;
  }

  public Map<String, String[]> getPortletPreference(Input input) {

    HashMap<String, String[]> out = new HashMap<String, String[]>();
    String[] arr;
    arr = new String[5];
    Set<String> keys = portletprefs.keySet();
    int i = 0;
    for (String key : keys) {
      try {
        arr[i] = portletprefs.get(key);
        i++;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    out.put("testKey", arr);
    return out;
  }

  public Collection<PortletMode> getSupportedPortletModes() {
    ArrayList res = new ArrayList();
    Iterator it = portletApp.values().iterator();
    while (it.hasNext()) {
      PortletApp app = (PortletApp) it.next();
      if (app != null) {
        List ls = app.getCustomPortletMode();
        res.addAll(ls);
      }
    }
    return res;
  }

  public Collection<WindowState> getSupportedWindowStates() {
    ArrayList res = new ArrayList();
    Iterator it = portletApp.values().iterator();
    while (it.hasNext()) {
      PortletApp app = (PortletApp) it.next();
      if (app != null) {
        List ls = app.getCustomWindowState();
        res.addAll(ls);
      }
    }
    return res;
  }

  public Collection<WindowState> getWindowStates(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    return null;
  }

  public boolean isModeSuported(String portletAppName,
                                String portletName,
                                String markup,
                                PortletMode mode) {
    return false;
  }

  public boolean isStateSupported(String portletAppName,
                                  String portletName,
                                  String markup,
                                  WindowState state) {
    return false;
  }

  public ActionOutput processAction(HttpServletRequest request,
                                    HttpServletResponse response,
                                    ActionInput input) throws PortletContainerException {
    return null;
  }

  public EventOutput processEvent(HttpServletRequest request,
                                  HttpServletResponse response,
                                  EventInput input) throws PortletContainerException {
    EventOutput out = new EventOutput();
    out.setRenderParameter("TestParam", "TEstParamValue");
    return out;
  }

  public RenderOutput render(HttpServletRequest request,
                             HttpServletResponse response,
                             RenderInput input) throws PortletContainerException {
    RenderOutput out = new RenderOutput();
    System.out.println("Render Called in #1");
    out.setTitle("TEstTiTle");
    return out;
  }

  public void sendAttrs(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> attrs,
                        String portletApplicationName) throws PortletContainerException {
  }

  public ResourceOutput serveResource(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ResourceInput input) throws PortletContainerException {
    return null;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setMajorVersion(int majorVersion) {
  }

  public void setMinorVersion(int minorVersion) {
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPortletPreference(Input input, Map<String, String> preferences) throws PortletContainerException {
    this.portletprefs = preferences;
  }

  public void setProperties(Map<String, String> properties) {
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  public boolean isEventPayloadTypeMatches(String portletAppName,
                                           QName eventName,
                                           Serializable payload) throws PortletContainerException {
    return false;
  }

  public PortletPreferences getPortletPreferences(Input input) {
    return null;
  }

  public void setPortletPreference2(Input input, Map<String, String[]> preferences) throws PortletContainerException {
  }

  public void setPortletPreferences(Input input, PortletPreferences preferences) throws PortletContainerException {
  }

  public final String[] getPortalManagedPortletModes(final String portletAppName,
                                                     final String portletName) {
    return null;
  }

  /**
   * Get portlet app names.
   * 
   * @return collection of string
   */
  public final Collection<String> getPortletAppNames() {
    return Collections.unmodifiableSet(portletApp.keySet());
  }

}
