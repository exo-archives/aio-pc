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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.pci.CustomModeWithDescription;
import org.exoplatform.services.portletcontainer.pci.CustomWindowStateWithDescription;
import org.exoplatform.services.portletcontainer.pci.LocalisedDescription;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.model.Description;
import org.exoplatform.services.portletcontainer.pci.model.EventDefinition;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationsHolder;
import org.exoplatform.services.wsrp2.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp2.producer.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp2.type.CookieProtocol;
import org.exoplatform.services.wsrp2.type.EventDescription;
import org.exoplatform.services.wsrp2.type.ItemDescription;
import org.exoplatform.services.wsrp2.type.ModelDescription;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ResourceList;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.utils.Utils;

/**
 * Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 10 Dec. 2003 Time: 09:40:23
 */
public class ServiceDescriptionInterfaceImpl implements ServiceDescriptionInterface {

  private PortletContainerProxy     proxy;

  public static String[]            localesArray = { "en", "fr" };

  private WSRPConfiguration         conf;

  private Log                       log;

  private ExoContainer              container;

  private PortletContainerConf      pcConf;

  private PortletApplicationsHolder pcHolder;

  public ServiceDescriptionInterfaceImpl(PortletContainerProxy cont,
                                         WSRPConfiguration conf,
                                         ExoContainerContext context) {
    this.proxy = cont;
    this.conf = conf;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    this.container = context.getContainer();
    this.pcConf = (PortletContainerConf) container.getComponentInstanceOfType(PortletContainerConf.class);
    this.pcHolder = (PortletApplicationsHolder) container.getComponentInstanceOfType(PortletApplicationsHolder.class);
  }

  // Store portlet application within ServiceDescription
  public ServiceDescription getServiceDescription(RegistrationContext registrationContext,
                                                  String[] desiredLocales) throws RemoteException {

    if (desiredLocales == null) {
      desiredLocales = new String[] { "en", "fr" };
    }

    log.debug("getServiceDescription entered with registrationContext : " + registrationContext);
    Map<String, PortletData> portletMetaDatas = proxy.getAllPortletMetaData();
    Set<String> keys = portletMetaDatas.keySet();
    Set<String> keys2 = new HashSet<String>(keys);
    if (conf.getExcludeList() != null) {
      for (Iterator<String> iter = conf.getExcludeList().iterator(); iter.hasNext();) {
        String handle = (String) iter.next();
        if (handle.endsWith("*")) {
          for (Object object : keys2) {
            if (((String) object).startsWith(handle.substring(0, handle.length() - 1))) {
              keys.remove((String) object);
            }
          }
        } else {
          if (keys.contains(handle))
            keys.remove(handle);
        }
      }
    }
    PortletDescription[] pdescription = new PortletDescription[keys.size()];
    int i = 0;
    for (Iterator<String> iter = keys.iterator(); iter.hasNext(); i++) {
      String producerOfferedPortletHandle = (String) iter.next();
      log.debug("fill service description with portlet description: " + producerOfferedPortletHandle);
      pdescription[i] = proxy.getPortletDescription(producerOfferedPortletHandle, desiredLocales);
    }
    ServiceDescription sD = new ServiceDescription();
    sD.setRequiresRegistration(conf.isRegistrationRequired());
    sD.setRegistrationPropertyDescription(new ModelDescription());// extension of the WSRP specs
    sD.setRequiresInitCookie(CookieProtocol.none);
    sD.setCustomModeDescriptions(getCustomModeDescriptions(pcConf.getSupportedPortletModesWithDescriptions()));
    //sD.setCustomUserProfileItemDescriptions(new ItemDescription[0]);
    sD.setCustomWindowStateDescriptions(getCustomWindowStateDescriptions(pcConf.getSupportedWindowStatesWithDescriptions()));
    sD.setLocales(localesArray);
    sD.setOfferedPortlets(pdescription);
    sD.setResourceList(new ResourceList());

    // WSRP v2 spec
    sD.setExtensionDescriptions(null);
    sD.setEventDescriptions(getEventDescriptions());
    sD.setSupportedOptions(new String[] { "wsrp:events", "wsrp:leasing", "wsrp:copyPortlets", "wsrp:import", "wsrp:export" });
    sD.setExportDescription(null);
    sD.setMayReturnRegistrationState(null);
    return sD;
  }

  private EventDescription[] getEventDescriptions() {
    List<EventDescription> eventDescriptions = new ArrayList<EventDescription>();
    List<PortletApp> portletApps = pcHolder.getPortletAppList();
    for (PortletApp portletApp : portletApps) {
      List<EventDefinition> eventDefinitions = portletApp.getEventDefinition();
      for (EventDefinition eventDefinition : eventDefinitions) {
        EventDescription ed = new EventDescription();
        ed.setName(eventDefinition.getPrefferedName());
        ed.setAliases(Utils.getQNameArray(eventDefinition.getAliases()));
        if (eventDefinition.getDescription() != null) {
          if (!eventDefinition.getDescription().isEmpty()) {
            Description d = eventDefinition.getDescription().get(0);
            ed.setDescription(Utils.getLocalizedString(d.getDescription(), "en"));
          }
        }
        ed.setSchemaLocation(null);
        if (eventDefinition.getJavaClass() != null && !"".equals(eventDefinition.getJavaClass())) {
          ed.setType(new QName(eventDefinition.getJavaClass()));
        }
        ed.setSchemaType(null);
        ed.setLabel(null);
        ed.setHint(null);
        ed.setExtensions(null);
        eventDescriptions.add(ed);
      }
    }
    return eventDescriptions.toArray(new EventDescription[] {});
  }

