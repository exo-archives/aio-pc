/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 9:17:39 PM
 */
package org.exoplatform.services.portletcontainer.pci;


import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;

import java.util.Collection;
import java.util.Map;

/**
 * This objects must be created by the Portal that access this
 * portlet container.
 * <p/>
 * The windowID is a unique id that references a portlet window
 * in the user scope.
 */
public class Input {

  private PortletURLFactory portletURLFactory;
  private PortletMode portletMode;
  private WindowState windowState;
  private String markup;
  private String baseURL;
  private Map userAttributes;
  private boolean stateSaveOnClient;
  private byte[] portletState;
  private WindowID windowID;
  private PortletPreferencesPersister portletPreferencesPersister;  
  private List locales;

  public boolean isStateSaveOnClient() {
    return stateSaveOnClient;
  }

  public void setStateSaveOnClient(boolean stateSaveOnClient) {
    this.stateSaveOnClient = stateSaveOnClient;
  }

  public PortletURLFactory getPortletURLFactory() {
    return portletURLFactory;
  }

  public void setPortletURLFactory(PortletURLFactory portletURLFactory) {
    this.portletURLFactory = portletURLFactory;
  }

  public WindowID getWindowID() {
    return windowID;
  }

  public void setWindowID(WindowID windowID) {
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

  public String getBaseURL() {
    return baseURL;
  }

  public void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
  }

  public Map getUserAttributes() {
    return userAttributes;
  }

  public void setUserAttributes(Map userAttributes) {
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
  
  
  public List getLocales() {
    return locales;
  }
  public void setLocales(List locales) {
    this.locales = locales;
  }
}
