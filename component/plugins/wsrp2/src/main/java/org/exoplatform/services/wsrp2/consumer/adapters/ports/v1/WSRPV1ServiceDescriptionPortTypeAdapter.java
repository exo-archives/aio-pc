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
package org.exoplatform.services.wsrp2.consumer.adapters.ports.v1;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp1.intf.WS1InvalidRegistration;
import org.exoplatform.services.wsrp1.intf.WS1OperationFailed;
import org.exoplatform.services.wsrp1.intf.WSRPV1ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPServiceDescriptionPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public class WSRPV1ServiceDescriptionPortTypeAdapter implements
    WSRPServiceDescriptionPortTypeAdapterAPI {

  private WSRPV1ServiceDescriptionPortType serviceDescriptionPort;

  private static final Log                 LOG = ExoLogger.getLogger(WSRPV1ServiceDescriptionPortTypeAdapter.class);

  public WSRPV1ServiceDescriptionPortTypeAdapter(WSRPV1ServiceDescriptionPortType serviceDescriptionPort) {
    this.serviceDescriptionPort = serviceDescriptionPort;
  }

  public ServiceDescription getServiceDescription(GetServiceDescription serviceDescription) throws ResourceSuspended,
                                                                                           InvalidRegistration,
                                                                                           ModifyRegistrationRequired,
                                                                                           OperationFailed {

    if (LOG.isDebugEnabled())
      LOG.debug("Invoking getServiceDescription...");

    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _getServiceDescription_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(serviceDescription.getRegistrationContext());
    java.util.List<java.lang.String> _getServiceDescription_desiredLocales = serviceDescription.getDesiredLocales();
//    java.util.List<java.lang.String> _getServiceDescription_portletHandles = serviceDescription.getPortletHandles();
//    org.exoplatform.services.wsrp2.type.UserContext _getServiceDescription_userContext = serviceDescription.getUserContext();
    javax.xml.ws.Holder<java.lang.Boolean> _getServiceDescription_requiresRegistration = new javax.xml.ws.Holder<java.lang.Boolean>();

    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1PortletDescription>> _getServiceDescription_offeredPortlets = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1PortletDescription>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>> _getServiceDescription_userCategoryDescriptions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>> _getServiceDescription_customUserProfileItemDescriptions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>> _getServiceDescription_customWindowStateDescriptions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>> _getServiceDescription_customModeDescriptions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ItemDescription>>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1CookieProtocol> _getServiceDescription_requiresInitCookie = new javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1CookieProtocol>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ModelDescription> _getServiceDescription_registrationPropertyDescription = new javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ModelDescription>();
    javax.xml.ws.Holder<java.util.List<java.lang.String>> _getServiceDescription_locales = new javax.xml.ws.Holder<java.util.List<java.lang.String>>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ResourceList> _getServiceDescription_resourceList = new javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ResourceList>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> _getServiceDescription_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>>();

    try {
      serviceDescriptionPort.getServiceDescription(_getServiceDescription_registrationContext,
                                                   _getServiceDescription_desiredLocales,
                                                   _getServiceDescription_requiresRegistration,
                                                   _getServiceDescription_offeredPortlets,
                                                   _getServiceDescription_userCategoryDescriptions,
                                                   _getServiceDescription_customUserProfileItemDescriptions,
                                                   _getServiceDescription_customWindowStateDescriptions,
                                                   _getServiceDescription_customModeDescriptions,
                                                   _getServiceDescription_requiresInitCookie,
                                                   _getServiceDescription_registrationPropertyDescription,
                                                   _getServiceDescription_locales,
                                                   _getServiceDescription_resourceList,
                                                   _getServiceDescription_extensions);

      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_requiresRegistration="
            + _getServiceDescription_requiresRegistration.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_offeredPortlets="
            + _getServiceDescription_offeredPortlets.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_userCategoryDescriptions="
            + _getServiceDescription_userCategoryDescriptions.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_customUserProfileItemDescriptions="
            + _getServiceDescription_customUserProfileItemDescriptions.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_customWindowStateDescriptions="
            + _getServiceDescription_customWindowStateDescriptions.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_customModeDescriptions="
            + _getServiceDescription_customModeDescriptions.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_requiresInitCookie="
            + _getServiceDescription_requiresInitCookie.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_registrationPropertyDescription="
            + _getServiceDescription_registrationPropertyDescription.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_locales="
            + _getServiceDescription_locales.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_resourceList="
            + _getServiceDescription_resourceList.value);
      if (LOG.isDebugEnabled())
        LOG.debug("getServiceDescription._getServiceDescription_extensions="
            + _getServiceDescription_extensions.value);

    } catch (WS1InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (WS1OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    }

    // commented unused in wsrp1 items

    ServiceDescription response = new ServiceDescription();
    response.setRequiresRegistration(_getServiceDescription_requiresRegistration.value);
    response.getOfferedPortlets()
            .addAll(WSRPTypesTransformer.getWS2PortletDescriptions(_getServiceDescription_offeredPortlets.value));
    response.getUserCategoryDescriptions()
            .addAll(WSRPTypesTransformer.getWS2ItemDescriptions(_getServiceDescription_userCategoryDescriptions.value));
//    response.getExtensionDescriptions().addAll(_getServiceDescription_extensionDescriptions.value);
    response.getCustomWindowStateDescriptions()
            .addAll(WSRPTypesTransformer.getWS2ItemDescriptions(_getServiceDescription_customWindowStateDescriptions.value));
    response.getCustomModeDescriptions()
            .addAll(WSRPTypesTransformer.getWS2ItemDescriptions(_getServiceDescription_customModeDescriptions.value));
    response.setRequiresInitCookie(WSRPTypesTransformer.getWS2CookieProtocol(_getServiceDescription_requiresInitCookie.value));
    response.setRegistrationPropertyDescription(WSRPTypesTransformer.getWS2ModelDescription(_getServiceDescription_registrationPropertyDescription.value));
    response.getLocales().addAll(_getServiceDescription_locales.value);
    response.setResourceList(WSRPTypesTransformer.getWS2ResourceList(_getServiceDescription_resourceList.value));
//    response.getEventDescriptions().addAll(_getServiceDescription_eventDescriptions.value);
//    response.setSchemaType(_getServiceDescription_schemaType.value);
//    response.getSupportedOptions().addAll(_getServiceDescription_supportedOptions.value);
//    response.setExportDescription(_getServiceDescription_exportDescription.value);
//    response.setMayReturnRegistrationState(_getServiceDescription_mayReturnRegistrationState.value);
    response.getExtensions()
            .addAll(WSRPTypesTransformer.getWS2Extensions(_getServiceDescription_extensions.value));
    return response;

  }
}
