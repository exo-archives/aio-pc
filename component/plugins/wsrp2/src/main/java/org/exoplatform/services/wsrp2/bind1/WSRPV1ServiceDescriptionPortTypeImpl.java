package org.exoplatform.services.wsrp2.bind1;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp1.intf.WS1InvalidRegistration;
import org.exoplatform.services.wsrp1.intf.WS1OperationFailed;
import org.exoplatform.services.wsrp1.intf.WSRPV1ServiceDescriptionPortType;
import org.exoplatform.services.wsrp1.type.WS1InvalidRegistrationFault;
import org.exoplatform.services.wsrp1.type.WS1OperationFailedFault;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.producer.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 * 
 */

@javax.jws.WebService(name = "WSRPV1ServiceDescriptionPortType", serviceName = "WSRPService", portName = "WSRPMarkupService", targetNamespace = "urn:oasis:names:tc:wsrp:v1:wsdl", wsdlLocation = "file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl1/wsrp_service.wsdl", endpointInterface = "org.exoplatform.services.wsrp1.intf.WSRPV1ServiceDescriptionPortType")
public class WSRPV1ServiceDescriptionPortTypeImpl implements WSRPV1ServiceDescriptionPortType {

  private static final Log            LOG = ExoLogger.getLogger(WSRPV1ServiceDescriptionPortTypeImpl.class.getName());

  private ServiceDescriptionInterface serviceDescriptionInterface;

  public WSRPV1ServiceDescriptionPortTypeImpl(ServiceDescriptionInterface serviceDescriptionInterface) {
    this.serviceDescriptionInterface = serviceDescriptionInterface;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1ServiceDescriptionPortType#getServiceDescription(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)java.util.List<java.lang.String>  desiredLocales ,)java.lang.Boolean  requiresRegistration ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1PortletDescription>  offeredPortlets ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>  userCategoryDescriptions ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>  customUserProfileItemDescriptions ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>  customWindowStateDescriptions ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>  customModeDescriptions ,)org.exoplatform.services.wsrp1.type.WS1CookieProtocol  requiresInitCookie ,)org.exoplatform.services.wsrp1.type.WS1ModelDescription  registrationPropertyDescription ,)java.util.List<java.lang.String>  locales ,)org.exoplatform.services.wsrp1.type.WS1ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void getServiceDescription(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                                    java.util.List<java.lang.String> desiredLocales,
                                    javax.xml.ws.Holder<java.lang.Boolean> requiresRegistration,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1PortletDescription>> offeredPortlets,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>> userCategoryDescriptions,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>> customUserProfileItemDescriptions,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>> customWindowStateDescriptions,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>> customModeDescriptions,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1CookieProtocol> requiresInitCookie,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ModelDescription> registrationPropertyDescription,
                                    javax.xml.ws.Holder<java.util.List<java.lang.String>> locales,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ResourceList> resourceList,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1InvalidRegistration,
                                                                                                                                     WS1OperationFailed {
    LOG.info("Executing operation getServiceDescription");
    System.out.println(registrationContext);
    System.out.println(desiredLocales);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);

      ServiceDescription response = serviceDescriptionInterface.getServiceDescription(ws2registrationContext,
                                                                                      desiredLocales,
                                                                                      null,
                                                                                      null);

      requiresRegistration.value = false;
      offeredPortlets.value = WSRPTypesTransformer.getWS1PortletDescriptions(response.getOfferedPortlets());
      userCategoryDescriptions.value = WSRPTypesTransformer.getWS1ItemDescriptions(response.getUserCategoryDescriptions());
//      customUserProfileItemDescriptions.value = WSRPTypesTransformer.getWS1ItemDescriptions(response.getCustomUserProfileItemDescriptions());
      customWindowStateDescriptions.value = WSRPTypesTransformer.getWS1ItemDescriptions(response.getCustomWindowStateDescriptions());
      customModeDescriptions.value = WSRPTypesTransformer.getWS1ItemDescriptions(response.getCustomModeDescriptions());
      requiresInitCookie.value = WSRPTypesTransformer.getWS1CookieProtocol(response.getRequiresInitCookie());
      registrationPropertyDescription.value = WSRPTypesTransformer.getWS1ModelDescription(response.getRegistrationPropertyDescription());
      locales.value = null;
      resourceList.value = null;
      extensions.value = null;

    } catch (InvalidRegistration ir) {
      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (OperationFailed of) {
      LOG.error(of.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new WS1OperationFailed(wsrpe.getMessage(), new WS1OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new WS1OperationFailed(e.getMessage(), new WS1OperationFailedFault());
    }

  }

}
