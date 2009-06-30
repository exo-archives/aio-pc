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

package org.exoplatform.services.wsrp2.bind.v2;

import java.util.List;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
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
import org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType;
import org.exoplatform.services.wsrp2.producer.MarkupOperationsInterface;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.type.AccessDeniedFault;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.InconsistentParametersFault;
import org.exoplatform.services.wsrp2.type.InvalidCookieFault;
import org.exoplatform.services.wsrp2.type.InvalidHandleFault;
import org.exoplatform.services.wsrp2.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp2.type.InvalidSessionFault;
import org.exoplatform.services.wsrp2.type.InvalidUserCategoryFault;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.MissingParametersFault;
import org.exoplatform.services.wsrp2.type.ModifyRegistrationRequiredFault;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.PortletStateChangeRequiredFault;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.ResourceSuspendedFault;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.UnsupportedLocaleFault;
import org.exoplatform.services.wsrp2.type.UnsupportedMimeTypeFault;
import org.exoplatform.services.wsrp2.type.UnsupportedModeFault;
import org.exoplatform.services.wsrp2.type.UnsupportedWindowStateFault;

/**
 * 
 */

@javax.jws.WebService(name = "WSRPV2MarkupPortType", serviceName = "WSRPService2", portName = "WSRP_v2_Markup_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v2:wsdl", wsdlLocation = "/WEB-INF/wsdl2/wsrp-service.wsdl", endpointInterface = "org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType")
public class WSRPV2MarkupPortTypeImpl implements WSRPV2MarkupPortType, AbstractSingletonWebService {

  private static final Log          LOG = ExoLogger.getLogger(WSRPV2MarkupPortTypeImpl.class.getName());

  private MarkupOperationsInterface markupOperationsInterface;

  public WSRPV2MarkupPortTypeImpl(MarkupOperationsInterface markupOperationsInterface) {
    this.markupOperationsInterface = markupOperationsInterface;
  }

