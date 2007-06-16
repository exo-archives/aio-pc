/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 ***/
package org.exoplatform.services.wsrp.producer.impl;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.services.wsrp.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp.producer.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp.type.*;
import org.exoplatform.services.wsrp.utils.Utils;

import java.rmi.RemoteException;
import java.util.*;

/**
 *  Created by the Exo Development team.
 *  Author : Tuan Nguyen
 *           tuan08@users.sourceforge.net
 *  Author : Mestrallet Benjamin
 *           benjmestrallet@users.sourceforge.net
 *  Date: 10 Dec. 2003
 *  Time: 09:40:23
 * */
public class ServiceDescriptionInterfaceImpl implements ServiceDescriptionInterface {
  private PortletContainerProxy container;
  public static String[] localesArray = {"en", "fr"};
  private WSRPConfiguration conf;
  private Log log;

  public ServiceDescriptionInterfaceImpl(PortletContainerProxy container,
                                         WSRPConfiguration conf) {
    this.container = container;
    this.conf = conf;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp");
  }

  public ServiceDescription getServiceDescription(RegistrationContext registrationContext,
                                                  String[] desiredLocales) throws RemoteException {
    
	//si les locales ne sont pas définies, alors on les créer par défaut.
	if(desiredLocales==null){
		desiredLocales=new String[]{"en","fr"};
	}
	  
	log.debug("getServiceDescription entered with registrationContext : " + registrationContext);
    Map portletMetaDatas = container.getAllPortletMetaData();
    Set keys = portletMetaDatas.keySet();
    if (conf.getExcludeList() != null)
      for (Iterator iter = conf.getExcludeList().iterator(); iter.hasNext();) {
        String handle = (String) iter.next();
        if (keys.contains(handle))
          keys.remove(handle);
      }
    PortletDescription[] pdescription = new PortletDescription[keys.size()];
    int i = 0;
    for (Iterator iter = keys.iterator(); iter.hasNext(); i++) {
      String producerOfferedPortletHandle = (String) iter.next();
      log.debug("fill service description with portlet description ");
      pdescription[i] = container.getPortletDesciption(producerOfferedPortletHandle,
                                                       desiredLocales);
    }
    ServiceDescription sD = new ServiceDescription() ;
    sD.setRequiresRegistration(conf.isRegistrationRequired());
    sD.setRegistrationPropertyDescription(new ModelDescription());//extension of the WSRP specs
    sD.setRequiresInitCookie(CookieProtocol.none);
    sD.setCustomModeDescriptions(getCustomModeDescriptions(container.getSupportedPortletModesWithDescriptions()));
    sD.setCustomUserProfileItemDescriptions(new ItemDescription[0]);
    sD.setCustomWindowStateDescriptions(getCustomWindowStateDescriptions(container.getSupportedWindowStatesWithDescriptions()));
    sD.setLocales(localesArray);
    sD.setOfferedPortlets(pdescription);
    sD.setResourceList(new ResourceList());
    return sD ;
  }

  private ItemDescription[] getCustomWindowStateDescriptions(Collection collection) {
    Collection c = new ArrayList();
    for (Iterator iter = collection.iterator(); iter.hasNext();) {
      CustomWindowStateWithDescription element = (CustomWindowStateWithDescription) iter.next();
      List l = element.getDescriptions();
      ItemDescription iD = null;
      for (Iterator iterator = l.iterator(); iterator.hasNext();) {
        LocalisedDescription d = (LocalisedDescription) iterator.next();
        iD = new ItemDescription();
        iD.setItemName(element.getWindowState().toString());
        iD.setDescription(Utils.getLocalizedString(d.getDescription(),
                                                   d.getLocale().getLanguage()));
        c.add(iD);
      }
    }
    ItemDescription[] iDTab = new ItemDescription[c.size()];
    int i = 0;
    for (Iterator iter = c.iterator(); iter.hasNext(); i++) {
      iDTab[i] = (ItemDescription) iter.next();
    }
    return iDTab;
  }

  private ItemDescription[] getCustomModeDescriptions(Collection collection) {
    Collection c = new ArrayList();
    for (Iterator iter = collection.iterator(); iter.hasNext();) {
      CustomModeWithDescription element = (CustomModeWithDescription) iter.next();
      List l = element.getDescriptions();
      ItemDescription iD = null;
      for (Iterator iterator = l.iterator(); iterator.hasNext();) {
        LocalisedDescription d = (LocalisedDescription) iterator.next();
        iD = new ItemDescription();
        iD.setItemName(element.getPortletMode().toString());
        iD.setDescription(Utils.getLocalizedString(d.getDescription(),
                                                   d.getLocale().getLanguage()));
        c.add(iD);
      }
    }
    ItemDescription[] iDTab = new ItemDescription[c.size()];
    int i = 0;
    for (Iterator iter = c.iterator(); iter.hasNext(); i++) {
      iDTab[i] = (ItemDescription) iter.next();
    }
    return iDTab;
  }

}