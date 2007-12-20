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

package org.exoplatform.services.wsrp2.consumer.impl;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletDataImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.bundle.ResourceBundleManager;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp2.consumer.GroupSession;
import org.exoplatform.services.wsrp2.consumer.PortletDriver;
import org.exoplatform.services.wsrp2.consumer.PortletKey;
import org.exoplatform.services.wsrp2.consumer.PortletSession;
import org.exoplatform.services.wsrp2.consumer.PortletWindowSession;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp2.consumer.URLTemplateComposer;
import org.exoplatform.services.wsrp2.consumer.User;
import org.exoplatform.services.wsrp2.consumer.UserSession;
import org.exoplatform.services.wsrp2.consumer.UserSessionMgr;
import org.exoplatform.services.wsrp2.consumer.WSRPEventsRequest;
import org.exoplatform.services.wsrp2.consumer.WSRPInteractionRequest;
import org.exoplatform.services.wsrp2.consumer.WSRPMarkupRequest;
import org.exoplatform.services.wsrp2.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp2.consumer.WSRPResourceRequest;
import org.exoplatform.services.wsrp2.consumer.adapters.PortletKeyAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.UserAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.WSRPBaseRequestAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.WSRPEventsRequestAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.WSRPInteractionRequestAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.WSRPMarkupRequestAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.WSRPPortletAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.WSRPResourceRequestAdapter;
import org.exoplatform.services.wsrp2.consumer.impl.helpers.UserSessionImpl;
import org.exoplatform.services.wsrp2.consumer.portlets.WSRPAdminPortletDataImp;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.impl.WSRPConfiguration;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.EventDescription;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.ItemDescription;
import org.exoplatform.services.wsrp2.type.MarkupContext;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.MarkupType;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.NavigationalContext;
import org.exoplatform.services.wsrp2.type.ParameterDescription;
import org.exoplatform.services.wsrp2.type.PersonName;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.ResourceContext;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SessionContext;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.UpdateResponse;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.type.UserProfile;
import org.exoplatform.services.wsrp2.utils.JAXBEventTransformer;
import org.exoplatform.services.wsrp2.utils.Modes;
import org.exoplatform.services.wsrp2.utils.Utils;
import org.exoplatform.services.wsrp2.utils.WindowStates;

/**
 * Based on WSRPConsumerPortlet written by Benjamin Mestrallet Author: Roman
 * Pedchenko roman.pedchenko@exoplatform.com.ua
 */

public class WSRPConsumerPlugin implements PortletContainerPlugin {

  private static final String[]     characterEncodings = { "UTF-8" };

  private static final String[]     mimeTypes          = { "text/html", "text/wml" };

  public static final String[]      SUPPORTED_LOCALES  = { "en", "fr" };

  private static final String       consumerAgent      = "exoplatform.3.0";

  private static final String       userAgent          = "userAgent";

  //  private static final String       basePath           = "/portal/";

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
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
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

