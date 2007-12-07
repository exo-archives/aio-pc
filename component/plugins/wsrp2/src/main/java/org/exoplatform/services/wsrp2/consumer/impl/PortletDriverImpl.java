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

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp2.consumer.GroupSessionMgr;
import org.exoplatform.services.wsrp2.consumer.PortletDriver;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.URLRewriter;
import org.exoplatform.services.wsrp2.consumer.URLTemplateComposer;
import org.exoplatform.services.wsrp2.consumer.User;
import org.exoplatform.services.wsrp2.consumer.UserSessionMgr;
import org.exoplatform.services.wsrp2.consumer.WSRPBaseRequest;
import org.exoplatform.services.wsrp2.consumer.WSRPEventsRequest;
import org.exoplatform.services.wsrp2.consumer.WSRPInteractionRequest;
import org.exoplatform.services.wsrp2.consumer.WSRPMarkupRequest;
import org.exoplatform.services.wsrp2.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp2.consumer.WSRPResourceRequest;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_Markup_PortType;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_PortletManagement_PortType;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.CookieProtocol;
import org.exoplatform.services.wsrp2.type.DestroyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.EventParams;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetPortletDescription;
import org.exoplatform.services.wsrp2.type.GetPortletProperties;
import org.exoplatform.services.wsrp2.type.GetPortletPropertyDescription;
import org.exoplatform.services.wsrp2.type.GetResource;
import org.exoplatform.services.wsrp2.type.HandleEvents;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.InitCookie;
import org.exoplatform.services.wsrp2.type.InteractionParams;
import org.exoplatform.services.wsrp2.type.InvalidCookieFault;
import org.exoplatform.services.wsrp2.type.MarkupContext;
import org.exoplatform.services.wsrp2.type.MarkupParams;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.MimeRequest;
import org.exoplatform.services.wsrp2.type.NavigationalContext;
import org.exoplatform.services.wsrp2.type.PerformBlockingInteraction;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ReleaseSessions;
import org.exoplatform.services.wsrp2.type.ResourceContext;
import org.exoplatform.services.wsrp2.type.ResourceParams;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SessionParams;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * The implementation of this class is based on the WSRP4J project
 */
public class PortletDriverImpl implements PortletDriver {

  private WSRPPortlet                        portlet               = null;

  private WSRP_v2_Markup_PortType            markupPort            = null;

  private WSRP_v2_PortletManagement_PortType portletManagementPort = null;

  private ConsumerEnvironment                consumer              = null;

  private Producer                           producer              = null;

  private CookieProtocol                     initCookie            = CookieProtocol.none;

  private Log                                log;

  public PortletDriverImpl(ExoContainer cont,
                           WSRPPortlet portlet) throws WSRPException {
    this.consumer = (ConsumerEnvironment) cont.getComponentInstanceOfType(ConsumerEnvironment.class);
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2.consumer");
    this.portlet = portlet;
    this.producer = consumer.getProducerRegistry().getProducer(portlet.getPortletKey().getProducerId());
    portletManagementPort = producer.getPortletManagementInterface();
    ServiceDescription serviceDescription = producer.getServiceDescription(false);
    if (serviceDescription != null) {
      this.initCookie = serviceDescription.getRequiresInitCookie();
      log.debug("Requires cookie initialization : " + initCookie.getValue());
      if (initCookie == null) {
        initCookie = CookieProtocol.none; // TODO - get from config
      }
    }
  }

  public WSRPPortlet getPortlet() {
    return this.portlet;
  }

  private void resetInitCookie(UserSessionMgr userSession) throws WSRPException {
    log.debug("reset cookies");
    if (initCookie.getValue().equalsIgnoreCase(CookieProtocol._none)) {
      userSession.setInitCookieDone(false);
    } else if (initCookie.getValue().equalsIgnoreCase(CookieProtocol._perGroup)) {
      PortletDescription portletDescription = null;
      try {
        portletDescription = producer.getPortletDescription(getPortlet().getParent());
      } catch (WSRPException e) {
        e.printStackTrace();
      }
      String groupID = null;
      if (portletDescription != null) {
        groupID = portletDescription.getGroupID();
      }
      if (groupID != null) {
        GroupSessionMgr groupSession = (GroupSessionMgr) userSession.getGroupSession(groupID);
        groupSession.setInitCookieDone(false);
      }
    }
  }

