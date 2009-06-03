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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
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
import org.exoplatform.services.wsrp2.consumer.adapters.WSRPBaseRequestAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPMarkupPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPPortletManagementPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.InvalidCookie;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.InvalidSession;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.CookieProtocol;
import org.exoplatform.services.wsrp2.type.Deregister;
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
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * The implementation of this class is based on the WSRP4J project Author :
 * Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua
 */
public class PortletDriverImpl implements PortletDriver {

  private WSRPPortlet                             portlet               = null;

  private WSRPMarkupPortTypeAdapterAPI            markupPort            = null;

  private WSRPPortletManagementPortTypeAdapterAPI portletManagementPort = null;

  private ConsumerEnvironment                     consumer              = null;

  private Producer                                producer              = null;

  private CookieProtocol                          initCookie            = CookieProtocol.NONE;

  private Log                                     LOG;

  public PortletDriverImpl(ExoContainer cont, WSRPPortlet portlet) throws WSRPException {
    this.consumer = (ConsumerEnvironment) cont.getComponentInstanceOfType(ConsumerEnvironment.class);
    this.LOG = ExoLogger.getLogger("org.exoplatform.services.wsrp2.consumer");
    this.portlet = portlet;
    this.producer = consumer.getProducerRegistry().getProducer(portlet.getPortletKey()
                                                                      .getProducerId());
    this.portletManagementPort = producer.getPortletManagementAdapter();
    this.markupPort = producer.getMarkupAdapter();
    ServiceDescription serviceDescription = producer.getServiceDescription(false);
    if (serviceDescription != null && serviceDescription.getRequiresInitCookie() != null) {
      this.initCookie = serviceDescription.getRequiresInitCookie();
      LOG.debug("Requires cookie initialization : " + initCookie.value());
    }
  }

