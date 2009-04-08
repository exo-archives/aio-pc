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

package org.exoplatform.services.wsrp2.consumer.impl;

import java.io.Serializable;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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
import org.exoplatform.services.wsrp2.utils.LocaleUtils;
import org.exoplatform.services.wsrp2.utils.Modes;
import org.exoplatform.services.wsrp2.utils.Utils;
import org.exoplatform.services.wsrp2.utils.WindowStates;

/**
 * Based on WSRPConsumerPortlet written by Benjamin Mestrallet Author : Roman
 * Pedchenko roman.pedchenko@exoplatform.com.ua Author : Alexey Zavizionov
 * alexey.zavizionov@exoplatform.com.ua
 */

public class WSRPConsumerPlugin implements PortletContainerPlugin {

  private List<String>              characterEncodings = new ArrayList<String>();

  private List<String>              mimeTypes          = new ArrayList<String>();

  public List<String>               SUPPORTED_LOCALES  = new ArrayList<String>();

  private static final String       consumerAgent      = WSRPConstants.DEFAULT_consumerAgentName;

  private static final String       userAgent          = "userAgent";

  protected WSRPAdminPortletDataImp adminPortlet       = null;

  public static final String        USER_SESSIONS_KEY  = "user_session_key";

  private ExoContainer              container;

  private ConsumerEnvironment       consumer;

  private PortletContainerService   pcService;

  private PortletContainerConf      pcConf;

  private WSRPConfiguration         conf;

  private static final Log          LOG                = ExoLogger.getLogger(WSRPConsumerPlugin.class);

  private String                    pluginName;

  private static boolean            init               = false;

