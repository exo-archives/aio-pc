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
import java.util.Iterator;
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
 * Created by The eXo Platform SAS Author : Alexey Zavizionov
 * alexey.zavizionov@exoplatform.com.ua 25.05.2007
 */
public class StateAwareResponseImp extends PortletResponseImp implements StateAwareResponse {

  public StateAwareResponseImp(ResponseContext resCtx) {
    super(resCtx);
  }

  public void setWindowState(WindowState windowState) throws WindowStateException {
    if (sendRedirectAlreadyOccured_)
      throw new IllegalStateException("sendRedirect was already called");
    if (windowState == null) {
      throw new WindowStateException("The portlet mode is null", windowState);
    }
    if (windowState == WindowState.NORMAL || windowState == WindowState.MINIMIZED || windowState == WindowState.MAXIMIZED) {
      ((EventOutput) this.getOutput()).setNextState(windowState);
      redirectionPossible_ = false;
      return;
    }
    ExoContainer manager = cont_;
    Enumeration<WindowState> e = ((PortletContainerConf) manager.getComponentInstanceOfType(PortletContainerConf.class)).getSupportedWindowStates();
    while (e.hasMoreElements()) {
      WindowState state = e.nextElement();
      if (state.toString().equals(windowState.toString())) {
        ((EventOutput) this.getOutput()).setNextState(windowState);
        redirectionPossible_ = false;
        return;
      }
    }
    throw new WindowStateException("The window state " + windowState.toString() + " is not supported by the portlet container", windowState);
  }

  public void setPortletMode(PortletMode portletMode) throws PortletModeException {
    if (sendRedirectAlreadyOccured_)
      throw new IllegalStateException("sendRedirect was already called");
    if (portletMode == null)
      throw new PortletModeException("The portlet mode is null", portletMode);
    if (portletMode == PortletMode.VIEW) {
      ((EventOutput) this.getOutput()).setNextMode(portletMode);
      redirectionPossible_ = false;
      return;
    }
    List<Supports> l = portletDatas_.getSupports();
    for (Iterator<Supports> iterator = l.iterator(); iterator.hasNext();) {
      Supports supports = iterator.next();
      if (input_.getMarkup().equals(supports.getMimeType())) {
        List<String> modeList = supports.getPortletMode();
        for (Iterator<String> iterator1 = modeList.iterator(); iterator1.hasNext();) {
          String modeString = iterator1.next();
          modeString = modeString.toLowerCase();
          if (modeString != null && modeString.equals(portletMode.toString())) {
            ((EventOutput) this.getOutput()).setNextMode(portletMode);
            redirectionPossible_ = false;
            return;
          }
        }
      }
    }
    throw new PortletModeException("The mode " + portletMode.toString() + " is not supported by that portlet", portletMode);
  }

  public void setRenderParameters(Map<String, String[]> map) {
    if (map == null)
      throw new IllegalArgumentException("the map given is null");
    if (map.containsKey(null))
      throw new IllegalArgumentException("the map given contains a null key");
    Set<String> keys = map.keySet();
    for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
      if (!(iter.next() instanceof String)) {
        throw new IllegalArgumentException("the map contains a non String key");
      }
    }
    Collection<String[]> values = map.values();
    for (Iterator<String[]> iter = values.iterator(); iter.hasNext();) {
      if (!(iter.next() instanceof String[])) {
        throw new IllegalArgumentException("the map contains a non String[] value");
      }
    }
    if (sendRedirectAlreadyOccured_)
      throw new IllegalStateException("sendRedirect was already called");
    redirectionPossible_ = false;
    ((EventOutput) this.getOutput()).setRenderParameters(map);
  }

  public void setRenderParameter(String s,
                                 String s1) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (s1 == null)
      throw new IllegalArgumentException("the value given is null");
    if (sendRedirectAlreadyOccured_)
      throw new IllegalStateException("sendRedirect was already called");
    redirectionPossible_ = false;
    ((EventOutput) this.getOutput()).setRenderParameter(s, s1);
  }

  public void setRenderParameter(String s,
                                 String[] strings) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (strings == null)
      throw new IllegalArgumentException("the value given is null");
    if (sendRedirectAlreadyOccured_)
      throw new IllegalStateException("sendRedirect was already called");
    redirectionPossible_ = false;
    ((EventOutput) this.getOutput()).setRenderParameters(s, strings);
  }

  public boolean isSendRedirectAlreadyOccured() {
    return sendRedirectAlreadyOccured_;
  }

  public PortletMode getPortletMode() {
    return input_.getPortletMode();
  }

  public Map getRenderParameterMap() {
    // TODO
    return null;
  }

  public WindowState getWindowState() {
    return input_.getWindowState();
  }

  private String[] jaxb8_5_2List = new String[] { "java.lang.String", "java.math.BigInteger", "java.math.BigDecimal", "java.util.Calendar",
      "java.util.Date", "javax.xml.namespace.QName", "java.net.URI", "javax.xml.datatype.XMLGregorianCalendar", "javax.xml.datatype.Duration",
      "java.awt.Image", "javax.activation.DataHandler", "javax.xml.transform.Source", "java.util.UUID" };

  protected boolean validateByJAXB8_5_2List(Object o) {
    return java.util.Arrays.asList(jaxb8_5_2List).contains(o.getClass().getName());
  }

  protected boolean validateWithJAXB(Object o) {
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

  public void setEvent(QName name,
                       java.io.Serializable event) {
    if (!validateWithJAXB(event))
      throw new java.lang.IllegalArgumentException("setEvent(): can't get binding of " + event.getClass().getName());
    ((EventOutput) getOutput()).setEvent(name, event);
  }

  public void setEvent(String name,
                       java.io.Serializable event) {
    ((EventOutput) getOutput()).setEvent(new QName(portletDatas_.getApplication().getDefaultNamespace(), name), event);
  }

  private class NullOutputStream extends java.io.OutputStream {
    public void write(int b) {
    }
  }

  public void removePublicRenderParameter(String param) {
    this.getOutput().removePublicRenderParameter(param);
  }

}
