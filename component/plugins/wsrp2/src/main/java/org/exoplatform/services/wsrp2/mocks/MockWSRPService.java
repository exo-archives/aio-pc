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

import java.net.URL;
import java.rmi.Remote;

import javax.xml.namespace.QName;

import org.exoplatform.services.wsrp2.bind.WSRPV2MarkupPortTypeImpl;
import org.exoplatform.services.wsrp2.bind.WSRPV2PortletManagementPortTypeImpl;
import org.exoplatform.services.wsrp2.bind.WSRPV2RegistrationPortTypeImpl;
import org.exoplatform.services.wsrp2.bind.WSRPV2ServiceDescriptionPortTypeImpl;
import org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2ServiceDescriptionPortType;
import org.exoplatform.services.wsrp2.wsdl.WSRPService;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 3 févr. 2004
 * Time: 02:59:48
 */

public class MockWSRPService extends WSRPService { //WSRPServiceLocator implements 
  private WSRPV2ServiceDescriptionPortTypeImpl serviceDescriptionInterface;

  private WSRPV2RegistrationPortTypeImpl       registrationOperationsInterface;

  private WSRPV2MarkupPortTypeImpl             markupOperationsInterface;

  private WSRPV2PortletManagementPortTypeImpl  portletManagementOperationsInterface;

  public MockWSRPService() {
    serviceDescriptionInterface = new WSRPV2ServiceDescriptionPortTypeImpl(null);
    registrationOperationsInterface = new WSRPV2RegistrationPortTypeImpl(null);
    markupOperationsInterface = new WSRPV2MarkupPortTypeImpl(null);
    portletManagementOperationsInterface = new WSRPV2PortletManagementPortTypeImpl(null);
  }

  public String getWSRPPortletManagementServiceAddress() {
    return "Mock";
  }

  public WSRPV2PortletManagementPortType getWSRPPortletManagementService() {
    return portletManagementOperationsInterface;
  }

  public WSRPV2PortletManagementPortType getWSRPPortletManagementService(URL portAddress) {
    return portletManagementOperationsInterface;
  }

  public String getWSRPRegistrationServiceAddress() {
    return "Mock";
  }

  public WSRPV2RegistrationPortType getWSRPRegistrationService() {
    return registrationOperationsInterface;
  }

  public WSRPV2RegistrationPortType getWSRPRegistrationService(URL portAddress) {
    return registrationOperationsInterface;
  }

  public String getWSRPMarkupServiceAddress() {
    return "Mock";
  }

  public WSRPV2MarkupPortType getWSRPMarkupService() {
    return markupOperationsInterface;
  }

  public WSRPV2MarkupPortType getWSRPMarkupService(URL portAddress) {
    return markupOperationsInterface;
  }

  public String getWSRPServiceDescriptionServiceAddress() {
    return "Mock";
  }

  public WSRPV2ServiceDescriptionPortType getWSRPServiceDescriptionService() {
    return serviceDescriptionInterface;
  }

  public WSRPV2ServiceDescriptionPortType getWSRPServiceDescriptionService(URL portAddress) {
    return serviceDescriptionInterface;
  }

  ///not necessary to implement

//  public Remote getPort(QName qName, Class aClass) {
//    return null; //To change body of implemented methods use File | Settings | File Templates.
//  }
//
//  public Remote getPort(Class aClass) {
//    return null; //To change body of implemented methods use File | Settings | File Templates.
//  }

//
//  public Call[] getCalls(QName qName) {
//    return new Call[0]; //To change body of implemented methods use File | Settings | File Templates.
//  }
//
//  public Call createCall(QName qName) {
//    return null; //To change body of implemented methods use File | Settings | File Templates.
//  }
//
//  public Call createCall(QName qName, QName qName1) {
//    return null; //To change body of implemented methods use File | Settings | File Templates.
//  }
//
//  public Call createCall(QName qName, String string) {
//    return null; //To change body of implemented methods use File | Settings | File Templates.
//  }
//
//  public Call createCall() {
//    return null; //To change body of implemented methods use File | Settings | File Templates.
//  }

  public QName getServiceName() {
    return null; //To change body of implemented methods use File | Settings | File Templates.
  }

  /*  public Iterator getPorts() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }*/

  public URL getWSDLDocumentLocation() {
    return null; //To change body of implemented methods use File | Settings | File Templates.
  }

//  public TypeMappingRegistry getTypeMappingRegistry() {
//    return null; //To change body of implemented methods use File | Settings | File Templates.
//  }
//
//  public HandlerRegistry getHandlerRegistry() {
//    return null; //To change body of implemented methods use File | Settings | File Templates.
//  }
}
