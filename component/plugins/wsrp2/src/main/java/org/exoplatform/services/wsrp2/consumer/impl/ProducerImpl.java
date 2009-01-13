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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp1.intf.WSRPV1ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPMarkupPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPPortletManagementPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPRegistrationPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPServiceDescriptionPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v1.WSRPV1MarkupPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v1.WSRPV1PortletManagementPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v1.WSRPV1RegistrationPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v1.WSRPV1ServiceDescriptionPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v2.WSRPV2MarkupPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v2.WSRPV2PortletManagementPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v2.WSRPV2RegistrationPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.v2.WSRPV2ServiceDescriptionPortTypeAdapter;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.GetRegistrationLifetime;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SetRegistrationLifetime;
import org.exoplatform.services.wsrp2.wsdl.WSRPService2;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 23:10:08
 */

public class ProducerImpl implements Producer, java.io.Serializable {

  private String                                             name;

  private String                                             ID;

  private String                                             description;

  private transient WSRPServiceDescriptionPortTypeAdapterAPI serviceDescriptionAdapter;

  private transient WSRPMarkupPortTypeAdapterAPI             markupAdapter;

  private transient WSRPPortletManagementPortTypeAdapterAPI  portletManagementAdapter;

  private transient WSRPRegistrationPortTypeAdapterAPI       registrationAdapter;

  private ServiceDescription                                 serviceDescription;

  private RegistrationData                                   registrationData;

  private RegistrationContext                                registrationContext;

  private List<String>                                       desiredLocales = new ArrayList<String>();

  private URL                                                url;

  private int                                                version        = 2;

  private static final Log                                   LOG            = ExoLogger.getLogger(ProducerImpl.class);

  public ProducerImpl(ExoContainer container, String producerURL, Integer version) {
//    this.desiredLocales = new ArrayList<String>();
    this.url = initProducerURL(producerURL);
    if (version != null)
      setVersion(version);
    init(container);
  }

  /**
   * Usefull for ProducerRegistryImpl.loadProducers()
   */
  public void init(ExoContainer container, String producerURL) {
    this.url = initProducerURL(producerURL);
    init(container);
  }

  private void init(ExoContainer container) {
    if (this.url == null)
      return;
    if (this.version == 1)
      init1(container);
    else
      init2(container);
  }

  private void init1(ExoContainer container) {
    this.ID = createProducerID("producer1");
    org.exoplatform.services.wsrp1.intf.WSRPService service = new org.exoplatform.services.wsrp1.intf.WSRPService(this.url);
    createAdapters1(service, container);
  }

  private void init2(ExoContainer container) {
    this.ID = createProducerID("producer2");
    WSRPService2 service = new WSRPService2(this.url);
    createAdapters2(service, container);
  }

  /**
   * Usefull for BaseTest.setUp()
   */
  public void createAdapters(javax.xml.ws.Service service, ExoContainer container) {
    if (this.version == 1)
      createAdapters1((org.exoplatform.services.wsrp1.intf.WSRPService) service, container);
    else
      createAdapters2((WSRPService2) service, container);
  }

  private void createAdapters1(org.exoplatform.services.wsrp1.intf.WSRPService service,
                               ExoContainer container) {
//    container.registerComponentInstance(this.ID, service);

//    WSRPV1ServiceDescriptionPortType SDpt = service.getWSRPServiceDescriptionService();
//    setTimeOut(ClientProxy.getClient(SDpt));
    this.serviceDescriptionAdapter = new WSRPV1ServiceDescriptionPortTypeAdapter(service.getWSRPServiceDescriptionService());
    this.markupAdapter = new WSRPV1MarkupPortTypeAdapter(service.getWSRPMarkupService());
    this.registrationAdapter = new WSRPV1RegistrationPortTypeAdapter(service.getWSRPRegistrationService());
    this.portletManagementAdapter = new WSRPV1PortletManagementPortTypeAdapter(service.getWSRPPortletManagementService());
  }

  private void createAdapters2(WSRPService2 service, ExoContainer container) {
//    container.registerComponentInstance(this.ID, service);

//    WSRPV2ServiceDescriptionPortType SDpt = service.getWSRPV2ServiceDescriptionService();
//    setTimeOut(ClientProxy.getClient(SDpt));
    this.serviceDescriptionAdapter = new WSRPV2ServiceDescriptionPortTypeAdapter(service.getWSRPV2ServiceDescriptionService());
    this.markupAdapter = new WSRPV2MarkupPortTypeAdapter(service.getWSRPV2MarkupService());
    this.registrationAdapter = new WSRPV2RegistrationPortTypeAdapter(service.getWSRPV2RegistrationService());
    this.portletManagementAdapter = new WSRPV2PortletManagementPortTypeAdapter(service.getWSRPV2PortletManagementService());
  }

