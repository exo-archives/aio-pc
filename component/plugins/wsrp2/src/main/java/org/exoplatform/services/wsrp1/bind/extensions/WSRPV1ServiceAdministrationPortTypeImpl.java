
package org.exoplatform.services.wsrp1.bind.extensions;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
import org.exoplatform.services.wsrp2.utils.Utils;

/**
 */

@javax.jws.WebService(name = "WSRPV1ServiceAdministrationPortType", serviceName = "WSRPService", portName = "WSRP_v1_ServiceAdministration_Service")
public class WSRPV1ServiceAdministrationPortTypeImpl implements
    WSRPV1ServiceAdministrationPortType, AbstractSingletonWebService {

  private static final Log               LOG = ExoLogger.getLogger(WSRPV1ServiceAdministrationPortTypeImpl.class.getName());

  private ServiceAdministrationInterface serviceAdministrationInterface;

  public WSRPV1ServiceAdministrationPortTypeImpl(ServiceAdministrationInterface serviceAdministrationInterface) {
    this.serviceAdministrationInterface = serviceAdministrationInterface;
  }

  public void getServiceAdministration(javax.xml.ws.Holder<String> properties) {

    LOG.info("Executing operation getServiceAdministration");
    System.out.println(properties);

    try {

      Map<String, String> props = new HashMap<String, String>();
      if (properties.value != null)
        props = Utils.getMapFromString(properties.value);

      ServiceAdministration response = serviceAdministrationInterface.getServiceAdministration(props);
      properties.value = response.getPropertiesAsString();

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
    //throw new ResourceSuspended("ResourceSuspended...");
    //throw new InvalidRegistration("InvalidRegistration...");
    //throw new ModifyRegistrationRequired("ModifyRegistrationRequired...");
    //throw new OperationFailed("OperationFailed...");
  }

}
