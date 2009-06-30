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
package org.exoplatform.services.wsrp2.consumer.adapters.ports.v2;

import java.util.List;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPMarkupPortTypeAdapterAPI;
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
import org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetResource;
import org.exoplatform.services.wsrp2.type.HandleEvents;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.InitCookie;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.PerformBlockingInteraction;
import org.exoplatform.services.wsrp2.type.ReleaseSessions;
import org.exoplatform.services.wsrp2.type.ResourceResponse;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public class WSRPV2MarkupPortTypeAdapter implements WSRPMarkupPortTypeAdapterAPI {

  private WSRPV2MarkupPortType markupPort;

  private static final Log     LOG = ExoLogger.getLogger(WSRPV2MarkupPortTypeAdapter.class);

  public WSRPV2MarkupPortTypeAdapter(WSRPV2MarkupPortType markupPort) {
    this.markupPort = markupPort;
  }

  public MarkupResponse getMarkup(GetMarkup getMarkup) throws AccessDenied,
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
                                                      UnsupportedLocale {
    if (LOG.isDebugEnabled())
      LOG.debug("Invoking getMarkup...");

    org.exoplatform.services.wsrp2.type.RegistrationContext _getMarkup_registrationContext = getMarkup.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.PortletContext _getMarkup_portletContext = getMarkup.getPortletContext();
    org.exoplatform.services.wsrp2.type.RuntimeContext _getMarkup_runtimeContext = getMarkup.getRuntimeContext();
    org.exoplatform.services.wsrp2.type.UserContext _getMarkup_userContext = getMarkup.getUserContext();
    org.exoplatform.services.wsrp2.type.MarkupParams _getMarkup_markupParams = getMarkup.getMarkupParams();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.MarkupContext> _getMarkup_markupContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.MarkupContext>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.SessionContext> _getMarkup_sessionContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.SessionContext>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _getMarkup_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    markupPort.getMarkup(_getMarkup_registrationContext,
                         _getMarkup_portletContext,
                         _getMarkup_runtimeContext,
                         _getMarkup_userContext,
                         _getMarkup_markupParams,
                         _getMarkup_markupContext,
                         _getMarkup_sessionContext,
                         _getMarkup_extensions);

    if (LOG.isDebugEnabled())
      LOG.debug("getMarkup._getMarkup_markupContext=" + _getMarkup_markupContext.value);
    if (LOG.isDebugEnabled())
      LOG.debug("getMarkup._getMarkup_sessionContext=" + _getMarkup_sessionContext.value);
    if (LOG.isDebugEnabled())
      LOG.debug("getMarkup._getMarkup_extensions=" + _getMarkup_extensions.value);

    MarkupResponse response = new MarkupResponse();
    response.setMarkupContext(_getMarkup_markupContext.value);
    response.setSessionContext(_getMarkup_sessionContext.value);
    if (_getMarkup_extensions.value != null)
      response.getExtensions().addAll(_getMarkup_extensions.value);
    return response;

  }

  public ResourceResponse getResource(GetResource getResource) throws OperationNotSupported,
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
                                                              UnsupportedLocale {
    if (LOG.isDebugEnabled())
      LOG.debug("Invoking getResource...");

    org.exoplatform.services.wsrp2.type.RegistrationContext _getResource_registrationContext = getResource.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.PortletContext _getResource_portletContextVal = getResource.getPortletContext();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.PortletContext> _getResource_portletContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.PortletContext>(_getResource_portletContextVal);
    org.exoplatform.services.wsrp2.type.RuntimeContext _getResource_runtimeContext = getResource.getRuntimeContext();
    org.exoplatform.services.wsrp2.type.UserContext _getResource_userContext = getResource.getUserContext();
    org.exoplatform.services.wsrp2.type.ResourceParams _getResource_resourceParams = getResource.getResourceParams();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceContext> _getResource_resourceContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceContext>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.SessionContext> _getResource_sessionContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.SessionContext>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _getResource_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    markupPort.getResource(_getResource_registrationContext,
                           _getResource_portletContext,
                           _getResource_runtimeContext,
                           _getResource_userContext,
                           _getResource_resourceParams,
                           _getResource_resourceContext,
                           _getResource_sessionContext,
                           _getResource_extensions);

    if (LOG.isDebugEnabled())
      LOG.debug("getResource._getResource_portletContext="
        + _getResource_portletContext.value);
    if (LOG.isDebugEnabled())
      LOG.debug("getResource._getResource_resourceContext="
        + _getResource_resourceContext.value);
    if (LOG.isDebugEnabled())
      LOG.debug("getResource._getResource_sessionContext="
        + _getResource_sessionContext.value);
    if (LOG.isDebugEnabled())
      LOG.debug("getResource._getResource_extensions=" + _getResource_extensions.value);

    ResourceResponse response = new ResourceResponse();
    response.setPortletContext(_getResource_portletContext.value);
    response.setResourceContext(_getResource_resourceContext.value);
    response.setSessionContext(_getResource_sessionContext.value);
    if (_getResource_extensions.value != null)
      response.getExtensions().addAll(_getResource_extensions.value);
    return response;

  }

  public BlockingInteractionResponse performBlockingInteraction(PerformBlockingInteraction performBlockingInteraction) throws AccessDenied,
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
                                                                                                                      PortletStateChangeRequired {

    if (LOG.isDebugEnabled())
      LOG.debug("Invoking performBlockingInteraction...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _performBlockingInteraction_registrationContext = performBlockingInteraction.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.PortletContext _performBlockingInteraction_portletContext = performBlockingInteraction.getPortletContext();
    org.exoplatform.services.wsrp2.type.RuntimeContext _performBlockingInteraction_runtimeContext = performBlockingInteraction.getRuntimeContext();
    org.exoplatform.services.wsrp2.type.UserContext _performBlockingInteraction_userContext = performBlockingInteraction.getUserContext();
    org.exoplatform.services.wsrp2.type.MarkupParams _performBlockingInteraction_markupParams = performBlockingInteraction.getMarkupParams();
    org.exoplatform.services.wsrp2.type.InteractionParams _performBlockingInteraction_interactionParams = performBlockingInteraction.getInteractionParams();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.UpdateResponse> _performBlockingInteraction_updateResponse = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.UpdateResponse>();
    javax.xml.ws.Holder<java.lang.String> _performBlockingInteraction_redirectURL = new javax.xml.ws.Holder<java.lang.String>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _performBlockingInteraction_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    markupPort.performBlockingInteraction(_performBlockingInteraction_registrationContext,
                                          _performBlockingInteraction_portletContext,
                                          _performBlockingInteraction_runtimeContext,
                                          _performBlockingInteraction_userContext,
                                          _performBlockingInteraction_markupParams,
                                          _performBlockingInteraction_interactionParams,
                                          _performBlockingInteraction_updateResponse,
                                          _performBlockingInteraction_redirectURL,
                                          _performBlockingInteraction_extensions);

    if (LOG.isDebugEnabled())
      LOG.debug("performBlockingInteraction._performBlockingInteraction_updateResponse="
        + _performBlockingInteraction_updateResponse.value);
    if (LOG.isDebugEnabled())
      LOG.debug("performBlockingInteraction._performBlockingInteraction_redirectURL="
        + _performBlockingInteraction_redirectURL.value);
    if (LOG.isDebugEnabled())
      LOG.debug("performBlockingInteraction._performBlockingInteraction_extensions="
        + _performBlockingInteraction_extensions.value);

    BlockingInteractionResponse response = new BlockingInteractionResponse();
    response.setUpdateResponse(_performBlockingInteraction_updateResponse.value);
    response.setRedirectURL(_performBlockingInteraction_redirectURL.value);
    if (_performBlockingInteraction_extensions.value != null)
      response.getExtensions().addAll(_performBlockingInteraction_extensions.value);
    return response;

  }

  public HandleEventsResponse handleEvents(HandleEvents handleEvents) throws OperationNotSupported,
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
                                                                     PortletStateChangeRequired {

    if (LOG.isDebugEnabled())
      LOG.debug("Invoking handleEvents...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _handleEvents_registrationContext = handleEvents.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.PortletContext _handleEvents_portletContext = handleEvents.getPortletContext();
    org.exoplatform.services.wsrp2.type.RuntimeContext _handleEvents_runtimeContext = handleEvents.getRuntimeContext();
    org.exoplatform.services.wsrp2.type.UserContext _handleEvents_userContext = handleEvents.getUserContext();
    org.exoplatform.services.wsrp2.type.MarkupParams _handleEvents_markupParams = handleEvents.getMarkupParams();
    org.exoplatform.services.wsrp2.type.EventParams _handleEvents_eventParams = handleEvents.getEventParams();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.UpdateResponse> _handleEvents_updateResponse = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.UpdateResponse>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.HandleEventsFailed>> _handleEvents_failedEvents = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.HandleEventsFailed>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _handleEvents_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    markupPort.handleEvents(_handleEvents_registrationContext,
                            _handleEvents_portletContext,
                            _handleEvents_runtimeContext,
                            _handleEvents_userContext,
                            _handleEvents_markupParams,
                            _handleEvents_eventParams,
                            _handleEvents_updateResponse,
                            _handleEvents_failedEvents,
                            _handleEvents_extensions);

    if (LOG.isDebugEnabled())
      LOG.debug("handleEvents._handleEvents_updateResponse="
        + _handleEvents_updateResponse.value);
    if (LOG.isDebugEnabled())
      LOG.debug("handleEvents._handleEvents_failedEvents="
        + _handleEvents_failedEvents.value);
    if (LOG.isDebugEnabled())
      LOG.debug("handleEvents._handleEvents_extensions=" + _handleEvents_extensions.value);

    HandleEventsResponse response = new HandleEventsResponse();
    response.setUpdateResponse(_handleEvents_updateResponse.value);
    if (_handleEvents_failedEvents.value != null)
      response.getFailedEvents().addAll(_handleEvents_failedEvents.value);
    if (_handleEvents_extensions.value != null)
      response.getExtensions().addAll(_handleEvents_extensions.value);
    return response;

  }

  public List<Extension> initCookie(InitCookie initCookie) throws OperationNotSupported,
                                                    AccessDenied,
                                                    ResourceSuspended,
                                                    InvalidRegistration,
                                                    ModifyRegistrationRequired,
                                                    OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Invoking initCookie...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _initCookie_registrationContext = initCookie.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.UserContext _initCookie_userContext = initCookie.getUserContext();

    List<org.exoplatform.services.wsrp2.type.Extension> _initCookie__return = markupPort.initCookie(_initCookie_registrationContext,
                                                                                              _initCookie_userContext);
    if (LOG.isDebugEnabled())
      LOG.debug("initCookie.result=" + _initCookie__return);

    return _initCookie__return;
  }

  public List<Extension> releaseSessions(ReleaseSessions releaseSessions) throws OperationNotSupported,
                                                                   AccessDenied,
                                                                   ResourceSuspended,
                                                                   InvalidRegistration,
                                                                   ModifyRegistrationRequired,
                                                                   MissingParameters,
                                                                   OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Invoking releaseSessions...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _releaseSessions_registrationContext = releaseSessions.getRegistrationContext();
    java.util.List<java.lang.String> _releaseSessions_sessionIDs = releaseSessions.getSessionIDs();
    org.exoplatform.services.wsrp2.type.UserContext _releaseSessions_userContext = releaseSessions.getUserContext();

    List<org.exoplatform.services.wsrp2.type.Extension> _releaseSessions__return = markupPort.releaseSessions(_releaseSessions_registrationContext,
                                                                                                        _releaseSessions_sessionIDs,
                                                                                                        _releaseSessions_userContext);
    if (LOG.isDebugEnabled())
      LOG.debug("releaseSessions.result=" + _releaseSessions__return);

    return _releaseSessions__return;
  }

}
