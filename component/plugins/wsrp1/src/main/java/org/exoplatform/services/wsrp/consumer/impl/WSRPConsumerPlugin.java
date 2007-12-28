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

package org.exoplatform.services.wsrp.consumer.impl;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.MapResourceBundle;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerPlugin;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.DisplayName;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletDataImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.bundle.ResourceBundleManager;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp.consumer.GroupSession;
import org.exoplatform.services.wsrp.consumer.InteractionRequest;
import org.exoplatform.services.wsrp.consumer.PortletDriver;
import org.exoplatform.services.wsrp.consumer.PortletKey;
import org.exoplatform.services.wsrp.consumer.PortletSession;
import org.exoplatform.services.wsrp.consumer.PortletWindowSession;
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp.consumer.URLTemplateComposer;
import org.exoplatform.services.wsrp.consumer.User;
import org.exoplatform.services.wsrp.consumer.UserSessionMgr;
import org.exoplatform.services.wsrp.consumer.WSRPMarkupRequest;
import org.exoplatform.services.wsrp.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp.consumer.adapters.InteractionRequestAdapter;
import org.exoplatform.services.wsrp.consumer.adapters.PortletKeyAdapter;
import org.exoplatform.services.wsrp.consumer.adapters.UserAdapter;
import org.exoplatform.services.wsrp.consumer.adapters.WSRPBaseRequestAdapter;
import org.exoplatform.services.wsrp.consumer.adapters.WSRPMarkupRequestAdapter;
import org.exoplatform.services.wsrp.consumer.adapters.WSRPPortletAdapter;
import org.exoplatform.services.wsrp.consumer.impl.helpers.UserSessionImpl;
import org.exoplatform.services.wsrp.consumer.portlets.WSRPAdminPortletDataImp;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.impl.WSRPConfiguration;
import org.exoplatform.services.wsrp.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp.type.ClientData;
import org.exoplatform.services.wsrp.type.ItemDescription;
import org.exoplatform.services.wsrp.type.MarkupContext;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.MarkupType;
import org.exoplatform.services.wsrp.type.NamedString;
import org.exoplatform.services.wsrp.type.PersonName;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.wsrp.type.SessionContext;
import org.exoplatform.services.wsrp.type.StateChange;
import org.exoplatform.services.wsrp.type.UpdateResponse;
import org.exoplatform.services.wsrp.type.UserContext;
import org.exoplatform.services.wsrp.type.UserProfile;
import org.exoplatform.services.wsrp.utils.Modes;
import org.exoplatform.services.wsrp.utils.Utils;
import org.exoplatform.services.wsrp.utils.WindowStates;

/**
 * Based on WSRPConsumerPortlet written by Benjamin Mestrallet 
 * Author: Roman Pedchenko roman.pedchenko@exoplatform.com.ua
 * Author: Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */

public class WSRPConsumerPlugin implements PortletContainerPlugin {

  private static final String[]     characterEncodings = { "UTF-8" };

  private static final String[]     mimeTypes          = { "text/html", "text/wml" };

  public static final String[]      SUPPORTED_LOCALES  = { "en", "fr" };

  private static final String       consumerAgent      = "exoplatform.3.0";

  private static final String       userAgent          = "userAgent";

  private static final String       basePath           = "/portal/";

  protected WSRPAdminPortletDataImp adminPortlet       = null;

  public static final String        USER_SESSIONS_KEY  = "user_session_key";

  private ExoContainer              container;

  private ConsumerEnvironment       consumer;

  private PortletContainerService   pcService;

  private URLTemplateComposer       templateComposer;

  private PortletContainerConf      pcConf;

  private WSRPConfiguration         conf;

  private Log                       log;

  private String                    pluginName;

  private static boolean            init               = false;

  public WSRPConsumerPlugin(ExoContainerContext context,
                            ConsumerEnvironment consumer,
                            PortletContainerService pcService,
                            URLTemplateComposer templateComposer,
                            PortletContainerConf pcConf,
                            WSRPConfiguration conf) {
    this.container = context.getContainer();
    this.consumer = consumer;
    this.pcService = pcService;
    this.templateComposer = templateComposer;
    this.pcConf = pcConf;
    this.conf = conf;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp1");
    initConsumer();
  }

  public void setName(String name) {
    pluginName = name;
  }

  public String getName() {
    return pluginName;
  }

  public void setDescription(String description) {
    pcConf.setDescription(description);
  }

  public String getDescription() {
    return pcConf.getDescription();
  }

  public void setMajorVersion(int majorVersion) {
    pcConf.setMajorVersion(majorVersion);
  }

  public void setMinorVersion(int minorVersion) {
    pcConf.setMinorVersion(minorVersion);
  }

