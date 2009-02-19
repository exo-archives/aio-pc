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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PreferencesValidator;
import javax.portlet.UnavailableException;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.plugins.pc.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletConfigImp;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 11 nov. 2003
 * Time: 22:56:04
 */
public class PortletApplicationProxy implements Startable {
  /**
   * Waiting time before destroying (ms).
   */
  private static final int WAITING_TIME_BEFORE_DESTROY = 50;

  /**
   * Portlet application holder.
   */
  PortletApplicationsHolder holder;

  /**
   * Portlet application name.
   */
  private String portletAppName;

  /**
   * Configs.
   */
  private final Map<String, PortletConfig> configs;

  /**
   * Monitor.
   */
  private final PortletContainerMonitorImpl monitor;

  /**
   * Logger.
   */
  private final Log log;

  /**
   * Pico container.
   */
  private final MutablePicoContainer pico;

  /**
   * @param context exo container context
   * @param holder app holder
   * @param monitor monitor
   */
  public PortletApplicationProxy(final ExoContainerContext context,
      final PortletApplicationsHolder holder,
      final PortletContainerMonitorImpl monitor) {
    this.holder = holder;
    this.monitor = monitor;
    this.configs = new HashMap<String, PortletConfig>();
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    this.pico = new DefaultPicoContainer(context.getContainer());
  }

  /**
   * @param portletContext portlet context
   * @param portletName portlet name
   * @return portlet object
   * @throws PortletException exception
   */
  public final javax.portlet.Portlet getPortlet(final PortletContext portletContext,
      final String portletName) throws PortletException {
    log.debug("getPortlet() in PortletApplicationProxy entered");
    if (!monitor.isInitialized(portletAppName, portletName)) {
      synchronized (monitor) {
        if (!monitor.isInitialized(portletAppName, portletName)) {
          log.debug("init monitor");
          init(monitor, portletName, portletContext);
        }
      }
    }
    return (javax.portlet.Portlet) pico.getComponentInstance(portletAppName
        + Constants.PORTLET_ENCODER + portletName);
  }

  /**
   * @param monitor1 monitor
   * @param portletName portlet name
   * @param portletContext portlet context
   * @throws PortletException exception
   */
  private void init(final PortletContainerMonitorImpl monitor1,
      final String portletName,
      final PortletContext portletContext) throws PortletException {
    long accessTime = System.currentTimeMillis();
    if (!monitor1.isInitialisationAllowed(portletAppName, portletName, accessTime))
      throw new UnavailableException("Portlet initialization not possible");

    Portlet portletDatas = holder.getPortletMetaData(portletAppName, portletName);
    PortletApp portletApp = holder.getPortletApplication(portletAppName);
    PortletConfig config = new PortletConfigImp(portletDatas,
        portletContext,
        portletApp.getSecurityConstraint(),
        portletApp.getUserAttribute(),
        portletApp.getCustomPortletMode(),
        portletApp.getCustomWindowState(),
        portletApp.getDefaultNamespace());
    try {
      if (pico.getComponentInstance(portletAppName + Constants.PORTLET_ENCODER
          + portletDatas.getPortletName()) == null) {
        log.debug("First registration of portlet : " + portletAppName + "/" + portletName);
        registerPortlet(portletDatas.getPortletName());
      }
      ((javax.portlet.Portlet) pico.getComponentInstance(portletAppName
          + Constants.PORTLET_ENCODER + portletDatas.getPortletName())).init(config);
      configs.put(portletDatas.getPortletName(), config);
      Integer expirationStr = portletDatas.getCaching();
      int expiration = 0;
      if (expirationStr != null)
        expiration = expirationStr.intValue();
      monitor1.init(portletAppName, portletName, expiration);
      monitor1.setInitializationTime(portletAppName, portletName, accessTime);
    } catch (Throwable t) {
      log.error("exception while initializing portlet : " + portletName, t);
      monitor1.setLastInitFailureAccessTime(portletAppName, portletName, accessTime);
      releasePortlet(portletName);
      if (t instanceof UnavailableException) {
        UnavailableException e = (UnavailableException) t;
        if (!e.isPermanent())
          monitor1.setUnavailabilityPeriod(portletAppName, portletName, e.getUnavailableSeconds());
        throw e;
      }
      throw new PortletException("exception while initializing portlet", t);
    }
  }