  public WSRPConsumerPlugin(ExoContainerContext context,
                            ConsumerEnvironment consumer,
                            PortletContainerService pcService,
                            PortletContainerConf pcConf,
                            WSRPConfiguration conf) {
    this.container = context.getContainer();
    this.consumer = consumer;
    this.pcService = pcService;
    this.pcConf = pcConf;
    this.conf = conf;
    this.characterEncodings = Arrays.asList("UTF-8");
    this.mimeTypes = Arrays.asList("text/html", "text/wml");
    this.SUPPORTED_LOCALES = Arrays.asList("en", "fr");
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
    if (LOG.isDebugEnabled())
      LOG.debug("WSRPConsumerPlugin.initConsumer");
    consumer.setCharacterEncodingSet(characterEncodings);
    consumer.setConsumerAgent(consumerAgent);
    consumer.setMimeTypes(mimeTypes);
    consumer.setPortletStateChange(conf.getPortletStateChange() != null ? conf.getPortletStateChange()
                                                                       : StateChange.READ_WRITE);
    // TODO modes and states should be getting from producer
    // consumer.setSupportedModes(getPortletModes(Collections.list(pcConf.getSupportedPortletModes())));
    List<String> supportedPortletModes = getPortletModes(pcService.getSupportedPortletModes());
    if (supportedPortletModes != null)
      consumer.getSupportedModes().addAll(supportedPortletModes);
    // consumer.setSupportedWindowStates(getWindowStates(Collections.list(pcConf.getSupportedWindowStates())));
    consumer.getSupportedWindowStates()
            .addAll(getWindowStates(pcService.getSupportedWindowStates()));
    consumer.setUserAuthentication(WSRPConstants.AUTH_NO_USER_AUTHENTIFICATION);
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
    }
    return array;
  }

  private List<String> getWindowStates(Collection<WindowState> supportedWindowStates) {
    List<String> array = new ArrayList<String>();
    int i = 0;
    for (Iterator<WindowState> iterator = supportedWindowStates.iterator(); iterator.hasNext(); i++) {
      WindowState windowState = iterator.next();
      array.add(windowState.toString());
    }
    return array;
  }

  public Collection<PortletMode> getSupportedPortletModes() {
    if (!init) {
      return null;
    }
    List<PortletMode> result = null;

    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    if (i == null)
      return result;

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
    List<WindowState> result = null;

    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    if (i == null)
      return result;

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
    String pcFullPortletHandle = getPortletHandle(portletAppName, portletName, null);

    if (!init)
      return null;

    Collection<PortletMode> result = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    if (i == null)
      return result;

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
              if (portletHandleTemp.equalsIgnoreCase(pcFullPortletHandle)) {
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
    String pcFullPortletHandle = getPortletHandle(portletAppName, portletName, null);

    if (!init)
      return null;

    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> producers = pregistry.getAllProducers();
    if (producers == null)
      return null;

    while (producers.hasNext()) {
      Producer producer = producers.next();
      if (producer.getID().equalsIgnoreCase(producerID)) {
        try {
          ServiceDescription sD = producer.getServiceDescription();
          List<PortletDescription> portletDescriptions = sD.getOfferedPortlets();
          if (portletDescriptions != null) {
            for (PortletDescription portletDescription : portletDescriptions) {
              if (portletDescription.getPortletHandle().equalsIgnoreCase(pcFullPortletHandle)) {
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

  /**
   * Returns 'portletApp/portletName' from 'producerID@portletApp' and
   * 'portletName'.
   * 
   * @param pcPortletAppName
   * @param pcPortletName
   * @param pcUniqueID TODO
   * @return
   */
  private String getPortletHandle(String pcPortletAppName, String pcPortletName, String pcUniqueID) {
    if (pcPortletAppName == null || pcPortletName == null)
      return null;
    String result = null;
    // add portlet App Name
    int idxProducerHhdle = pcPortletAppName.indexOf(WSRPConstants.PRODUCER_HANDLE_ENCODER);
    if (idxProducerHhdle != -1)
      result = pcPortletAppName.substring(idxProducerHhdle + 1);
    else
      result = pcPortletAppName;
    // add portlet name
    result += Constants.PORTLET_HANDLE_ENCODER + pcPortletName;
    // add unique id if not null
    if (pcUniqueID != null) {
      result += Constants.PORTLET_HANDLE_ENCODER + pcUniqueID;
    }
    if (LOG.isDebugEnabled())
      LOG.debug("WSRPConsumerPlugin.getPortletHandle() result ");
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
    String pcFullPortletHandle = getPortletHandle(portletAppName, portletName, null);

    if (!init)
      return null;

    ArrayList<WindowState> result = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    if (i == null)
      return result;

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
              if (portletHandleTemp.equalsIgnoreCase(pcFullPortletHandle)) {
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
            if (LOG.isDebugEnabled())
              LOG.debug("Event loaded class for eventName: '" + eventName + "' is: " + clazz);
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
    LOG.debug("getAllPortletMetaData() entered");
    // similar to PortletApplicationsHolder.getAllPortletMetaData()
    Map<String, PortletData> result = new HashMap<String, PortletData>();
    // put WSRPAdminPortlet
    result.put(WSRPConstants.WSRP_ADMIN_PORTLET_KEY, adminPortlet);
    // put all remote portlets
    String producerId = null;
    String portletHandle = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> producers = pregistry.getAllProducers();
    if (producers == null)
      return result;

    while (producers.hasNext()) {
      Producer producer = producers.next();
      try {
        ServiceDescription desc = producer.getServiceDescription();
        producerId = producer.getID();
        List<PortletDescription> portletDescriptions = desc.getOfferedPortlets();
        if (portletDescriptions != null) {
          for (PortletDescription portletDescription : portletDescriptions) {
            portletHandle = portletDescription.getPortletHandle();
            // Create new Portlet remote instance
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
                if (markupType.getModes() != null) {
                  supports.getPortletMode().addAll(markupType.getModes());
                }
                if (markupType.getWindowStates() != null)
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

            // resulted portlet handle looks like : producer2-1006454248@portlets2/TestPortlet/806704985_A629953C5653522BAE25354D61E21B7B

            result.put(producerId + WSRPConstants.PRODUCER_HANDLE_ENCODER + portletHandle,
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
    LOG.debug("getBundle method in WSRPConsumerPlugin entered");
    MapResourceBundle bundle = new MapResourceBundle(locale);
    /* For WSRP Admin Portlet - request handle ! */
    if (portletAppName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_APP)) {
      if (portletName.equals(WSRPConstants.WSRP_ADMIN_PORTLET_NAME)) {
        bundle.add(PortletData.PORTLET_TITLE, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        bundle.add(PortletData.PORTLET_SHORT_TITLE, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        bundle.add(PortletData.KEYWORDS, WSRPConstants.WSRP_ADMIN_PORTLET_NAME);
        return bundle;
      }
    }
    String producerID = getProducerID(portletAppName);
    String pcFullPortletHandle = getPortletHandle(portletAppName, portletName, null);

    try {
      WSRPPortlet portlet = getPortlet(producerID, pcFullPortletHandle, null);
      PortletDescription pd = getPortletDescription(portlet);
      bundle.add(PortletData.PORTLET_TITLE, Utils.getStringFromLocalizedString(pd.getTitle()));
      bundle.add(PortletData.PORTLET_SHORT_TITLE,
                 Utils.getStringFromLocalizedString(pd.getShortTitle()));
      if (pd.getKeywords() != null) {
        bundle.add(PortletData.KEYWORDS,
                   pd.getKeywords().isEmpty() ? ""
                                             : Utils.getStringFromLocalizedString(pd.getKeywords()
                                                                                    .get(0)));
      } else {
        bundle.add(PortletData.KEYWORDS, null);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bundle;
  }

  public void setPortletPreference(Input input, Map<String, String> preferences) throws PortletContainerException {
    //TODO to call remote PortletManagementInterface.setPortletProperties
  }

  public Map<String, String[]> getPortletPreference(Input input) {
    // TODO to call remote PortletManagementInterface.getPortletProperties
    return null;
  }

  public PortletPreferences getPortletPreferences(Input input) {
    // TODO to call remote PortletManagementInterface.getPortletProperties
    return null;
  }

  public void setPortletPreference2(Input input, Map<String, String[]> preferences) throws PortletContainerException {
    //TODO to call remote PortletManagementInterface.setPortletProperties
  }

  public void setPortletPreferences(Input input, PortletPreferences preferences) throws PortletContainerException {
    //TODO to call remote PortletManagementInterface.setPortletProperties
  }

  public ActionOutput processAction(HttpServletRequest request,
                                    HttpServletResponse response,
                                    ActionInput input) throws PortletContainerException {
    LOG.debug("processAction method in WSRPConsumerPlugin entered");
    if (!init)
      return null;

    ActionOutput output = new ActionOutput();

    // portletAppName mixed with producer id
    String pcPortletAppName = input.getInternalWindowID().getPortletApplicationName();
    // example for debug: producer2-1006454248@portlets2
    String pcPortletName = input.getInternalWindowID().getPortletName();
    // example for debug: TestPortlet
    String pcUniqueID = input.getInternalWindowID().getUniqueID();
    // example for debug: -806704985_A629953C5653522BAE25354D61E21B7B

    /* For WSRP Admin Portlet - request handle ! */
    if (WSRPAdminPortletDataImp.isOfferToProcess(pcPortletAppName, pcPortletName)) {
      adminPortlet.getPortletObject().processAction(input, output, request);
      return output;
    }

    String producerID = getProducerID(pcPortletAppName);
    String pcFullPortletHandle = getPortletHandle(pcPortletAppName, pcPortletName, pcUniqueID);

    // TODO get wsrp portlet handle from special request parameter if it was an URL request
    Map<String, String> parsedParams = parseParams(input.getRenderParameters());

    String wsrpPortletHandle = null;// parsedParams.get(WSRPConstants.WSRP_PORTLET_HANDLE);

    // Get WSRP Portlet instance
    WSRPPortlet portlet = getPortlet(producerID, pcFullPortletHandle, null);

    if (getProducer(portlet.getPortletKey().getProducerId()) == null) {
      throw new PortletContainerException("There is no producer instance with id = '"
          + portlet.getPortletKey().getProducerId() + "'");
    }

    try {
      String windowID = pcUniqueID;
      LOG.debug("use windowID : " + windowID);

      // processing user identity. 
      // put User to the consumer.getUserRegistry()
      String userID = input.getInternalWindowID().getOwner(); // default: "portal#anon"
      if (userID == null) {
        userID = "portal#anon";
      }
      User user = getUser(userID, request);

      UserSessionMgr userSession = getUserSession(request.getSession(), portlet.getPortletKey()
                                                                               .getProducerId());
      PortletWindowSession portletWindowSession = getWindowSession(portlet, userSession, windowID);

      WSRPInteractionRequest iRequest = getInteractionRequest(input, portletWindowSession, request);

      String baseURL = null;
      //        baseURL = request.getRequestURI();
      //        LOG.debug("User path info : " + baseURL);
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
      //        LOG.debug("use base path : " + baseURL);
      baseURL = input.getBaseURL();

      /* MAIN INVOKE */
      BlockingInteractionResponse iResponse = getPortletDriver(portlet).performBlockingInteraction(iRequest,
                                                                                                   userSession,
                                                                                                   baseURL);
      if (iResponse != null) {
        LOG.debug("manage BlockingInteractionResponse object content");
        String redirectURL = iResponse.getRedirectURL();
        if (redirectURL != null) {
          LOG.debug("Redirect action to URL : " + redirectURL);
          if (redirectURL.startsWith("/") || redirectURL.startsWith("http://")
              || redirectURL.startsWith("https://")) {
            output.addProperty(Output.SEND_REDIRECT, redirectURL);
          } else {
            LOG.error("Can not redirect action: a relative or incorrect path URL is given");
          }
        } else {
          UpdateResponse updateResponse = iResponse.getUpdateResponse();

          if (updateResponse != null) {
            // set events
            output.setEvents(JAXBEventTransformer.getEventsUnmarshal(updateResponse.getEvents()));
            if (portletWindowSession != null) {
              // store session context
              if (needSessionUpdate(portletWindowSession.getPortletSession().getSessionContext(),
                                    updateResponse.getSessionContext())) {
                updateSessionContext(updateResponse.getSessionContext(),
                                     portletWindowSession.getPortletSession());
              }
              // update markup cache 
              portletWindowSession.updateMarkupCache(updateResponse.getMarkupContext());
            }

            updatePortletContext(updateResponse.getPortletContext(), portlet);

            processNavigationalContext(output, updateResponse.getNavigationalContext());

            String newMode = updateResponse.getNewMode();
            if (newMode != null) {
              LOG.debug("set Mode required : " + newMode);
              output.setNextMode(Modes.getJsrPortletMode(newMode));
            }
            String newWindowState = updateResponse.getNewWindowState();
            if (newWindowState != null) {
              LOG.debug("set new required window state : " + newWindowState);
              output.setNextState(WindowStates.getJsrWindowState(newWindowState));
            }
          }
        }

      }
      return output;
    } catch (WSRPException e) {
      throw new PortletContainerException("Exception in WSRPConsumerPlugin.processAction method", e);
    }

  }

  public RenderOutput render(HttpServletRequest request,
                             HttpServletResponse response,
                             RenderInput input) throws PortletContainerException {

    LOG.debug("Render method in WSRPConsumerPlugin entered");
    if (!init)
      return null;

    RenderOutput output = new RenderOutput();

    // portletAppName mixed with producer id
    String pcPortletAppName = input.getInternalWindowID().getPortletApplicationName();
    // example for debug: producer2-1006454248@portlets2
    String pcPortletName = input.getInternalWindowID().getPortletName();
    // example for debug: TestPortlet
    String pcUniqueID = input.getInternalWindowID().getUniqueID();
    // example for debug: -806704985_A629953C5653522BAE25354D61E21B7B

    /* For WSRP Admin Portlet - request handle ! */
    if (WSRPAdminPortletDataImp.isOfferToProcess(pcPortletAppName, pcPortletName)) {
      response.setContentType("text/html");
      adminPortlet.getPortletObject().render(input, output);
      return output;
    }

    String producerID = getProducerID(pcPortletAppName);
    String pcFullPortletHandle = getPortletHandle(pcPortletAppName, pcPortletName, pcUniqueID);

    // TODO get wsrp portlet handle from special request parameter if it was an URL request
    Map<String, String> parsedParams = parseParams(input.getRenderParameters());

    // Get WSRP Portlet instance
    WSRPPortlet portlet = getPortlet(producerID, pcFullPortletHandle, null);

    if (getProducer(portlet.getPortletKey().getProducerId()) == null) {
      throw new PortletContainerException("There is no producer instance with id = '"
          + portlet.getPortletKey().getProducerId() + "'");
    }

    try {
      WindowState state = input.getWindowState();
      if (!state.equals(WindowState.MINIMIZED)) {

        String windowID = pcUniqueID;
        LOG.debug("use windowID : " + windowID);

        // processing user identity. 
        // put User to the consumer.getUserRegistry()
        String userID = input.getInternalWindowID().getOwner(); // default: "portal#anon"
        if (userID == null) {
          userID = "portal#anon";
        }
        User user = getUser(userID, request);

        try {
          UserSessionMgr userSession = getUserSession(request.getSession(), portlet.getPortletKey()
                                                                                   .getProducerId());

          // below I add the uniqueID to the portlet handle within PortletContext
//            String newPortletHandle = portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
//            portlet.getPortletContext().setPortletHandle(newPortletHandle);

          PortletWindowSession portletWindowSession = getWindowSession(portlet,
                                                                       userSession,
                                                                       windowID);

          WSRPMarkupRequest markupRequest = getMarkupRequest(input, portletWindowSession, request);
          String baseURL = null;
          //            baseURL = request.getRequestURI();
          //            LOG.debug("User path info : " + baseURL);
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
          //            LOG.debug("use base path : " + baseURL);
          baseURL = input.getBaseURL();

          /* MAIN INVOKE */
          MarkupResponse mResponse = getPortletDriver(portlet).getMarkup(markupRequest,
                                                                         userSession,
                                                                         baseURL);
          if (mResponse != null) {

            // store session context
            if (portletWindowSession != null) {
              if (needSessionUpdate(portletWindowSession.getPortletSession().getSessionContext(),
                                    mResponse.getSessionContext())) {

                updateSessionContext(mResponse.getSessionContext(),
                                     portletWindowSession.getPortletSession());
              }
            }
            // update markup cache if doesn't use cached item
            if (!mResponse.getMarkupContext().isUseCachedItem() && portletWindowSession != null) {
              LOG.debug("Update cache");
              portletWindowSession.updateMarkupCache(mResponse.getMarkupContext());
              portletWindowSession.setMode(markupRequest.getMode());
              portletWindowSession.setWindowState(markupRequest.getWindowState());
            }
            // write output
            processMarkupContext(mResponse.getMarkupContext(), output);
          }

          if (input.getTitle() != null) {
            output.setTitle(input.getTitle());
          }
        } catch (Throwable t) {
          LOG.error("WS Fault occured", t);
          Writer w = response.getWriter();
          w.write("a WSRP Fault occured");
        }

      } else {
        // if WindowState equals MINIMIZED
        if (input.getTitle() != null) {
          output.setTitle(input.getTitle());
        } else {
          LocalizedString locStr = getPortletDescription(portlet).getTitle();
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

  public ResourceOutput serveResource(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ResourceInput input) throws PortletContainerException {

    LOG.debug("serveResource method in WSRPConsumerPlugin entered");
    if (!init)
      return null;

    ResourceOutput output = new ResourceOutput();

    // portletAppName mixed with producer id
    String pcPortletAppName = input.getInternalWindowID().getPortletApplicationName();
    // example for debug: producer2-1006454248@portlets2
    String pcPortletName = input.getInternalWindowID().getPortletName();
    // example for debug: TestPortlet
    String pcUniqueID = input.getInternalWindowID().getUniqueID();
    // example for debug: -806704985_A629953C5653522BAE25354D61E21B7B

    /* For WSRP Admin Portlet - request handle ! */
    if (WSRPAdminPortletDataImp.isOfferToProcess(pcPortletAppName, pcPortletName)) {
      response.setContentType("text/html");
      adminPortlet.getPortletObject().serveResource(input, output);
      return output;
    }

    String producerID = getProducerID(pcPortletAppName);
    String pcFullPortletHandle = getPortletHandle(pcPortletAppName, pcPortletName, pcUniqueID);

    // TODO get wsrp portlet handle from special request parameter if it was an URL request
    Map<String, String> parsedParams = parseParams(input.getRenderParameters());

    // Get WSRP Portlet instance
    WSRPPortlet portlet = getPortlet(producerID, pcFullPortletHandle, null);

    if (getProducer(portlet.getPortletKey().getProducerId()) == null) {
      throw new PortletContainerException("There is no producer instance with id = '"
          + portlet.getPortletKey().getProducerId() + "'");
    }

    try {
      WindowState state = input.getWindowState();
      if (!state.equals(WindowState.MINIMIZED)) {

        String windowID = pcUniqueID;
        LOG.debug("use windowID : " + windowID);

        // processing user identity. 
        // put User to the consumer.getUserRegistry()
        String userID = input.getInternalWindowID().getOwner(); // default: "portal#anon"
        if (userID == null) {
          userID = "portal#anon";
        }
        User user = getUser(userID, request);

        try {
          UserSessionMgr userSession = getUserSession(request.getSession(), portlet.getPortletKey()
                                                                                   .getProducerId());

          // below I add the uniqueID to the portlet handle within PortletContext
//            String newPortletHandle = portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
//            portlet.getPortletContext().setPortletHandle(newPortletHandle);

          PortletWindowSession portletWindowSession = getWindowSession(portlet,
                                                                       userSession,
                                                                       windowID);

          WSRPResourceRequest resourceRequest = getResourceRequest(input,
                                                                   portletWindowSession,
                                                                   request);

          String baseURL = null;
          //            baseURL = request.getRequestURI();
          //            LOG.debug("User path info : " + baseURL);
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
          //            LOG.debug("use base path : " + baseURL);
          baseURL = input.getBaseURL();

          /* MAIN INVOKE */
          ResourceResponse resResponse = getPortletDriver(portlet).getResource(resourceRequest,
                                                                               userSession,
                                                                               baseURL);

          if (resResponse != null) {
            if (portletWindowSession != null) {

              // store session context
              if (needSessionUpdate(portletWindowSession.getPortletSession().getSessionContext(),
                                    resResponse.getSessionContext())) {
                updateSessionContext(resResponse.getSessionContext(),
                                     portletWindowSession.getPortletSession());
              }
              // update resource cache if doesn't use cached item
              if (!resResponse.getResourceContext().isUseCachedItem()
                  && portletWindowSession != null) {
                LOG.debug("Update cache");
                portletWindowSession.updateResourceCache(resResponse.getResourceContext());
              }
            }

            updatePortletContext(resResponse.getPortletContext(), portlet);

            // write output
            processResourceContext(resResponse.getResourceContext(), output);
          }

          if (input.getTitle() != null) {
            output.setTitle(input.getTitle());
          }
        } catch (Throwable t) {
          LOG.error("WS Fault occured", t);
          Writer w = response.getWriter();
          w.write("a WSRP Fault occured");
        }

      } else {
        // if WindowState equals MINIMIZED
        if (input.getTitle() != null) {
          output.setTitle(input.getTitle());
        } else {
          LocalizedString locStr = getPortletDescription(portlet).getTitle();
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

  public EventOutput processEvent(HttpServletRequest request,
                                  HttpServletResponse response,
                                  EventInput input) throws PortletContainerException {
    LOG.debug("processEvent method in WSRPConsumerPlugin entered");

    if (!init)
      return null;

    EventOutput output = new EventOutput();

    // portletAppName mixed with producer id
    String pcPortletAppName = input.getInternalWindowID().getPortletApplicationName();
    // example for debug: producer2-1006454248@portlets2
    String pcPortletName = input.getInternalWindowID().getPortletName();
    // example for debug: TestPortlet
    String pcUniqueID = input.getInternalWindowID().getUniqueID();
    // example for debug: -806704985_A629953C5653522BAE25354D61E21B7B

    /* For WSRP Admin Portlet - request handle ! */
    if (WSRPAdminPortletDataImp.isOfferToProcess(pcPortletAppName, pcPortletName)) {
      adminPortlet.getPortletObject().processEvent(input, output);
      return output;
    }

    String producerID = getProducerID(pcPortletAppName);
    String pcFullPortletHandle = getPortletHandle(pcPortletAppName, pcPortletName, pcUniqueID);

    // TODO get wsrp portlet handle from special request parameter if it was an URL request
    Map<String, String> parsedParams = parseParams(input.getRenderParameters());

    // Get WSRP Portlet instance
    WSRPPortlet portlet = getPortlet(producerID, pcFullPortletHandle, null);

    if (getProducer(portlet.getPortletKey().getProducerId()) == null) {
      throw new PortletContainerException("There is no producer instance with id = '"
          + portlet.getPortletKey().getProducerId() + "'");
    }

    try {
      String windowIDkey = input.getInternalWindowID().getUniqueID();
      LOG.debug("use windowID : " + windowIDkey);

      // processing user identity. 
      // put User to the consumer.getUserRegistry()
      String userID = input.getInternalWindowID().getOwner(); // default: "portal#anon"
      if (userID == null) {
        userID = "portal#anon";
      }
      User user = getUser(userID, request);

      // below I add the uniqueID to the portlet handle within PortletContext
//        String newPortletHandle = portletHandle + Constants.PORTLET_HANDLE_ENCODER + uniqueID;
//        portlet.getPortletContext().setPortletHandle(newPortletHandle);

      UserSessionMgr userSession = getUserSession(request.getSession(), portlet.getPortletKey()
                                                                               .getProducerId());
      PortletWindowSession portletWindowSession = getWindowSession(portlet,
                                                                   userSession,
                                                                   windowIDkey);

      WSRPEventsRequest iRequest = getEventsRequest(input, portletWindowSession, request);

      String baseURL = null;
      //         baseURL = request.getRequestURI();
      //        LOG.debug("User path info : " + baseURL);
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
      //        LOG.debug("use base path : " + baseURL);
      baseURL = input.getBaseURL();

      /* MAIN INVOKE */
      HandleEventsResponse iResponse = getPortletDriver(portlet).handleEvents(iRequest,
                                                                              userSession,
                                                                              baseURL);

      if (iResponse != null) {
        LOG.debug("manage BlockingInteractionResponse object content");
        UpdateResponse updateResponse = iResponse.getUpdateResponse();
        if (updateResponse != null) {
          // set events
          output.setEvents(JAXBEventTransformer.getEventsUnmarshal(updateResponse.getEvents()));
          if (portletWindowSession != null) {
            // store session context
            if (needSessionUpdate(portletWindowSession.getPortletSession().getSessionContext(),
                                  updateResponse.getSessionContext())) {
              updateSessionContext(updateResponse.getSessionContext(),
                                   portletWindowSession.getPortletSession());
            }
            // update markup cache 
            portletWindowSession.updateMarkupCache(updateResponse.getMarkupContext());
          }

          updatePortletContext(updateResponse.getPortletContext(), portlet);

          processNavigationalContext(output, updateResponse.getNavigationalContext());

          String newMode = updateResponse.getNewMode();
          if (newMode != null) {
            LOG.debug("set Mode required : " + newMode);
            output.setNextMode(Modes.getJsrPortletMode(newMode));
          }
          String newWindowState = updateResponse.getNewWindowState();
          if (newWindowState != null) {
            LOG.debug("set new required window state : " + newWindowState);
            output.setNextState(WindowStates.getJsrWindowState(newWindowState));
          }
        }
      }
      return output;
    } catch (WSRPException e) {
      throw new PortletContainerException("exception in WSRPConsumerPlugin.processEvent method", e);
    }

  }

  private String getValueByKey0(String key, Map<String, String[]> renderParameters) {
    String[] ssPortletHandle = renderParameters.get(key);
    if (ssPortletHandle != null) {
      return ssPortletHandle[0];
    } else {
      return null;
    }
  }

  private Map<String, String> parseParams(Map<String, String[]> renderParameters) {
    Map<String, String> newParams = new HashMap<String, String>();

    // GENERIC PARAMS

    newParams.put(WSRPConstants.WSRP_PORTLET_HANDLE,
                  getValueByKey0(WSRPConstants.WSRP_PORTLET_HANDLE, renderParameters));

    newParams.put(WSRPConstants.WSRP_USER_CONTEXT_KEY,
                  getValueByKey0(WSRPConstants.WSRP_USER_CONTEXT_KEY, renderParameters));

    newParams.put(WSRPConstants.WSRP_PORTLET_INSTANCE_KEY,
                  getValueByKey0(WSRPConstants.WSRP_PORTLET_INSTANCE_KEY, renderParameters));

    newParams.put(WSRPConstants.WSRP_SESSION_ID, getValueByKey0(WSRPConstants.WSRP_SESSION_ID,
                                                               renderParameters));

    //WSRPConstants.WSRP_PAGE_STATE
    //WSRPConstants.WSRP_PORTLET_STATES

    // COMMON PARAMS
    //WSRPConstants.WSRP_FRAGMENT_ID

    // RESOURCE PARAMS
    //WSRPConstants.WSRP_RESOURCE_ID
    //WSRPConstants.WSRP_URL
    //WSRPConstants.WSRP_RESOURCE_CACHEABILITY
    //WSRPConstants.WSRP_REQUIRES_REWRITE
    //WSRPConstants.WSRP_PREFER_OPERATION

    return newParams;
  }

  private void processNavigationalContext(EventOutput output,
                                          NavigationalContext navigationalContext) {
    if (navigationalContext != null) {
      String navState = navigationalContext.getOpaqueValue();
      if (navState != null) {
        LOG.debug("set new navigational state : " + navState);
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
    if (!portletAppName.contains(WSRPConstants.PRODUCER_HANDLE_ENCODER))
      return null;
    return portletAppName.substring(0,
                                    portletAppName.indexOf(WSRPConstants.PRODUCER_HANDLE_ENCODER));
  }

  private boolean needSessionUpdate(SessionContext newSessionContext,
                                    SessionContext oldSessionContext) {
    if (newSessionContext == null)
      return false;
    if (newSessionContext.getExpires() != oldSessionContext.getExpires())
      return true;
    if (newSessionContext.getSessionID() != oldSessionContext.getSessionID()) {
      return true;
    } else {
      return false;
    }
  }

  public void sendAttrs(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<String, Object> attrs,
                        String portletApplicationName) throws PortletContainerException {
  }

  private UserSessionMgr getUserSession(HttpSession httpSession, String producerID) throws WSRPException {
    UserSessionMgr userSession = null;
    userSession = (UserSessionMgr) httpSession.getAttribute(USER_SESSIONS_KEY + producerID);
    if (userSession == null) {
      LOG.debug("Create new UserSession");
      userSession = new UserSessionImpl(producerID);
      httpSession.setAttribute(USER_SESSIONS_KEY + producerID, userSession);
    } else {
      LOG.debug("Use existing UserSession");
    }
    return userSession;
  }

//  private UserSessionMgr getUserSession2(HttpSession httpSession, String producerID) throws WSRPException {
//    UserSessionMgr userSession = null;
//    LOG.debug("Create new UserSession");
//    userSession = getProducer(producerID).getUserSession();
//    return userSession;
//  }

  /**
   * Put User to the consumer.getUserRegistry().
   * 
   * @param user TODO
   * @param request
   * @return
   */
  private User getUser(String owner, HttpServletRequest request) {
    User user = null;
//    WindowInfosContainer scontainer = WindowInfosContainer.getInstance();
//    if (scontainer != null) {
    String userKey = owner;//scontainer.getOwner();
    LOG.debug("getUser method with user key : " + userKey);
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
//    }
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

  private PortletWindowSession getWindowSession(WSRPPortlet portlet,
                                                UserSession userSession,
                                                String windowID) throws WSRPException {
    if (userSession != null) {
      LOG.debug("get group session form user session");
      String groupID = getPortletDescription(portlet).getGroupID();
      groupID = groupID == null ? "default" : groupID;
      LOG.debug("   group ID : " + groupID);
      GroupSession groupSession = userSession.getGroupSession(groupID);
      if (groupSession != null) {
        LOG.debug("get portlet session from group session");
        org.exoplatform.services.wsrp2.consumer.PortletSession portletSession = groupSession.getPortletSession(portlet.getPortletHandle());
        LOG.debug("   portlet handle : " + portlet.getPortletHandle());
        if (portletSession != null) {
          LOG.debug("get portlet window session from portlet session");
          PortletWindowSession windowSession = portletSession.getPortletWindowSession(windowID);
          LOG.debug("   windowID : " + windowID);
          LOG.debug("success in extraction of the window session");
          return windowSession;
        } else {
          LOG.error("portlet session was null");
          throw new WSRPException(Faults.INVALID_SESSION_FAULT);
        }
      } else {
        LOG.error("group session was null");
        throw new WSRPException(Faults.INVALID_SESSION_FAULT);
      }
    } else {
      LOG.error("user session was null");
      throw new WSRPException(Faults.INVALID_SESSION_FAULT);
    }
  }

  private PortletDescription getPortletDescription(WSRPPortlet portlet) throws WSRPException {
    LOG.debug("getPortletDescription entered");
    String producerID = portlet.getPortletKey().getProducerId();
    Producer producer = getProducer(producerID);
    PortletDescription portletDesc = producer.getPortletDescription(portlet.getParent());
    if (portletDesc == null) {
      throw new WSRPException(Faults.UNKNOWN_PORTLET_DESCRIPTION);
    }
    return portletDesc;
  }

  private WSRPInteractionRequest getInteractionRequest(ActionInput input,
                                                       PortletWindowSession portletWindowSession,
                                                       HttpServletRequest request) {
    LOG.debug("getInteractionRequest entered");
    WSRPInteractionRequestAdapter interactionRequest = new WSRPInteractionRequestAdapter();
    fillMimeRequest(interactionRequest, input, portletWindowSession, request);
    interactionRequest.setFormParameters(getRenderParametersAsNamedString(input));
    interactionRequest.setInteractionState(getInteractionState(input, portletWindowSession));
    return interactionRequest;
  }

  private String getInteractionState(Input input, PortletWindowSession portletWindowSession) {
    if (input != null && input.getRenderParameters() != null) {
      String[] interactionState = input.getRenderParameters()
                                       .get(WSRPConstants.WSRP_INTERACTION_STATE);
      if (interactionState != null && interactionState.length != 0) {
        LOG.debug("user interaction state : " + interactionState[0]);
        portletWindowSession.setInteractionState(interactionState[0]);
      } else {
        LOG.debug("Interaction state is null");
      }
    } else {
      LOG.debug("Input input is null or input.getRenderParameters() is null");
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
                               PortletWindowSession portletWindowSession,
                               HttpServletRequest request) {
    baseRequest.setMarkupCharacterSets(characterEncodings);
    baseRequest.setClientData(getClientData());

    baseRequest.getLocales().addAll(LocaleUtils.processLocalesToStrings(input.getLocales(),
                                                                        SUPPORTED_LOCALES));

    Collection<String> mimeTypes = pcConf.getSupportedContent();
    if (mimeTypes != null)
      baseRequest.getMimeTypes().addAll(mimeTypes);

    baseRequest.setMode(Modes.getWSRPModeString(input.getPortletMode()));
    baseRequest.setValidNewModes(null);
    baseRequest.setWindowState(WindowStates.getWSRPStateString(input.getWindowState()));
    baseRequest.setValidNewWindowStates(null);

    // TODO
    baseRequest.setSecureClientCommunication(request.isSecure());

    // Set fields for NavigationalContext from specified url params
    baseRequest.setNavigationalState(getNavigationalState(input, portletWindowSession));
    baseRequest.setNavigationalValues(getNavigationalValues(input, portletWindowSession));

    // Rewrite public params when they are present in input
    if (input.getPublicParameterMap() != null)
      baseRequest.setNavigationalValues(Utils.getNamedStringListParametersFromMap(input.getPublicParameterMap()));

    // For RuntimeContext
    // TODO
    String sid = getValueByKey0(WSRPConstants.WSRP_SESSION_ID, input.getRenderParameters());
    SessionContext sc = portletWindowSession.getPortletSession().getSessionContext();
    if (sc != null) {
      baseRequest.setSessionID(sc.getSessionID());
    }

//    String pik = getValueByKey0(WSRPConstants.WSRP_PORTLET_INSTANCE_KEY, input.getRenderParameters());
    String pik = input.getInternalWindowID().getUniqueID();

    baseRequest.setPortletInstanceKey(pik);

    // TODO
//    baseRequest.setUserAuthentication(WSRPConstants.AUTH_NO_USER_AUTHENTIFICATION);
    baseRequest.setUserAuthentication(getUserAuthentication(request));

    // For CACHE
    if (portletWindowSession.getCachedMarkup() != null
        && portletWindowSession.getCachedMarkup().getCacheControl() != null
        && !portletWindowSession.getMode().equalsIgnoreCase(baseRequest.getMode())
        && !portletWindowSession.getWindowState().equalsIgnoreCase(baseRequest.getWindowState())) {
      baseRequest.setValidateTag(portletWindowSession.getCachedMarkup()
                                                     .getCacheControl()
                                                     .getValidateTag());

    }
  }

  private String getUserAuthentication(HttpServletRequest request) {
    if (request.getAuthType() == null)
      return WSRPConstants.AUTH_NO_USER_AUTHENTIFICATION;

    if (HttpServletRequest.CLIENT_CERT_AUTH.equalsIgnoreCase(request.getAuthType()))
      return WSRPConstants.AUTH_CERTIFICATE_USER_AUTHENTIFICATION;

    if (HttpServletRequest.BASIC_AUTH.equalsIgnoreCase(request.getAuthType())
        || HttpServletRequest.DIGEST_AUTH.equalsIgnoreCase(request.getAuthType())
        || HttpServletRequest.FORM_AUTH.equalsIgnoreCase(request.getAuthType()))
      return WSRPConstants.AUTH_PASSWORD_USER_AUTHENTIFICATION;

    return WSRPConstants.AUTH_NO_USER_AUTHENTIFICATION;
  }

  private ClientData getClientData() {
    ClientData clientData = new ClientData();
    clientData.setUserAgent(userAgent);
    clientData.setCcppHeaders(null);
//    clientData.getClientAttributes().addAll(null);
    clientData.setRequestVerb(null);
    return clientData;
  }

  private String getNavigationalState(Input input, PortletWindowSession portletWindowSession) {
    if (input != null && input.getRenderParameters() != null) {
      String[] navigationalState = input.getRenderParameters()
                                        .get(WSRPConstants.WSRP_NAVIGATIONAL_STATE);
      if (navigationalState != null && navigationalState.length != 0) {
        LOG.debug("user navigational state : " + navigationalState[0]);
        portletWindowSession.setNavigationalState(navigationalState[0]);
      } else {
        LOG.debug("Navigational state is null or empty");
      }
    } else {
      LOG.debug("Input input is null or input.getRenderParameters() is null");
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
      if (LOG.isDebugEnabled())
        LOG.debug("user navigational values : " + resultArray);
      portletWindowSession.setNavigationalValues(resultArray);
    } else {
      LOG.debug("Navigational values are null");
    }
    return portletWindowSession.getNavigationalValues();
  }

  private void updateSessionContext(SessionContext sessionContext, PortletSession portletSession) {
    if (sessionContext != null) {
      LOG.debug("update session context");
      if (portletSession != null) {
        portletSession.setSessionContext(sessionContext);
      }
    }
  }

  private void updatePortletContext(PortletContext newPortletContext, WSRPPortlet portlet) throws WSRPException {
    if (newPortletContext != null && portlet != null) {
      LOG.info("update portlet context");
      String newPortletHandle = newPortletContext.getPortletHandle();
      if (newPortletHandle != null && !newPortletHandle.equals(portlet.getPortletHandle())) {
        LOG.info("portlet was cloned, new handle : " + newPortletHandle);
      }
      // update PortletContext
      portlet.setPortletContext(newPortletContext);
      consumer.getPortletRegistry().addPortlet(portlet);
    }
  }

  private WSRPMarkupRequest getMarkupRequest(RenderInput input,
                                             PortletWindowSession portletWindowSession,
                                             HttpServletRequest request) {
    WSRPMarkupRequestAdapter markupRequest = new WSRPMarkupRequestAdapter();
    fillMimeRequest(markupRequest, input, portletWindowSession, request);
    // if we need this
    markupRequest.setCachedMarkup(portletWindowSession.getCachedMarkup());
    return markupRequest;
  }

  private void processMarkupContext(MarkupContext markupContext, RenderOutput output) throws WSRPException {
    LOG.debug("process markup context for returned markup");
    if (markupContext != null && output != null) {
      processMimeResponse(markupContext, output);
      output.setNextPossiblePortletModes(getPortletModesFromStrings(markupContext.getValidNewModes()));
      String title = markupContext.getPreferredTitle();
      if (title != null) {
        LOG.debug("user title : " + title);
        output.setTitle(title);
      }
    }
  }

  // Below changes of WSRP2 spec

  private Collection<PortletMode> getPortletModesFromStrings(List<String> validNewModes) {
    if (validNewModes == null || validNewModes.isEmpty())
      return null;
    Collection<PortletMode> portletModes = new ArrayList<PortletMode>();
    for (String mode : validNewModes) {
      portletModes.add(Modes.getJsrPortletMode(mode));
    }
    return portletModes;
  }

  private WSRPResourceRequest getResourceRequest(ResourceInput input,
                                                 PortletWindowSession portletWindowSession,
                                                 HttpServletRequest request) {
    WSRPResourceRequestAdapter resourceRequest = new WSRPResourceRequestAdapter();
    fillMimeRequest(resourceRequest, input, portletWindowSession, request);
    // fill resource params
    resourceRequest.setFormParameters(getRenderParametersAsNamedString(input));
    resourceRequest.setResourceID(input.getResourceID());
    resourceRequest.setResourceState(getResourceState(input, portletWindowSession));
    resourceRequest.setResourceCacheability(input.getCacheability());

    // if cached is set we don't call producer for this method
    resourceRequest.setCachedResource(portletWindowSession.getCachedResource());

    // resourceRequest.setUploadContexts(uploadContexts);
    // resourceRequest.setPortletStateChange(portletStateChange);

    if (portletWindowSession.getCachedMarkup() != null
        && portletWindowSession.getCachedMarkup().getCacheControl() != null) {
      resourceRequest.setValidateTag(portletWindowSession.getCachedMarkup()
                                                         .getCacheControl()
                                                         .getValidateTag());
    }

    return resourceRequest;
  }

  private String getResourceState(Input input, PortletWindowSession portletWindowSession) {
    if (input != null && input.getRenderParameters() != null) {
      String[] resourceState = input.getRenderParameters().get(WSRPConstants.WSRP_RESOURCE_STATE);
      if (resourceState != null && resourceState.length != 0) {
        LOG.debug("user resource state : " + resourceState[0]);
        portletWindowSession.setResourceState(resourceState[0]);
      } else {
        LOG.debug("Resource state is null");
      }
    } else {
      LOG.debug("Input input is null or input.getRenderParameters() is null");
    }
    return portletWindowSession.getResourceState();
  }

  private void processResourceContext(ResourceContext resourceContext, ResourceOutput output) throws WSRPException {
    LOG.debug("process resource context for returned markup");
    if (resourceContext != null && output != null) {
      processMimeResponse(resourceContext, output);
    }
  }

  private void processMimeResponse(MimeResponse mimeResponse, RenderOutput output) throws WSRPException {
    LOG.debug("process resource context for returned markup");
    if (mimeResponse != null && output != null) {
      if (mimeResponse.getMimeType() != null) {
        output.setContentType(mimeResponse.getMimeType());
      }

      // process content
      byte[] binaryMarkup = mimeResponse.getItemBinary();
      if (binaryMarkup == null) {// && mimeResponse.getItemString() != null) {
        LOG.debug("string markup not null");
        try {
          binaryMarkup = mimeResponse.getItemString().getBytes("utf-8");
        } catch (java.io.UnsupportedEncodingException e) {
          binaryMarkup = mimeResponse.getItemString().getBytes();
        }
      } else {
        LOG.debug("binary markup not null");
      }
      output.setContent(binaryMarkup);
    }
  }

  private WSRPEventsRequest getEventsRequest(EventInput input,
                                             PortletWindowSession portletWindowSession,
                                             HttpServletRequest request) {
    WSRPEventsRequestAdapter eventRequest = new WSRPEventsRequestAdapter();
    fillMimeRequest(eventRequest, input, portletWindowSession, request);
    if (input.getEvent() != null)
      eventRequest.getEvents().add(JAXBEventTransformer.getEventMarshal(input.getEvent()));
    return eventRequest;
  }

  /**
   * Get portlet app names.
   * 
   * @return collection of string
   */
  public final Collection<String> getPortletAppNames() {
    LOG.debug("getPortletAppNames() entered");
    Collection<String> result = new HashSet<String>();
    // put WSRPAdminPortlet
    result.add(WSRPConstants.WSRP_ADMIN_PORTLET_APP);
    // put all remote portlets
    String producerId = null;
    String portletHandle = null;
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    Iterator<Producer> i = pregistry.getAllProducers();
    if (i == null)
      return result;

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
        String newPortletHandle = producerId + WSRPConstants.PRODUCER_HANDLE_ENCODER
            + portletHandle;
        String[] ss = StringUtils.split(newPortletHandle, "/");
        String portletAppName = ss[0];
        result.add(portletAppName);
      }
    }
    return result;
  }

  private WSRPPortlet getPortlet(String producerID,
                                 String pcFullPortletHandle,
                                 String wsrpPortletHandle) throws PortletContainerException {
    try {
      WSRPPortlet portlet = consumer.getPortletRegistry().getPortlet(producerID,
                                                                     pcFullPortletHandle);
      if (portlet == null) {
        // create portlet key
        PortletKey portletKey = new PortletKeyAdapter();
        portletKey.setProducerId(producerID);
        portletKey.setPortletHandle(pcFullPortletHandle);
        LOG.debug("Created new PortletKey with producerID = '" + producerID
            + "' and  portletHandle = '" + pcFullPortletHandle + "'");

        String parentHandle = getSimplePortletHandle(pcFullPortletHandle);
        if (wsrpPortletHandle == null) {
          wsrpPortletHandle = parentHandle;
        }
        //create portlet with created portlet key
        portlet = createWSRPPortlet(portletKey, parentHandle, wsrpPortletHandle);
        // store WSRP portlet
        consumer.getPortletRegistry().addPortlet(portlet);
      }
      // do we need to update wsrp portlet handle got from URL
      if (wsrpPortletHandle != null
          && !wsrpPortletHandle.equalsIgnoreCase(portlet.getPortletHandle())) {
        portlet.setPortletHandle(wsrpPortletHandle);
      }
      return portlet;
    } catch (WSRPException e) {
      throw new PortletContainerException(e.getMessage(), e);
    }
  }

  private String getSimplePortletHandle(String pcFullPortletHandle) {
    String simplePortletHandle = new String();
    String[] ss = StringUtils.split(pcFullPortletHandle, "/");
    simplePortletHandle = ss[0] + "/" + ss[1];
    return simplePortletHandle;
  }

  private WSRPPortlet createWSRPPortlet(PortletKey portletKey,
                                        String parentHandle,
                                        String wsrpPortletHandle) {
    // parentHandle == wsrpPortletHandle
    WSRPPortlet portlet = new WSRPPortletAdapter(portletKey);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(wsrpPortletHandle);
    //    portletContext.setPortletState(portletState);
    //    portletContext.setScheduledDestruction(scheduledDestruction);
    portlet.setPortletContext(portletContext);
    if (parentHandle != null) {
      portlet.setParent(parentHandle);
    }
    return portlet;
  }

  private Producer getProducer(String producerID) {
    LOG.debug("getProducer : " + producerID);
    Producer producer = consumer.getProducerRegistry().getProducer(producerID);
    return producer;
  }

}
