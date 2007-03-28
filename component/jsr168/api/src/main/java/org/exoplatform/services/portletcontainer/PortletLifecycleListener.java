/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletcontainer;


import javax.portlet.PortletConfig;
import javax.servlet.ServletContext;
import org.exoplatform.container.component.ComponentPlugin ;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public interface PortletLifecycleListener extends ComponentPlugin {

  public void preDeploy(String portletApplicationName, PortletApp portletApplication,
                        ServletContext servletContext);
  public void postDeploy(String portletApplicationName, PortletApp portletApplication,
                         ServletContext servletContext);

  public void preInit(PortletConfig portletConfig);
  public void postInit(PortletConfig portletConfig);

  public void preDestroy();
  public void postDestroy();

  public void preUndeploy(String portletApplicationName, PortletApp portletApplication,
                          ServletContext servletContext);
  public void postUndeploy(String portletApplicationName, PortletApp portletApplication,
                           ServletContext servletContext);

}
