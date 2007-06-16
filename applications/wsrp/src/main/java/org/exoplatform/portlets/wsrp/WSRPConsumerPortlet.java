/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.portlets.wsrp;

import java.io.*;
import java.util.*;
import javax.portlet.*;
import javax.portlet.PortletSession;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
//import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.consumer.*;
import org.exoplatform.services.wsrp.consumer.adapters.*;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.type.*;
import org.exoplatform.services.wsrp.utils.Modes;
import org.exoplatform.services.wsrp.utils.Utils;
import org.exoplatform.services.wsrp.utils.WindowStates;
//import org.exoplatform.container.*;
import javax.servlet.http.HttpServletRequest;


/*
 * A part of the business logic of this portlet was taken from the WSRP4J
 * project
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 7 févr. 2004
 * Time: 18:38:56
 */
public class WSRPConsumerPortlet extends GenericPortlet {
  public static final String USER_SESSIONS_KEY = "user_session_key";
  private static final String[] characterEncodings = {"UTF-8"};
  private static final String[] mimeTypes = {"text/html", "text/wml"};
  private static final String userAgent = "userAgent";
  private static final String basePath = "/portal/";
  public static final String[] CONSUMER_SCOPES = {"chunk_data"};
  public static final String[] CONSUMER_CUSTOM_PROFILES = {"what_more"};
  public static final String[] SUPPORTED_LOCALES = {"en", "fr"};

  private static PortletMode wsrpMode_ = new PortletMode("wsrp");

  private WSRPConfigModeHandler wsrpConfigModeHandler_;
  private ConsumerEnvironment consumerEnvironment;
  private PortletContainerService portletContainerService;
  private Log log;
  private URLTemplateComposer templateComposer;
  private static boolean init;

  public WSRPConsumerPortlet() {

    ExoContainer container = null;
    if ( ExoContainerContext.getTopContainer() instanceof RootContainer) {
      //PORTALCONTAINER
      container = PortalContainer.getInstance();
    } else {
      //STANDALONE
      container = ExoContainerContext.getTopContainer();
    }

    this.consumerEnvironment =      (ConsumerEnvironment)     container.getComponentInstanceOfType(ConsumerEnvironment.class);
    this.portletContainerService =  (PortletContainerService) container.getComponentInstanceOfType(PortletContainerService.class);
    this.log =                      ExoLogger.getLogger(getClass());
    this.templateComposer =         (URLTemplateComposer)     container.getComponentInstanceOfType(URLTemplateComposer.class);
  }

  public void init(PortletConfig portletConfig) throws PortletException {
    super.init(portletConfig);
    wsrpConfigModeHandler_  =
      new WSRPConfigModeHandler(portletConfig, consumerEnvironment, log);
    initConsumer(portletConfig);
  }