  public void setProperties(Map<String, String> properties) {
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
    // 
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
    List<PortletMode> result = null;
    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      try {
        ServiceDescription desc = producer.getServiceDescription();
        result = new ArrayList<PortletMode>();
        org.exoplatform.services.wsrp2.type.ItemDescription[] iDArray = desc.getCustomModeDescriptions();
        if (iDArray != null) {
          for (int j = 0; j < iDArray.length; j++) {
            ItemDescription iD = iDArray[j];
            String mode = iD.getItemName();
            if (!result.contains(mode)) {
              result.add(new PortletMode(mode));
            }
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
    List<WindowState> result = null;
    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      try {
        ServiceDescription desc = producer.getServiceDescription();
        result = new ArrayList<WindowState>();
        ItemDescription[] iDArray = desc.getCustomWindowStateDescriptions();
        if (iDArray != null) {
          for (int j = 0; j < iDArray.length; j++) {
            ItemDescription iD = iDArray[j];
            String state = iD.getItemName();
            if (!result.contains(state)) {
              result.add(new WindowState(state));
            }
          }
        }
      } catch (WSRPException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public Collection<PortletMode> getPortletModes(String portletAppName,
                                                 String portletName,
                                                 String markup) {

    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);

    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP))
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME))
        return adminPortlet.getPortletModes(markup);

    if (!init)
      return null;

    ArrayList<PortletMode> result = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      if (producer.getID().equalsIgnoreCase(producerID)) {
        try {
          ServiceDescription desc = producer.getServiceDescription();
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

  private String getPortletHandle(String portletAppName,
                                  String portletName) {
    if (portletAppName == null || portletName == null)
      return null;
    String result;
    if (!portletAppName.contains(WSRPConstants.WSRP_PRODUCER_APP_ENCODER))
      result = portletAppName;
    else
      result = portletAppName.substring(portletAppName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER) + 1);
    result += Constants.PORTLET_HANDLE_ENCODER + portletName;
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
    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);

    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP))
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME))
        return adminPortlet.getWindowStates(markup);

    if (!init)
      return null;

    ArrayList<WindowState> result = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();

    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      if (producer.getID().equalsIgnoreCase(producerID)) {
        try {
          ServiceDescription desc = producer.getServiceDescription();
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
                                           Object payload) {
    // consumer.getProducerRegistry().

    Producer producer = getProducer(getProducerID(portletAppName));
    ServiceDescription sd;
    try {
      sd = producer.getServiceDescription();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    if (sd == null)
      return false;
    if (sd != null) {
      EventDescription[] eds = sd.getEventDescriptions();
      if (eds != null) {
        for (EventDescription ed : eds) {
          // if this name doesn't present in getName or getAliases then continue to next iteration
          if (!(ed.getName() != null && ed.getName().equals(eventName))
              && !(ed.getAliases() != null && Utils.getQNameList(ed.getAliases()).contains(eventName)))
            continue;
          // caught appropriate event
          if (ed.getType() == null)
            return false;
          try {
            Class clazz = Class.forName(ed.getType().toString());
            if (log.isDebugEnabled())
              log.debug("Event loaded class for eventName: '" + eventName + "' is: " + clazz);
            return clazz.isInstance(payload); // just here we would return TRUE
          } catch (Exception e) {
            return false;
          }
        }
      }
    }
    return false;
  }

  public Map<String, PortletData> getAllPortletMetaData() {
    log.debug("getAllPortletMetaData() entered");
    // similar to PortletApplicationsHolder.getAllPortletMetaData()
    Map<String, PortletData> result = new HashMap<String, PortletData>();
    // put WSRPAdminPortlet
    result.put(WSRPConstants.WSRP_ADMIN_PORTLET_KEY, adminPortlet);
    // put all remote portlets
    String producerId = null;
    String portletHandle = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    while (i.hasNext()) {
      Producer producer = (Producer) i.next();
      try {
        ServiceDescription desc = producer.getServiceDescription();
        producerId = producer.getID();
        PortletDescription[] portletDescriptions = desc.getOfferedPortlets();
        if (portletDescriptions != null) {
          for (int k = 0; k < portletDescriptions.length; k++) {
            PortletDescription portletDescription = portletDescriptions[k];
            portletHandle = portletDescription.getPortletHandle();
            // Create new Portlet remote instance
            // TODO
            // Similar to XMLParser.readPortlet()
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
              for (MarkupType markupType : portletDescription.getMarkupTypes()) {
                Supports supports = new Supports();
                supports.setMimeType(markupType.getMimeType());
                supports.setPortletMode((List<String>) Arrays.asList(markupType.getModes()));
                supports.setWindowState((List<String>) Arrays.asList(markupType.getWindowStates()));
                portlet.addSupports(supports);
              }
            }
            portlet.setEscapeXml(Boolean.TRUE);// portletDescription.getEscapeXml();
            portlet.setApplication(new PortletApp());
            if (portletDescription.getPublishedEvents() != null)
              for (QName event : portletDescription.getPublishedEvents())
                portlet.addSupportedPublishingEvent(event);
            if (portletDescription.getHandledEvents() != null)
              for (QName event : portletDescription.getHandledEvents())
                portlet.addSupportedProcessingEvent(event);
            if (portletDescription.getNavigationalPublicValueDescriptions() != null)
              for (ParameterDescription parameter : portletDescription.getNavigationalPublicValueDescriptions())
                for (QName parameterName : parameter.getNames())
                  portlet.addSupportedPublicRenderParameter(parameterName.toString());
            // portletDescription.getMayReturnPortletState()
            if (StringUtils.split(portletHandle, "/").length == 1)
              portletHandle = "unnamed" + "/" + portletHandle;
            result.put(producerId + WSRPConstants.WSRP_PRODUCER_APP_ENCODER + portletHandle, new PortletDataImp(this.container,
                                                                                                                portlet,
                                                                                                                null,
                                                                                                                new ArrayList<UserAttribute>()));
          }
        }
      } catch (WSRPException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public ResourceBundle getBundle(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String portletAppName,
                                  String portletName,
                                  Locale locale) throws PortletContainerException {
    log.debug("getBundle method in WSRPConsumerPlugin entered");
    MapResourceBundle bundle = new MapResourceBundle(locale);
    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP)) {
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        bundle.add(ResourceBundleManager.PORTLET_TITLE, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        bundle.add(ResourceBundleManager.PORTLET_SHORT_TITLE, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        bundle.add(ResourceBundleManager.KEYWORDS, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        return bundle;
      }
    }
    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);
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

    ActionOutput output = new ActionOutput();

    configureTemplateComposer(request);

    String appName = input.getInternalWindowID().getPortletApplicationName();
    String portletName = input.getInternalWindowID().getPortletName();
    String uniqueID = input.getInternalWindowID().getUniqueID();

    /* for WSRP Admin Portlet */
    if (appName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP)) {
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        adminPortlet.getPortletObject().processAction(input, output, request);
        return output;
      }
    }

    String producerID = getProducerID(appName);
    String portletHandle = getPortletHandle(appName, portletName);

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
        //        User user = getUser(request);
        //        String userID = "";
        //        if (user != null) {
        //          userID = user.getUserID();
        //          log.debug("use userID : " + userID);
        //        }

        WSRPPortlet portlet = getPortlet(portletKey, portletHandle);
        portlet.getPortletContext().setPortletHandle(portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID);

        UserSessionMgr userSession = getUserSession(request.getSession(), portletKey.getProducerId());
        PortletWindowSession windowSession = getWindowSession(portletKey, portlet, userSession, key);

        PortletDriver portletDriver = consumer.getPortletDriverRegistry().getPortletDriver(portlet);
        WSRPInteractionRequest iRequest = getInteractionRequest(windowSession, request, input);

        String baseURL = null;
        //        baseURL = request.getRequestURI();
        //        log.debug("User path info : " + baseURL);
        //        if (baseURL == null) {
        //          //path = basePath;
        //        }
        //        baseURL += "?";
        //        String remoteUser = request.getRemoteUser();
        //        if (remoteUser != null) {
        //          baseURL += org.exoplatform.Constants.PORTAL_CONTEXT + "=" + request.getRemoteUser() + "&";
        //        }
        //        baseURL += org.exoplatform.Constants.COMPONENT_PARAMETER + "=" + appName + Constants.PORTLET_HANDLE_ENCODER + portletName;
        //        // + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
        //        log.debug("use base path : " + baseURL);
        baseURL = input.getBaseURL();

        /* MAIN INVOKE */
        BlockingInteractionResponse iResponse = portletDriver.performBlockingInteraction(iRequest, userSession, baseURL);
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

            NavigationalContext navigationalContext = updateResponse.getNavigationalContext();
            if (navigationalContext != null) {
              String navState = navigationalContext.getOpaqueValue();
              if (navState != null) {
                log.debug("set new navigational state : " + navState);
                output.setRenderParameter(WSRPConstants.WSRP_NAVIGATIONAL_STATE, navState);
              }
              NamedString[] namedStrings = navigationalContext.getPublicValues();
              if (namedStrings != null) {
                for (NamedString namedString : namedStrings) {
                  log.debug("set new navigational values : " + namedStrings);
                  output.setRenderParameter(WSRPConstants.WSRP_NAVIGATIONAL_VALUES, namedString.getValue());
                }
              }
            }
            output.setNextMode(Modes.getJsrPortletModeFromWsrpMode(updateResponse.getNewMode()));
            output.setNextState(WindowStates.getJsrPortletStateFromWsrpState(updateResponse.getNewWindowState()));
            // set events
            output.setEvents(JAXBEventTransformer.getEventsUnmarshal(updateResponse.getEvents()));

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

  private String getProducerID(String portletAppName) {
    if (portletAppName == null)
      return null;
    if (!portletAppName.contains(WSRPConstants.WSRP_PRODUCER_APP_ENCODER))
      return null;
    return portletAppName.substring(0, portletAppName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER));
  }

  public RenderOutput render(HttpServletRequest request,
                             HttpServletResponse response,
                             RenderInput input) throws PortletContainerException {

    // input.isStateSaveOnClient()
    // input.isUpdateCache()
    // input.getUserAttributes()
    // input.getPortletState()
    // input.getPortletPreferencesPersister()
    // input.getLocales()
    // input.getEscapeXml()
    // input.getCacheability()
    // input.getRenderParameters()
    // input.getPublicParamNames()
    // input.getTitle()

    log.debug("Render method in WSRPConsumerPlugin entered");
    if (!init)
      return null;

    RenderOutput output = new RenderOutput();

    configureTemplateComposer(request);

    String appName = input.getInternalWindowID().getPortletApplicationName();
    // example for debug: producer2-1006454248@portlets2
    String portletName = input.getInternalWindowID().getPortletName();
    // example for debug: TestPortlet
    String uniqueID = input.getInternalWindowID().getUniqueID();
    // example for debug: -806704985_A629953C5653522BAE25354D61E21B7B

    /* for WSRP Admin Portlet */
    if (appName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP)) {
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        response.setContentType("text/html");
        adminPortlet.getPortletObject().render(input, output);
        return output;
      }
    }

    String producerID = getProducerID(appName);
    // example for debug: producer2-1006454248
    String portletHandle = getPortletHandle(appName, portletName);
    // example for debug: portlets2/TestPortlet

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
          //          User user = getUser(request);
          //          String userID = null;
          //          if (user != null) {
          //            log.debug("use userID : " + userID);
          //            userID = user.getUserID();
          //          }

          try {
            UserSessionMgr userSession = getUserSession(request.getSession(), portletKey.getProducerId());
            WSRPPortlet portlet = getPortlet(portletKey, portletHandle);
            // below I add the uniqueID to the portlet handle within PortletContext
            portlet.getPortletContext().setPortletHandle(portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID);
            PortletDriver portletDriver = consumer.getPortletDriverRegistry().getPortletDriver(portlet);
            PortletWindowSession portletWindowSession = getWindowSession(portletKey, portlet, userSession, key);

            WSRPMarkupRequest markupRequest = getMarkupRequest(request, portletWindowSession, input);
            String baseURL = null;
            //            baseURL = request.getRequestURI();
            //            log.debug("User path info : " + baseURL);
            //            if (baseURL == null) {
            //              //path = basePath;
            //            }
            //            baseURL += "?";
            //            String remoteUser = request.getRemoteUser();
            //            if (remoteUser != null) {
            //              baseURL += org.exoplatform.Constants.PORTAL_CONTEXT + "=" + request.getRemoteUser() + "&";
            //            }
            //            baseURL += org.exoplatform.Constants.COMPONENT_PARAMETER + "=" + appName + Constants.PORTLET_HANDLE_ENCODER + portletName;
            //            // + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
            //            log.debug("use base path : " + baseURL);
            baseURL = input.getBaseURL();
            System.out.println(">>> EXOMAN WSRPConsumerPlugin.render() path = " + baseURL);

            /* MAIN INVOKE */
            MarkupResponse mResponse = portletDriver.getMarkup(markupRequest, userSession, baseURL);
            if (mResponse != null) {
              if (portletWindowSession != null) {
                updateSessionContext(mResponse.getSessionContext(), portletWindowSession.getPortletSession());
              }
              processMarkupContext(mResponse.getMarkupContext(), output);
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
                                                UserSession userSession,
                                                String windowID) throws WSRPException {
    if (userSession != null) {
      log.debug("get group session form user session");
      String groupID = getPortletDescription(portlet).getGroupID();
      groupID = groupID == null ? "default" : groupID;
      log.debug("group ID : " + groupID);
      GroupSession groupSession = userSession.getGroupSession(groupID);
      if (groupSession != null) {
        log.debug("get portlet session from group session");
        org.exoplatform.services.wsrp2.consumer.PortletSession portletSession = groupSession.getPortletSession(portletKey.getPortletHandle());
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

  private WSRPInteractionRequest getInteractionRequest(PortletWindowSession portletWindowSession,
                                                       HttpServletRequest request,
                                                       ActionInput input) {
    log.debug("getInteractionRequest entered");
    WSRPInteractionRequestAdapter interactionRequest = new WSRPInteractionRequestAdapter();
    fillMarkupRequest(interactionRequest, portletWindowSession, input);
    interactionRequest.setNavigationalState(getNavigationalState(request, portletWindowSession));
    interactionRequest.setNavigationalValues(getNavigationalValues(request, portletWindowSession));
    interactionRequest.setFormParameters(getFormParameters(input));
    interactionRequest.setInteractionState(getInteractionState(request, portletWindowSession));
    return interactionRequest;
  }

  private String getInteractionState(HttpServletRequest request,
                                     PortletWindowSession portletWindowSession) {
    String interactionState = request.getParameter(WSRPConstants.WSRP_INTERACTION_STATE);
    if (interactionState != null) {
      log.debug("user interaction state : " + interactionState);
      portletWindowSession.setInteractionState(interactionState);
    } else {
      log.debug("Interaction state is null");
    }
    return portletWindowSession.getInteractionState();
  }

  // Store only non wsrp- params
  private NamedString[] getFormParameters(Input input) {
    Map<String, String[]> params = (Map<String, String[]>) input.getRenderParameters();
    return Utils.getNamedStringArrayParameters(params, true);
  }

  private void fillMarkupRequest(WSRPBaseRequestAdapter baseRequest,
                                 PortletWindowSession portletWindowSession,
                                 Input input) {
    baseRequest.setMarkupCharacterSets(characterEncodings);
    baseRequest.setClientData(getClientData());

    List<Locale> locales = input.getLocales();
    List<String> localesStrings = new ArrayList<String>();
    for (Locale locale : locales) {
      localesStrings.add(locale.toString());
    }
    baseRequest.setLocales(localesStrings.toArray(new String[input.getLocales().size()]));

    Collection<String> supportedContent = pcConf.getSupportedContent();
    String[] mimeTypes = supportedContent.toArray(new String[supportedContent.size()]);
    baseRequest.setMimeTypes(mimeTypes);

    baseRequest.setMode(Modes.addPrefixWSRP(input.getPortletMode().toString()));
    baseRequest.setValidNewModes(null); // TODO
    baseRequest.setUserAuthentication("none");
    baseRequest.setWindowState(WindowStates.addPrefixWSRP(input.getWindowState().toString()));
    baseRequest.setValidNewWindowStates(null); // TODO
    baseRequest.setSecureClientCommunication(false);// TODO
    baseRequest.setExtensions(null);

    // For RuntimeContext
    SessionContext sc = portletWindowSession.getPortletSession().getSessionContext();
    if (sc != null) {
      baseRequest.setSessionID(sc.getSessionID());
    }
    baseRequest.setPortletInstanceKey(input.getInternalWindowID().generateKey());
  }

  private ClientData getClientData() {
    ClientData clientData = new ClientData();
    clientData.setUserAgent(userAgent);
    return clientData;
  }

  private String getNavigationalState(HttpServletRequest request,
                                      PortletWindowSession portletWindowSession) {
    String navigationalState = request.getParameter(WSRPConstants.WSRP_NAVIGATIONAL_STATE);
    if (navigationalState != null) {
      log.debug("user navigational state : " + navigationalState);
      portletWindowSession.setNavigationalState(navigationalState);
    } else {
      log.debug("Navigational state is null");
    }
    return portletWindowSession.getNavigationalState();
  }

  private NamedString[] getNavigationalValues(HttpServletRequest request,
                                              PortletWindowSession portletWindowSession) {
    NamedString[] resultArray = null;
    String[] navigationalValues = request.getParameterValues(WSRPConstants.WSRP_NAVIGATIONAL_VALUES);
    // from String[] to NamedString[] converting navigational values
    if (navigationalValues != null) {
      List<NamedString> navigationalValuesList = new ArrayList<NamedString>(navigationalValues.length);
      for (String navigationalValue : navigationalValues) {
        navigationalValuesList.add(new NamedString(navigationalValue, null));
      }
      resultArray = navigationalValuesList.toArray(new NamedString[navigationalValues.length]);
    }
    if (resultArray != null) {
      log.debug("user navigational values : " + resultArray);
      portletWindowSession.setNavigationalValues(resultArray);
    } else {
      log.debug("Navigational values are null");
    }
    return portletWindowSession.getNavigationalValues();
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
    fillMarkupRequest(markupRequest, portletWindowSession, input);
    // For NavigationalContext
    markupRequest.setNavigationalState(getNavigationalState(request, portletWindowSession));
    markupRequest.setNavigationalValues(getNavigationalValues(request, portletWindowSession));

    markupRequest.setCachedMarkup(portletWindowSession.getCachedMarkup());
    return markupRequest;
  }

  private void processMarkupContext(MarkupContext markupContext,
                                    RenderOutput output) throws WSRPException {
    log.debug("process markup context for returned markup");
    if (markupContext != null && output != null) {
      // markupContext.getCacheControl()
      // markupContext.getCcppProfileWarning()
      // markupContext.getExtensions()
      // markupContext.getLocale()
      // markupContext.getRequiresRewriting()
      // markupContext.getUseCachedItem()
      output.setNextPossiblePortletModes(getPortletModesFromStrings(markupContext.getValidNewModes()));
      String title = markupContext.getPreferredTitle();
      if (title != null) {
        log.debug("user title : " + title);
        output.setTitle(title);
      }
      output.setContentType(markupContext.getMimeType());
      String stringMarkup = markupContext.getItemString();
      byte[] binaryMarkup = markupContext.getItemBinary();
      if (stringMarkup != null) {
        log.debug("string markup not null");
        try {
          output.setContent(stringMarkup.getBytes("utf-8"));
        } catch (java.io.UnsupportedEncodingException e) {
          output.setContent(stringMarkup.getBytes());
        }
      }
      if (binaryMarkup != null) {
        log.debug("binary markup not null");
        output.setContent(binaryMarkup);
      }
    }
  }

  // Below changes of WSRP2 spec

  private Collection<PortletMode> getPortletModesFromStrings(String[] validNewModes) {
    if (validNewModes == null)
      return null;
    Collection<PortletMode> portletModes = new ArrayList<PortletMode>();
    for (String mode : validNewModes) {
      portletModes.add(new PortletMode(mode));
    }
    return portletModes;
  }

  public ResourceOutput serveResource(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ResourceInput input) throws PortletContainerException {
    // input.getCacheability()
    // input.getResourceID()

    log.debug("serveResource method in WSRPConsumerPlugin entered");
    if (!init)
      return null;

    ResourceOutput output = new ResourceOutput();

    configureTemplateComposer(request);

    String appName = input.getInternalWindowID().getPortletApplicationName();
    String portletName = input.getInternalWindowID().getPortletName();
    String uniqueID = input.getInternalWindowID().getUniqueID();

    /* for WSRP Admin Portlet */
    if (appName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP)) {
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        response.setContentType("text/html");
        adminPortlet.getPortletObject().serveResource(input, output);
        return output;
      }
    }

    String producerID = getProducerID(appName);
    String portletHandle = getPortletHandle(appName, portletName);

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
          //          User user = getUser(request);
          //          String userID = null;
          //          if (user != null) {
          //            log.debug("use userID : " + userID);
          //            userID = user.getUserID();
          //          }
          try {
            UserSessionMgr userSession = getUserSession(request.getSession(), portletKey.getProducerId());
            WSRPPortlet portlet = getPortlet(portletKey, portletHandle);
            // below I add the uniqueID to the portlet handle within
            // PortletContext
            portlet.getPortletContext().setPortletHandle(portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID);
            PortletDriver portletDriver = consumer.getPortletDriverRegistry().getPortletDriver(portlet);
            PortletWindowSession portletWindowSession = getWindowSession(portletKey, portlet, userSession, key);

            WSRPResourceRequest resourceRequest = getResourceRequest(request, portletWindowSession, input);

            String baseURL = null;
            //            baseURL = request.getRequestURI();
            //            log.debug("User path info : " + baseURL);
            //            if (baseURL == null) {
            //              //path = basePath;
            //            }
            //            baseURL += "?";
            //            String remoteUser = request.getRemoteUser();
            //            if (remoteUser != null) {
            //              baseURL += org.exoplatform.Constants.PORTAL_CONTEXT + "=" + request.getRemoteUser() + "&";
            //            }
            //            baseURL += org.exoplatform.Constants.COMPONENT_PARAMETER + "=" + appName + Constants.PORTLET_HANDLE_ENCODER + portletName;
            //            // + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
            //            log.debug("use base path : " + baseURL);
            baseURL = input.getBaseURL();

            /* MAIN INVOKE */
            ResourceResponse resResponse = portletDriver.getResource(resourceRequest, userSession, baseURL);

            if (resResponse != null) {
              if (portletWindowSession != null) {
                updateSessionContext(resResponse.getSessionContext(), portletWindowSession.getPortletSession());
              }
              processResourceContext(resResponse.getResourceContext(), output);
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
    } finally {
      return output;
    }
  }

  private void configureTemplateComposer(HttpServletRequest request) {
    templateComposer.setHost(request.getServerName());
    templateComposer.setPort(request.getServerPort());
  }

  private WSRPResourceRequest getResourceRequest(HttpServletRequest request,
                                                 PortletWindowSession portletWindowSession,
                                                 ResourceInput input) {
    WSRPResourceRequestAdapter resourceRequest = new WSRPResourceRequestAdapter();
    fillMarkupRequest(resourceRequest, portletWindowSession, input);
    resourceRequest.setNavigationalState(getNavigationalState(request, portletWindowSession));
    resourceRequest.setNavigationalValues(getNavigationalValues(request, portletWindowSession));
    resourceRequest.setCachedResource(portletWindowSession.getCachedResource());
    resourceRequest.setFormParameters(getFormParameters(input));
    resourceRequest.setResourceID(input.getResourceID());
    resourceRequest.setResourceState(getResourceState(request, portletWindowSession));
    resourceRequest.setResourceCacheability(input.getCacheability());
    return resourceRequest;
  }

  private String getResourceState(HttpServletRequest request,
                                  PortletWindowSession portletWindowSession) {
    String resourceState = request.getParameter(WSRPConstants.WSRP_RESOURCE_STATE);
    if (resourceState != null) {
      log.debug("user resource state : " + resourceState);
      portletWindowSession.setResourceState(resourceState);
    } else {
      log.debug("Resource state is null");
    }
    return portletWindowSession.getResourceState();
  }

  private void processResourceContext(ResourceContext resourceContext,
                                      ResourceOutput output) throws WSRPException {
    log.debug("process markup context for returned markup");
    String stringMarkup = null;
    byte[] binaryMarkup = null;
    if (resourceContext != null && output != null) {
      stringMarkup = resourceContext.getItemString();
      binaryMarkup = resourceContext.getItemBinary();
      if (resourceContext.getMimeType() != null) {
        output.setContentType(resourceContext.getMimeType());
      }
      if (stringMarkup != null) {
        log.debug("string markup not null");
        try {
          output.setContent(stringMarkup.getBytes("utf-8"));
        } catch (java.io.UnsupportedEncodingException e) {
          output.setContent(stringMarkup.getBytes());
        }
      }
      if (binaryMarkup != null) {
        log.debug("binary markup not null");
        output.setContent(binaryMarkup);
      }
    }
  }

  public EventOutput processEvent(HttpServletRequest request,
                                  HttpServletResponse response,
                                  EventInput input) throws PortletContainerException {
    log.debug("processEvent method in WSRPConsumerPlugin entered");

    if (!init)
      return null;

    EventOutput output = new EventOutput();

    configureTemplateComposer(request);

    String appName = input.getInternalWindowID().getPortletApplicationName();
    String portletName = input.getInternalWindowID().getPortletName();
    String uniqueID = input.getInternalWindowID().getUniqueID();

    /* for WSRP Admin Portlet */
    if (appName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP)) {
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        adminPortlet.getPortletObject().processEvent(input, output);
        return output;
      }
    }

    String producerID = getProducerID(appName);
    String portletHandle = getPortletHandle(appName, portletName);

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
        //        User user = getUser(request);
        //        String userID = "";
        //        if (user != null) {
        //          userID = user.getUserID();
        //          log.debug("use userID : " + userID);
        //        }

        WSRPPortlet portlet = getPortlet(portletKey, portletHandle);
        portlet.getPortletContext().setPortletHandle(portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID);

        UserSessionMgr userSession = getUserSession(request.getSession(), portletKey.getProducerId());
        PortletWindowSession windowSession = getWindowSession(portletKey, portlet, userSession, key);

        PortletDriver portletDriver = consumer.getPortletDriverRegistry().getPortletDriver(portlet);
        WSRPEventsRequest iRequest = getEventsRequest(windowSession, request, input);

        String baseURL = null;
        //         baseURL = request.getRequestURI();
        //        log.debug("User path info : " + baseURL);
        //        if (baseURL == null) {
        //          //path = basePath;
        //        }
        //        baseURL += "?";
        //        String remoteUser = request.getRemoteUser();
        //        if (remoteUser != null) {
        //          baseURL += org.exoplatform.Constants.PORTAL_CONTEXT + "=" + request.getRemoteUser() + "&";
        //        }
        //        baseURL += org.exoplatform.Constants.COMPONENT_PARAMETER + "=" + appName + Constants.PORTLET_HANDLE_ENCODER + portletName;
        //        // + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
        //        log.debug("use base path : " + baseURL);
        baseURL = input.getBaseURL();

        /* MAIN INVOKE */
        HandleEventsResponse iResponse = portletDriver.handleEvents(iRequest, userSession, baseURL);

        if (iResponse != null) {
          log.debug("manage BlockingInteractionResponse object content");
          UpdateResponse updateResponse = iResponse.getUpdateResponse();
          if (updateResponse != null) {
            output.setEvents(JAXBEventTransformer.getEventsUnmarshal(updateResponse.getEvents()));
            if (windowSession != null) {
              updateSessionContext(updateResponse.getSessionContext(), windowSession.getPortletSession());
              windowSession.updateMarkupCache(updateResponse.getMarkupContext());
            }
            updatePortletContext(updateResponse.getPortletContext(), portlet);
            NavigationalContext navigationalContext = updateResponse.getNavigationalContext();
            if (navigationalContext != null) {
              String navState = navigationalContext.getOpaqueValue();
              if (navState != null) {
                log.debug("set new navigational state : " + navState);
                output.setRenderParameter(WSRPConstants.WSRP_NAVIGATIONAL_STATE, navState);
              }
              NamedString[] namedStrings = navigationalContext.getPublicValues();
              if (namedStrings != null) {
                for (NamedString namedString : namedStrings) {
                  log.debug("set new navigational values : " + namedStrings);
                  output.setRenderParameter(WSRPConstants.WSRP_NAVIGATIONAL_VALUES, namedString.getValue());
                }
              }
            }
            output.setNextMode(Modes.getJsrPortletModeFromWsrpMode(updateResponse.getNewMode()));
            output.setNextState(WindowStates.getJsrPortletStateFromWsrpState(updateResponse.getNewWindowState()));
          }
        }
        return output;
      } catch (WSRPException e) {
        throw new PortletContainerException("exception in WSRPConsumerPlugin.processEvent method", e);
      }
    }
    return output;

  }

  private WSRPEventsRequest getEventsRequest(PortletWindowSession portletWindowSession,
                                             HttpServletRequest request,
                                             EventInput input) {
    WSRPEventsRequestAdapter eventRequest = new WSRPEventsRequestAdapter();
    fillMarkupRequest(eventRequest, portletWindowSession, input);
    eventRequest.setNavigationalState(getNavigationalState(request, portletWindowSession));
    eventRequest.setNavigationalValues(getNavigationalValues(request, portletWindowSession));
    eventRequest.setEvents(JAXBEventTransformer.getEventsMarshal(input.getEvent()));
    return eventRequest;
  }

}
