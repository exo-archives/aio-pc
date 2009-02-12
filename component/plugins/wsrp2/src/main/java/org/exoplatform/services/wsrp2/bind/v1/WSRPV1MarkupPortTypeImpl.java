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

package org.exoplatform.services.wsrp2.bind.v1;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
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
import org.exoplatform.services.wsrp1.type.WS1Extension;
import org.exoplatform.services.wsrp1.type.WS1InconsistentParametersFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidCookieFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidHandleFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidRegistrationFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidSessionFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidUserCategoryFault;
import org.exoplatform.services.wsrp1.type.WS1MissingParametersFault;
import org.exoplatform.services.wsrp1.type.WS1OperationFailedFault;
import org.exoplatform.services.wsrp1.type.WS1PortletStateChangeRequiredFault;
import org.exoplatform.services.wsrp1.type.WS1UnsupportedLocaleFault;
import org.exoplatform.services.wsrp1.type.WS1UnsupportedMimeTypeFault;
import org.exoplatform.services.wsrp1.type.WS1UnsupportedModeFault;
import org.exoplatform.services.wsrp1.type.WS1UnsupportedWindowStateFault;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InconsistentParameters;
import org.exoplatform.services.wsrp2.intf.InvalidCookie;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.InvalidSession;
import org.exoplatform.services.wsrp2.intf.InvalidUserCategory;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.PortletStateChangeRequired;
import org.exoplatform.services.wsrp2.intf.UnsupportedLocale;
import org.exoplatform.services.wsrp2.intf.UnsupportedMimeType;
import org.exoplatform.services.wsrp2.intf.UnsupportedMode;
import org.exoplatform.services.wsrp2.intf.UnsupportedWindowState;
import org.exoplatform.services.wsrp2.producer.MarkupOperationsInterface;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.InteractionParams;
import org.exoplatform.services.wsrp2.type.MarkupParams;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 */

@javax.jws.WebService(name = "WSRPV1MarkupPortType", serviceName = "WSRPService1", portName = "WSRP_v1_Markup_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v1:wsdl", wsdlLocation = "/WEB-INF/wsdl1/wsrp_service.wsdl", endpointInterface = "org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType")
public class WSRPV1MarkupPortTypeImpl implements WSRPV1MarkupPortType, AbstractSingletonWebService {

  private MarkupOperationsInterface markupOperationsInterface;

  private final Log                 LOG = ExoLogger.getLogger(WSRPV1MarkupPortTypeImpl.class);