  public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
    PortletMode mode = request.getPortletMode();
    if (mode.equals(wsrpMode_)) {
      wsrpConfigModeHandler_.processAction(request, response);
    } else {
      Map preferences = getPreferences(request);
      PortletKey portletKey = getPortletKey(preferences);
      if(getProducer(portletKey.getProducerId()) != null){
        wsrpProcessAction(request, response);
      }
    }
  }

  public void wsrpProcessAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    try {
      ActionRequestImp request = (ActionRequestImp) actionRequest;
      String key = request.getInput().getWindowID().generateKey();
      log.debug("use windowID : " + key);

      User user = getUser(actionRequest);
      String userID = "";
      if (user != null) {
        userID = user.getUserID();
        log.debug("use userID : " + userID);
      }
      Map preferences = getPreferences(actionRequest);
      PortletKey portletKey = getPortletKey(preferences);
      WSRPPortlet portlet = getPortlet(portletKey, preferences);
      UserSessionMgr userSession = getUserSession(actionRequest.getPortletSession(),
          portletKey.getProducerId());

      PortletWindowSession windowSession = getWindowSession(portletKey, portlet,
          userSession, key);
      PortletDriver portletDriver = consumerEnvironment.
          getPortletDriverRegistry().getPortletDriver(portlet);
      InteractionRequest interactionRequest = getInteractionRequest(windowSession, actionRequest);

      BlockingInteractionResponse response = portletDriver.
          performBlockingInteraction(interactionRequest, userSession, userID);
      
      
      
      if (response != null) {
        log.debug("manage BlockingInteractionResponse object content");
        UpdateResponse updateResponse = response.getUpdateResponse();
        String redirectURL = response.getRedirectURL();
        if (updateResponse != null) {
          if (windowSession != null) {
            updateSessionContext(updateResponse.getSessionContext(), windowSession.getPortletSession());
            windowSession.updateMarkupCache(updateResponse.getMarkupContext());
          }
          updatePortletContext(actionRequest, updateResponse.getPortletContext(), portlet);
          String navState = updateResponse.getNavigationalState();
          if (navState != null) {
            log.debug("set new navigational state : " + navState);
            actionResponse.setRenderParameter(WSRPConstants.WSRP_NAVIGATIONAL_STATE, navState);
          }
          String newMode = updateResponse.getNewMode();
          if (newMode != null) {
            log.debug("set Mode required : " + newMode);
            try {
              if (newMode.equalsIgnoreCase(Modes._view)) {
                actionResponse.setPortletMode(PortletMode.VIEW);
              } else if (newMode.equalsIgnoreCase(Modes._edit)) {
                actionResponse.setPortletMode(PortletMode.EDIT);
              } else if (newMode.equalsIgnoreCase(Modes._help)) {
                actionResponse.setPortletMode(PortletMode.HELP);
              }
            } catch (PortletModeException e) {
              log.error("Unable to set new mode", e);
            }
          }
          String newWindowState = updateResponse.getNewWindowState();
          if (newWindowState != null) {
            log.debug("set new required window state : " + newWindowState);
            try {
              if (newWindowState.equalsIgnoreCase(WindowStates._maximized)) {
                actionResponse.setWindowState(WindowState.MAXIMIZED);
              } else if (newWindowState.equalsIgnoreCase(WindowStates._minimized)) {
                actionResponse.setWindowState(WindowState.MINIMIZED);
              } else if (newWindowState.equalsIgnoreCase(WindowStates._normal)) {
                actionResponse.setWindowState(WindowState.NORMAL);
              }
            } catch (WindowStateException e) {
              log.error("uncable to set new window state");
            }
          }
        } else if (redirectURL != null) {
          try {
            log.debug("Redirect action to URL : " + redirectURL);
            actionResponse.sendRedirect(redirectURL);
          } catch (IOException ioEx) {
            log.error("Can not redirect action", ioEx);
          }
        }
      }
    } catch (WSRPException e) {
      throw new PortletException("exception in processAction method", e);
    }
  }

  private void updatePortletContext(PortletRequest request,
                                    org.exoplatform.services.wsrp.type.PortletContext portletContext,
                                    WSRPPortlet portlet)
      throws WSRPException {
    if (portletContext != null && portlet != null) {
      log.debug("update portlet context");
      String newPortletHandle = portletContext.getPortletHandle();
      PortletKey portletKey = portlet.getPortletKey();
      if (newPortletHandle != null && !newPortletHandle.equals(portletKey.getPortletHandle())) {
        log.debug("portlet was cloned, new handle : " + newPortletHandle);
        String producerID = portletKey.getProducerId();
        PortletKey newPortletKey = new PortletKeyAdapter();
        portletKey.setPortletHandle(newPortletHandle);
        portletKey.setPortletHandle(producerID);
        portlet = createPortlet(newPortletKey, portlet.getParent());
        consumerEnvironment.getPortletRegistry().addPortlet(portlet);
        PortletPreferences preferences = request.getPreferences();
        try {
          preferences.setValue(WSRPConstants.WSRP_PORTLET_HANDLE, newPortletHandle);
          preferences.setValue(WSRPConstants.WSRP_PARENT_HANDLE, portlet.getParent());
          preferences.store();
        } catch (Exception e) {
          log.error("unable to store the new portlet info in the preferences", e);
          throw new WSRPException(Faults.PREFERENCES_STORING_IMPOSSIBLE, e);
        }
      }
      portlet.setPortletContext(portletContext);
    }
  }

  public void render(RenderRequest request, RenderResponse response)
      throws PortletException, IOException {
    try {
      if(!init){
        templateComposer.setHost(request.getServerName());
        templateComposer.setPort(request.getServerPort());
      }
      response.setTitle(getTitle(request));
      WindowState state = request.getWindowState();
      if (!state.equals(WindowState.MINIMIZED)) {
        PortletMode mode = request.getPortletMode();
        if (mode.equals(wsrpMode_)) {
          wsrpConfigModeHandler_.render(request, response);
        } else {
          Map preferences = getPreferences(request);
          PortletKey portletKey = getPortletKey(preferences);
          if(getProducer(portletKey.getProducerId()) == null){
            response.setContentType("text/html");
            Writer w = response.getWriter();

            PortletURL actionURL = response.createActionURL();
            actionURL.setPortletMode(wsrpMode_);

            w.write("<a href='" + actionURL.toString() + "'>");
            w.write("Configure a producer first");
            w.write("</a>");

          } else {
            wsrpRender(request, response);
          }
        }
      }
    } catch (Exception e) {
      System.out.println( e.getMessage());
    }

  }

  public void wsrpRender(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
    log.debug("WSRPConsumerPortlet render entered");
    RenderRequestImp request = (RenderRequestImp) renderRequest;
    WindowID windowID = request.getInput().getWindowID();
    String key = windowID.generateKey();
    log.debug("key generated by windowID : " + key);
    renderResponse.setContentType(renderRequest.getResponseContentType());

    User user = getUser(renderRequest);
    String userID = null;
    if (user != null) {
      log.debug("use userID : " + userID);
      userID = user.getUserID();
    }
    Map preferences = getPreferences(renderRequest);
    PortletKey portletKey = getPortletKey(preferences);
    WSRPPortlet portlet = null;
    PortletDriver portletDriver = null;
    PortletWindowSession portletWindowSession = null;
    MarkupResponse response = null;
    UserSessionMgr userSession = null;
    try {
      userSession = getUserSession(renderRequest.getPortletSession(), portletKey.getProducerId());
      portlet = getPortlet(portletKey, preferences);
      portletDriver = consumerEnvironment.getPortletDriverRegistry().getPortletDriver(portlet);
      portletWindowSession = getWindowSession(portletKey, portlet, userSession, key);
      WSRPMarkupRequest markupRequest = getMarkupRequest(renderRequest, portletWindowSession);

      String path = null;
      HttpServletRequest servletRequest = ((HttpServletRequest) renderRequest);
      path = servletRequest.getRequestURI();
      log.debug("User path info : " + path);
      if (path == null) path = basePath;
      path += "?";
      String remoteUser = servletRequest.getRemoteUser();
      if (remoteUser != null) {
        path += Constants.PORTAL_CONTEXT + "=" + servletRequest.getRemoteUser() + "&";          
      }
      path += Constants.COMPONENT_PARAMETER + "=";
      
      log.debug("use base path : " + path + windowID.getUniqueID());
      response = portletDriver.getMarkup(markupRequest, userSession, path + windowID.getUniqueID());
      if (response != null) {
        if (portletWindowSession != null) {
          updateSessionContext(response.getSessionContext(), portletWindowSession.getPortletSession());
        }
        processMarkupContext(response.getMarkupContext(), renderResponse);
      }
      if (portletWindowSession != null) {
        log.debug("Update cache");
        portletWindowSession.updateMarkupCache(null);
      }
    } catch (Throwable t) {
      log.error("WS Fault occured", t);
      Writer w = renderResponse.getWriter();
      w.write("a WSRP Fault occured");
    }
  }

  private PortletKey getPortletKey(Map preferences) throws PortletException {
    PortletKey portletKey = null;
    String portletHandle = (String) preferences.get(WSRPConstants.WSRP_PORTLET_HANDLE);
    if (portletHandle != null) {
      String producerID = (String) preferences.get(WSRPConstants.WSRP_PRODUCER_ID);
      if (producerID != null) {
        portletKey = new PortletKeyAdapter();
        portletKey.setProducerId(producerID);
        log.debug("user portlet key, producerID : ");
        portletKey.setPortletHandle(portletHandle);
      } else {
        throw new PortletException("no producer id specified");
      }
    } else {
      throw new PortletException("no portlet handle specified for portlet");
    }
    return portletKey;
  }

  private WSRPPortlet getPortlet(PortletKey portletKey, Map preferences) throws WSRPException {
    WSRPPortlet portlet = null;
    if (portletKey != null) {
      portlet = consumerEnvironment.getPortletRegistry().getPortlet(portletKey);
      if (portlet == null) {
        String parentHandle = (String) preferences.get(WSRPConstants.WSRP_PARENT_HANDLE);
        portlet = createPortlet(portletKey, parentHandle);
        consumerEnvironment.getPortletRegistry().addPortlet(portlet);
      }
    }
    return portlet;
  }

  private WSRPPortlet createPortlet(PortletKey portletKey, String parentHandle) {
    WSRPPortlet portlet = new WSRPPortletAdapter(portletKey);
    org.exoplatform.services.wsrp.type.PortletContext portletContext =
        new org.exoplatform.services.wsrp.type.PortletContext();
    portletContext.setPortletHandle(portletKey.getPortletHandle());
    portlet.setPortletContext(portletContext);
    if (parentHandle != null) {
      portlet.setParent(parentHandle);
    } else {
      portlet.setParent(portletKey.getPortletHandle());
    }
    return portlet;
  }

  private Map getPreferences(PortletRequest portletRequest) {
    Map preferences = new HashMap();
    log.debug("getPreferences entered");
    Enumeration keys = portletRequest.getPreferences().getNames();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      String value = portletRequest.getPreferences().getValue(key, null);
      log.debug("use of preference, key : " + key + " & value : " + value);
      preferences.put(key, value);
    }
    return preferences;
  }

  protected void initConsumer(PortletConfig portletConfig) {
    consumerEnvironment.setCharacterEncodingSet(characterEncodings);
    consumerEnvironment.setConsumerAgent(portletConfig.getInitParameter("consumerAgent"));
    consumerEnvironment.setMimeTypes(mimeTypes);
    consumerEnvironment.setPortletStateChange(StateChange.readWrite);
    consumerEnvironment.setSupportedModes(getPortletModes(portletContainerService.getSupportedPortletModes()));
    consumerEnvironment.setSupportedWindowStates(getWindowStates(portletContainerService.getSupportedWindowStates()));
    consumerEnvironment.setUserAuthentication(WSRPConstants.NO_USER_AUTHENTIFICATION);
    consumerEnvironment.setSupportedLocales(SUPPORTED_LOCALES);
  }

  private String[] getWindowStates(Collection supportedWindowStates) {
    String[] array = new String[supportedWindowStates.size()];
    int i = 0;
    for (Iterator iterator = supportedWindowStates.iterator(); iterator.hasNext(); i++) {
      WindowState windowState = (WindowState) iterator.next();
      array[i] = WSRPConstants.WSRP_PREFIX + windowState.toString();
    }
    return array;
  }

  private String[] getPortletModes(Collection supportedPortletModes) {
    String[] array = new String[supportedPortletModes.size()];
    int i = 0;
    for (Iterator iterator = supportedPortletModes.iterator(); iterator.hasNext(); i++) {
      PortletMode portletMode = (PortletMode) iterator.next();
      array[i] = WSRPConstants.WSRP_PREFIX + portletMode.toString();
    }
    return array;
  }

  private User getUser(PortletRequest request) {
    User user = null;
    WindowInfosContainer scontainer = WindowInfosContainer.getInstance();
    if (scontainer != null) {
      String userKey = scontainer.getOwner();
      log.debug("getUser method with user key : " + userKey);
      user = consumerEnvironment.getUserRegistry().getUser(userKey);
      if (user == null) {
        user = new UserAdapter();
        UserContext userContext = new UserContext();
        userContext.setProfile(fillUserProfile(request));
        userContext.setUserContextKey(userKey);
        user.setUserID(userKey);
        user.setUserContext(userContext);
        consumerEnvironment.getUserRegistry().addUser(user);
      }
    }
    return user;
  }

  private UserProfile fillUserProfile(PortletRequest request) {
    UserProfile userProfile = null;
    Map userInfo = (Map) request.getAttribute(PortletRequest.USER_INFO);
    if (userInfo != null) {
      userProfile = new UserProfile();
      PersonName personName = new PersonName();
      Object nameObject = userInfo.get("name.given");
      if(nameObject == null)
        personName.setGiven("unknow name");
      else
        personName.setGiven(nameObject.toString());
      userProfile.setName(personName);
    }
    return userProfile;
  }

  private WSRPMarkupRequest getMarkupRequest(RenderRequest renderRequest,
                                         PortletWindowSession portletWindowSession) {
    WSRPMarkupRequestAdapter markupRequest = new WSRPMarkupRequestAdapter();
    fillMarkupRequest(markupRequest, renderRequest, portletWindowSession);
    markupRequest.setNavigationalState(getNavigationalState(renderRequest, portletWindowSession));
    markupRequest.setCachedMarkup(portletWindowSession.getCachedMarkup());
    return markupRequest;
  }

  private InteractionRequest getInteractionRequest(PortletWindowSession portletWindowSession,
                                                   ActionRequest actionRequest) {
    log.debug("getInteractionRequest entered");
    InteractionRequestAdapter interactionRequest = new InteractionRequestAdapter();
    fillMarkupRequest(interactionRequest, actionRequest, portletWindowSession);
    interactionRequest.setNavigationalState(getNavigationalState(actionRequest, portletWindowSession));
    Map map = actionRequest.getParameterMap();
    log.debug("Parameter map empty : " + map.isEmpty());
    Set keys = map.keySet();
    NamedString[] array = new NamedString[keys.size()];
    int i = 0;
    for (Iterator iterator = keys.iterator(); iterator.hasNext(); i++) {
      String key = (String) iterator.next();
      String value = ((String[]) map.get(key))[0];
      array[i] = Utils.getNamesString(key, value);
    }
    interactionRequest.setFormParameters(array);
    //interactionRequest.setInteractionState();
    return interactionRequest;
  }


  private void fillMarkupRequest(WSRPBaseRequestAdapter markupRequest,
                                 PortletRequest portletRequest,
                                 PortletWindowSession portletWindowSession) {
    markupRequest.setCharacterEncodingSet(characterEncodings);
    markupRequest.setClientData(getClientData());
    markupRequest.setLocales(manageEnumeration(portletRequest.getLocales()));
    markupRequest.setMimeTypes(manageEnumeration(portletRequest.getResponseContentTypes()));
    markupRequest.setMode(WSRPConstants.WSRP_PREFIX + portletRequest.getPortletMode().toString());
    markupRequest.setModes(null);//TODO
    markupRequest.setUserAuthentication("none");
    markupRequest.setWindowState(WSRPConstants.WSRP_PREFIX + portletRequest.getWindowState().toString());
    markupRequest.setWindowStates(null);//TODO

    //specific to WSRP
    if (portletWindowSession.getPortletSession().getSessionContext() != null) {
      markupRequest.setSessionID(portletWindowSession.
          getPortletSession().getSessionContext().getSessionID());
    }

    markupRequest.setPortletInstanceKey(null);
  }

  private String getNavigationalState(PortletRequest portletRequest,
                                      PortletWindowSession portletWindowSession) {
    String ns = portletRequest.getParameter(WSRPConstants.WSRP_NAVIGATIONAL_STATE);
    if (ns != null) {
      log.debug("user navigational state : " + ns);
      portletWindowSession.setNavigationalState(ns);
    } else {
      log.debug("Navigational state null");
    }
    return portletWindowSession.getNavigationalState();
  }

  private ClientData getClientData() {
    ClientData clientData = new ClientData();
    clientData.setUserAgent(userAgent);
    return clientData;
  }

  private PortletWindowSession getWindowSession(PortletKey portletKey,
                                                WSRPPortlet portlet,
                                                UserSessionMgr userSession,
                                                String windowID) throws WSRPException {
    if (userSession != null) {
      log.debug("get group session form user session");
      String groupID = getPortletDescription(portlet).getGroupID();
      groupID = groupID == null ? "default" : groupID;
      log.debug("group ID : " + groupID);
      GroupSession groupSession = userSession.getGroupSession(groupID);
      if (groupSession != null) {
        log.debug("get portlet session from group session");
        org.exoplatform.services.wsrp.consumer.PortletSession portletSession = groupSession.
            getPortletSession(portletKey.getPortletHandle());
        log.debug("portlet handle : " + portletKey.getPortletHandle());
        if (portletSession != null) {
          PortletWindowSession windowSession = portletSession.getPortletWindowSession(windowID);
          log.debug("success in extraction of the window session");
          return windowSession;
        } else {
          log.error("portlet session was null");
          throw new WSRPException(Faults.INVALID_SESSION_FAULT);
        }
      } else {
        log.error("group session was null");
        throw new WSRPException(Faults.INVALID_SESSION_FAULT);
      }
    } else {
      log.error("user session was null");
      throw new WSRPException(Faults.INVALID_SESSION_FAULT);
    }
  }

  private UserSessionMgr getUserSession(PortletSession jsr168PortletSession,
                                        String producerID) throws WSRPException {

    UserSessionMgr userSession = (UserSessionMgr) jsr168PortletSession.
      getAttribute(USER_SESSIONS_KEY + producerID, PortletSession.PORTLET_SCOPE);
    if (userSession == null) {
      log.debug("Create new UserSession");
      userSession = new UserSessionImpl(getProducer(producerID).getMarkupInterfaceEndpoint());
      jsr168PortletSession.setAttribute(USER_SESSIONS_KEY + producerID,
          userSession, PortletSession.PORTLET_SCOPE);
    } else {
      log.debug("use existing UserSession");
    }
    return userSession;
  }

  private PortletDescription getPortletDescription(WSRPPortlet portlet)
      throws WSRPException {
    log.debug("getPortletDescription entered");
    String producerID = portlet.getPortletKey().getProducerId();
    Producer producer = getProducer(producerID);
    PortletDescription portletDesc = producer.getPortletDescription(portlet.getParent());
    if (portletDesc == null) {
      throw new WSRPException(Faults.UNKNOWN_PORTLET_DESCRIPTION);
    }
    return portletDesc;
  }

  private String[] manageEnumeration(Enumeration e) {
    List l = Collections.list(e);
    String[] array = new String[l.size()];
    int i = 0;
    for (Iterator iterator = l.iterator(); iterator.hasNext();) {
      Object o = iterator.next();
      array[i] = o.toString();
    }
    return array;
  }

  private Producer getProducer(String producerID) {
    log.debug("getProducer : " + producerID);
    Producer producer = consumerEnvironment.getProducerRegistry().getProducer(producerID);
    return producer;
  }

  private void updateSessionContext(SessionContext sessionContext,
                                    org.exoplatform.services.wsrp.consumer.PortletSession portletSession) {
    if (sessionContext != null) {
      log.debug("update session context");
      if (portletSession != null) {
        portletSession.setSessionContext(sessionContext);
      }
    }
  }

  private String processMarkupContext(MarkupContext markupContext,
                                      RenderResponse renderResponse)
      throws WSRPException {
    log.debug("process returned markup");
    String markup = null;
    byte[] binaryMarkup = null;
    if (markupContext != null && renderResponse != null) {
      String title = markupContext.getPreferredTitle();
      if (title != null) {
        log.debug("user title : " + title);
        renderResponse.setTitle(title);
      }
      markup = markupContext.getMarkupString();
      binaryMarkup = markupContext.getMarkupBinary();
      if (markup != null) {
        log.debug("markup non null");
        try {
          Writer w = renderResponse.getWriter();
          //PortletURL actionURL = renderResponse.createActionURL();
          //actionURL.setPortletMode(wsrpMode_);
          //w.write("<center><a href=\"" + actionURL.toString() + "\">WSRP</a><br><hr></center>");
          w.write(markup);
        } catch (IOException e) {
          log.error("can not write content to writer output", e);
          throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
        //} catch (PortletModeException e) {
        //  log.error("can not set wsrpMode_ = '" + wsrpMode_ + "' to actionURL.setPortletMode ", e);
        //  throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
        }
      }
      if(binaryMarkup != null){
        log.debug("binary markup non null");
        Reader reader = new InputStreamReader(new ByteArrayInputStream(binaryMarkup));
        char[] stringArray = new char[binaryMarkup.length];
        try {
          reader.read(stringArray);
          renderResponse.getWriter().write(stringArray);
        } catch (IOException e) {
          log.error("can not write content to writer output", e);
          throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
        }
      }
    }
    return markup;
  }
  
/*  
  private String contextPath;
  protected String defaultPage;
  private String ownerUri;
  public static String PARAM_PREFIX = "portlet:";
  
  private String getPortletBasedParamName(String shortName){
    return PARAM_PREFIX + contextPath + ":" + shortName;
  }

  private String getRequestURL(HttpServletRequest request) {
    String paramURL = getPortletBasedParamName("url");
    String result = request.getParameter(paramURL);
    result = result == null ? defaultPage : result;
    return result;
  }

  private String getPorletBaseURI(HttpServletRequest request) {
    String result = getRequestURL(request);
    if (result != null)
      if (result.indexOf('/') != -1) {
        result = result.substring(0, result.lastIndexOf('/'));
      }
    return result;
  }

  private String getPortalBaseURI() {
    String path = ownerUri;
    return path;
  }
*/
  
}
