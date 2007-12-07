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

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.MarkupOperationsInterface;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp2.producer.TransientStateManager;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPConsumerRewriterPortletURLFactory;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPProducerRewriterPortletURLFactory;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.CacheControl;
import org.exoplatform.services.wsrp2.type.Event;
import org.exoplatform.services.wsrp2.type.EventParams;
import org.exoplatform.services.wsrp2.type.HandleEventsFailed;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.InteractionParams;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.MarkupContext;
import org.exoplatform.services.wsrp2.type.MarkupParams;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.NavigationalContext;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ResourceContext;
import org.exoplatform.services.wsrp2.type.ResourceParams;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.SessionContext;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.type.UpdateResponse;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.utils.JAXBEventTransformer;
import org.exoplatform.services.wsrp2.utils.Modes;
import org.exoplatform.services.wsrp2.utils.WindowStates;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class MarkupOperationsInterfaceImpl implements MarkupOperationsInterface {

  // private static final String DEFAULT_WINDOW_ID = "windowID";
  // private static String WSRP_CONTAINER = "portal";

  private Log                                  log;

  private WSRPConfiguration                    conf;

  private PersistentStateManager               persistentStateManager;

  private PortletContainerProxy                proxy;

  private TransientStateManager                transientStateManager;

  private PortletManagementOperationsInterface portletManagementOperationsInterface;

  private OrganizationService                  orgService;

  private WSRPPortletPreferencesPersister      persister;

  public MarkupOperationsInterfaceImpl(PortletManagementOperationsInterface portletManagementOperationsInterface,
                                       PersistentStateManager persitentStateManager,
                                       TransientStateManager transientStateManager,
                                       PortletContainerProxy proxy,
                                       WSRPConfiguration conf,
                                       OrganizationService orgService) {
    this.portletManagementOperationsInterface = portletManagementOperationsInterface;
    this.proxy = proxy;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    this.conf = conf;
    this.persistentStateManager = persitentStateManager;
    this.transientStateManager = transientStateManager;
    this.orgService = orgService;
    this.persister = WSRPPortletPreferencesPersister.getInstance();
  }

  public MarkupResponse getMarkup(RegistrationContext registrationContext,
                                  PortletContext portletContext,
                                  RuntimeContext runtimeContext,
                                  UserContext userContext,
                                  MarkupParams markupParams) throws RemoteException {

    // runtimeContext.getPageState()
    // runtimeContext.getPortletStates()
    // markupParams.getNavigationalContext().getPublicValues()

    // manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    log.debug("Portlet handle : " + portletHandle);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = k[2];
    Integer sessiontimeperiod = getSessionTimePeriod();

    // manage session
    String sessionID = runtimeContext.getSessionParams().getSessionID();
    WSRPHttpSession session = resolveSession(sessionID, userContext.getUserContextKey(), sessiontimeperiod);

    // build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(session.getId());
    sessionContext.setExpires(sessiontimeperiod);

    // manage user
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String owner = userContext.getUserContextKey();
    log.debug("Owner Context : " + owner);

    // manage cache
    if (markupParams.getValidateTag() != null) {
      try {
        if (transientStateManager.validateCache(markupParams.getValidateTag())) {
          MarkupContext markupContext = new MarkupContext();
          markupContext.setUseCachedItem(new Boolean(true));
          MarkupResponse markup = new MarkupResponse();
          markup.setMarkupContext(markupContext);
          markup.setSessionContext(sessionContext);
          return markup;
        }
      } catch (WSRPException e) {
        log.debug("Can not validate Cache for validateTag : " + markupParams.getValidateTag());
        Exception2Fault.handleException(e);
      }
    }

    // get portlet datas
    Map<String, PortletData> portletMetaDatas = proxy.getAllPortletMetaData();
    PortletData portletDatas = (PortletData) portletMetaDatas.get(portletApplicationName + Constants.PORTLET_META_DATA_ENCODER + portletName);
    // manage navigationalState
    Map renderParameters = null;
    try {
      renderParameters = processNavigationalState(markupParams.getNavigationalContext().getOpaqueValue());
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    if (renderParameters == null) {
      renderParameters = new HashMap();
      log.debug("No navigational state exists");
    }

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    // manage mime type
    String mimeType = null;
    try {
      mimeType = getMimeType(markupParams.getMimeTypes(), portletDatas);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    String baseURL = null;
    PortletURLFactory portletURLFactory = null;

    // creating Portlet URL Factory
    if (conf.isDoesUrlTemplateProcessing()) {// default is true
      log.debug("Producer URL rewriting");
      Templates templates = manageTemplates(runtimeContext, session);
      baseURL = templates.getRenderTemplate();
      portletURLFactory = new WSRPProducerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    markupParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID,
                                                                    portletDatas.getEscapeXml(),
                                                                    ResourceURL.PAGE);
    } else {
      log.debug("Consumer URL rewriting");
      baseURL = WSRPConstants.WSRP_REWRITE_PREFIX;
      portletURLFactory = new WSRPConsumerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    markupParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID,
                                                                    portletDatas.getEscapeXml(),
                                                                    ResourceURL.PAGE);
    }

    // manage mode and states
    PortletMode mode = processMode(markupParams.getMode());
    WindowState windowState = processWindowState(markupParams.getWindowState());

    // prepare the call to the portlet container
    WSRPHttpServletRequest request = new WSRPHttpServletRequest(session);
    WSRPHttpServletResponse response = new WSRPHttpServletResponse();

    // prepare the Input object
    RenderInput input = new RenderInput();
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(owner);
    windowID.setPortletApplicationName(portletApplicationName);
    windowID.setPortletName(portletName);
    windowID.setUniqueID(uniqueID);
    input.setInternalWindowID(windowID);
    input.setBaseURL(baseURL);
    input.setUserAttributes(new HashMap<String, String>());
    input.setPortletMode(new PortletMode(Modes.delAllPrefixWSRP(mode.toString())));
    input.setWindowState(new WindowState(WindowStates.delAllPrefixWSRP(windowState.toString())));
    input.setMarkup(mimeType);
    input.setRenderParameters(renderParameters);
    input.setPortletURLFactory(portletURLFactory);
    input.setPortletState(portletState);
    input.setPortletPreferencesPersister(persister);
    // createUserProfile(userContext, request, session);

    RenderOutput output = null;
    try {
      /* MAIN INVOKE */
      output = proxy.render(request, response, input);
    } catch (WSRPException e) {
      log.debug("The call to render method was a failure ", e);
      Exception2Fault.handleException(e);
    }

    // prepare the cache control object
    CacheControl cacheControl = null;
    try {
      cacheControl = transientStateManager.getCacheControl(portletDatas);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    // build markup context
    MarkupContext markupContext = new MarkupContext();
    markupContext.setMimeType(mimeType);
    markupContext.setCacheControl(cacheControl);
    markupContext.setItemString(new String(output.getContent()));
    markupContext.setItemBinary(output.getBinContent());
    markupContext.setPreferredTitle(output.getTitle());
    markupContext.setRequiresRewriting(new Boolean(conf.isDoesUrlTemplateProcessing()));
    markupContext.setUseCachedItem(new Boolean(false));
    // markupContext.setMarkupBinary(null);//TODO
    // markupContext.setLocale(null);

    // ajoute une locale au markupcontext
    markupContext.setLocale("en");

    MarkupResponse markupResponse = new MarkupResponse();
    markupResponse.setSessionContext(sessionContext);
    markupResponse.setMarkupContext(markupContext);
    return markupResponse;
  }

  public BlockingInteractionResponse performBlockingInteraction(RegistrationContext registrationContext,
                                                                PortletContext portletContext,
                                                                RuntimeContext runtimeContext,
                                                                UserContext userContext,
                                                                MarkupParams markupParams,
                                                                InteractionParams interactionParams) throws RemoteException {
    // manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();

    // manage session
    String sessionID = runtimeContext.getSessionParams().getSessionID();
    WSRPHttpSession session = resolveSession(sessionID, userContext.getUserContextKey(), sessiontimeperiod);

    // build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(session.getId());
    sessionContext.setExpires(sessiontimeperiod);

    // manage user
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String owner = userContext.getUserContextKey();
    log.debug("Owner Context : " + owner);

    // ---------- BEGIN FOR CREATING FACTORY --------------
    // get portlet datas
    Map<String, PortletData> portletMetaDatas = proxy.getAllPortletMetaData();
    PortletData portletDatas = (PortletData) portletMetaDatas.get(portletApplicationName + Constants.PORTLET_META_DATA_ENCODER + portletName);
    // manage mime type
    String mimeType = null;
    try {
      mimeType = getMimeType(markupParams.getMimeTypes(), portletDatas);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    
    // manage rewriting mechanism
    String baseURL = null;
    PortletURLFactory portletURLFactory = null;

    // creating Portlet URL Factory
    if (conf.isDoesUrlTemplateProcessing()) {// default is true
      log.debug("Producer URL rewriting");
      Templates templates = manageTemplates(runtimeContext, session);
      baseURL = templates.getRenderTemplate();
      portletURLFactory = new WSRPProducerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    markupParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID,
                                                                    portletDatas.getEscapeXml(),
                                                                    ResourceURL.PAGE);
    } else {
      log.debug("Consumer URL rewriting");
      baseURL = WSRPConstants.WSRP_REWRITE_PREFIX;
      portletURLFactory = new WSRPConsumerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    markupParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID,
                                                                    portletDatas.getEscapeXml(),
                                                                    ResourceURL.PAGE);
    }
    // ---------- END FOR CREATING FACTORY --------------

    // manage rewriting mechanism
    // String baseURL = null;
    // if (conf.isDoesUrlTemplateProcessing()) {
    // log.debug("Producer URL rewriting");
    // Templates templates = manageTemplates(runtimeContext, session);
    // baseURL = templates.getRenderTemplate();
    // } else {
    // log.debug("Consumer URL rewriting");
    // baseURL = WSRPConstants.WSRP_REWRITE_PREFIX;
    // }
    // String mimeType = markupParams.getMimeTypes(0);

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    // manage mode and states
    PortletMode mode = processMode(markupParams.getMode());
    WindowState windowState = processWindowState(markupParams.getWindowState());

    // manage portlet state change
    boolean isStateChangeAuthorized = false;
    String stateChange = interactionParams.getPortletStateChange().getValue();
    if (StateChange.readWrite.getValue().equalsIgnoreCase(stateChange)) {
      log.debug("readWrite state change");
      // every modification is allowed on the portlet
      isStateChangeAuthorized = true;
    } else if (StateChange.cloneBeforeWrite.getValue().equalsIgnoreCase(stateChange)) {
      log.debug("cloneBeforWrite state change");
      portletContext = portletManagementOperationsInterface.clonePortlet(registrationContext, portletContext, userContext);
      // any modification will be made on the
      isStateChangeAuthorized = true;
    } else if (StateChange.readOnly.getValue().equalsIgnoreCase(stateChange)) {
      log.debug("readOnly state change");
      // if an attempt to change the state is done (means change the portlet
      // pref in JSR 168)
      // then a fault will be launched
    } else {
      log.debug("The submited portlet state change value : " + stateChange + " is not supported");

      Exception2Fault.handleException(new WSRPException(Faults.PORTLET_STATE_CHANGE_REQUIRED_FAULT));
    }

    NavigationalContext navigationalContext = markupParams.getNavigationalContext(); // TODO EXOMAN
    String navigationalState = navigationalContext.getOpaqueValue();
    NamedString[] namedStringArray = navigationalContext.getPublicValues();
    Map<String, String[]> navigationalParams = new HashMap<String, String[]>();
    for (NamedString namedString : namedStringArray) {
      navigationalParams.put(namedString.getName(), new String[]{namedString.getValue()});
    }
    
    
    // prepare objects for portlet container
    WSRPHttpServletRequest request = new WSRPHttpServletRequest(session);
    WSRPHttpServletResponse response = new WSRPHttpServletResponse();
    Map<String, String[]> renderParameters = getFormParameters(interactionParams);

    // prepare the Input object
    ActionInput input = new ActionInput();
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(owner);
    windowID.setPortletApplicationName(portletApplicationName);
    windowID.setPortletName(portletName);
    windowID.setUniqueID(uniqueID);
    input.setInternalWindowID(windowID);
    input.setBaseURL(baseURL);
    input.setUserAttributes(new HashMap<String, String>());
    input.setPortletMode(new PortletMode(Modes.delAllPrefixWSRP(mode.toString())));
    input.setWindowState(new WindowState(WindowStates.delAllPrefixWSRP(windowState.toString())));
    input.setMarkup(mimeType);
    input.setStateChangeAuthorized(isStateChangeAuthorized);
    input.setStateSaveOnClient(conf.isSavePortletStateOnConsumer());
    input.setPortletState(portletState);
    input.setPortletPreferencesPersister(persister);
    input.setRenderParameters(renderParameters);
    // createUserProfile(userContext, request, session);
    ActionOutput output = null;
    try {

      /* MAIN INVOKE */
      output = proxy.processAction(request, response, input);
    } catch (WSRPException e) {
      log.debug("The call to processAction method was a failure ", e);
      Exception2Fault.handleException(e);
    }

    // manage navigational state, save render parameters
    String ns = markupParams.getNavigationalContext().getOpaqueValue();//was: IdentifierUtil.generateUUID(output);
    if (ns == null) {
      ns = IdentifierUtil.generateUUID(output);
    }
    try {
      log.debug("set new navigational state : " + ns);
      persistentStateManager.putNavigationalState(ns, output.getRenderParameters());
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    
    // TODO EXOMAN
    // output.getRenderParameters()
    // portletDatas.getSupportedPublicRenderParameter()
    // markupParams.getNavigationalContext().getPublicValues()
    
    BlockingInteractionResponse blockingInteractionResponse = new BlockingInteractionResponse();

    if (output.getProperties().get(ActionOutput.SEND_REDIRECT) != null) {
      log.debug("Redirect the response to : " + (String) output.getProperties().get(ActionOutput.SEND_REDIRECT));
      blockingInteractionResponse.setRedirectURL((String) output.getProperties().get(ActionOutput.SEND_REDIRECT));
      blockingInteractionResponse.setUpdateResponse(null);
      
    } else {
      MarkupContext markupContext = null;
      if (conf.isBlockingInteractionOptimized()) {
        // markupParams.setWindowState(ns);
        MarkupResponse markupResponse = getMarkup(registrationContext, portletContext, runtimeContext, userContext, markupParams);
        markupContext = markupResponse.getMarkupContext();
      }

      UpdateResponse updateResponse = new UpdateResponse();

      if (output.getNextMode() != null) {
        updateResponse.setNewMode(WSRPConstants.WSRP_PREFIX + output.getNextMode().toString());
      }
      if (output.getNextState() != null) {
        updateResponse.setNewWindowState(WSRPConstants.WSRP_PREFIX + output.getNextState().toString());
      }
      updateResponse.setSessionContext(sessionContext);
      updateResponse.setMarkupContext(markupContext);
      // fill the state to send it to consumer if allowed
      if (conf.isSavePortletStateOnConsumer())
        portletContext.setPortletState(output.getPortletState());
      updateResponse.setPortletContext(portletContext);

      // copy from old markup params to response
      NavigationalContext oldNavigationalContext = markupParams.getNavigationalContext();
      NavigationalContext newNavigationalContext = new NavigationalContext();
      newNavigationalContext.setOpaqueValue(oldNavigationalContext.getOpaqueValue());
      newNavigationalContext.setPublicValues(oldNavigationalContext.getPublicValues());
      newNavigationalContext.setExtensions(null);
      updateResponse.setNavigationalContext(newNavigationalContext);

      updateResponse.setEvents(JAXBEventTransformer.getEventsToWSRP(output.getEvents()));

      blockingInteractionResponse.setUpdateResponse(updateResponse);
    }

    return blockingInteractionResponse;
  }

  public ResourceResponse getResource(RegistrationContext registrationContext,
                                      PortletContext portletContext,
                                      RuntimeContext runtimeContext,
                                      UserContext userContext,
                                      ResourceParams resourceParams) throws java.rmi.RemoteException {

    // manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    log.debug("Portlet handle : " + portletHandle);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();

    // manage session
    String sessionID = runtimeContext.getSessionParams().getSessionID();
    WSRPHttpSession session = resolveSession(sessionID, userContext.getUserContextKey(), sessiontimeperiod);

    // build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(session.getId());
    sessionContext.setExpires(sessiontimeperiod);

    // manage user
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String owner = userContext.getUserContextKey();
    log.debug("Owner Context : " + owner);

    // manage cache
    /*
     * if (markupParams.getValidateTag() != null) { try { if
     * (transientStateManager.validateCache(markupParams.getValidateTag())) {
     * MarkupContext markupContext = new MarkupContext();
     * markupContext.setUseCachedMarkup(new Boolean(true)); MarkupResponse
     * markup = new MarkupResponse(); markup.setMarkupContext(markupContext);
     * markup.setSessionContext(sessionContext); return markup; } } catch
     * (WSRPException e) { log.debug("Can not validate Cache for validateTag : " +
     * markupParams.getValidateTag()); Exception2Fault.handleException(e); } }
     */

    //---------- BEGIN FOR CREATING FACTORY --------------
    Map<String, PortletData> portletMetaDatas = proxy.getAllPortletMetaData();
    PortletData portletDatas = (PortletData) portletMetaDatas.get(portletApplicationName + Constants.PORTLET_META_DATA_ENCODER + portletName);

    // manage navigationalState
    Map renderParameters = null;
    try {
      renderParameters = processNavigationalState(resourceParams.getNavigationalContext().getOpaqueValue());
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    if (renderParameters == null) {
      renderParameters = new HashMap();
      log.debug("No navigational state exists");
    }

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    // manage mime type
    String mimeType = null;
    try {
      mimeType = getMimeType(resourceParams.getMimeTypes(), portletDatas);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    String baseURL = null;
    PortletURLFactory portletURLFactory = null;

    // creating Portlet URL Factory
    if (conf.isDoesUrlTemplateProcessing()) {// default is true
      log.debug("Producer URL rewriting");
      Templates templates = manageTemplates(runtimeContext, session);
      baseURL = templates.getRenderTemplate();
      portletURLFactory = new WSRPProducerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    resourceParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID,
                                                                    portletDatas.getEscapeXml(),
                                                                    resourceParams.getResourceCacheability());
    } else {
      log.debug("Consumer URL rewriting");
      baseURL = WSRPConstants.WSRP_REWRITE_PREFIX;
      portletURLFactory = new WSRPConsumerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    resourceParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID,
                                                                    portletDatas.getEscapeXml(),
                                                                    resourceParams.getResourceCacheability());
    }

    // manage mode and states
    PortletMode mode = processMode(resourceParams.getMode());
    WindowState windowState = processWindowState(resourceParams.getWindowState());

    // prepare the call to the portlet container
    WSRPHttpServletRequest request = new WSRPHttpServletRequest(session);
    WSRPHttpServletResponse response = new WSRPHttpServletResponse();
    // putFormParametersInRequest(request, resourceParams);

    // prepare the Input object
    ResourceInput input = new ResourceInput();
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(owner);
    windowID.setPortletApplicationName(portletApplicationName);
    windowID.setPortletName(portletName);
    windowID.setUniqueID(uniqueID);
    input.setInternalWindowID(windowID);
    input.setBaseURL(baseURL);
    input.setUserAttributes(new HashMap<String, String>());
    input.setPortletMode(mode);
    input.setWindowState(windowState);
    input.setMarkup(mimeType);
    input.setRenderParameters(renderParameters);
    input.setPortletURLFactory(portletURLFactory);
    input.setPortletState(portletState);
    input.setPortletPreferencesPersister(persister);
    input.setResourceID(resourceParams.getResourceID());
    input.setCacheability(resourceParams.getResourceCacheability());
    // createUserProfile(userContext, request, session);

    ResourceOutput output = null;
    try {
      /* MAIN INVOKE */
      output = proxy.serveResource(request, response, input);
    } catch (WSRPException e) {
      log.debug("The call to render method was a failure ", e);
      Exception2Fault.handleException(e);
    }

    // prepare the cache control object
    CacheControl cacheControl = null;
    try {
      cacheControl = transientStateManager.getCacheControl(portletDatas);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    // build markup context
    ResourceContext resourceContext = new ResourceContext();
    resourceContext.setMimeType(output.getContentType());// was: mimeType
    resourceContext.setCacheControl(cacheControl);
    resourceContext.setItemString(removeNonValidXMLCharacters(new String(output.getContent())));
    resourceContext.setItemBinary(output.getBinContent()); // TODO next comment
    // resourceContext.setPreferredTitle(output.getTitle());
    resourceContext.setRequiresRewriting(new Boolean(conf.isDoesUrlTemplateProcessing()));
    resourceContext.setUseCachedItem(Boolean.FALSE);

    // add local to the markupcontext
    resourceContext.setLocale("en");

    ResourceResponse resourceResponse = new ResourceResponse();
    resourceResponse.setPortletContext(portletContext);
    resourceResponse.setSessionContext(sessionContext);
    resourceResponse.setResourceContext(resourceContext);
    return resourceResponse;
  }

  public HandleEventsResponse processEvent(RegistrationContext registrationContext,
                                           PortletContext portletContext,
                                           RuntimeContext runtimeContext,
                                           UserContext userContext,
                                           MarkupParams markupParams,
                                           EventParams eventParams) throws java.rmi.RemoteException {

    // manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();

    // manage session
    String sessionID = runtimeContext.getSessionParams().getSessionID();
    WSRPHttpSession session = resolveSession(sessionID, userContext.getUserContextKey(), sessiontimeperiod);

    // build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(session.getId());
    sessionContext.setExpires(sessiontimeperiod);

    // manage user
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String owner = userContext.getUserContextKey();
    log.debug("Owner Context : " + owner);

    // ---------- BEGIN CREATING FACTORY --------------
    // get portlet datas
    Map<String, PortletData> portletMetaDatas = proxy.getAllPortletMetaData();
    PortletData portletDatas = (PortletData) portletMetaDatas.get(portletApplicationName + Constants.PORTLET_META_DATA_ENCODER + portletName);
    // manage mime type
    String mimeType = null;
    try {
      mimeType = getMimeType(markupParams.getMimeTypes(), portletDatas);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    // manage rewriting mechanism
    String baseURL = null;
    PortletURLFactory portletURLFactory = null;

    // creating Portlet URL Factory
    if (conf.isDoesUrlTemplateProcessing()) {// default is true
      log.debug("Producer URL rewriting");
      Templates templates = manageTemplates(runtimeContext, session);
      baseURL = templates.getRenderTemplate();
      portletURLFactory = new WSRPProducerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    markupParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID,
                                                                    portletDatas.getEscapeXml(),
                                                                    ResourceURL.PAGE);
    } else {
      log.debug("Consumer URL rewriting");
      baseURL = WSRPConstants.WSRP_REWRITE_PREFIX;
      portletURLFactory = new WSRPConsumerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    markupParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID,
                                                                    portletDatas.getEscapeXml(),
                                                                    ResourceURL.PAGE);
    }
    // ---------- END CREATING FACTORY --------------

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    // manage mode and states
    PortletMode mode = processMode(markupParams.getMode());
    WindowState windowState = processWindowState(markupParams.getWindowState());

    // manage portlet state change
    boolean isStateChangeAuthorized = false;
    String stateChange = eventParams.getPortletStateChange().getValue();
    if (StateChange.readWrite.getValue().equalsIgnoreCase(stateChange)) {

      log.debug("readWrite state change");
      // every modification is allowed on the portlet
      isStateChangeAuthorized = true;
    } else if (StateChange.cloneBeforeWrite.getValue().equalsIgnoreCase(stateChange)) {

      log.debug("cloneBeforWrite state change");
      portletContext = portletManagementOperationsInterface.clonePortlet(registrationContext, portletContext, userContext);
      // any modification will be made on the
      isStateChangeAuthorized = true;
    } else if (StateChange.readOnly.getValue().equalsIgnoreCase(stateChange)) {
      log.debug("readOnly state change");
      // if an attempt to change the state is done (means change the portlet
      // pref in JSR 168)
      // then a fault will be launched
    } else {
      log.debug("The submited portlet state change value : " + stateChange + " is not supported");
      Exception2Fault.handleException(new WSRPException(Faults.PORTLET_STATE_CHANGE_REQUIRED_FAULT));
    }

    // prepare objects for portlet container
    WSRPHttpServletRequest request = new WSRPHttpServletRequest(session);
    WSRPHttpServletResponse response = new WSRPHttpServletResponse();
    // putInteractionParameterInRequest(request, eventParams);

    HandleEventsResponse handleEventsResponse = new HandleEventsResponse();

    ArrayList<HandleEventsFailed> failed_array = new ArrayList<HandleEventsFailed>();

    Event[] events = eventParams.getEvents();

    List<javax.portlet.Event> nativeEventsList = JAXBEventTransformer.getEventsFromWSRP(events);

//    BigInteger[] indexes;

    // iteration over incoming javax events
    for (int i = 0; i < events.length; i++) {

      javax.portlet.Event event = nativeEventsList.get(i);

      // prepare the Input object
      EventInput input = new EventInput();
      ExoWindowID windowID = new ExoWindowID();
      windowID.setOwner(owner);
      windowID.setPortletApplicationName(portletApplicationName);
      windowID.setPortletName(portletName);
      windowID.setUniqueID(uniqueID);
      input.setInternalWindowID(windowID);
      input.setBaseURL(baseURL);
      input.setUserAttributes(new HashMap<String, String>());
      input.setPortletMode(mode);
      input.setWindowState(windowState);
      input.setMarkup(mimeType);
      input.setEvent(event);
      // input.setStateChangeAuthorized(isStateChangeAuthorized);
      input.setStateSaveOnClient(conf.isSavePortletStateOnConsumer());
      input.setPortletState(portletState);
      input.setPortletURLFactory(portletURLFactory);
      input.setPortletPreferencesPersister(persister);
      // createUserProfile(userContext, request, session);
      EventOutput output = null;
      try {
        /* MAIN INVOKE */
        output = proxy.processEvent(request, response, input);
      } catch (WSRPException e) {
        log.debug("The call to processEvent method was a failure ", e);
        HandleEventsFailed handleEventsFailed = new HandleEventsFailed();
        //handleEventsFailed.setErrorCode(ErrorCodes.fromValue());
        handleEventsFailed.setReason(new LocalizedString(e.getLocalizedMessage(), ""));// TODO
        // EXOMAN
        // indexes[0] = new BigInteger(i);
        // handleEventsFailed.setIndex(indexes);
        failed_array.add(handleEventsFailed);
        Exception2Fault.handleException(e);
      }

      // manage navigational state
      String ns = IdentifierUtil.generateUUID(output);
      try {
        log.debug("set new navigational state : " + ns);
        persistentStateManager.putNavigationalState(ns, output.getRenderParameters());
        //persistentStateManager.putNavigationalState(ns, new HashMap());
      } catch (WSRPException e) {
        Exception2Fault.handleException(e);
      }

      MarkupContext markupContext = null;
      if (conf.isBlockingInteractionOptimized()) {
        // markupParams.setWindowState(ns);
        MarkupResponse markupResponse = getMarkup(registrationContext, portletContext, runtimeContext, userContext, markupParams);
        markupContext = markupResponse.getMarkupContext();
      }

      UpdateResponse updateResponse = new UpdateResponse();

      if (output.getNextMode() != null) {
        updateResponse.setNewMode(WSRPConstants.WSRP_PREFIX + output.getNextMode().toString());
      }
      if (output.getNextState() != null) {
        updateResponse.setNewWindowState(WSRPConstants.WSRP_PREFIX + output.getNextState().toString());
      }
      updateResponse.setSessionContext(sessionContext);
      updateResponse.setMarkupContext(markupContext);

      updateResponse.setPortletContext(portletContext);
      updateResponse.setEvents(JAXBEventTransformer.getEventsToWSRP(output.getEvents()));

      // copy from old markup params to response
      NavigationalContext oldNavigationalContext = markupParams.getNavigationalContext();
      NavigationalContext newNavigationalContext = new NavigationalContext();
      newNavigationalContext.setOpaqueValue(oldNavigationalContext.getOpaqueValue());
      newNavigationalContext.setPublicValues(oldNavigationalContext.getPublicValues());
      newNavigationalContext.setExtensions(null);
      updateResponse.setNavigationalContext(newNavigationalContext);

      handleEventsResponse.setUpdateResponse(updateResponse);
      handleEventsResponse.setFailedEvents((HandleEventsFailed[]) failed_array.toArray(new HandleEventsFailed[failed_array.size()]));

    }
    return handleEventsResponse;

  }

  private Map<String, String[]> getFormParameters(InteractionParams interactionParams) {
    Map<String, String[]> result = null;
    NamedString[] array = interactionParams.getFormParameters();
    if (array == null) {
      log.debug("no form parameters");
      return result;
    }
    result = new HashMap<String, String[]>();
    for (NamedString namedString : array) {
      log.debug("form parameters; name : " + namedString.getName() + "; value : " + namedString.getValue());
      String key2 = namedString.getName();
      String value2 = namedString.getValue();
      if (result.get(key2) == null) {
        result.put(key2, new String[] { value2 });
      } else {
        if (value2 == null || value2.equals(""))
          result.remove(key2);
        else {
          String[] oldValue = result.get(key2);
          String[] newValue = new String[oldValue.length + 1];
          int i = 0;
          for (String v : oldValue)
            newValue[i++] = v;
          newValue[i] = value2;
          result.put(key2, newValue);
        }
      }
    }
    return result;
  }

  public ReturnAny initCookie(RegistrationContext registrationContext) throws RemoteException {
    if (conf.isRegistrationRequired()) {
      log.debug("Registration required");
      if (registrationContext == null) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    }
    return new ReturnAny();
  }

  public ReturnAny initCookie(RegistrationContext registrationContext,
                              UserContext userContext) throws RemoteException {
    if (conf.isRegistrationRequired()) {
      log.debug("Registration required");
      if (registrationContext == null) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      } else {
        String owner = userContext.getUserContextKey();
        log.debug("Logged Owner Context : " + owner);
      }
    }
    return new ReturnAny();
  }

  public ReturnAny releaseSessions(RegistrationContext registrationContext,
                                   String[] sessionIDs) throws RemoteException {
    if (conf.isRegistrationRequired()) {
      log.debug("Registration required");
      if (registrationContext == null) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    }
    for (int i = 0; i < sessionIDs.length; i++) {
      transientStateManager.releaseSession(sessionIDs[i]);
    }
    return new ReturnAny();
  }

  public ReturnAny releaseSessions(RegistrationContext registrationContext,
                                   String[] sessionIDs,
                                   UserContext userContext) throws RemoteException {
    if (conf.isRegistrationRequired()) {
      log.debug("Registration required");
      if (registrationContext == null) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    }
    String owner = userContext.getUserContextKey();
    log.debug("Released Owner Context : " + owner);
    for (int i = 0; i < sessionIDs.length; i++) {
      transientStateManager.releaseSession(sessionIDs[i]);
    }
    return new ReturnAny();
  }

  private WSRPHttpSession resolveSession(String sessionID,
                                         String user,
                                         Integer sessiontimeperiod) throws RemoteException {
    WSRPHttpSession session = null;
    try {
      session = transientStateManager.resolveSession(sessionID, user, sessiontimeperiod);
    } catch (WSRPException e) {
      log.debug("Can not lookup or create new session, sessionID parameter : " + sessionID);
      Exception2Fault.handleException(e);
    }
    log.debug("Use session with ID : " + session.getId());
    return session;
  }

  private String manageRegistration(String portletHandle,
                                    RegistrationContext registrationContext) throws RemoteException {
    log.debug("manageRegistration called for portlet handle : " + portletHandle);
    if (!proxy.isPortletOffered(portletHandle)) {
      log.debug("The latter handle is not offered by the Producer");
      Exception2Fault.handleException(new WSRPException(Faults.INVALID_HANDLE_FAULT));
    } else {
      String[] keys = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
      if (keys.length == 2) {
        portletHandle += Constants.PORTLET_HANDLE_ENCODER + String.valueOf(portletHandle.hashCode()); // DEFAULT_WINDOW_ID;
      }
    }
    if (conf.isRegistrationRequired()) {
      log.debug("Registration required");
      if (registrationContext == null) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    } else {
      log.debug("Registration non required");
    }
    return portletHandle;
  }

  private PortletMode processMode(String mode) {
    log.debug("Call with portlet mode : " + mode);
    if ((WSRPConstants.WSRP_PREFIX + PortletMode.VIEW.toString()).equalsIgnoreCase(mode)) {
      return PortletMode.VIEW;
    } else if ((WSRPConstants.WSRP_PREFIX + PortletMode.EDIT.toString()).equalsIgnoreCase(mode)) {
      return PortletMode.EDIT;
    } else if ((WSRPConstants.WSRP_PREFIX + PortletMode.HELP.toString()).equalsIgnoreCase(mode)) {
      return PortletMode.HELP;
    }
    return new PortletMode(mode.substring(WSRPConstants.WSRP_PREFIX.length()));
  }

  private WindowState processWindowState(String state) {
    log.debug("Call with window state : " + state);
    if ((WSRPConstants.WSRP_PREFIX + WindowState.NORMAL.toString()).equalsIgnoreCase(state)) {
      return WindowState.NORMAL;
    } else if ((WSRPConstants.WSRP_PREFIX + WindowState.MINIMIZED.toString()).equalsIgnoreCase(state)) {
      return WindowState.MINIMIZED;
    } else if ((WSRPConstants.WSRP_PREFIX + WindowState.MAXIMIZED.toString()).equalsIgnoreCase(state)) {
      return WindowState.MAXIMIZED;
    }
    return new WindowState(state.substring(WSRPConstants.WSRP_PREFIX.length()));
  }

  private Map processNavigationalState(String navigationalState) throws WSRPException {
    log.debug("Lookup navigational state : " + navigationalState);
    Map map = persistentStateManager.getNavigationalSate(navigationalState);
    // for debug:
    if (log.isDebugEnabled()) {
      if (map != null) {
        for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
          String s = (String) iterator.next();
          log.debug("attribute in map referenced by ns : " + s);
        }
      }
    }
    return map;
  }

  private String getMimeType(String[] mimeTypes,
                             PortletData portletData) throws WSRPException {
    if (mimeTypes == null || mimeTypes.length == 0) {
      log.debug("the given array of MimeTypes is empty or null");
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
    List l = portletData.getSupports();
    for (int i = 0; i < mimeTypes.length; i++) {
      String mimeType = mimeTypes[i];
      for (Iterator iterator = l.iterator(); iterator.hasNext();) {
        String supports = ((Supports) iterator.next()).getMimeType();
        if (supports.equalsIgnoreCase(mimeType))
          return mimeType;
      }
    }
    log.debug("No mime type is supported");
    throw new WSRPException(Faults.UNSUPPORTED_MIME_TYPE_FAULT);
  }

  private byte[] managePortletState(PortletContext portletContext) {
    if (conf.isSavePortletStateOnConsumer()) {
      log.debug("Save state on consumer");
      return portletContext.getPortletState();
    }
    log.debug("Save state on producer");
    return null;
  }

  private Templates manageTemplates(RuntimeContext runtimeContext,
                                    WSRPHttpSession session) {
    Templates templates = runtimeContext.getTemplates();
    System.out.println(">>> EXOMAN MarkupOperationsInterfaceImpl.manageTemplates() templates.getRenderTemplate() = " + templates.getRenderTemplate());
    System.out.println(">>> EXOMAN MarkupOperationsInterfaceImpl.manageTemplates() templates.getBlockingActionTemplate() = "
        + templates.getBlockingActionTemplate());
    if (conf.isTemplatesStoredInSession()) { // default is false
      log.debug("Optimized mode : templates store in session");
      if (templates == null) {
        log.debug("Optimized mode : retrieves the template from session");
        templates = transientStateManager.getTemplates(session);
      } else {
        log.debug("Optimized mode : store the templates in session");
        transientStateManager.storeTemplates(templates, session);
      }
    }
    return templates;
  }

  private Integer getSessionTimePeriod() {
    try {
      ExoContainer container = ExoContainerContext.getCurrentContainer();
      WSRPHttpSession wsrpHttpSession = (WSRPHttpSession) container.getComponentInstanceOfType(WSRPHttpSession.class);
      return wsrpHttpSession.getMaxInactiveInterval();
    } catch (Exception e) {
      System.out.println("Exception: MarkupOperationsInterfaceImpl.getSessionTimePeriod: = " + e.getCause());
    }
    return 900;
  }

  private String removeNonValidXMLCharacters(String in) {
    StringBuffer out = new StringBuffer();
    char current;

    if (in == null || ("".equals(in)))
      return "";
    for (int i = 0; i < in.length(); i++) {
      current = in.charAt(i);
      if ((current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF))
          || ((current >= 0xE000) && (current <= 0xFFFD)) || ((current >= 0x10000) && (current <= 0x10FFFF)))
        out.append(current);
    }
    return out.toString();
  }

}