  public WSRPV1MarkupPortTypeImpl(MarkupOperationsInterface markupOperationsInterface) {
    this.markupOperationsInterface = markupOperationsInterface;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType#releaseSessions(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)java.util.List<java.lang.String>  sessionIDs )*
   */
  public java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> releaseSessions(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                                                                                          java.util.List<java.lang.String> sessionIDs) throws WS1InvalidRegistration,
                                                                                                                                      WS1MissingParameters,
                                                                                                                                      WS1AccessDenied,
                                                                                                                                      WS1OperationFailed {
    LOG.info("Executing operation releaseSessions");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(sessionIDs);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);

      ReturnAny returnAny = markupOperationsInterface.releaseSessions(ws2registrationContext,
                                                                      sessionIDs,
                                                                      null);

      java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> _return = new ArrayList<WS1Extension>();
      _return.addAll(WSRPTypesTransformer.getWS1Extensions(returnAny.getExtensions()));
      return _return;

    } catch (InvalidRegistration ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (MissingParameters mp) {
//      LOG.error(mp.getMessage(), mp);
      throw new WS1MissingParameters(mp.getMessage(), new WS1MissingParametersFault());
    } catch (AccessDenied ad) {
//      LOG.error(ad.getMessage(), ad);
      throw new WS1AccessDenied(ad.getMessage(), new WS1AccessDeniedFault());
    } catch (OperationFailed of) {
//      LOG.error(of.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
//      LOG.error(wsrpe.getMessage(), wsrpe);
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                   + "'on a PRODUCER side with exception at '"
                                   + wsrpe.getStackTrace()[0].toString() + "'",
                               new WS1OperationFailedFault(),
                               wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
         e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType#getMarkup(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp1.type.WS1PortletContext  portletContext ,)org.exoplatform.services.wsrp1.type.WS1RuntimeContext  runtimeContext ,)org.exoplatform.services.wsrp1.type.WS1UserContext  userContext ,)org.exoplatform.services.wsrp1.type.WS1MarkupParams  markupParams ,)org.exoplatform.services.wsrp1.type.WS1MarkupContext  markupContext ,)org.exoplatform.services.wsrp1.type.WS1SessionContext  sessionContext ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void getMarkup(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                        org.exoplatform.services.wsrp1.type.WS1PortletContext portletContext,
                        org.exoplatform.services.wsrp1.type.WS1RuntimeContext runtimeContext,
                        org.exoplatform.services.wsrp1.type.WS1UserContext userContext,
                        org.exoplatform.services.wsrp1.type.WS1MarkupParams markupParams,
                        javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1MarkupContext> markupContext,
                        javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1SessionContext> sessionContext,
                        javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1UnsupportedLocale,
                                                                                                                         WS1InvalidRegistration,
                                                                                                                         WS1InvalidUserCategory,
                                                                                                                         WS1UnsupportedMimeType,
                                                                                                                         WS1MissingParameters,
                                                                                                                         WS1InvalidCookie,
                                                                                                                         WS1AccessDenied,
                                                                                                                         WS1InvalidHandle,
                                                                                                                         WS1UnsupportedMode,
                                                                                                                         WS1InvalidSession,
                                                                                                                         WS1UnsupportedWindowState,
                                                                                                                         WS1InconsistentParameters,
                                                                                                                         WS1OperationFailed {

    LOG.info("Executing operation getMarkup");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(portletContext);
    if (LOG.isDebugEnabled())
      LOG.debug(runtimeContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    if (LOG.isDebugEnabled())
      LOG.debug(markupParams);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);
      PortletContext ws2portletContext = WSRPTypesTransformer.getWS2PortletContext(portletContext);
      RuntimeContext ws2runtimeContext = WSRPTypesTransformer.getWS2RuntimeContext(runtimeContext);
      UserContext ws2userContext = WSRPTypesTransformer.getWS2UserContext(userContext);
      MarkupParams ws2markupParams = WSRPTypesTransformer.getWS2MarkupParams(markupParams);

      WSRPHTTPContainer.getInstance().setVersion(1);

      MarkupResponse markupResponse = markupOperationsInterface.getMarkup(ws2registrationContext,
                                                                          ws2portletContext,
                                                                          ws2runtimeContext,
                                                                          ws2userContext,
                                                                          ws2markupParams);

      markupContext.value = WSRPTypesTransformer.getWS1MarkupContext(markupResponse.getMarkupContext());

      sessionContext.value = WSRPTypesTransformer.getWS1SessionContext(markupResponse.getSessionContext());
      extensions.value = WSRPTypesTransformer.getWS1Extensions(markupResponse.getExtensions());

    } catch (UnsupportedLocale ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1UnsupportedLocale(ir.getMessage(), new WS1UnsupportedLocaleFault());
    } catch (InvalidRegistration ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (InvalidUserCategory ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidUserCategory(ir.getMessage(), new WS1InvalidUserCategoryFault());
    } catch (UnsupportedMimeType ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1UnsupportedMimeType(ir.getMessage(), new WS1UnsupportedMimeTypeFault());
    } catch (MissingParameters ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (InvalidCookie ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidCookie(ir.getMessage(), new WS1InvalidCookieFault());
    } catch (AccessDenied ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1AccessDenied(ir.getMessage(), new WS1AccessDeniedFault());
    } catch (InvalidHandle ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidHandle(ir.getMessage(), new WS1InvalidHandleFault());
    } catch (UnsupportedMode mp) {
//      LOG.error(mp.getMessage(), mp);
      throw new WS1UnsupportedMode(mp.getMessage(), new WS1UnsupportedModeFault());
    } catch (InvalidSession ad) {
//      LOG.error(ad.getMessage(), ad);
      throw new WS1InvalidSession(ad.getMessage(), new WS1InvalidSessionFault());
    } catch (UnsupportedWindowState mp) {
//      LOG.error(mp.getMessage(), mp);
      throw new WS1UnsupportedWindowState(mp.getMessage(), new WS1UnsupportedWindowStateFault());
    } catch (InconsistentParameters ad) {
//      LOG.error(ad.getMessage(), ad);
      throw new WS1InconsistentParameters(ad.getMessage(), new WS1InconsistentParametersFault());
    } catch (OperationFailed of) {
      System.out.println(">>> EXOMAN WSRPV1MarkupPortTypeImpl.getMarkup() of = " + of);
      of.printStackTrace();
//      LOG.error(of.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + wsrpe.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
         e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType#initCookie(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext )*
   */
  public java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> initCookie(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext) throws WS1InvalidRegistration,
                                                                                                                                                                    WS1AccessDenied,
                                                                                                                                                                    WS1OperationFailed {
    LOG.info("Executing operation initCookie");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);

      ReturnAny returnAny = markupOperationsInterface.initCookie(ws2registrationContext, null);

      java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> _return = new ArrayList<WS1Extension>();
      _return.addAll(WSRPTypesTransformer.getWS1Extensions(returnAny.getExtensions())); // in a wsrp1 we have to return a list of Extensions
      return _return;

    } catch (InvalidRegistration ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (AccessDenied ad) {
//      LOG.error(ad.getMessage(), ad);
      throw new WS1AccessDenied(ad.getMessage(), new WS1AccessDeniedFault());
    } catch (OperationFailed of) {
//      LOG.error(of.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                   + "'on a PRODUCER side with exception at '"
                                   + wsrpe.getStackTrace()[0].toString() + "'",
                               new WS1OperationFailedFault(),
                               wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
         e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType#performBlockingInteraction(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp1.type.WS1PortletContext  portletContext ,)org.exoplatform.services.wsrp1.type.WS1RuntimeContext  runtimeContext ,)org.exoplatform.services.wsrp1.type.WS1UserContext  userContext ,)org.exoplatform.services.wsrp1.type.WS1MarkupParams  markupParams ,)org.exoplatform.services.wsrp1.type.WS1InteractionParams  interactionParams ,)org.exoplatform.services.wsrp1.type.WS1UpdateResponse  updateResponse ,)java.lang.String  redirectURL ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void performBlockingInteraction(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                                         org.exoplatform.services.wsrp1.type.WS1PortletContext portletContext,
                                         org.exoplatform.services.wsrp1.type.WS1RuntimeContext runtimeContext,
                                         org.exoplatform.services.wsrp1.type.WS1UserContext userContext,
                                         org.exoplatform.services.wsrp1.type.WS1MarkupParams markupParams,
                                         org.exoplatform.services.wsrp1.type.WS1InteractionParams interactionParams,
                                         javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1UpdateResponse> updateResponse,
                                         javax.xml.ws.Holder<java.lang.String> redirectURL,
                                         javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1UnsupportedLocale,
                                                                                                                                          WS1InvalidRegistration,
                                                                                                                                          WS1InvalidUserCategory,
                                                                                                                                          WS1UnsupportedMimeType,
                                                                                                                                          WS1MissingParameters,
                                                                                                                                          WS1InvalidCookie,
                                                                                                                                          WS1AccessDenied,
                                                                                                                                          WS1InvalidHandle,
                                                                                                                                          WS1UnsupportedMode,
                                                                                                                                          WS1PortletStateChangeRequired,
                                                                                                                                          WS1InvalidSession,
                                                                                                                                          WS1UnsupportedWindowState,
                                                                                                                                          WS1InconsistentParameters,
                                                                                                                                          WS1OperationFailed {
    LOG.info("Executing operation performBlockingInteraction");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(portletContext);
    if (LOG.isDebugEnabled())
      LOG.debug(runtimeContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    if (LOG.isDebugEnabled())
      LOG.debug(markupParams);
    if (LOG.isDebugEnabled())
      LOG.debug(interactionParams);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);
      PortletContext ws2portletContext = WSRPTypesTransformer.getWS2PortletContext(portletContext);
      RuntimeContext ws2runtimeContext = WSRPTypesTransformer.getWS2RuntimeContext(runtimeContext);
      UserContext ws2userContext = WSRPTypesTransformer.getWS2UserContext(userContext);
      MarkupParams ws2markupParams = WSRPTypesTransformer.getWS2MarkupParams(markupParams);
      InteractionParams ws2interactionParams = WSRPTypesTransformer.getWS2InteractionParams(interactionParams);

      WSRPHTTPContainer.getInstance().setVersion(1);

      BlockingInteractionResponse blockingInteractionResponse = markupOperationsInterface.performBlockingInteraction(ws2registrationContext,
                                                                                                                     ws2portletContext,
                                                                                                                     ws2runtimeContext,
                                                                                                                     ws2userContext,
                                                                                                                     ws2markupParams,
                                                                                                                     ws2interactionParams);

      updateResponse.value = WSRPTypesTransformer.getWS1UpdateResponse(blockingInteractionResponse.getUpdateResponse());
      redirectURL.value = blockingInteractionResponse.getRedirectURL();
      extensions.value = WSRPTypesTransformer.getWS1Extensions(blockingInteractionResponse.getExtensions());

    } catch (UnsupportedLocale ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1UnsupportedLocale(ir.getMessage(), new WS1UnsupportedLocaleFault());
    } catch (InvalidRegistration ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (InvalidUserCategory ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidUserCategory(ir.getMessage(), new WS1InvalidUserCategoryFault());
    } catch (UnsupportedMimeType ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1UnsupportedMimeType(ir.getMessage(), new WS1UnsupportedMimeTypeFault());
    } catch (MissingParameters ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (InvalidCookie ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidCookie(ir.getMessage(), new WS1InvalidCookieFault());
    } catch (AccessDenied ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1AccessDenied(ir.getMessage(), new WS1AccessDeniedFault());
    } catch (InvalidHandle ir) {
//      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidHandle(ir.getMessage(), new WS1InvalidHandleFault());
    } catch (UnsupportedMode mp) {
//      LOG.error(mp.getMessage(), mp);
      throw new WS1UnsupportedMode(mp.getMessage(), new WS1UnsupportedModeFault());
    } catch (PortletStateChangeRequired ad) {
//      LOG.error(ad.getMessage(), ad);
      throw new WS1PortletStateChangeRequired(ad.getMessage(),
                                              new WS1PortletStateChangeRequiredFault());
    } catch (InvalidSession ad) {
//      LOG.error(ad.getMessage(), ad);
      throw new WS1InvalidSession(ad.getMessage(), new WS1InvalidSessionFault());
    } catch (UnsupportedWindowState mp) {
//      LOG.error(mp.getMessage(), mp);
      throw new WS1UnsupportedWindowState(mp.getMessage(), new WS1UnsupportedWindowStateFault());
    } catch (InconsistentParameters ad) {
//      LOG.error(ad.getMessage(), ad);
      throw new WS1InconsistentParameters(ad.getMessage(), new WS1InconsistentParametersFault());
    } catch (OperationFailed of) {
//      LOG.error(of.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
//      LOG.error(wsrpe.getMessage(), wsrpe);
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                   + "'on a PRODUCER side with exception at '"
                                   + wsrpe.getStackTrace()[0].toString() + "'",
                               new WS1OperationFailedFault(),
                               wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
         e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }

  }

}
