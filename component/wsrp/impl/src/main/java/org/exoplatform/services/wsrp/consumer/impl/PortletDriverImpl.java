/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.impl;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp.consumer.GroupSessionMgr;
import org.exoplatform.services.wsrp.consumer.InteractionRequest;
import org.exoplatform.services.wsrp.consumer.PortletDriver;
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.consumer.URLRewriter;
import org.exoplatform.services.wsrp.consumer.URLTemplateComposer;
import org.exoplatform.services.wsrp.consumer.User;
import org.exoplatform.services.wsrp.consumer.UserSessionMgr;
import org.exoplatform.services.wsrp.consumer.WSRPBaseRequest;
import org.exoplatform.services.wsrp.consumer.WSRPMarkupRequest;
import org.exoplatform.services.wsrp.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.intf.WSRP_v1_Markup_PortType;
import org.exoplatform.services.wsrp.intf.WSRP_v1_PortletManagement_PortType;
import org.exoplatform.services.wsrp.type.BlockingInteractionRequest;
import org.exoplatform.services.wsrp.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp.type.ClientData;
import org.exoplatform.services.wsrp.type.ClonePortletRequest;
import org.exoplatform.services.wsrp.type.CookieProtocol;
import org.exoplatform.services.wsrp.type.DestroyPortletsRequest;
import org.exoplatform.services.wsrp.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp.type.GetPortletPropertiesRequest;
import org.exoplatform.services.wsrp.type.InitCookieRequest;
import org.exoplatform.services.wsrp.type.InteractionParams;
import org.exoplatform.services.wsrp.type.InvalidCookieFault;
import org.exoplatform.services.wsrp.type.MarkupContext;
import org.exoplatform.services.wsrp.type.MarkupParams;
import org.exoplatform.services.wsrp.type.MarkupRequest;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.PortletDescriptionRequest;
import org.exoplatform.services.wsrp.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp.type.PortletPropertyDescriptionRequest;
import org.exoplatform.services.wsrp.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp.type.PropertyList;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ReleaseSessionsRequest;
import org.exoplatform.services.wsrp.type.ReturnAny;
import org.exoplatform.services.wsrp.type.RuntimeContext;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.wsrp.type.SetPortletPropertiesRequest;
import org.exoplatform.services.wsrp.type.StateChange;
import org.exoplatform.services.wsrp.type.Templates;
import org.exoplatform.services.wsrp.type.UserContext;


/**
 * The implementation of this class is based on the WSRP4J
 * project
 */
public class PortletDriverImpl implements PortletDriver {

  private WSRPPortlet portlet = null;
  private WSRP_v1_Markup_PortType markupPort = null;
  private WSRP_v1_PortletManagement_PortType portletPort = null;
  private ConsumerEnvironment consumerEnv = null;
  private Producer producer = null;

  private CookieProtocol initCookie = CookieProtocol.none;
  private Log log;


