package org.exoplatform.services.wsrp2.bind1.extensions;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 
 */

@WebService(name = "WSRP_v1_ServiceAdministration_PortType")
public interface WSRPV1ServiceAdministrationPortType {

  @WebMethod(action = "urn:oasis:names:tc:wsrp:v1:getServiceAdministration")
  public void getServiceAdministration(@WebParam(mode = WebParam.Mode.INOUT, name = "properties", targetNamespace = "urn:oasis:names:tc:wsrp:v1:types")
  javax.xml.ws.Holder<String> properties);

}
