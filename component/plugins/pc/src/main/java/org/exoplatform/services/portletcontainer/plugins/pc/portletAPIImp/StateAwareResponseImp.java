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

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.StateAwareResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.xml.namespace.QName;
import javax.xml.bind.JAXBContext;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.model.Supports;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 25.05.2007.
 */
public class StateAwareResponseImp extends PortletResponseImp implements StateAwareResponse {

  /**
   * @param resCtx response context
   */
  public StateAwareResponseImp(final ResponseContext resCtx) {
    super(resCtx);
  }

  /**
   * Overridden method.
   *
   * @param windowState window state
   * @throws WindowStateException exception
   * @see javax.portlet.StateAwareResponse#setWindowState(javax.portlet.WindowState)
   */
  public final void setWindowState(final WindowState windowState) throws WindowStateException {
    if (isSendRedirectAlreadyOccured())
      throw new IllegalStateException("sendRedirect was already called");
    if (windowState == null)
      throw new WindowStateException("The portlet mode is null", windowState);
    if ((windowState == WindowState.NORMAL) || (windowState == WindowState.MINIMIZED)
        || (windowState == WindowState.MAXIMIZED)) {
      ((EventOutput) this.getOutput()).setNextState(windowState);
      setRedirectionPossible(false);
      return;
    }
    ExoContainer manager = getCont();
    Enumeration<WindowState> e = ((PortletContainerConf) manager
        .getComponentInstanceOfType(PortletContainerConf.class)).getSupportedWindowStates();
    while (e.hasMoreElements()) {
      WindowState state = e.nextElement();
      if (state.toString().equals(windowState.toString())) {
        ((EventOutput) this.getOutput()).setNextState(windowState);
        setRedirectionPossible(false);
        return;
      }
    }
    throw new WindowStateException("The window state " + windowState.toString()
        + " is not supported by the portlet container", windowState);
  }

  /**
   * Overridden method.
   *
   * @param portletMode portlet mode
   * @throws PortletModeException exception
   * @see javax.portlet.StateAwareResponse#setPortletMode(javax.portlet.PortletMode)
   */
  public final void setPortletMode(final PortletMode portletMode) throws PortletModeException {
    if (isSendRedirectAlreadyOccured())
      throw new IllegalStateException("sendRedirect was already called");
    if (portletMode == null)
      throw new PortletModeException("The portlet mode is null", portletMode);
    if (portletMode == PortletMode.VIEW) {
      ((EventOutput) this.getOutput()).setNextMode(portletMode);
      setRedirectionPossible(false);
      return;
    }
    List<Supports> l = getPortletDatas().getSupports();
    for (Supports supports : l)
      if (getInput().getMarkup().equals(supports.getMimeType())) {
        List<String> modeList = supports.getPortletMode();
        for (String modeString : modeList) {
          modeString = modeString.toLowerCase();
          if ((modeString != null) && modeString.equals(portletMode.toString())) {
            ((EventOutput) this.getOutput()).setNextMode(portletMode);
            setRedirectionPossible(false);
            return;
          }
        }
      }
    throw new PortletModeException("The mode " + portletMode.toString()
        + " is not supported by that portlet", portletMode);
  }

  /**
   * Overridden method.
   *
   * @param map map
   * @see javax.portlet.StateAwareResponse#setRenderParameters(java.util.Map)
   */
  public final void setRenderParameters(final Map<String, String[]> map) {
    if (map == null)
      throw new IllegalArgumentException("the map given is null");
    if (map.containsKey(null))
      throw new IllegalArgumentException("the map given contains a null key");
    Set<String> keys = map.keySet();
    for (String string : keys)
      if (!(string instanceof String))
        throw new IllegalArgumentException("the map contains a non String key");
    Collection<String[]> values = map.values();
    for (String[] name : values)
      if (!(name instanceof String[]))
        throw new IllegalArgumentException("the map contains a non String[] value");
    if (isSendRedirectAlreadyOccured())
      throw new IllegalStateException("sendRedirect was already called");
    setRedirectionPossible(false);
    ((EventOutput) this.getOutput()).setRenderParameters(map);
  }

