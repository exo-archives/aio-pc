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
package org.exoplatform.services.wsrp2.consumer.adapters.ports1;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp.WSRPTypesTransformer;
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
public class WSRPV1MarkupPortTypeAdapter {

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

    System.out.println("Invoking getMarkup...");
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

      System.out.println("getMarkup._getMarkup_markupContext=" + _getMarkup_markupContext.value);
      System.out.println("getMarkup._getMarkup_sessionContext=" + _getMarkup_sessionContext.value);
      System.out.println("getMarkup._getMarkup_extensions=" + _getMarkup_extensions.value);
    } catch (WS1UnsupportedLocale e) {
      System.out.println("Expected exception: UnsupportedLocale has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidRegistration e) {
      System.out.println("Expected exception: InvalidRegistration has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidUserCategory e) {
      System.out.println("Expected exception: InvalidUserCategory has occurred.");
      System.out.println(e.toString());
    } catch (WS1UnsupportedMimeType e) {
      System.out.println("Expected exception: UnsupportedMimeType has occurred.");
      System.out.println(e.toString());
    } catch (WS1MissingParameters e) {
      System.out.println("Expected exception: MissingParameters has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidCookie e) {
      System.out.println("Expected exception: InvalidCookie has occurred.");
      System.out.println(e.toString());
    } catch (WS1AccessDenied e) {
      System.out.println("Expected exception: AccessDenied has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidHandle e) {
      System.out.println("Expected exception: InvalidHandle has occurred.");
      System.out.println(e.toString());
    } catch (WS1UnsupportedMode e) {
      System.out.println("Expected exception: UnsupportedMode has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidSession e) {
      System.out.println("Expected exception: InvalidSession has occurred.");
      System.out.println(e.toString());
    } catch (WS1UnsupportedWindowState e) {
      System.out.println("Expected exception: UnsupportedWindowState has occurred.");
      System.out.println(e.toString());
    } catch (WS1InconsistentParameters e) {
      System.out.println("Expected exception: InconsistentParameters has occurred.");
      System.out.println(e.toString());
    } catch (WS1OperationFailed e) {
      System.out.println("Expected exception: OperationFailed has occurred.");
      System.out.println(e.toString());
    }

    System.out.println("getMarkup._getMarkup_markupContext=" + _getMarkup_markupContext.value);
    System.out.println("getMarkup._getMarkup_sessionContext=" + _getMarkup_sessionContext.value);
    System.out.println("getMarkup._getMarkup_extensions=" + _getMarkup_extensions.value);

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
    System.out.println("Invoking getResource...");
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

    System.out.println("Invoking performBlockingInteraction...");
    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _performBlockingInteraction_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(performBlockingInteraction.getRegistrationContext());
    ;
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

      System.out.println("performBlockingInteraction._performBlockingInteraction_updateResponse="
          + _performBlockingInteraction_updateResponse.value);
      System.out.println("performBlockingInteraction._performBlockingInteraction_redirectURL="
          + _performBlockingInteraction_redirectURL.value);
      System.out.println("performBlockingInteraction._performBlockingInteraction_extensions="
          + _performBlockingInteraction_extensions.value);
    } catch (WS1UnsupportedLocale e) {
      System.out.println("Expected exception: UnsupportedLocale has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidRegistration e) {
      System.out.println("Expected exception: InvalidRegistration has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidUserCategory e) {
      System.out.println("Expected exception: InvalidUserCategory has occurred.");
      System.out.println(e.toString());
    } catch (WS1UnsupportedMimeType e) {
      System.out.println("Expected exception: UnsupportedMimeType has occurred.");
      System.out.println(e.toString());
    } catch (WS1MissingParameters e) {
      System.out.println("Expected exception: MissingParameters has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidCookie e) {
      System.out.println("Expected exception: InvalidCookie has occurred.");
      System.out.println(e.toString());
    } catch (WS1AccessDenied e) {
      System.out.println("Expected exception: AccessDenied has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidHandle e) {
      System.out.println("Expected exception: InvalidHandle has occurred.");
      System.out.println(e.toString());
    } catch (WS1UnsupportedMode e) {
      System.out.println("Expected exception: UnsupportedMode has occurred.");
      System.out.println(e.toString());
    } catch (WS1PortletStateChangeRequired e) {
      System.out.println("Expected exception: PortletStateChangeRequired has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidSession e) {
      System.out.println("Expected exception: InvalidSession has occurred.");
      System.out.println(e.toString());
    } catch (WS1UnsupportedWindowState e) {
      System.out.println("Expected exception: UnsupportedWindowState has occurred.");
      System.out.println(e.toString());
    } catch (WS1InconsistentParameters e) {
      System.out.println("Expected exception: InconsistentParameters has occurred.");
      System.out.println(e.toString());
    } catch (WS1OperationFailed e) {
      System.out.println("Expected exception: OperationFailed has occurred.");
      System.out.println(e.toString());
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

    System.out.println("Invoking handleEvents...");
    // wsrp1 doesn't have this operation
    return null;
  }

  public Extension initCookie(InitCookie initCookie) throws OperationNotSupported,
                                                    AccessDenied,
                                                    ResourceSuspended,
                                                    InvalidRegistration,
                                                    ModifyRegistrationRequired,
                                                    OperationFailed {

    System.out.println("Invoking initCookie...");
    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _initCookie_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(initCookie.getRegistrationContext());
    java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> _initCookie__return = null;

    try {
      _initCookie__return = markupPort.initCookie(_initCookie_registrationContext);
      System.out.println("initCookie.result=" + _initCookie__return);

    } catch (WS1InvalidRegistration e) {
      System.out.println("Expected exception: InvalidRegistration has occurred.");
      System.out.println(e.toString());
    } catch (WS1AccessDenied e) {
      System.out.println("Expected exception: AccessDenied has occurred.");
      System.out.println(e.toString());
    } catch (WS1OperationFailed e) {
      System.out.println("Expected exception: OperationFailed has occurred.");
      System.out.println(e.toString());
    }

    return WSRPTypesTransformer.getWS2Extensions(_initCookie__return).get(0); // in a WSRP2 we have t oreturn just one Extension
  }

  public Extension releaseSessions(ReleaseSessions releaseSessions) throws OperationNotSupported,
                                                                   AccessDenied,
                                                                   ResourceSuspended,
                                                                   InvalidRegistration,
                                                                   ModifyRegistrationRequired,
                                                                   MissingParameters,
                                                                   OperationFailed {

    System.out.println("Invoking releaseSessions...");
    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _releaseSessions_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(releaseSessions.getRegistrationContext());
    java.util.List<java.lang.String> _releaseSessions_sessionIDs = releaseSessions.getSessionIDs();
    java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> _releaseSessions__return = null;
    try {
      _releaseSessions__return = markupPort.releaseSessions(_releaseSessions_registrationContext,
                                                            _releaseSessions_sessionIDs);
      System.out.println("releaseSessions.result=" + _releaseSessions__return);

    } catch (WS1InvalidRegistration e) {
      System.out.println("Expected exception: InvalidRegistration has occurred.");
      System.out.println(e.toString());
    } catch (WS1MissingParameters e) {
      System.out.println("Expected exception: MissingParameters has occurred.");
      System.out.println(e.toString());
    } catch (WS1AccessDenied e) {
      System.out.println("Expected exception: AccessDenied has occurred.");
      System.out.println(e.toString());
    } catch (WS1OperationFailed e) {
      System.out.println("Expected exception: OperationFailed has occurred.");
      System.out.println(e.toString());
    }

    return WSRPTypesTransformer.getWS2Extensions(_releaseSessions__return).get(0); // in a WSRP2 we have t oreturn just one Extension
  }

}