  public WSRPPortlet getPortlet() {
    return this.portlet;
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
    runtimeContext.setUserAuthentication(request.getUserAuthentication());
    // runtimeContext.setPortletInstanceKey: unused in the previous versions, now this is portletApp/portletName/windowId = input.getInternalWindowID().getUniqueID()
    runtimeContext.setPortletInstanceKey(request.getPortletInstanceKey());
    URLTemplateComposer templateComposer = consumer.getTemplateComposer(producer.getVersion());

    if (templateComposer != null) {
      runtimeContext.setNamespacePrefix(templateComposer.getNamespacePrefix());

      Boolean doesUrlTemplateProcess = null;
      Boolean getTemplatesStoredInSession = null;
      PortletDescription desc = producer.getPortletDescription(getPortlet().getParent());
      if (desc != null) {
        doesUrlTemplateProcess = desc.isDoesUrlTemplateProcessing();
        getTemplatesStoredInSession = desc.isTemplatesStoredInSession();

        if (doesUrlTemplateProcess != null && doesUrlTemplateProcess.booleanValue()) {
          Templates templates = null;
          if (getTemplatesStoredInSession) {
            //TODO 
          } else {
            templates = new Templates();
            if (baseURL != null) {
              // a path should be conform to the template--> "http://.../..?portal:componentId=portlet_handle";
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
        } // ends doesUrlTemplateProcess
      }

    } // ends templateComposer !=null

    SessionParams sessionParams = new SessionParams();
    sessionParams.setSessionID(request.getSessionID());
    runtimeContext.setSessionParams(sessionParams);
    runtimeContext.setPageState(null);//pageState);
    runtimeContext.setPortletStates(null);//portletStates);
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
    interactionParams.setPortletStateChange(consumer.getPortletStateChange());// set state change
    if (!portlet.isConsumerConfigured()
        && interactionParams.getPortletStateChange()
                            .value()
                            .equalsIgnoreCase(StateChange.READ_WRITE.value())) {
      interactionParams.setPortletStateChange(StateChange.CLONE_BEFORE_WRITE);// update state change
    }
    interactionParams.setInteractionState(actionRequest.getInteractionState());
    if (actionRequest.getFormParameters() != null)
      interactionParams.getFormParameters().addAll(actionRequest.getFormParameters());
    if (actionRequest.getUploadContexts() != null)
      interactionParams.getUploadContexts().addAll(actionRequest.getUploadContexts());
    return interactionParams;
  }

  public MarkupResponse getMarkup(WSRPMarkupRequest markupRequest,
                                  UserSessionMgr userSession,
                                  String baseURL) throws WSRPException {
    checkInitCookie(userSession);
    MarkupResponse response = null;
    try {
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
        LOG.debug("User context used in getMarkup : " + userCtx.getUserContextKey());
        request.setUserContext(userCtx);
      }

      /* MAIN INVOKE */
      response = markupPort.getMarkup(request);

      if (response.getMarkupContext().isUseCachedItem()) {
        if (markupRequest.getCachedMarkup() != null) {
          LOG.debug("get cached markup");
          response = new MarkupResponse();
          response.setMarkupContext(markupRequest.getCachedMarkup());
          response.setSessionContext(response.getSessionContext());
          return response;
        } else {
          throw new WSRPException("producer wants to use cached but consumer haven't it");
          //throw exc when producer wants to use cached but consumer haven't it
        }
      }

      Map<String, String> genericParams = writeGenericParams(request.getPortletContext(),
                                                             request.getUserContext(),
                                                             request.getRuntimeContext(),
                                                             response.getSessionContext());

      processMimeResponseMarkup(response.getMarkupContext(), genericParams, baseURL);

      
      return response;
      
    } catch (InvalidCookie exc) {
      LOG.info("Problem with cookies ", exc);
      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
      resetInitCookie(userSession);
      return getMarkup(markupRequest, userSession, baseURL);
    } catch (InvalidSession exc) {
      LOG.info("Problem with session :" + exc.getMessage());
      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
      List<String> sessionIDs = new ArrayList<String>();
      sessionIDs.add(markupRequest.getSessionID());
      releaseSessions(sessionIDs, userSession);
      ((WSRPBaseRequestAdapter) markupRequest).setSessionID(null);
      return getMarkup(markupRequest, userSession, baseURL);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
    }
    return null;
  }

  private Map<String, String> writeGenericParams(PortletContext portletContext,
                                                 UserContext userContext,
                                                 RuntimeContext runtimeContext,
                                                 SessionContext sessionContext) {
    Map<String, String> genericParams = new HashMap<String, String>();
    genericParams.put(WSRPConstants.WSRP_PORTLET_HANDLE, portletContext.getPortletHandle());
    genericParams.put(WSRPConstants.WSRP_USER_CONTEXT_KEY, userContext.getUserContextKey());
    genericParams.put(WSRPConstants.WSRP_PORTLET_INSTANCE_KEY,
                      runtimeContext.getPortletInstanceKey());
    genericParams.put(WSRPConstants.WSRP_SESSION_ID, runtimeContext.getSessionParams()
                                                                   .getSessionID());
    genericParams.put(WSRPConstants.WSRP_PAGE_STATE, runtimeContext.getPageState());
    genericParams.put(WSRPConstants.WSRP_PORTLET_STATES, runtimeContext.getPortletStates());
    return genericParams;
  }

  private void processMimeResponseMarkup(MimeResponse mimeContext,
                                         Map<String, String> genericParams,
                                         String baseURL) throws WSRPException {
    Boolean requiresRewriting = mimeContext.isRequiresRewriting();
    LOG.debug("requires URL rewriting : " + requiresRewriting);
    String content = getContent(mimeContext);

    URLRewriter urlRewriter = consumer.getURLRewriter();

    if (mimeContext.getMimeType().startsWith("text/")) {
      if (requiresRewriting) {
        // does.url template.processing = false  --- Consumer rewrite
        baseURL = writeGenericParams(mimeContext, genericParams, baseURL);
        content = urlRewriter.rewriteURLs(baseURL, content);
        LOG.debug("rewrittenMarkup = " + content);
        if (content != null) {
          mimeContext.setItemString(content);
          try {
            mimeContext.setItemBinary(mimeContext.getItemString().getBytes("utf-8"));
          } catch (java.io.UnsupportedEncodingException e) {
            mimeContext.setItemBinary(mimeContext.getItemString().getBytes());
          }
        }
      } else {
        // does.url template.processing = true --- Producer rewrite
        String oldBaseURL = baseURL + WSRPConstants.NEXT_PARAM + Constants.TYPE_PARAMETER + "="
            + WSRPConstants.URL_TYPE_BLOCKINGACTION;
        String newBaseURL = baseURL + WSRPConstants.NEXT_PARAM + Constants.TYPE_PARAMETER + "="
            + PCConstants.ACTION_STRING;
        content = content.replace(oldBaseURL, newBaseURL);
        content = urlRewriter.rewriteURLAfterTemplateProcessing(baseURL, content);
        mimeContext.setItemString(content);
        try {
          mimeContext.setItemBinary(mimeContext.getItemString().getBytes("utf-8"));
        } catch (java.io.UnsupportedEncodingException e) {
          mimeContext.setItemBinary(mimeContext.getItemString().getBytes());
        }
      }
    }
  }

  private String writeGenericParams(MimeResponse mimeContext,
                                    Map<String, String> genericParams,
                                    String baseURL) {
    StringBuffer stringBuffer = new StringBuffer(baseURL);
    stringBuffer.append("&" + WSRPConstants.WSRP_PORTLET_HANDLE + "="
        + genericParams.get(WSRPConstants.WSRP_PORTLET_HANDLE));
    stringBuffer.append("&" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "="
        + genericParams.get(WSRPConstants.WSRP_USER_CONTEXT_KEY));
    stringBuffer.append("&" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "="
        + genericParams.get(WSRPConstants.WSRP_PORTLET_INSTANCE_KEY));
    stringBuffer.append("&" + WSRPConstants.WSRP_SESSION_ID + "="
        + genericParams.get(WSRPConstants.WSRP_SESSION_ID));
    stringBuffer.append("&" + WSRPConstants.WSRP_PAGE_STATE + "="
        + genericParams.get(WSRPConstants.WSRP_PAGE_STATE));
    stringBuffer.append("&" + WSRPConstants.WSRP_PORTLET_STATES + "="
        + genericParams.get(WSRPConstants.WSRP_PORTLET_STATES));
    baseURL = stringBuffer.toString();
    return baseURL;
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
        LOG.debug("User context used in performBlockingInteraction : "
            + userCtx.getUserContextKey());
        request.setUserContext(userCtx);
      }

      /* MAIN INVOKE */
      response = markupPort.performBlockingInteraction(request);

    } catch (InvalidCookie exc) {
      LOG.info("Problem with cookies ", exc);
      resetInitCookie(userSession);
      performBlockingInteraction(actionRequest, userSession, baseURL);
    } catch (InvalidSession exc) {
      LOG.info("Problem with session ", exc);
      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
      List<String> sessionIDs = new ArrayList<String>();
      sessionIDs.add(actionRequest.getSessionID());
      releaseSessions(sessionIDs, userSession);
      ((WSRPBaseRequestAdapter) actionRequest).setSessionID(null);
      performBlockingInteraction(actionRequest, userSession, baseURL);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
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
      LOG.debug("User context used in clonePortlet : " + userCtx.getUserContextKey());
      request.setUserContext(userCtx);
    }
    PortletContext response = null;
    try {
      response = portletManagementPort.clonePortlet(request);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
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
    if (portletHandles != null)
      request.getPortletHandles().addAll(portletHandles);
    DestroyPortletsResponse response = null;

    try {
      response = portletManagementPort.destroyPortlets(request);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
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
    if (sessionIDs != null)
      request.getSessionIDs().addAll(sessionIDs);
    ReturnAny response = null;
    try {
      /* MAIN INVOKE */
      List<Extension> extension = markupPort.releaseSessions(request);
      response = new ReturnAny();
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
    }
    return response;
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
      LOG.debug("User context used in getPortletDescription : " + userCtx.getUserContextKey());
      request.setUserContext(userCtx);
    }
    if (desiredLocales != null)
      request.getDesiredLocales().addAll(desiredLocales);
    PortletDescriptionResponse response = null;

    try {
      response = portletManagementPort.getPortletDescription(request);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
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
      LOG.debug("User context used in getPortletPropertyDescription : "
          + userCtx.getUserContextKey());
      request.setUserContext(userCtx);
    }
    if (consumer.getSupportedLocales() != null)
      request.getDesiredLocales().addAll(consumer.getSupportedLocales());
    PortletPropertyDescriptionResponse response = null;
    try {
      response = portletManagementPort.getPortletPropertyDescription(request);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
    }
    return response;
  }