  public void setProperties(Map properties) {
    pcConf.setProperties(properties);
  }

  protected void initConsumer() {
    if (log.isDebugEnabled())
      log.debug("WSRPConsumerPlugin.initConsumer");
    consumer.setCharacterEncodingSet(characterEncodings);
    consumer.setConsumerAgent(consumerAgent);
    consumer.setMimeTypes(mimeTypes);
    consumer.setPortletStateChange(StateChange.readWrite);
    // TODO modes and states should be getting from producer
    // consumer.setSupportedModes(getPortletModes(Collections.list(pcConf.getSupportedPortletModes())));
    consumer.setSupportedModes(getPortletModes(pcService.getSupportedPortletModes()));
    // consumer.setSupportedWindowStates(getWindowStates(Collections.list(pcConf.getSupportedWindowStates())));
    consumer.setSupportedWindowStates(getWindowStates(pcService.getSupportedWindowStates()));
    consumer.setUserAuthentication(WSRPConstants.NO_USER_AUTHENTIFICATION);
    consumer.setSupportedLocales(SUPPORTED_LOCALES);
    adminPortlet = new WSRPAdminPortletDataImp(this.container, conf.getAdminPortletParams());
    init = true;
  }

  private String[] getPortletModes(Collection<PortletMode> supportedPortletModes) {
    String[] array = new String[supportedPortletModes.size()];
    int i = 0;
    for (Iterator<PortletMode> iterator = supportedPortletModes.iterator(); iterator.hasNext(); i++) {
      PortletMode portletMode = (PortletMode) iterator.next();
      array[i] = portletMode.toString();
      // was: array[i] = WSRPConstants.WSRP_PREFIX + portletMode.toString();
    }
    return array;
  }

  private String[] getWindowStates(Collection<WindowState> supportedWindowStates) {
    String[] array = new String[supportedWindowStates.size()];
    int i = 0;
    for (Iterator<WindowState> iterator = supportedWindowStates.iterator(); iterator.hasNext(); i++) {
      WindowState windowState = (WindowState) iterator.next();
      array[i] = windowState.toString();
      // was: array[i] = WSRPConstants.WSRP_PREFIX + windowState.toString();
    }
    return array;
  }

