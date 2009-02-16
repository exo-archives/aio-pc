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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;

/**
 * This objects must be created by the Portal that access this portlet
 * container. The windowID is a unique id that references a portlet window in
 * the user scope. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Jul 27, 2003 Time: 9:17:39 PM
 */

public class Input {

  /**
   * Markup.
   */
  private String markup;

  /**
   * Portlet mode.
   */
  private PortletMode portletMode;

  /**
   * Window state.
   */
  private WindowState windowState;

  /**
   * User attributes.
   */
  private Map<String, String> userAttributes;

  /**
   * windowID.
   */
  private WindowID windowID;

  /**
   * Locales.
   */
  private List<Locale> locales;

  /**
   * Render parameters.
   */
  private Map<String, String[]> renderParameters;

  /**
   * Save on client.
   */
  private boolean stateSaveOnClient;

  /**
   * Portlet state.
   */
  private byte[] portletState;

  /**
   * Portlet preferences persister.
   */
  private PortletPreferencesPersister portletPreferencesPersister;

  /**
   * Portlet URL factory.
   */
  private PortletURLFactory portletURLFactory;

  /**
   * Base URL.
   */
  private String baseURL;

  /**
   * escapeXml.
   */
  private boolean escapeXml;

  /**
   * Public param name list.
   */
  private List<String> pubNames;
  
  /**
   * Property parameters extracted from http request.
   */
  private HashMap<String, String[]> propertyParams;

  /**
   * @return either state is being saved on client side
   */
  public final boolean isStateSaveOnClient() {
    return stateSaveOnClient;
  }

  /**
   * @param stateSaveOnClient either state is being saved on client side
   */
  public final void setStateSaveOnClient(final boolean stateSaveOnClient) {
    this.stateSaveOnClient = stateSaveOnClient;
  }

  /**
   * @return window id object
   */
  public final WindowID getInternalWindowID() {
    return windowID;
  }

  /**
   * @param windowID1 window id object
   */
  public final void setInternalWindowID(final WindowID windowID1) {
    this.windowID = windowID1;
  }

  /**
   * @return portlet mode
   */
  public final PortletMode getPortletMode() {
    return portletMode;
  }

  /**
   * @param portletMode portlet mode
   */
  public final void setPortletMode(final PortletMode portletMode) {
    this.portletMode = portletMode;
  }

  /**
   * @return window state
   */
  public final WindowState getWindowState() {
    return windowState;
  }

  /**
   * @param windowState window state
   */
  public final void setWindowState(final WindowState windowState) {
    this.windowState = windowState;
  }

  /**
   * @return markup
   */
  public final String getMarkup() {
    return markup;
  }

  /**
   * @param markup markup
   */
  public final void setMarkup(final String markup) {
    this.markup = markup;
  }

  /**
   * @return user attributes
   */
  public final Map<String, String> getUserAttributes() {
    return userAttributes;
  }

  /**
   * @param userAttributes user attributes
   */
  public final void setUserAttributes(final Map<String, String> userAttributes) {
    this.userAttributes = userAttributes;
  }

  /**
   * @return portlet state
   */
  public final byte[] getPortletState() {
    return portletState;
  }

  /**
   * @param portletState portlet state
   */
  public final void setPortletState(final byte[] portletState) {
    this.portletState = portletState;
  }

  /**
   * @return preferences persister
   */
  public final PortletPreferencesPersister getPortletPreferencesPersister() {
    return portletPreferencesPersister;
  }

  /**
   * @param portletPreferencesPersister preferences persister
   */
  public final void setPortletPreferencesPersister(final PortletPreferencesPersister portletPreferencesPersister) {
    this.portletPreferencesPersister = portletPreferencesPersister;
  }

  /**
   * @return locale list
   */
  public final List<Locale> getLocales() {
    return locales;
  }

  /**
   * @param locales locale list
   */
  public final void setLocales(final List<Locale> locales) {
    this.locales = locales;
  }

  /**
   * @return render params
   */
  public final Map<String, String[]> getRenderParameters() {
    if (renderParameters == null)
      renderParameters = new HashMap<String, String[]>();
    return renderParameters;
  }

  /**
   * @param renderParameters1 render params
   */
  public final void setRenderParameters(final Map<String, String[]> renderParameters1) {
    if (renderParameters1 == null)
      this.renderParameters = new HashMap<String, String[]>();
    else
      this.renderParameters = renderParameters1;
  }

  /**
   * @return portlet url factory
   */
  public final PortletURLFactory getPortletURLFactory() {
    return portletURLFactory;
  }

  /**
   * @param portletURLFactory portlet url factory
   */
  public final void setPortletURLFactory(final PortletURLFactory portletURLFactory) {
    this.portletURLFactory = portletURLFactory;
  }

  /**
   * @return base url
   */
  public final String getBaseURL() {
    return baseURL;
  }

  /**
   * @param baseURL base url
   */
  public final void setBaseURL(final String baseURL) {
    this.baseURL = baseURL;
  }

  /**
   * @return escapeXml
   */
  public final boolean getEscapeXml() {
    return escapeXml;
  }

  /**
   * @param escapeXml escapeXml
   */
  public final void setEscapeXml(final boolean escapeXml) {
    this.escapeXml = escapeXml;
  }

  /**
   * @param pubNames1 public param name list
   */
  public final void setPublicParamNames(final List<String> pubNames1) {
    this.pubNames = pubNames1;
  }
  
  /**
   * @param propertyParams  hash map
   */
  public final void setPropertyParams(final HashMap<String, String[]> propertyParams)
  {
    this.propertyParams = propertyParams;
  }

  /**
   * @return property params
   */
  public final HashMap getPropertyParams()
  {
    return propertyParams;
  }

  /**
   * @return public param name list
   */
  public final List<String> getPublicParamNames() {
    return pubNames;
  }

  /**
   * @return public param map
   */
  public final Map<String, String[]> getPublicParameterMap() {
    if (pubNames == null)
      return null;
    HashMap<String, String[]> publicMap = new HashMap<String, String[]>();
    Iterator<String> names = renderParameters.keySet().iterator();
    while (names.hasNext()) {
      String name = names.next();
      if (pubNames.contains(name))
        publicMap.put(name, renderParameters.get(name));
    }
    return publicMap;
  }

}
