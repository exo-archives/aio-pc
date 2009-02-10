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
package org.exoplatform.services.wsrp2.consumer.adapters.ports.v1;

import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp1.intf.WS1AccessDenied;
import org.exoplatform.services.wsrp1.intf.WS1InconsistentParameters;
import org.exoplatform.services.wsrp1.intf.WS1InvalidCookie;
import org.exoplatform.services.wsrp1.intf.WS1InvalidHandle;
import org.exoplatform.services.wsrp1.intf.WS1InvalidRegistration;
import org.exoplatform.services.wsrp1.intf.WS1InvalidSession;
import org.exoplatform.services.wsrp1.intf.WS1InvalidUserCategory;
import org.exoplatform.services.wsrp1.intf.WS1MissingParameters;
import org.exoplatform.services.wsrp1.intf.WS1OperationFailed;
import org.exoplatform.services.wsrp1.intf.WS1PortletStateChangeRequired;
import org.exoplatform.services.wsrp1.intf.WS1UnsupportedLocale;
import org.exoplatform.services.wsrp1.intf.WS1UnsupportedMimeType;
import org.exoplatform.services.wsrp1.intf.WS1UnsupportedMode;
import org.exoplatform.services.wsrp1.intf.WS1UnsupportedWindowState;
import org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType;
import org.exoplatform.services.wsrp1.type.WS1AccessDeniedFault;
import org.exoplatform.services.wsrp1.type.WS1InconsistentParametersFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidCookieFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidHandleFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidRegistrationFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidSessionFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidUserCategoryFault;
import org.exoplatform.services.wsrp1.type.WS1MissingParametersFault;
import org.exoplatform.services.wsrp1.type.WS1OperationFailedFault;
import org.exoplatform.services.wsrp1.type.WS1UnsupportedLocaleFault;
import org.exoplatform.services.wsrp1.type.WS1UnsupportedMimeTypeFault;
import org.exoplatform.services.wsrp1.type.WS1UnsupportedModeFault;
import org.exoplatform.services.wsrp1.type.WS1UnsupportedWindowStateFault;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPMarkupPortTypeAdapterAPI;
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
import org.exoplatform.services.wsrp2.type.AccessDeniedFault;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetResource;
import org.exoplatform.services.wsrp2.type.HandleEvents;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.InconsistentParametersFault;
import org.exoplatform.services.wsrp2.type.InitCookie;
import org.exoplatform.services.wsrp2.type.InvalidCookieFault;
import org.exoplatform.services.wsrp2.type.InvalidHandleFault;
import org.exoplatform.services.wsrp2.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp2.type.InvalidSessionFault;
import org.exoplatform.services.wsrp2.type.InvalidUserCategoryFault;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.MissingParametersFault;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.PerformBlockingInteraction;
import org.exoplatform.services.wsrp2.type.PortletStateChangeRequiredFault;
import org.exoplatform.services.wsrp2.type.ReleaseSessions;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.UnsupportedLocaleFault;
import org.exoplatform.services.wsrp2.type.UnsupportedMimeTypeFault;
import org.exoplatform.services.wsrp2.type.UnsupportedModeFault;
import org.exoplatform.services.wsrp2.type.UnsupportedWindowStateFault;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public class WSRPV1MarkupPortTypeAdapter implements WSRPMarkupPortTypeAdapterAPI {

  private WSRPV1MarkupPortType markupPort;

  private static final Log     LOG = ExoLogger.getLogger(WSRPV1MarkupPortTypeAdapter.class);

  public WSRPV1MarkupPortTypeAdapter(WSRPV1MarkupPortType markupPort) {
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
    
    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _getMarkup_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(getMarkup.getRegistrationContext());
    org.exoplatform.services.wsrp1.type.WS1PortletContext _getMarkup_portletContext = WSRPTypesTransformer.getWS1PortletContext(getMarkup.getPortletContext());
    org.exoplatform.services.wsrp1.type.WS1RuntimeContext _getMarkup_runtimeContext = WSRPTypesTransformer.getWS1RuntimeContext(getMarkup.getRuntimeContext());
    org.exoplatform.services.wsrp1.type.WS1UserContext _getMarkup_userContext = WSRPTypesTransformer.getWS1UserContext(getMarkup.getUserContext());
    org.exoplatform.services.wsrp1.type.WS1MarkupParams _getMarkup_markupParams = WSRPTypesTransformer.getWS1MarkupParams(getMarkup.getMarkupParams());
    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1MarkupContext> _getMarkup_markupContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1MarkupContext>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1SessionContext> _getMarkup_sessionContext = new javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1SessionContext>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> _getMarkup_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>>();
    
    try {
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

    } catch (WS1UnsupportedLocale ir) {
      throw new UnsupportedLocale(ir.getMessage(), new UnsupportedLocaleFault());
    } catch (WS1InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (WS1InvalidUserCategory ir) {
      throw new InvalidUserCategory(ir.getMessage(), new InvalidUserCategoryFault());
    } catch (WS1UnsupportedMimeType ir) {
      throw new UnsupportedMimeType(ir.getMessage(), new UnsupportedMimeTypeFault());
    } catch (WS1MissingParameters ir) {
      throw new MissingParameters(ir.getMessage(), new MissingParametersFault());
    } catch (WS1InvalidCookie ir) {
      throw new InvalidCookie(ir.getMessage(), new InvalidCookieFault());
    } catch (WS1AccessDenied ir) {
      throw new AccessDenied(ir.getMessage(), new AccessDeniedFault());
    } catch (WS1InvalidHandle ir) {
      throw new InvalidHandle(ir.getMessage(), new InvalidHandleFault());
    } catch (WS1UnsupportedMode mp) {
      throw new UnsupportedMode(mp.getMessage(), new UnsupportedModeFault());
    } catch (WS1InvalidSession ad) {
      throw new InvalidSession(ad.getMessage(), new InvalidSessionFault());
    } catch (WS1UnsupportedWindowState mp) {
      throw new UnsupportedWindowState(mp.getMessage(), new UnsupportedWindowStateFault());
    } catch (WS1InconsistentParameters ad) {
      throw new InconsistentParameters(ad.getMessage(), new InconsistentParametersFault());
    } catch (WS1OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    }

    MarkupResponse response = new MarkupResponse();
    response.setMarkupContext(WSRPTypesTransformer.getWS2MarkupContext(_getMarkup_markupContext.value));
    response.setSessionContext(WSRPTypesTransformer.getWS2SessionContext(_getMarkup_sessionContext.value));
    if (_getMarkup_extensions.value != null)
      response.getExtensions()
              .addAll(WSRPTypesTransformer.getWS2Extensions(_getMarkup_extensions.value));
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
    // wsrp1 doesn't have this operation
    return null;
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
    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _performBlockingInteraction_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(performBlockingInteraction.getRegistrationContext());
    org.exoplatform.services.wsrp1.type.WS1PortletContext _performBlockingInteraction_portletContext = WSRPTypesTransformer.getWS1PortletContext(performBlockingInteraction.getPortletContext());
    org.exoplatform.services.wsrp1.type.WS1RuntimeContext _performBlockingInteraction_runtimeContext = WSRPTypesTransformer.getWS1RuntimeContext(performBlockingInteraction.getRuntimeContext());
    org.exoplatform.services.wsrp1.type.WS1UserContext _performBlockingInteraction_userContext = WSRPTypesTransformer.getWS1UserContext(performBlockingInteraction.getUserContext());
    org.exoplatform.services.wsrp1.type.WS1MarkupParams _performBlockingInteraction_markupParams = WSRPTypesTransformer.getWS1MarkupParams(performBlockingInteraction.getMarkupParams());
    org.exoplatform.services.wsrp1.type.WS1InteractionParams _performBlockingInteraction_interactionParams = WSRPTypesTransformer.getWS1InteractionParams(performBlockingInteraction.getInteractionParams());
    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1UpdateResponse> _performBlockingInteraction_updateResponse = new javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1UpdateResponse>();
    javax.xml.ws.Holder<java.lang.String> _performBlockingInteraction_redirectURL = new javax.xml.ws.Holder<java.lang.String>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> _performBlockingInteraction_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>>();
    try {
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

    } catch (WS1UnsupportedLocale ir) {
      throw new UnsupportedLocale(ir.getMessage(), new UnsupportedLocaleFault());
    } catch (WS1InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (WS1InvalidUserCategory ir) {
      throw new InvalidUserCategory(ir.getMessage(), new InvalidUserCategoryFault());
    } catch (WS1UnsupportedMimeType ir) {
      throw new UnsupportedMimeType(ir.getMessage(), new UnsupportedMimeTypeFault());
    } catch (WS1MissingParameters ir) {
      throw new MissingParameters(ir.getMessage(), new MissingParametersFault());
    } catch (WS1InvalidCookie ir) {
      throw new InvalidCookie(ir.getMessage(), new InvalidCookieFault());
    } catch (WS1AccessDenied ir) {
      throw new AccessDenied(ir.getMessage(), new AccessDeniedFault());
    } catch (WS1InvalidHandle ir) {
      throw new InvalidHandle(ir.getMessage(), new InvalidHandleFault());
    } catch (WS1UnsupportedMode mp) {
      throw new UnsupportedMode(mp.getMessage(), new UnsupportedModeFault());
    } catch (WS1PortletStateChangeRequired ad) {
      throw new PortletStateChangeRequired(ad.getMessage(), new PortletStateChangeRequiredFault());
    } catch (WS1InvalidSession ad) {
      throw new InvalidSession(ad.getMessage(), new InvalidSessionFault());
    } catch (WS1UnsupportedWindowState mp) {
      throw new UnsupportedWindowState(mp.getMessage(), new UnsupportedWindowStateFault());
    } catch (WS1InconsistentParameters ad) {
      throw new InconsistentParameters(ad.getMessage(), new InconsistentParametersFault());
    } catch (WS1OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    }

    BlockingInteractionResponse response = new BlockingInteractionResponse();
    response.setUpdateResponse(WSRPTypesTransformer.getWS2UpdateResponse(_performBlockingInteraction_updateResponse.value));
    response.setRedirectURL(_performBlockingInteraction_redirectURL.value);
    if (_performBlockingInteraction_extensions.value != null)
      response.getExtensions()
              .addAll(WSRPTypesTransformer.getWS2Extensions(_performBlockingInteraction_extensions.value));
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
    // wsrp1 doesn't have this operation
    return null;
  }

  public List<Extension> initCookie(InitCookie initCookie) throws OperationNotSupported,
                                                          AccessDenied,
                                                          ResourceSuspended,
                                                          InvalidRegistration,
                                                          ModifyRegistrationRequired,
                                                          OperationFailed {

    if (LOG.isDebugEnabled())
      LOG.debug("Invoking initCookie...");
    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _initCookie_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(initCookie.getRegistrationContext());
    java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> _initCookie__return = null;

    try {
      _initCookie__return = markupPort.initCookie(_initCookie_registrationContext);
      if (LOG.isDebugEnabled())
        LOG.debug("initCookie.result=" + _initCookie__return);

    } catch (WS1InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (WS1AccessDenied ad) {
      throw new AccessDenied(ad.getMessage(), new AccessDeniedFault());
    } catch (WS1OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    }

    return WSRPTypesTransformer.getWS2Extensions(_initCookie__return); // in a WSRP2 we have t oreturn just one Extension
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
    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _releaseSessions_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(releaseSessions.getRegistrationContext());
    java.util.List<java.lang.String> _releaseSessions_sessionIDs = releaseSessions.getSessionIDs();
    java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> _releaseSessions__return = null;
    try {
      _releaseSessions__return = markupPort.releaseSessions(_releaseSessions_registrationContext,
                                                            _releaseSessions_sessionIDs);
      if (LOG.isDebugEnabled())
        LOG.debug("releaseSessions.result=" + _releaseSessions__return);

    } catch (WS1InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (WS1MissingParameters mp) {
      throw new MissingParameters(mp.getMessage(), new MissingParametersFault());
    } catch (WS1AccessDenied ad) {
      throw new AccessDenied(ad.getMessage(), new AccessDeniedFault());
    } catch (WS1OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    }

    return WSRPTypesTransformer.getWS2Extensions(_releaseSessions__return); // in a WSRP2 we have t oreturn just one Extension
  }

}