  /**
   * @param portletName portlet name
   * @return portlet config
   */
  public final PortletConfig getPortletConfig(final String portletName) {
    return configs.get(portletName);
  }

  /**
   * @param key key
   */
  private void registerPortlet(final String key) {
    try {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      String portletClassName = getPortletClassName(key);
      if (log.isDebugEnabled())
        log.debug("Load and register portlet '"+ key + "' with class '" + portletClassName + "'.");
      pico.registerComponentImplementation(portletAppName + Constants.PORTLET_ENCODER + key, cl.loadClass(portletClassName));
    } catch (Throwable e) {
      log.error("Can not register portlet : " + key, e);
    }
  }

  /**
   * @param portletName portlet name
   * @return portlet class name
   */
  private String getPortletClassName(final String portletName) {
    PortletApp portletApp = holder.getPortletApplication(portletAppName);
    List<Portlet> portletMetaDataList = portletApp.getPortlet();
    for (Iterator<Portlet> iterator = portletMetaDataList.iterator(); iterator.hasNext();) {
      Portlet portlet = iterator.next();
      if (portlet.getPortletName().equals(portletName))
        return portlet.getPortletClass();
    }
    return null;
  }

  /**
   * @param portletName portlet name
   */
  private void releasePortlet(final String portletName) {
    try {
      pico.unregisterComponent(portletAppName + Constants.PORTLET_ENCODER + portletName);
    } catch (Exception e) {
      log.error("Can not release portlet : " + portletName, e);
    }
  }

  /**
   * @param portletName portlet name
   */
  public final void destroy(final String portletName) {
    try {
      boolean everLoaded = false;
      synchronized (monitor) {
        everLoaded = monitor.isInitialized(portletAppName, portletName);
        log.debug("Was the portlet : " + portletAppName + "/" + portletName + " ever loaded : "
            + everLoaded);
        monitor.destroy(portletAppName, portletName);
      }
      if (!everLoaded)
        return;
      if (pico.getComponentInstance(portletAppName + Constants.PORTLET_ENCODER + portletName) == null) {
        log.debug("The portlet is already destroyed or in broken state");
        return;
      }
      log.debug("Wait " + WAITING_TIME_BEFORE_DESTROY + " seconds before destroying the portlet");
      Thread.sleep(WAITING_TIME_BEFORE_DESTROY);
      ((javax.portlet.Portlet) pico.getComponentInstance(portletAppName
          + Constants.PORTLET_ENCODER + portletName)).destroy();
    } catch (Throwable t) {
      //spec p34 ligne 28
      log
          .error(
              "If the portlet object throws a RuntimeException within the execution of the destroy "
                  + "method the portlet container must consider the portlet object successfully destroyed.",
              t);
    } finally {
      releasePortlet(portletName);
    }
  }

  /**
   * Load and register portlet classes.
   */
  public final void loadAndRegisterPortletClasses() {
    String[] portletNames = getPortletNames();
    initMonitor(portletNames);
    loadAndRegisterClassesByKey(portletNames);
  }

  /**
   * @return portlet names
   */
  private String[] getPortletNames() {
    PortletApp portletApp = holder.getPortletApplication(portletAppName);
    List<Portlet> portletMetaDataList = portletApp.getPortlet();
    String[] portletNames = new String[portletMetaDataList.size()];
    int i = 0;
    for (Iterator<Portlet> iterator = portletMetaDataList.iterator(); iterator.hasNext();) {
      Portlet portlet = iterator.next();
      portletNames[i] = portlet.getPortletName();
      i++;
    }
    return portletNames;
  }

