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

package org.exoplatform.services.wsrp2.consumer.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.wsdl.WSRPService;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 23:10:08
 */

public class ProducerImpl implements Producer, java.io.Serializable {

  private String                                     name;

  private String                                     ID;

  private String                                     description;

  private String                                     serviceDescriptionInterfaceEndpoint;

  private transient WSRPV2ServiceDescriptionPortType serviceDescriptionInterface;

  private String                                     markupInterfaceEndpoint;

  private transient WSRPV2MarkupPortType             markupInterface;

  private String                                     portletManagementInterfaceEndpoint;

  private transient WSRPV2PortletManagementPortType  portletManagementInterface;

  private String                                     registrationInterfaceEndpoint;

  private ServiceDescription                         serviceDescription;

  private transient WSRPV2RegistrationPortType       registrationInterface;

  private boolean                                    registrationRequired;

  private RegistrationData                           registrationData;

  private RegistrationContext                        registrationContext;

  private transient WSRPService                      service;

  private List<String>                               desiredLocales = new ArrayList<String>();

  private transient Log                              log;

  private URL                                        url;

  public ProducerImpl(ExoContainer cont, String producerURL) {
//    this.desiredLocales = new ArrayList<String>();
    init(cont, producerURL);
  }

  public void init(ExoContainer cont, String producerURL) {
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    System.out.println(">>> EXOMAN ProducerImpl.init() producerURL = " + producerURL);
    try {
      this.url = new URL(producerURL);
      service = new WSRPService(this.url);//(WSRPService) cont.getComponentInstanceOfType(WSRPService.class);
    } catch (MalformedURLException e) {
      log.error("Exception eithin ProducerImpl.init() while creating producer url:'" + producerURL
          + "'", e);
    }
    System.out.println(">>> EXOMAN ProducerImpl.init() service = " + service);
//    ((WSRPServiceLocator) service).setMaintainSession(true);
  }

  public List<String> getDesiredLocales() {
    return desiredLocales;
  }