  /**
   * Overridden method.
   *
   * @param s name
   * @param s1 value
   * @see javax.portlet.StateAwareResponse#setRenderParameter(java.lang.String, java.lang.String)
   */
  public final void setRenderParameter(final String s, final String s1) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (s1 == null)
      throw new IllegalArgumentException("the value given is null");
    if (isSendRedirectAlreadyOccured())
      throw new IllegalStateException("sendRedirect was already called");
    setRedirectionPossible(false);
    ((EventOutput) this.getOutput()).setRenderParameter(s, s1);
  }

  /**
   * Overridden method.
   *
   * @param s name
   * @param strings values
   * @see javax.portlet.StateAwareResponse#setRenderParameter(java.lang.String, java.lang.String[])
   */
  public final void setRenderParameter(final String s, final String[] strings) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (strings == null)
      throw new IllegalArgumentException("the value given is null");
    if (isSendRedirectAlreadyOccured())
      throw new IllegalStateException("sendRedirect was already called");
    setRedirectionPossible(false);
    ((EventOutput) this.getOutput()).setRenderParameters(s, strings);
  }

  /**
   * Overridden method.
   *
   * @return portlet mode
   * @see javax.portlet.StateAwareResponse#getPortletMode()
   */
  public final PortletMode getPortletMode() {
    return getInput().getPortletMode();
  }

  /**
   * Overridden method.
   *
   * @return render parameters
   * @see javax.portlet.StateAwareResponse#getRenderParameterMap()
   */
  public final Map getRenderParameterMap() {
    // TODO
    return null;
  }

  /**
   * Overridden method.
   *
   * @return window state
   * @see javax.portlet.StateAwareResponse#getWindowState()
   */
  public final WindowState getWindowState() {
    return getInput().getWindowState();
  }

  private final String[] jaxb8_5_2List = new String[] { "java.lang.String", "java.math.BigInteger",
      "java.math.BigDecimal", "java.util.Calendar", "java.util.Date", "javax.xml.namespace.QName",
      "java.net.URI", "javax.xml.datatype.XMLGregorianCalendar", "javax.xml.datatype.Duration",
      "java.awt.Image", "javax.activation.DataHandler", "javax.xml.transform.Source",
      "java.util.UUID" };

  /**
   * @param o event payload
   * @return is it default JAXB serializable object
   */
  protected boolean validateByJAXB8_5_2List(final Object o) {
    return java.util.Arrays.asList(jaxb8_5_2List).contains(o.getClass().getName());
  }

  /**
   * @param o event payload
   * @return is it JAXB serializable object
   */
  protected boolean validateWithJAXB(final Object o) {
    if (o == null)
      return true;
    if (validateByJAXB8_5_2List(o))
      return true;
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
      jaxbContext.createMarshaller().marshal(o, new NullOutputStream());
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param event event payload
   * @see javax.portlet.StateAwareResponse#setEvent(javax.xml.namespace.QName, java.io.Serializable)
   */
  public final void setEvent(final QName name, final java.io.Serializable event) {
    if (!validateWithJAXB(event))
      throw new java.lang.IllegalArgumentException("setEvent(): can't get binding of "
          + event.getClass().getName());
    ((EventOutput) getOutput()).setEvent(name, event);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param event event payload
   * @see javax.portlet.StateAwareResponse#setEvent(java.lang.String, java.io.Serializable)
   */
  public final void setEvent(final String name, final java.io.Serializable event) {
    ((EventOutput) getOutput()).setEvent(new QName(getPortletDatas().getApplication()
        .getDefaultNamespace(), name), event);
  }

  /**
   * @author Roman Pedchenko
   * Null output stream
   */
  private class NullOutputStream extends java.io.OutputStream {
    /**
     * Overridden method.
     *
     * @param b data byte
     * @see java.io.OutputStream#write(int)
     */
    public final void write(final int b) {
    }
  }

  /**
   * Overridden method.
   *
   * @param param param
   * @see javax.portlet.StateAwareResponse#removePublicRenderParameter(java.lang.String)
   */
  public final void removePublicRenderParameter(final String param) {
    setRedirectionPossible(false);
    this.getOutput().removePublicRenderParameter(param);
  }

}
