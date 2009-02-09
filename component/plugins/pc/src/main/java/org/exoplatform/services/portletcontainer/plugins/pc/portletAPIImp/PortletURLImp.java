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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletURL;
import javax.portlet.PortletURLGenerationListener;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.Supports;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class PortletURLImp extends BaseURLImp implements PortletURL {

  /**
   * Supports.
   */
  protected List<Supports> supports;

  /**
   * Required window state.
   */
  protected WindowState requiredWindowState;

  /**
   * Required portlet mode.
   */
  protected PortletMode requiredPortletMode;

  /**
   * Markup.
   */
  protected String markup;

  /**
   * @param type url type
   * @param baseURL base url
   * @param markup markup
   * @param supports supports
   * @param isCurrentlySecured is currently secured
   * @param defaultEscapeXml default escape xml
   * @param portletDatas portlet datas
   */
  public PortletURLImp(final String type,
      final String baseURL,
      final String markup,
      final List<Supports> supports,
      final boolean isCurrentlySecured,
      final boolean defaultEscapeXml,
      final Portlet portletDatas) {
    super(type, baseURL, isCurrentlySecured, defaultEscapeXml, portletDatas);
    this.markup = markup;
    this.supports = supports;
  }

  /**
   * Overridden method.
   *
   * @param windowState window state
   * @throws WindowStateException exception
   * @see javax.portlet.PortletURL#setWindowState(javax.portlet.WindowState)
   */
  public final void setWindowState(final WindowState windowState) throws WindowStateException {

    if (windowState == null)
      throw new WindowStateException("The portlet state is null", windowState);
    if ((windowState == WindowState.NORMAL) || (windowState == WindowState.MINIMIZED)
        || (windowState == WindowState.MAXIMIZED)) {
      requiredWindowState = windowState;
      return;
    }

    boolean supported = false;
    for (Object element : supports) {
      Supports sp = (Supports) element;
      if (markup.equals(sp.getMimeType())) {
        List stateList = sp.getWindowState();
        for (Iterator iterator1 = stateList.iterator(); iterator1.hasNext();) {
          String stateString = (String) iterator1.next();
          if ((stateString != null) && stateString.equalsIgnoreCase(windowState.toString())) {
            supported = true;
            break;
          }
        }
        break;
      }
    }
    if (!supported)
      throw new WindowStateException("The window state " + windowState.toString()
          + " is not supported by that portlet", windowState);

    requiredWindowState = windowState;
  }

  /**
   * Overridden method.
   *
   * @param portletMode portlet mode
   * @throws PortletModeException exception
   * @see javax.portlet.PortletURL#setPortletMode(javax.portlet.PortletMode)
   */
  public final void setPortletMode(final PortletMode portletMode) throws PortletModeException {

    if (portletMode == null)
      throw new PortletModeException("The portlet mode is null", portletMode);
    if (portletMode == PortletMode.VIEW) {
      requiredPortletMode = portletMode;
      return;
    }

    boolean supported = false;
    for (Object element : supports) {
      Supports sp = (Supports) element;
      if (markup.equals(sp.getMimeType())) {
        List modeList = sp.getPortletMode();
        for (Iterator iterator1 = modeList.iterator(); iterator1.hasNext();) {
          String modeString = (String) iterator1.next();
          if ((modeString != null) && modeString.equalsIgnoreCase(portletMode.toString())) {
            supported = true;
            break;
          }
        }
        break;
      }
    }
    if (!supported)
      throw new PortletModeException("The mode " + portletMode.toString()
          + " is not supported by that portlet", portletMode);

    requiredPortletMode = portletMode;
  }

  /**
   * Overridden method.
   *
   * @return portlet mode
   * @see javax.portlet.PortletURL#getPortletMode()
   */
  public final PortletMode getPortletMode() {
    return requiredPortletMode;
  }

  /**
   * Overridden method.
   *
   * @return window state
   * @see javax.portlet.PortletURL#getWindowState()
   */
  public final WindowState getWindowState() {
    return requiredWindowState;
  }

  /**
   * Invoke filter render url.
   */
  protected final void invokeFilterRenderURL() {
    if (getPortletDatas() == null)
      return;
    List<PortletURLGenerationListener> list = getPortletDatas().getApplication().getUrlListeners();
    if (list == null)
      return;
    for (PortletURLGenerationListener listener : list)
      try {
        listener.filterRenderURL(this);
      } catch (Exception e) {
      }
  }

  /**
   * Invoke filter action url.
   */
  protected final void invokeFilterActionURL() {
    if (getPortletDatas() == null)
      return;
    List<PortletURLGenerationListener> list = getPortletDatas().getApplication().getUrlListeners();
    if (list == null)
      return;
    for (PortletURLGenerationListener listener : list)
      try {
        listener.filterActionURL(this);
      } catch (Exception e) {
      }
  }

  /**
   * Overridden method.
   *
   * @param escapeXML escape xml
   * @return string representation
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.BaseURLImp#toString(boolean)
   */
  public String toString(final boolean escapeXML) {
    if (getType().equals(PCConstants.ACTION_STRING))
      invokeFilterActionURL();
    else
      invokeFilterRenderURL();

    if (!isSetSecureCalled() && isCurrentlySecured())
      setSecure(true);

    StringBuffer sB = new StringBuffer();
    sB.append(baseURL);

    sB.append(PCConstants.AMPERSAND);
    sB.append(Constants.TYPE_PARAMETER);
    sB.append("=");
    sB.append(getType());

    sB.append(PCConstants.AMPERSAND);
    sB.append(Constants.SECURE_PARAMETER);
    sB.append("=");
    sB.append(isSecure());

    if (requiredPortletMode != null) {
      sB.append(PCConstants.AMPERSAND);
      sB.append(Constants.PORTLET_MODE_PARAMETER);
      sB.append("=");
      sB.append(requiredPortletMode);
    }
    if (requiredWindowState != null) {
      sB.append(PCConstants.AMPERSAND);
      sB.append(Constants.WINDOW_STATE_PARAMETER);
      sB.append("=");
      sB.append(requiredWindowState);
    }

    Set names = parameters.keySet();
    for (Iterator iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next();
      Object obj = parameters.get(name);
      if (obj instanceof String) {
        String value = (String) obj;
        sB.append(PCConstants.AMPERSAND);
        sB.append(encode(name));
        sB.append("=");
        sB.append(encode(value));
      } else {
        String[] values = (String[]) obj;
        for (String element : values) {
          sB.append(PCConstants.AMPERSAND);
          sB.append(encode(name));
          sB.append("=");
          sB.append(encode(element));
        }
      }
    }
    String propertyString = getPropertyString();
    if ((propertyString != "") && (propertyString != null))
      // sB.append(PCConstants.AMPERSAND);
      sB.append(propertyString);
    if (escapeXML)
      return encodeChars(sB.toString());
    return sB.toString();
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @see javax.portlet.PortletURL#removePublicRenderParameter(java.lang.String)
   */
  public final void removePublicRenderParameter(final String name) {
    setParameter(PCConstants.REMOVE_PUBLIC_STRING, name);
  }

}
