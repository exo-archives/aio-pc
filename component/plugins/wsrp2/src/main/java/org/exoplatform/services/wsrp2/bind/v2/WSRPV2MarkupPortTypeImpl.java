package org.exoplatform.services.wsrp2.bind.v2;

import org.apache.commons.logging.Log;
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
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.ReturnAny;

/**
 */

@javax.jws.WebService(name = "WSRPV2MarkupPortType", serviceName = "WSRPService2", portName = "WSRP_v2_Markup_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v2:wsdl", wsdlLocation = "/WEB-INF/wsdl/wsrp-service.wsdl", endpointInterface = "org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType")
public class WSRPV2MarkupPortTypeImpl implements WSRPV2MarkupPortType, AbstractSingletonWebService {

  private static final Log          LOG = ExoLogger.getLogger(WSRPV2MarkupPortTypeImpl.class.getName());

  private MarkupOperationsInterface markupOperationsInterface;

  public WSRPV2MarkupPortTypeImpl(MarkupOperationsInterface markupOperationsInterface) {
    this.markupOperationsInterface = markupOperationsInterface;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#getResource(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.PortletContext  portletContext ,)org.exoplatform.services.wsrp2.type.RuntimeContext  runtimeContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)org.exoplatform.services.wsrp2.type.ResourceParams  resourceParams ,)org.exoplatform.services.wsrp2.type.ResourceContext  resourceContext ,)org.exoplatform.services.wsrp2.type.SessionContext  sessionContext ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
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
    LOG.info("Executing operation getResource");
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
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#initCookie(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext )*
   */
  public org.exoplatform.services.wsrp2.type.Extension initCookie(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                                                  org.exoplatform.services.wsrp2.type.UserContext userContext) throws OperationNotSupported,
                                                                                                                              AccessDenied,
                                                                                                                              ResourceSuspended,
                                                                                                                              InvalidRegistration,
                                                                                                                              ModifyRegistrationRequired,
                                                                                                                              OperationFailed {
    LOG.info("Executing operation initCookie");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    try {
      ReturnAny response = markupOperationsInterface.initCookie(registrationContext, userContext);
      org.exoplatform.services.wsrp2.type.Extension _return = response.getExtensions();
      return _return;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#performBlockingInteraction(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.PortletContext  portletContext ,)org.exoplatform.services.wsrp2.type.RuntimeContext  runtimeContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)org.exoplatform.services.wsrp2.type.MarkupParams  markupParams ,)org.exoplatform.services.wsrp2.type.InteractionParams  interactionParams ,)org.exoplatform.services.wsrp2.type.UpdateResponse  updateResponse ,)java.lang.String  redirectURL ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
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
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#getMarkup(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.PortletContext  portletContext ,)org.exoplatform.services.wsrp2.type.RuntimeContext  runtimeContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)org.exoplatform.services.wsrp2.type.MarkupParams  markupParams ,)org.exoplatform.services.wsrp2.type.MarkupContext  markupContext ,)org.exoplatform.services.wsrp2.type.SessionContext  sessionContext ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
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
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }

  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#releaseSessions(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)java.util.List<java.lang.String>  sessionIDs ,)org.exoplatform.services.wsrp2.type.UserContext  userContext )*
   */
  public org.exoplatform.services.wsrp2.type.Extension releaseSessions(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                                                       java.util.List<java.lang.String> sessionIDs,
                                                                       org.exoplatform.services.wsrp2.type.UserContext userContext) throws OperationNotSupported,
                                                                                                                                   AccessDenied,
                                                                                                                                   ResourceSuspended,
                                                                                                                                   InvalidRegistration,
                                                                                                                                   ModifyRegistrationRequired,
                                                                                                                                   MissingParameters,
                                                                                                                                   OperationFailed {
    LOG.info("Executing operation releaseSessions");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(sessionIDs);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    try {
      ReturnAny response = markupOperationsInterface.releaseSessions(registrationContext,
                                                                     sessionIDs,
                                                                     userContext);
      org.exoplatform.services.wsrp2.type.Extension _return = response.getExtensions();
      return _return;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType#handleEvents(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.PortletContext  portletContext ,)org.exoplatform.services.wsrp2.type.RuntimeContext  runtimeContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)org.exoplatform.services.wsrp2.type.MarkupParams  markupParams ,)org.exoplatform.services.wsrp2.type.EventParams  eventParams ,)org.exoplatform.services.wsrp2.type.UpdateResponse  updateResponse ,)java.util.List<org.exoplatform.services.wsrp2.type.HandleEventsFailed>  failedEvents ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
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
    LOG.info("Executing operation handleEvents");
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
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

}