  public Collection<PortletMode> getSupportedPortletModes() {
    if (!init) {
      return null;
    }
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    ServiceDescription desc = null;
    ArrayList<PortletMode> result = null;
    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      try {
        desc = producer.getServiceDescription();
        result = new ArrayList<PortletMode>();
        org.exoplatform.services.wsrp.type.ItemDescription[] iDArray = desc.getCustomModeDescriptions();
        if (iDArray != null) {
          for (int j = 0; j < java.lang.reflect.Array.getLength(iDArray); j++) {
            ItemDescription iD = iDArray[j];
            String mode = iD.getItemName();
            if (!result.contains(mode))
              result.add(new PortletMode(mode));
          }
        }
      } catch (WSRPException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public Collection<WindowState> getSupportedWindowStates() {
    if (!init) {
      return null;
    }
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    ServiceDescription desc = null;
    ArrayList<WindowState> result = null;
    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      try {
        desc = producer.getServiceDescription();
        result = new ArrayList<WindowState>();
        org.exoplatform.services.wsrp.type.ItemDescription[] iDArray = desc.getCustomWindowStateDescriptions();
        if (iDArray != null) {
          for (int j = 0; j < java.lang.reflect.Array.getLength(iDArray); j++) {
            ItemDescription iD = iDArray[j];
            String state = iD.getItemName();
            if (!result.contains(state))
              result.add(new WindowState(state));
          }
        }
      } catch (WSRPException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  // public Collection getSupportedPortletModesWithDescriptions() {
  // return null;
  // }

  // public Collection getSupportedWindowStatesWithDescriptions() {
  // return null;
  // }

  public Collection<PortletMode> getPortletModes(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    String producerID = portletAppName.substring(0, portletAppName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER));
    String portletHandle = portletAppName.substring(portletAppName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER) + 1)
        + Constants.PORTLET_HANDLE_ENCODER + portletName;

    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP))
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME))
        return adminPortlet.getPortletModes(markup);

    if (!init)
      return null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    ServiceDescription desc = null;
    ArrayList<PortletMode> result = null;
    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      if (producer.getID().equalsIgnoreCase(producerID)) {
        try {
          desc = producer.getServiceDescription();
          result = new ArrayList<PortletMode>();
          PortletDescription[] portletDescriptions = desc.getOfferedPortlets();
          if (portletDescriptions != null) {
            for (int k = 0; k < portletDescriptions.length; k++) {
              PortletDescription portletDescription = portletDescriptions[k];
              String portletHandleTemp = portletDescription.getPortletHandle();
              if (portletHandleTemp.equalsIgnoreCase(portletHandle)) {
                MarkupType[] markupType = portletDescription.getMarkupTypes();
                for (int j = 0; j < markupType.length; j++) {
                  MarkupType type = markupType[j];
                  if (type.getMimeType().equalsIgnoreCase(markup)) {
                    String[] result_in_array = type.getModes();
                    for (int m = 0; m < result_in_array.length; m++) {
                      result.add(new PortletMode(Modes.delAllPrefixWSRP(result_in_array[m])));
                    }
                  }
                }
                return result; // for performance improving
              }
            }
          }
        } catch (WSRPException e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }

  public boolean isModeSuported(String portletAppName,
                                String portletName,
                                String markup,
                                PortletMode mode) {
    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP))
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME))
        return adminPortlet.isModeSuported(markup, mode);
    return false;
  }

  public Collection<WindowState> getWindowStates(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    String producerID = portletAppName.substring(0, portletAppName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER));
    String portletHandle = portletAppName.substring(portletAppName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER) + 1)
        + Constants.PORTLET_HANDLE_ENCODER + portletName;

    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP))
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME))
        return adminPortlet.getWindowStates(markup);

    if (!init)
      return null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    ServiceDescription desc = null;
    ArrayList<WindowState> result = null;
    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      if (producer.getID().equalsIgnoreCase(producerID)) {
        try {
          desc = producer.getServiceDescription();
          result = new ArrayList<WindowState>();
          PortletDescription[] portletDescriptions = desc.getOfferedPortlets();
          if (portletDescriptions != null) {
            for (int k = 0; k < portletDescriptions.length; k++) {
              PortletDescription portletDescription = portletDescriptions[k];
              String portletHandleTemp = portletDescription.getPortletHandle();
              if (portletHandleTemp.equalsIgnoreCase(portletHandle)) {
                MarkupType[] markupType = portletDescription.getMarkupTypes();
                for (int j = 0; j < markupType.length; j++) {
                  MarkupType type = markupType[j];
                  if (type.getMimeType().equalsIgnoreCase(markup)) {
                    String[] result_in_array = type.getWindowStates();
                    for (int m = 0; m < result_in_array.length; m++) {
                      result.add(new WindowState(WindowStates.delAllPrefixWSRP(result_in_array[m])));
                    }
                  }
                }
                return result; // for performance improving
              }
            }
          }
        } catch (WSRPException e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }

  public boolean isStateSupported(String portletAppName,
                                  String portletName,
                                  String markup,
                                  WindowState state) {
    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP))
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME))
        return adminPortlet.isStateSupported(markup, state);
    return false;
  }

  public boolean isEventPayloadTypeMatches(String portletAppName,
                                           QName eventName,
                                           Object payload) throws PortletContainerException {
    throw new UnsupportedOperationException("Unsupported operation 'isEventPayloadTypeMatches' for WSRP1 plugin");
    // throw new PortletContainerException("Unsupported operation 'isEventPayloadTypeMatches'
    // for WSRP1 plugin");
    // return null;
  }

  public Map<String, PortletData> getAllPortletMetaData() {
    log.debug("getAllPortletMetaData() entered");
    HashMap<String, PortletData> result = new HashMap<String, PortletData>();
    // put WSRPAdminPortlet
    result.put(WSRPConstants.WSRP_ADMIN_PORTLET_KEY, adminPortlet);
    // put all remote portlets
    String producerId = null;
    String portletHandle = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    ServiceDescription desc = null;
    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      try {
        desc = producer.getServiceDescription();
      } catch (WSRPException e) {
        e.printStackTrace();
      }
      producerId = producer.getID();
      PortletDescription[] portletDescriptions = desc.getOfferedPortlets();
      if (portletDescriptions != null) {
        for (int k = 0; k < portletDescriptions.length; k++) {
          PortletDescription portletDescription = portletDescriptions[k];
          portletHandle = portletDescription.getPortletHandle();
          // Create new Portlet remote instance
          // TODO
          Portlet portlet = new Portlet();
          portlet.setPortletName(portletDescription.getPortletHandle());
          portlet.setPortletClass(null);
          if (portletDescription.getDisplayName() != null) {
            DisplayName displayName = new DisplayName();
            displayName.setDisplayName(portletDescription.getDisplayName().getValue());
            displayName.setLang("en");
            portlet.addDisplayName(displayName);
          }
          if (portletDescription.getMarkupTypes() != null) {
            MarkupType[] mt = portletDescription.getMarkupTypes();
            for (int t = 0; t < mt.length; t++) {
              MarkupType markupType = mt[t];
              Supports supports = new Supports();
              supports.setMimeType(markupType.getMimeType());
              List<String> modes = new ArrayList<String>();
              String[] mm = markupType.getModes();
              for (int j = 0; j < mm.length; j++) {
                modes.add(mm[j]);
              }
              supports.setPortletMode(modes);
              List<String> states = new ArrayList<String>();
              String[] ss = markupType.getWindowStates();
              for (int j = 0; j < ss.length; j++) {
                states.add(ss[j]);
              }
              supports.setWindowState(states);
              portlet.addSupports(supports);
            }
          }
          if (StringUtils.split(portletHandle, "/").length == 1)
            portletHandle = "unnamed" + "/" + portletHandle;
          result.put(producerId + WSRPConstants.WSRP_PRODUCER_APP_ENCODER + portletHandle, new PortletDataImp(this.container,
                                                                                                              portlet,
                                                                                                              null,
                                                                                                              new ArrayList<UserAttribute>()));
        }
      }
    }
    return result;
  }

  public ResourceBundle getBundle(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String portletAppName,
                                  String portletName,
                                  Locale locale) throws PortletContainerException {
    MapResourceBundle bundle = new MapResourceBundle(locale);
    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP)) {
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        bundle.add(ResourceBundleManager.PORTLET_TITLE, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        bundle.add(ResourceBundleManager.PORTLET_SHORT_TITLE, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        bundle.add(ResourceBundleManager.KEYWORDS, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        return bundle;
      }
    }
    String producerID = portletAppName.substring(0, portletAppName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER));
    String portletHandle = portletAppName.substring(portletAppName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER) + 1)
        + Constants.PORTLET_HANDLE_ENCODER + portletName;
    try {
      PortletDescription pd = getProducer(producerID).getPortletDescription(portletHandle);
      bundle.add(ResourceBundleManager.PORTLET_TITLE, pd.getTitle().getValue());
      bundle.add(ResourceBundleManager.PORTLET_SHORT_TITLE, pd.getShortTitle().getValue());
      if (pd.getKeywords() != null) {
        bundle.add(ResourceBundleManager.KEYWORDS, pd.getKeywords()[0].getValue());
      } else {
        bundle.add(ResourceBundleManager.KEYWORDS, null);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bundle;
  }

  public void setPortletPreference(Input input,
                                   Map<String, String> preferences) throws PortletContainerException {
  }

  public Map<String, String[]> getPortletPreference(Input input) {
    return null;
  }

  public ActionOutput processAction(HttpServletRequest request,
                                    HttpServletResponse response,
                                    ActionInput input) throws PortletContainerException {
    log.debug("processAction method in WSRPConsumerPlugin entered");
    if (!init)
      return null;
    String appName = input.getInternalWindowID().getPortletApplicationName();
    String portletName = input.getInternalWindowID().getPortletName();

    String producerID = appName.substring(0, appName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER));
    String portletHandle = appName.substring(appName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER) + 1) + "/" + portletName;

    ActionOutput output = new ActionOutput();
    if (appName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP)) {
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        adminPortlet.getPortletObject().processAction(input, output, request);
        return output;
      }
    }

    PortletKey portletKey = null;
    try {
      portletKey = getPortletKey(producerID, portletHandle);
    } catch (Exception e) {
      throw new PortletContainerException("exception in WSRPConsumerPlugin.processAction method", e);
    }

    if (getProducer(portletKey.getProducerId()) != null) {
      try {
        String key = input.getInternalWindowID().generateKey();
        log.debug("use windowID : " + key);

        User user = getUser(request);
        String userID = "";
        if (user != null) {
          userID = user.getUserID();
          log.debug("use userID : " + userID);
        }

        WSRPPortlet portlet = getPortlet(portletKey, portletHandle);
        UserSessionMgr userSession = getUserSession(request.getSession(), portletKey.getProducerId());
        PortletWindowSession windowSession = getWindowSession(portletKey, portlet, userSession, key);
        PortletDriver portletDriver = consumer.getPortletDriverRegistry().getPortletDriver(portlet);
        InteractionRequest iRequest = getInteractionRequest(windowSession, request, input);

        /* MAIN INVOKE */
        BlockingInteractionResponse iResponse = portletDriver.performBlockingInteraction(iRequest, userSession, userID);

        if (iResponse != null) {
          log.debug("manage BlockingInteractionResponse object content");
          UpdateResponse updateResponse = iResponse.getUpdateResponse();
          String redirectURL = iResponse.getRedirectURL();
          if (updateResponse != null) {
            if (windowSession != null) {
              updateSessionContext(updateResponse.getSessionContext(), windowSession.getPortletSession());
              windowSession.updateMarkupCache(updateResponse.getMarkupContext());
            }
            updatePortletContext(updateResponse.getPortletContext(), portlet);
            String navState = updateResponse.getNavigationalState();
            if (navState != null) {
              log.debug("set new navigational state : " + navState);
              output.setRenderParameter(WSRPConstants.WSRP_NAVIGATIONAL_STATE, navState);
            }
            String newMode = updateResponse.getNewMode();
            if (newMode != null) {
              log.debug("set Mode required : " + newMode);
              if (newMode.equalsIgnoreCase(Modes._view)) {
                output.setNextMode(PortletMode.VIEW);
              } else if (newMode.equalsIgnoreCase(Modes._edit)) {
                output.setNextMode(PortletMode.EDIT);
              } else if (newMode.equalsIgnoreCase(Modes._help)) {
                output.setNextMode(PortletMode.HELP);
              }
            }
            String newWindowState = updateResponse.getNewWindowState();
            if (newWindowState != null) {
              log.debug("set new required window state : " + newWindowState);
              if (newWindowState.equalsIgnoreCase(WindowStates._maximized)) {
                output.setNextState(WindowState.MAXIMIZED);
              } else if (newWindowState.equalsIgnoreCase(WindowStates._minimized)) {
                output.setNextState(WindowState.MINIMIZED);
              } else if (newWindowState.equalsIgnoreCase(WindowStates._normal)) {
                output.setNextState(WindowState.NORMAL);
              }
            }
          } else if (redirectURL != null) {
            log.debug("Redirect action to URL : " + redirectURL);
            if (redirectURL.startsWith("/") || redirectURL.startsWith("http://") || redirectURL.startsWith("https://")) {
              output.addProperty(Output.SEND_REDIRECT, redirectURL);
            } else {
              log.error("Can not redirect action: a relative or incorrect path URL is given");
            }
          }
        }
        return output;
      } catch (WSRPException e) {
        throw new PortletContainerException("exception in WSRPConsumerPlugin.processAction method", e);
      }
    }
    return output;
  }

  public EventOutput processEvent(HttpServletRequest request,
                                  HttpServletResponse response,
                                  EventInput input) throws PortletContainerException {
    throw new UnsupportedOperationException("Unsupported operation 'processEvent' for WSRP1 plugin");
    // throw new PortletContainerException("Unsupported operation 'processEvent'
    // for WSRP1 plugin");
    // return null;
  }

  public ResourceOutput serveResource(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ResourceInput input) throws PortletContainerException {
    throw new UnsupportedOperationException("Unsupported operation 'serveResource' for WSRP1 plugin");
    // throw new PortletContainerException("Unsupported operation
    // 'serveResource' for WSRP1 plugin");
    // return null;
  }

  public RenderOutput render(HttpServletRequest request,
                             HttpServletResponse response,
                             RenderInput input) throws PortletContainerException {
    log.debug("Render method in WSRPConsumerPlugin entered");

    templateComposer.setHost(request.getServerName());
    templateComposer.setPort(request.getServerPort());

    String appName = input.getInternalWindowID().getPortletApplicationName();
    String portletName = input.getInternalWindowID().getPortletName();
    String uniqueID = input.getInternalWindowID().getUniqueID();

    RenderOutput output = new RenderOutput();
    if (appName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP))
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        response.setContentType("text/html");
        adminPortlet.getPortletObject().render(input, output);
        return output;
      }

    String producerID = appName.substring(0, appName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER));
    String portletHandle = appName.substring(appName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER) + 1) + Constants.PORTLET_HANDLE_ENCODER
        + portletName;

    try {
      WindowState state = input.getWindowState();
      if (!state.equals(WindowState.MINIMIZED)) {
        PortletKey portletKey = getPortletKey(producerID, portletHandle);
        if (getProducer(portletKey.getProducerId()) == null) {
          // haven't producer or doesn't configured
        } else {
          WindowID windowID = input.getInternalWindowID();
          String key = windowID.generateKey();
          log.debug("key generated by windowID : " + key);
          response.setContentType(input.getMarkup());

          User user = getUser(request);
          String userID = null;
          if (user != null) {
            log.debug("use userID : " + userID);
            userID = user.getUserID();
          }
          WSRPPortlet portlet = null;
          PortletDriver portletDriver = null;
          PortletWindowSession portletWindowSession = null;
          MarkupResponse mResponse = null;
          UserSessionMgr userSession = null;

          try {
            userSession = getUserSession(request.getSession(), portletKey.getProducerId());
            portlet = getPortlet(portletKey, portletHandle);
            // below I add the uniqueID to the portlet handle within
            // PortletContext
            portlet.getPortletContext().setPortletHandle(portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID);
            portletDriver = consumer.getPortletDriverRegistry().getPortletDriver(portlet);
            portletWindowSession = getWindowSession(portletKey, portlet, userSession, key);

            WSRPMarkupRequest markupRequest = getMarkupRequest(request, portletWindowSession, input);
            String path = null;
            path = request.getRequestURI();
            log.debug("User path info : " + path);
            if (path == null)
              path = basePath;
            path += "?";
            String remoteUser = request.getRemoteUser();
            if (remoteUser != null) {
              path += org.exoplatform.Constants.PORTAL_CONTEXT + "=" + request.getRemoteUser() + "&";
            }
            path += org.exoplatform.Constants.COMPONENT_PARAMETER + "=" + appName + Constants.PORTLET_HANDLE_ENCODER + portletName;
            // + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
            log.debug("use base path : " + path);
            path = input.getBaseURL();

            /* MAIN INVOKE */
            mResponse = portletDriver.getMarkup(markupRequest, userSession, path);
            if (mResponse != null) {
              if (portletWindowSession != null) {
                updateSessionContext(mResponse.getSessionContext(), portletWindowSession.getPortletSession());
              }
              processMarkupContext(mResponse.getMarkupContext(), response, output);
            }
            if (portletWindowSession != null) {
              log.debug("Update cache");
              portletWindowSession.updateMarkupCache(null);
            }
          } catch (Throwable t) {
            log.error("WS Fault occured", t);
            Writer w = response.getWriter();
            w.write("a WSRP Fault occured");
          }
        }
      } else {
        // if WindowState equals MINIMIZED
        output.setTitle(consumer.getProducerRegistry().getProducer(producerID).getPortletDescription(portletHandle).getTitle().getValue());
      }
    } catch (Exception e) {
      e.printStackTrace();
      try {
        output.setContent(e.toString().getBytes("utf-8"));
      } catch (java.io.UnsupportedEncodingException unExc) {
        output.setContent(unExc.toString().getBytes());
      }
    } finally {
      return output;
    }

  }

  public void sendAttrs(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> attrs,
                        String portletApplicationName) throws PortletContainerException {
  }

  private UserSessionMgr getUserSession(HttpSession httpSession,
                                        String producerID) throws WSRPException {

    UserSessionMgr userSession = (UserSessionMgr) httpSession.getAttribute(USER_SESSIONS_KEY + producerID);
    if (userSession == null) {
      log.debug("Create new UserSession");
      userSession = new UserSessionImpl(getProducer(producerID).getMarkupInterfaceEndpoint());
      httpSession.setAttribute(USER_SESSIONS_KEY + producerID, userSession);
    } else {
      log.debug("Use existing UserSession");
    }
    return userSession;
  }

  private PortletKey getPortletKey(String producerID,
                                   String portletHandle) throws PortletException {
    PortletKey portletKey = null;
    Iterator<WSRPPortlet> iter = consumer.getPortletRegistry().getAllPortlets();
    while (iter.hasNext()) {
      WSRPPortlet element = (WSRPPortlet) iter.next();
      if (producerID.equals(element.getPortletKey().getProducerId()) && portletHandle.equals(element.getPortletKey().getPortletHandle())) {
        portletKey = (PortletKeyAdapter) element.getPortletKey();
      }
    }
    if (portletKey == null) {
      portletKey = new PortletKeyAdapter();
      portletKey.setProducerId(producerID);
      log.debug("user portlet key, producerID : ");
      portletKey.setPortletHandle(portletHandle);
    }
    return portletKey;
  }

  private Producer getProducer(String producerID) {
    log.debug("getProducer : " + producerID);
    Producer producer = consumer.getProducerRegistry().getProducer(producerID);
    return producer;
  }

  private User getUser(HttpServletRequest request) {
    User user = null;
    WindowInfosContainer scontainer = WindowInfosContainer.getInstance(); // TODO
    if (scontainer != null) {
      String userKey = scontainer.getOwner();
      log.debug("getUser method with user key : " + userKey);
      user = consumer.getUserRegistry().getUser(userKey);
      if (user == null) {
        user = new UserAdapter();
        UserContext userContext = new UserContext();
        userContext.setProfile(fillUserProfile(request));
        userContext.setUserContextKey(userKey);
        user.setUserID(userKey);
        user.setUserContext(userContext);
        consumer.getUserRegistry().addUser(user);
      }
    }
    return user;
  }

  private UserProfile fillUserProfile(HttpServletRequest request) {
    UserProfile userProfile = null;
    Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
    if (userInfo != null) {
      userProfile = new UserProfile();
      PersonName personName = new PersonName();
      Object nameObject = userInfo.get("name.given");
      if (nameObject == null)
        personName.setGiven("unknow name");
      else
        personName.setGiven(nameObject.toString());
      userProfile.setName(personName);
    }
    return userProfile;
  }

  private WSRPPortlet getPortlet(PortletKey portletKey,
                                 String parentHandle) throws WSRPException {
    WSRPPortlet portlet = null;
    if (portletKey != null) {
      portlet = consumer.getPortletRegistry().getPortlet(portletKey);
      if (portlet == null) {
        portlet = createPortlet(portletKey, parentHandle);
        consumer.getPortletRegistry().addPortlet(portlet);
      }
    }
    return portlet;
  }

  private WSRPPortlet createPortlet(PortletKey portletKey,
                                    String parentHandle) {
    WSRPPortlet portlet = new WSRPPortletAdapter(portletKey);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletKey.getPortletHandle());
    portlet.setPortletContext(portletContext);
    if (parentHandle != null) {
      portlet.setParent(parentHandle);
    } else {
      portlet.setParent(portletKey.getPortletHandle());
    }
    return portlet;
  }

  private PortletWindowSession getWindowSession(PortletKey portletKey,
                                                WSRPPortlet portlet,
                                                UserSessionMgr userSession,
                                                String windowID) throws WSRPException {
    if (userSession != null) {
      log.debug("get group session form user session");
      String groupID = getPortletDescription(portlet).getGroupID();
      groupID = groupID == null ? "default" : groupID;
      log.debug("group ID : " + groupID);
      GroupSession groupSession = userSession.getGroupSession(groupID);
      if (groupSession != null) {
        log.debug("get portlet session from group session");
        org.exoplatform.services.wsrp.consumer.PortletSession portletSession = groupSession.getPortletSession(portletKey.getPortletHandle());
        log.debug("portlet handle : " + portletKey.getPortletHandle());
        if (portletSession != null) {
          PortletWindowSession windowSession = portletSession.getPortletWindowSession(windowID);
          log.debug("success in extraction of the window session");
          return windowSession;
        } else {
          log.error("portlet session was null");
          throw new WSRPException(Faults.INVALID_SESSION_FAULT);
        }
      } else {
        log.error("group session was null");
        throw new WSRPException(Faults.INVALID_SESSION_FAULT);
      }
    } else {
      log.error("user session was null");
      throw new WSRPException(Faults.INVALID_SESSION_FAULT);
    }
  }

  private PortletDescription getPortletDescription(WSRPPortlet portlet) throws WSRPException {
    log.debug("getPortletDescription entered");
    String producerID = portlet.getPortletKey().getProducerId();
    Producer producer = getProducer(producerID);
    PortletDescription portletDesc = producer.getPortletDescription(portlet.getParent());
    if (portletDesc == null) {
      throw new WSRPException(Faults.UNKNOWN_PORTLET_DESCRIPTION);
    }
    return portletDesc;
  }

  private InteractionRequest getInteractionRequest(PortletWindowSession portletWindowSession,
                                                   HttpServletRequest request,
                                                   ActionInput input) {
    log.debug("getInteractionRequest entered");
    InteractionRequestAdapter interactionRequest = new InteractionRequestAdapter();
    fillMarkupRequest(interactionRequest, request, portletWindowSession, input);
    interactionRequest.setNavigationalState(getNavigationalState(request, portletWindowSession));
    interactionRequest.setInteractionState(getInteractionState(request, portletWindowSession));
    interactionRequest.setFormParameters(getFormParameters(input));
    // interactionRequest.setInteractionState();
    return interactionRequest;
  }

  private NamedString[] getFormParameters(Input input) {
    Map<String, String[]> params = (Map<String, String[]>) input.getRenderParameters();// request.getParameterMap();
    if (params == null)
      return null;
    log.debug("Parameter map empty : " + params.isEmpty());
    if (params.isEmpty())
      return null;
    Set<String> keys = params.keySet();
    List<NamedString> listNamedStringParams = new ArrayList<NamedString>();
    Iterator<String> iteratorKeys = keys.iterator();
    while (iteratorKeys.hasNext()) {
      String key = (String) iteratorKeys.next();
      String[] values = (String[]) params.get(key);
      for (String value : values) {
        listNamedStringParams.add(Utils.getNamesString(key, value));
      }
    }
    return (NamedString[]) listNamedStringParams.toArray(new NamedString[listNamedStringParams.size()]);
  }

  private String getInteractionState(HttpServletRequest request,
                                     PortletWindowSession portletWindowSession) {
    String is = request.getParameter(WSRPConstants.WSRP_INTERACTION_STATE);
    log.debug("user interaction state : " + is);
    return is;
  }

  private void fillMarkupRequest(WSRPBaseRequestAdapter markupRequest,
                                 HttpServletRequest request,
                                 PortletWindowSession portletWindowSession,
                                 Input input) {
    markupRequest.setCharacterEncodingSet(characterEncodings);
    markupRequest.setClientData(getClientData());

    List<Locale> locales = input.getLocales();
    List<String> localesStrings = new ArrayList<String>();
    for (Locale locale : locales) {
      localesStrings.add(locale.toString());
    }
    markupRequest.setLocales(localesStrings.toArray(new String[input.getLocales().size()]));// manageEnumeration(request.getLocales()));

    Collection<String> supportedContent = pcConf.getSupportedContent();
    String[] mimeTypes = (String[]) supportedContent.toArray(new String[supportedContent.size()]);
    markupRequest.setMimeTypes(mimeTypes);

    markupRequest.setMode(Modes.addPrefixWSRP(input.getPortletMode().toString()));
    markupRequest.setModes(null);// TODO
    markupRequest.setUserAuthentication("none");
    markupRequest.setWindowState(WindowStates.addPrefixWSRP(input.getWindowState().toString()));
    markupRequest.setWindowStates(null);// TODO
    // specific to WSRP
    if (portletWindowSession.getPortletSession().getSessionContext() != null) {
      markupRequest.setSessionID(portletWindowSession.getPortletSession().getSessionContext().getSessionID());
    }
    markupRequest.setPortletInstanceKey(input.getInternalWindowID().generateKey());
  }

  private ClientData getClientData() {
    ClientData clientData = new ClientData();
    clientData.setUserAgent(userAgent);
    return clientData;
  }

  private String getNavigationalState(HttpServletRequest request,
                                      PortletWindowSession portletWindowSession) {
    String ns = request.getParameter(WSRPConstants.WSRP_NAVIGATIONAL_STATE);
    if (ns != null) {
      log.debug("user navigational state : " + ns);
      portletWindowSession.setNavigationalState(ns);
    } else {
      log.debug("Navigational state null");
    }
    return portletWindowSession.getNavigationalState();
  }

  private void updateSessionContext(SessionContext sessionContext,
                                    PortletSession portletSession) {
    if (sessionContext != null) {
      log.debug("update session context");
      if (portletSession != null) {
        portletSession.setSessionContext(sessionContext);
      }
    }
  }

  private void updatePortletContext(PortletContext portletContext,
                                    WSRPPortlet portlet) throws WSRPException {
    if (portletContext != null && portlet != null) {
      log.debug("update portlet context");
      String newPortletHandle = portletContext.getPortletHandle();
      PortletKey portletKey = portlet.getPortletKey();
      if (newPortletHandle != null && !newPortletHandle.equals(portletKey.getPortletHandle())) {
        log.debug("portlet was cloned, new handle : " + newPortletHandle);
        String producerID = portletKey.getProducerId();
        PortletKey newPortletKey = new PortletKeyAdapter();
        portletKey.setPortletHandle(newPortletHandle);
        portletKey.setPortletHandle(producerID);
        portlet = createPortlet(newPortletKey, portlet.getParent());
        consumer.getPortletRegistry().addPortlet(portlet);
      }
      portlet.setPortletContext(portletContext);
    }
  }

  private WSRPMarkupRequest getMarkupRequest(HttpServletRequest request,
                                             PortletWindowSession portletWindowSession,
                                             RenderInput input) {
    WSRPMarkupRequestAdapter markupRequest = new WSRPMarkupRequestAdapter();
    fillMarkupRequest(markupRequest, request, portletWindowSession, input);
    markupRequest.setNavigationalState(getNavigationalState(request, portletWindowSession));
    markupRequest.setCachedMarkup(portletWindowSession.getCachedMarkup());
    return markupRequest;
  }

  private String processMarkupContext(MarkupContext markupContext,
                                      HttpServletResponse response,
                                      RenderOutput output) throws WSRPException {
    log.debug("process returned markup");
    String markup = null;
    byte[] binaryMarkup = null;
    if (markupContext != null && output != null) { // && response != null) {
      String title = markupContext.getPreferredTitle();
      if (title != null) {
        log.debug("user title : " + title);
        output.setTitle(title);
      }
      output.setContentType(markupContext.getMimeType());
      markup = markupContext.getMarkupString();
      binaryMarkup = markupContext.getMarkupBinary();
      if (markup != null) {
        log.debug("markup non null");
        try {
          output.setContent(markup.getBytes("utf-8"));
        } catch (java.io.UnsupportedEncodingException e) {
          output.setContent(markup.getBytes());
        }
      }
      if (binaryMarkup != null) {
        output.setContent(binaryMarkup);
      }
    }
    return markup;
  }

}
