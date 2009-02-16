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

package org.exoplatform.services.wsrp2.producer.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
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
import org.exoplatform.services.portletcontainer.plugins.pc.PortletDataImp;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InconsistentParameters;
import org.exoplatform.services.wsrp2.intf.InvalidCookie;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.InvalidSession;
import org.exoplatform.services.wsrp2.intf.InvalidUserCategory;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.PortletStateChangeRequired;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.intf.UnsupportedLocale;
import org.exoplatform.services.wsrp2.intf.UnsupportedMimeType;
import org.exoplatform.services.wsrp2.intf.UnsupportedMode;
import org.exoplatform.services.wsrp2.intf.UnsupportedWindowState;
import org.exoplatform.services.wsrp2.producer.MarkupOperationsInterface;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp2.producer.TransientStateManager;
import org.exoplatform.services.wsrp2.producer.impl.helpers.LifetimeHelper;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp2.producer.impl.helpers.urls.WSRPRewriterPortletURLFactoryBuilder;
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
import org.exoplatform.services.wsrp2.type.UpdateResponse;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.utils.JAXBEventTransformer;
import org.exoplatform.services.wsrp2.utils.Modes;
import org.exoplatform.services.wsrp2.utils.Utils;
import org.exoplatform.services.wsrp2.utils.WindowStates;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Author :
 *         Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua
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
    // this.orgService = orgService;
    this.persister = WSRPPortletPreferencesPersister.getInstance();
  }

  public MarkupResponse getMarkup(RegistrationContext registrationContext,
                                  PortletContext portletContext,
                                  RuntimeContext runtimeContext,
                                  UserContext userContext,
                                  MarkupParams markupParams) throws AccessDenied,
                                                            ResourceSuspended,
                                                            UnsupportedMimeType,
                                                            InvalidRegistration,
                                                            InvalidHandle,
                                                            InvalidCookie,
                                                            UnsupportedWindowState,
                                                            InvalidUserCategory,
                                                            UnsupportedMode,
                                                            ModifyRegistrationRequired,
                                                            InvalidSession,
                                                            MissingParameters,
                                                            InconsistentParameters,
                                                            OperationFailed,
                                                            UnsupportedLocale,
                                                            WSRPException {

    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
    }

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
    String uniqueID = null;
    if (k.length > 2)
      uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();

    // manage SESSION
    String sessionID = runtimeContext.getSessionParams().getSessionID();
    //get session from cache or create a new one
    WSRPHttpSession session = resolveSession(sessionID, sessiontimeperiod);
    sessionID = session.getId(); // whether renew ID if it is null

    // manage USER
    // if isUserContextStoredInSession: if userContext is null get user context from cache, else put to cache
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String user = userContext != null ? userContext.getUserContextKey() : null;

    String owner = user;
    log.debug("Owner Context : " + owner);

    ExoContainer cont = ExoContainerContext.getCurrentContainer();
    WindowInfosContainer.createInstance(cont, sessionID, user);

    // build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(sessionID);
    sessionContext.setExpires(sessiontimeperiod);

    // manage cache
    if (markupParams.getValidateTag() != null) {
      try {
        if (transientStateManager.validateCache(markupParams.getValidateTag())) {
          MarkupContext markupContext = new MarkupContext();
          markupContext.setUseCachedItem(new Boolean(true));
          MarkupResponse markupResponse = new MarkupResponse();
          markupResponse.setMarkupContext(markupContext);
          markupResponse.setSessionContext(sessionContext);
          return markupResponse;
        }
      } catch (WSRPException e) {
        log.debug("Can not validate markup cache for validateTag : "
            + markupParams.getValidateTag());
        e.printStackTrace();
        throw new WSRPException();
      }
    }

    // get portlet data
    PortletData portletData = getPortletMetaData(portletApplicationName
        + Constants.PORTLET_META_DATA_ENCODER + portletName);

    // PROCESS PARAMETERS    

    // get public param names from config
    List<String> publicParamNames = portletData.getSupportedPublicRenderParameter();
    // manage navigational context
    NavigationalContext navigationalContext = markupParams.getNavigationalContext();
    // process opaque (navigational) values
    Map<String, String[]> persistentNavigationalParameters = processNavigationalState(navigationalContext);
    // get navigational (public) values
    Map<String, String[]> navigationalParameters = Utils.getMapParametersFromNamedStringArray(navigationalContext.getPublicValues());

    // create render params map for input
    Map<String, String[]> renderParameters = new HashMap<String, String[]>();

    if (persistentNavigationalParameters != null && !persistentNavigationalParameters.isEmpty()) {
      renderParameters = persistentNavigationalParameters;
    }

    replacePublicParams(renderParameters, publicParamNames, navigationalParameters);

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    // manage mime type
    String mimeType = null;
    try {
      mimeType = getMimeType(markupParams.getMimeTypes(), portletData);
    } catch (WSRPException e) {
      e.printStackTrace();
      throw new WSRPException();
    }

    // ---------- BEGIN FOR CREATING FACTORY --------------
    PortletURLFactory portletURLFactory = WSRPRewriterPortletURLFactoryBuilder.getFactory(conf.isDoesUrlTemplateProcessing(),
                                                                                          runtimeContext,
                                                                                          session,
                                                                                          conf.isTemplatesStoredInSession(),
                                                                                          transientStateManager,
                                                                                          mimeType,
                                                                                          portletData.getSupports(),
                                                                                          markupParams.isSecureClientCommunication(),
                                                                                          portletHandle,
                                                                                          persistentStateManager,
                                                                                          sessionID,
                                                                                          portletData.getEscapeXml(),
                                                                                          ResourceURL.PAGE,
                                                                                          portletData.getSupportedPublicRenderParameter(),
                                                                                          ((PortletDataImp) portletData).getWrappedPortletTyped());
    // ---------- END FOR CREATING FACTORY --------------

    // manage mode and states
    PortletMode portletMode = Modes.getJsrPortletMode(markupParams.getMode());
    WindowState windowState = WindowStates.getJsrWindowState(markupParams.getWindowState());

    // prepare the call to the portlet container
    WSRPHttpServletRequest request = (WSRPHttpServletRequest) WSRPHTTPContainer.getInstance()
                                                                               .getRequest();
    WSRPHttpServletResponse response = (WSRPHttpServletResponse) WSRPHTTPContainer.getInstance()
                                                                                  .getResponse();
    WSRPHTTPContainer.getInstance().getRequest().setWsrpSession(session);

    // for get params within included jsp struts
    request.setParameters(renderParameters);

    // prepare the Input object
    RenderInput input = new RenderInput();
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(owner);
    windowID.setPortletApplicationName(portletApplicationName);
    windowID.setPortletName(portletName);
    windowID.setUniqueID(uniqueID);
    input.setInternalWindowID(windowID);
    input.setBaseURL(null);
    input.setPortletURLFactory(portletURLFactory);
    input.setEscapeXml(true);
    input.setUserAttributes(new HashMap<String, String>());
    input.setPortletMode(portletMode);
    input.setWindowState(windowState);
    input.setMarkup(mimeType);
    input.setRenderParameters(renderParameters);
    input.setPublicParamNames(publicParamNames);
    input.setPortletState(portletState);
    input.setPortletPreferencesPersister(persister);
    // createUserProfile(userContext, request, session);

    RenderOutput output = null;
    try {
      //INVOKE
      output = proxy.render(request, response, input);
      if (output.hasError())
        throw new WSRPException("render output hasError");
    } catch (WSRPException e) {
      log.debug("The call to render method was a failure ", e);
      e.printStackTrace();
      throw new WSRPException();
    }

    // preparing cache control object
    CacheControl cacheControl = null;
    try {
      cacheControl = transientStateManager.getCacheControl(portletData);
    } catch (WSRPException e) {
      e.printStackTrace();
      throw new WSRPException();
    }

    // preparing markup context
    MarkupContext markupContext = new MarkupContext();
    markupContext.setCacheControl(cacheControl);
    //markupContext.setCcppProfileWarning(ccppProfileWarning);
    //markupContext.setClientAttributes(clientAttributes);
    markupContext.setItemBinary(output.getBinContent());
    markupContext.setItemString(removeNonValidXMLCharacters(new String(output.getContent())));
    markupContext.setLocale("en");
    markupContext.setMimeType(mimeType);
    markupContext.setPreferredTitle(output.getTitle());
    markupContext.setRequiresRewriting(!conf.isDoesUrlTemplateProcessing());
    markupContext.setUseCachedItem(false);
    Collection<PortletMode> portletModes = output.getNextPossiblePortletModes();
    if (portletModes != null && !portletModes.isEmpty())
      markupContext.getValidNewModes().addAll(getValidNewModes(portletModes));

    // preparing markup response
    MarkupResponse markupResponse = new MarkupResponse();
    markupResponse.setMarkupContext(markupContext);
    markupResponse.setSessionContext(sessionContext);

    return markupResponse;
  }

  private Collection<String> getValidNewModes(Collection<PortletMode> portletModes) {
    if (portletModes == null || portletModes.isEmpty())
      return null;
    List<String> validNewModes = new ArrayList<String>();
    for (PortletMode mode : portletModes) {
      validNewModes.add(Modes.addPrefixWSRP(mode.toString()));
    }
    return validNewModes;
  }

  private PortletData getPortletMetaData(String portletHandle) {
    if (proxy != null) {
      return proxy.getAllPortletMetaData().get(portletHandle);
    }
    return null;
  }

  public BlockingInteractionResponse performBlockingInteraction(RegistrationContext registrationContext,
                                                                PortletContext portletContext,
                                                                RuntimeContext runtimeContext,
                                                                UserContext userContext,
                                                                MarkupParams markupParams,
                                                                InteractionParams interactionParams) throws AccessDenied,
                                                                                                    ResourceSuspended,
                                                                                                    UnsupportedMimeType,
                                                                                                    InvalidRegistration,
                                                                                                    InvalidHandle,
                                                                                                    InvalidCookie,
                                                                                                    UnsupportedWindowState,
                                                                                                    InvalidUserCategory,
                                                                                                    UnsupportedMode,
                                                                                                    ModifyRegistrationRequired,
                                                                                                    InvalidSession,
                                                                                                    MissingParameters,
                                                                                                    InconsistentParameters,
                                                                                                    OperationFailed,
                                                                                                    UnsupportedLocale,
                                                                                                    PortletStateChangeRequired,
                                                                                                    WSRPException {

    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
    }
    // manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = null;
    if (k.length > 2)
      uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();

    // manage SESSION
    String sessionID = runtimeContext.getSessionParams().getSessionID();
    //get session from cache or create a new one
    WSRPHttpSession session = resolveSession(sessionID, sessiontimeperiod);
    sessionID = session.getId(); // whether renew ID if it is null

    // manage USER
    // if isUserContextStoredInSession: if userContext is null get user context from cache, else put to cache
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String user = userContext != null ? userContext.getUserContextKey() : null;

    String owner = user;
    log.debug("Owner Context : " + owner);

    ExoContainer cont = ExoContainerContext.getCurrentContainer();
    WindowInfosContainer.createInstance(cont, sessionID, user);

    // build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(sessionID);
    sessionContext.setExpires(sessiontimeperiod);

    // get portlet data
    PortletData portletData = getPortletMetaData(portletApplicationName
        + Constants.PORTLET_META_DATA_ENCODER + portletName);

    // manage mime type
    String mimeType = null;
    try {
      mimeType = getMimeType(markupParams.getMimeTypes(), portletData);
    } catch (WSRPException e) {
      e.printStackTrace();
      throw new WSRPException();
    }

    // ---------- BEGIN FOR CREATING FACTORY --------------
    PortletURLFactory portletURLFactory = WSRPRewriterPortletURLFactoryBuilder.getFactory(conf.isDoesUrlTemplateProcessing(),
                                                                                          runtimeContext,
                                                                                          session,
                                                                                          conf.isTemplatesStoredInSession(),
                                                                                          transientStateManager,
                                                                                          mimeType,
                                                                                          portletData.getSupports(),
                                                                                          markupParams.isSecureClientCommunication(),
                                                                                          portletHandle,
                                                                                          persistentStateManager,
                                                                                          sessionID,
                                                                                          portletData.getEscapeXml(),
                                                                                          ResourceURL.PAGE,
                                                                                          portletData.getSupportedPublicRenderParameter(),
                                                                                          ((PortletDataImp) portletData).getWrappedPortletTyped());
    // ---------- END FOR CREATING FACTORY --------------

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    // manage mode and states
    PortletMode portletMode = Modes.getJsrPortletMode(markupParams.getMode());
    WindowState windowState = WindowStates.getJsrWindowState(markupParams.getWindowState());

    // manage portlet state change
    boolean isStateChangeAuthorized = false;
    String stateChange = interactionParams.getPortletStateChange().value();
    if (StateChange.READ_WRITE.value().equalsIgnoreCase(stateChange)) {
      log.debug("readWrite state change");
      // every modification is allowed on the portlet
      isStateChangeAuthorized = true;
    } else if (StateChange.CLONE_BEFORE_WRITE.value().equalsIgnoreCase(stateChange)) {
      log.debug("cloneBeforWrite state change");
      try {
        portletContext = portletManagementOperationsInterface.clonePortlet(registrationContext,
                                                                           portletContext,
                                                                           userContext,
                                                                           portletContext.getScheduledDestruction());

      } catch (OperationNotSupported e) {
        throw new OperationFailed(e.getMessage(), e);
      }
      // any modification will be made on the
      isStateChangeAuthorized = true;
    } else if (StateChange.READ_ONLY.value().equalsIgnoreCase(stateChange)) {
      log.debug("readOnly state change");
      // if an attempt to change the state is done (means change the portlet
      // pref in JSR 168)
      // then a fault will be launched
      throw new PortletStateChangeRequired("StateChange is READ_ONLY");
    } else {
      log.debug("The submited portlet state change value : " + stateChange + " is not supported");
      throw new PortletStateChangeRequired();
    }

    // PROCESS PARAMETERS

    // get public param names from config
    List<String> publicParamNames = portletData.getSupportedPublicRenderParameter();
    // manage navigational context
    NavigationalContext navigationalContext = markupParams.getNavigationalContext();
    // process opaque (navigational) values
    Map<String, String[]> persistentNavigationalParameters = processNavigationalState(navigationalContext);
    // get navigational (public) values
    Map<String, String[]> navigationalParameters = Utils.getMapParametersFromNamedStringArray(navigationalContext.getPublicValues());
    // manage form parameters
    Map<String, String[]> formParameters = Utils.getMapParametersFromNamedStringArray(interactionParams.getFormParameters());
    // manage interaction state
    Map<String, String[]> persistentInteractionParameters = processInteractionState(interactionParams.getInteractionState());

    // create render params map for input
    Map<String, String[]> renderParameters = new HashMap<String, String[]>();
    if (formParameters != null && !formParameters.isEmpty()) { // default
      renderParameters = formParameters;
    } else if (persistentInteractionParameters != null
        && !persistentInteractionParameters.isEmpty()) {
      renderParameters = persistentInteractionParameters;
    } else if (persistentNavigationalParameters != null
        && !persistentNavigationalParameters.isEmpty()) {
      renderParameters = persistentNavigationalParameters;
    }

    replacePublicParams(renderParameters, publicParamNames, navigationalParameters);

    // prepare objects for portlet container
    WSRPHttpServletRequest request = (WSRPHttpServletRequest) WSRPHTTPContainer.getInstance()
                                                                               .getRequest();
    WSRPHttpServletResponse response = (WSRPHttpServletResponse) WSRPHTTPContainer.getInstance()
                                                                                  .getResponse();
    WSRPHTTPContainer.getInstance().getRequest().setWsrpSession(session);

    // for get params within included jsp struts
    request.setParameters(renderParameters);

    // prepare the Input object
    ActionInput input = new ActionInput();
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(owner);
    windowID.setPortletApplicationName(portletApplicationName);
    windowID.setPortletName(portletName);
    windowID.setUniqueID(uniqueID);
    input.setInternalWindowID(windowID);
    input.setBaseURL(null);
    input.setPortletURLFactory(portletURLFactory);
    input.setEscapeXml(true);
    input.setUserAttributes(new HashMap<String, String>());
    input.setPortletMode(portletMode);
    input.setWindowState(windowState);
    input.setMarkup(mimeType);
    input.setStateChangeAuthorized(isStateChangeAuthorized);
    input.setStateSaveOnClient(conf.isSavePortletStateOnConsumer());
    input.setPortletState(portletState);
    input.setPortletPreferencesPersister(persister);
    input.setRenderParameters(renderParameters);
    input.setPublicParamNames(publicParamNames);
    // createUserProfile(userContext, request, session);
    ActionOutput output = null;
    try {

      // INVOKE
      output = proxy.processAction(request, response, input);
      if (output.hasError()) {
        throw new WSRPException("processAction output hasError()");
      }
    } catch (WSRPException e) {
      e.printStackTrace();
      log.debug("The call to processAction method was a failure ", e);
      throw new WSRPException();
    }

    BlockingInteractionResponse blockingInteractionResponse = new BlockingInteractionResponse();

    if (output.getProperties().get(ActionOutput.SEND_REDIRECT) != null) {
      log.debug("Redirect the response to : "
          + (String) output.getProperties().get(ActionOutput.SEND_REDIRECT));
      blockingInteractionResponse.setRedirectURL((String) output.getProperties()
                                                                .get(ActionOutput.SEND_REDIRECT));
      blockingInteractionResponse.setUpdateResponse(null);
    } else {
      MarkupContext markupContext = null;

      // call render to optimized 
      if (conf.isBlockingInteractionOptimized()) {
        // markupParams.setWindowState(ns);
        MarkupResponse markupResponse = getMarkup(registrationContext,
                                                  portletContext,
                                                  runtimeContext,
                                                  userContext,
                                                  markupParams);
        markupContext = markupResponse.getMarkupContext();
      }

      UpdateResponse updateResponse = new UpdateResponse();

      if (output.getNextMode() != null)
        updateResponse.setNewMode(Modes.getWSRPModeString(output.getNextMode()));
      if (output.getNextState() != null)
        updateResponse.setNewWindowState(WindowStates.getWSRPStateString(output.getNextState()));
      updateResponse.setSessionContext(sessionContext);
      updateResponse.setMarkupContext(markupContext);
      // fill the state to send it to consumer if allowed
      if (conf.isSavePortletStateOnConsumer())
        portletContext.setPortletState(output.getPortletState());
      updateResponse.setPortletContext(portletContext);

      // get render parameters
      renderParameters = output.getRenderParameters();

      // manage navigational state, save render parameters
      String navigationalState = IdentifierUtil.generateUUID(output);//markupParams.getNavigationalContext().getOpaqueValue();
      try {
        log.debug("set new navigational state : " + navigationalState);
        persistentStateManager.putNavigationalState(navigationalState, renderParameters);
      } catch (WSRPException e) {
        e.printStackTrace();
        throw new WSRPException();
      }

      // get public parameters
      Map<String, String[]> publicParameters = null;
      // put all public params from output
      if (publicParamNames != null) {
        publicParameters = new HashMap<String, String[]>();
        for (String name : publicParamNames) {
          if (renderParameters.containsKey(name)) {
            publicParameters.put(name, renderParameters.get(name));
          }
        }
      }

      // create navigational context
      NavigationalContext newNavigationalContext = new NavigationalContext();
      newNavigationalContext.setOpaqueValue(navigationalState);
//      newNavigationalContext.getPublicValues().addAll(c);
      newNavigationalContext.getPublicValues()
                            .addAll(Utils.getNamedStringListParametersFromMap(publicParameters));//setPublicValues(Utils.getNamedStringArrayParametersFromMap(publicParameters));
      updateResponse.setNavigationalContext(newNavigationalContext);

      updateResponse.getEvents().addAll(JAXBEventTransformer.getEventsMarshal(output.getEvents()));

      blockingInteractionResponse.setUpdateResponse(updateResponse);
    }

    return blockingInteractionResponse;

  }

  //delete all public and put new public from input
  private void replacePublicParams(Map<String, String[]> renderParameters,
                                   List<String> publicParamNames,
                                   Map<String, String[]> navigationalParameters) {
    // delete all public 
    if (publicParamNames != null) {
      for (String param : publicParamNames) {
        if (renderParameters.containsKey(param)) {
          renderParameters.remove(param);
        }
      }
    }
    // put new public params
    if (navigationalParameters != null) {
      renderParameters.putAll(navigationalParameters);
    }
  }

  public ResourceResponse getResource(RegistrationContext registrationContext,
                                      PortletContext portletContext,
                                      RuntimeContext runtimeContext,
                                      UserContext userContext,
                                      ResourceParams resourceParams) throws OperationNotSupported,
                                                                    AccessDenied,
                                                                    ResourceSuspended,
                                                                    UnsupportedMimeType,
                                                                    InvalidRegistration,
                                                                    InvalidHandle,
                                                                    InvalidCookie,
                                                                    UnsupportedWindowState,
                                                                    InvalidUserCategory,
                                                                    UnsupportedMode,
                                                                    ModifyRegistrationRequired,
                                                                    InvalidSession,
                                                                    MissingParameters,
                                                                    InconsistentParameters,
                                                                    OperationFailed,
                                                                    UnsupportedLocale,
                                                                    WSRPException {

    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
    }

    // manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    log.debug("Portlet handle : " + portletHandle);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = null;
    if (k.length > 2)
      uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();

    // manage SESSION
    String sessionID = runtimeContext.getSessionParams().getSessionID();
    //get session from cache or create a new one
    WSRPHttpSession session = resolveSession(sessionID, sessiontimeperiod);
    sessionID = session.getId(); // whether renew ID if it is null

    // manage USER
    // if isUserContextStoredInSession: if userContext is null get user context from cache, else put to cache
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String user = userContext != null ? userContext.getUserContextKey() : null;

    String owner = user;
    log.debug("Owner Context : " + owner);

    ExoContainer cont = ExoContainerContext.getCurrentContainer();
    WindowInfosContainer.createInstance(cont, sessionID, user);

    // build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(sessionID);
    sessionContext.setExpires(sessiontimeperiod);

    // manage cache
    if (resourceParams.getValidateTag() != null) {
      try {
        if (transientStateManager.validateCache(resourceParams.getValidateTag())) {
          ResourceContext resourceContext = new ResourceContext();
          resourceContext.setUseCachedItem(new Boolean(true));
          ResourceResponse resourceResponse = new ResourceResponse();
          resourceResponse.setResourceContext(resourceContext);
          resourceResponse.setSessionContext(sessionContext);
          return resourceResponse;
        }
      } catch (WSRPException e) {
        log.debug("Can not validate resource cache for validateTag : "
            + resourceParams.getValidateTag());
        throw new WSRPException();
      }
    }

    // get portlet data
    PortletData portletData = getPortletMetaData(portletApplicationName
        + Constants.PORTLET_META_DATA_ENCODER + portletName);

    // PROCESS PARAMETERS

    // get public param names from config
    List<String> publicParamNames = portletData.getSupportedPublicRenderParameter();
    // manage navigational context
    NavigationalContext navigationalContext = resourceParams.getNavigationalContext();
    // process opaque (navigational) values
    Map<String, String[]> persistentNavigationalParameters = processNavigationalState(navigationalContext);
    // get navigational (public) values
    Map<String, String[]> navigationalParameters = Utils.getMapParametersFromNamedStringArray(navigationalContext.getPublicValues());
    // manage form parameters
    Map<String, String[]> formParameters = Utils.getMapParametersFromNamedStringArray(resourceParams.getFormParameters());
    // process resource parameters
    Map<String, String[]> persistentResourceParameters = processResourceState(resourceParams.getResourceState());

    // create render params map for input
    Map<String, String[]> renderParameters = new HashMap<String, String[]>();
    ;
    if (formParameters != null && !formParameters.isEmpty()) { // default
      renderParameters = formParameters;
    } else if (persistentResourceParameters != null && !persistentResourceParameters.isEmpty()) {
      renderParameters = persistentResourceParameters;
    } else if (persistentNavigationalParameters != null
        && !persistentNavigationalParameters.isEmpty()) {
      renderParameters = persistentNavigationalParameters;
    }

    replacePublicParams(renderParameters, publicParamNames, navigationalParameters);

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    // manage mime type
    String mimeType = null;
    try {
      mimeType = getMimeType(resourceParams.getMimeTypes(), portletData);
    } catch (WSRPException e) {
      throw new WSRPException();
    }

    // ---------- BEGIN CREATING FACTORY --------------
    PortletURLFactory portletURLFactory = WSRPRewriterPortletURLFactoryBuilder.getFactory(conf.isDoesUrlTemplateProcessing(),
                                                                                          runtimeContext,
                                                                                          session,
                                                                                          conf.isTemplatesStoredInSession(),
                                                                                          transientStateManager,
                                                                                          mimeType,
                                                                                          portletData.getSupports(),
                                                                                          resourceParams.isSecureClientCommunication(),
                                                                                          portletHandle,
                                                                                          persistentStateManager,
                                                                                          sessionID,
                                                                                          portletData.getEscapeXml(),
                                                                                          ResourceURL.PAGE,
                                                                                          portletData.getSupportedPublicRenderParameter(),
                                                                                          ((PortletDataImp) portletData).getWrappedPortletTyped());
    // ---------- END CREATING FACTORY --------------

    // manage mode and states
    PortletMode portletMode = Modes.getJsrPortletMode(resourceParams.getMode());
    WindowState windowState = WindowStates.getJsrWindowState(resourceParams.getWindowState());

    // prepare the call to the portlet container
    WSRPHttpServletRequest request = (WSRPHttpServletRequest) WSRPHTTPContainer.getInstance()
                                                                               .getRequest();
    WSRPHttpServletResponse response = (WSRPHttpServletResponse) WSRPHTTPContainer.getInstance()
                                                                                  .getResponse();
    WSRPHTTPContainer.getInstance().getRequest().setWsrpSession(session);

    // for get params within included jsp struts
    request.setParameters(renderParameters);

    // putFormParametersInRequest(request, resourceParams);

    // preparing Input object
    ResourceInput input = new ResourceInput();
    ExoWindowID windowID = new ExoWindowID();
    windowID.setOwner(owner);
    windowID.setPortletApplicationName(portletApplicationName);
    windowID.setPortletName(portletName);
    windowID.setUniqueID(uniqueID);
    input.setInternalWindowID(windowID);
    input.setBaseURL(null);
    input.setPortletURLFactory(portletURLFactory);
    input.setEscapeXml(true);
    input.setUserAttributes(new HashMap<String, String>());
    input.setPortletMode(portletMode);
    input.setWindowState(windowState);
    input.setMarkup(mimeType);
    input.setRenderParameters(renderParameters);
    input.setPublicParamNames(publicParamNames);
    input.setPortletState(portletState);
    input.setPortletPreferencesPersister(persister);
    input.setResourceID(resourceParams.getResourceID());
    input.setCacheability(resourceParams.getResourceCacheability());
    // createUserProfile(userContext, request, session);

    ResourceOutput output = null;
    try {
      /* MAIN INVOKE */
      output = proxy.serveResource(request, response, input);
      if (output.hasError())
        throw new WSRPException("serveResource output hasError");
    } catch (WSRPException e) {
      log.debug("The call to render method was a failure ", e);
      throw new WSRPException();
    }

    // preparing cache control object
    CacheControl cacheControl = null;
    try {
      cacheControl = transientStateManager.getCacheControl(portletData);
    } catch (WSRPException e) {
      throw new WSRPException();
    }
    //output.getProperties() // TODO
    // preparing resource context
    ResourceContext resourceContext = new ResourceContext();
    resourceContext.setCacheControl(cacheControl);
    // resourceContext.setCcppProfileWarning(ccppProfileWarning);
    // resourceContext.setClientAttributes(clientAttributes);
    resourceContext.setItemBinary(output.getBinContent()); // TODO next comment
    resourceContext.setItemString(removeNonValidXMLCharacters(new String(output.getContent())));
    resourceContext.setLocale("en");
    resourceContext.setMimeType(output.getContentType());// was: mimeType
    resourceContext.setRequiresRewriting(!conf.isDoesUrlTemplateProcessing());
    resourceContext.setUseCachedItem(Boolean.FALSE);

    ResourceResponse resourceResponse = new ResourceResponse();
    resourceResponse.setPortletContext(portletContext);
    resourceResponse.setResourceContext(resourceContext);
    resourceResponse.setSessionContext(sessionContext);
    return resourceResponse;
  }

  public HandleEventsResponse handleEvents(RegistrationContext registrationContext,
                                           PortletContext portletContext,
                                           RuntimeContext runtimeContext,
                                           UserContext userContext,
                                           MarkupParams markupParams,
                                           EventParams eventParams) throws OperationNotSupported,
                                                                   AccessDenied,
                                                                   ResourceSuspended,
                                                                   UnsupportedMimeType,
                                                                   InvalidRegistration,
                                                                   InvalidHandle,
                                                                   InvalidCookie,
                                                                   UnsupportedWindowState,
                                                                   InvalidUserCategory,
                                                                   UnsupportedMode,
                                                                   ModifyRegistrationRequired,
                                                                   InvalidSession,
                                                                   MissingParameters,
                                                                   InconsistentParameters,
                                                                   OperationFailed,
                                                                   UnsupportedLocale,
                                                                   PortletStateChangeRequired,
                                                                   WSRPException {

    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
    }
    // manage the portlet handle
    String portletHandle = portletContext.getPortletHandle();
    portletHandle = manageRegistration(portletHandle, registrationContext);
    String[] k = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String portletApplicationName = k[0];
    String portletName = k[1];
    String uniqueID = null;
    if (k.length > 2)
      uniqueID = k[2];

    Integer sessiontimeperiod = getSessionTimePeriod();

    // manage SESSION
    String sessionID = runtimeContext.getSessionParams().getSessionID();
    //get session from cache or create a new one
    WSRPHttpSession session = resolveSession(sessionID, sessiontimeperiod);
    sessionID = session.getId(); // whether renew ID if it is null

    // manage USER
    // if isUserContextStoredInSession: if userContext is null get user context from cache, else put to cache
    userContext = transientStateManager.resolveUserContext(userContext, session);
    String user = userContext != null ? userContext.getUserContextKey() : null;

    String owner = user;
    log.debug("Owner Context : " + owner);

    ExoContainer cont = ExoContainerContext.getCurrentContainer();
    WindowInfosContainer.createInstance(cont, sessionID, user);

    // build the session context
    SessionContext sessionContext = new SessionContext();
    sessionContext.setSessionID(sessionID);
    sessionContext.setExpires(sessiontimeperiod);

    // get portlet data
    PortletData portletData = getPortletMetaData(portletApplicationName
        + Constants.PORTLET_META_DATA_ENCODER + portletName);

    // manage mime type
    String mimeType = null;
    try {
      mimeType = getMimeType(markupParams.getMimeTypes(), portletData);
    } catch (WSRPException e) {
      throw new WSRPException();
    }

    // ---------- BEGIN CREATING FACTORY --------------
    PortletURLFactory portletURLFactory = WSRPRewriterPortletURLFactoryBuilder.getFactory(conf.isDoesUrlTemplateProcessing(),
                                                                                          runtimeContext,
                                                                                          session,
                                                                                          conf.isTemplatesStoredInSession(),
                                                                                          transientStateManager,
                                                                                          mimeType,
                                                                                          portletData.getSupports(),
                                                                                          markupParams.isSecureClientCommunication(),
                                                                                          portletHandle,
                                                                                          persistentStateManager,
                                                                                          sessionID,
                                                                                          portletData.getEscapeXml(),
                                                                                          ResourceURL.PAGE,
                                                                                          portletData.getSupportedPublicRenderParameter(),
                                                                                          ((PortletDataImp) portletData).getWrappedPortletTyped());
    // ---------- END CREATING FACTORY --------------

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    // manage mode and states
    PortletMode portletMode = Modes.getJsrPortletMode(markupParams.getMode());
    WindowState windowState = WindowStates.getJsrWindowState(markupParams.getWindowState());

    // manage portlet state change
    boolean isStateChangeAuthorized = false;
    String stateChange = eventParams.getPortletStateChange().value();
    if (StateChange.READ_WRITE.value().equalsIgnoreCase(stateChange)) {
      log.debug("readWrite state change");
      // every modification is allowed on the portlet
      isStateChangeAuthorized = true;
    } else if (StateChange.CLONE_BEFORE_WRITE.value().equalsIgnoreCase(stateChange)) {
      log.debug("cloneBeforWrite state change");
      portletContext = portletManagementOperationsInterface.clonePortlet(registrationContext,
                                                                         portletContext,
                                                                         userContext,
                                                                         portletContext.getScheduledDestruction());
      // any modification will be made on the
      isStateChangeAuthorized = true;
    } else if (StateChange.READ_ONLY.value().equalsIgnoreCase(stateChange)) {
      log.debug("readOnly state change");
      // if an attempt to change the state is done (means change the portlet
      // pref in JSR 168)
      // then a fault will be launched
    } else {
      log.debug("The submited portlet state change value : " + stateChange + " is not supported");
      throw new PortletStateChangeRequired();
    }

    HandleEventsResponse handleEventsResponse = new HandleEventsResponse();

    List<HandleEventsFailed> failedEventsList = new ArrayList<HandleEventsFailed>();

    List<Event> events = eventParams.getEvents();
    List<javax.portlet.Event> nativeEventsList = JAXBEventTransformer.getEventsUnmarshal(events);
    List<javax.portlet.Event> resultNativeEventsList = new ArrayList<javax.portlet.Event>();

    // PROCESS PARAMETERS

    // get public param names from config
    List<String> publicParamNames = portletData.getSupportedPublicRenderParameter();
    // manage navigational context
    NavigationalContext navigationalContext = markupParams.getNavigationalContext();
    // process opaque (navigational) values
    Map<String, String[]> persistentNavigationalParameters = processNavigationalState(navigationalContext);
    // get navigational (public) values
    Map<String, String[]> navigationalParameters = Utils.getMapParametersFromNamedStringArray(navigationalContext.getPublicValues());

    // create render params map for input
    Map<String, String[]> renderParameters = new HashMap<String, String[]>();

    if (persistentNavigationalParameters != null && !persistentNavigationalParameters.isEmpty()) {
      renderParameters = persistentNavigationalParameters;
    }

    replacePublicParams(renderParameters, publicParamNames, navigationalParameters);

    // prepare objects for portlet container
    WSRPHttpServletRequest request = (WSRPHttpServletRequest) WSRPHTTPContainer.getInstance()
                                                                               .getRequest();
    WSRPHttpServletResponse response = (WSRPHttpServletResponse) WSRPHTTPContainer.getInstance()
                                                                                  .getResponse();
    WSRPHTTPContainer.getInstance().getRequest().setWsrpSession(session);
    // putInteractionParameterInRequest(request, eventParams);

    // for get params within included jsp struts
    request.setParameters(renderParameters);

    String navigationalState = null;

    Integer index = 0;
    int eventsLength = events.size();

    // iteration over incoming javax events
    Iterator<javax.portlet.Event> nativeEventsListIterator = nativeEventsList.iterator();

    EventOutput output = null;

    // was: "while (nativeEventsListIterator.hasNext()) {"
    // but we have one "setNewMode" and "setNewWindowState" for updateResponse
    if (nativeEventsListIterator.hasNext()) {
      javax.portlet.Event event = nativeEventsListIterator.next();

      // prepare the Input object
      EventInput input = new EventInput();
      ExoWindowID windowID = new ExoWindowID();
      windowID.setOwner(owner);
      windowID.setPortletApplicationName(portletApplicationName);
      windowID.setPortletName(portletName);
      windowID.setUniqueID(uniqueID);
      input.setInternalWindowID(windowID);
      input.setBaseURL(null);
      input.setPortletURLFactory(portletURLFactory);
      input.setEscapeXml(true);
      input.setUserAttributes(new HashMap<String, String>());
      input.setPortletMode(portletMode);
      input.setWindowState(windowState);
      input.setMarkup(mimeType);
      input.setEvent(event);
      input.setRenderParameters(renderParameters);
      input.setPublicParamNames(publicParamNames);
      //      input.setStateChangeAuthorized(isStateChangeAuthorized);
      input.setStateSaveOnClient(conf.isSavePortletStateOnConsumer());
      input.setPortletState(portletState);
      input.setPortletPreferencesPersister(persister);
      // createUserProfile(userContext, request, session);

      try {
        /* MAIN INVOKE */
        output = proxy.processEvent(request, response, input);
        if (output.hasError())
          throw new WSRPException("processEvent output hasError");
      } catch (WSRPException e) {
        log.debug("The call to processEvent with event: '" + event.getName()
            + "' method was a failure ", e);
        HandleEventsFailed handleEventsFailed = new HandleEventsFailed();
        //        handleEventsFailed.setErrorCode(ErrorCodes.fromValue(new QName(e.getFault())));
        LocalizedString reason = new LocalizedString();
        reason.setValue(e.getLocalizedMessage());
        handleEventsFailed.setReason(reason);
        BigInteger indexBigInteger = new BigInteger(index.toString());
        handleEventsFailed.getIndex().add(indexBigInteger);
        failedEventsList.add(handleEventsFailed);
        throw new WSRPException();
      } finally {
        index++;
      }

      resultNativeEventsList.addAll(output.getEvents());

      // get render parameters for next iteration of processEvent or to client
      renderParameters = output.getRenderParameters();

      // unnecessary manage navigational state for each process event
      //      if (eventsLength == 1) {
      navigationalState = IdentifierUtil.generateUUID(output);
      try {
        log.debug("set new navigational state : " + navigationalState);
        persistentStateManager.putNavigationalState(navigationalState, renderParameters);
      } catch (WSRPException e) {
        throw new WSRPException();
      }
      //      }
    }

    UpdateResponse updateResponse = new UpdateResponse();
    updateResponse.getEvents()
                  .addAll(JAXBEventTransformer.getEventsMarshal(resultNativeEventsList));
    if (output.getNextMode() != null)
      updateResponse.setNewMode(Modes.getWSRPModeString(output.getNextMode()));
    if (output.getNextState() != null)
      updateResponse.setNewWindowState(WindowStates.getWSRPStateString(output.getNextState()));
    updateResponse.setSessionContext(sessionContext);
    MarkupContext markupContext = null;

    // call render to optimized 
    if (conf.isHandleEventsOptimized()) {
      // markupParams.setWindowState(ns);
      MarkupResponse markupResponse = getMarkup(registrationContext,
                                                portletContext,
                                                runtimeContext,
                                                userContext,
                                                markupParams);
      markupContext = markupResponse.getMarkupContext();
    }
    updateResponse.setMarkupContext(markupContext);
    updateResponse.setPortletContext(portletContext);

    // get public parameters
    Map<String, String[]> publicParameters = new HashMap<String, String[]>();
    // put all public params from output
    if (publicParamNames != null) {
      for (String name : publicParamNames) {
        if (renderParameters.containsKey(name)) {
          publicParameters.put(name, renderParameters.get(name));
        }
      }
    }

    // create navigational context
    NavigationalContext newNavigationalContext = new NavigationalContext();
    newNavigationalContext.setOpaqueValue(navigationalState);
    newNavigationalContext.getPublicValues()
                          .addAll(Utils.getNamedStringListParametersFromMap(publicParameters));
    updateResponse.setNavigationalContext(navigationalContext);

    handleEventsResponse.setUpdateResponse(updateResponse);
    // converting failed events from list to array and set that
    if (failedEventsList != null)
      handleEventsResponse.getFailedEvents().addAll(failedEventsList);

    return handleEventsResponse;
  }

  public ReturnAny initCookie(RegistrationContext registrationContext, UserContext userContext) throws OperationNotSupported,
                                                                                               AccessDenied,
                                                                                               ResourceSuspended,
                                                                                               InvalidRegistration,
                                                                                               ModifyRegistrationRequired,
                                                                                               OperationFailed,
                                                                                               WSRPException {

    try {
      if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
        LifetimeHelper.checkRegistrationLifetime(registrationContext, userContext);
      }
    } catch (InvalidHandle ih) {
      throw new InvalidRegistration(ih.getMessage(), ih);
    }
    WSRPHTTPContainer.getInstance().getRequest().getSession();
    return new ReturnAny();
  }

  public ReturnAny releaseSessions(RegistrationContext registrationContext,
                                   List<String> sessionIDs,
                                   UserContext userContext) throws OperationNotSupported,
                                                           AccessDenied,
                                                           ResourceSuspended,
                                                           InvalidRegistration,
                                                           ModifyRegistrationRequired,
                                                           MissingParameters,
                                                           OperationFailed,
                                                           WSRPException {

    try {
      if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
        LifetimeHelper.checkRegistrationLifetime(registrationContext, userContext);
      }
    } catch (InvalidHandle ih) {
      throw new InvalidRegistration(ih.getMessage(), ih);
    }
    for (Iterator<String> iterator = sessionIDs.iterator(); iterator.hasNext();) {
      String name = iterator.next();
      transientStateManager.releaseSession(name);
    }
    return new ReturnAny();
  }

  private WSRPHttpSession resolveSession(String sessionID, Integer sessiontimeperiod) throws InvalidSession {
    WSRPHttpSession session = null;

    session = transientStateManager.resolveSession(sessionID, sessiontimeperiod);

    log.debug("Use session with ID : " + session.getId());
    return session;
  }

  private String manageRegistration(String portletHandle, RegistrationContext registrationContext) throws InvalidRegistration,
                                                                                                  InvalidHandle {
    log.debug("manageRegistration called for portlet handle : " + portletHandle);
    if (!proxy.isPortletOffered(portletHandle)) {
      log.debug("The latter handle is not offered by the Producer");
      throw new InvalidHandle();
    } else {
      String[] keys = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
      if (keys.length == 2) {
        portletHandle += Constants.PORTLET_HANDLE_ENCODER
            + String.valueOf(portletHandle.hashCode()); // DEFAULT_WINDOW_ID;
      }
    }
    RegistrationVerifier.checkRegistrationContext(registrationContext);
    return portletHandle;
  }

  private Map<String, String[]> processNavigationalState(NavigationalContext navigationalContext) throws WSRPException {
    Map<String, String[]> map = null;
    try {
      String navigationalState = navigationalContext.getOpaqueValue();
      log.debug("Lookup navigational state : " + navigationalState);
      map = persistentStateManager.getNavigationalState(navigationalState);
      // for debug:
      if (log.isDebugEnabled() && map != null) {
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
          String key = iterator.next();
          log.debug("attribute name in map referenced by navigationalState : " + key);
        }
      }
    } catch (WSRPException e) {
      throw new WSRPException();
    }
    return map;
  }

  private Map<String, String[]> processInteractionState(String interactionState) throws WSRPException {
    Map<String, String[]> map = null;
    try {
      log.debug("Lookup interaction state : " + interactionState);
      map = persistentStateManager.getInteractionSate(interactionState);
      // for debug:
      if (log.isDebugEnabled() && map != null) {
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
          String key = iterator.next();
          log.debug("attribute name in map referenced by interactionState : " + key);
        }
      }
    } catch (WSRPException e) {
      throw new WSRPException();
    }
    if (map == null)
      map = new HashMap<String, String[]>();
    return map;
  }

  private Map<String, String[]> processResourceState(String resourceState) throws WSRPException {
    Map<String, String[]> map = null;
    try {
      log.debug("Lookup resource state : " + resourceState);
      map = persistentStateManager.getResourceState(resourceState);
      // for debug:
      if (log.isDebugEnabled() && map != null) {
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
          String key = iterator.next();
          log.debug("attribute name in map referenced by resourceState : " + key);
        }
      }
    } catch (WSRPException e) {
      throw new WSRPException();
    }
    if (map == null)
      map = new HashMap<String, String[]>();
    return map;
  }

  private String getMimeType(List<String> mimeTypes, PortletData portletData) throws WSRPException {
    if (mimeTypes == null || mimeTypes.size() == 0) {
      log.debug("the given array of MimeTypes is empty or null");
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
    List<Supports> l = portletData.getSupports();
    for (String mimeType : mimeTypes) {
      for (Iterator<Supports> iterator = l.iterator(); iterator.hasNext();) {
        String supports = (iterator.next()).getMimeType();
        if (supports.equalsIgnoreCase(mimeType))
          return mimeType;
      }
    }
    log.debug("No mime type is supported");
    throw new WSRPException(Faults.UNSUPPORTED_MIME_TYPE_FAULT);
  }

  private byte[] managePortletState(PortletContext portletContext) {
    // default is "wsrp.save.portlet.state.on.consumer"="false"
    if (conf.isSavePortletStateOnConsumer()) {
      log.debug("Save state on consumer");
      return portletContext.getPortletState();
    }
    log.debug("Save state on producer");
    return null;
  }

  private Integer getSessionTimePeriod() {
    try {
      WSRPHttpSession wsrpHttpSession = (WSRPHttpSession) WSRPHTTPContainer.getInstance()
                                                                           .getRequest()
                                                                           .getSession();
      return wsrpHttpSession.getMaxInactiveInterval();
    } catch (Exception e) {
      System.out.println("MarkupOperationsInterfaceImpl.getSessionTimePeriod: = " + e.getCause());
      e.printStackTrace();
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
      if ((current == 0x9) || (current == 0xA) || (current == 0xD)
          || ((current >= 0x20) && (current <= 0xD7FF))
          || ((current >= 0xE000) && (current <= 0xFFFD))
          || ((current >= 0x10000) && (current <= 0x10FFFF)))
        out.append(current);
    }
    return out.toString();
  }
}
