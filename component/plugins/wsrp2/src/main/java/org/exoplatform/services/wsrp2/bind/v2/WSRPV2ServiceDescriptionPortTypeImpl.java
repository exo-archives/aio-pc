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
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.intf.UnsupportedLocale;
import org.exoplatform.services.wsrp2.intf.UnsupportedMimeType;
import org.exoplatform.services.wsrp2.intf.UnsupportedMode;
import org.exoplatform.services.wsrp2.intf.UnsupportedWindowState;
import org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.producer.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp2.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp2.type.ModifyRegistrationRequiredFault;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.ResourceSuspendedFault;
import org.exoplatform.services.wsrp2.type.ServiceDescription;

/**
 */

@javax.jws.WebService(name = "WSRPV2ServiceDescriptionPortType", serviceName = "WSRPService2", portName = "WSRP_v2_ServiceDescription_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v2:wsdl", wsdlLocation = "/WEB-INF/wsdl2/wsrp-service.wsdl", endpointInterface = "org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType")
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
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation getServiceDescription");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(desiredLocales);
    if (LOG.isDebugEnabled())
      LOG.debug(portletHandles);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);

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

}
