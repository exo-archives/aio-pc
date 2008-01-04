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
package org.exoplatform.services.portletcontainer.plugins.pc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletURLGenerationListener;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletLifecycleListener;
import org.exoplatform.services.portletcontainer.pci.model.Filter;
import org.exoplatform.services.portletcontainer.pci.model.FilterMapping;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterChainImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterWrapper;

/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com
 */
public class PortletApplicationRegisterImpl implements PortletApplicationRegister {

  private Collection                listeners_;

  private Log                       log;

  private PortletApplicationsHolder holder_;

  protected ExoContainer            cont;

  protected ExoContainer            appcont;

  public PortletApplicationRegisterImpl(ExoContainerContext context, PortletApplicationsHolder holder) {
    listeners_ = new ArrayList();
    holder_ = holder;
    this.log = ExoLogger.getLogger(getClass());
    cont = context.getContainer();
    appcont = ExoContainerContext.getTopContainer();
  }

  public void addListenerPlugin(ComponentPlugin listener) {
    if (listener instanceof PortletLifecycleListener) {
      listeners_.add(listener);
    } else {
      throw new RuntimeException("Expect listener of type PortletLifecycleListener");
    }
  }

  public ComponentPlugin removeListener(String name) {
    return null;
  }

  public Collection getListeners() {
    return listeners_;
  }

  public void registerPortletApplication(ServletContext servletContext, PortletApp portletApp_, Collection<String> roles, String portletAppName) throws PortletContainerException {
    log.debug("send pre deploy event for portlet app : " + servletContext.getServletContextName());
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator.next();
      portletLifecycleListener.preDeploy(portletAppName, portletApp_, servletContext);
    }
    holder_.registerPortletApplication(portletAppName, portletApp_, roles);
    createFilterChains(portletApp_);
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    try {
      appcont.registerComponentImplementation(portletAppName,
                                              cl.loadClass("org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationProxy"));
    } catch (ClassNotFoundException e) {
      log.error("Class not found", e);
      throw new PortletContainerException("Class not found", e);
    }
    PortletApplicationProxy proxy = (PortletApplicationProxy) appcont.getComponentInstance(portletAppName);
    proxy.setApplicationName(portletAppName);
    proxy.load();
    log.debug("send post deploy event");
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator.next();
      portletLifecycleListener.postDeploy(portletAppName, portletApp_, servletContext);
    }
  }

  public void removePortletApplication(ServletContext servletContext, String portletAppName) throws PortletContainerException {
    PortletApp portletApp = holder_.getPortletApplication(portletAppName);
    if (portletApp == null)
      return;
    log.debug("send pre undeploy event");
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator.next();
      portletLifecycleListener.preUndeploy(portletAppName, portletApp, servletContext);
    }
    appcont.unregisterComponent(portletAppName);
    holder_.removePortletApplication(portletAppName);
    removeFilters(portletAppName, portletApp);
    log.debug("send post undeploy event");
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator.next();
      portletLifecycleListener.postUndeploy(portletAppName, portletApp, servletContext);
    }
  }

  private void createFilterChains(PortletApp portletApp) throws PortletContainerException {
    log.debug("create filter chains entered");
    List filterList = portletApp.getFilter();
    Map filters = new HashMap();

    for (Iterator iterator = filterList.iterator(); iterator.hasNext();) {
      Filter filter = (Filter) iterator.next();
      filters.put(filter.getFilterName(), new PortletFilterWrapper(filter.getFilterName(),
                                                                   filter.getFilterClass(),
                                                                   filter.getInitParam(),
                                                                   filter.getLifecycle()));
    }
    List portlets = portletApp.getPortlet();
    for (Iterator iterator = portlets.iterator(); iterator.hasNext();) {
      Collection chain = new ArrayList();
      Portlet portlet = (Portlet) iterator.next();
      List mappings = portletApp.getFilterMapping();
      for (Iterator iterator1 = mappings.iterator(); iterator1.hasNext();) {
        FilterMapping mapping = (FilterMapping) iterator1.next();
        List portletName = mapping.getPortletName();
        Iterator iter = portletName.iterator();
        while (iter.hasNext()) {
          String onePortletName = (String) iter.next();
          // if portlet name in mapping is *
          if (onePortletName.equals("*") ||
          // if portlet name in mapping fully equals to current portlet name
              onePortletName.equals(portlet.getPortletName()) ||
              // if portlet name in mapping ends with * and partially equals to
              // current portlet name
              (onePortletName.endsWith("*") && (portlet.getPortletName().startsWith(onePortletName.substring(0, onePortletName.length() - 1))))) {
            // then we add it to the chain
            PortletFilterWrapper filter = (PortletFilterWrapper) filters.get(mapping.getFilterName());
            chain.add(filter);
          }
        }
      }
      portlet.setFilterChain(new PortletFilterChainImpl(chain, portlet));
    }
    portletApp.setUrlListeners(createUrlListeners(portletApp.getUrlGenerationListener()));
  }

  private List<PortletURLGenerationListener> createUrlListeners(List<String> urlGenerationListener) throws PortletContainerException {
    if (urlGenerationListener == null)
      return null;
    List<PortletURLGenerationListener> list = new ArrayList<PortletURLGenerationListener>();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    for (Iterator<String> i = urlGenerationListener.iterator(); i.hasNext();) {
      try {
        PortletURLGenerationListener obj = (PortletURLGenerationListener) cl.loadClass(i.next()).newInstance();
        list.add(obj);
      } catch (Exception e) {
        log.error("Class not found", e);
        throw new PortletContainerException("Class not found", e);
      }
    }
    return list;
  }

  private void removeFilters(String portletAppName, PortletApp portletApp) {
    log.debug("remove filters entered");
    List<Portlet> portlets = portletApp.getPortlet();
    for (Iterator<Portlet> iterator = portlets.iterator(); iterator.hasNext();) {
      Portlet portlet = (Portlet) iterator.next();
      PortletFilterChainImpl chain = (PortletFilterChainImpl) portlet.getFilterChain();
      for (Iterator iter = chain.getFiltersIterator(); iter.hasNext();) {
        PortletFilterWrapper filter = (PortletFilterWrapper) iter.next();
        filter.destroy();
      }
    }
  }

}
