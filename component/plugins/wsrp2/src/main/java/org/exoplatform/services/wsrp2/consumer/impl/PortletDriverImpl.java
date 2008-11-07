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

import java.util.List;

import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.wsrp2.WSRPConstants;
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
import org.exoplatform.services.wsrp2.intf.InconsistentParameters;
import org.exoplatform.services.wsrp2.intf.InvalidUserCategory;
import org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.CookieProtocol;
import org.exoplatform.services.wsrp2.type.DestroyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.EventParams;
import org.exoplatform.services.wsrp2.type.Extension;
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
import org.exoplatform.services.wsrp2.type.MimeResponse;
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
import org.exoplatform.services.wsrp2.type.SessionContext;
import org.exoplatform.services.wsrp2.type.SessionParams;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.type.UpdateResponse;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * The implementation of this class is based on the WSRP4J project Author :
 * Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua
 */
public class PortletDriverImpl implements PortletDriver {

  private WSRPPortlet                     portlet               = null;

  private WSRPV2MarkupPortType            markupPort            = null;

  private WSRPV2PortletManagementPortType portletManagementPort = null;

  private ConsumerEnvironment             consumer              = null;

  private Producer                        producer              = null;

  private CookieProtocol                  initCookie            = CookieProtocol.NONE;

  private Log                             LOG;

  public PortletDriverImpl(ExoContainer cont, WSRPPortlet portlet) throws WSRPException {
    this.consumer = (ConsumerEnvironment) cont.getComponentInstanceOfType(ConsumerEnvironment.class);
    this.LOG = ExoLogger.getLogger("org.exoplatform.services.wsrp2.consumer");
    this.portlet = portlet;
    this.producer = consumer.getProducerRegistry().getProducer(portlet.getPortletKey()
                                                                      .getProducerId());
    portletManagementPort = producer.getPortletManagementInterface();
    ServiceDescription serviceDescription = producer.getServiceDescription(false);
    if (serviceDescription != null) {
      this.initCookie = serviceDescription.getRequiresInitCookie();
      LOG.debug("Requires cookie initialization : " + initCookie.value());
      if (initCookie == null) {
        initCookie = CookieProtocol.NONE; // TODO - get from config
      }
    }
  }

  public WSRPPortlet getPortlet() {
    return this.portlet;
  }

