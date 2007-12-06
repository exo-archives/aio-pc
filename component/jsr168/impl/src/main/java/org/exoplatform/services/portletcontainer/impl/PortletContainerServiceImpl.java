package org.exoplatform.services.portletcontainer.impl;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.*;
import org.picocontainer.Startable;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class PortletContainerServiceImpl implements PortletContainerService,  Startable {

  private PortletContainerDispatcher dispatcher_;

  public PortletContainerServiceImpl( PortletContainerDispatcher dispatcher) {
    dispatcher_ = dispatcher ;
  }

  public void start() {
  }

  public void stop() {
  }

  public void setPortletContainerName(String containerName) {
    dispatcher_.setPortletContainerName(containerName);
  }

  public void setMajorVersion(int majorVersion) {
    dispatcher_.setMajorVersion(majorVersion);
  }

  public void setMinorVersion(int minorVersion) {
    dispatcher_.setMinorVersion(minorVersion);
  }

  public void setProperties(Map properties) {
    dispatcher_.setProperties(properties);
  }

  public void setSupportedPortletModesWithDescriptions(Collection portletModes) {
    dispatcher_.setSupportedPortletModesWithDescriptions(portletModes);
  }

  public void setSupportedWindowStatesWithDescriptions(Collection windowStates) {
    dispatcher_.setSupportedWindowStatesWithDescriptions(windowStates);
  }

  public Collection getSupportedPortletModes() {
    return dispatcher_.getSupportedPortletModes();
  }

  public Collection getSupportedWindowStates() {
    return dispatcher_.getSupportedWindowStates();
  }

  public Collection getSupportedPortletModesWithDescriptions() {
    return dispatcher_.getSupportedPortletModesWithDescriptions();
  }

  public Collection getSupportedWindowStatesWithDescriptions() {
    return dispatcher_.getSupportedWindowStatesWithDescriptions();
  }

  public Collection getPortletModes(String portletAppName, String portletName,
      String markup) {
    return dispatcher_.getPortletModes(portletAppName, portletName, markup);
  }

  public boolean isModeSuported(String portletAppName, String portletName,
      String markup, PortletMode mode) {
    return dispatcher_
        .isModeSuported(portletAppName, portletName, markup, mode);
  }

  public Collection getWindowStates(String portletApplicationName) {
    return dispatcher_.getWindowStates(portletApplicationName);
  }

  public boolean isStateSupported(WindowState state, String portletApplication) {
    return dispatcher_.isStateSupported(state, portletApplication);
  }

  public Map getAllPortletMetaData() {
    return dispatcher_.getAllPortletMetaData();
  }

  public ResourceBundle getBundle(HttpServletRequest request,
      HttpServletResponse response, String portletAppName, String portletName,
      Locale locale) throws PortletContainerException {
    return dispatcher_.getBundle(request, response, portletAppName,
        portletName, locale);
  }

  public void setPortletPreference(Input input, Map preferences)
      throws PortletContainerException {
    dispatcher_.setPortletPreference(input, preferences);
  }

  public Map getPortletPreference(Input input) {
    return dispatcher_.getPortletPreference(input);
  }

  public ActionOutput processAction(HttpServletRequest request,
      HttpServletResponse response, ActionInput input)
      throws PortletContainerException {
    return dispatcher_.processAction(request, response, input);
  }

  public RenderOutput render(HttpServletRequest request,
      HttpServletResponse response, RenderInput input)
      throws PortletContainerException {
    return dispatcher_.render(request, response, input);
  }

  public void sendAttrs(HttpServletRequest request, HttpServletResponse response,
      Map attrs, String portletApplicationName) throws PortletContainerException{
      dispatcher_.sendAttrs(request,response,attrs,portletApplicationName);
      }
}