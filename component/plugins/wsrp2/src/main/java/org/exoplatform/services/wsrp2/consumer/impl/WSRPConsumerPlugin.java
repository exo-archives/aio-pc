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

import java.io.Serializable;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
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
import org.exoplatform.services.wsrp2.producer.impl.helpers.NamedStringWrapper;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.EventDescription;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.ItemDescription;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.MarkupContext;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.MarkupType;
import org.exoplatform.services.wsrp2.type.MimeResponse;
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
 * Based on WSRPConsumerPortlet written by Benjamin Mestrallet Author : Roman
 * Pedchenko roman.pedchenko@exoplatform.com.ua Author : Alexey Zavizionov
 * alexey.zavizionov@exoplatform.com.ua
 */

public class WSRPConsumerPlugin implements PortletContainerPlugin {

  private static final List<String> characterEncodings = new ArrayList<String>(); // = { "UTF-8" };

  private static final List<String> mimeTypes          = new ArrayList<String>(); //= { "text/html", "text/wml" };

  public static final List<String>  SUPPORTED_LOCALES  = new ArrayList<String>(); //= { "en", "fr" };

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
    consumer.setPortletStateChange(StateChange.READ_WRITE);
    // TODO modes and states should be getting from producer
    //
    // consumer.setSupportedModes(getPortletModes(Collections.list(pcConf.getSupportedPortletModes())));
    consumer.getSupportedModes().addAll(getPortletModes(pcService.getSupportedPortletModes()));
    // consumer.setSupportedWindowStates(getWindowStates(Collections.list(pcConf.getSupportedWindowStates())));
    consumer.getSupportedWindowStates()
            .addAll(getWindowStates(pcService.getSupportedWindowStates()));
    consumer.setUserAuthentication(WSRPConstants.NO_USER_AUTHENTIFICATION);
    consumer.setSupportedLocales(SUPPORTED_LOCALES);
    // Create WSRPAdminPortlet
    adminPortlet = new WSRPAdminPortletDataImp(this.container, conf.getAdminPortletParams());
    init = true;
  }

  private List<String> getPortletModes(Collection<PortletMode> supportedPortletModes) {
    List<String> array = new ArrayList<String>();
    int i = 0;
    for (Iterator<PortletMode> iterator = supportedPortletModes.iterator(); iterator.hasNext(); i++) {
      PortletMode portletMode = iterator.next();
      array.add(portletMode.toString());
      // was: array[i] = WSRPConstants.WSRP_PREFIX + portletMode.toString();
    }
    return array;
  }

  private List<String> getWindowStates(Collection<WindowState> supportedWindowStates) {
    List<String> array = new ArrayList<String>();
    int i = 0;
    for (Iterator<WindowState> iterator = supportedWindowStates.iterator(); iterator.hasNext(); i++) {
      WindowState windowState = iterator.next();
      array.add(windowState.toString());
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
      Producer producer = i.next();
      try {
        ServiceDescription desc = producer.getServiceDescription();
        result = new ArrayList<PortletMode>();
        List<ItemDescription> iDArray = desc.getCustomModeDescriptions();
        if (iDArray != null) {
          for (ItemDescription iD : iDArray) {
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
      Producer producer = i.next();
      try {
        ServiceDescription desc = producer.getServiceDescription();
        result = new ArrayList<WindowState>();
        List<ItemDescription> iDArray = desc.getCustomWindowStateDescriptions();
        if (iDArray != null) {
          for (ItemDescription iD : iDArray) {
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
    /* for WSRP Admin Portlet */
    if (WSRPAdminPortletDataImp.isOfferToProcess(portletAppName, portletName))
      return adminPortlet.getPortletModes(markup);

    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);

    if (!init)
      return null;

    ArrayList<PortletMode> result = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    while (i.hasNext()) {
      Producer producer = i.next();
      if (producer.getID().equalsIgnoreCase(producerID)) {
        try {
          ServiceDescription sD = producer.getServiceDescription();
          result = new ArrayList<PortletMode>();
          List<PortletDescription> portletDescriptions = sD.getOfferedPortlets();
          if (portletDescriptions != null) {
            for (PortletDescription portletDescription : portletDescriptions) {
              String portletHandleTemp = portletDescription.getPortletHandle();
              if (portletHandleTemp.equalsIgnoreCase(portletHandle)) {
                List<MarkupType> markupType = portletDescription.getMarkupTypes();
                for (MarkupType type : markupType) {
                  if (type.getMimeType().equalsIgnoreCase(markup)) {
                    List<String> result_in_array = type.getModes();
                    for (String string : result_in_array) {
                      result.add(Modes.getJsrPortletMode(string));
                    }
                  }
                }
                return result; // for improving performance
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

  public final String[] getPortalManagedPortletModes(final String portletAppName,
                                                     final String portletName) {
    /* for WSRP Admin Portlet */
    if (WSRPAdminPortletDataImp.isOfferToProcess(portletAppName, portletName))
      return new String[] {};

    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);

    if (!init)
      return null;

    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> producers = pregistry.getAllProducers();

    while (producers.hasNext()) {
      Producer producer = producers.next();
      if (producer.getID().equalsIgnoreCase(producerID)) {
        try {
          ServiceDescription sD = producer.getServiceDescription();
          List<PortletDescription> portletDescriptions = sD.getOfferedPortlets();
          if (portletDescriptions != null) {
            for (PortletDescription portletDescription : portletDescriptions) {
              if (portletDescription.getPortletHandle().equalsIgnoreCase(portletAppName
                  + Constants.PORTLET_HANDLE_ENCODER + portletName)) {
                return portletDescription.getPortletManagedModes().toArray(new String[] {});
              }
            }
          }
        } catch (WSRPException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  private String getPortletHandle(String portletAppName, String portletName) {
    if (portletAppName == null || portletName == null)
      return null;
    String result = null;
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
    /* for WSRP Admin Portlet */
    if (WSRPAdminPortletDataImp.isOfferToProcess(portletAppName, portletName))
      return adminPortlet.isModeSuported(markup, mode);

    return false;
  }

  public Collection<WindowState> getWindowStates(String portletAppName,
                                                 String portletName,
                                                 String markup) {
    /* for WSRP Admin Portlet */
    if (WSRPAdminPortletDataImp.isOfferToProcess(portletAppName, portletName))
      return adminPortlet.getWindowStates(markup);

    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);

    if (!init)
      return null;

    ArrayList<WindowState> result = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();

    while (i.hasNext()) {
      Producer producer = i.next();
      if (producer.getID().equalsIgnoreCase(producerID)) {
        try {
          ServiceDescription sD = producer.getServiceDescription();
          result = new ArrayList<WindowState>();
          List<PortletDescription> portletDescriptions = sD.getOfferedPortlets();
          if (portletDescriptions != null) {
            for (PortletDescription portletDescription : portletDescriptions) {
              String portletHandleTemp = portletDescription.getPortletHandle();
              if (portletHandleTemp.equalsIgnoreCase(portletHandle)) {
                List<MarkupType> markupType = portletDescription.getMarkupTypes();
                for (MarkupType type : markupType) {
                  if (type.getMimeType().equalsIgnoreCase(markup)) {
                    List<String> result_in_array = type.getWindowStates();
                    for (String string : result_in_array) {
                      result.add(WindowStates.getJsrWindowState(string));
                    }
                  }
                }
                return result; // for improving performance
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
    /* for WSRP Admin Portlet */
    if (WSRPAdminPortletDataImp.isOfferToProcess(portletAppName, portletName))
      return adminPortlet.isStateSupported(markup, state);
    return false;
  }

  public boolean isEventPayloadTypeMatches(String portletAppName,
                                           QName eventName,
                                           Serializable payload) {
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
      List<EventDescription> eds = sd.getEventDescriptions();
      if (eds != null) {
        for (EventDescription ed : eds) {
          // if this name doesn't present in getName or getAliases then continue to next iteration
          if (!(ed.getName() != null && ed.getName().equals(eventName))
              && !(ed.getAliases() != null && ed.getAliases().contains(eventName)))
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
      Producer producer = i.next();
      try {
        ServiceDescription desc = producer.getServiceDescription();
        producerId = producer.getID();
        List<PortletDescription> portletDescriptions = desc.getOfferedPortlets();
        if (portletDescriptions != null) {
          for (PortletDescription portletDescription : portletDescriptions) {
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
                supports.getPortletMode().addAll(markupType.getModes());
                supports.getWindowState().addAll(markupType.getWindowStates());
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
            result.put(producerId + WSRPConstants.WSRP_PRODUCER_APP_ENCODER + portletHandle,
                       new PortletDataImp(this.container,
                                          portlet,
                                          null,
                                          new ArrayList<UserAttribute>(),
                                          false));
          }
        }
      } catch (WSRPException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public PortletApp getPortletApp(String portletAppName) {
    return null;
  }

  public ResourceBundle getBundle(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String portletAppName,
                                  String portletName,
                                  Locale locale) throws PortletContainerException {
    log.debug("getBundle method in WSRPConsumerPlugin entered");
    MapResourceBundle bundle = new MapResourceBundle(locale);
    /* for WSRP Admin Portlet */
    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP)) {
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        bundle.add(PortletData.PORTLET_TITLE, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        bundle.add(PortletData.PORTLET_SHORT_TITLE, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        bundle.add(PortletData.KEYWORDS, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        return bundle;
      }
    }
    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);
    try {
      PortletDescription pd = getProducer(producerID).getPortletDescription(portletHandle);
      bundle.add(PortletData.PORTLET_TITLE,
                 Utils.getStringFromLocalizedString(pd.getTitle()));
      bundle.add(PortletData.PORTLET_SHORT_TITLE,
                 Utils.getStringFromLocalizedString(pd.getShortTitle()));
      if (pd.getKeywords() != null) {
        bundle.add(PortletData.KEYWORDS,
                   Utils.getStringFromLocalizedString(pd.getKeywords().get(0)));
      } else {
        bundle.add(PortletData.KEYWORDS, null);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bundle;
  }

  public void setPortletPreference(Input input, Map<String, String> preferences) throws PortletContainerException {
  }

  public Map<String, String[]> getPortletPreference(Input input) {
    return null;
  }

  public PortletPreferences getPortletPreferences(Input input) {
    return null;
  }

  public void setPortletPreference2(Input input, Map<String, String[]> preferences) throws PortletContainerException {
  }

  public void setPortletPreferences(Input input, PortletPreferences preferences) throws PortletContainerException {
  }

  public ActionOutput processAction(HttpServletRequest request,
                                    HttpServletResponse response,
                                    ActionInput input) throws PortletContainerException {
    log.debug("processAction method in WSRPConsumerPlugin entered");
    if (!init)
      return null;

    ActionOutput output = new ActionOutput();

    configureTemplateComposer(request);

    String portletAppName = input.getInternalWindowID().getPortletApplicationName();
    String portletName = input.getInternalWindowID().getPortletName();
    String uniqueID = input.getInternalWindowID().getUniqueID();

    /* for WSRP Admin Portlet */
    if (WSRPAdminPortletDataImp.isOfferToProcess(portletAppName, portletName)) {
      adminPortlet.getPortletObject().processAction(input, output, request);
      return output;
    }

    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);

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
        String newPortletHandle = portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
        portlet.getPortletContext().setPortletHandle(newPortletHandle);
        UserSessionMgr userSession = getUserSession(request.getSession(),
                                                    portletKey.getProducerId());
        PortletWindowSession windowSession = getWindowSession(portletKey, portlet, userSession, key);

        WSRPInteractionRequest iRequest = getInteractionRequest(input, windowSession);

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
        //        baseURL += org.exoplatform.Constants.COMPONENT_PARAMETER + "=" + portletAppName + Constants.PORTLET_HANDLE_ENCODER + portletName;
        //        // + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
        //        log.debug("use base path : " + baseURL);
        baseURL = input.getBaseURL();

        /* MAIN INVOKE */
        BlockingInteractionResponse iResponse = getPortletDriver(portlet).performBlockingInteraction(iRequest,
                                                                                                     userSession,
                                                                                                     baseURL);
        if (iResponse != null) {
          log.debug("manage BlockingInteractionResponse object content");
          String redirectURL = iResponse.getRedirectURL();
          if (redirectURL != null) {
            log.debug("Redirect action to URL : " + redirectURL);
            if (redirectURL.startsWith("/") || redirectURL.startsWith("http://")
                || redirectURL.startsWith("https://")) {
              output.addProperty(Output.SEND_REDIRECT, redirectURL);
            } else {
              log.error("Can not redirect action: a relative or incorrect path URL is given");
            }
          } else {
            UpdateResponse updateResponse = iResponse.getUpdateResponse();

            if (updateResponse != null) {
              // set events
              output.setEvents(JAXBEventTransformer.getEventsUnmarshal(updateResponse.getEvents()));
              if (windowSession != null) {
                updateSessionContext(updateResponse.getSessionContext(),
                                     windowSession.getPortletSession());
                windowSession.updateMarkupCache(updateResponse.getMarkupContext());
              }
              updatePortletContext(updateResponse.getPortletContext(), portlet);

              processNavigationalContext(output, updateResponse.getNavigationalContext());

              String newMode = updateResponse.getNewMode();
              if (newMode != null) {
                log.debug("set Mode required : " + newMode);
                output.setNextMode(Modes.getJsrPortletMode(newMode));
              }
              String newWindowState = updateResponse.getNewWindowState();
              if (newWindowState != null) {
                log.debug("set new required window state : " + newWindowState);
                output.setNextState(WindowStates.getJsrWindowState(newWindowState));
              }
              //              output.setPortletState(portletState);
              //              output.setSessionMap(map);
            }
          }

        }
        return output;
      } catch (WSRPException e) {
        throw new PortletContainerException("exception in WSRPConsumerPlugin.processAction method",
                                            e);
      }
    }
    return output;
  }

  private void processNavigationalContext(EventOutput output,
                                          NavigationalContext navigationalContext) {
    if (navigationalContext != null) {
      String navState = navigationalContext.getOpaqueValue();
      if (navState != null) {
        log.debug("set new navigational state : " + navState);
        output.setRenderParameter(WSRPConstants.WSRP_NAVIGATIONAL_STATE, navState);
      }

      // process public params from array of NamedString to plain string
      List<NamedString> namedStrings = navigationalContext.getPublicValues();
      String navigationalValues = "";
      if (namedStrings != null) {
        for (NamedString namedString : namedStrings) {
          if (navigationalValues != "") {
            navigationalValues += WSRPConstants.NEXT_PARAM;
          }
          navigationalValues += namedString.getName() + "=" + namedString.getValue();
        }
      }
      try {
        navigationalValues = URLEncoder.encode(navigationalValues, "utf-8");
      } catch (Exception e) {
        e.printStackTrace();
      }
      output.setRenderParameter(WSRPConstants.WSRP_NAVIGATIONAL_VALUES, navigationalValues);

      Map<String, String[]> params = Utils.getMapParametersFromNamedStringArray(namedStrings);
      if (params != null) {
        Iterator<String> paramsIter = params.keySet().iterator();
        while (paramsIter.hasNext()) {
          String key = paramsIter.next();
          output.setRenderParameters(key, params.get(key));
        }
      }

    }
  }

  private PortletDriver getPortletDriver(WSRPPortlet portlet) throws WSRPException {
    return consumer.getPortletDriverRegistry().getPortletDriver(portlet);
  }

  private String getProducerID(String portletAppName) {
    if (portletAppName == null)
      return null;
    if (!portletAppName.contains(WSRPConstants.WSRP_PRODUCER_APP_ENCODER))
      return null;
    return portletAppName.substring(0,
                                    portletAppName.indexOf(WSRPConstants.WSRP_PRODUCER_APP_ENCODER));
  }

  public RenderOutput render(HttpServletRequest request,
                             HttpServletResponse response,
                             RenderInput input) throws PortletContainerException {

    // input.isStateSaveOnClient()
    // input.isUpdateCache()
    // input.getUserAttributes()
    // input.getPortletState()
    // input.getPortletPreferencesPersister()
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

    String portletAppName = input.getInternalWindowID().getPortletApplicationName();
    // example for debug: producer2-1006454248@portlets2
    String portletName = input.getInternalWindowID().getPortletName();
    // example for debug: TestPortlet
    String uniqueID = input.getInternalWindowID().getUniqueID();
    // example for debug: -806704985_A629953C5653522BAE25354D61E21B7B

    /* for WSRP Admin Portlet */
    if (WSRPAdminPortletDataImp.isOfferToProcess(portletAppName, portletName)) {
      response.setContentType("text/html");
      adminPortlet.getPortletObject().render(input, output);
      return output;
    }

    String producerID = getProducerID(portletAppName);
    // example for debug: producer2-1006454248
    String portletHandle = getPortletHandle(portletAppName, portletName);
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
            UserSessionMgr userSession = getUserSession(request.getSession(),
                                                        portletKey.getProducerId());
            WSRPPortlet portlet = getPortlet(portletKey, portletHandle);
            // below I add the uniqueID to the portlet handle within PortletContext
            String newPortletHandle = portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
            portlet.getPortletContext().setPortletHandle(newPortletHandle);
            PortletWindowSession portletWindowSession = getWindowSession(portletKey,
                                                                         portlet,
                                                                         userSession,
                                                                         key);

            WSRPMarkupRequest markupRequest = getMarkupRequest(input, portletWindowSession);
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
            MarkupResponse mResponse = getPortletDriver(portlet).getMarkup(markupRequest,
                                                                           userSession,
                                                                           baseURL);
            if (mResponse != null) {
              if (portletWindowSession != null) {
                updateSessionContext(mResponse.getSessionContext(),
                                     portletWindowSession.getPortletSession());
              }
              processMarkupContext(mResponse.getMarkupContext(), output);
            }
            if (portletWindowSession != null) {
              log.debug("Update cache");
              portletWindowSession.updateMarkupCache(null);
            }
            if (input.getTitle() != null) {
              output.setTitle(input.getTitle());
            }
          } catch (Throwable t) {
            log.error("WS Fault occured", t);
            Writer w = response.getWriter();
            w.write("a WSRP Fault occured");
          }
        }
      } else {
        // if WindowState equals MINIMIZED
        if (input.getTitle() != null) {
          output.setTitle(input.getTitle());
        } else {
          LocalizedString locStr = consumer.getProducerRegistry()
                                           .getProducer(producerID)
                                           .getPortletDescription(portletHandle)
                                           .getTitle();
          output.setTitle(Utils.getStringFromLocalizedString(locStr));
        }
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

  private UserSessionMgr getUserSession(HttpSession httpSession, String producerID) throws WSRPException {

    UserSessionMgr userSession = (UserSessionMgr) httpSession.getAttribute(USER_SESSIONS_KEY
        + producerID);
    if (userSession == null) {
      log.debug("Create new UserSession");
      userSession = new UserSessionImpl(getProducer(producerID).getMarkupInterfaceEndpoint());
      httpSession.setAttribute(USER_SESSIONS_KEY + producerID, userSession);
    } else {
      log.debug("Use existing UserSession");
    }
    return userSession;
  }

  private PortletKey getPortletKey(String producerID, String portletHandle) throws PortletException {
    PortletKey portletKey = null;
    Iterator<WSRPPortlet> portlets = consumer.getPortletRegistry().getAllPortlets();
    while (portlets.hasNext()) {
      WSRPPortlet element = portlets.next();
      if (producerID.equals(element.getPortletKey().getProducerId())
          && portletHandle.equals(element.getPortletKey().getPortletHandle())) {
        portletKey = element.getPortletKey();
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

  private WSRPPortlet getPortlet(PortletKey portletKey, String parentHandle) throws WSRPException {
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

  private WSRPPortlet createPortlet(PortletKey portletKey, String parentHandle) {
    WSRPPortlet portlet = new WSRPPortletAdapter(portletKey);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletKey.getPortletHandle());
    //    portletContext.setPortletState(portletState);
    //    portletContext.setScheduledDestruction(scheduledDestruction);
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

  private WSRPInteractionRequest getInteractionRequest(ActionInput input,
                                                       PortletWindowSession portletWindowSession) {
    log.debug("getInteractionRequest entered");
    WSRPInteractionRequestAdapter interactionRequest = new WSRPInteractionRequestAdapter();
    fillMimeRequest(interactionRequest, input, portletWindowSession);
    interactionRequest.setFormParameters(getRenderParametersAsNamedString(input));
    interactionRequest.setInteractionState(getInteractionState(input, portletWindowSession));
    return interactionRequest;
  }

  private String getInteractionState(Input input, PortletWindowSession portletWindowSession) {
    if (input != null && input.getRenderParameters() != null) {
      String[] interactionState = input.getRenderParameters()
                                       .get(WSRPConstants.WSRP_INTERACTION_STATE);
      if (interactionState != null && interactionState.length != 0) {
        log.debug("user interaction state : " + interactionState[0]);
        portletWindowSession.setInteractionState(interactionState[0]);
      } else {
        log.debug("Interaction state is null");
      }
    } else {
      log.debug("Input input is null or input.getRenderParameters() is null");
    }
    return portletWindowSession.getInteractionState();
  }

  // Store only parameters without the "wsrp-" prefix
  private List<NamedString> getRenderParametersAsNamedString(Input input) {
    Map<String, String[]> params = input.getRenderParameters();
    return Utils.getNamedStringListParametersFromMap(params, true);
  }

  private void fillMimeRequest(WSRPBaseRequestAdapter baseRequest,
                               Input input,
                               PortletWindowSession portletWindowSession) {
    baseRequest.setMarkupCharacterSets(characterEncodings);
    baseRequest.setClientData(getClientData());

    List<Locale> locales = input.getLocales();
    List<String> localesStrings = new ArrayList<String>();
    if (locales != null) {
      for (Locale locale : locales) {
        localesStrings.add(locale.toString());
      }
      baseRequest.getLocales().addAll(localesStrings);
    } else {
      baseRequest.setLocales(SUPPORTED_LOCALES);//new String[]{Locale.getDefault().getDisplayLanguage()});
    }

    Collection<String> mimeTypes = pcConf.getSupportedContent();
    baseRequest.getMimeTypes().addAll(mimeTypes);

    baseRequest.setMode(Modes.getWSRPModeString(input.getPortletMode()));
    baseRequest.setValidNewModes(null);
    baseRequest.setWindowState(WindowStates.getWSRPStateString(input.getWindowState()));
    baseRequest.setValidNewWindowStates(null);

    baseRequest.setSecureClientCommunication(false);
    baseRequest.setUserAuthentication("none");
    baseRequest.setExtensions(null);

    // Set fields for NavigationalContext from specified url params
    baseRequest.setNavigationalState(getNavigationalState(input, portletWindowSession));
    baseRequest.setNavigationalValues(getNavigationalValues(input, portletWindowSession));

    // Rewrite public params when they are present in input
    if (input.getPublicParameterMap() != null)
      baseRequest.setNavigationalValues(Utils.getNamedStringListParametersFromMap(input.getPublicParameterMap()));

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
    clientData.setCcppHeaders(null);
    clientData.getClientAttributes().addAll(null);
    clientData.setRequestVerb(null);
    return clientData;
  }

  private String getNavigationalState(Input input, PortletWindowSession portletWindowSession) {
    if (input != null && input.getRenderParameters() != null) {
      String[] navigationalState = input.getRenderParameters()
                                        .get(WSRPConstants.WSRP_NAVIGATIONAL_STATE);
      if (navigationalState != null && navigationalState.length != 0) {
        log.debug("user navigational state : " + navigationalState[0]);
        portletWindowSession.setNavigationalState(navigationalState[0]);
      } else {
        log.debug("Navigational state is null or empty");
      }
    } else {
      log.debug("Input input is null or input.getRenderParameters() is null");
    }
    return portletWindowSession.getNavigationalState();
  }

  private List<NamedString> getNavigationalValues(Input input,
                                                  PortletWindowSession portletWindowSession) {
    List<NamedString> resultArray = null;

    if (input.getRenderParameters() != null
        && input.getRenderParameters().get(WSRPConstants.WSRP_NAVIGATIONAL_VALUES) != null) {
      String parameterNavigationalValues = input.getRenderParameters()
                                                .get(WSRPConstants.WSRP_NAVIGATIONAL_VALUES)[0];
      if (parameterNavigationalValues != null && parameterNavigationalValues != "") {
        String parameterNavigationalValuesDec = null;
        try {
          parameterNavigationalValuesDec = URLDecoder.decode(parameterNavigationalValues, "utf-8");
        } catch (Exception e) {
          e.printStackTrace();
        }
        if (parameterNavigationalValuesDec != null) {
          String[] navigationalValues = parameterNavigationalValuesDec.split(WSRPConstants.NEXT_PARAM);
          // from String[] to List<NamedString> converting navigational values
          if (navigationalValues != null) {
            List<NamedString> navigationalValuesList = new ArrayList<NamedString>(navigationalValues.length);
            for (String navigationalValue : navigationalValues) {
              navigationalValuesList.add(new NamedStringWrapper(navigationalValue, null));
            }
            resultArray = navigationalValuesList;
          }
        }
      }
    }
    if (resultArray != null) {
      if (log.isDebugEnabled())
        log.debug("user navigational values : " + resultArray);
      portletWindowSession.setNavigationalValues(resultArray);
    } else {
      log.debug("Navigational values are null");
    }
    return portletWindowSession.getNavigationalValues();
  }

  private void updateSessionContext(SessionContext sessionContext, PortletSession portletSession) {
    if (sessionContext != null) {
      log.debug("update session context");
      if (portletSession != null) {
        portletSession.setSessionContext(sessionContext);
      }
    }
  }

  private void updatePortletContext(PortletContext portletContext, WSRPPortlet portlet) throws WSRPException {
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

  private WSRPMarkupRequest getMarkupRequest(RenderInput input,
                                             PortletWindowSession portletWindowSession) {
    WSRPMarkupRequestAdapter markupRequest = new WSRPMarkupRequestAdapter();
    fillMimeRequest(markupRequest, input, portletWindowSession);
    markupRequest.setCachedMarkup(portletWindowSession.getCachedMarkup());
    return markupRequest;
  }

  private void processMarkupContext(MarkupContext markupContext, RenderOutput output) throws WSRPException {
    log.debug("process markup context for returned markup");
    if (markupContext != null && output != null) {
      processMimeResponse(markupContext, output);
      output.setNextPossiblePortletModes(getPortletModesFromStrings(markupContext.getValidNewModes()));
      String title = markupContext.getPreferredTitle();
      if (title != null) {
        log.debug("user title : " + title);
        output.setTitle(title);
      }
    }
  }

  // Below changes of WSRP2 spec

  private Collection<PortletMode> getPortletModesFromStrings(List<String> validNewModes) {
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

    log.debug("serveResource method in WSRPConsumerPlugin entered");
    if (!init)
      return null;

    ResourceOutput output = new ResourceOutput();

    configureTemplateComposer(request);

    String portletAppName = input.getInternalWindowID().getPortletApplicationName();
    String portletName = input.getInternalWindowID().getPortletName();
    String uniqueID = input.getInternalWindowID().getUniqueID();

    /* for WSRP Admin Portlet */
    if (WSRPAdminPortletDataImp.isOfferToProcess(portletAppName, portletName)) {
      response.setContentType("text/html");
      adminPortlet.getPortletObject().serveResource(input, output);
      return output;
    }

    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);

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
            UserSessionMgr userSession = getUserSession(request.getSession(),
                                                        portletKey.getProducerId());
            WSRPPortlet portlet = getPortlet(portletKey, portletHandle);
            // below I add the uniqueID to the portlet handle within PortletContext
            String newPortletHandle = portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
            portlet.getPortletContext().setPortletHandle(newPortletHandle);
            PortletWindowSession portletWindowSession = getWindowSession(portletKey,
                                                                         portlet,
                                                                         userSession,
                                                                         key);

            WSRPResourceRequest resourceRequest = getResourceRequest(input, portletWindowSession);

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
            ResourceResponse resResponse = getPortletDriver(portlet).getResource(resourceRequest,
                                                                                 userSession,
                                                                                 baseURL);

            if (resResponse != null) {
              if (portletWindowSession != null) {
                updateSessionContext(resResponse.getSessionContext(),
                                     portletWindowSession.getPortletSession());
              }
              processResourceContext(resResponse.getResourceContext(), output);
            }
            if (portletWindowSession != null) {
              log.debug("Update cache");
              portletWindowSession.updateMarkupCache(null);
            }
            if (input.getTitle() != null) {
              output.setTitle(input.getTitle());
            }
          } catch (Throwable t) {
            log.error("WS Fault occured", t);
            Writer w = response.getWriter();
            w.write("a WSRP Fault occured");
          }
        }
      } else {
        // if WindowState equals MINIMIZED
        if (input.getTitle() != null) {
          output.setTitle(input.getTitle());
        } else {
          LocalizedString locStr = consumer.getProducerRegistry()
                                           .getProducer(producerID)
                                           .getPortletDescription(portletHandle)
                                           .getTitle();
          output.setTitle(Utils.getStringFromLocalizedString(locStr));
        }
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

  private void configureTemplateComposer(HttpServletRequest request) {
    templateComposer.setHost(request.getServerName());
    templateComposer.setPort(request.getServerPort());
  }

  private WSRPResourceRequest getResourceRequest(ResourceInput input,
                                                 PortletWindowSession portletWindowSession) {
    WSRPResourceRequestAdapter resourceRequest = new WSRPResourceRequestAdapter();
    fillMimeRequest(resourceRequest, input, portletWindowSession);
    // fill resource params
    resourceRequest.setFormParameters(getRenderParametersAsNamedString(input));
    resourceRequest.setResourceID(input.getResourceID());
    resourceRequest.setResourceState(getResourceState(input, portletWindowSession));
    resourceRequest.setResourceCacheability(input.getCacheability());

    // if cached is set we don't call producer for this method
    resourceRequest.setCachedResource(portletWindowSession.getCachedResource());

    // resourceRequest.setUploadContexts(uploadContexts);
    // resourceRequest.setPortletStateChange(portletStateChange);
    return resourceRequest;
  }

  private String getResourceState(Input input, PortletWindowSession portletWindowSession) {
    if (input != null && input.getRenderParameters() != null) {
      String[] resourceState = input.getRenderParameters().get(WSRPConstants.WSRP_RESOURCE_STATE);
      if (resourceState != null && resourceState.length != 0) {
        log.debug("user resource state : " + resourceState[0]);
        portletWindowSession.setResourceState(resourceState[0]);
      } else {
        log.debug("Resource state is null");
      }
    } else {
      log.debug("Input input is null or input.getRenderParameters() is null");
    }
    return portletWindowSession.getResourceState();
  }

  private void processResourceContext(ResourceContext resourceContext, ResourceOutput output) throws WSRPException {
    log.debug("process resource context for returned markup");
    if (resourceContext != null && output != null) {
      processMimeResponse(resourceContext, output);
    }
  }

  private void processMimeResponse(MimeResponse mimeResponse, RenderOutput output) throws WSRPException {
    log.debug("process resource context for returned markup");
    if (mimeResponse != null && output != null) {
      // resourceContext.getCacheControl()
      // resourceContext.getCcppProfileWarning()
      // resourceContext.getExtensions()
      // resourceContext.getLocale()
      // resourceContext.getRequiresRewriting()
      // resourceContext.getUseCachedItem()
      if (mimeResponse.getMimeType() != null) {
        output.setContentType(mimeResponse.getMimeType());
      }

      // process content
      byte[] binaryMarkup = mimeResponse.getItemBinary();
      if (binaryMarkup == null && mimeResponse.getItemString() != null) {
        log.debug("string markup not null");
        try {
          binaryMarkup = mimeResponse.getItemString().getBytes("utf-8");
        } catch (java.io.UnsupportedEncodingException e) {
          binaryMarkup = mimeResponse.getItemString().getBytes();
        }
      } else {
        log.debug("binary markup not null");
      }
      output.setContent(binaryMarkup);
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

    String portletAppName = input.getInternalWindowID().getPortletApplicationName();
    String portletName = input.getInternalWindowID().getPortletName();
    String uniqueID = input.getInternalWindowID().getUniqueID();

    /* for WSRP Admin Portlet */
    if (WSRPAdminPortletDataImp.isOfferToProcess(portletAppName, portletName)) {
      adminPortlet.getPortletObject().processEvent(input, output);
      return output;
    }

    String producerID = getProducerID(portletAppName);
    String portletHandle = getPortletHandle(portletAppName, portletName);

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
        String newPortletHandle = portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
        portlet.getPortletContext().setPortletHandle(newPortletHandle);

        UserSessionMgr userSession = getUserSession(request.getSession(),
                                                    portletKey.getProducerId());
        PortletWindowSession windowSession = getWindowSession(portletKey, portlet, userSession, key);

        WSRPEventsRequest iRequest = getEventsRequest(input, windowSession);

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
        HandleEventsResponse iResponse = getPortletDriver(portlet).handleEvents(iRequest,
                                                                                userSession,
                                                                                baseURL);

        if (iResponse != null) {
          log.debug("manage BlockingInteractionResponse object content");
          UpdateResponse updateResponse = iResponse.getUpdateResponse();
          if (updateResponse != null) {
            // set events
            output.setEvents(JAXBEventTransformer.getEventsUnmarshal(updateResponse.getEvents()));
            if (windowSession != null) {
              updateSessionContext(updateResponse.getSessionContext(),
                                   windowSession.getPortletSession());
              windowSession.updateMarkupCache(updateResponse.getMarkupContext());
            }
            updatePortletContext(updateResponse.getPortletContext(), portlet);

            processNavigationalContext(output, updateResponse.getNavigationalContext());

            String newMode = updateResponse.getNewMode();
            if (newMode != null) {
              log.debug("set Mode required : " + newMode);
              output.setNextMode(Modes.getJsrPortletMode(newMode));
            }
            String newWindowState = updateResponse.getNewWindowState();
            if (newWindowState != null) {
              log.debug("set new required window state : " + newWindowState);
              output.setNextState(WindowStates.getJsrWindowState(newWindowState));
            }
          }
        }
        return output;
      } catch (WSRPException e) {
        e.printStackTrace();
        throw new PortletContainerException("exception in WSRPConsumerPlugin.processEvent method",
                                            e);
      }
    }
    return output;
  }

  private WSRPEventsRequest getEventsRequest(EventInput input,
                                             PortletWindowSession portletWindowSession) {
    WSRPEventsRequestAdapter eventRequest = new WSRPEventsRequestAdapter();
    fillMimeRequest(eventRequest, input, portletWindowSession);
    eventRequest.getEvents().add(JAXBEventTransformer.getEventMarshal(input.getEvent()));
    return eventRequest;
  }

  /**
   * Get portlet app names.
   * 
   * @return collection of string
   */
  public final Collection<String> getPortletAppNames() {
    log.debug("getPortletAppNames() entered");
    Collection<String> result = new HashSet<String>();
    // put WSRPAdminPortlet
    result.add(WSRPConstants.WSRP_ADMIN_PORTLET_APP);
    // put all remote portlets
    String producerId = null;
    String portletHandle = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    ServiceDescription desc = null;
    while (i.hasNext()) {
      Producer producer = i.next();
      try {
        desc = producer.getServiceDescription();
      } catch (WSRPException e) {
        e.printStackTrace();
      }
      producerId = producer.getID();
      List<PortletDescription> portletDescriptions = desc.getOfferedPortlets();
      for (PortletDescription portletDescription : portletDescriptions) {
        portletHandle = portletDescription.getPortletHandle();
        if (StringUtils.split(portletHandle, "/").length == 1) {
          portletHandle = "unnamed" + "/" + portletHandle;
        }
        String newPortletHandle = producerId + WSRPConstants.WSRP_PRODUCER_APP_ENCODER
            + portletHandle;
        String[] ss = StringUtils.split(newPortletHandle, "/");
        String portletAppName = ss[0];
        result.add(portletAppName);
      }
    }
    return result;
  }

}