  private void resetInitCookie(UserSessionMgr userSession) throws WSRPException {
    LOG.debug("reset cookies");
    if (initCookie.value().equalsIgnoreCase(CookieProtocol.NONE.value())) {
      userSession.setInitCookieDone(false);
    } else if (initCookie.value().equalsIgnoreCase(CookieProtocol.PER_GROUP.value())) {
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
    LOG.debug("init cookies : " + initCookie.value());
    if (initCookie.value().equalsIgnoreCase(CookieProtocol.PER_USER.value())) {
      LOG.debug("cookies management per user");
      if (!userSession.isInitCookieDone()) {
        LOG.debug("Init cookies : " + userSession);
        this.markupPort = userSession.getWSRPMarkupService();
        userSession.setInitCookieRequired(true);
        initCookie(userSession);
        userSession.setInitCookieDone(true);
      }
    } else if (initCookie.value().equalsIgnoreCase(CookieProtocol.PER_GROUP.value())) {
      LOG.debug("cookies management per group");
      PortletDescription portletDescription = producer.getPortletDescription(getPortlet().getParent());
      String groupID = null;
      if (portletDescription != null) {
        groupID = portletDescription.getGroupID();
        LOG.debug("Group Id used for cookies management : " + groupID);
      }
      if (groupID != null) {
        GroupSessionMgr groupSession = (GroupSessionMgr) userSession.getGroupSession(groupID);
        this.markupPort = groupSession.getWSRPMarkupService();
        if (!groupSession.isInitCookieDone()) {
          LOG.debug("Group session in init cookies : " + groupSession);
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
    fillMimeRequestParams(markupParams, request);
    return markupParams;
  }

  private RuntimeContext getRuntimeContext(WSRPBaseRequest request, String baseURL) throws WSRPException {
    if (LOG.isDebugEnabled())
      LOG.debug("PortletDriverImpl.getRuntimeContext() baseURL = " + baseURL);
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
        doesUrlTemplateProcess = desc.isDoesUrlTemplateProcessing();
        getTemplatesStoredInSession = desc.isTemplatesStoredInSession();
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

    SessionParams sessionParams = new SessionParams();
    sessionParams.setSessionID(request.getSessionID());
    runtimeContext.setSessionParams(sessionParams);
    runtimeContext.setPageState(null);//pageState);
    runtimeContext.setPortletStates(null);//portletStates);
    runtimeContext.getExtensions().clear();
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
                            .value()
                            .equalsIgnoreCase(StateChange.READ_WRITE.value())) {
      interactionParams.setPortletStateChange(StateChange.CLONE_BEFORE_WRITE);
    }
    interactionParams.setInteractionState(actionRequest.getInteractionState());
    interactionParams.getFormParameters().addAll(actionRequest.getFormParameters());
    interactionParams.getUploadContexts().addAll(actionRequest.getUploadContexts());
    interactionParams.getExtensions().addAll(actionRequest.getExtensions());
    return interactionParams;
  }

  public MarkupResponse getMarkup(WSRPMarkupRequest markupRequest,
                                  UserSessionMgr userSession,
                                  String baseURL) throws WSRPException {
    System.out.println(">>> EXOMAN PortletDriverImpl.getMarkup() 1 = " + 1);
    checkInitCookie(userSession);
    MarkupResponse response = null;
    try {
      MarkupContext markupContext = markupRequest.getCachedMarkup();
      if (markupContext == null) {
        LOG.debug("get non cached markup");
        GetMarkup request = new GetMarkup();
        request.setPortletContext(getPortlet().getPortletContext());
        request.setMarkupParams(getMarkupParams(markupRequest));
        request.setRuntimeContext(getRuntimeContext(markupRequest, baseURL));
        RegistrationContext regCtx = producer.getRegistrationContext();
        if (regCtx != null) {
          LOG.debug("Registration context used in getMarkup : " + regCtx.getRegistrationHandle());
          request.setRegistrationContext(regCtx);
        }
        UserContext userCtx = getUserContext(userSession);
        if (userCtx != null) {
          request.setUserContext(userCtx);
        }
        Holder<MarkupContext> holderMarkupContext = new Holder<MarkupContext>();
        Holder<SessionContext> holderSessionContext = new Holder<SessionContext>();
        Holder<List<Extension>> holderListExtension = new Holder<List<Extension>>();
        /* MAIN INVOKE */
        markupPort.getMarkup(request.getRegistrationContext(),
                             request.getPortletContext(),
                             request.getRuntimeContext(),
                             request.getUserContext(),
                             request.getMarkupParams(),
                             holderMarkupContext,
                             holderSessionContext,
                             holderListExtension);
      } else {
        LOG.debug("get cached markup");
        response = new MarkupResponse();
        response.setMarkupContext(markupContext);
      }
      markupContext = response.getMarkupContext();
      Boolean requiresRewriting = markupContext.isRequiresRewriting();
      LOG.debug("requires URL rewriting : " + requiresRewriting);
      String content = getContent(markupContext);

      if (markupContext.getMimeType().startsWith("text/")) {
        String rewrittenMarkup = null;
        if (requiresRewriting) {
          URLRewriter urlRewriter = consumer.getURLRewriter();
          rewrittenMarkup = urlRewriter.rewriteURLs(baseURL, content);
          LOG.debug("rewrittenMarkup = " + rewrittenMarkup);
          if (rewrittenMarkup != null) {
            markupContext.setItemString(rewrittenMarkup);
            try {
              markupContext.setItemBinary(markupContext.getItemString().getBytes("utf-8"));
            } catch (java.io.UnsupportedEncodingException e) {
              markupContext.setItemBinary(markupContext.getItemString().getBytes());
            }
          }
        } else {
          String oldBaseURL = baseURL + WSRPConstants.NEXT_PARAM + Constants.TYPE_PARAMETER + "="
              + WSRPConstants.URL_TYPE_BLOCKINGACTION;
          String newBaseURL = baseURL + WSRPConstants.NEXT_PARAM + Constants.TYPE_PARAMETER + "="
              + PCConstants.ACTION_STRING;
          rewrittenMarkup = content.replace(oldBaseURL, newBaseURL);
          markupContext.setItemString(rewrittenMarkup);
          try {
            markupContext.setItemBinary(markupContext.getItemString().getBytes("utf-8"));
          } catch (java.io.UnsupportedEncodingException e) {
            markupContext.setItemBinary(markupContext.getItemString().getBytes());
          }
        }
      }
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (InvalidCookieFault cookieFault) {
//      LOG.error("Problem with cookies ", cookieFault);
//      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
//      resetInitCookie(userSession);
//      getMarkup(markupRequest, userSession, baseURL);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      LOG.error("Remote exception ", wsrpFault);
//      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, wsrpFault);
    }
    return response;
  }

  public BlockingInteractionResponse performBlockingInteraction(WSRPInteractionRequest actionRequest,
                                                                UserSessionMgr userSession,
                                                                String baseURL) throws WSRPException {
    checkInitCookie(userSession);
    BlockingInteractionResponse response = null;
    try {
      PerformBlockingInteraction request = new PerformBlockingInteraction();
      request.setPortletContext(getPortlet().getPortletContext());
      request.setInteractionParams(getInteractionParams(actionRequest));
      request.setMarkupParams(getMarkupParams(actionRequest));
      request.setRuntimeContext(getRuntimeContext(actionRequest, baseURL));
      RegistrationContext regCtx = producer.getRegistrationContext();
      if (regCtx != null) {
        request.setRegistrationContext(regCtx);
      }
      UserContext userCtx = getUserContext(userSession);
      if (userCtx != null) {
        request.setUserContext(userCtx);
      }
      Holder<UpdateResponse> holderUpdateResponse = new Holder<UpdateResponse>();
      Holder<String> holderRedirectURL = new Holder<String>();
      Holder<List<Extension>> holderListExtension = new Holder<List<Extension>>();
      /* MAIN INVOKE */
      markupPort.performBlockingInteraction(request.getRegistrationContext(),
                                            request.getPortletContext(),
                                            request.getRuntimeContext(),
                                            request.getUserContext(),
                                            request.getMarkupParams(),
                                            request.getInteractionParams(),
                                            holderUpdateResponse,
                                            holderRedirectURL,
                                            holderListExtension);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (InvalidCookieFault cookieFault) {
//      resetInitCookie(userSession);
//      performBlockingInteraction(actionRequest, userSession, baseURL);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      throw new WSRPException();
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
      javax.xml.ws.Holder<java.lang.String> _clonePortlet_portletHandle = new javax.xml.ws.Holder<java.lang.String>();
      javax.xml.ws.Holder<byte[]> _clonePortlet_portletState = new javax.xml.ws.Holder<byte[]>();
      javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> _clonePortlet_scheduledDestruction = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime>();
      javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _clonePortlet_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();
      portletManagementPort.clonePortlet(request.getRegistrationContext(),
                                         request.getPortletContext(),
                                         request.getUserContext(),
                                         request.getLifetime(),
                                         _clonePortlet_portletHandle,
                                         _clonePortlet_portletState,
                                         _clonePortlet_scheduledDestruction,
                                         _clonePortlet_extensions);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      throw new WSRPException();
    }
    return response;
  }

  public DestroyPortletsResponse destroyPortlets(List<String> portletHandles,
                                                 UserSessionMgr userSession) throws WSRPException {
    DestroyPortlets request = new DestroyPortlets();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    request.getPortletHandles().addAll(portletHandles);
    DestroyPortletsResponse response = null;
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>> _destroyPortlets_failedPortlets = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _destroyPortlets_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    try {
      portletManagementPort.destroyPortlets(request.getRegistrationContext(),
                                            request.getPortletHandles(),
                                            request.getUserContext(),
                                            _destroyPortlets_failedPortlets,
                                            _destroyPortlets_extensions);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      throw new WSRPException();
    }
    return response;
  }

  public ReturnAny releaseSessions(List<String> sessionIDs, UserSessionMgr userSession) throws WSRPException {
    checkInitCookie(userSession);
    ReleaseSessions request = new ReleaseSessions();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    request.getSessionIDs().addAll(sessionIDs);
    ReturnAny response = null;
    try {
      /* MAIN INVOKE */
      markupPort.releaseSessions(request.getRegistrationContext(),
                                 request.getSessionIDs(),
                                 request.getUserContext());
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      throw new WSRPException();
    }
    return response;
  }

  public void initCookie(UserSessionMgr userSession) throws WSRPException {
    InitCookie request = new InitCookie();
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      LOG.debug("Registration context used in initCookie : " + regCtx.getRegistrationHandle());
      request.setRegistrationContext(regCtx);
      request.setUserContext(getUserContext(userSession));
    }
    try {
      LOG.debug("Call initCookie on Markup Port");
      /* MAIN INVOKE */
      markupPort.initCookie(request.getRegistrationContext(), request.getUserContext());
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      LOG.error("Problem while initializing cookies", wsrpFault);
//      throw new WSRPException("Problem while initializing cookies", wsrpFault);
    }
  }

  public PortletDescriptionResponse getPortletDescription(UserSessionMgr userSession,
                                                          List<String> desiredLocales) throws WSRPException {
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
    request.getDesiredLocales().addAll(desiredLocales);
    PortletDescriptionResponse response = null;
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.PortletDescription> _getPortletDescription_portletDescription = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.PortletDescription>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> _getPortletDescription_resourceList = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _getPortletDescription_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    try {
      portletManagementPort.getPortletDescription(request.getRegistrationContext(),
                                                  request.getPortletContext(),
                                                  request.getUserContext(),
                                                  request.getDesiredLocales(),
                                                  _getPortletDescription_portletDescription,
                                                  _getPortletDescription_resourceList,
                                                  _getPortletDescription_extensions);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      throw new WSRPException();
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
    request.getDesiredLocales().addAll(consumer.getSupportedLocales());
    PortletPropertyDescriptionResponse response = null;
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelDescription> _getPortletPropertyDescription_modelDescription = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelDescription>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> _getPortletPropertyDescription_resourceList = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _getPortletPropertyDescription_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();
    try {
      portletManagementPort.getPortletPropertyDescription(request.getRegistrationContext(),
                                                          request.getPortletContext(),
                                                          request.getUserContext(),
                                                          request.getDesiredLocales(),
                                                          _getPortletPropertyDescription_modelDescription,
                                                          _getPortletPropertyDescription_resourceList,
                                                          _getPortletPropertyDescription_extensions);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      throw new WSRPException();
    }
    return response;
  }

  public PropertyList getPortletProperties(List<String> names, UserSessionMgr userSession) throws WSRPException {
    GetPortletProperties request = new GetPortletProperties();
    request.setPortletContext(getPortlet().getPortletContext());
    request.getNames().addAll(names);
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    UserContext userCtx = getUserContext(userSession);
    if (userCtx != null) {
      request.setUserContext(userCtx);
    }
    PropertyList response = null;
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Property>> _getPortletProperties_properties = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Property>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ResetProperty>> _getPortletProperties_resetProperties = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ResetProperty>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _getPortletProperties_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();
    try {
      portletManagementPort.getPortletProperties(request.getRegistrationContext(),
                                                 request.getPortletContext(),
                                                 request.getUserContext(),
                                                 request.getNames(),
                                                 _getPortletProperties_properties,
                                                 _getPortletProperties_resetProperties,
                                                 _getPortletProperties_extensions);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      throw new WSRPException();
    }
    return response;
  }

  public PortletContext setPortletProperties(PropertyList properties, UserSessionMgr userSession) throws WSRPException {
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
    javax.xml.ws.Holder<java.lang.String> _setPortletProperties_portletHandle = new javax.xml.ws.Holder<java.lang.String>();
    javax.xml.ws.Holder<byte[]> _setPortletProperties_portletState = new javax.xml.ws.Holder<byte[]>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> _setPortletProperties_scheduledDestruction = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _setPortletProperties_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();
    try {
      portletManagementPort.setPortletProperties(request.getRegistrationContext(),
                                                 request.getPortletContext(),
                                                 request.getUserContext(),
                                                 request.getPropertyList(),
                                                 _setPortletProperties_portletHandle,
                                                 _setPortletProperties_portletState,
                                                 _setPortletProperties_scheduledDestruction,
                                                 _setPortletProperties_extensions);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      throw new WSRPException();
    }
    return response;
  }

  // Below changes of WSRP2 spec

  public ResourceResponse getResource(WSRPResourceRequest resourceRequest,
                                      UserSessionMgr userSession,
                                      String baseURL) throws WSRPException {
    checkInitCookie(userSession);
    ResourceResponse response = null;
    try {
      ResourceContext resourceContext = resourceRequest.getCachedResource();
      if (resourceContext == null) {
        LOG.debug("get non cached resource");
        GetResource request = new GetResource();
        request.setPortletContext(getPortlet().getPortletContext());
        request.setResourceParams(getResourceParams(resourceRequest));
        request.setRuntimeContext(getRuntimeContext(resourceRequest, baseURL));
        RegistrationContext regCtx = producer.getRegistrationContext();
        if (regCtx != null) {
          request.setRegistrationContext(regCtx);
        }
        UserContext userCtx = getUserContext(userSession);
        if (userCtx != null) {
          request.setUserContext(userCtx);
        }
        javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.PortletContext> _getResource_portletContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.PortletContext>(request.getPortletContext());
        javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceContext> _getResource_resourceContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceContext>();
        javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.SessionContext> _getResource_sessionContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.SessionContext>();
        javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _getResource_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

        /* MAIN INVOKE */
        markupPort.getResource(request.getRegistrationContext(),
                               _getResource_portletContext,
                               request.getRuntimeContext(),
                               request.getUserContext(),
                               request.getResourceParams(),
                               _getResource_resourceContext,
                               _getResource_sessionContext,
                               _getResource_extensions);
      } else {
        LOG.debug("get cached resource");
        response = new ResourceResponse();
        response.setResourceContext(resourceContext);
      }

      resourceContext = response.getResourceContext();
      Boolean requiresRewriting = resourceContext.isRequiresRewriting();
      LOG.debug("requires URL rewriting : " + requiresRewriting);
      String content = getContent(resourceContext);

      if (resourceContext.getMimeType().startsWith("text/")) {
        String rewrittenMarkup = null;
        if (requiresRewriting) {
          URLRewriter urlRewriter = consumer.getURLRewriter();
          rewrittenMarkup = urlRewriter.rewriteURLs(baseURL, content);
          LOG.debug("rewrittenMarkup = " + rewrittenMarkup);
          if (rewrittenMarkup != null) {
            resourceContext.setItemString(rewrittenMarkup);
            try {
              resourceContext.setItemBinary(resourceContext.getItemString().getBytes("utf-8"));
            } catch (java.io.UnsupportedEncodingException e) {
              resourceContext.setItemBinary(resourceContext.getItemString().getBytes());
            }
          }
        } else {
          String oldBaseURL = baseURL + WSRPConstants.NEXT_PARAM + Constants.TYPE_PARAMETER + "="
              + WSRPConstants.URL_TYPE_BLOCKINGACTION;
          String newBaseURL = baseURL + WSRPConstants.NEXT_PARAM + Constants.TYPE_PARAMETER + "="
              + PCConstants.ACTION_STRING;
          rewrittenMarkup = content.replace(oldBaseURL, newBaseURL);
          resourceContext.setItemString(rewrittenMarkup);
          try {
            resourceContext.setItemBinary(resourceContext.getItemString().getBytes("utf-8"));
          } catch (java.io.UnsupportedEncodingException e) {
            resourceContext.setItemBinary(resourceContext.getItemString().getBytes());
          }
        }
      }

    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (InvalidCookieFault cookieFault) {
//      LOG.error("Problem with cookies ", cookieFault);
//      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
//      resetInitCookie(userSession);
//      getResource(resourceRequest, userSession, baseURL);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      LOG.error("Remote exception ", wsrpFault);
//      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, wsrpFault);
    }
    return response;
  }

  private String getContent(MimeResponse mimeResponse) {
    LOG.debug("mimeResponse.getItemString() = " + mimeResponse.getItemString());
    LOG.debug("mimeResponse.getItemBinary() = " + mimeResponse.getItemBinary());
    String content = null;
    if (mimeResponse.getItemBinary() != null) {
      content = new String(mimeResponse.getItemBinary());
    } else {
      content = mimeResponse.getItemString();
    }
    return content;
  }

  private ResourceParams getResourceParams(WSRPResourceRequest resourceRequest) {
    ResourceParams resourceParams = new ResourceParams();
    fillMimeRequestParams(resourceParams, resourceRequest);
    resourceParams.getFormParameters().addAll(resourceRequest.getFormParameters());
    resourceParams.getUploadContexts().addAll(resourceRequest.getUploadContexts());
    resourceParams.setResourceID(resourceRequest.getResourceID());
    resourceParams.setPortletStateChange(resourceRequest.getPortletStateChange());
    resourceParams.setResourceState(resourceRequest.getResourceState());
    resourceParams.setResourceCacheability(resourceRequest.getResourceCacheability());
    return resourceParams;
  }

  private void fillMimeRequestParams(MimeRequest params, WSRPBaseRequest request) {
    ClientData clientData = new ClientData();
    // lets just set this to the consumer agent for now
    if (producer.getRegistrationData() != null)
      clientData.setUserAgent(producer.getRegistrationData().getConsumerAgent());
    clientData.setCcppHeaders(null);
    clientData.getClientAttributes().addAll(null);

    params.setClientData(clientData);
    params.setSecureClientCommunication(request.isSecureClientCommunication());
    params.getLocales().addAll(request.getLocales());//consumer.getSupportedLocales());
    params.getMimeTypes().addAll(request.getMimeTypes());//consumer.getMimeTypes());
    params.setMode(request.getMode());
    params.setWindowState(request.getWindowState());
    params.getMarkupCharacterSets().addAll(consumer.getCharacterEncodingSet());
    params.setValidateTag(request.getValidateTag());

    // TODO: Set only modes and window states that are supported by the portlet
    // as described in it's portlet description.
    params.getValidNewModes().addAll(request.getValidNewModes());//consumer.getSupportedModes());
    params.getValidNewWindowStates().addAll(request.getValidNewWindowStates());//consumer.getSupportedWindowStates());

    params.setNavigationalContext(getNavigationalContext(request));

    params.getExtensions().clear();
  }

  private NavigationalContext getNavigationalContext(WSRPBaseRequest request) {
    NavigationalContext navCont = new NavigationalContext();
    navCont.setOpaqueValue(request.getNavigationalState());
    navCont.getPublicValues().addAll(request.getNavigationalValues());
    navCont.getExtensions().addAll(null);
    return navCont;
  }

  public HandleEventsResponse handleEvents(WSRPEventsRequest eventRequest,
                                           UserSessionMgr userSession,
                                           String baseURL) throws WSRPException {
    checkInitCookie(userSession);
    HandleEventsResponse response = null;
    try {
      HandleEvents request = new HandleEvents();
      request.setPortletContext(getPortlet().getPortletContext());
      request.setEventParams(getEventParams(eventRequest));
      request.setMarkupParams(getMarkupParams(eventRequest));
      request.setRuntimeContext(getRuntimeContext(eventRequest, baseURL));
      RegistrationContext regCtx = producer.getRegistrationContext();
      if (regCtx != null) {
        request.setRegistrationContext(regCtx);
      }
      UserContext userCtx = getUserContext(userSession);
      if (userCtx != null) {
        request.setUserContext(userCtx);
      }
      javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.UpdateResponse> _handleEvents_updateResponse = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.UpdateResponse>();
      javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.HandleEventsFailed>> _handleEvents_failedEvents = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.HandleEventsFailed>>();
      javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _handleEvents_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

      /* MAIN INVOKE */
      markupPort.handleEvents(request.getRegistrationContext(),
                              request.getPortletContext(),
                              request.getRuntimeContext(),
                              request.getUserContext(),
                              request.getMarkupParams(),
                              request.getEventParams(),
                              _handleEvents_updateResponse,
                              _handleEvents_failedEvents,
                              _handleEvents_extensions);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
//    } catch (InvalidCookieFault cookieFault) {
//      resetInitCookie(userSession);
//      handleEvents(eventRequest, userSession, baseURL);
//    } catch (java.rmi.RemoteException wsrpFault) {
//      throw new WSRPException();
    }
    return response;
  }

  private EventParams getEventParams(WSRPEventsRequest eventRequest) {
    EventParams eventParams = new EventParams();
    eventParams.getEvents().addAll(eventRequest.getEvents());
    eventParams.setPortletStateChange(consumer.getPortletStateChange());
    if (!portlet.isConsumerConfigured()
        && eventParams.getPortletStateChange()
                      .value()
                      .equalsIgnoreCase(StateChange.READ_WRITE.value())) {
      eventParams.setPortletStateChange(StateChange.CLONE_BEFORE_WRITE);
    }
    eventParams.getExtensions().clear();
    return eventParams;
  }

}
