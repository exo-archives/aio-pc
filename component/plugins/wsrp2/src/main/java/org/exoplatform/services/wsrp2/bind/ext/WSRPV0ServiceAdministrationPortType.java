package org.exoplatform.services.wsrp2.bind.ext;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 
 */

@WebService(name = "WSRP_v0_ServiceAdministration_PortType")
public interface WSRPV0ServiceAdministrationPortType {

  @WebMethod(action = "urn:oasis:names:tc:wsrp:v0:getServiceAdministration")
  public void getServiceAdministration(@WebParam(mode = WebParam.Mode.INOUT, name = "properties", targetNamespace = "urn:oasis:names:tc:wsrp:v0:types")
  javax.xml.ws.Holder<String> properties);

}
