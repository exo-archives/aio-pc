/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp.producer.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;
import org.exoplatform.services.wsrp.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp.type.DestroyFailed;
import org.exoplatform.services.wsrp.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp.type.Property;
import org.exoplatform.services.wsrp.type.PropertyList;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ResourceList;
import org.exoplatform.services.wsrp.type.UserContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class PortletManagementOperationsInterfaceImpl implements
    PortletManagementOperationsInterface {

  private PortletContainerProxy  container;

  private Log                    log;

  private PersistentStateManager stateManager;

  public PortletManagementOperationsInterfaceImpl(PersistentStateManager stateManager,
                                                  PortletContainerProxy container) {
    this.container = container;
    this.stateManager = stateManager;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp");
  }

  public PortletContext clonePortlet(RegistrationContext registrationContext,
                                     PortletContext portletContext,
                                     UserContext userContext) throws RemoteException {
    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Clone a portlet for the registered consumer : " + registrationHandle);
    org.exoplatform.services.wsrp.producer.impl.utils.Utils.testRegistration(registrationContext,
                                                                             stateManager);

    String portletHandle = portletContext.getPortletHandle();
    String newPortletHandle = null;
    try {
      if (stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
        log.debug("Clone a Consumer Configured Portlet with handle : " + portletHandle);
        PropertyList list = getPortletProperties(registrationContext,
                                                 portletContext,
                                                 userContext,
                                                 null);
        newPortletHandle = createNewPortletHandle(portletHandle);
        stateManager.addConsumerConfiguredPortletHandle(newPortletHandle, registrationContext);
        portletContext.setPortletHandle(newPortletHandle);
        setPortletProperties(registrationContext, portletContext, userContext, list);
      } else {
        log.debug("Clone a Producer Offered Portlet with handle : " + portletHandle);
        if (!container.isPortletOffered(portletHandle)) {
          log.debug("The latter handle is not offered by the Producer");
          Exception2Fault.handleException(new WSRPException(Faults.INVALID_HANDLE_FAULT));
        }
        newPortletHandle = createNewPortletHandle(portletHandle);
        stateManager.addConsumerConfiguredPortletHandle(newPortletHandle, registrationContext);
      }
    } catch (WSRPException e) {
      log.error("Can not clone portlet", e);
      Exception2Fault.handleException(e);
    }
    log.debug("New portlet handle : " + newPortletHandle);
    PortletContext clonedPortletContext = new PortletContext();
    clonedPortletContext.setPortletHandle(newPortletHandle);
    return clonedPortletContext;
  }

  public DestroyPortletsResponse destroyPortlets(RegistrationContext registrationContext,
                                                 String[] portletHandles) throws RemoteException {
    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Destroy portlet for registration handle " + registrationHandle);
    org.exoplatform.services.wsrp.producer.impl.utils.Utils.testRegistration(registrationContext,
                                                                             stateManager);

    Collection<DestroyFailed> fails = new ArrayList<DestroyFailed>();
    for (int i = 0; i < portletHandles.length; i++) {
      String portletHandle = portletHandles[i];
      try {
        if (stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
          log.debug("Destroy a consumer configured portlet : " + portletHandle);
          stateManager.removeConsumerConfiguredPortletHandle(portletHandle, registrationContext);
        } else {
          log.debug("Can't destroy a portlet that did not exist : " + portletHandle);
          DestroyFailed destroyFailed = new DestroyFailed();
          destroyFailed.setPortletHandle(portletHandle);
          destroyFailed.setReason("Can't destroy a portlet that did not exist");
          fails.add(destroyFailed);
        }
      } catch (WSRPException e) {
        Exception2Fault.handleException(e);
      }
    }
    DestroyPortletsResponse response = new DestroyPortletsResponse();
    DestroyFailed[] array = (DestroyFailed[]) fails.toArray(new DestroyFailed[fails.size()]);
    if (array != null)
      response.setDestroyFailed(array);
    return response;
  }

  public PortletDescriptionResponse getPortletDescription(RegistrationContext registrationContext,
                                                          PortletContext portletContext,
                                                          UserContext userContext,
                                                          String[] desiredLocales) throws RemoteException {
    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Get portlet description for registration handle " + registrationHandle);
    org.exoplatform.services.wsrp.producer.impl.utils.Utils.testRegistration(registrationContext,
                                                                             stateManager);

    String portletHandle = portletContext.getPortletHandle();

    try {
      if (!stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
        log.debug("This portlet handle " + portletHandle
            + " is not valid in the scope of that registration ");
        Exception2Fault.handleException(new WSRPException(Faults.ACCESS_DENIED_FAULT));
      }
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    PortletDescription pD = container.getPortletDescription(portletHandle, desiredLocales);
    ResourceList resourceList = container.getResourceList(desiredLocales);

    PortletDescriptionResponse response = new PortletDescriptionResponse();
    response.setPortletDescription(pD);
    response.setResourceList(resourceList);

    return response;
  }

  public PortletContext setPortletProperties(RegistrationContext registrationContext,
                                             PortletContext portletContext,
                                             UserContext userContext,
                                             PropertyList propertyList) throws RemoteException {
    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Set portlet properties for registration handle " + registrationHandle);
    org.exoplatform.services.wsrp.producer.impl.utils.Utils.testRegistration(registrationContext,
                                                                             stateManager);
    String portletHandle = portletContext.getPortletHandle();

    try {
      if (!stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
        log.debug("This portlet handle " + portletHandle
            + " is not valid in the scope of that registration ");
        Exception2Fault.handleException(new WSRPException(Faults.ACCESS_DENIED_FAULT));
      }
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    String userID = userContext.getUserContextKey();

    try {
      container.setPortletProperties(portletHandle, userID, propertyList);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    return portletContext;
  }

  public PropertyList getPortletProperties(RegistrationContext registrationContext,
                                           PortletContext portletContext,
                                           UserContext userContext,
                                           String[] names) throws RemoteException {
    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("get portlet properties for registration handle " + registrationHandle);
    org.exoplatform.services.wsrp.producer.impl.utils.Utils.testRegistration(registrationContext,
                                                                             stateManager);
    String portletHandle = portletContext.getPortletHandle();
    try {
      if (!stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
        log.debug("This portlet handle " + portletHandle
            + " is not valid in the scope of that registration ");
        Exception2Fault.handleException(new WSRPException(Faults.ACCESS_DENIED_FAULT));
      }
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    String userID = userContext.getUserContextKey();
    Map<String, String[]> properties = null;
    try {
      properties = container.getPortletProperties(portletHandle, userID);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    Collection<Property> properties2return = new ArrayList<Property>();
    Set<String> keys = properties.keySet();
    for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
      String key = (String) iterator.next();
      if (names == null || arrayContainsKey(names, key)) {
        String[] values = (String[]) properties.get(key);
        Property prop = new Property();
        prop.setName(key);
        prop.setStringValue(values[0]);
        properties2return.add(prop);
      }
    }
    PropertyList list = new PropertyList();
    list.setProperties((Property[]) properties2return.toArray(new Property[properties2return.size()]));
    list.setResetProperties(null);
    return list;
  }

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(RegistrationContext registrationContext,
                                                                          PortletContext portletContext,
                                                                          UserContext userContext,
                                                                          String[] desiredLocales) throws RemoteException {
    return new PortletPropertyDescriptionResponse();
  }

  private boolean arrayContainsKey(String[] array, String key) {
    for (int i = 0; i < array.length; i++) {
      String s = array[i];
      if (s.equals(key)) {
        return true;
      }
    }
    return false;
  }

  private String createNewPortletHandle(String portletHandle) {
    String[] keys = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String newPortletHandle = keys[0] + Constants.PORTLET_HANDLE_ENCODER + keys[1]
        + Constants.PORTLET_HANDLE_ENCODER + IdentifierUtil.generateUUID(portletHandle);
    return newPortletHandle;
  }

}
