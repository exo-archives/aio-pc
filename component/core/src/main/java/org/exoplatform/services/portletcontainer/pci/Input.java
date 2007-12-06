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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;

/**
 * This objects must be created by the Portal that access this portlet
 * container. 
 * The windowID is a unique id that references a portlet window
 * in the user scope.
 * 
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 9:17:39 PM
 */

public class Input {

  private String                      markup;

  private PortletMode                 portletMode;

  private WindowState                 windowState;

  private Map<String, String>         userAttributes;

  private WindowID                    windowID;

  private List<Locale>                locales;

  private Map<String, String[]>       renderParameters;

  private boolean                     stateSaveOnClient;

  private byte[]                      portletState;

  private PortletPreferencesPersister portletPreferencesPersister;

  // for creating URL
  private PortletURLFactory           portletURLFactory;

  // for creating URL
  private String                      baseURL;

  // for creating URL
  private boolean                     escapeXml;

  public boolean isStateSaveOnClient() {
    return stateSaveOnClient;
  }

  public void setStateSaveOnClient(boolean stateSaveOnClient) {
    this.stateSaveOnClient = stateSaveOnClient;
  }

  public WindowID getInternalWindowID() {
    return windowID;
  }

  public void setInternalWindowID(WindowID windowID) {
    this.windowID = windowID;
  }

  public PortletMode getPortletMode() {
    return portletMode;
  }

  public void setPortletMode(PortletMode portletMode) {
    this.portletMode = portletMode;
  }

  public WindowState getWindowState() {
    return windowState;
  }

  public void setWindowState(WindowState windowState) {
    this.windowState = windowState;
  }

  public String getMarkup() {
    return markup;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public Map<String, String> getUserAttributes() {
    return userAttributes;
  }

  public void setUserAttributes(Map<String, String> userAttributes) {
    this.userAttributes = userAttributes;
  }

  public byte[] getPortletState() {
    return portletState;
  }

  public void setPortletState(byte[] portletState) {
    this.portletState = portletState;
  }

  public PortletPreferencesPersister getPortletPreferencesPersister() {
    return portletPreferencesPersister;
  }

  public void setPortletPreferencesPersister(PortletPreferencesPersister portletPreferencesPersister) {
    this.portletPreferencesPersister = portletPreferencesPersister;
  }

  public List<Locale> getLocales() {
    return locales;
  }

  public void setLocales(List<Locale> locales) {
    this.locales = locales;
  }

  public Map<String, String[]> getRenderParameters() {
    if (renderParameters == null) {
      renderParameters = new HashMap<String, String[]>();
    }
    return renderParameters;
  }

  public void setRenderParameters(Map<String, String[]> renderParameters) {
    if (renderParameters == null) {
      renderParameters = new HashMap<String, String[]>();
    }
    this.renderParameters = renderParameters;
  }

  public PortletURLFactory getPortletURLFactory() {
    return portletURLFactory;
  }

  public void setPortletURLFactory(PortletURLFactory portletURLFactory) {
    this.portletURLFactory = portletURLFactory;
  }

  public String getBaseURL() {
    return baseURL;
  }

  public void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
  }

  public boolean getEscapeXml() {
    return escapeXml;
  }

  public void setEscapeXml(boolean escapeXml) {
    this.escapeXml = escapeXml;
  }

}
