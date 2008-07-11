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

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.services.wsrp.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp.producer.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp.type.*;
import org.exoplatform.services.wsrp.utils.Utils;

import java.rmi.RemoteException;
import java.util.*;

/**
 *  Author : Tuan Nguyen
 *           tuan08@users.sourceforge.net
 *  Author : Mestrallet Benjamin
 *           benjmestrallet@users.sourceforge.net
 *  Date: 10 Dec. 2003
 *  Time: 09:40:23
 * */
public class ServiceDescriptionInterfaceImpl implements ServiceDescriptionInterface {

  private PortletContainerProxy cont;
  public static String[]        localesArray = { "en", "fr" };
  private WSRPConfiguration     conf;
  private Log                   log;
  private ExoContainer          container;
  private PortletContainerConf  config;

  public ServiceDescriptionInterfaceImpl(PortletContainerProxy cont,
                                         WSRPConfiguration conf,
                                         ExoContainerContext context) {
    this.cont = cont;
    this.conf = conf;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp");
    this.container = context.getContainer();
    this.config = (PortletContainerConf) container.getComponentInstanceOfType(PortletContainerConf.class);
  }

  public ServiceDescription getServiceDescription(RegistrationContext registrationContext,
                                                  String[] desiredLocales) throws RemoteException {

    //si les locales ne sont pas d�finies, alors on les cr�er par d�faut.
    if (desiredLocales == null) {
      desiredLocales = new String[] { "en", "fr" };
    }

    log.debug("getServiceDescription entered with registrationContext : " + registrationContext);
    Map<String, PortletData> portletMetaDatas = cont.getAllPortletMetaData();
    Set<String> keys = portletMetaDatas.keySet();
    Set<String> iterableKeys = new HashSet<String>(keys);
    if (conf.getExcludeList() != null) {
      //remove exclude portlets from portletMetaDatas
      for (Iterator<String> excludeIter = conf.getExcludeList().iterator(); excludeIter.hasNext();) {
        String excludeHandle = (String) excludeIter.next();
        if (excludeHandle.endsWith("*")) {
          for (String iterKey : iterableKeys) {
            if (iterKey.startsWith(excludeHandle.substring(0, excludeHandle.length() - 1))) {
              keys.remove(iterKey);
            }
          }
        } else {
          if (keys.contains(excludeHandle))
            keys.remove(excludeHandle);
        }
      }
    }
    PortletDescription[] pdescription = new PortletDescription[keys.size()];
    int i = 0;
    for (Iterator<String> iter = keys.iterator(); iter.hasNext(); i++) {
      String producerOfferedPortletHandle = (String) iter.next();
      log.debug("fill service description with portlet description ");
      pdescription[i] = cont.getPortletDescription(producerOfferedPortletHandle, desiredLocales);
    }
    ServiceDescription sD = new ServiceDescription();
    sD.setRequiresRegistration(conf.isRegistrationRequired());
    sD.setRegistrationPropertyDescription(new ModelDescription());//extension of the WSRP specs
    sD.setRequiresInitCookie(CookieProtocol.none);
    sD.setCustomModeDescriptions(getCustomModeDescriptions(config.getSupportedPortletModesWithDescriptions()));
    sD.setCustomUserProfileItemDescriptions(new ItemDescription[0]);
    sD.setCustomWindowStateDescriptions(getCustomWindowStateDescriptions(config.getSupportedWindowStatesWithDescriptions()));
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
