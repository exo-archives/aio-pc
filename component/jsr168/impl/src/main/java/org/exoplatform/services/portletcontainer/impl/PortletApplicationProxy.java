package org.exoplatform.services.portletcontainer.impl;

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
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.impl.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletConfigImp;
import org.exoplatform.services.portletcontainer.pci.model.*;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 11 nov. 2003
 * Time: 22:56:04
 */
public class PortletApplicationProxy implements Startable{
	private static final int WAITING_TIME_BEFORE_DESTROY = 50;//ms
	PortletApplicationsHolder holder_;
	private String portletAppName_;	
  private Map configs_;
  private PortletContainerMonitorImpl monitor_;
  private Log log_;
  private MutablePicoContainer pico_;

  public PortletApplicationProxy(ExoContainerContext context,
                                 PortletApplicationsHolder holder,
                                 PortletContainerMonitorImpl monitor,
                                 LogService logService) {
		this.holder_ = holder;
    this.monitor_ = monitor;
    configs_ = new HashMap();
    this.log_ = logService.getLog("org.exoplatform.services.portletcontainer");
    pico_ = new DefaultPicoContainer(context.getContainer());
	}

	public javax.portlet.Portlet getPortlet(PortletContext portletContext, String portletName)
					throws PortletException {
    log_.debug("getPortlet() in PortletApplicationProxy entered");
		synchronized (monitor_) {
			if (!monitor_.isInitialized(portletAppName_, portletName)) {
        log_.debug("init monitor");
				init(monitor_, portletName, portletContext);
			}
		}
		return (javax.portlet.Portlet) pico_.
        getComponentInstance(portletAppName_ + Constants.PORTLET_ENCODER + portletName);
	}

	private void init(PortletContainerMonitorImpl monitor, String portletName, PortletContext portletContext) throws PortletException {
		long accessTime = System.currentTimeMillis();
		if (!monitor.isInitialisationAllowed(portletAppName_, portletName, accessTime))
			throw new UnavailableException("Portlet initialization not possible");

		Portlet portletDatas = holder_.getPortletMetaData(portletAppName_, portletName);
		PortletApp portletApp = holder_.getPortletApplication(portletAppName_);
		PortletConfig config = new PortletConfigImp(portletDatas, portletContext,
						portletApp.getSecurityConstraint(),
						portletApp.getUserAttribute(),
						portletApp.getCustomPortletMode(),
						portletApp.getCustomWindowState());
		try {
			if (pico_.getComponentInstance(portletAppName_ + Constants.PORTLET_ENCODER +
          portletDatas.getPortletName()) == null){
        log_.debug("First registration of portlet : " + portletAppName_ + "/" + portletName);
				registerPortlet(portletDatas.getPortletName());
      }
			((javax.portlet.Portlet) pico_.getComponentInstance(portletAppName_ + Constants.PORTLET_ENCODER
							+ portletDatas.getPortletName())).init(config);
      configs_.put(portletDatas.getPortletName(), config);
      String expirationStr = portletDatas.getExpirationCache() ;
      int expiration = 0 ;
      if(expirationStr != null) {
        expiration = Integer.parseInt(portletDatas.getExpirationCache()) ;
      }
		  monitor.init(portletAppName_, portletName, expiration);
			monitor.setInitializationTime(portletAppName_, portletName, accessTime);
		} catch (Throwable t) {
      log_.error("exception while initializing portlet : " + portletName, t);
			monitor.setLastInitFailureAccessTime(portletAppName_, portletName, accessTime);
			releasePortlet(portletName);
			if (t instanceof UnavailableException) {
				UnavailableException e = (UnavailableException) t;
				if (!e.isPermanent()) {
					monitor.setUnavailabilityPeriod(portletAppName_, portletName, e.getUnavailableSeconds());
				}
				throw e;
			}
		  throw new PortletException("exception while initializing portlet", t);
		}
	}
  
  public PortletConfig getPortletConfig(String portletName){
    return (PortletConfig) configs_.get(portletName);
  }

	private void registerPortlet(String key) {
		try {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      pico_.registerComponentImplementation(portletAppName_ + Constants.PORTLET_ENCODER + key,
          cl.loadClass(getPortletClassName(key)));
		} catch (Exception e) {
      log_.error("Can not register portlet : " + key, e);
		}
	}

	private String getPortletClassName(String portletName) {
		PortletApp portletApp = holder_.getPortletApplication(portletAppName_);
		List portletMetaDataList = portletApp.getPortlet();
		for (Iterator iterator = portletMetaDataList.iterator(); iterator.hasNext();) {
			Portlet portlet = (Portlet) iterator.next();
			if (portlet.getPortletName().equals(portletName))
				return portlet.getPortletClass();
		}
		return null;
	}

	private void releasePortlet(String portletName) {
		try {
		  pico_.unregisterComponent(portletAppName_ + Constants.PORTLET_ENCODER + portletName);
		} catch (Exception e) {
      log_.error("Can not release portlet : " + portletName, e);
		}
	}

