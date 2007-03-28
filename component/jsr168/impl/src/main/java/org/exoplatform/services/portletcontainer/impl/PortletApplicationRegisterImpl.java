/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.portletcontainer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.*;
import org.exoplatform.services.portletcontainer.pci.model.*;


/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com
 */
public class PortletApplicationRegisterImpl implements PortletApplicationRegister {
  
  private Collection listeners_;
  private LogService logService_;
  private Log log;
  private PortletApplicationsHolder holder_;
  protected ExoContainer cont;
  protected ExoContainer appcont;
  
  public PortletApplicationRegisterImpl(ExoContainerContext context,
                                        PortletApplicationsHolder holder,
                                        LogService logService) {
    logService_ = logService ;
    listeners_ = new ArrayList();
    holder_ = holder;    
    this.log = logService_.getLog(getClass());
    cont = context.getContainer();
    appcont = ExoContainerContext.getTopContainer();
  }   
  
  public void addListenerPlugin(ComponentPlugin listener) {
    if(listener instanceof PortletLifecycleListener) {
      listeners_.add(listener) ;
    } else {
      throw new RuntimeException("Expect listener of type PortletLifecycleListener") ;
    }
  }

  public ComponentPlugin removeListener(String name) {
    return null;
  }

  public Collection getListeners() {  return listeners_; }

  public void registerPortletApplication(ServletContext servletContext,
      PortletApp portletApp_, Collection roles)
      throws PortletContainerException {
    String portletAppName = servletContext.getServletContextName();
    log.debug("send pre deploy event for portlet app : " + servletContext.getServletContextName());
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = 
        (PortletLifecycleListener) iterator.next();
      portletLifecycleListener.preDeploy(portletAppName, portletApp_, servletContext);
    }
    holder_.registerPortletApplication(portletAppName, portletApp_, roles);
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    try {
      appcont.registerComponentImplementation( portletAppName, cl.loadClass("org.exoplatform.services.portletcontainer.impl.PortletApplicationProxy"));
    } catch (ClassNotFoundException e) {
      log.error("Class not found", e);
      throw new PortletContainerException("Class not found", e);
    }
    PortletApplicationProxy proxy = 
      (PortletApplicationProxy) appcont.getComponentInstance(portletAppName);
    proxy.setApplicationName(portletAppName);
    proxy.load();
    log.debug("send post deploy event");
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator.next();
      portletLifecycleListener.postDeploy(portletAppName, portletApp_, servletContext);
    }
  }

  public void removePortletApplication(ServletContext servletContext)
      throws PortletContainerException {
    PortletApp portletApp = holder_.getPortletApplication(servletContext .getServletContextName());
    if (portletApp == null) return;
    String portletAppName = servletContext.getServletContextName();
    log.debug("send pre undeploy event");
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = 
        (PortletLifecycleListener) iterator.next();
      portletLifecycleListener.preUndeploy(portletAppName, portletApp, servletContext);
    }
    appcont.unregisterComponent(portletAppName);
    removeFilters(portletAppName, portletApp);
    holder_.removePortletApplication(portletAppName);
    log.debug("send post undeploy event");
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator.next();
      portletLifecycleListener.postUndeploy(portletAppName, portletApp, servletContext);
    }
  }

  private void removeFilters(String portletAppName, PortletApp portletApp) {
    log.debug("remove filters entered");
    List portlets = portletApp.getPortlet();
    for (Iterator iterator = portlets.iterator(); iterator.hasNext();) {
      Portlet portlet = (Portlet) iterator.next();
      List filters = portlet.getFilter();
      for (Iterator iterator1 = filters.iterator(); iterator1.hasNext();) {
        Filter portletFilterData = (Filter) iterator1.next();
        String key = portletAppName + Constants.FILTER_ENCODER
                     + portletFilterData.getFilterName();
        appcont.unregisterComponent(key);
      }
    }
  }
}
