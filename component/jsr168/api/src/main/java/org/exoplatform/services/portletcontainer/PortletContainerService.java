package org.exoplatform.services.portletcontainer;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;


/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 09:40:23
 */
public interface PortletContainerService {  

  public void setPortletContainerName(String containerName);

  public void setMajorVersion(int majorVersion);

  public void setMinorVersion(int minorVersion);

  public void setProperties(Map properties);

  public void setSupportedPortletModesWithDescriptions(Collection portletModes);

  public void setSupportedWindowStatesWithDescriptions(Collection windowStates);

  public Collection getSupportedPortletModes();

  public Collection getSupportedWindowStates();

  public Collection getSupportedPortletModesWithDescriptions();

  public Collection getSupportedWindowStatesWithDescriptions();

  public Collection getPortletModes(String portletAppName, String portletName, String markup);

  public boolean isModeSuported(String portletAppName, String portletName,
                                String markup, PortletMode mode);

  public Collection getWindowStates(String portletApplicationName);

  public boolean isStateSupported(WindowState state, String portletApplication);

  public Map getAllPortletMetaData();

  public ResourceBundle getBundle(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String portletAppName,
                                  String portletName,
                                  Locale locale)
      throws PortletContainerException;

  public void setPortletPreference(Input input, Map preferences) throws PortletContainerException;

  public Map getPortletPreference(Input input);

  public ActionOutput processAction(HttpServletRequest request,
                                    HttpServletResponse response,
                                    ActionInput input) throws PortletContainerException;

  public RenderOutput render(HttpServletRequest request,
                             HttpServletResponse response,
                             RenderInput input)
      throws PortletContainerException;

  public void sendAttrs(HttpServletRequest request, HttpServletResponse response,
      Map attrs, String portletApplicationName) throws PortletContainerException;

}
