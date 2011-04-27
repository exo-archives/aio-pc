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

import org.apache.commons.logging.Log;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_Markup_PortType;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_PortletManagement_PortType;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_Registration_PortType;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_ServiceDescription_PortType;
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
import org.exoplatform.services.wsrp2.wsdl.WSRPServiceLocator;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 23:10:08
 */

public class ProducerImpl implements Producer, java.io.Serializable {

  private String                                        name;

  private String                                        ID;

  private String                                        description;

  private String                                        serviceDescriptionInterfaceEndpoint;

  private transient WSRP_v2_ServiceDescription_PortType serviceDescriptionInterface;

  private String                                        markupInterfaceEndpoint;

  private transient WSRP_v2_Markup_PortType             markupInterface;

  private String                                        portletManagementInterfaceEndpoint;

  private transient WSRP_v2_PortletManagement_PortType  portletManagementInterface;

  private String                                        registrationInterfaceEndpoint;

  private ServiceDescription                            serviceDescription;

  private transient WSRP_v2_Registration_PortType       registrationInterface;

  private boolean                                       registrationRequired;

  private RegistrationData                              registrationData;

  private RegistrationContext                           registrationContext;

  private transient WSRPService                         service;

  private String[]                                      desiredLocales;

  private transient Log                                 log;

  public ProducerImpl(ExoContainer cont) {
    init(cont);
  }

  public void init(ExoContainer cont) {
    this.service = (WSRPService) cont.getComponentInstanceOfType(WSRPService.class);
    ((WSRPServiceLocator) this.service).setMaintainSession(true);
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
  }

  public String[] getDesiredLocales() {
    return desiredLocales;
  }

  public void setDesiredLocales(String[] desiredLocales) {
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
    this.serviceDescriptionInterfaceEndpoint = serviceDescriptionInterfaceEndpoint;
    serviceDescriptionInterface = null;
  }

  public WSRP_v2_ServiceDescription_PortType getServiceDescriptionInterface() {
    if (serviceDescriptionInterface == null) {
      try {
        try {
          serviceDescriptionInterface = service.getWSRPServiceDescriptionService(new URL(serviceDescriptionInterfaceEndpoint));

        } catch (MalformedURLException e) {
          log.debug("Malformed URL : " + serviceDescriptionInterfaceEndpoint);
          serviceDescriptionInterface = service.getWSRPServiceDescriptionService();
        }
      } catch (ServiceException e) {
        e.printStackTrace();
      }
    }
    applySecurityParams((org.apache.axis.client.Stub)serviceDescriptionInterface);
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
    PortletDescription[] array = serviceDescription.getOfferedPortlets();
    for (int i = 0; i < array.length; i++) {
      PortletDescription portletDescription = array[i];
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
      serviceDescription = serviceDescriptionInterface.getServiceDescription(getServiceDescription);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  private GetServiceDescription getServiceDescription(String[] desiredLocales) {
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.setRegistrationContext(registrationContext);
    getServiceDescription.setDesiredLocales(desiredLocales);
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
    portletManagementInterface = null;
  }

  public WSRP_v2_PortletManagement_PortType getPortletManagementInterface() {
    if (portletManagementInterface == null) {
      try {
        try {
          portletManagementInterface = service.getWSRPPortletManagementService(new URL(portletManagementInterfaceEndpoint));
        } catch (MalformedURLException e) {
          portletManagementInterface = service.getWSRPPortletManagementService();
        }
      } catch (ServiceException e) {
        e.printStackTrace();
      }
    }
    applySecurityParams((org.apache.axis.client.Stub)portletManagementInterface);
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
    registrationInterface = null;
  }

  public WSRP_v2_Registration_PortType getRegistrationInterface() {
    if (registrationInterface == null) {
      try {
        try {
          registrationInterface = service.getWSRPRegistrationService(new URL(registrationInterfaceEndpoint));
        } catch (MalformedURLException e) {
          registrationInterface = service.getWSRPRegistrationService();
        }
      } catch (ServiceException e) {
        e.printStackTrace();
      }
    }
    applySecurityParams((org.apache.axis.client.Stub)registrationInterface);
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
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return registrationContext;
  }

  public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws WSRPException {
    //ModifyRegistration modifyRegistration = new ModifyRegistration();
    modifyRegistration.setRegistrationData(registrationData);
    modifyRegistration.setRegistrationContext(registrationContext);
    try {
      return registrationInterface.modifyRegistration(modifyRegistration);
    } catch (RemoteException e) {
      throw new WSRPException(Faults.INVALID_REGISTRATION_FAULT, e);
    }
  }

  public ReturnAny deregister(Deregister deregister) throws WSRPException {
    if (registrationInterface == null) {
      getRegistrationInterface();
    }
    try {
      return registrationInterface.deregister(deregister);
    } catch (RemoteException e) {
      throw new WSRPException(Faults.INVALID_REGISTRATION_FAULT, e);
    } finally {
      registrationContext = null;
      registrationData = null;
    }
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
  
  private void applySecurityParams(org.apache.axis.client.Stub port) {
    String username = WindowInfosContainer.getInstance().getOwner();
      if (username != null) {
      ExoContainer cont = ExoContainerContext.getCurrentContainer();
      OrganizationService orgService = (OrganizationService)cont .getComponentInstanceOfType(OrganizationService.class);
      String password = null;
      try {
        org.exoplatform.services.organization.User user = orgService.getUserHandler().findUserByName(username);
        password = user.getPassword();
      } catch (Exception e) {
        log.error("Fail to get user from organization service", e);
      }
      if (password != null) {
        port.setUsername(username);
        port.setPassword(password);
        port._setProperty(Call.SESSION_MAINTAIN_PROPERTY, Boolean.FALSE);
        port._setProperty(WSHandlerConstants.USER, username);
      }
    }
  }

}
