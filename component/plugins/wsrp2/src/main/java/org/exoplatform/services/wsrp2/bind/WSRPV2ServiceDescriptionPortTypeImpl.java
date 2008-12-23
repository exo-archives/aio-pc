package org.exoplatform.services.wsrp2.bind;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.producer.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.ServiceDescription;

/**
 */

@javax.jws.WebService(name = "WSRPV2ServiceDescriptionPortType", serviceName = "WSRPService", portName = "WSRP_v2_ServiceDescription_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v2:wsdl", wsdlLocation = "file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl/wsrp-service.wsdl", endpointInterface = "org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType")
public class WSRPV2ServiceDescriptionPortTypeImpl implements WSRPV2ServiceDescriptionPortType,
    AbstractSingletonWebService {

  private static final Log            LOG = ExoLogger.getLogger(WSRPV2ServiceDescriptionPortTypeImpl.class.getName());

  private ServiceDescriptionInterface serviceDescriptionInterface;

  public WSRPV2ServiceDescriptionPortTypeImpl(ServiceDescriptionInterface serviceDescriptionInterface) {
    this.serviceDescriptionInterface = serviceDescriptionInterface;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType#getServiceDescription(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)java.util.List<java.lang.String>  desiredLocales ,)java.util.List<java.lang.String>  portletHandles ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)java.lang.Boolean  requiresRegistration ,)java.util.List<org.exoplatform.services.wsrp2.type.PortletDescription>  offeredPortlets ,)java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>  userCategoryDescriptions ,)java.util.List<org.exoplatform.services.wsrp2.type.ExtensionDescription>  extensionDescriptions ,)java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>  customWindowStateDescriptions ,)java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>  customModeDescriptions ,)org.exoplatform.services.wsrp2.type.CookieProtocol  requiresInitCookie ,)org.exoplatform.services.wsrp2.type.ModelDescription  registrationPropertyDescription ,)java.util.List<java.lang.String>  locales ,)org.exoplatform.services.wsrp2.type.ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp2.type.EventDescription>  eventDescriptions ,)org.exoplatform.services.wsrp2.type.ModelTypes  schemaType ,)java.util.List<java.lang.String>  supportedOptions ,)org.exoplatform.services.wsrp2.type.ExportDescription  exportDescription ,)java.lang.Boolean  mayReturnRegistrationState ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void getServiceDescription(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                    java.util.List<java.lang.String> desiredLocales,
                                    java.util.List<java.lang.String> portletHandles,
                                    org.exoplatform.services.wsrp2.type.UserContext userContext,
                                    javax.xml.ws.Holder<java.lang.Boolean> requiresRegistration,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.PortletDescription>> offeredPortlets,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>> userCategoryDescriptions,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ExtensionDescription>> extensionDescriptions,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>> customWindowStateDescriptions,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>> customModeDescriptions,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.CookieProtocol> requiresInitCookie,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelDescription> registrationPropertyDescription,
                                    javax.xml.ws.Holder<java.util.List<java.lang.String>> locales,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> resourceList,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.EventDescription>> eventDescriptions,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelTypes> schemaType,
                                    javax.xml.ws.Holder<java.util.List<java.lang.String>> supportedOptions,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ExportDescription> exportDescription,
                                    javax.xml.ws.Holder<java.lang.Boolean> mayReturnRegistrationState,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws ResourceSuspended,
                                                                                                                                  InvalidRegistration,
                                                                                                                                  ModifyRegistrationRequired,
                                                                                                                                  OperationFailed {
    LOG.info("Executing operation getServiceDescription");
    System.out.println(registrationContext);
    System.out.println(desiredLocales);
    System.out.println(portletHandles);
    System.out.println(userContext);

    try {

      ServiceDescription response = serviceDescriptionInterface.getServiceDescription(registrationContext,
                                                                                      desiredLocales,
                                                                                      portletHandles,
                                                                                      userContext);
      java.lang.Boolean requiresRegistrationValue = response.isRequiresRegistration();
      requiresRegistration.value = requiresRegistrationValue;
      java.util.List<org.exoplatform.services.wsrp2.type.PortletDescription> offeredPortletsValue = response.getOfferedPortlets();
      offeredPortlets.value = offeredPortletsValue;
      java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription> userCategoryDescriptionsValue = response.getUserCategoryDescriptions();
      userCategoryDescriptions.value = userCategoryDescriptionsValue;
      java.util.List<org.exoplatform.services.wsrp2.type.ExtensionDescription> extensionDescriptionsValue = response.getExtensionDescriptions();
      extensionDescriptions.value = extensionDescriptionsValue;
      java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription> customWindowStateDescriptionsValue = response.getCustomWindowStateDescriptions();
      customWindowStateDescriptions.value = customWindowStateDescriptionsValue;
      java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription> customModeDescriptionsValue = response.getCustomModeDescriptions();
      customModeDescriptions.value = customModeDescriptionsValue;
      org.exoplatform.services.wsrp2.type.CookieProtocol requiresInitCookieValue = response.getRequiresInitCookie();
      requiresInitCookie.value = requiresInitCookieValue;
      org.exoplatform.services.wsrp2.type.ModelDescription registrationPropertyDescriptionValue = response.getRegistrationPropertyDescription();
      registrationPropertyDescription.value = registrationPropertyDescriptionValue;
      java.util.List<java.lang.String> localesValue = response.getLocales();
      locales.value = localesValue;
      org.exoplatform.services.wsrp2.type.ResourceList resourceListValue = response.getResourceList();
      resourceList.value = resourceListValue;
      java.util.List<org.exoplatform.services.wsrp2.type.EventDescription> eventDescriptionsValue = response.getEventDescriptions();
      eventDescriptions.value = eventDescriptionsValue;
      org.exoplatform.services.wsrp2.type.ModelTypes schemaTypeValue = response.getSchemaType();
      schemaType.value = schemaTypeValue;
      java.util.List<java.lang.String> supportedOptionsValue = response.getSupportedOptions();
      supportedOptions.value = supportedOptionsValue;
      org.exoplatform.services.wsrp2.type.ExportDescription exportDescriptionValue = response.getExportDescription();
      exportDescription.value = exportDescriptionValue;
      java.lang.Boolean mayReturnRegistrationStateValue = response.isMayReturnRegistrationState();
      mayReturnRegistrationState.value = mayReturnRegistrationStateValue;
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