	public void destroy(String portletName) {
		try {
			boolean everLoaded = false;
			synchronized (monitor_) {
				everLoaded = monitor_.isInitialized(portletAppName_, portletName);
        log_.debug("Was the portlet : " + portletAppName_ + "/" + portletName + " ever loaded : " + everLoaded);
				monitor_.destroy(portletAppName_, portletName);
			}      
			if (!everLoaded)
				return;
			if (pico_.getComponentInstance(portletAppName_ + Constants.PORTLET_ENCODER + portletName) == null){
        log_.debug("The portlet is already destroyed or in broken state");
				return;
      }      
      log_.debug("Wait " + WAITING_TIME_BEFORE_DESTROY + " seconds before destroying the portlet");
			Thread.sleep(WAITING_TIME_BEFORE_DESTROY);
			((javax.portlet.Portlet) pico_.
          getComponentInstance(portletAppName_ + Constants.PORTLET_ENCODER + portletName)).destroy();
		} catch (Throwable t) {
			//spec p34 ligne 28
			log_.error("If the portlet object throws a RuntimeException within the execution of the destroy " +
			           "method the portlet container must consider the portlet object successfully destroyed.", t);
		} finally {
			releasePortlet(portletName);
		}
	}

	public void loadAndRegisterPortletClasses() {
		String[] portletNames = getPortletNames();
		initMonitor(portletNames);
		loadAndRegisterClassesByKey(portletNames);
	}

	private String[] getPortletNames() {
		PortletApp portletApp = holder_.getPortletApplication(portletAppName_);
		List portletMetaDataList = portletApp.getPortlet();
		String[] portletNames = new String[portletMetaDataList.size()];
		int i = 0;
		for (Iterator iterator = portletMetaDataList.iterator(); iterator.hasNext();) {
			Portlet portlet = (Portlet) iterator.next();
			portletNames[i] = portlet.getPortletName() ;
			i++;
		}
		return portletNames;
	}

	protected void initMonitor(String[] portletNames) {
		synchronized (monitor_) {
			monitor_.registerPortletApp(portletAppName_);
		}
		for (int i = 0; i < portletNames.length; i++) {
			String portletName = portletNames[i];
			registerPortletToMonitor(portletName);
		}
	}

	public void registerPortletToMonitor(String portletName) {
		synchronized (monitor_) {
			monitor_.register(portletAppName_, portletName);
		}
	}

	private void loadAndRegisterClassesByKey(String[] keys) {
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			registerPortlet(key);
		}
	}

	public PreferencesValidator getValidator(String validatorClass, String portletName) {
		return (PreferencesValidator) pico_.getComponentInstance(portletAppName_ + "_" + portletName +
						Constants.VALIDATOR_ENCODER + validatorClass);
	}

	public void loadAndRegisterValidatorClasses() {
		String[] classNames = getValidatorClassNames();
		String[] portletNames = getValidatorsPortletsNames();
		if (classNames == null || portletNames == null)
			return;
		loadAndRegisterClasses(classNames, portletNames);
	}

	public String[] getValidatorClassNames() {
		PortletApp portletApp = holder_.getPortletApplication(portletAppName_);
		List portletMetaDataList = portletApp.getPortlet();
		if (portletMetaDataList.size() == 0)
			return null;
		String[] validatorNames = new String[portletMetaDataList.size()];
		int i = 0;
		for (Iterator iterator = portletMetaDataList.iterator(); iterator.hasNext();) {
			Portlet portlet = (Portlet) iterator.next();

			ExoPortletPreferences preferences = portlet.getPortletPreferences();
			if (preferences != null)
				validatorNames[i] = preferences.getPreferencesValidator();
			i++;
		}
		return validatorNames;
	}

	public String[] getValidatorsPortletsNames() {
		PortletApp portletApp = holder_.getPortletApplication(portletAppName_);
		List portletMetaDataList = portletApp.getPortlet();
		if (portletMetaDataList.size() == 0)
			return null;
		String[] validatorNames = new String[portletMetaDataList.size()];
		int i = 0;
		for (Iterator iterator = portletMetaDataList.iterator(); iterator.hasNext();) {
			Portlet portlet = (Portlet) iterator.next();

			ExoPortletPreferences preferences = portlet.getPortletPreferences();
			if (preferences != null)
				validatorNames[i] = portlet.getPortletName();
			i++;
		}
		return validatorNames;
	}

	private void loadAndRegisterClasses(String[] classNames, String[] portletNames) {
		for (int i = 0; i < classNames.length; i++) {
			String className = classNames[i];
			String portletName = portletNames[i];
			if (className != null) {
				try {
          ClassLoader cl = Thread.currentThread().getContextClassLoader();
					pico_.registerComponentImplementation(portletAppName_ + "_" + portletName + Constants.VALIDATOR_ENCODER +
					    className, cl.loadClass(className));
				} catch (Exception e) {
          log_.error("Can not load and register class : " + className, e);
				}
			}
		}
	}

	public void load() {
		loadAndRegisterPortletClasses();
		loadAndRegisterValidatorClasses();
	}

	public void setApplicationName(String servletContextName) {
		this.portletAppName_ = servletContextName;
	}

  public void start() {
  }

  public void stop() {
  }
}