  private void checkInitCookie(UserSessionMgr userSession) throws WSRPException {
    log.debug("init cookies : " + initCookie.getValue());
    if (initCookie.getValue().equalsIgnoreCase(CookieProtocol._perUser)) {
      log.debug("cookies management per user");
      if (!userSession.isInitCookieDone()) {
        log.debug("Init cookies : " + userSession);
        this.markupPort = userSession.getWSRPBaseService();
        userSession.setInitCookieRequired(true);
        initCookie();
        userSession.setInitCookieDone(true);
      }
    } else if (initCookie.getValue().equalsIgnoreCase(CookieProtocol._perGroup)) {
      log.debug("cookies management per group");
      PortletDescription portletDescription = producer.getPortletDescription(getPortlet().getParent());
      String groupID = null;
      if (portletDescription != null) {
        groupID = portletDescription.getGroupID();
        log.debug("Group Id used for cookies management : " + groupID);
      }
      if (groupID != null) {
        GroupSessionMgr groupSession = (GroupSessionMgr) userSession.getGroupSession(groupID);
        this.markupPort = groupSession.getWSRPBaseService();
        if (!groupSession.isInitCookieDone()) {
          log.debug("Group session in init cookies : " + groupSession);
          groupSession.setInitCookieRequired(true);
          initCookie();
          groupSession.setInitCookieDone(true);
        }
      } else {
        // means either we have no service description from the producer
        // containg the portlet
        // or the producer specified initCookieRequired perGroup but didn't
        // provide
        // a groupID in the portlet description
      }
    } else {
      this.markupPort = userSession.getWSRPBaseService();
    }
  }

  private MarkupParams getMarkupParams(WSRPBaseRequest request) {
    MarkupParams markupParams = new MarkupParams();
    fillMimeRequestParams(markupParams, request);
    return markupParams;
  }

  private RuntimeContext getRuntimeContext(WSRPBaseRequest request,
                                           String path) {
    RuntimeContext runtimeContext = new RuntimeContext();
    runtimeContext.setUserAuthentication(consumer.getUserAuthentication());
    runtimeContext.setPortletInstanceKey(request.getPortletInstanceKey());
    URLTemplateComposer templateComposer = consumer.getTemplateComposer();

    if (templateComposer != null) {
      runtimeContext.setNamespacePrefix(templateComposer.getNamespacePrefix());
    }
    Boolean doesUrlTemplateProcess = null;
    try {
      PortletDescription desc = producer.getPortletDescription(getPortlet().getParent());
      if (desc != null) {
        doesUrlTemplateProcess = desc.getDoesUrlTemplateProcessing();
      }
    } catch (WSRPException e) {
      // do nothing since exception has been logged already
      // continue with assumption that portlet does not support template
      // processing
    }
    if (doesUrlTemplateProcess != null && templateComposer != null && doesUrlTemplateProcess.booleanValue()) {
      // If path starts with protocol then don't use templateComposer for create
      // templates
      if (path != null) {
        if (path.startsWith(URLTemplateComposerImpl.NON_SECURE_PROTOCOL) || path.startsWith(URLTemplateComposerImpl.SECURE_PROTOCOL)) {
          Templates templates = new Templates();
          templates.setBlockingActionTemplate(path);
          templates.setRenderTemplate(path);
          templates.setDefaultTemplate(path);
          templates.setResourceTemplate(path);
          templates.setSecureBlockingActionTemplate(URLTemplateComposerImpl.SECURE_PROTOCOL
              + path.substring(URLTemplateComposerImpl.NON_SECURE_PROTOCOL.length()));
          templates.setSecureRenderTemplate(URLTemplateComposerImpl.SECURE_PROTOCOL
              + path.substring(URLTemplateComposerImpl.NON_SECURE_PROTOCOL.length()));
          templates.setSecureDefaultTemplate(URLTemplateComposerImpl.SECURE_PROTOCOL
              + path.substring(URLTemplateComposerImpl.NON_SECURE_PROTOCOL.length()));
          templates.setSecureResourceTemplate(URLTemplateComposerImpl.SECURE_PROTOCOL
              + path.substring(URLTemplateComposerImpl.NON_SECURE_PROTOCOL.length()));
          runtimeContext.setTemplates(templates);

          runtimeContext.setSessionParams(new SessionParams(null, request.getSessionID()));
          runtimeContext.setExtensions(null);
          return runtimeContext;
        }
      }
      Templates templates = new Templates();
      templates.setBlockingActionTemplate(templateComposer.createBlockingActionTemplate(path));
      templates.setRenderTemplate(templateComposer.createRenderTemplate(path));
      templates.setDefaultTemplate(templateComposer.createDefaultTemplate(path));
      templates.setResourceTemplate(templateComposer.createResourceTemplate(path));
      templates.setSecureBlockingActionTemplate(templateComposer.createSecureBlockingActionTemplate(path));
      templates.setSecureRenderTemplate(templateComposer.createSecureRenderTemplate(path));
      templates.setSecureDefaultTemplate(templateComposer.createSecureDefaultTemplate(path));
      templates.setSecureResourceTemplate(templateComposer.createSecureResourceTemplate(path));
      runtimeContext.setTemplates(templates);
    }
    runtimeContext.setSessionParams(new SessionParams(null, request.getSessionID()));
    // runtimeContext.setPageState(pageState);
    // runtimeContext.setPortletStates(portletStates);
    runtimeContext.setExtensions(null);
    return runtimeContext;
  }

