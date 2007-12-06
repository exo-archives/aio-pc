/*
* Copyright 2001-2007 The eXo platform SAS  All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.mock;


import java.net.URL;
import java.rmi.Remote;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.TypeMappingRegistry;
import javax.xml.rpc.handler.HandlerRegistry;

import org.exoplatform.services.wsrp.bind.WSRP_v2_Markup_Binding_SOAPImpl;
import org.exoplatform.services.wsrp.bind.WSRP_v2_PortletManagement_Binding_SOAPImpl;
import org.exoplatform.services.wsrp.bind.WSRP_v2_Registration_Binding_SOAPImpl;
import org.exoplatform.services.wsrp.bind.WSRP_v2_ServiceDescription_Binding_SOAPImpl;
import org.exoplatform.services.wsrp.intf.WSRP_v2_Markup_PortType;
import org.exoplatform.services.wsrp.intf.WSRP_v2_PortletManagement_PortType;
import org.exoplatform.services.wsrp.intf.WSRP_v2_Registration_PortType;
import org.exoplatform.services.wsrp.intf.WSRP_v2_ServiceDescription_PortType;
import org.exoplatform.services.wsrp.wsdl.WSRPService;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 3 févr. 2004
 * Time: 02:59:48
 */

public class MockWSRPService implements WSRPService {
  private WSRP_v2_ServiceDescription_Binding_SOAPImpl serviceDescriptionInterface;
  private WSRP_v2_Registration_Binding_SOAPImpl registrationOperationsInterface;
  private WSRP_v2_Markup_Binding_SOAPImpl markupOperationsInterface;
  private WSRP_v2_PortletManagement_Binding_SOAPImpl portletManagementOperationsInterface;

  public MockWSRPService() {
    serviceDescriptionInterface = new WSRP_v2_ServiceDescription_Binding_SOAPImpl();
    registrationOperationsInterface = new WSRP_v2_Registration_Binding_SOAPImpl();
    markupOperationsInterface = new WSRP_v2_Markup_Binding_SOAPImpl();
    portletManagementOperationsInterface = new WSRP_v2_PortletManagement_Binding_SOAPImpl();
  }

  public String getWSRPPortletManagementServiceAddress() {
    return "Mock";
  }

  public WSRP_v2_PortletManagement_PortType getWSRPPortletManagementService() throws ServiceException {
    return portletManagementOperationsInterface;
  }

  public WSRP_v2_PortletManagement_PortType getWSRPPortletManagementService(URL portAddress) throws ServiceException {
    return portletManagementOperationsInterface;
  }

  public String getWSRPRegistrationServiceAddress() {
    return "Mock";
  }

  public WSRP_v2_Registration_PortType getWSRPRegistrationService() throws ServiceException {
    return registrationOperationsInterface;
  }

  public WSRP_v2_Registration_PortType getWSRPRegistrationService(URL portAddress) throws ServiceException {
    return registrationOperationsInterface;
  }

  public String getWSRPBaseServiceAddress() {
    return "Mock";
  }

  public WSRP_v2_Markup_PortType getWSRPBaseService() throws ServiceException {
    return markupOperationsInterface;
  }

  public WSRP_v2_Markup_PortType getWSRPBaseService(URL portAddress) throws ServiceException {
    return markupOperationsInterface;
  }

  public String getWSRPServiceDescriptionServiceAddress() {
    return "Mock";
  }

  public WSRP_v2_ServiceDescription_PortType getWSRPServiceDescriptionService() throws ServiceException {
    return serviceDescriptionInterface;
  }

  public WSRP_v2_ServiceDescription_PortType getWSRPServiceDescriptionService(URL portAddress) throws ServiceException {
    return serviceDescriptionInterface;
  }



  ///not necessary to implement


  public Remote getPort(QName qName, Class aClass) throws ServiceException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Remote getPort(Class aClass) throws ServiceException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Call[] getCalls(QName qName) throws ServiceException {
    return new Call[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Call createCall(QName qName) throws ServiceException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Call createCall(QName qName, QName qName1) throws ServiceException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Call createCall(QName qName, String string) throws ServiceException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Call createCall() throws ServiceException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public QName getServiceName() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Iterator getPorts() throws ServiceException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public URL getWSDLDocumentLocation() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public TypeMappingRegistry getTypeMappingRegistry() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public HandlerRegistry getHandlerRegistry() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
