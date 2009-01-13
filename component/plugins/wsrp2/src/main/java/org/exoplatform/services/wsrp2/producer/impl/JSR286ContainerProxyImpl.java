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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import javax.portlet.WindowState;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.pci.model.Description;
import org.exoplatform.services.portletcontainer.pci.model.DisplayName;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.MarkupType;
import org.exoplatform.services.wsrp2.type.ParameterDescription;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.Property;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.ResourceList;
import org.exoplatform.services.wsrp2.utils.Modes;
import org.exoplatform.services.wsrp2.utils.Utils;
import org.exoplatform.services.wsrp2.utils.WindowStates;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class JSR286ContainerProxyImpl implements PortletContainerProxy {

  private static final Log                LOG = ExoLogger.getLogger(JSR286ContainerProxyImpl.class);

  private PortletContainerService         pcService;

  private WSRPConfiguration               conf;

  private WSRPPortletPreferencesPersister persister;

  public JSR286ContainerProxyImpl(PortletContainerService service, WSRPConfiguration conf) {
    this.pcService = service;
    this.conf = conf;
    this.persister = WSRPPortletPreferencesPersister.getInstance();
  }

  public boolean isPortletOffered(String portletHandle) {
    String[] key = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String pcPortletHandle = key[0] + Constants.PORTLET_HANDLE_ENCODER + key[1];
    if (pcPortletHandle != null) {
      if (this.pcService.getAllPortletMetaData().get(pcPortletHandle) != null) {
        return true;
      }
    }
    return false;
  }

  public ResourceList getResourceList(String[] desiredLocales) {
    // TODO discover what a resource is
    return new ResourceList();
  }

  // Store portlet data within PortletDescription
  public PortletDescription getPortletDescription(String portletHandle, String[] desiredLocales) {
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_META_DATA_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    if (LOG.isDebugEnabled()) {
      LOG.debug("get description of portlet in application: " + portletApplicationName);
      LOG.debug("get description of portlet: " + portletName);
    }
    Map<String, PortletData> portletMetaDatas = pcService.getAllPortletMetaData();
    PortletData portlet = (PortletData) portletMetaDatas.get(portletApplicationName
        + Constants.PORTLET_META_DATA_ENCODER + portletName);
    PortletDescription pD = new PortletDescription();

    // delegation to JSR 168 specs
    pD.setPortletHandle(portletHandle);
    pD.setPortletID(null);// TODO // is invariant across deployments of
    // compatible versions of the Portlet
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
    pD.getKeywords().addAll(getKeyWords(portletApplicationName, portletName, desiredLocales));
    pD.getMarkupTypes().addAll(setMarkupTypes(portlet.getSupports(), desiredLocales));

    pD.setTitle(getTitle(portletApplicationName, portletName, desiredLocales));
    pD.setShortTitle(getShortTitle(portletApplicationName, portletName, desiredLocales));

    pD.getUserProfileItems().addAll(getUserProfileItems(portlet.getUserAttributes()));
    pD.getUserCategories().clear();

    pD.getPortletManagedModes()
    .addAll(Arrays.asList(pcService.getPortalManagedPortletModes(portletApplicationName,
                                                                 portletName)));

    // WSRP from config
    pD.setHasUserSpecificState(new Boolean(conf.isHasUserSpecificState()));
    pD.setDoesUrlTemplateProcessing(new Boolean(conf.isDoesUrlTemplateProcessing()));
    pD.setTemplatesStoredInSession(new Boolean(conf.isTemplatesStoredInSession()));
    pD.setUserContextStoredInSession(new Boolean(conf.isUserContextStoredInSession()));
    pD.setUsesMethodGet(new Boolean(conf.isUsesMethodGet()));

    // WSRP v2
    pD.getPublishedEvents().addAll(portlet.getSupportedPublishingEvent());
    pD.getHandledEvents().addAll(portlet.getSupportedProcessingEvent());
    pD.getNavigationalPublicValueDescriptions()
      .addAll(getNavigationalPublicValueDescriptions(portlet.getSupportedPublicRenderParameter()));
    pD.setMayReturnPortletState(false);
    pD.getExtensions().clear();

    return pD;
  }

  private List<ParameterDescription> getNavigationalPublicValueDescriptions(List<String> supportedPublicRenderParameter) {
    if (supportedPublicRenderParameter == null)
      return null;
    List<ParameterDescription> parameterDescriptions = new ArrayList<ParameterDescription>(supportedPublicRenderParameter.size());
    int i = 0;
    for (String parameterString : (List<String>) supportedPublicRenderParameter) {
      ParameterDescription parameterDescription = new ParameterDescription();
      parameterDescription.getNames().add(new QName(parameterString));
      // parameterDescription.setDescription(description);
      // parameterDescription.setLabel(label);
      // parameterDescription.setHint(hint);
      // parameterDescription.setIdentifier(identifier);
      parameterDescription.getExtensions().add(null);
      parameterDescriptions.add(parameterDescription);
    }
    return parameterDescriptions;
  }

  public void setPortletProperties(String portletHandle, String owner, PropertyList propertyList) throws WSRPException {
    // key[0] = application name , key[1] portlet name
    LOG.debug("portlet handle to split in setPortletProperties : " + portletHandle);
    String[] key = StringUtils.split(portletHandle, Constants.PORTLET_META_DATA_ENCODER);
    // mapping WSRP / JSR 168 : a property is a preference type
    List<Property> properties = propertyList.getProperties();
    Map<String, String> propertiesMap = new HashMap<String, String>();
    for (Property property : properties) {
      // Locale locale = new Locale(property.getLang());//No mapping available
      // in JSR 168
      String preferenceName = property.getName().toString();
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
      pcService.setPortletPreference(input, propertiesMap);
    } catch (Exception e) {
      LOG.error("error while storing preferences", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT);
    }
  }

  public Map<String, String[]> getPortletProperties(String portletHandle, String owner) throws WSRPException {
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
      return pcService.getPortletPreference(input);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT);
    }
  }

  public Map<String, PortletData> getAllPortletMetaData() {
    return pcService.getAllPortletMetaData();// for exclude register remote to remote use parameter 'true'
  }
  
  // public Collection getWindowStates(String s) {
  // return service.getWindowStates(s);
  // }

  public Collection<WindowState> getSupportedWindowStates() {
    return pcService.getSupportedWindowStates();
  }

  public RenderOutput render(WSRPHttpServletRequest request,
                             WSRPHttpServletResponse response,
                             RenderInput input) throws WSRPException {
    try {
      RenderOutput renderOut = pcService.render(request, response, input);
      return renderOut;
    } catch (PortletContainerException e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  public ResourceOutput serveResource(WSRPHttpServletRequest request,
                                      WSRPHttpServletResponse response,
                                      ResourceInput input) throws WSRPException {
    try {
      ResourceOutput resourceOut = pcService.serveResource(request, response, input);
      return resourceOut;
    } catch (PortletContainerException e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  public EventOutput processEvent(WSRPHttpServletRequest request,
                                  WSRPHttpServletResponse response,
                                  EventInput input) throws WSRPException {
    try {
      EventOutput eventOut = pcService.processEvent(request, response, input);
      return eventOut;
    } catch (PortletContainerException e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  public ActionOutput processAction(WSRPHttpServletRequest request,
                                    WSRPHttpServletResponse response,
                                    ActionInput input) throws WSRPException {
    try {
      ActionOutput actionOut = pcService.processAction(request, response, input);
      Map<String, Object> propertiesMap = actionOut.getProperties();
      Set<String> set = propertiesMap.keySet();
      for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
        String key = (String) iterator.next();
        if (key.startsWith(PCConstants.EXCEPTION)) {
          LOG.error("Error body : " + propertiesMap.get(key));
          throw new WSRPException(Faults.PORTLET_STATE_CHANGE_REQUIRED_FAULT);
        }
      }
      return actionOut;
    } catch (PortletContainerException e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  private LocalizedString getDescription(List<Description> list, String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      for (Iterator<Description> iter = list.iterator(); iter.hasNext();) {
        Description desc = (Description) iter.next();
        if (desc != null && desc.getLang() != null
            && desc.getLang().equalsIgnoreCase(desiredLocale)) {
          return Utils.getLocalizedString(desc.getDescription(), desiredLocale);
        }
      }
    }
    return null;
  }

  private LocalizedString getDisplayName(List<DisplayName> list, String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      for (Iterator<DisplayName> iter = list.iterator(); iter.hasNext();) {
        DisplayName displayName = (DisplayName) iter.next();
        if (displayName != null && displayName.getLang() != null
            && displayName.getLang().equalsIgnoreCase(desiredLocale)) {
          return Utils.getLocalizedString(displayName.getDisplayName(), desiredLocale);
        }
      }
    }
    return null;
  }

  private List<LocalizedString> getKeyWords(String portletAppName,
                                            String portletName,
                                            String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      java.util.ResourceBundle resourceBundle = getBundle(portletAppName,
                                                          portletName,
                                                          new Locale(desiredLocale));
      System.out.println(" ---- " + portletAppName + "/" + portletName + "/" + desiredLocale);
      System.out.println(" ---- 1:" + resourceBundle);
      System.out.println(" ---- 2:" + resourceBundle.getLocale());
      if (resourceBundle.getLocale().getLanguage().equalsIgnoreCase(desiredLocale)
          || i == desiredLocales.length - 1) {
        try {
          String keyWords = resourceBundle.getString(PortletData.KEYWORDS);
          String[] a = StringUtils.split(keyWords, ",");
          List<LocalizedString> b = new ArrayList<LocalizedString>(a.length);
          for (int j = 0; j < a.length; j++) {
            b.add(Utils.getLocalizedString(a[j], desiredLocale));
          }
          return b;
        } catch (MissingResourceException ex) {
//          LOG.error("No keyword defined for the portlet " + portletAppName + "/" + portletName, ex);
          return new ArrayList<LocalizedString>();
        }
      }
    }
    return new ArrayList<LocalizedString>();
  }

  private List<MarkupType> setMarkupTypes(List<Supports> list, String[] locales) {
    List<MarkupType> result = new ArrayList<MarkupType>(list.size());
    int i = 0;
    MarkupType mT = null;
    for (Iterator<Supports> iter = list.iterator(); iter.hasNext(); i++) {
      Supports support = (Supports) iter.next();
      mT = new MarkupType();
      mT.setMimeType(support.getMimeType());
      List<String> portletModes = support.getPortletMode();
      String[] modesInArray = new String[portletModes.size()];
      int j = 0;
      for (Iterator<String> iterator = portletModes.iterator(); iterator.hasNext(); j++) {
        String pM = (String) iterator.next();
        modesInArray[j] = Modes.addPrefixWSRP(pM);
      }
      mT.getModes().addAll(Arrays.asList(modesInArray));
      j = 0;
      List<String> windowStates = support.getWindowState();
      String[] windowStatesInArray = new String[windowStates.size()];
      for (Iterator<String> iterator = windowStates.iterator(); iterator.hasNext(); j++) {
        String wS = (String) iterator.next();
        windowStatesInArray[j] = WindowStates.addPrefixWSRP(wS);
      }
      mT.getWindowStates().addAll(Arrays.asList(windowStatesInArray));
      mT.getLocales().addAll(Arrays.asList(locales));
      result.add(mT);
    }
    return result;
  }

  private LocalizedString getTitle(String portletAppName,
                                   String portletName,
                                   String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      java.util.ResourceBundle resourceBundle = getBundle(portletAppName,
                                                          portletName,
                                                          new Locale(desiredLocale));
      if (resourceBundle.getLocale().getLanguage().equalsIgnoreCase(desiredLocale)
          || i == desiredLocales.length - 1) {
        return Utils.getLocalizedString(resourceBundle.getString(PortletData.PORTLET_TITLE),
                                        desiredLocale);
      }
    }
    return null;
  }

  private LocalizedString getShortTitle(String portletAppName,
                                        String portletName,
                                        String[] desiredLocales) {
    for (int i = 0; i < desiredLocales.length; i++) {
      String desiredLocale = desiredLocales[i];
      java.util.ResourceBundle resourceBundle = getBundle(portletAppName,
                                                          portletName,
                                                          new Locale(desiredLocale));
      if (resourceBundle.getLocale().getLanguage().equalsIgnoreCase(desiredLocale)
          || i == desiredLocales.length - 1) {
        try {
          return Utils.getLocalizedString(resourceBundle.getString(PortletData.PORTLET_SHORT_TITLE),
                                          desiredLocale);
        } catch (MissingResourceException ex) {
          LOG.debug("No short title defined for the portlet " + portletAppName + "/" + portletName);
          return null;
        }
      }
    }
    return null;
  }

  private List<String> getUserProfileItems(List<UserAttribute> userAttributes) {
    List<String> toReturnList = new ArrayList<String>(userAttributes.size());
    int i = 0;
    for (Iterator<UserAttribute> iter = userAttributes.iterator(); iter.hasNext(); i++) {
      UserAttribute userAttr = (UserAttribute) iter.next();
      toReturnList.add(userAttr.getName());
    }
    return toReturnList;
  }

  private java.util.ResourceBundle getBundle(String portletAppName,
                                             String portletName,
                                             Locale locale) {
    try {
      WSRPHttpServletRequest request = WSRPHTTPContainer.getInstance().getRequest();
      WSRPHttpServletResponse response = WSRPHTTPContainer.getInstance().getResponse();
      return pcService.getBundle(request, response, portletAppName, portletName, locale);
    } catch (PortletContainerException e) {
      LOG.error("Error in the method JSR286ContainerProxyImpl.getBundle(): " + e.getMessage(), e);
      return null;
    }
  }
}
