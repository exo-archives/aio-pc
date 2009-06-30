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

import org.exoplatform.services.log.Log;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletLifecycleListener;
import org.exoplatform.services.portletcontainer.management.PortletContainerManaged;
import org.exoplatform.services.portletcontainer.pci.model.Filter;
import org.exoplatform.services.portletcontainer.pci.model.FilterMapping;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterChainImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.filter.PortletFilterWrapper;
import org.exoplatform.management.annotations.ManagedBy;

/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com
 */
@ManagedBy(PortletContainerManaged.class)
public class PortletApplicationRegisterImpl implements PortletApplicationRegister {

  /**
   * Listeners.
   */
  private final Collection<PortletLifecycleListener> listeners;

  /**
   * Logger.
   */
  private final Log log;

  /**
   * Application holder.
   */
  private final PortletApplicationsHolder holder;

  /**
   * Application exo container.
   */
  protected ExoContainer appcont;

  /**
   * @param context exo container context
   * @param holder application holder
   */
  public PortletApplicationRegisterImpl(final ExoContainerContext context,
      final PortletApplicationsHolder holder) {
    this.listeners = new ArrayList<PortletLifecycleListener>();
    this.holder = holder;
    this.log = ExoLogger.getLogger(getClass());
    this.appcont = ExoContainerContext.getCurrentContainer();
  }

  /**
   * Overridden method.
   *
   * @param listener listener
   * @see org.exoplatform.services.portletcontainer.PortletApplicationRegister#addListenerPlugin(org.exoplatform.container.component.ComponentPlugin)
   */
  public final void addListenerPlugin(final ComponentPlugin listener) {
    if (listener instanceof PortletLifecycleListener)
      listeners.add((PortletLifecycleListener) listener);
    else
      throw new RuntimeException("Expect listener of type PortletLifecycleListener");
  }

  /**
   * @param name listener name
   * @return removed listener
   */
  public final ComponentPlugin removeListener(final String name) {
    return null;
  }

  /**
   * @return listener collection
   */
  public final Collection<PortletLifecycleListener> getListeners() {
    return listeners;
  }

  /**
   * Overridden method.
   *
   * @param servletContext servlet context
   * @param portletApp_ portlet app instance
   * @param roles roles
   * @param portletAppName portlet app name
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletApplicationRegister#registerPortletApplication(javax.servlet.ServletContext, org.exoplatform.services.portletcontainer.pci.model.PortletApp, java.util.Collection, java.lang.String)
   */
  public final void registerPortletApplication(final ServletContext servletContext,
      final PortletApp portletApp_,
      final Collection<String> roles,
      final String portletAppName) throws PortletContainerException {
    log.debug("send pre deploy event for portlet app : " + servletContext.getServletContextName());
    for (Iterator<PortletLifecycleListener> iterator = listeners.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = iterator.next();
      portletLifecycleListener.preDeploy(portletAppName, portletApp_, servletContext);
    }
    holder.registerPortletApplication(portletAppName, portletApp_, roles);
    createFilterChains(portletApp_);
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    try {
      appcont
          .registerComponentImplementation(
              portletAppName + PCConstants.PORTLET_APP_ENCODER,
              cl
                  .loadClass("org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationProxy"));
    } catch (ClassNotFoundException e) {
      log.error("Class not found", e);
      throw new PortletContainerException("Class not found", e);
    }
    PortletApplicationProxy proxy = (PortletApplicationProxy) appcont
        .getComponentInstance(portletAppName + PCConstants.PORTLET_APP_ENCODER);
    proxy.setApplicationName(portletAppName);
    proxy.load();
    log.debug("send post deploy event");
    for (Iterator<PortletLifecycleListener> iterator = listeners.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = iterator.next();
      portletLifecycleListener.postDeploy(portletAppName, portletApp_, servletContext);
    }
  }

