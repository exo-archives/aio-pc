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

import org.apache.commons.logging.Log;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.wsrp.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp.consumer.GroupSessionMgr;
import org.exoplatform.services.wsrp.consumer.PortletDriver;
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.consumer.URLRewriter;
import org.exoplatform.services.wsrp.consumer.URLTemplateComposer;
import org.exoplatform.services.wsrp.consumer.User;
import org.exoplatform.services.wsrp.consumer.UserSessionMgr;
import org.exoplatform.services.wsrp.consumer.WSRPBaseRequest;
import org.exoplatform.services.wsrp.consumer.WSRPInteractionRequest;
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

import javax.xml.rpc.Call;

/**
 * The implementation of this class is based on the WSRP4J project
 */
public class PortletDriverImpl implements PortletDriver {

  private WSRPPortlet                        portlet     = null;

  private WSRP_v1_Markup_PortType            markupPort  = null;

  private WSRP_v1_PortletManagement_PortType portletManagementPort = null;

  private ConsumerEnvironment                consumer = null;

  private Producer                           producer    = null;

  private CookieProtocol                     initCookie  = CookieProtocol.none;

  private Log                                log;
  
  private OrganizationService                orgService;

  public PortletDriverImpl(ExoContainer cont, WSRPPortlet portlet) throws WSRPException {
    this.consumer = (ConsumerEnvironment) cont.getComponentInstanceOfType(ConsumerEnvironment.class);
    this.orgService = (OrganizationService) cont.getComponentInstanceOfType(OrganizationService.class);
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp.consumer");
    this.portlet = portlet;
    this.producer = consumer.getProducerRegistry().getProducer(portlet.getPortletKey()
                                                                         .getProducerId());
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
      portletDescription = producer.getPortletDescription(getPortlet().getParent());
      String groupID = null;
      if (portletDescription != null) {
        groupID = portletDescription.getGroupID();
        if (groupID != null) {
          GroupSessionMgr groupSession = (GroupSessionMgr) userSession.getGroupSession(groupID);
          groupSession.setInitCookieDone(false);
        }
      }
    }
  }

  private void checkInitCookie(UserSessionMgr userSession) throws WSRPException {
    log.debug("init cookies : " + initCookie.getValue());
    if (initCookie.getValue().equalsIgnoreCase(CookieProtocol._perUser)) {
      log.debug("cookies management per user");
      if (!userSession.isInitCookieDone()) {
        log.debug("Init cookies : " + userSession);
        this.markupPort = userSession.getWSRPMarkupService();
        userSession.setInitCookieRequired(true);
        initCookie(userSession);
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
        this.markupPort = groupSession.getWSRPMarkupService();
        if (!groupSession.isInitCookieDone()) {
          log.debug("Group session in init cookies : " + groupSession);
          groupSession.setInitCookieRequired(true);
          initCookie(userSession);
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
      this.markupPort = userSession.getWSRPMarkupService();
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
    markupParams.setLocales(consumer.getSupportedLocales());
    markupParams.setMimeTypes(consumer.getMimeTypes());
    markupParams.setMode(request.getMode());
    markupParams.setWindowState(request.getWindowState());
    markupParams.setNavigationalState(request.getNavigationalState());
    markupParams.setMarkupCharacterSets(consumer.getCharacterEncodingSet());
    markupParams.setValidateTag(null); // TODO ValidateTag
    // TODO: Set only modes and window states that are supported by the portlet
    // as
    // described in it's portlet description.
    markupParams.setValidNewModes(consumer.getSupportedModes());
    markupParams.setValidNewWindowStates(consumer.getSupportedWindowStates());
    markupParams.setExtensions(null);
    return markupParams;
  }

  private RuntimeContext getRuntimeContext(WSRPBaseRequest request, String baseURL) throws WSRPException {
    if (log.isDebugEnabled())
      log.debug("PortletDriverImpl.getRuntimeContext() baseURL = " + baseURL);
    RuntimeContext runtimeContext = new RuntimeContext();
    runtimeContext.setUserAuthentication(consumer.getUserAuthentication());
    runtimeContext.setPortletInstanceKey(request.getPortletInstanceKey());
    URLTemplateComposer templateComposer = consumer.getTemplateComposer();

    if (templateComposer != null) {
      runtimeContext.setNamespacePrefix(templateComposer.getNamespacePrefix());

      Boolean doesUrlTemplateProcess = null;
      Boolean getTemplatesStoredInSession = null;
      PortletDescription desc = producer.getPortletDescription(getPortlet().getParent());
      if (desc != null) {
        doesUrlTemplateProcess = desc.getDoesUrlTemplateProcessing();
        getTemplatesStoredInSession = desc.getTemplatesStoredInSession();
        if (getTemplatesStoredInSession) {
          //TODO
        }
        Templates templates = null;
        if (doesUrlTemplateProcess != null && doesUrlTemplateProcess.booleanValue()) {
          templates = new Templates();
          if (baseURL != null) {
            // a path should be conform to the template--> "/" + ... + "?" + "portal:componentId=" + portlet_handle ;
            templates.setBlockingActionTemplate(templateComposer.createBlockingActionTemplate(baseURL));
            templates.setRenderTemplate(templateComposer.createRenderTemplate(baseURL));
            templates.setDefaultTemplate(templateComposer.createDefaultTemplate(baseURL));
            templates.setResourceTemplate(templateComposer.createResourceTemplate(baseURL));
            templates.setSecureBlockingActionTemplate(templateComposer.createSecureBlockingActionTemplate(baseURL));
            templates.setSecureRenderTemplate(templateComposer.createSecureRenderTemplate(baseURL));
            templates.setSecureDefaultTemplate(templateComposer.createSecureDefaultTemplate(baseURL));
            templates.setSecureResourceTemplate(templateComposer.createSecureResourceTemplate(baseURL));
          }
        }
        runtimeContext.setTemplates(templates);
      }
    }

    runtimeContext.setSessionID(request.getSessionID());
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
    if (!portlet.isConsumerConfigured()
        && interactionParams.getPortletStateChange()
                            .toString()
                            .equalsIgnoreCase(StateChange._readWrite)) {
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
                                  String path) throws WSRPException {
    checkInitCookie(userSession);
    MarkupResponse response = null;
    try {
      MarkupContext markupContext = markupRequest.getCachedMarkup();
      if (markupContext == null) {
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
        org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
        String password = user.getPassword();
        applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
        /* MAIN INVOKE */
        response = markupPort.getMarkup(request);
      } else {
        log.debug("get cached markup");
        response = new MarkupResponse();
        response.setMarkupContext(markupContext);
      }
      Boolean requiresRewriting = response.getMarkupContext().getRequiresUrlRewriting();
      log.debug("response.getMarkupContext().getMarkupString() = "
          + response.getMarkupContext().getMarkupString());
      log.debug("requires URL rewriting : " + requiresRewriting);
      if (!Boolean.TRUE.equals(requiresRewriting)) {
        URLRewriter urlRewriter = consumer.getURLRewriter();
        String rewrittenMarkup = urlRewriter.rewriteURLs(path, response.getMarkupContext()
                                                                       .getMarkupString());
        if (rewrittenMarkup != null) {
          response.getMarkupContext().setMarkupString(rewrittenMarkup);
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
    } catch (Exception ex){
    	log.error("Remote exception ", ex);
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
    }
    return response;
  }

  public BlockingInteractionResponse performBlockingInteraction(WSRPInteractionRequest actionRequest,
                                                                UserSessionMgr userSession,
                                                                String path) throws WSRPException {
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
      org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
      String password = user.getPassword();
      applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
      /* MAIN INVOKE */
      response = markupPort.performBlockingInteraction(request);
    } catch (InvalidCookieFault cookieFault) {
      resetInitCookie(userSession);
      performBlockingInteraction(actionRequest, userSession, path);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    } catch (Exception ex){
    	log.error("Remote exception ", ex);
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
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
      org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
      String password = user.getPassword();
      applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
      /* MAIN INVOKE */
      response = portletManagementPort.clonePortlet(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    } catch (Exception ex){
    	log.error("Remote exception ", ex);
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
    }
    return response;
  }

  public DestroyPortletsResponse destroyPortlets(String[] portletHandles, UserSessionMgr userSession) throws WSRPException {
    DestroyPortletsRequest request = new DestroyPortletsRequest();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    request.setPortletHandles(portletHandles);
    DestroyPortletsResponse response = null;
    try {
      org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
      String password = user.getPassword();
      applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
      /* MAIN INVOKE */
      response = portletManagementPort.destroyPortlets(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    } catch (Exception ex){
    	log.error("Remote exception ", ex);
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
    }
    return response;
  }

  public ReturnAny releaseSessions(String[] sessionIDs, UserSessionMgr userSession) throws WSRPException {
    checkInitCookie(userSession);
    ReleaseSessionsRequest request = new ReleaseSessionsRequest();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    request.setSessionIDs(sessionIDs);
    ReturnAny response = null;
    try {
      org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
      String password = user.getPassword();
      applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
      /* MAIN INVOKE */
      response = markupPort.releaseSessions(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    }catch (Exception ex){
    	log.error("Remote exception ", ex);
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
    }
    return response;
  }

  /**
   * @see org.exoplatform.services.wsrp.consumer.PortletDriver#initCookie()
   */
  @Deprecated
  public void initCookie() throws WSRPException {
    // Nothing to do since it was deprecated.
  }
  
  public void initCookie(UserSessionMgr userSession) throws WSRPException {
    InitCookieRequest request = new InitCookieRequest();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      log.debug("Registration context used in initCookie : " + regCtx.getRegistrationHandle());
      request.setRegistrationContext(regCtx);
    }
    try {
      log.debug("Call initCookie on Markup Port");
      org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
      String password = user.getPassword();
      applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
      /* MAIN INVOKE */
      markupPort.initCookie(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      log.error("Problem while initializing cookies", wsrpFault);
      throw new WSRPException("Problem while initializing cookies", wsrpFault);
    } catch (Exception ex){
      log.error("Remote exception ", ex);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
    }
  }

  public PortletDescriptionResponse getPortletDescription(UserSessionMgr userSession,
                                                          String[] desiredLocales) throws WSRPException {
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
    	org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
      String password = user.getPassword();
      applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
      /* MAIN INVOKE */
      response = portletManagementPort.getPortletDescription(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    } catch (Exception ex){
    	log.error("Remote exception ", ex);
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
    }
    return response;
  }

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(UserSessionMgr userSession) throws WSRPException {
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
    request.setDesiredLocales(consumer.getSupportedLocales());
    PortletPropertyDescriptionResponse response = null;
    try {
    	org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
      String password = user.getPassword();
      applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
      /* MAIN INVOKE */
      response = portletManagementPort.getPortletPropertyDescription(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    } catch (Exception ex){
    	log.error("Remote exception ", ex);
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
    }
    return response;
  }

  public PropertyList getPortletProperties(String[] names, UserSessionMgr userSession) throws WSRPException {
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
    	org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
      String password = user.getPassword();
      applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
      /* MAIN INVOKE */
      response = portletManagementPort.getPortletProperties(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    } catch (Exception ex){
    	log.error("Remote exception ", ex);
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
    }
    return response;
  }

  public PortletContext setPortletProperties(PropertyList properties, UserSessionMgr userSession) throws WSRPException {
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
    	org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(userSession.getUserID());
      String password = user.getPassword();
      applySecurityParams((org.apache.axis.client.Stub)markupPort, userSession.getUserID(), password);
      /* MAIN INVOKE */
      response = portletManagementPort.setPortletProperties(request);
    } catch (java.rmi.RemoteException wsrpFault) {
      throw new WSRPException();
    } catch (Exception ex){
    	log.error("Remote exception ", ex);
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, ex);
    }
    return response;
  }
  
  private void applySecurityParams(org.apache.axis.client.Stub port, String userID, String password) {
    if (userID != null && password != null) {
      port.setUsername(userID);
      port.setPassword(password);
      port._setProperty(Call.SESSION_MAINTAIN_PROPERTY, Boolean.FALSE);
      port._setProperty(WSHandlerConstants.USER, userID);
    }
  }
  
}
