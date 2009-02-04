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
package org.exoplatform.services.wsrp2.mocks;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.wsdl.WSRPService2;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Dec 8, 2008
 */

@WebServiceClient(name = "WSRPService2", wsdlLocation = "/WEB-INF/wsdl/wsrp-service.wsdl", targetNamespace = "urn:oasis:names:tc:wsrp:v2:wsdl")
public class MockWSRPService extends WSRPService2 {

  public final static URL   WSDL_LOCATION;

  public final static QName SERVICE                         = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRPService2");

  public final static QName WSRPV2ServiceDescriptionService = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRP_v2_ServiceDescription_Service");

  public final static QName WSRPV2MarkupService             = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRP_v2_Markup_Service");

  public final static QName WSRPV2RegistrationService       = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRP_v2_Registration_Service");

  public final static QName WSRPV2PortletManagementService  = new QName("urn:oasis:names:tc:wsrp:v2:wsdl",
                                                                        "WSRP_v2_PortletManagement_Service");
  
  private ExoContainer container;
  
  static {
    URL url = null;
//    try {
//      url = new URL("file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl/wsrp-service.wsdl");
//    } catch (MalformedURLException e) {
//      System.err.println("Can not initialize the default wsdl from file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl/wsrp-service.wsdl");
//      // e.printStackTrace();
//    }
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
   * @return returns WSRPV2ServiceDescriptionPortType
   */
  @WebEndpoint(name = "WSRP_v2_ServiceDescription_Service")
  public WSRPV2ServiceDescriptionPortType getWSRPV2ServiceDescriptionService() {
    return (WSRPV2ServiceDescriptionPortType) container.getComponentInstanceOfType(WSRPV2ServiceDescriptionPortType.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
   *          configure on the proxy. Supported features not in the
   *          <code>features</code> parameter will have their default values.
   * @return returns WSRPV2ServiceDescriptionPortType
   */
  @WebEndpoint(name = "WSRP_v2_ServiceDescription_Service")
  public WSRPV2ServiceDescriptionPortType getWSRPV2ServiceDescriptionService(WebServiceFeature... features) {
    return null;
  }

  /**
   * @return returns WSRPV2MarkupPortType
   */
  @WebEndpoint(name = "WSRP_v2_Markup_Service")
  public WSRPV2MarkupPortType getWSRPV2MarkupService() {
    return (WSRPV2MarkupPortType) container.getComponentInstanceOfType(WSRPV2MarkupPortType.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
   *          configure on the proxy. Supported features not in the
   *          <code>features</code> parameter will have their default values.
   * @return returns WSRPV2MarkupPortType
   */
  @WebEndpoint(name = "WSRP_v2_Markup_Service")
  public WSRPV2MarkupPortType getWSRPV2MarkupService(WebServiceFeature... features) {
    return null;
  }

  /**
   * @return returns WSRPV2RegistrationPortType
   */
  @WebEndpoint(name = "WSRP_v2_Registration_Service")
  public WSRPV2RegistrationPortType getWSRPV2RegistrationService() {
    return (WSRPV2RegistrationPortType) container.getComponentInstanceOfType(WSRPV2RegistrationPortType.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
   *          configure on the proxy. Supported features not in the
   *          <code>features</code> parameter will have their default values.
   * @return returns WSRPV2RegistrationPortType
   */
  @WebEndpoint(name = "WSRP_v2_Registration_Service")
  public WSRPV2RegistrationPortType getWSRPV2RegistrationService(WebServiceFeature... features) {
    return null;
  }

  /**
   * @return returns WSRPV2PortletManagementPortType
   */
  @WebEndpoint(name = "WSRP_v2_PortletManagement_Service")
  public WSRPV2PortletManagementPortType getWSRPV2PortletManagementService() {
    return (WSRPV2PortletManagementPortType) container.getComponentInstanceOfType(WSRPV2PortletManagementPortType.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
   *          configure on the proxy. Supported features not in the
   *          <code>features</code> parameter will have their default values.
   * @return returns WSRPV2PortletManagementPortType
   */
  @WebEndpoint(name = "WSRP_v2_PortletManagement_Service")
  public WSRPV2PortletManagementPortType getWSRPV2PortletManagementService(WebServiceFeature... features) {
    return null;
  }

}
