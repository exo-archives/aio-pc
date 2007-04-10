/*
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 *
 * Created on 20 d�c. 2003
 */
package org.exoplatform.services.wsrp.producer.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.MarkupOperationsInterface;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;
import org.exoplatform.services.wsrp.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp.producer.TransientStateManager;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPConsumerRewriterPortletURLFactory;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPProducerRewriterPortletURLFactory;
import org.exoplatform.services.wsrp.type.*;


/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class MarkupOperationsInterfaceImpl implements MarkupOperationsInterface {

  private static final String DEFAULT_WINDOW_ID = "windowID";
  private Log log;
  private WSRPConfiguration conf;
  private PersistentStateManager persitentStateManager;
  private PortletContainerProxy container;
  private TransientStateManager transientStateManager;
  private PortletManagementOperationsInterface portletManagementOperationsInterface;
  private OrganizationService orgService;
  private WSRPPortletPreferencesPersister persister;
  
  private static String WSRP_CONTAINER = "portal";  
  

  public MarkupOperationsInterfaceImpl(PortletManagementOperationsInterface portletManagementOperationsInterface,
                                       PersistentStateManager persitentStateManager,
                                       TransientStateManager transientStateManager,
                                       PortletContainerProxy container,
                                       WSRPConfiguration conf,
                                       OrganizationService orgService,
                                       LogService logService) {
    this.portletManagementOperationsInterface = portletManagementOperationsInterface;
    this.container = container;
    this.log = logService.getLog("org.exoplatform.services.wsrp");
    this.conf = conf;
    this.persitentStateManager = persitentStateManager;
    this.transientStateManager = transientStateManager;
    this.orgService = orgService;
    this.persister = WSRPPortletPreferencesPersister.getInstance();
  }

  public MarkupResponse getMarkup(RegistrationContext registrationContext,
                                  PortletContext portletContext,
                                  RuntimeContext runtimeContext,
                                  UserContext userContext,
                                  MarkupParams markupParams)
      throws RemoteException {
    //manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    log.debug("Portlet handle : " + portletHandle);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();
    
    //manage session
    String sessionID = runtimeContext.getSessionID();
    WSRPHttpSession session = resolveSession(sessionID, userContext.getUserContextKey(), sessiontimeperiod);
    sessionID = session.getId();
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(session.getId());
    sessionContext.setExpires(sessiontimeperiod);

    //manage user
    userContext = transientStateManager.reolveUserContext(userContext, session);
    String owner = userContext.getUserContextKey();
    log.debug("Owner Context : " + owner);

    //manage cache
    if (markupParams.getValidateTag() != null) {
      try {
        if (transientStateManager.validateCache(markupParams.getValidateTag())) {
          MarkupContext markupContext = new MarkupContext();
          markupContext.setUseCachedMarkup(new Boolean(true));
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

    Map portletMetaDatas = container.getAllPortletMetaData();
    PortletData portletDatas = (PortletData) portletMetaDatas.
        get(portletApplicationName + Constants.PORTLET_META_DATA_ENCODER + portletName);

    //manage navigationalState
    Map renderParameters = null;
    try {
      renderParameters = processNavigationalState(markupParams.getNavigationalState());
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    if (renderParameters == null) {
      renderParameters = new HashMap();
      log.debug("No navigational state exists");
    }

    //manage portlet state
    byte[] portletState = managePortletState(portletContext);

    //manage mime type
    String mimeType = null;
    try {
      mimeType = getMimeType(markupParams.getMimeTypes(), portletDatas);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    String baseURL = null;
    PortletURLFactory portletURLFactory = null;

    if (conf.isDoesUrlTemplateProcessing()) {
      log.debug("Producer URL rewriting");
      Templates templates = manageTemplates(runtimeContext, session);
      baseURL = templates.getRenderTemplate();
      portletURLFactory = new WSRPProducerRewriterPortletURLFactory(mimeType, portletDatas.getSupports(),
          markupParams.isSecureClientCommunication(), new ArrayList(container.getWindowStates(portletApplicationName)),
          Collections.enumeration(container.getSupportedWindowStates()),
          templates.getRenderTemplate(),
          portletHandle, persitentStateManager, sessionID);
    } else {
      log.debug("Consumer URL rewriting");
      baseURL = WSRPConstants.WSRP_REWITE_PREFIX;
      portletURLFactory = new WSRPConsumerRewriterPortletURLFactory(mimeType, portletDatas.getSupports(),
          markupParams.isSecureClientCommunication(), new ArrayList(container.getWindowStates(portletApplicationName)),
          Collections.enumeration(container.getSupportedWindowStates()),
          baseURL, portletHandle, persitentStateManager, sessionID);
    }

    //manage mode and states
    PortletMode mode = processMode(markupParams.getMode());
    WindowState windowState = processWindowState(markupParams.getWindowState());

    //prepare the call to the portlet container
    WSRPHttpServletRequest request = new WSRPHttpServletRequest(session);
    WSRPHttpServletResponse response = new WSRPHttpServletResponse();

    //prepare the Input object
    RenderInput input = new RenderInput();
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(owner);
    windowID.setPortletApplicationName(portletApplicationName);
    windowID.setPortletName(portletName);
    windowID.setUniqueID(uniqueID);
    input.setWindowID(windowID);
    input.setBaseURL(baseURL);
    input.setUserAttributes(new HashMap());
    input.setPortletMode(mode);
    input.setWindowState(windowState);
    input.setMarkup(mimeType);
    input.setRenderParameters(renderParameters);
    input.setPortletURLFactory(portletURLFactory);
    input.setPortletState(portletState);
    input.setPortletPreferencesPersister(persister);
    //createUserProfile(userContext, request, session);

    RenderOutput output = null;
    try {
      output = container.render(request, response, input);
    } catch (WSRPException e) {
      log.debug("The call to render method was a failure ", e);
      Exception2Fault.handleException(e);
    }

    //prepare the cache control object
    CacheControl cacheControl = null;
    try {
      cacheControl = transientStateManager.getCacheControl(portletDatas);
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    //build markup context
    MarkupContext markupContext = new MarkupContext();
    markupContext.setMimeType(mimeType);
    markupContext.setCacheControl(cacheControl);
    markupContext.setMarkupString(new String(output.getContent()));
    markupContext.setPreferredTitle(output.getTitle());
    markupContext.setRequiresUrlRewriting(new Boolean(conf.isDoesUrlTemplateProcessing()));
    markupContext.setUseCachedMarkup(new Boolean(false));
    //markupContext.setMarkupBinary(null);//TODO
    //markupContext.setLocale(null);
    
    //ajoute une locale au markupcontext
    markupContext.setLocale("en");

    MarkupResponse markup = new MarkupResponse();
    markup.setSessionContext(sessionContext);
    markup.setMarkupContext(markupContext);
    return markup;
  }


  public BlockingInteractionResponse performBlockingInteraction(RegistrationContext registrationContext,
                                                                PortletContext portletContext,
                                                                RuntimeContext runtimeContext,
                                                                UserContext userContext,
                                                                MarkupParams markupParams,
                                                                InteractionParams interactionParams)
      throws RemoteException {
    //manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();
    
    //manage session
    String sessionID = runtimeContext.getSessionID();
    WSRPHttpSession session = resolveSession(sessionID, userContext.getUserContextKey(), sessiontimeperiod);

    //build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(session.getId());
    sessionContext.setExpires(sessiontimeperiod);

    //manage user
    userContext = transientStateManager.reolveUserContext(userContext, session);
    String owner = userContext.getUserContextKey();
    log.debug("Owner Context : " + owner);

    //manage rewriting mechanism
    String baseURL = null;
    if (conf.isDoesUrlTemplateProcessing()) {
      log.debug("Producer URL rewriting");
      Templates templates = manageTemplates(runtimeContext, session);
      baseURL = templates.getRenderTemplate();
    } else {
      log.debug("Consumer URL rewriting");
      baseURL = WSRPConstants.WSRP_REWITE_PREFIX;
    }

    //manage portlet state
    byte[] portletState = managePortletState(portletContext);

    //manage mode and states
    PortletMode mode = processMode(markupParams.getMode());
    WindowState windowState = processWindowState(markupParams.getWindowState());

    //manage portlet state change
    boolean isStateChangeAuthorized = false;
    String stateChange = interactionParams.getPortletStateChange().getValue();
    if (StateChange.readWrite.getValue().equalsIgnoreCase(stateChange)) {
      log.debug("readWrite state change");
      //every modification is allowed on the portlet
      isStateChangeAuthorized = true;
    } else if (StateChange.cloneBeforeWrite.getValue().equalsIgnoreCase(stateChange)) {
      log.debug("cloneBeforWrite state change");
      portletContext = portletManagementOperationsInterface.clonePortlet(registrationContext,
          portletContext, userContext);
      //any modification will be made on the
      isStateChangeAuthorized = true;
    } else if (StateChange.readOnly.getValue().equalsIgnoreCase(stateChange)) {
      log.debug("readOnly state change");
      //if an attempt to change the state is done (means change the portlet pref in JSR 168)
      //then a fault will be launched
    } else {
      log.debug("The submited portlet state change value : " + stateChange + " is not supported");
      Exception2Fault.handleException(new WSRPException(Faults.PORTLET_STATE_CHANGE_REQUIRED_FAULT));
    }

    //prepare objects for portlet container
    String mimeType = markupParams.getMimeTypes(0);
    WSRPHttpServletRequest request = new WSRPHttpServletRequest(session);
    WSRPHttpServletResponse response = new WSRPHttpServletResponse();
    putInteractionParameterInRequest(request, interactionParams);

    //prepare the Input object
    ActionInput input = new ActionInput();
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(owner);
    windowID.setPortletApplicationName(portletApplicationName);
    windowID.setPortletName(portletName);
    windowID.setUniqueID(uniqueID);
    input.setWindowID(windowID);
    input.setBaseURL(baseURL);
    input.setUserAttributes(new HashMap());
    input.setPortletMode(mode);
    input.setWindowState(windowState);
    input.setMarkup(mimeType);
    input.setStateChangeAuthorized(isStateChangeAuthorized);
    input.setStateSaveOnClient(conf.isSavePortletStateOnConsumer());
    input.setPortletState(portletState);
    input.setPortletPreferencesPersister(persister);
//    createUserProfile(userContext, request, session);
    ActionOutput output = null;
    try {
      output = container.processAction(request, response, input);
    } catch (WSRPException e) {
      log.debug("The call to processAction method was a failure ", e);
      Exception2Fault.handleException(e);
    }

    //manage navigational state
    String ns = IdentifierUtil.generateUUID(output);
    try {
      log.debug("set new navigational state : " + ns);
      persitentStateManager.putNavigationalState(ns, output.getRenderParameters());
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    BlockingInteractionResponse blockingInteractionResponse = new BlockingInteractionResponse();

    if (output.getProperties().get(ActionOutput.SEND_REDIRECT) != null) {
      log.debug("Redirect the response to : " + (String) output.getProperties().get(ActionOutput.SEND_REDIRECT));
      blockingInteractionResponse.setRedirectURL((String) output.getProperties().get(ActionOutput.SEND_REDIRECT));
      blockingInteractionResponse.setUpdateResponse(null);
    } else {
      MarkupContext markupContext = null;
      if (conf.isBlockingInteractionOptimized()) {
        markupParams.setNavigationalState(ns);
        MarkupResponse markupResponse = getMarkup(registrationContext, portletContext,
            runtimeContext, userContext, markupParams);
        markupContext = markupResponse.getMarkupContext();
      }

      UpdateResponse updateResponse = new UpdateResponse();
      updateResponse.setNavigationalState(ns);
      if (output.getNextMode() != null) {
        updateResponse.setNewMode(WSRPConstants.WSRP_PREFIX + output.getNextMode().toString());
      }
      if (output.getNextState() != null) {
        updateResponse.setNewWindowState(WSRPConstants.WSRP_PREFIX + output.getNextState().toString());
      }
      updateResponse.setSessionContext(sessionContext);
      updateResponse.setMarkupContext(markupContext);
      //fill the state to send it to consumer if allowed
      if (conf.isSavePortletStateOnConsumer())
        portletContext.setPortletState(output.getPortletState());
      updateResponse.setPortletContext(portletContext);
      blockingInteractionResponse.setUpdateResponse(updateResponse);
    }

    return blockingInteractionResponse;
  }

  private void putInteractionParameterInRequest(WSRPHttpServletRequest request, InteractionParams interactionParams) {
    NamedString[] array = interactionParams.getFormParameters();
    if (array == null) {
      log.debug("no form parameters");
      return;
    }
    for (int i = 0; i < array.length; i++) {
      log.debug("form parameters; name : " + array[i].getName() + "; value : " + array[i].getValue());
      request.setParameter(array[i].getName(), array[i].getValue());
    }
  }

  public ReturnAny initCookie(RegistrationContext registrationContext)
      throws RemoteException {
    if (conf.isRegistrationRequired()) {
      log.debug("Registration required");
      if (registrationContext == null) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    }
    return new ReturnAny();
  }

  public ReturnAny releaseSessions(RegistrationContext registrationContext,
                                   String[] sessionIDs)
      throws RemoteException {
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

  private WSRPHttpSession resolveSession(String sessionID, String user, Integer sessiontimeperiod) throws RemoteException {
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

  private String manageRegistration(String portletHandle, RegistrationContext registrationContext) throws RemoteException {
    log.debug("manageRegistration called for portlet handle : " + portletHandle);
    if (!container.isPortletOffered(portletHandle)) {
      log.debug("The latter handle is not offered by the Producer");
      Exception2Fault.handleException(new WSRPException(Faults.INVALID_HANDLE_FAULT));
    } else {
      String[] keys = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
      if (keys.length == 2) {
        portletHandle += Constants.PORTLET_HANDLE_ENCODER + DEFAULT_WINDOW_ID;
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
    Map map = persitentStateManager.getNavigationalSate(navigationalState);
    //if (log.isDebugEnabled()) {
    if(map != null){
      for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
        String s = (String) iterator.next();
        log.debug("attribute in map referenced by ns : " + s);
      }
    }
    //}
    return map;
  }

  private String getMimeType(String[] mimeTypes, PortletData portletData) throws WSRPException {
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

  private Templates manageTemplates(RuntimeContext runtimeContext, WSRPHttpSession session) {
    Templates templates = runtimeContext.getTemplates();
    if (conf.isTemplatesStoredInSession()) {
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
      if ( ExoContainerContext.getTopContainer() instanceof RootContainer) {
        //PORTALCONTAINER
        PortalContainer container = RootContainer.getInstance().getPortalContainer(WSRP_CONTAINER);
        WSRPHttpSession wsrpHttpSession = (WSRPHttpSession) container.getComponentInstanceOfType(WSRPHttpSession.class);
        return wsrpHttpSession.getMaxInactiveInterval();
      } else {
        //STANDALONECONTAINER
        ExoContainer container = ExoContainerContext.getTopContainer();
        WSRPHttpSession wsrpHttpSession = (WSRPHttpSession) container.getComponentInstanceOfType(WSRPHttpSession.class);
        return wsrpHttpSession.getMaxInactiveInterval();
      }
    } catch (Exception e) {
      System.out.println("Exception: WSRPHttpServletRequest e.getCause() = " + e.getCause());
    }
    return 900;
  }

}