  /**
   * Overridden method.
   *
   * @param servletContext servlet context
   * @param portletAppName portlet app name
   * @throws PortletContainerException exception
   * @see org.exoplatform.services.portletcontainer.PortletApplicationRegister#removePortletApplication(javax.servlet.ServletContext, java.lang.String)
   */
  public final void removePortletApplication(final ServletContext servletContext,
      final String portletAppName) throws PortletContainerException {
    PortletApp portletApp = holder.getPortletApplication(portletAppName);
    if (portletApp == null)
      return;
    log.debug("send pre undeploy event");
    for (Iterator<PortletLifecycleListener> iterator = listeners.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = iterator.next();
      portletLifecycleListener.preUndeploy(portletAppName, portletApp, servletContext);
    }
    appcont.unregisterComponent(portletAppName + PCConstants.PORTLET_APP_ENCODER);
    holder.removePortletApplication(portletAppName);
    removeFilters(portletAppName, portletApp);
    log.debug("send post undeploy event");
    for (Iterator<PortletLifecycleListener> iterator = listeners.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = iterator.next();
      portletLifecycleListener.postUndeploy(portletAppName, portletApp, servletContext);
    }
  }

  /**
   * @param portletApp portlet app instance
   * @throws PortletContainerException exception
   */
  private void createFilterChains(final PortletApp portletApp) throws PortletContainerException {
    log.debug("create filter chains entered");
    List<Filter> filterList = portletApp.getFilter();
    Map<String, PortletFilterWrapper> filters = new HashMap<String, PortletFilterWrapper>();

    for (Iterator<Filter> iterator = filterList.iterator(); iterator.hasNext();) {
      Filter filter = (Filter) iterator.next();
      filters.put(filter.getFilterName(), new PortletFilterWrapper(filter.getFilterName(), filter
          .getFilterClass(), filter.getInitParam(), filter.getLifecycle()));
    }
    List<Portlet> portlets = portletApp.getPortlet();
    for (Iterator<Portlet> iterator = portlets.iterator(); iterator.hasNext();) {
      Collection<PortletFilterWrapper> chain = new ArrayList<PortletFilterWrapper>();
      Portlet portlet = (Portlet) iterator.next();
      List<FilterMapping> mappings = portletApp.getFilterMapping();
      for (Iterator<FilterMapping> iterator1 = mappings.iterator(); iterator1.hasNext();) {
        FilterMapping mapping = (FilterMapping) iterator1.next();
        List<String> portletName = mapping.getPortletName();
        Iterator<String> iter = portletName.iterator();
        while (iter.hasNext()) {
          String onePortletName = (String) iter.next();
          // if portlet name in mapping is *
          if (onePortletName.equals("*") ||
          // if portlet name in mapping fully equals to current portlet name
              onePortletName.equals(portlet.getPortletName()) ||
              // if portlet name in mapping ends with * and partially equals to
              // current portlet name
              (onePortletName.endsWith("*") && (portlet.getPortletName().startsWith(onePortletName
                  .substring(0, onePortletName.length() - 1))))) {
            // then we add it to the chain
            PortletFilterWrapper filter = (PortletFilterWrapper) filters.get(mapping
                .getFilterName());
            chain.add(filter);
          }
        }
      }
      portlet.setFilterChain(new PortletFilterChainImpl(chain));
    }
    portletApp.setUrlListeners(createUrlListeners(portletApp.getUrlGenerationListener()));
  }

  /**
   * @param urlGenerationListener url generation listener class name list
   * @return url generation listener instance list
   * @throws PortletContainerException exception
   */
  private List<PortletURLGenerationListener> createUrlListeners(final List<String> urlGenerationListener) throws PortletContainerException {
    if (urlGenerationListener == null)
      return null;
    List<PortletURLGenerationListener> list = new ArrayList<PortletURLGenerationListener>();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    for (String string : urlGenerationListener)
      try {
        PortletURLGenerationListener obj = (PortletURLGenerationListener) cl.loadClass(string)
            .newInstance();
        list.add(obj);
      } catch (Exception e) {
        log.error("Class not found", e);
        throw new PortletContainerException("Class not found", e);
      }
    return list;
  }

  /**
   * @param portletAppName portlet application name
   * @param portletApp portlet application object
   */
  private void removeFilters(final String portletAppName, final PortletApp portletApp) {
    log.debug("remove filters entered");
    List<Portlet> portlets = portletApp.getPortlet();
    for (Portlet portlet : portlets) {
      PortletFilterChainImpl chain = (PortletFilterChainImpl) portlet.getFilterChain();
      for (Iterator<PortletFilterWrapper> iter = chain.getFiltersIterator(); iter.hasNext();) {
        PortletFilterWrapper filter = (PortletFilterWrapper) iter.next();
        filter.destroy();
      }
    }
  }

}

