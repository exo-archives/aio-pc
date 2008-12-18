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
package org.exoplatform.services.wsrp1.mocks;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType;
import org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType;
import org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType;
import org.exoplatform.services.wsrp1.intf.WSRPV1ServiceDescriptionPortType;
import org.exoplatform.services.wsrp1.wsdl.WSRPService;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Dec 8, 2008
 */

@WebServiceClient(name = "WSRPService", wsdlLocation = "file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl/wsrp-service.wsdl", targetNamespace = "urn:oasis:names:tc:wsrp:v2:wsdl")
public class MockWSRPService extends WSRPService {

  public final static URL   WSDL_LOCATION;

  public final static QName SERVICE                         = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRPService");

  public final static QName WSRPV1ServiceDescriptionService = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRP_v1_ServiceDescription_Service");

  public final static QName WSRPV1MarkupService             = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRP_v1_Markup_Service");

  public final static QName WSRPV1RegistrationService       = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRP_v1_Registration_Service");

  public final static QName WSRPV1PortletManagementService  = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRP_v1_PortletManagement_Service");
  
  private ExoContainer container;
  
  static {
    URL url = null;
    try {
      url = new URL("file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl/wsrp-service.wsdl");
    } catch (MalformedURLException e) {
      System.err.println("Can not initialize the default wsdl from file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl/wsrp-service.wsdl");
      // e.printStackTrace();
    }
    WSDL_LOCATION = url;
  }

//      public MockWSRPService2(URL wsdlLocation) {
//          super(wsdlLocation, SERVICE);
//      }
//
//      public MockWSRPService2(URL wsdlLocation, QName serviceName) {
//          super(wsdlLocation, serviceName);
//      }
//
//      public MockWSRPService2() {
//          super(WSDL_LOCATION, SERVICE);
//      }

  public MockWSRPService(ExoContainer container) {
    super(null, null);
    this.container = container;
  }

  /**
   * @return returns WSRPV1ServiceDescriptionPortType
   */
  @WebEndpoint(name = "WSRP_v1_ServiceDescription_Service")
  public WSRPV1ServiceDescriptionPortType getWSRPV1ServiceDescriptionService() {
    return (WSRPV1ServiceDescriptionPortType) container.getComponentInstanceOfType(WSRPV1ServiceDescriptionPortType.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
   *          configure on the proxy. Supported features not in the
   *          <code>features</code> parameter will have their default values.
   * @return returns WSRPV1ServiceDescriptionPortType
   */
  @WebEndpoint(name = "WSRP_v1_ServiceDescription_Service")
  public WSRPV1ServiceDescriptionPortType getWSRPV1ServiceDescriptionService(WebServiceFeature... features) {
    return null;
  }

  /**
   * @return returns WSRPV1MarkupPortType
   */
  @WebEndpoint(name = "WSRP_v1_Markup_Service")
  public WSRPV1MarkupPortType getWSRPV1MarkupService() {
    return (WSRPV1MarkupPortType) container.getComponentInstanceOfType(WSRPV1MarkupPortType.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
   *          configure on the proxy. Supported features not in the
   *          <code>features</code> parameter will have their default values.
   * @return returns WSRPV1MarkupPortType
   */
  @WebEndpoint(name = "WSRP_v1_Markup_Service")
  public WSRPV1MarkupPortType getWSRPV1MarkupService(WebServiceFeature... features) {
    return null;
  }

  /**
   * @return returns WSRPV1RegistrationPortType
   */
  @WebEndpoint(name = "WSRP_v1_Registration_Service")
  public WSRPV1RegistrationPortType getWSRPV1RegistrationService() {
    return (WSRPV1RegistrationPortType) container.getComponentInstanceOfType(WSRPV1RegistrationPortType.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
   *          configure on the proxy. Supported features not in the
   *          <code>features</code> parameter will have their default values.
   * @return returns WSRPV1RegistrationPortType
   */
  @WebEndpoint(name = "WSRP_v1_Registration_Service")
  public WSRPV1RegistrationPortType getWSRPV1RegistrationService(WebServiceFeature... features) {
    return null;
  }

  /**
   * @return returns WSRPV1PortletManagementPortType
   */
  @WebEndpoint(name = "WSRP_v1_PortletManagement_Service")
  public WSRPV1PortletManagementPortType getWSRPV1PortletManagementService() {
    return (WSRPV1PortletManagementPortType) container.getComponentInstanceOfType(WSRPV1PortletManagementPortType.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
   *          configure on the proxy. Supported features not in the
   *          <code>features</code> parameter will have their default values.
   * @return returns WSRPV1PortletManagementPortType
   */
  @WebEndpoint(name = "WSRP_v1_PortletManagement_Service")
  public WSRPV1PortletManagementPortType getWSRPV1PortletManagementService(WebServiceFeature... features) {
    return null;
  }

}
