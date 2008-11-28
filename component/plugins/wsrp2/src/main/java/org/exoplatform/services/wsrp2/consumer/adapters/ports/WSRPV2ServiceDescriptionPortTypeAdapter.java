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
package org.exoplatform.services.wsrp2.consumer.adapters.ports;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.ServiceDescription;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public class WSRPV2ServiceDescriptionPortTypeAdapter {

  private WSRPV2ServiceDescriptionPortType serviceDescriptionPort;

  private static final Log                 LOG = ExoLogger.getLogger(WSRPV2ServiceDescriptionPortTypeAdapter.class);

  public WSRPV2ServiceDescriptionPortTypeAdapter(WSRPV2ServiceDescriptionPortType serviceDescriptionPort) {
    this.serviceDescriptionPort = serviceDescriptionPort;
  }

  public ServiceDescription getServiceDescription(GetServiceDescription serviceDescription) throws ResourceSuspended,
                                                                                           InvalidRegistration,
                                                                                           ModifyRegistrationRequired,
                                                                                           OperationFailed {

    System.out.println("Invoking getServiceDescription...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _getServiceDescription_registrationContext = serviceDescription.getRegistrationContext();
    java.util.List<java.lang.String> _getServiceDescription_desiredLocales = serviceDescription.getDesiredLocales();
    java.util.List<java.lang.String> _getServiceDescription_portletHandles = serviceDescription.getPortletHandles();
    org.exoplatform.services.wsrp2.type.UserContext _getServiceDescription_userContext = serviceDescription.getUserContext();
    javax.xml.ws.Holder<java.lang.Boolean> _getServiceDescription_requiresRegistration = new javax.xml.ws.Holder<java.lang.Boolean>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.PortletDescription>> _getServiceDescription_offeredPortlets = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.PortletDescription>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>> _getServiceDescription_userCategoryDescriptions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ExtensionDescription>> _getServiceDescription_extensionDescriptions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ExtensionDescription>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>> _getServiceDescription_customWindowStateDescriptions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>> _getServiceDescription_customModeDescriptions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ItemDescription>>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.CookieProtocol> _getServiceDescription_requiresInitCookie = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.CookieProtocol>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelDescription> _getServiceDescription_registrationPropertyDescription = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelDescription>();
    javax.xml.ws.Holder<java.util.List<java.lang.String>> _getServiceDescription_locales = new javax.xml.ws.Holder<java.util.List<java.lang.String>>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> _getServiceDescription_resourceList = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.EventDescription>> _getServiceDescription_eventDescriptions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.EventDescription>>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelTypes> _getServiceDescription_schemaType = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelTypes>();
    javax.xml.ws.Holder<java.util.List<java.lang.String>> _getServiceDescription_supportedOptions = new javax.xml.ws.Holder<java.util.List<java.lang.String>>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ExportDescription> _getServiceDescription_exportDescription = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ExportDescription>();
    javax.xml.ws.Holder<java.lang.Boolean> _getServiceDescription_mayReturnRegistrationState = new javax.xml.ws.Holder<java.lang.Boolean>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _getServiceDescription_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    serviceDescriptionPort.getServiceDescription(_getServiceDescription_registrationContext,
                                                 _getServiceDescription_desiredLocales,
                                                 _getServiceDescription_portletHandles,
                                                 _getServiceDescription_userContext,
                                                 _getServiceDescription_requiresRegistration,
                                                 _getServiceDescription_offeredPortlets,
                                                 _getServiceDescription_userCategoryDescriptions,
                                                 _getServiceDescription_extensionDescriptions,
                                                 _getServiceDescription_customWindowStateDescriptions,
                                                 _getServiceDescription_customModeDescriptions,
                                                 _getServiceDescription_requiresInitCookie,
                                                 _getServiceDescription_registrationPropertyDescription,
                                                 _getServiceDescription_locales,
                                                 _getServiceDescription_resourceList,
                                                 _getServiceDescription_eventDescriptions,
                                                 _getServiceDescription_schemaType,
                                                 _getServiceDescription_supportedOptions,
                                                 _getServiceDescription_exportDescription,
                                                 _getServiceDescription_mayReturnRegistrationState,
                                                 _getServiceDescription_extensions);

    System.out.println("getServiceDescription._getServiceDescription_requiresRegistration="
        + _getServiceDescription_requiresRegistration.value);
    System.out.println("getServiceDescription._getServiceDescription_offeredPortlets="
        + _getServiceDescription_offeredPortlets.value);
    System.out.println("getServiceDescription._getServiceDescription_userCategoryDescriptions="
        + _getServiceDescription_userCategoryDescriptions.value);
    System.out.println("getServiceDescription._getServiceDescription_extensionDescriptions="
        + _getServiceDescription_extensionDescriptions.value);
    System.out.println("getServiceDescription._getServiceDescription_customWindowStateDescriptions="
        + _getServiceDescription_customWindowStateDescriptions.value);
    System.out.println("getServiceDescription._getServiceDescription_customModeDescriptions="
        + _getServiceDescription_customModeDescriptions.value);
    System.out.println("getServiceDescription._getServiceDescription_requiresInitCookie="
        + _getServiceDescription_requiresInitCookie.value);
    System.out.println("getServiceDescription._getServiceDescription_registrationPropertyDescription="
        + _getServiceDescription_registrationPropertyDescription.value);
    System.out.println("getServiceDescription._getServiceDescription_locales="
        + _getServiceDescription_locales.value);
    System.out.println("getServiceDescription._getServiceDescription_resourceList="
        + _getServiceDescription_resourceList.value);
    System.out.println("getServiceDescription._getServiceDescription_eventDescriptions="
        + _getServiceDescription_eventDescriptions.value);
    System.out.println("getServiceDescription._getServiceDescription_schemaType="
        + _getServiceDescription_schemaType.value);
    System.out.println("getServiceDescription._getServiceDescription_supportedOptions="
        + _getServiceDescription_supportedOptions.value);
    System.out.println("getServiceDescription._getServiceDescription_exportDescription="
        + _getServiceDescription_exportDescription.value);
    System.out.println("getServiceDescription._getServiceDescription_mayReturnRegistrationState="
        + _getServiceDescription_mayReturnRegistrationState.value);
    System.out.println("getServiceDescription._getServiceDescription_extensions="
        + _getServiceDescription_extensions.value);

    ServiceDescription response = new ServiceDescription();
    response.setRequiresRegistration(_getServiceDescription_requiresRegistration.value);
    response.getOfferedPortlets().addAll(_getServiceDescription_offeredPortlets.value);
    response.getUserCategoryDescriptions()
            .addAll(_getServiceDescription_userCategoryDescriptions.value);
    response.getExtensionDescriptions().addAll(_getServiceDescription_extensionDescriptions.value);
    response.getCustomWindowStateDescriptions()
            .addAll(_getServiceDescription_customWindowStateDescriptions.value);
    response.getCustomModeDescriptions()
            .addAll(_getServiceDescription_customModeDescriptions.value);
    response.setRequiresInitCookie(_getServiceDescription_requiresInitCookie.value);
    response.setRegistrationPropertyDescription(_getServiceDescription_registrationPropertyDescription.value);
    response.getLocales().addAll(_getServiceDescription_locales.value);
    response.setResourceList(_getServiceDescription_resourceList.value);
    response.getEventDescriptions().addAll(_getServiceDescription_eventDescriptions.value);
    response.setSchemaType(_getServiceDescription_schemaType.value);
    response.getSupportedOptions().addAll(_getServiceDescription_supportedOptions.value);
    response.setExportDescription(_getServiceDescription_exportDescription.value);
    response.setMayReturnRegistrationState(_getServiceDescription_mayReturnRegistrationState.value);
    response.getExtensions().addAll(_getServiceDescription_extensions.value);
    return response;

  }
}
