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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import javax.portlet.WindowState;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerConstants;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.model.Description;
import org.exoplatform.services.portletcontainer.pci.model.DisplayName;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp.type.LocalizedString;
import org.exoplatform.services.wsrp.type.MarkupType;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.Property;
import org.exoplatform.services.wsrp.type.PropertyList;
import org.exoplatform.services.wsrp.type.ResourceList;
import org.exoplatform.services.wsrp.utils.Utils;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class JSR168ContainerProxyImpl implements PortletContainerProxy {

  private Log                             log;

  private PortletContainerService         pcService;

  private WSRPConfiguration               conf;

  private WSRPPortletPreferencesPersister persister;

  public JSR168ContainerProxyImpl(PortletContainerService service,
                                  WSRPConfiguration conf) {
    this.pcService = service;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp1");
    this.conf = conf;
    this.persister = WSRPPortletPreferencesPersister.getInstance();
  }

  public boolean isPortletOffered(String portletHandle) {
    String[] key = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    if (this.pcService.getAllPortletMetaData().get(key[0] + Constants.PORTLET_HANDLE_ENCODER + key[1]) != null) {
      return true;
    }
    return false;
  }

  public ResourceList getResourceList(String[] desiredLocales) {
    // TODO discover what a resource is
    return new ResourceList();
  }

  // Store portlet data within PortletDescription
  public PortletDescription getPortletDescription(String portletHandle,
                                                  String[] desiredLocales) {
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_META_DATA_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    if (log.isDebugEnabled()) {
      log.debug("get description of portlet in application : " + portletApplicationName);
      log.debug("get description of portlet : " + portletName);
    }
    Map<String, PortletData> portletMetaDatas = this.pcService.getAllPortletMetaData();
    PortletData portlet = (PortletData) portletMetaDatas.get(k[0] + Constants.PORTLET_META_DATA_ENCODER + k[1]);
    PortletDescription pD = new PortletDescription();

    // delegation to JSR 168 specs
    pD.setPortletHandle(portletHandle);
    pD.setOnlySecure(new Boolean(portlet.isSecure()));
    pD.setDefaultMarkupSecure(new Boolean(portlet.isSecure()));
    List<Description> portletDescriptions = portlet.getDescription();
    if (!(portletDescriptions == null || portletDescriptions.size() == 0)) {
      pD.setDescription(getDescription(portletDescriptions, desiredLocales));
    }
    List<DisplayName> portletDisplayNames = portlet.getDisplayName();
    if (!(portletDisplayNames == null || portletDisplayNames.size() == 0)) {
      pD.setDisplayName(getDisplayName(portletDisplayNames, desiredLocales));
    }
    pD.setGroupID(portletApplicationName);
    pD.setKeywords(getKeyWords(portletApplicationName, portletName, desiredLocales));
    pD.setMarkupTypes(setMarkupTypes(portlet.getSupports(),
    // this.service.getWindowStates(portletApplicationName, portletName , ),
                                     desiredLocales));

    pD.setShortTitle(getShortTitle(portletApplicationName, portletName, desiredLocales));
    pD.setTitle(getTitle(portletApplicationName, portletName, desiredLocales));
    pD.setUserProfileItems(getUserProfileItems(portlet.getUserAttributes()));

    // WSRP specific issues
    pD.setHasUserSpecificState(new Boolean(conf.isHasUserSpecificState()));
    pD.setDoesUrlTemplateProcessing(new Boolean(conf.isDoesUrlTemplateProcessing()));
    pD.setTemplatesStoredInSession(new Boolean(conf.isTemplatesStoredInSession()));
    pD.setUserContextStoredInSession(new Boolean(conf.isUserContextStoredInSession()));
    pD.setUsesMethodGet(new Boolean(conf.isUsesMethodGet()));
    // pD.setUserCategories(null);

    return pD;
  }

  public void setPortletProperties(String portletHandle,
                                   String owner,
                                   PropertyList propertyList) throws WSRPException {
    // key[0] = application name , key[1] portlet name
    log.debug("portlet handle to split in setPortletProperties : " + portletHandle);
    String[] key = StringUtils.split(portletHandle, Constants.PORTLET_META_DATA_ENCODER);
    // mapping WSRP / JSR 168 : a property is a preference type
    Property[] properties = propertyList.getProperties();
    Map<String, String> propertiesMap = new HashMap<String, String>();
    for (int i = 0; i < properties.length; i++) {
      Property property = properties[i];
      // Locale locale = new Locale(property.getLang());//No mapping available
      // in JSR 168
      String preferenceName = property.getName();
      String preferenceValue = property.getStringValue();
      propertiesMap.put(preferenceName, preferenceValue);
    }
    Input input = new Input();
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(owner);
    windowID.setPortletApplicationName(key[0]);
    windowID.setPortletName(key[1]);
    windowID.setUniqueID(key[2]);
    input.setInternalWindowID(windowID);
    input.setPortletPreferencesPersister(persister);
    try {
      this.pcService.setPortletPreference(input, propertiesMap);
    } catch (Exception e) {
      log.error("error while storing preferences", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT);
    }

  }

  public Map<String, String[]> getPortletProperties(String portletHandle,
                                                    String owner) throws WSRPException {
    // key[0] = application name , key[1] portlet name
    String[] key = StringUtils.split(portletHandle, Constants.PORTLET_META_DATA_ENCODER);
    try {
      Input input = new Input();
      ExoWindowID windowID = new ExoWindowID();
      windowID.setOwner(owner);
      windowID.setPortletApplicationName(key[0]);
      windowID.setPortletName(key[1]);
      windowID.setUniqueID(key[2]);
      input.setInternalWindowID(windowID);
      input.setPortletPreferencesPersister(persister);
      return this.pcService.getPortletPreference(input);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT);
    }
  }

  public Map<String, PortletData> getAllPortletMetaData() {
    return this.pcService.getAllPortletMetaData();
  }

  // public Collection getWindowStates(String s) {
  // return this.service.getWindowStates(s);
  // }

  public Collection<WindowState> getSupportedWindowStates() {
    return this.pcService.getSupportedWindowStates();
  }

  public RenderOutput render(WSRPHttpServletRequest request,
                             WSRPHttpServletResponse response,
                             RenderInput input) throws WSRPException {
    try {
      return this.pcService.render(request, response, input);
    } catch (PortletContainerException e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  public ActionOutput processAction(WSRPHttpServletRequest request,
                                    WSRPHttpServletResponse response,
                                    ActionInput input) throws WSRPException {
    try {
      ActionOutput out = this.pcService.processAction(request, response, input);
      Map<String, Object> propertiesMap = out.getProperties();
      Set<String> set = propertiesMap.keySet();
      for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
        String key = (String) iterator.next();
        if (key.startsWith(PortletContainerConstants.EXCEPTION)) {
          log.error("Error body : " + propertiesMap.get(key));
          throw new WSRPException(Faults.PORTLET_STATE_CHANGE_REQUIRED_FAULT);
        }
      }
      return out;
    } catch (PortletContainerException e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  // public Collection getSupportedPortletModesWithDescriptions() {
  // return this.service.getSupportedPortletModesWithDescriptions();
  // }
  //
  // public Collection getSupportedWindowStatesWithDescriptions() {
  // return this.service.getSupportedWindowStatesWithDescriptions();
  // }

  private LocalizedString getDescription(List list,
                                         String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      for (Iterator iter = list.iterator(); iter.hasNext();) {
        Description desc = (Description) iter.next();
        if (desc!=null && desc.getLang()!=null && desc.getLang().equalsIgnoreCase(desiredLocale)) {
          return Utils.getLocalizedString(desc.getDescription(), desiredLocale);
        }
      }
    }
    return null;
  }

  private LocalizedString getDisplayName(List list,
                                         String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      for (Iterator iter = list.iterator(); iter.hasNext();) {
        DisplayName displayName = (DisplayName) iter.next();
        if (displayName!=null && displayName.getLang()!=null && displayName.getLang().equalsIgnoreCase(desiredLocale)) {
          return Utils.getLocalizedString(displayName.getDisplayName(), desiredLocale);
        }
      }
    }
    return null;
  }

  private LocalizedString[] getKeyWords(String portletAppName,
                                        String portletName,
                                        String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      java.util.ResourceBundle resourceBundle = getBundle(portletAppName, portletName, new Locale(desiredLocale));
      System.out.println(" ---- " + portletAppName + "/" + portletName + "/" + desiredLocale);
      System.out.println(" ---- 1:" + resourceBundle);
      System.out.println(" ---- 2:" + resourceBundle.getLocale());
      System.out.println(" ---- 3:" + resourceBundle.getLocale().getLanguage());
      if (resourceBundle.getLocale().getLanguage().equalsIgnoreCase(desiredLocale) || i == desiredLocales.length - 1) {
        try {
          String keyWords = resourceBundle.getString(PortletData.KEYWORDS);
          String[] a = StringUtils.split(keyWords, ",");
          LocalizedString[] b = new LocalizedString[a.length];
          for (int j = 0; j < a.length; j++) {
            b[j] = Utils.getLocalizedString(a[j], desiredLocale);
          }
          return b;
        } catch (MissingResourceException ex) {
          log.debug("No keyword defined for the portlet " + portletAppName + "/" + portletName);
          return null;
        }
      }
    }
    return null;
  }

  private MarkupType[] setMarkupTypes(List supports,
                                      // Collection windowStates,
                                      String[] locales) {
    MarkupType[] array = new MarkupType[supports.size()];
    int i = 0;
    MarkupType mT = null;
    for (Iterator iter = supports.iterator(); iter.hasNext(); i++) {
      Supports support = (Supports) iter.next();
      mT = new MarkupType();
      mT.setMimeType(support.getMimeType());
      List portletModes = support.getPortletMode();
      String[] modesInArray = new String[portletModes.size()];
      int j = 0;
      for (Iterator iterator = portletModes.iterator(); iterator.hasNext(); j++) {
        String pM = (String) iterator.next();
        modesInArray[j] = WSRPConstants.WSRP_PREFIX + pM.toString();
      }
      mT.setModes(modesInArray);
      j = 0;
      List windowStates = support.getWindowState();
      String[] windowStatesInArray = new String[windowStates.size()];
      for (Iterator iterator = windowStates.iterator(); iterator.hasNext(); j++) {
        String wS = (String) iterator.next();
        windowStatesInArray[j] = WSRPConstants.WSRP_PREFIX + wS.toString();
      }
      mT.setWindowStates(windowStatesInArray);
      mT.setLocales(locales);
      array[i] = mT;
    }
    return array;
  }

  private LocalizedString getTitle(String portletAppName,
                                   String portletName,
                                   String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      java.util.ResourceBundle resourceBundle = getBundle(portletAppName, portletName, new Locale(desiredLocale));
      if (resourceBundle.getLocale().getLanguage().equalsIgnoreCase(desiredLocale) || i == desiredLocales.length - 1) {
        return Utils.getLocalizedString(resourceBundle.getString(PortletData.PORTLET_TITLE), desiredLocale);
      }
    }
    return null;
  }

  private LocalizedString getShortTitle(String portletAppName,
                                        String portletName,
                                        String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      java.util.ResourceBundle resourceBundle = getBundle(portletAppName, portletName, new Locale(desiredLocale));
      if (resourceBundle.getLocale().getLanguage().equalsIgnoreCase(desiredLocale) || i == desiredLocales.length - 1) {
        try {
          return Utils.getLocalizedString(resourceBundle.getString(PortletData.PORTLET_SHORT_TITLE), desiredLocale);
        } catch (MissingResourceException ex) {
          log.debug("No short title defined for the portlet " + portletAppName + "/" + portletName);
          return null;
        }
      }
    }
    return null;
  }

  private String[] getUserProfileItems(List userAttributes) {
    String[] toReturnArray = new String[userAttributes.size()];
    int i = 0;
    for (Iterator iter = userAttributes.iterator(); iter.hasNext(); i++) {
      UserAttribute userAttr = (UserAttribute) iter.next();
      toReturnArray[i] = userAttr.getName();
    }
    return toReturnArray;
  }

  private java.util.ResourceBundle getBundle(String portletAppName,
                                             String portletName,
                                             Locale locale) {
    try {
      WSRPHttpServletRequest request = new WSRPHttpServletRequest((HttpSession) null);
      WSRPHttpServletResponse response = new WSRPHttpServletResponse();
      return this.pcService.getBundle(request, response, portletAppName, portletName, locale);
    } catch (PortletContainerException e) {
      e.printStackTrace();
    }
    return null;
  }
}
