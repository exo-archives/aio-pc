/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp2.producer.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp2.producer.impl.helpers.LifetimeHelper;
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
 * Author : Tuan Nguyen tuan08@users.sourceforge.net Author : Mestrallet
 * Benjamin benjmestrallet@users.sourceforge.net Date: 10 Dec. 2003 Time:
 * 09:40:23
 */
public class ServiceDescriptionInterfaceImpl implements ServiceDescriptionInterface {

  private PortletContainerProxy     proxy;

  private final static List<String> LOCALES           = Arrays.asList(new String[] { "en", "fr" });

  private final static List<String> SUPPORTED_OPTIONS = Arrays.asList(new String[] { "wsrp:events",
      "wsrp:leasing", "wsrp:copyPortlets", "wsrp:import", "wsrp:export" });

  private WSRPConfiguration         conf;

  private Log                       log;

  private ExoContainer              container;

  private PortletContainerConf      pcConf;

  private PortletApplicationsHolder pcHolder;

  public ServiceDescriptionInterfaceImpl(PortletContainerProxy cont,
                                         WSRPConfiguration conf,
                                         ExoContainerContext context,
                                         PortletContainerConf pcConf,
                                         PortletApplicationsHolder pcHolder) {
    this.proxy = cont;
    this.conf = conf;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    this.container = context.getContainer();
    this.pcConf = pcConf;//(PortletContainerConf) container.getComponentInstanceOfType(PortletContainerConf.class);
    this.pcHolder = pcHolder;//(PortletApplicationsHolder) container.getComponentInstanceOfType(PortletApplicationsHolder.class);
  }

  private List<EventDescription> getEventDescriptions() {
    List<EventDescription> eventDescriptions = new ArrayList<EventDescription>();
    List<PortletApp> portletApps = pcHolder.getPortletAppList();
    for (PortletApp portletApp : portletApps) {
      List<EventDefinition> eventDefinitions = portletApp.getEventDefinition();
      for (EventDefinition eventDefinition : eventDefinitions) {
        EventDescription ed = new EventDescription();
        ed.setName(eventDefinition.getPrefferedName());
        ed.getAliases().addAll(eventDefinition.getAliases());
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
        eventDescriptions.add(ed);
      }
    }
    return eventDescriptions;
  }

  public ServiceDescription getServiceDescription(RegistrationContext registrationContext,
                                                  List<String> desiredLocales,
                                                  List<String> portletHandles,
                                                  UserContext userContext) throws ResourceSuspended,
                                                                          InvalidRegistration,
                                                                          ModifyRegistrationRequired,
                                                                          OperationFailed,
                                                                          WSRPException {

    try {
      if (registrationContext != null
          && RegistrationVerifier.checkRegistrationContext(registrationContext)) {
        LifetimeHelper.checkRegistrationLifetime(registrationContext, userContext);
      }
    } catch (InvalidHandle ih) {
      throw new InvalidRegistration(ih.getMessage(), ih);
    } catch (OperationNotSupported e) {
      throw new OperationFailed(e.getMessage(), e);
    } catch (AccessDenied e2) {
      throw new OperationFailed(e2.getMessage(), e2);
    }

    if (desiredLocales == null) {
      desiredLocales = new ArrayList<String>();
      desiredLocales.add("en");
    }

    Map<String, PortletData> portletMetaDatas = proxy.getAllPortletMetaData();

    Set<String> keys = portletMetaDatas.keySet();
//    Set<String> iterableKeys = new HashSet<String>(keys);
    if (conf.getExcludeList() != null) {
      //remove exclude portlets from portletMetaDatas
      for (Iterator<String> excludeIter = conf.getExcludeList().iterator(); excludeIter.hasNext();) {
        String excludeHandle = (String) excludeIter.next();
        if (excludeHandle.endsWith("*")) {
          for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
            String iterKey = (String) iterator.next();
            if (iterKey.startsWith(excludeHandle.substring(0, excludeHandle.length() - 1))) {
              iterator.remove();
            }
          }
        } else {
          if (keys.contains(excludeHandle)) {
            keys.remove(excludeHandle);
          }
        }
      }
    }