  public PortletDriverImpl(ExoContainer cont, WSRPPortlet portlet) throws WSRPException {
    this.consumerEnv = (ConsumerEnvironment) cont.
        getComponentInstanceOfType(ConsumerEnvironment.class);
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp.consumer");
    this.portlet = portlet;
    this.producer = consumerEnv.getProducerRegistry().
        getProducer(portlet.getPortletKey().getProducerId());
    portletPort = producer.getPortletManagementInterface();
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
        // means either we have no service description from the producer containg the portlet
        // or the producer specified initCookieRequired perGroup but didn't provide
        // a groupID in the portlet description
      }
    } else {
      this.markupPort = userSession.getWSRPBaseService();
    }
  }

  private MarkupParams getMarkupParams(WSRPBaseRequest request) {
    MarkupParams markupParams = new MarkupParams();
    ClientData clientData = new ClientData();
    // lets just set this to the consumer agent for now
    if (producer.getRegistrationData() != null)
      clientData.setUserAgent(producer.getRegistrationData().getConsumerAgent());
    markupParams.setClientData(clientData);
    markupParams.setSecureClientCommunication(false);
    markupParams.setLocales(consumerEnv.getSupportedLocales());
    markupParams.setMimeTypes(consumerEnv.getMimeTypes());
    markupParams.setMode(request.getMode());
    markupParams.setWindowState(request.getWindowState());
    markupParams.setNavigationalState(request.getNavigationalState());
    markupParams.setMarkupCharacterSets(consumerEnv.getCharacterEncodingSet());
    markupParams.setValidateTag(null); // TODO ValidateTag
    // TODO: Set only modes and window states that are supported by the portlet as
    //       described in it's portlet description.
    markupParams.setValidNewModes(consumerEnv.getSupportedModes());
    markupParams.setValidNewWindowStates(consumerEnv.getSupportedWindowStates());
    markupParams.setExtensions(null);
    return markupParams;
  }

  private RuntimeContext getRuntimeContext(WSRPBaseRequest request, String path) {
    RuntimeContext runtimeContext = new RuntimeContext();    
    runtimeContext.setUserAuthentication(consumerEnv.getUserAuthentication());
    runtimeContext.setPortletInstanceKey(request.getPortletInstanceKey());
    URLTemplateComposer templateComposer = consumerEnv.getTemplateComposer();

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
      // continue with assumption that portlet does not support template processing
    }
    if (doesUrlTemplateProcess != null && templateComposer != null
        && doesUrlTemplateProcess.booleanValue()) {
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
    runtimeContext.setSessionID(request.getSessionID());
    runtimeContext.setExtensions(null);
    return runtimeContext;
  }

  private UserContext getUserContext(UserSessionMgr userSession) {
    UserContext userContext = null;
    if (userSession.getUserID() != null) {
      User user = consumerEnv.getUserRegistry().getUser(userSession.getUserID());

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

  private InteractionParams getInteractionParams(InteractionRequest actionRequest) {
    InteractionParams interactionParams = new InteractionParams();
    interactionParams.setPortletStateChange(consumerEnv.getPortletStateChange());
    if (!portlet.isConsumerConfigured() &&
        interactionParams.getPortletStateChange().toString().equalsIgnoreCase(StateChange._readWrite)) {
      interactionParams.setPortletStateChange(StateChange.cloneBeforeWrite);
    }
    interactionParams.setInteractionState(actionRequest.getInteractionState());
    interactionParams.setFormParameters(actionRequest.getFormParameters());
    interactionParams.setUploadContexts(null);
    interactionParams.setExtensions(null);
    return interactionParams;
  }

  public MarkupResponse getMarkup(WSRPMarkupRequest markupRequest,
                                  UserSessionMgr userSession,
                                  String path)
      throws WSRPException {
    checkInitCookie(userSession);
    MarkupResponse response = null;
    try {
      MarkupContext markupContext = null;
      if ((markupContext = markupRequest.getCachedMarkup()) == null) {
        log.debug("get non cached markup");
        MarkupRequest request = new MarkupRequest();
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
          request.setUserContext(getUserContext(userSession));
        }
        response = markupPort.getMarkup(request);
      } else {
        log.debug("get cached markup");
        response = new MarkupResponse();
        response.setMarkupContext(markupContext);
      }
      Boolean requiresRewriting = response.getMarkupContext().getRequiresUrlRewriting();
      log.debug("requires URL rewriting : " + requiresRewriting);
      requiresRewriting = requiresRewriting == null ? Boolean.FALSE : requiresRewriting;
      if (requiresRewriting.booleanValue()) {
        URLRewriter urlRewriter = consumerEnv.getURLRewriter();

        String rewrittenMarkup = urlRewriter.rewriteURLs(path, response.getMarkupContext().getMarkupString());
        if (rewrittenMarkup != null) {
          response.getMarkupContext().setMarkupString(rewrittenMarkup);
        }
      }
    } catch (InvalidCookieFault cookieFault) {
      log.error("Problem with cookies ", cookieFault);
      //throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
      resetInitCookie(userSession);
      getMarkup(markupRequest, userSession, path);
    } catch (java.rmi.RemoteException wsrpFault) {
      log.error("Remote exception ", wsrpFault);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, wsrpFault);
    }
    return response;
  }

  public BlockingInteractionResponse performBlockingInteraction(InteractionRequest actionRequest,
                                                                UserSessionMgr userSession,
                                                                String path)
      throws WSRPException {
    checkInitCookie(userSession);
    BlockingInteractionResponse response = null;
    try {
      BlockingInteractionRequest request = new BlockingInteractionRequest();
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
    ClonePortletRequest request = new ClonePortletRequest();
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
      response = portletPort.clonePortlet(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }

    return response;
  }

  public DestroyPortletsResponse destroyPortlets(String[] portletHandles,
                                                 UserSessionMgr userSession)
      throws WSRPException {
    DestroyPortletsRequest request = new DestroyPortletsRequest();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    request.setPortletHandles(portletHandles);
    DestroyPortletsResponse response = null;
    try {
      response = portletPort.destroyPortlets(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public ReturnAny releaseSessions(String[] sessionIDs,
                                   UserSessionMgr userSession)
      throws WSRPException {
    checkInitCookie(userSession);
    ReleaseSessionsRequest request = new ReleaseSessionsRequest();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    request.setSessionIDs(sessionIDs);
    ReturnAny response = null;
    try {
      response = markupPort.releaseSessions(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public void initCookie() throws WSRPException {
    InitCookieRequest request = new InitCookieRequest();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      log.debug("Registration context use d in initCookie : " + regCtx.getRegistrationHandle());
      request.setRegistrationContext(regCtx);
    }
    try {
      log.debug("Call initCookie on Markup Port");
      markupPort.initCookie(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      log.error("Problem while initializing cookies", wsrpFault);
      throw new WSRPException("Problem while initializing cookies", wsrpFault);
    }
  }

  public PortletDescriptionResponse getPortletDescription(UserSessionMgr userSession,
                                                          String[] desiredLocales)
      throws WSRPException {
    PortletDescriptionRequest request = new PortletDescriptionRequest();
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
      response = portletPort.getPortletDescription(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }


  public PortletPropertyDescriptionResponse getPortletPropertyDescription(UserSessionMgr userSession)
      throws WSRPException {
    PortletPropertyDescriptionRequest request = new PortletPropertyDescriptionRequest();
    request.setPortletContext(getPortlet().getPortletContext());
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    UserContext userCtx = getUserContext(userSession);
    if (userCtx != null) {
      request.setUserContext(userCtx);
    }
    request.setDesiredLocales(consumerEnv.getSupportedLocales());
    PortletPropertyDescriptionResponse response = null;
    try {
      response = portletPort.getPortletPropertyDescription(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public PropertyList getPortletProperties(String[] names,
                                           UserSessionMgr userSession)
      throws WSRPException {
    GetPortletPropertiesRequest request = new GetPortletPropertiesRequest();
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
      response = portletPort.getPortletProperties(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }

  public PortletContext setPortletProperties(PropertyList properties,
                                             UserSessionMgr userSession)
      throws WSRPException {
    SetPortletPropertiesRequest request = new SetPortletPropertiesRequest();
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
      response = portletPort.setPortletProperties(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }
    return response;
  }
}