  public void setDesiredLocales(List<String> desiredLocales) {
    this.desiredLocales = desiredLocales;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  //service description
  public String getServiceDescriptionInterfaceEndpoint() {
    return serviceDescriptionInterfaceEndpoint;
  }

  public void setServiceDescriptionInterfaceEndpoint(String serviceDescriptionInterfaceEndpoint) {
    System.out.println(">>> EXOMAN ProducerImpl.setServiceDescriptionInterfaceEndpoint() 1 = " + 1);
//    this.serviceDescriptionInterfaceEndpoint = serviceDescriptionInterfaceEndpoint;
//    serviceDescriptionInterface = null;
  }

  public WSRPV2ServiceDescriptionPortType getServiceDescriptionInterface() {
    if (serviceDescriptionInterface == null) {
//      try {
//        try {s
      serviceDescriptionInterface = service.getWSRPV2ServiceDescriptionService();
//          (new URL(serviceDescriptionInterfaceEndpoint));

//        } catch (MalformedURLException e) {
//          log.debug("Malformed URL : " + serviceDescriptionInterfaceEndpoint);
//          serviceDescriptionInterface = service.getWSRPV2ServiceDescriptionService();
//        }
//      } catch (ServiceException e) {
//        e.printStackTrace();
//      }
    }
    return serviceDescriptionInterface;
  }

  public ServiceDescription getServiceDescription(boolean newRequest) throws WSRPException {
    if (newRequest) {
      return getServiceDescription();
    } else {
      return serviceDescription;
    }
  }

  public ServiceDescription getServiceDescription() throws WSRPException {
    if (desiredLocales == null) {
      throw new IllegalStateException("Desired locales field must be set");
    }
    if (serviceDescription == null) {
      updateServiceDescription();
    }
    return serviceDescription;
  }

  public PortletDescription getPortletDescription(String portletHandle) throws WSRPException {
    if (serviceDescription == null) {
      updateServiceDescription();
    }
    List<PortletDescription> array = serviceDescription.getOfferedPortlets();
    for (PortletDescription portletDescription : array) {
      if (portletDescription.getPortletHandle().equals(portletHandle)) {
        return portletDescription;
      }
    }
    return null;
  }

  private void updateServiceDescription() {
    try {
      getServiceDescriptionInterface();
      GetServiceDescription getServiceDescription = getServiceDescription(desiredLocales);
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

      getServiceDescriptionInterface().getServiceDescription(getServiceDescription.getRegistrationContext(),
                                                        getServiceDescription.getDesiredLocales(),
                                                        getServiceDescription.getPortletHandles(),
                                                        getServiceDescription.getUserContext(),
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private GetServiceDescription getServiceDescription(List<String> desiredLocales) {
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.setRegistrationContext(registrationContext);
    getServiceDescription.getDesiredLocales().addAll(desiredLocales);
    return getServiceDescription;
  }

  //markup
  public String getMarkupInterfaceEndpoint() {
    return markupInterfaceEndpoint;
  }

  public void setMarkupInterfaceEndpoint(String markupInterfaceEndpoint) {
    this.markupInterfaceEndpoint = markupInterfaceEndpoint;
  }

  //portlet management
  public String getPortletManagementInterfaceEndpoint() {
    return portletManagementInterfaceEndpoint;
  }

  public void setPortletManagementInterfaceEndpoint(String portletManagementInterfaceEndpoint) {
    this.portletManagementInterfaceEndpoint = portletManagementInterfaceEndpoint;
//    portletManagementInterface = null;
  }

  public WSRPV2PortletManagementPortType getPortletManagementInterface() {
    if (portletManagementInterface == null) {
      try {
//        try {
        portletManagementInterface = service.getWSRPV2PortletManagementService();//new URL(portletManagementInterfaceEndpoint));
//        } catch (MalformedURLException e) {
//          portletManagementInterface = service.getWSRPPortletManagementService();
//        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return portletManagementInterface;
  }

  public boolean isPortletManagementInferfaceSupported() {
    if (portletManagementInterface == null) {
      getPortletManagementInterface();
    }
    if (portletManagementInterface == null) {
      return false;
    } else {
      return true;
    }
  }

  //registration
  public String getRegistrationInterfaceEndpoint() {
    return registrationInterfaceEndpoint;
  }

  public void setRegistrationInterfaceEndpoint(String registrationInterfaceEndpoint) {
    this.registrationInterfaceEndpoint = registrationInterfaceEndpoint;
//    registrationInterface = null;
  }

  public WSRPV2RegistrationPortType getRegistrationInterface() {
    if (registrationInterface == null) {
      try {
//        try {
        registrationInterface = service.getWSRPV2RegistrationService();//RegistrationService(new URL(registrationInterfaceEndpoint));
//        } catch (MalformedURLException e) {
//          registrationInterface = service.getWSRPRegistrationService();
//        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return registrationInterface;
  }

  public boolean isRegistrationRequired() {
    if (serviceDescription == null) {
      updateServiceDescription();
    }
    return serviceDescription.isRequiresRegistration();
  }

  public RegistrationData getRegistrationData() {
    return registrationData;
  }

  public void setRegistrationData(RegistrationData registrationData) {
    this.registrationData = registrationData;
  }

  public RegistrationContext getRegistrationContext() throws WSRPException {
    return registrationContext;
  }

  public RegistrationContext register(Register register) throws WSRPException {
    if (registrationInterface == null) {
      getRegistrationInterface();
    }
    try {
      this.registrationContext = registrationInterface.register(register);
      this.registrationData = register.getRegistrationData();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return registrationContext;
  }

  public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws WSRPException {
    //ModifyRegistration modifyRegistration = new ModifyRegistration();
    modifyRegistration.setRegistrationData(registrationData);
    modifyRegistration.setRegistrationContext(registrationContext);
    javax.xml.ws.Holder<byte[]> _modifyRegistration_registrationState = new javax.xml.ws.Holder<byte[]>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> _modifyRegistration_scheduledDestruction = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _modifyRegistration_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();
    try {
      registrationInterface.modifyRegistration(modifyRegistration.getRegistrationContext(),
                                               modifyRegistration.getRegistrationData(),
                                               modifyRegistration.getUserContext(),
                                               _modifyRegistration_registrationState,
                                               _modifyRegistration_scheduledDestruction,
                                               _modifyRegistration_extensions);
    } catch (Exception e) {
      throw new WSRPException(Faults.INVALID_REGISTRATION_FAULT, e);
    }
    RegistrationState registrationState = new RegistrationState();
    registrationState.setRegistrationState(_modifyRegistration_registrationState.value);
    return registrationState;
  }

  public ReturnAny deregister(Deregister deregister) throws WSRPException {
    if (registrationInterface == null) {
      getRegistrationInterface();
    }
    try {
      registrationInterface.deregister(deregister.getRegistrationContext(),
                                       deregister.getUserContext());
    } catch (Exception e) {
      throw new WSRPException(Faults.INVALID_REGISTRATION_FAULT, e);
    } finally {
      registrationContext = null;
      registrationData = null;
    }
    ReturnAny returnAny = new ReturnAny();
    return returnAny;
  }

  public boolean isRegistrationInterfaceSupported() {
    if (serviceDescriptionInterface == null) {
      getServiceDescriptionInterface();
    }
    if (serviceDescriptionInterface == null) {
      return false;
    } else {
      return true;
    }
  }
  
  public URL getUrl() {
    return url;
  }
  
}