    if (portletHandles != null && !portletHandles.isEmpty()) {
      Iterator<String> iter = keys.iterator();
      while (iter.hasNext()) {
        String keysHandle = (String) iter.next();
        if (!portletHandles.contains(keysHandle)) {
          iter.remove();
        }
      }
    }

    // manage user
    if (userContext != null) {
      String owner = userContext.getUserContextKey();
      log.debug("Owner Context : " + owner);
    }

    List<PortletDescription> portletDescriptions = new ArrayList<PortletDescription>(keys.size());
    int i = 0;
    for (Iterator<String> iter = keys.iterator(); iter.hasNext(); i++) {
      String producerOfferedPortletHandle = (String) iter.next();
      log.debug("fill service description with portlet description: "
          + producerOfferedPortletHandle);
      PortletDescription pD = proxy.getPortletDescription(producerOfferedPortletHandle,
                                                          desiredLocales.toArray(new String[] {}));
      portletDescriptions.add(pD);
    }
    ServiceDescription sD = new ServiceDescription();
    sD.setRequiresRegistration(conf.isRegistrationRequired());
    sD.setRegistrationPropertyDescription(new ModelDescription());// extension of the WSRP specs
    sD.setRequiresInitCookie(CookieProtocol.fromValue(conf.getCookieProtocol()));
    sD.getCustomModeDescriptions()
      .addAll(getCustomModeDescriptions(pcConf.getSupportedPortletModesWithDescriptions()));
    //sD.setCustomUserProfileItemDescriptions(new ItemDescription[0]);
    sD.getCustomWindowStateDescriptions()
      .addAll(getCustomWindowStateDescriptions(pcConf.getSupportedWindowStatesWithDescriptions()));
    sD.getLocales().addAll(LOCALES);
    sD.getOfferedPortlets().addAll(portletDescriptions);
    sD.setResourceList(new ResourceList());

    // WSRP v2 spec
//    sD.getExtensionDescriptions().addAll(c);
    sD.getEventDescriptions().addAll(getEventDescriptions());
    sD.getSupportedOptions().addAll(SUPPORTED_OPTIONS);
    sD.setExportDescription(null);
    sD.setMayReturnRegistrationState(null);
    sD.setSchemaType(null);
    return sD;
  }

  private List<ItemDescription> getCustomWindowStateDescriptions(Collection<CustomWindowStateWithDescription> collection) {
    List<ItemDescription> result = new ArrayList<ItemDescription>();
    for (Iterator<CustomWindowStateWithDescription> iter = collection.iterator(); iter.hasNext();) {
      CustomWindowStateWithDescription element = (CustomWindowStateWithDescription) iter.next();
      List<LocalisedDescription> l = element.getDescriptions();
      ItemDescription iD = null;
      for (Iterator<LocalisedDescription> iterator = l.iterator(); iterator.hasNext();) {
        LocalisedDescription d = (LocalisedDescription) iterator.next();
        iD = new ItemDescription();
        iD.setItemName(element.getWindowState().toString());
        iD.setDescription(Utils.getLocalizedString(d.getDescription(), d.getLocale().getLanguage()));
        result.add(iD);
      }
    }
    return result;
  }

  private List<ItemDescription> getCustomModeDescriptions(Collection<CustomModeWithDescription> collection) {
    List<ItemDescription> result = new ArrayList<ItemDescription>();
    for (Iterator<CustomModeWithDescription> iter = collection.iterator(); iter.hasNext();) {
      CustomModeWithDescription element = (CustomModeWithDescription) iter.next();
      List<LocalisedDescription> l = element.getDescriptions();
      ItemDescription iD = null;
      for (Iterator<LocalisedDescription> iterator = l.iterator(); iterator.hasNext();) {
        LocalisedDescription d = (LocalisedDescription) iterator.next();
        iD = new ItemDescription();
        iD.setItemName(element.getPortletMode().toString());
        iD.setDescription(Utils.getLocalizedString(d.getDescription(), d.getLocale().getLanguage()));
        result.add(iD);
      }
    }
    return result;
  }

}