  private UserContext getUserContext(UserSessionMgr userSession) {
    UserContext userContext = null;
    if (userSession.getUserID() != null) {
      User user = consumer.getUserRegistry().getUser(userSession.getUserID());
      if (user != null) {
        userContext = user.getUserContext();
      }
    }
    // workaround for Oracle bug, always send a userContext with dummy value
    // if none was provided
    if (userContext == null) {
      userContext = new UserContext();
      userContext.setUserContextKey("dummyUserContextKey");
    }
    return userContext;
  }

  private InteractionParams getInteractionParams(WSRPInteractionRequest actionRequest) {
    InteractionParams interactionParams = new InteractionParams();
    interactionParams.setPortletStateChange(consumer.getPortletStateChange());
    if (!portlet.isConsumerConfigured() && interactionParams.getPortletStateChange().toString().equalsIgnoreCase(StateChange._readWrite)) {
      interactionParams.setPortletStateChange(StateChange.cloneBeforeWrite);
    }
    interactionParams.setInteractionState(actionRequest.getInteractionState());
    interactionParams.setFormParameters(actionRequest.getFormParameters());
    interactionParams.setUploadContexts(actionRequest.getUploadContexts());
    interactionParams.setExtensions(actionRequest.getExtensions());
    return interactionParams;
  }