  public ServiceDescription getServiceDescription(RegistrationContext registrationContext,
                                                  String[] desiredLocales,
                                                  String[] portletHandles,
                                                  UserContext userContext) throws RemoteException {

    if (desiredLocales == null) {
      desiredLocales = new String[] { "en", "fr" };
    }

    log.debug("getServiceDescription entered with registrationContext : " + registrationContext);

    Map<String, PortletData> portletMetaDatas = proxy.getAllPortletMetaData();
    Set<String> keys = portletMetaDatas.keySet();

    if (conf.getExcludeList() != null) {

      for (Iterator<String> iter = conf.getExcludeList().iterator(); iter.hasNext();) {
        String handle = (String) iter.next();
        if (keys.contains(handle))
          keys.remove(handle);
      }

    }

    int n = 0;
    for (Iterator<String> iter = keys.iterator(); iter.hasNext(); n++) {
      String keysHandle = (String) iter.next();
      boolean found = false;
      for (int k = 0; k < portletHandles.length; k++) {
        if (portletHandles[k].equals(keysHandle)) {
          found = true;
          break;
        }
      }
      if (found == false) {
        keys.remove(keysHandle);
      }
    }

    // manage user
    String owner = userContext.getUserContextKey();
    log.debug("Owner Context : " + owner);

    PortletDescription[] pdescription = new PortletDescription[keys.size()];
    int i = 0;
    for (Iterator<String> iter = keys.iterator(); iter.hasNext(); i++) {
      String producerOfferedPortletHandle = (String) iter.next();
      log.debug("fill service description with portlet description ");
      pdescription[i] = proxy.getPortletDescription(producerOfferedPortletHandle, desiredLocales);
    }
    ServiceDescription sD = new ServiceDescription();
    sD.setRequiresRegistration(conf.isRegistrationRequired());
    sD.setRegistrationPropertyDescription(new ModelDescription());// extension
    // of the WSRP
    // specs
    sD.setRequiresInitCookie(CookieProtocol.none);
    sD.setCustomModeDescriptions(getCustomModeDescriptions(pcConf.getSupportedPortletModesWithDescriptions()));
    //    sD.setCustomUserProfileItemDescriptions(new ItemDescription[0]);
    sD.setCustomWindowStateDescriptions(getCustomWindowStateDescriptions(pcConf.getSupportedWindowStatesWithDescriptions()));
    sD.setLocales(localesArray);
    sD.setOfferedPortlets(pdescription);
    sD.setResourceList(new ResourceList());
    return sD;

  }

  private ItemDescription[] getCustomWindowStateDescriptions(Collection<CustomWindowStateWithDescription> collection) {
    Collection<ItemDescription> c = new ArrayList<ItemDescription>();
    for (Iterator<CustomWindowStateWithDescription> iter = collection.iterator(); iter.hasNext();) {
      CustomWindowStateWithDescription element = (CustomWindowStateWithDescription) iter.next();
      List<LocalisedDescription> l = element.getDescriptions();
      ItemDescription iD = null;
      for (Iterator<LocalisedDescription> iterator = l.iterator(); iterator.hasNext();) {
        LocalisedDescription d = (LocalisedDescription) iterator.next();
        iD = new ItemDescription();
        iD.setItemName(element.getWindowState().toString());
        iD.setDescription(Utils.getLocalizedString(d.getDescription(), d.getLocale().getLanguage()));
        c.add(iD);
      }
    }
    ItemDescription[] iDTab = new ItemDescription[c.size()];
    int i = 0;
    for (Iterator<ItemDescription> iter = c.iterator(); iter.hasNext(); i++) {
      iDTab[i] = (ItemDescription) iter.next();
    }
    return iDTab;
  }

  private ItemDescription[] getCustomModeDescriptions(Collection<CustomModeWithDescription> collection) {
    Collection<ItemDescription> c = new ArrayList<ItemDescription>();
    for (Iterator<CustomModeWithDescription> iter = collection.iterator(); iter.hasNext();) {
      CustomModeWithDescription element = (CustomModeWithDescription) iter.next();
      List<LocalisedDescription> l = element.getDescriptions();
      ItemDescription iD = null;
      for (Iterator<LocalisedDescription> iterator = l.iterator(); iterator.hasNext();) {
        LocalisedDescription d = (LocalisedDescription) iterator.next();
        iD = new ItemDescription();
        iD.setItemName(element.getPortletMode().toString());
        iD.setDescription(Utils.getLocalizedString(d.getDescription(), d.getLocale().getLanguage()));
        c.add(iD);
      }
    }
    ItemDescription[] iDTab = new ItemDescription[c.size()];
    int i = 0;
    for (Iterator<ItemDescription> iter = c.iterator(); iter.hasNext(); i++) {
      iDTab[i] = (ItemDescription) iter.next();
    }
    return iDTab;
  }

}