  public int getVersion() {
    return this.version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  private void setTimeOut(Client client) {
    HTTPConduit http = (HTTPConduit) client.getConduit();
    org.apache.cxf.endpoint.Endpoint endpoint = client.getEndpoint();
    String address = endpoint.getEndpointInfo().getAddress();

    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
    httpClientPolicy.setConnectionTimeout(360000);
    httpClientPolicy.setAllowChunking(false);
    httpClientPolicy.setReceiveTimeout(320000);
    http.setClient(httpClientPolicy);
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

  public WSRPServiceDescriptionPortTypeAdapterAPI getServiceDescriptionAdapter() {
    return serviceDescriptionAdapter;
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
      GetServiceDescription getServiceDescription = getServiceDescription(desiredLocales);
      serviceDescription = serviceDescriptionAdapter.getServiceDescription(getServiceDescription);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private GetServiceDescription getServiceDescription(List<String> desiredLocales) {
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.setRegistrationContext(registrationContext);
    getServiceDescription.setUserContext(null);
    if (desiredLocales != null)
      getServiceDescription.getDesiredLocales().addAll(desiredLocales);
    return getServiceDescription;
  }

  public WSRPMarkupPortTypeAdapterAPI getMarkupAdapter() {
    return markupAdapter;
  }

  public WSRPPortletManagementPortTypeAdapterAPI getPortletManagementAdapter() {
    return portletManagementAdapter;
  }

  public boolean isPortletManagementInferfaceSupported() {
    if (portletManagementAdapter == null) {
      getPortletManagementAdapter();
    }
    if (portletManagementAdapter == null) {
      return false;
    } else {
      return true;
    }
  }

  public WSRPRegistrationPortTypeAdapterAPI getRegistrationAdapter() {
    return registrationAdapter;
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
    try {
      this.registrationContext = registrationAdapter.register(register);
      this.registrationData = register.getRegistrationData();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return registrationContext;
  }

  public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws WSRPException {
    RegistrationState registrationState = null;
//    ModifyRegistration modifyRegistration = new ModifyRegistration();
//    modifyRegistration.setRegistrationData(registrationData);
//    modifyRegistration.setRegistrationContext(registrationContext);
    try {
      registrationState = registrationAdapter.modifyRegistration(modifyRegistration);
    } catch (Exception e) {
      throw new WSRPException(Faults.INVALID_REGISTRATION_FAULT, e);
    }
    return registrationState;
  }

  public ReturnAny deregister(Deregister deregister) throws WSRPException {
    try {
      Extension extension = registrationAdapter.deregister(deregister);
    } catch (Exception e) {
      throw new WSRPException(Faults.INVALID_REGISTRATION_FAULT, e);
    } finally {
      registrationContext = null;
      registrationData = null;
    }
    ReturnAny returnAny = new ReturnAny();
    return returnAny;
  }

  public Lifetime getRegistrationLifetime(GetRegistrationLifetime getRegistrationLifetime) throws WSRPException {
    Lifetime lifetime = null;
    try {
      lifetime = registrationAdapter.getRegistrationLifetime(getRegistrationLifetime);
    } catch (Exception e) {
      throw new WSRPException(Faults.INVALID_REGISTRATION_FAULT, e);
    }
    return lifetime;
  }

  public Lifetime setRegistrationLifetime(SetRegistrationLifetime setRegistrationLifetime) throws WSRPException {
    Lifetime lifetime = null;
    try {
      lifetime = registrationAdapter.setRegistrationLifetime(setRegistrationLifetime);
    } catch (Exception e) {
      throw new WSRPException(Faults.INVALID_REGISTRATION_FAULT, e);
    }
    return lifetime;
  }

  public boolean isRegistrationAdapterSupported() {
    if (serviceDescriptionAdapter == null) {
      return false;
    } else {
      return true;
    }
  }

  public URL getUrl() {
    return url;
  }

  public void setServiceDescriptionAdapter(WSRPServiceDescriptionPortTypeAdapterAPI serviceDescriptionAdapter) {
    this.serviceDescriptionAdapter = serviceDescriptionAdapter;
  }

  public void setMarkupAdapter(WSRPMarkupPortTypeAdapterAPI markupAdapter) {
    this.markupAdapter = markupAdapter;
  }

  public void setRegistrationAdapter(WSRPRegistrationPortTypeAdapterAPI registrationAdapter) {
    this.registrationAdapter = registrationAdapter;
  }

  public void setPortletManagementAdapter(WSRPPortletManagementPortTypeAdapterAPI portletManagementAdapter) {
    this.portletManagementAdapter = portletManagementAdapter;
  }

  private URL initProducerURL(String producerURL) {
    if (producerURL == null || "".equalsIgnoreCase(producerURL))
      return null;
    try {
      return new URL(producerURL);
    } catch (MalformedURLException e) {
      LOG.error("Exception within ProducerImpl.init() while creating producer url:'" + producerURL
          + "'", e);
      return null;
    }
  }

  private String createProducerID(String string) {
    return string
        + Integer.toString(this.url.toExternalForm().hashCode()) + "_"
            + Long.toString(Calendar.getInstance().getTimeInMillis(), 16);
  }

}