  public MarkupResponse getMarkup(WSRPMarkupRequest markupRequest,
                                  UserSessionMgr userSession,
                                  String path) throws WSRPException {
    checkInitCookie(userSession);
    MarkupResponse response = null;
    try {
      MarkupContext markupContext = markupRequest.getCachedMarkup();
      if (markupContext == null) {
        log.debug("get non cached markup");
        GetMarkup request = new GetMarkup();
        request.setPortletContext(getPortlet().getPortletContext());
        request.setMarkupParams(getMarkupParams(markupRequest));
        request.setRuntimeContext(getRuntimeContext(markupRequest, path));
        RegistrationContext regCtx = producer.getRegistrationContext();
        if (regCtx != null) {
          log.debug("Registration context used in getMarkup : " + regCtx.getRegistrationHandle());
          request.setRegistrationContext(regCtx);
        }
        UserContext userCtx = getUserContext(userSession);
        if (userCtx != null) {
          request.setUserContext(userCtx);
        }
        /* MAIN INVOKE */
        response = markupPort.getMarkup(request);
      } else {
        log.debug("get cached markup");
        response = new MarkupResponse();
        response.setMarkupContext(markupContext);
      }
      markupContext = response.getMarkupContext();
      Boolean requiresRewriting = markupContext.getRequiresRewriting();
      log.debug("markupContext.getItemString() = " + markupContext.getItemString());
      log.debug("requires URL rewriting : " + requiresRewriting);
      if (Boolean.FALSE.equals(requiresRewriting)) {
        URLRewriter urlRewriter = consumer.getURLRewriter();
        String rewrittenMarkup = urlRewriter.rewriteURLs(path, markupContext.getItemString());
        if (rewrittenMarkup != null) {
          markupContext.setItemString(rewrittenMarkup);
        }
        log.debug("rewrittenMarkup = " + rewrittenMarkup);
      }
    } catch (InvalidCookieFault cookieFault) {
      log.error("Problem with cookies ", cookieFault);
      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
      resetInitCookie(userSession);
      getMarkup(markupRequest, userSession, path);
    } catch (java.rmi.RemoteException wsrpFault) {
      log.error("Remote exception ", wsrpFault);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, wsrpFault);
    }
    return response;
  }

  public BlockingInteractionResponse performBlockingInteraction(WSRPInteractionRequest actionRequest,
                                                                UserSessionMgr userSession,
                                                                String path) throws WSRPException {
    checkInitCookie(userSession);
    BlockingInteractionResponse response = null;
    try {
      PerformBlockingInteraction request = new PerformBlockingInteraction();
      request.setPortletContext(getPortlet().getPortletContext());
      request.setInteractionParams(getInteractionParams(actionRequest));
      request.setMarkupParams(getMarkupParams(actionRequest));
      request.setRuntimeContext(getRuntimeContext(actionRequest, path));
      RegistrationContext regCtx = producer.getRegistrationContext();
      if (regCtx != null) {
        request.setRegistrationContext(regCtx);
      }
      UserContext userCtx = getUserContext(userSession);
      if (userCtx != null) {
        request.setUserContext(userCtx);
      }
      /* MAIN INVOKE */
      response = markupPort.performBlockingInteraction(request);
    } catch (InvalidCookieFault cookieFault) {
      resetInitCookie(userSession);
      performBlockingInteraction(actionRequest, userSession, path);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public PortletContext clonePortlet(UserSessionMgr userSession) throws WSRPException {
    ClonePortlet request = new ClonePortlet();
    request.setPortletContext(getPortlet().getPortletContext());
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    UserContext userCtx = getUserContext(userSession);
    if (userCtx != null) {
      request.setUserContext(userCtx);
    }
    PortletContext response = null;
    try {
      response = portletManagementPort.clonePortlet(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public DestroyPortletsResponse destroyPortlets(String[] portletHandles,
                                                 UserSessionMgr userSession) throws WSRPException {
    DestroyPortlets request = new DestroyPortlets();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    request.setPortletHandles(portletHandles);
    DestroyPortletsResponse response = null;
    try {
      response = portletManagementPort.destroyPortlets(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public ReturnAny releaseSessions(String[] sessionIDs,
                                   UserSessionMgr userSession) throws WSRPException {
    checkInitCookie(userSession);
    ReleaseSessions request = new ReleaseSessions();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    request.setSessionIDs(sessionIDs);
    ReturnAny response = null;
    try {
      /* MAIN INVOKE */
      response = markupPort.releaseSessions(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public void initCookie() throws WSRPException {
    InitCookie request = new InitCookie();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      log.debug("Registration context use d in initCookie : " + regCtx.getRegistrationHandle());
      request.setRegistrationContext(regCtx);
    }
    try {
      log.debug("Call initCookie on Markup Port");
      /* MAIN INVOKE */
      markupPort.initCookie(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      log.error("Problem while initializing cookies", wsrpFault);
      throw new WSRPException("Problem while initializing cookies", wsrpFault);
    }
  }

  public PortletDescriptionResponse getPortletDescription(UserSessionMgr userSession,
                                                          String[] desiredLocales) throws WSRPException {
    GetPortletDescription request = new GetPortletDescription();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    request.setPortletContext(getPortlet().getPortletContext());
    UserContext userCtx = getUserContext(userSession);
    if (userCtx != null) {
      request.setUserContext(userCtx);
    }
    request.setDesiredLocales(desiredLocales);
    PortletDescriptionResponse response = null;
    try {
      response = portletManagementPort.getPortletDescription(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(UserSessionMgr userSession) throws WSRPException {
    GetPortletPropertyDescription request = new GetPortletPropertyDescription();
    request.setPortletContext(getPortlet().getPortletContext());
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    UserContext userCtx = getUserContext(userSession);
    if (userCtx != null) {
      request.setUserContext(userCtx);
    }
    request.setDesiredLocales(consumer.getSupportedLocales());
    PortletPropertyDescriptionResponse response = null;
    try {
      response = portletManagementPort.getPortletPropertyDescription(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public PropertyList getPortletProperties(String[] names,
                                           UserSessionMgr userSession) throws WSRPException {
    GetPortletProperties request = new GetPortletProperties();
    request.setPortletContext(getPortlet().getPortletContext());
    request.setNames(names);
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    UserContext userCtx = getUserContext(userSession);
    if (userCtx != null) {
      request.setUserContext(userCtx);
    }
    PropertyList response = null;
    try {
      response = portletManagementPort.getPortletProperties(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public PortletContext setPortletProperties(PropertyList properties,
                                             UserSessionMgr userSession) throws WSRPException {
    SetPortletProperties request = new SetPortletProperties();
    request.setPortletContext(getPortlet().getPortletContext());
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    UserContext userCtx = getUserContext(userSession);
    if (userCtx != null) {
      request.setUserContext(userCtx);
    }
    request.setPropertyList(properties);
    PortletContext response = null;
    try {
      response = portletManagementPort.setPortletProperties(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  // Below changes of WSRP2 spec

  public ResourceResponse getResource(WSRPResourceRequest resourceRequest,
                                      UserSessionMgr userSession,
                                      String path) throws WSRPException {
    checkInitCookie(userSession);
    ResourceResponse response = null;
    try {
      ResourceContext resourceContext = resourceRequest.getCachedResource();
      if (resourceContext == null) {
        log.debug("get non cached resource");
        GetResource request = new GetResource();
        request.setPortletContext(getPortlet().getPortletContext());
        request.setResourceParams(getResourceParams(resourceRequest));
        request.setRuntimeContext(getRuntimeContext(resourceRequest, path));
        RegistrationContext regCtx = producer.getRegistrationContext();
        if (regCtx != null) {
          request.setRegistrationContext(regCtx);
        }
        UserContext userCtx = getUserContext(userSession);
        if (userCtx != null) {
          request.setUserContext(userCtx);
        }
        /* MAIN INVOKE */
        response = markupPort.getResource(request);
      } else {
        log.debug("get cached resource");
        response = new ResourceResponse();
        response.setResourceContext(resourceContext);
      }
      resourceContext = response.getResourceContext();
      Boolean requiresRewriting = resourceContext.getRequiresRewriting();
      log.debug("resourceContext.getItemString() = " + resourceContext.getItemString());
      log.debug("requires URL rewriting : " + requiresRewriting);
      requiresRewriting = requiresRewriting == null ? Boolean.FALSE : requiresRewriting;
      if (requiresRewriting.booleanValue()) {
        URLRewriter urlRewriter = consumer.getURLRewriter();
        String rewrittenResource = urlRewriter.rewriteURLs(path, resourceContext.getItemString());
        if (rewrittenResource != null) {
          resourceContext.setItemString(rewrittenResource);
        }
        log.debug("rewrittenResource = " + rewrittenResource);
      }
    } catch (InvalidCookieFault cookieFault) {
      log.error("Problem with cookies ", cookieFault);
      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
      resetInitCookie(userSession);
      getResource(resourceRequest, userSession, path);
    } catch (java.rmi.RemoteException wsrpFault) {
      log.error("Remote exception ", wsrpFault);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, wsrpFault);
    }
    return response;
  }

  private ResourceParams getResourceParams(WSRPResourceRequest resourceRequest) {
    ResourceParams resourceParams = new ResourceParams();
    fillMimeRequestParams(resourceParams, resourceRequest);
    resourceParams.setFormParameters(resourceRequest.getFormParameters());
    resourceParams.setUploadContexts(resourceRequest.getUploadContexts());
    resourceParams.setResourceID(resourceRequest.getResourceID());
    resourceParams.setPortletStateChange(resourceRequest.getPortletStateChange());
    resourceParams.setResourceState(resourceRequest.getResourceState());
    resourceParams.setResourceCacheability(resourceRequest.getResourceCacheability());
    return resourceParams;
  }

  private void fillMimeRequestParams(MimeRequest params,
                                     WSRPBaseRequest request) {
    ClientData clientData = new ClientData();
    // lets just set this to the consumer agent for now
    if (producer.getRegistrationData() != null)
      clientData.setUserAgent(producer.getRegistrationData().getConsumerAgent());
    params.setClientData(clientData);
    params.setSecureClientCommunication(request.isSecureClientCommunication());
    params.setLocales(request.getLocales());//consumer.getSupportedLocales());
    params.setMimeTypes(request.getMimeTypes());//consumer.getMimeTypes());
    params.setMode(request.getMode());
    params.setWindowState(request.getWindowState());
    params.setMarkupCharacterSets(consumer.getCharacterEncodingSet());
    params.setValidateTag(request.getValidateTag());

    // TODO: Set only modes and window states that are supported by the portlet
    // as described in it's portlet description.
    params.setValidNewModes(request.getValidNewModes());//consumer.getSupportedModes());
    params.setValidNewWindowStates(request.getValidNewWindowStates());//consumer.getSupportedWindowStates());

    params.setNavigationalContext(getNavigationalContext(request));

    params.setExtensions(null);
  }

  private NavigationalContext getNavigationalContext(WSRPBaseRequest request) {
    NavigationalContext navCont = new NavigationalContext();
    navCont.setOpaqueValue(request.getNavigationalState());
    navCont.setPublicValues(request.getNavigationalValues());
    navCont.setExtensions(null); // TODO EXOMAN
    return navCont;
  }

  public HandleEventsResponse handleEvents(WSRPEventsRequest eventRequest,
                                           UserSessionMgr userSession,
                                           String path) throws WSRPException {
    checkInitCookie(userSession);
    HandleEventsResponse response = null;
    try {
      HandleEvents request = new HandleEvents();
      request.setPortletContext(getPortlet().getPortletContext());
      request.setEventParams(getEventParams(eventRequest));
      request.setMarkupParams(getMarkupParams(eventRequest));
      request.setRuntimeContext(getRuntimeContext(eventRequest, path));
      RegistrationContext regCtx = producer.getRegistrationContext();
      if (regCtx != null) {
        request.setRegistrationContext(regCtx);
      }
      UserContext userCtx = getUserContext(userSession);
      if (userCtx != null) {
        request.setUserContext(userCtx);
      }
      /* MAIN INVOKE */
      response = markupPort.handleEvents(request);
    } catch (InvalidCookieFault cookieFault) {
      resetInitCookie(userSession);
      handleEvents(eventRequest, userSession, path);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  private EventParams getEventParams(WSRPEventsRequest eventRequest) {
    EventParams eventParams = new EventParams();
    eventParams.setEvents(eventRequest.getEvents());
    eventParams.setPortletStateChange(consumer.getPortletStateChange());
    if (!portlet.isConsumerConfigured() && eventParams.getPortletStateChange().toString().equalsIgnoreCase(StateChange._readWrite)) {
      eventParams.setPortletStateChange(StateChange.cloneBeforeWrite);
    }
    eventParams.setExtensions(null);
    return eventParams;
  }

}