  /**
   * @param portletNames portlet names
   */
  protected void initMonitor(final String[] portletNames) {
    synchronized (monitor) {
      monitor.registerPortletApp(portletAppName);
    }
    for (String portletName : portletNames) {
      registerPortletToMonitor(portletName);
    }
  }

  /**
   * @param portletName portlet name
   */
  public final void registerPortletToMonitor(final String portletName) {
    synchronized (monitor) {
      monitor.register(portletAppName, portletName);
    }
  }

  /**
   * @param keys keys
   */
  private void loadAndRegisterClassesByKey(final String[] keys) {
    log.info("Registering portlets: " + Arrays.asList(keys));
    for (String key : keys) {
      registerPortlet(key);
    }
  }

  /**
   * @param validatorClass validator class
   * @param portletName portlet name
   * @return preferences validator
   */
  public final PreferencesValidator getValidator(final String validatorClass, final String portletName) {
    return (PreferencesValidator) pico.getComponentInstance(portletAppName + "_" + portletName
        + Constants.VALIDATOR_ENCODER + validatorClass);
  }

  /**
   * Load and register validator classes.
   */
  public final void loadAndRegisterValidatorClasses() {
    String[] classNames = getValidatorClassNames();
    String[] portletNames = getValidatorsPortletsNames();
    if ((classNames == null) || (portletNames == null))
      return;
    loadAndRegisterClasses(classNames, portletNames);
  }

  /**
   * @return validator class names
   */
  public final String[] getValidatorClassNames() {
    PortletApp portletApp = holder.getPortletApplication(portletAppName);
    List<Portlet> portletMetaDataList = portletApp.getPortlet();
    if (portletMetaDataList.size() == 0)
      return null;
    String[] validatorNames = new String[portletMetaDataList.size()];
    int i = 0;
    for (Iterator<Portlet> iterator = portletMetaDataList.iterator(); iterator.hasNext();) {
      Portlet portlet = iterator.next();

      ExoPortletPreferences preferences = portlet.getPortletPreferences();
      if (preferences != null)
        validatorNames[i] = preferences.getPreferencesValidator();
      i++;
    }
    return validatorNames;
  }

  /**
   * @return validator names
   */
  public final String[] getValidatorsPortletsNames() {
    PortletApp portletApp = holder.getPortletApplication(portletAppName);
    List<Portlet> portletMetaDataList = portletApp.getPortlet();
    if (portletMetaDataList.size() == 0)
      return null;
    String[] validatorNames = new String[portletMetaDataList.size()];
    int i = 0;
    for (Iterator<Portlet> iterator = portletMetaDataList.iterator(); iterator.hasNext();) {
      Portlet portlet = iterator.next();

      ExoPortletPreferences preferences = portlet.getPortletPreferences();
      if (preferences != null)
        validatorNames[i] = portlet.getPortletName();
      i++;
    }
    return validatorNames;
  }

  /**
   * @param classNames class names
   * @param portletNames portlet names
   */
  private void loadAndRegisterClasses(final String[] classNames, final String[] portletNames) {
    for (int i = 0; i < classNames.length; i++) {
      String className = classNames[i];
      String portletName = portletNames[i];
      if (className != null)
        try {
          ClassLoader cl = Thread.currentThread().getContextClassLoader();
          pico.registerComponentImplementation(portletAppName + "_" + portletName
              + Constants.VALIDATOR_ENCODER + className, cl.loadClass(className));
        } catch (Exception e) {
          log.error("Can not load and register class : " + className, e);
        }
    }
  }

  /**
   * Load.
   */
  public final void load() {
    loadAndRegisterPortletClasses();
    loadAndRegisterValidatorClasses();
  }

  /**
   * @param servletContextName servlet context name
   */
  public final void setApplicationName(final String servletContextName) {
    this.portletAppName = servletContextName;
  }

  /**
   * Overridden method.
   *
   * @see org.picocontainer.Startable#start()
   */
  public final void start() {
  }

  /**
   * Overridden method.
   *
   * @see org.picocontainer.Startable#stop()
   */
  public final void stop() {
  }
}