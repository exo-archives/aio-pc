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

package org.exoplatform.services.wsrp.producer.impl;

import java.rmi.RemoteException;
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
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
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
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPProducerRewriterPortletURLFactory;
import org.exoplatform.services.wsrp.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp.type.CacheControl;
import org.exoplatform.services.wsrp.type.InteractionParams;
import org.exoplatform.services.wsrp.type.MarkupContext;
import org.exoplatform.services.wsrp.type.MarkupParams;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.NamedString;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ReturnAny;
import org.exoplatform.services.wsrp.type.RuntimeContext;
import org.exoplatform.services.wsrp.type.SessionContext;
import org.exoplatform.services.wsrp.type.StateChange;
import org.exoplatform.services.wsrp.type.Templates;
import org.exoplatform.services.wsrp.type.UpdateResponse;
import org.exoplatform.services.wsrp.type.UserContext;
import org.exoplatform.services.wsrp.utils.Modes;
import org.exoplatform.services.wsrp.utils.Utils;
import org.exoplatform.services.wsrp.utils.WindowStates;

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
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp1");
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
    String sessionID = runtimeContext.getSessionID();
    WSRPHttpSession session = resolveSession(sessionID, userContext.getUserContextKey(), sessiontimeperiod);
    sessionID = session.getId();
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(session.getId());
    sessionContext.setExpires(sessiontimeperiod);

    // manage user
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String owner = userContext.getUserContextKey();
    log.debug("Owner Context : " + owner);

    // manage cache ???
    if (markupParams.getValidateTag() != null) {
      try {
        if (transientStateManager.validateCache(markupParams.getValidateTag())) {
          MarkupContext markupContext = new MarkupContext();
          markupContext.setUseCachedMarkup(Boolean.TRUE);
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

    Map portletMetaDatas = proxy.getAllPortletMetaData();
    PortletData portletDatas = (PortletData) portletMetaDatas.get(portletApplicationName + Constants.PORTLET_META_DATA_ENCODER + portletName);

    // manage navigationalState
    Map<String, String[]> renderParameters = null;
    try {
      renderParameters = (Map<String, String[]>) processNavigationalState(markupParams.getNavigationalState());
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    if (renderParameters == null) {
      renderParameters = new HashMap<String, String[]>();
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

    if (conf.isDoesUrlTemplateProcessing()) {
      log.debug("Producer URL rewriting");
      Templates templates = manageTemplates(runtimeContext, session);
      baseURL = templates.getRenderTemplate();
      portletURLFactory = new WSRPProducerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    markupParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID);
    } else {
      log.debug("Consumer URL rewriting");
      portletURLFactory = new WSRPConsumerRewriterPortletURLFactory(mimeType,
                                                                    baseURL,
                                                                    portletDatas.getSupports(),
                                                                    markupParams.isSecureClientCommunication(),
                                                                    portletHandle,
                                                                    persistentStateManager,
                                                                    sessionID);
    }

    // manage mode and states
    PortletMode mode = processMode(markupParams.getMode());
    WindowState windowState = processWindowState(markupParams.getWindowState());

    // prepare the call to the portlet proxy
    WSRPHttpServletRequest request = (WSRPHttpServletRequest) WSRPHTTPContainer.getInstance().getRequest();
    WSRPHttpServletResponse response = (WSRPHttpServletResponse) WSRPHTTPContainer.getInstance().getResponse();
    WSRPHTTPContainer.getInstance().getRequest().setWsrpSession(session);

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
    markupContext.setMarkupString(new String(output.getContent()));
    markupContext.setPreferredTitle(output.getTitle());
    markupContext.setRequiresUrlRewriting(new Boolean(conf.isDoesUrlTemplateProcessing()));
    markupContext.setUseCachedMarkup(Boolean.TRUE);
    // markupContext.setMarkupBinary(null);//TODO
    // markupContext.setLocale(null);

    // ajoute une locale au markupcontext
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
                                                                InteractionParams interactionParams) throws RemoteException {

    // markupParams.getNavigationalState()
    // interactionParams.getInteractionState()
    // manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();

    // manage session
    String sessionID = runtimeContext.getSessionID();
    WSRPHttpSession session = resolveSession(sessionID, userContext.getUserContextKey(), sessiontimeperiod);

    // build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(session.getId());
    sessionContext.setExpires(sessiontimeperiod);

    // manage user
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String owner = userContext.getUserContextKey();
    log.debug("Owner Context : " + owner);

    // manage rewriting mechanism
    String baseURL = null;
    if (conf.isDoesUrlTemplateProcessing()) {
      log.debug("Producer URL rewriting");
      Templates templates = manageTemplates(runtimeContext, session);
      baseURL = templates.getRenderTemplate();
    } else {
      log.debug("Consumer URL rewriting");
      baseURL = WSRPConstants.WSRP_REWRITE_PREFIX;
    }

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

    // prepare objects for portlet proxy
    String mimeType = markupParams.getMimeTypes(0);
    WSRPHttpServletRequest request = (WSRPHttpServletRequest) WSRPHTTPContainer.getInstance().getRequest();
    WSRPHttpServletResponse response = (WSRPHttpServletResponse) WSRPHTTPContainer.getInstance().getResponse();
    WSRPHTTPContainer.getInstance().getRequest().setWsrpSession(session);
    putFormParametersInRequest(request, interactionParams);

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
    Map<String, String[]> params = Utils.getRenderParametersFromFormParameters(interactionParams.getFormParameters());
    input.setRenderParameters(params);
    // createUserProfile(userContext, request, session);

    ActionOutput output = null;
    try {
      /* MAIN INVOKE */
      output = proxy.processAction(request, response, input);
    } catch (WSRPException e) {
      log.debug("The call to processAction method was a failure ", e);
      Exception2Fault.handleException(e);
    }

    // manage navigational state
    String ns = IdentifierUtil.generateUUID(output);
    try {
      log.debug("set new navigational state : " + ns);
      persistentStateManager.putNavigationalState(ns, output.getRenderParameters());
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
        MarkupResponse markupResponse = getMarkup(registrationContext, portletContext, runtimeContext, userContext, markupParams);
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
      // fill the state to send it to consumer if allowed
      if (conf.isSavePortletStateOnConsumer())
        portletContext.setPortletState(output.getPortletState());
      updateResponse.setPortletContext(portletContext);
      blockingInteractionResponse.setUpdateResponse(updateResponse);
    }

    return blockingInteractionResponse;
  }

  private void putFormParametersInRequest(WSRPHttpServletRequest request,
                                          InteractionParams interactionParams) {
    NamedString[] array = interactionParams.getFormParameters();
    if (array == null) {
      log.debug("no form parameters");
      return;
    }
    for (NamedString namedString : array) {
      log.debug("form parameters; name : " + namedString.getName() + "; value : " + namedString.getValue());
      request.setParameter(namedString.getName(), namedString.getValue());
    }
  }

  public ReturnAny initCookie(RegistrationContext registrationContext) throws RemoteException {
    // ReturnAny any = null;
    if (conf.isRegistrationRequired()) {
      log.debug("Registration required");
      if (registrationContext == null) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    }
    // return any;
    return new ReturnAny();
  }

  public ReturnAny releaseSessions(RegistrationContext registrationContext,
                                   String[] sessionIDs) throws RemoteException {
    // ReturnAny any = null;
    if (conf.isRegistrationRequired()) {
      log.debug("Registration required");
      if (registrationContext == null) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    }
    for (int i = 0; i < sessionIDs.length; i++) {
      transientStateManager.releaseSession(sessionIDs[i]);
    }
    // return any;
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
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
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
    List<Supports> l = portletData.getSupports();
    for (int i = 0; i < mimeTypes.length; i++) {
      String mimeType = mimeTypes[i];
      for (Iterator<Supports> iterator = l.iterator(); iterator.hasNext();) {
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
      WSRPHttpSession wsrpHttpSession = (WSRPHttpSession) WSRPHTTPContainer.getInstance().getRequest().getSession();
      return wsrpHttpSession.getMaxInactiveInterval();
    } catch (Exception e) {
      System.out.println("Exception: WSRPHttpServletRequest e.getCause() = " + e.getCause());
    }
    return 900;
  }

}
