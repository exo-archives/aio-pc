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

package org.exoplatform.services.wsrp2.producer.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp2.type.CopiedPortlet;
import org.exoplatform.services.wsrp2.type.CopyPortletsResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportedPortlet;
import org.exoplatform.services.wsrp2.type.FailedPortlets;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.ImportPortlet;
import org.exoplatform.services.wsrp2.type.ImportPortletsResponse;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.ModelDescription;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletLifetime;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.Property;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ResourceList;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class PortletManagementOperationsInterfaceImpl implements
    PortletManagementOperationsInterface {

  private PortletContainerProxy  proxy;

  private Log                    log;

  private PersistentStateManager stateManager;

  public PortletManagementOperationsInterfaceImpl(PersistentStateManager stateManager,
                                                  PortletContainerProxy portletContainerProxy) {
    this.proxy = portletContainerProxy;
    this.stateManager = stateManager;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
  }

  public PortletContext clonePortlet(RegistrationContext registrationContext,
                                     PortletContext portletContext,
                                     UserContext userContext,
                                     Lifetime lifetime) throws RemoteException {
    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Clone a portlet for the registered consumer : " + registrationHandle);
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(registrationContext,
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
        if (!proxy.isPortletOffered(portletHandle)) {
          log.debug("The latter handle is not offered by the Producer");
          Exception2Fault.handleException(new WSRPException(Faults.INVALID_HANDLE_FAULT));
        }
        newPortletHandle = createNewPortletHandle(portletHandle);
        stateManager.addConsumerConfiguredPortletHandle(newPortletHandle, registrationContext);
      }
      if (lifetime != null) {
        stateManager.putPortletLifetime(newPortletHandle, lifetime);
      }
    } catch (WSRPException e) {
      log.error("Can not clone portlet", e);
      Exception2Fault.handleException(e);
    }
    log.debug("New portlet handle : " + newPortletHandle);
    PortletContext clonedPortletContext = new PortletContext();
    clonedPortletContext.setPortletHandle(newPortletHandle);
    clonedPortletContext.setScheduledDestruction(lifetime);
    return clonedPortletContext;
  }

  public CopyPortletsResponse copyPortlets(RegistrationContext toRegistrationContext,
                                           UserContext toUserContext,
                                           RegistrationContext fromRegistrationContext,
                                           UserContext fromUserContext,
                                           PortletContext[] fromPortletContexts,
                                           Lifetime lifetime) throws RemoteException {

    // TODO verify the userContext content
    String registrationHandle = fromRegistrationContext.getRegistrationHandle();
    log.debug("Copying portlets for the registered consumer : " + registrationHandle);
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(fromRegistrationContext,
                                                                               stateManager);
    Collection<CopiedPortlet> copiedPortlets = new ArrayList<CopiedPortlet>();
    Collection<FailedPortlets> failedPortlets = new ArrayList<FailedPortlets>();

    for (int i = 0; i < fromPortletContexts.length; i++) {

      String portletHandle = fromPortletContexts[i].getPortletHandle();
      String newPortletHandle = null;

      try {

        PropertyList list = getPortletProperties(fromRegistrationContext,
                                                 fromPortletContexts[i],
                                                 fromUserContext,
                                                 null);
        newPortletHandle = createNewPortletHandle(portletHandle);

        PortletContext newPortletContext = new PortletContext();
        newPortletContext.setPortletHandle(newPortletHandle);
        if (lifetime != null)
          newPortletContext.setScheduledDestruction(lifetime);

        newPortletContext.setPortletState(fromPortletContexts[i].getPortletState());

        CopiedPortlet copiedPortlet = new CopiedPortlet();
        copiedPortlet.setNewPortletContext(newPortletContext);

        copiedPortlets.add(copiedPortlet);

      } catch (Exception e) {
        log.error("Can not copy portlet", e);
        FailedPortlets failedPortlet = new FailedPortlets();

        // TODO: Now Needs array here..
        // failedPortlet.setPortletHandles(newPortletHandle);

        LocalizedString reason = new LocalizedString();
        reason.setValue("Cannot copy portlet");
        failedPortlet.setReason(reason);
        failedPortlets.add(failedPortlet);
        //         Exception2Fault.handleException(e);
        e.printStackTrace();
      }

    }

    CopiedPortlet[] arrayCopied = (CopiedPortlet[]) copiedPortlets.toArray(new CopiedPortlet[copiedPortlets.size()]);
    FailedPortlets[] arrayFailed = (FailedPortlets[]) failedPortlets.toArray(new FailedPortlets[failedPortlets.size()]);

    CopyPortletsResponse copyPortletsResponse = new CopyPortletsResponse();
    copyPortletsResponse.setCopiedPortlets(arrayCopied);
    copyPortletsResponse.setFailedPortlets(arrayFailed);

    return copyPortletsResponse;

  }

  public ExportPortletsResponse exportPortlets(RegistrationContext registrationContext,
                                               PortletContext[] portletContexts,
                                               UserContext userContext,
                                               Lifetime lifetime,
                                               boolean exportByValueRequired) throws RemoteException {
    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Exporting portlets for the registered consumer : " + registrationHandle);
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(registrationContext,
                                                                               stateManager);
    Collection<ExportedPortlet> exportedPortlets = new ArrayList<ExportedPortlet>();
    Collection<FailedPortlets> failedPortlets = new ArrayList<FailedPortlets>();

    if (exportByValueRequired == true) {
      Exception2Fault.handleException(new WSRPException(Faults.EXPORT_BY_VALUE_NOT_SUPPORTED_FAULT));
    }

    for (int i = 0; i < portletContexts.length; i++) {

      PortletContext portContext = portletContexts[i];
      try {

        byte[] exportData = portContext.getPortletState();
        ExportedPortlet exportedPortlet = new ExportedPortlet();
        exportedPortlet.setExportData(exportData);
        exportedPortlet.setPortletHandle(portContext.getPortletHandle());
        exportedPortlets.add(exportedPortlet);

      } catch (Exception e) {
        log.error("Can not copy portlet", e);
        FailedPortlets failedPortlet = new FailedPortlets();
        // failedPortlet.setPortletHandles(portContext.getPortletHandle());
        LocalizedString reason = new LocalizedString();
        reason.setValue("Cannot copy portlet");
        failedPortlet.setReason(reason);
        failedPortlets.add(failedPortlet);
        // Exception2Fault.handleException(e);
        e.printStackTrace();
      }
    }

    ExportPortletsResponse exportPortletsResponse = new ExportPortletsResponse();
    if (lifetime != null)
      exportPortletsResponse.setLifetime(lifetime);

    ExportedPortlet[] arrayExported = (ExportedPortlet[]) exportedPortlets.toArray(new ExportedPortlet[exportedPortlets.size()]);
    FailedPortlets[] arrayFailed = (FailedPortlets[]) failedPortlets.toArray(new FailedPortlets[failedPortlets.size()]);
    exportPortletsResponse.setExportedPortlet(arrayExported);
    exportPortletsResponse.setFailedPortlets(arrayFailed);
    return exportPortletsResponse;

  }

  public ImportPortletsResponse importPortlets(RegistrationContext registrationContext,
                                               byte[] importContext,
                                               ImportPortlet[] importPortlets,
                                               UserContext userContext,
                                               Lifetime lifetime)

  throws RemoteException {
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Exporting portlets for the registered consumer : " + registrationHandle);
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(registrationContext,
                                                                               stateManager);
    Collection<ImportPortlet> importedPortlets = new ArrayList<ImportPortlet>();
    Collection<FailedPortlets> failedPortlets = new ArrayList<FailedPortlets>();

    for (int i = 0; i < importPortlets.length; i++) {

      ImportPortlet importPortlet = importPortlets[i];

      try {
        // PortletHandle portletHandle = importPortlet.
        //        byte[] importData = importPortlet.getExportData();
        importedPortlets.add(importPortlet);

      } catch (Exception e) {
        log.error("Can not import portlet", e);
        FailedPortlets failedPortlet = new FailedPortlets();
        // failedPortlet.setPortletHandles(portContext.getPortletHandle());
        LocalizedString reason = new LocalizedString();
        reason.setValue("Cannot import portlet");
        failedPortlet.setReason(reason);
        failedPortlets.add(failedPortlet);
        // Exception2Fault.handleException(e);
        e.printStackTrace();

      }

    }

    ImportPortletsResponse importPortletsResponse = new ImportPortletsResponse();

    return importPortletsResponse;

  }

  public ReturnAny releaseExport(byte[] exportContext,
                                 UserContext userContext,
                                 RegistrationContext registrationContext) {
    return new ReturnAny();

  }

  public Lifetime setExportLifetime(RegistrationContext registrationContext,
                                    byte[] exportContext,
                                    UserContext userContext,
                                    Lifetime lifetime) throws RemoteException {

    return lifetime;

  }

  public DestroyPortletsResponse destroyPortlets(RegistrationContext registrationContext,
                                                 String[] portletHandles,
                                                 UserContext userContext) throws RemoteException {
    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Destroy portlet for registration handle " + registrationHandle);
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(registrationContext,
                                                                               stateManager);

    Collection<FailedPortlets> fails = new ArrayList<FailedPortlets>();

    for (int i = 0; i < portletHandles.length; i++) {
      String portletHandle = portletHandles[i];
      try {
        if (stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
          log.debug("Destroy a consumer configured portlet : " + portletHandle);
          stateManager.removeConsumerConfiguredPortletHandle(portletHandle, registrationContext);
        } else {
          log.debug("Can't destroy a portlet that did not exist : " + portletHandle);
          FailedPortlets failedPortlets = new FailedPortlets();
          failedPortlets.setPortletHandles(new String[] { portletHandle });
          failedPortlets.setReason(new LocalizedString("Can't destroy a portlet that did not exist",
                                                       ""));
          fails.add(failedPortlets);
        }
      } catch (WSRPException e) {
        Exception2Fault.handleException(e);
      }
    }

    DestroyPortletsResponse response = new DestroyPortletsResponse();
    // Convert from Collection<FailedPortlets> to array FailedPortlets[]
    FailedPortlets[] array = (FailedPortlets[]) fails.toArray(new FailedPortlets[fails.size()]);
    if (array != null)
      response.setFailedPortlets(array);
    return response;
  }

  public GetPortletsLifetimeResponse getPortletsLifetime(RegistrationContext registrationContext,
                                                         PortletContext[] portletContexts,
                                                         UserContext userContext) throws RemoteException {

    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Set portlet properties for registration handle " + registrationHandle);
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(registrationContext,
                                                                               stateManager);

    Collection<PortletLifetime> portletLifetimes = new ArrayList<PortletLifetime>();
    Collection<FailedPortlets> failedPortlets = new ArrayList<FailedPortlets>();

    for (int i = 0; i < portletContexts.length; i++) {
      PortletContext portletContext = portletContexts[i];
      String portletHandle = portletContext.getPortletHandle();
      try {
        Lifetime lifetimeResult = stateManager.getPortletLifetime(portletHandle);
        PortletLifetime portletLifetime = new PortletLifetime();
        portletLifetime.setPortletContext(portletContext);
        portletLifetime.setScheduledDestruction(lifetimeResult);
        portletLifetimes.add(portletLifetime);
      } catch (Exception e) {
        FailedPortlets fPortlet = new FailedPortlets();
        failedPortlets.add(fPortlet);
      }
    }
    
    PortletLifetime[] arrayLifetime = (PortletLifetime[]) portletLifetimes.toArray(new PortletLifetime[portletLifetimes.size()]);
    FailedPortlets[] arrayFailed = (FailedPortlets[]) failedPortlets.toArray(new FailedPortlets[failedPortlets.size()]);
    
    GetPortletsLifetimeResponse lfResponse = new GetPortletsLifetimeResponse();
    lfResponse.setFailedPortlets(arrayFailed);
    lfResponse.setPortletLifetime(arrayLifetime);
    return lfResponse;

  }

  public SetPortletsLifetimeResponse setPortletsLifetime(RegistrationContext registrationContext,
                                                         PortletContext[] portletContexts,
                                                         UserContext userContext,
                                                         Lifetime lifetime) throws RemoteException {

    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Set portlet properties for registration handle " + registrationHandle);
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(registrationContext,
                                                                               stateManager);

    Collection<PortletLifetime> portletLifetimes = new ArrayList<PortletLifetime>();
    Collection<FailedPortlets> failedPortlets = new ArrayList<FailedPortlets>();

    for (int i = 0; i < portletContexts.length; i++) {
      PortletContext portletContext = portletContexts[i];
      String portletHandle = portletContext.getPortletHandle();
      try {
        Lifetime lifetimeResult = stateManager.putPortletLifetime(portletHandle, lifetime);
        PortletLifetime portletLifetime = new PortletLifetime();
        portletLifetime.setPortletContext(portletContext);
        portletLifetime.setScheduledDestruction(lifetimeResult);
        portletLifetimes.add(portletLifetime);
      } catch (Exception e) {
        FailedPortlets fPortlet = new FailedPortlets();
        failedPortlets.add(fPortlet);
      }
    }
    PortletLifetime[] arrayUpdated = (PortletLifetime[]) portletLifetimes.toArray(new PortletLifetime[portletLifetimes.size()]);
    FailedPortlets[] arrayFailed = (FailedPortlets[]) failedPortlets.toArray(new FailedPortlets[failedPortlets.size()]);
    
    SetPortletsLifetimeResponse setLifetimeResponse = new SetPortletsLifetimeResponse();
    setLifetimeResponse.setUpdatedPortlet(arrayUpdated);
    setLifetimeResponse.setFailedPortlets(arrayFailed);
    return setLifetimeResponse;
  }

  public PortletDescriptionResponse getPortletDescription(RegistrationContext registrationContext,
                                                          PortletContext portletContext,
                                                          UserContext userContext,
                                                          String[] desiredLocales) throws RemoteException {
    // TODO verify the userContext content
    String registrationHandle = registrationContext.getRegistrationHandle();
    log.debug("Get portlet description for registration handle " + registrationHandle);
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(registrationContext,
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

    PortletDescription pD = proxy.getPortletDescription(portletHandle, desiredLocales);
    ResourceList resourceList = proxy.getResourceList(desiredLocales);

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
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(registrationContext,
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
      proxy.setPortletProperties(portletHandle, userID, propertyList);
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
    org.exoplatform.services.wsrp2.producer.impl.utils.Utils.checkRegistration(registrationContext,
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
      properties = proxy.getPortletProperties(portletHandle, userID);
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
        prop.setName(new QName(key));
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
    PortletPropertyDescriptionResponse portletPropertyDescriptionResponse = new PortletPropertyDescriptionResponse();
    ModelDescription modelDescription = new ModelDescription();
    modelDescription.setPropertyDescriptions(null);
    ResourceList resourceList = new ResourceList();
    resourceList.setResources(null);
    portletPropertyDescriptionResponse.setModelDescription(null);
    portletPropertyDescriptionResponse.setResourceList(null);
    return portletPropertyDescriptionResponse;
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
