package org.exoplatform.services.wsrp2.bind.extensions;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 
 */

@WebService(name = "WSRP_v2_ServiceAdministration_PortType")
public interface WSRPV2ServiceAdministrationPortType {

  @WebMethod(action = "urn:oasis:names:tc:wsrp:v2:getServiceAdministration")
  public void getServiceAdministration(@WebParam(mode = WebParam.Mode.INOUT, name = "properties", targetNamespace = "urn:oasis:names:tc:wsrp:v2:types")
  javax.xml.ws.Holder<String> properties);

}