  /**
   * @see 
   *      org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#getResource(org
   *      .exoplatform.services.wsrp2.type.RegistrationContext
   *      registrationContext
   *      ,)org.exoplatform.services.wsrp2.type.PortletContext portletContext
   *      ,)org.exoplatform.services.wsrp2.type.RuntimeContext runtimeContext
   *      ,)org.exoplatform.services.wsrp2.type.UserContext userContext
   *      ,)org.exoplatform.services.wsrp2.type.ResourceParams resourceParams
   *      ,)org.exoplatform.services.wsrp2.type.ResourceContext resourceContext
   *      ,)org.exoplatform.services.wsrp2.type.SessionContext sessionContext
   *      ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>
   *      extensions )*
   */
  public void getResource(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                          javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.PortletContext> portletContext,
                          org.exoplatform.services.wsrp2.type.RuntimeContext runtimeContext,
                          org.exoplatform.services.wsrp2.type.UserContext userContext,
                          org.exoplatform.services.wsrp2.type.ResourceParams resourceParams,
                          javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceContext> resourceContext,
                          javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.SessionContext> sessionContext,
                          javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
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
      LOG.debug("Executing operation getResource");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(portletContext.value);
    if (LOG.isDebugEnabled())
      LOG.debug(runtimeContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    if (LOG.isDebugEnabled())
      LOG.debug(resourceParams);
    try {
      WSRPHTTPContainer.getInstance().setVersion(2);
      ResourceResponse response = markupOperationsInterface.getResource(registrationContext,
                                                                        portletContext.value,
                                                                        runtimeContext,
                                                                        userContext,
                                                                        resourceParams);
      org.exoplatform.services.wsrp2.type.ResourceContext resourceContextValue = response.getResourceContext();
      resourceContext.value = resourceContextValue;
      org.exoplatform.services.wsrp2.type.SessionContext sessionContextValue = response.getSessionContext();
      sessionContext.value = sessionContextValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (AccessDenied ad) {
      throw new AccessDenied(ad.getMessage(), new AccessDeniedFault());
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage(), new ResourceSuspendedFault());
    } catch (UnsupportedMimeType um) {
      throw new UnsupportedMimeType(um.getMessage(), new UnsupportedMimeTypeFault());
    } catch (InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (InvalidHandle ih) {
      throw new InvalidHandle(ih.getMessage(), new InvalidHandleFault());
    } catch (InvalidCookie ic) {
      throw new InvalidCookie(ic.getMessage(), new InvalidCookieFault());
    } catch (UnsupportedWindowState uws) {
      throw new UnsupportedWindowState(uws.getMessage(), new UnsupportedWindowStateFault());
    } catch (InvalidUserCategory iuc) {
      throw new InvalidUserCategory(iuc.getMessage(), new InvalidUserCategoryFault());
    } catch (UnsupportedMode unm) {
      throw new UnsupportedMode(unm.getMessage(), new UnsupportedModeFault());
    } catch (ModifyRegistrationRequired mrr) {
      throw new ModifyRegistrationRequired(mrr.getMessage(), new ModifyRegistrationRequiredFault());
    } catch (InvalidSession is) {
      throw new InvalidSession(is.getMessage(), new InvalidSessionFault());
    } catch (MissingParameters mp) {
      throw new MissingParameters(mp.getMessage(), new MissingParametersFault());
    } catch (InconsistentParameters icp) {
      throw new InconsistentParameters(icp.getMessage(), new InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    } catch (UnsupportedLocale ul) {
      throw new UnsupportedLocale(ul.getMessage(), new UnsupportedLocaleFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error WSRPException '" + wsrpe.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + wsrpe.getStackTrace()[0].toString() + "'",
                                new OperationFailed());

    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new OperationFailed("Error Exception '" + e.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + e.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    }
  }

  /**
   * @see 
   *      org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#initCookie(org
   *      .exoplatform.services.wsrp2.type.RegistrationContext
   *      registrationContext ,)org.exoplatform.services.wsrp2.type.UserContext
   *      userContext )*
   */
  public List<org.exoplatform.services.wsrp2.type.Extension> initCookie(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                                                        org.exoplatform.services.wsrp2.type.UserContext userContext) throws OperationNotSupported,
                                                                                                                                    AccessDenied,
                                                                                                                                    ResourceSuspended,
                                                                                                                                    InvalidRegistration,
                                                                                                                                    ModifyRegistrationRequired,
                                                                                                                                    OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation initCookie");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    try {
      WSRPHTTPContainer.getInstance().setVersion(2);
      ReturnAny response = markupOperationsInterface.initCookie(registrationContext, userContext);
      List<org.exoplatform.services.wsrp2.type.Extension> _return = response.getExtensions();
      return _return;
    } catch (AccessDenied ad) {
      throw new AccessDenied(ad.getMessage(), new AccessDeniedFault());
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage(), new ResourceSuspendedFault());
    } catch (InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (ModifyRegistrationRequired mrr) {
      throw new ModifyRegistrationRequired(mrr.getMessage(), new ModifyRegistrationRequiredFault());
    } catch (OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + wsrpe.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new OperationFailed("Error '" + e.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + e.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    }
  }

  /**
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#
   *      performBlockingInteraction
   *      (org.exoplatform.services.wsrp2.type.RegistrationContext
   *      registrationContext
   *      ,)org.exoplatform.services.wsrp2.type.PortletContext portletContext
   *      ,)org.exoplatform.services.wsrp2.type.RuntimeContext runtimeContext
   *      ,)org.exoplatform.services.wsrp2.type.UserContext userContext
   *      ,)org.exoplatform.services.wsrp2.type.MarkupParams markupParams
   *      ,)org.exoplatform.services.wsrp2.type.InteractionParams
   *      interactionParams ,)org.exoplatform.services.wsrp2.type.UpdateResponse
   *      updateResponse ,)java.lang.String redirectURL
   *      ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>
   *      extensions )*
   */
  public void performBlockingInteraction(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                         org.exoplatform.services.wsrp2.type.PortletContext portletContext,
                                         org.exoplatform.services.wsrp2.type.RuntimeContext runtimeContext,
                                         org.exoplatform.services.wsrp2.type.UserContext userContext,
                                         org.exoplatform.services.wsrp2.type.MarkupParams markupParams,
                                         org.exoplatform.services.wsrp2.type.InteractionParams interactionParams,
                                         javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.UpdateResponse> updateResponse,
                                         javax.xml.ws.Holder<java.lang.String> redirectURL,
                                         javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws AccessDenied,
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
      LOG.debug("Executing operation performBlockingInteraction");
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
      WSRPHTTPContainer.getInstance().setVersion(2);
      BlockingInteractionResponse response = markupOperationsInterface.performBlockingInteraction(registrationContext,
                                                                                                  portletContext,
                                                                                                  runtimeContext,
                                                                                                  userContext,
                                                                                                  markupParams,
                                                                                                  interactionParams);
      org.exoplatform.services.wsrp2.type.UpdateResponse updateResponseValue = response.getUpdateResponse();
      updateResponse.value = updateResponseValue;
      java.lang.String redirectURLValue = response.getRedirectURL();
      redirectURL.value = redirectURLValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (AccessDenied ad) {
      throw new AccessDenied(ad.getMessage(), new AccessDeniedFault());
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage(), new ResourceSuspendedFault());
    } catch (UnsupportedMimeType um) {
      throw new UnsupportedMimeType(um.getMessage(), new UnsupportedMimeTypeFault());
    } catch (InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (InvalidHandle ih) {
      throw new InvalidHandle(ih.getMessage(), new InvalidHandleFault());
    } catch (InvalidCookie ic) {
      throw new InvalidCookie(ic.getMessage(), new InvalidCookieFault());
    } catch (UnsupportedWindowState uws) {
      throw new UnsupportedWindowState(uws.getMessage(), new UnsupportedWindowStateFault());
    } catch (InvalidUserCategory iuc) {
      throw new InvalidUserCategory(iuc.getMessage(), new InvalidUserCategoryFault());
    } catch (UnsupportedMode unm) {
      throw new UnsupportedMode(unm.getMessage(), new UnsupportedModeFault());
    } catch (ModifyRegistrationRequired mrr) {
      throw new ModifyRegistrationRequired(mrr.getMessage(), new ModifyRegistrationRequiredFault());
    } catch (InvalidSession is) {
      throw new InvalidSession(is.getMessage(), new InvalidSessionFault());
    } catch (MissingParameters mp) {
      throw new MissingParameters(mp.getMessage(), new MissingParametersFault());
    } catch (InconsistentParameters icp) {
      throw new InconsistentParameters(icp.getMessage(), new InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new OperationFailed(of.getMessage());
    } catch (UnsupportedLocale ul) {
      throw new UnsupportedLocale(ul.getMessage());
    } catch (PortletStateChangeRequired ul) {
      throw new PortletStateChangeRequired(ul.getMessage(), new PortletStateChangeRequiredFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + wsrpe.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new OperationFailed("Error '" + e.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + e.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    }
  }

  /**
   * @see 
   *      org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#getMarkup(org.
   *      exoplatform.services.wsrp2.type.RegistrationContext
   *      registrationContext
   *      ,)org.exoplatform.services.wsrp2.type.PortletContext portletContext
   *      ,)org.exoplatform.services.wsrp2.type.RuntimeContext runtimeContext
   *      ,)org.exoplatform.services.wsrp2.type.UserContext userContext
   *      ,)org.exoplatform.services.wsrp2.type.MarkupParams markupParams
   *      ,)org.exoplatform.services.wsrp2.type.MarkupContext markupContext
   *      ,)org.exoplatform.services.wsrp2.type.SessionContext sessionContext
   *      ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>
   *      extensions )*
   */
  public void getMarkup(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                        org.exoplatform.services.wsrp2.type.PortletContext portletContext,
                        org.exoplatform.services.wsrp2.type.RuntimeContext runtimeContext,
                        org.exoplatform.services.wsrp2.type.UserContext userContext,
                        org.exoplatform.services.wsrp2.type.MarkupParams markupParams,
                        javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.MarkupContext> markupContext,
                        javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.SessionContext> sessionContext,
                        javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws AccessDenied,
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
      LOG.debug("Executing operation getMarkup");
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

      WSRPHTTPContainer.getInstance().setVersion(2);
      MarkupResponse response = markupOperationsInterface.getMarkup(registrationContext,
                                                                    portletContext,
                                                                    runtimeContext,
                                                                    userContext,
                                                                    markupParams);
      org.exoplatform.services.wsrp2.type.MarkupContext markupContextValue = response.getMarkupContext();
      markupContext.value = markupContextValue;
      org.exoplatform.services.wsrp2.type.SessionContext sessionContextValue = response.getSessionContext();
      sessionContext.value = sessionContextValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;

    } catch (AccessDenied ad) {
      throw new AccessDenied(ad.getMessage(), new AccessDeniedFault());
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage(), new ResourceSuspendedFault());
    } catch (UnsupportedMimeType um) {
      throw new UnsupportedMimeType(um.getMessage(), new UnsupportedMimeTypeFault());
    } catch (InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (InvalidHandle ih) {
      throw new InvalidHandle(ih.getMessage(), new InvalidHandleFault());
    } catch (InvalidCookie ic) {
      throw new InvalidCookie(ic.getMessage(), new InvalidCookieFault());
    } catch (UnsupportedWindowState uws) {
      throw new UnsupportedWindowState(uws.getMessage(), new UnsupportedWindowStateFault());
    } catch (InvalidUserCategory iuc) {
      throw new InvalidUserCategory(iuc.getMessage(), new InvalidUserCategoryFault());
    } catch (UnsupportedMode unm) {
      throw new UnsupportedMode(unm.getMessage(), new UnsupportedModeFault());
    } catch (ModifyRegistrationRequired mrr) {
      throw new ModifyRegistrationRequired(mrr.getMessage(), new ModifyRegistrationRequiredFault());
    } catch (InvalidSession is) {
      throw new InvalidSession(is.getMessage(), new InvalidSessionFault());
    } catch (MissingParameters mp) {
      throw new MissingParameters(mp.getMessage(), new MissingParametersFault());
    } catch (InconsistentParameters icp) {
      throw new InconsistentParameters(icp.getMessage(), new InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    } catch (UnsupportedLocale ul) {
      throw new UnsupportedLocale(ul.getMessage(), new UnsupportedLocaleFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + wsrpe.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new OperationFailed("Error '" + e.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + e.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    }

  }

  /**
   * @see 
   *      org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#releaseSessions
   *      (org.exoplatform.services.wsrp2.type.RegistrationContext
   *      registrationContext ,)java.util.List<java.lang.String> sessionIDs
   *      ,)org.exoplatform.services.wsrp2.type.UserContext userContext )*
   */
  public List<org.exoplatform.services.wsrp2.type.Extension> releaseSessions(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                                                             java.util.List<java.lang.String> sessionIDs,
                                                                             org.exoplatform.services.wsrp2.type.UserContext userContext) throws OperationNotSupported,
                                                                                                                                         AccessDenied,
                                                                                                                                         ResourceSuspended,
                                                                                                                                         InvalidRegistration,
                                                                                                                                         ModifyRegistrationRequired,
                                                                                                                                         MissingParameters,
                                                                                                                                         OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation releaseSessions");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(sessionIDs);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    try {
      WSRPHTTPContainer.getInstance().setVersion(2);
      ReturnAny response = markupOperationsInterface.releaseSessions(registrationContext,
                                                                     sessionIDs,
                                                                     userContext);
      List<org.exoplatform.services.wsrp2.type.Extension> _return = response.getExtensions();
      return _return;
    } catch (AccessDenied ad) {
      throw new AccessDenied(ad.getMessage(), new AccessDeniedFault());
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage(), new ResourceSuspendedFault());
    } catch (InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (ModifyRegistrationRequired mrr) {
      throw new ModifyRegistrationRequired(mrr.getMessage(), new ModifyRegistrationRequiredFault());
    } catch (MissingParameters mp) {
      throw new MissingParameters(mp.getMessage(), new MissingParametersFault());
    } catch (OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + wsrpe.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new OperationFailed("Error '" + e.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + e.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    }
  }

  /**
   * @see 
   *      org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#handleEvents(org
   *      .exoplatform.services.wsrp2.type.RegistrationContext
   *      registrationContext
   *      ,)org.exoplatform.services.wsrp2.type.PortletContext portletContext
   *      ,)org.exoplatform.services.wsrp2.type.RuntimeContext runtimeContext
   *      ,)org.exoplatform.services.wsrp2.type.UserContext userContext
   *      ,)org.exoplatform.services.wsrp2.type.MarkupParams markupParams
   *      ,)org.exoplatform.services.wsrp2.type.EventParams eventParams
   *      ,)org.exoplatform.services.wsrp2.type.UpdateResponse updateResponse
   *      ,)java
   *      .util.List<org.exoplatform.services.wsrp2.type.HandleEventsFailed>
   *      failedEvents
   *      ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>
   *      extensions )*
   */
  public void handleEvents(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                           org.exoplatform.services.wsrp2.type.PortletContext portletContext,
                           org.exoplatform.services.wsrp2.type.RuntimeContext runtimeContext,
                           org.exoplatform.services.wsrp2.type.UserContext userContext,
                           org.exoplatform.services.wsrp2.type.MarkupParams markupParams,
                           org.exoplatform.services.wsrp2.type.EventParams eventParams,
                           javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.UpdateResponse> updateResponse,
                           javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.HandleEventsFailed>> failedEvents,
                           javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
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
      LOG.debug("Executing operation handleEvents");
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
      LOG.debug(eventParams);
    try {
      WSRPHTTPContainer.getInstance().setVersion(2);
      HandleEventsResponse response = markupOperationsInterface.handleEvents(registrationContext,
                                                                             portletContext,
                                                                             runtimeContext,
                                                                             userContext,
                                                                             markupParams,
                                                                             eventParams);
      org.exoplatform.services.wsrp2.type.UpdateResponse updateResponseValue = response.getUpdateResponse();
      updateResponse.value = updateResponseValue;
      java.util.List<org.exoplatform.services.wsrp2.type.HandleEventsFailed> failedEventsValue = response.getFailedEvents();
      failedEvents.value = failedEventsValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;

    } catch (AccessDenied ad) {
      throw new AccessDenied(ad.getMessage(), new AccessDeniedFault());
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage(), new ResourceSuspendedFault());
    } catch (UnsupportedMimeType um) {
      throw new UnsupportedMimeType(um.getMessage(), new UnsupportedMimeTypeFault());
    } catch (InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (InvalidHandle ih) {
      throw new InvalidHandle(ih.getMessage(), new InvalidHandleFault());
    } catch (InvalidCookie ic) {
      throw new InvalidCookie(ic.getMessage(), new InvalidCookieFault());
    } catch (UnsupportedWindowState uws) {
      throw new UnsupportedWindowState(uws.getMessage(), new UnsupportedWindowStateFault());
    } catch (InvalidUserCategory iuc) {
      throw new InvalidUserCategory(iuc.getMessage(), new InvalidUserCategoryFault());
    } catch (UnsupportedMode unm) {
      throw new UnsupportedMode(unm.getMessage(), new UnsupportedModeFault());
    } catch (ModifyRegistrationRequired mrr) {
      throw new ModifyRegistrationRequired(mrr.getMessage(), new ModifyRegistrationRequiredFault());
    } catch (InvalidSession is) {
      throw new InvalidSession(is.getMessage(), new InvalidSessionFault());
    } catch (MissingParameters mp) {
      throw new MissingParameters(mp.getMessage(), new MissingParametersFault());
    } catch (InconsistentParameters icp) {
      throw new InconsistentParameters(icp.getMessage(), new InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    } catch (UnsupportedLocale ul) {
      throw new UnsupportedLocale(ul.getMessage(), new UnsupportedLocaleFault());
    } catch (PortletStateChangeRequired ul) {
      throw new PortletStateChangeRequired(ul.getMessage(), new PortletStateChangeRequiredFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + wsrpe.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new OperationFailed("Error '" + e.toString()
                                    + "'on a PRODUCER side with exception at '"
                                    + e.getStackTrace()[0].toString() + "'",
                                new OperationFailed());
    }
  }

}