  public PropertyList getPortletProperties(List<String> names, UserSessionMgr userSession) throws WSRPException {
    GetPortletProperties request = new GetPortletProperties();
    request.setPortletContext(getPortlet().getPortletContext());
    if (names != null)
      request.getNames().addAll(names);
    RegistrationContext regCtx = producer.getRegistrationContext();
    if (regCtx != null) {
      request.setRegistrationContext(regCtx);
    }
    UserContext userCtx = getUserContext(userSession);
    if (userCtx != null) {
      LOG.debug("User context used in getPortletProperties : " + userCtx.getUserContextKey());
      request.setUserContext(userCtx);
    }
    PropertyList response = null;
    try {
      response = portletManagementPort.getPortletProperties(request);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
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
      LOG.debug("User context used in setPortletProperties : " + userCtx.getUserContextKey());
      request.setUserContext(userCtx);
    }
    request.setPropertyList(properties);
    PortletContext response = null;
    try {
      response = portletManagementPort.setPortletProperties(request);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
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
        LOG.debug("User context used in getResource : " + userCtx.getUserContextKey());
        request.setUserContext(userCtx);
      }

      if (producer.getVersion() != 1)
      response = markupPort.getResource(request);
      else{
        LOG.info("WSRP 1 does not support serving resources");
        return null;
      }
        

      if (response.getResourceContext().isUseCachedItem()) {
        if (resourceRequest.getCachedResource() != null) {
          LOG.debug("get cached resource");
          response = new ResourceResponse();
          response.setResourceContext(resourceRequest.getCachedResource());
          response.setSessionContext(response.getSessionContext());
          return response;

        } else {
          throw new WSRPException("producer wants to use cached but consumer haven't it");
          //throw exc when producer wants to use cached but consumer haven't it
        }
      }

      Map<String, String> genericParams = writeGenericParams(request.getPortletContext(),
                                                             request.getUserContext(),
                                                             request.getRuntimeContext(),
                                                             response.getSessionContext());

      processMimeResponseMarkup(response.getResourceContext(), genericParams, baseURL);

    } catch (InvalidCookie exc) {
      LOG.error("Problem with cookies ", exc);
      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
      resetInitCookie(userSession);
      return getResource(resourceRequest, userSession, baseURL); //?
    } catch (InvalidSession exc) {
      LOG.info("Problem with session ", exc);
      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
      List<String> sessionIDs = new ArrayList<String>();
      sessionIDs.add(resourceRequest.getSessionID());
      releaseSessions(sessionIDs, userSession);
      ((WSRPBaseRequestAdapter) resourceRequest).setSessionID(null);
     return  getResource(resourceRequest, userSession, baseURL); //?
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
    }
    return response;
  }

  private String getContent(MimeResponse mimeResponse) {
    LOG.debug("mimeResponse.getItemString() = " + mimeResponse.getItemString());
    LOG.debug("mimeResponse.getItemBinary() = " + mimeResponse.getItemBinary());
    String content = null;
    if (mimeResponse.getItemBinary() != null) {
     try {
       content = new String(mimeResponse.getItemBinary(), "utf-8");
      } catch (java.io.UnsupportedEncodingException e) {
      }
    } else {
      content = mimeResponse.getItemString();
    }
    return content;
  }

  private ResourceParams getResourceParams(WSRPResourceRequest resourceRequest) {
    ResourceParams resourceParams = new ResourceParams();
    fillMimeRequestParams(resourceParams, resourceRequest);
    if (resourceRequest.getFormParameters() != null)
      resourceParams.getFormParameters().addAll(resourceRequest.getFormParameters());
    if (resourceRequest.getUploadContexts() != null)
      resourceParams.getUploadContexts().addAll(resourceRequest.getUploadContexts());
    resourceParams.setResourceID(resourceRequest.getResourceID());
    resourceParams.setPortletStateChange(resourceRequest.getPortletStateChange());
    resourceParams.setResourceState(resourceRequest.getResourceState());
    resourceParams.setResourceCacheability(resourceRequest.getResourceCacheability());

    resourceParams.setPortletStateChange(consumer.getPortletStateChange());
    if (!portlet.isConsumerConfigured()
        && resourceParams.getPortletStateChange()
                         .value()
                         .equalsIgnoreCase(StateChange.READ_WRITE.value())) {
      resourceParams.setPortletStateChange(StateChange.CLONE_BEFORE_WRITE);
    }
    return resourceParams;
  }

  private void fillMimeRequestParams(MimeRequest params, WSRPBaseRequest request) {
    ClientData clientData = new ClientData();
    // lets just set this to the consumer agent for now
    if (producer.getRegistrationData() != null)
      clientData.setUserAgent(producer.getRegistrationData().getConsumerAgent());
    clientData.setCcppHeaders(null);
//    clientData.getClientAttributes().addAll(null);

    params.setClientData(clientData);
    params.setSecureClientCommunication(request.isSecureClientCommunication());
//    if (request.getLocales() != null)
    params.getLocales().addAll(request.getLocales());
    if (request.getMimeTypes() != null)
      params.getMimeTypes().addAll(request.getMimeTypes());//consumer.getMimeTypes());
    params.setMode(request.getMode());
    params.setWindowState(request.getWindowState());
    if (consumer.getCharacterEncodingSet() != null)
      params.getMarkupCharacterSets().addAll(consumer.getCharacterEncodingSet());
    params.setValidateTag(request.getValidateTag());

    // TODO: Set only modes and window states that are supported by the portlet
    // as described in it's portlet description.
    if (request.getValidNewModes() != null)
      params.getValidNewModes().addAll(request.getValidNewModes());//consumer.getSupportedModes());
    if (request.getValidNewWindowStates() != null)
      params.getValidNewWindowStates().addAll(request.getValidNewWindowStates());//consumer.getSupportedWindowStates());

    params.setNavigationalContext(getNavigationalContext(request));

  }

  private NavigationalContext getNavigationalContext(WSRPBaseRequest request) {
    NavigationalContext navCont = new NavigationalContext();
    navCont.setOpaqueValue(request.getNavigationalState());
    if (request.getNavigationalValues() != null)
      navCont.getPublicValues().addAll(request.getNavigationalValues());
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
        LOG.debug("User context used in handleEvents : " + userCtx.getUserContextKey());
        request.setUserContext(userCtx);
      }

      response = markupPort.handleEvents(request);
    } catch (InvalidCookie exc) {
      LOG.info("Problem with cookies ", exc);
      resetInitCookie(userSession);
      handleEvents(eventRequest, userSession, baseURL);
    } catch (InvalidSession exc) {
      LOG.info("Problem with session ", exc);
      // throw new WSRPException(Faults.INVALID_COOKIE_FAULT, cookieFault);
      List<String> sessionIDs = new ArrayList<String>();
      sessionIDs.add(eventRequest.getSessionID());
      releaseSessions(sessionIDs, userSession);
      ((WSRPBaseRequestAdapter) eventRequest).setSessionID(null);
      handleEvents(eventRequest, userSession, baseURL);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem with :" + exc);
    }
    return response;
  }

  private EventParams getEventParams(WSRPEventsRequest eventRequest) {
    EventParams eventParams = new EventParams();
    if (eventRequest.getEvents() != null)
      eventParams.getEvents().addAll(eventRequest.getEvents());
    eventParams.setPortletStateChange(consumer.getPortletStateChange());
    if (!portlet.isConsumerConfigured()
        && eventParams.getPortletStateChange()
                      .value()
                      .equalsIgnoreCase(StateChange.READ_WRITE.value())) {
      eventParams.setPortletStateChange(StateChange.CLONE_BEFORE_WRITE);
    }
    return eventParams;
  }

  // Cookies private operations

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
      List<Extension> extension = markupPort.initCookie(request);
    } catch (InvalidRegistration e) {
      LOG.info("Problem with registration ", e);
      deregister(userSession);
    } catch (Exception exc) {
      LOG.error("Problem while initializing cookies :" + exc);
    }
  }

  private void resetInitCookie(UserSessionMgr userSession) throws WSRPException {
    LOG.debug("reset cookies");
    if (initCookie.value().equalsIgnoreCase(CookieProtocol.PER_USER.value())) {
      userSession.setInitCookieDone(false);
    } else if (initCookie.value().equalsIgnoreCase(CookieProtocol.PER_GROUP.value())) {
      PortletDescription portletDescription = null;
      portletDescription = producer.getPortletDescription(getPortlet().getParent());
      if (portletDescription != null) {
        String groupID = portletDescription.getGroupID();
        if (groupID != null) {
          GroupSessionMgr groupSession = (GroupSessionMgr) userSession.getGroupSession(groupID);
          groupSession.setInitCookieDone(false);
        }
      }
    } else if (initCookie.value().equalsIgnoreCase(CookieProtocol.NONE.value())) {
      userSession.setInitCookieDone(false);
    }
  }

  private void checkInitCookie(UserSessionMgr userSession) throws WSRPException {
    LOG.debug("init cookies: with serviceDescription.getRequiresInitCookie() = "
        + initCookie.value());
    if (initCookie.value().equalsIgnoreCase(CookieProtocol.PER_USER.value())) {
      // If CookieProtocol.PER_USER
      LOG.debug("Cookies management per user");
      if (!userSession.isInitCookieDone()) {
        LOG.debug("Init cookies : " + userSession);
//        this.markupPort = userSession.getWSRPMarkupService();
        userSession.setInitCookieRequired(true);
        initCookie(userSession);
        userSession.setInitCookieDone(true);
      }
    } else if (initCookie.value().equalsIgnoreCase(CookieProtocol.PER_GROUP.value())) {
      // If CookieProtocol.PER_GROUP
      LOG.debug("Cookies management per group");
      PortletDescription portletDescription = producer.getPortletDescription(getPortlet().getParent());
      String groupID = null;
      if (portletDescription != null) {
        groupID = portletDescription.getGroupID();
        LOG.debug("Group Id used for cookies management : " + groupID);
      }
      if (groupID != null) {
        GroupSessionMgr groupSession = (GroupSessionMgr) userSession.getGroupSession(groupID);
//        this.markupPort = groupSession.getWSRPMarkupService();
        if (!groupSession.isInitCookieDone()) {
          LOG.debug("Group session in init cookies : " + groupSession);
          groupSession.setInitCookieRequired(true);
          initCookie(userSession);
          groupSession.setInitCookieDone(true);
        }
      } else {
        // means either we have no service description from the producer
        // contain the portlet
        // or the producer specified initCookieRequired perGroup but didn't
        // provide
        // a groupID in the portlet description
      }
    } else {
      // If CookieProtocol.NONE
      //  this.markupPort = userSession.getWSRPMarkupService();
    }
  }

  private void deregister(UserSessionMgr userSession) throws WSRPException {
    ProducerRegistry pregistry = consumer.getProducerRegistry();
    RegistrationContext registrationContext = producer.getRegistrationContext();
    UserContext userCtx = getUserContext(userSession);
    Deregister deregister = new Deregister();
    deregister.setRegistrationContext(registrationContext);
    deregister.setUserContext(userCtx);
    try {
      producer.deregister(deregister);
    } catch (Exception e1) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Exception deregistering producer: " + producer.getID());
      }
    }
    try {
      pregistry.removeProducer(producer.getID());
    } catch (Exception e) {
      throw new WSRPException(e.getMessage(), e);
    }

  }

}
