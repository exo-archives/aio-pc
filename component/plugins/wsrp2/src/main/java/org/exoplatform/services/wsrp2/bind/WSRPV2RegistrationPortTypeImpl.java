/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package org.exoplatform.services.wsrp2.bind;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType;
import org.exoplatform.services.wsrp2.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;

/**
 * This class was generated by Apache CXF 2.1.2 Wed Oct 08 15:10:04 GMT+02:00
 * 2008 Generated source version: 2.1.2
 */

@javax.jws.WebService(name = "WSRPV2RegistrationPortType", serviceName = "WSRPService", portName = "WSRP_v2_Registration_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v2:wsdl", wsdlLocation = "file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl/wsrp-service.wsdl", endpointInterface = "org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType")
public class WSRPV2RegistrationPortTypeImpl implements WSRPV2RegistrationPortType,
    AbstractSingletonWebService {

  private static final Log                LOG = ExoLogger.getLogger(WSRPV2RegistrationPortTypeImpl.class.getName());

  private RegistrationOperationsInterface registrationOperationsInterface;

  public WSRPV2RegistrationPortTypeImpl(RegistrationOperationsInterface registrationOperationsInterface) {
    this.registrationOperationsInterface = registrationOperationsInterface;
    System.out.println(">>> EXOMAN WSRPV2RegistrationPortTypeImpl.WSRPV2RegistrationPortTypeImpl() registrationOperationsInterface = "
        + registrationOperationsInterface);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType#register(org.exoplatform.services.wsrp2.type.Register  register )*
   */
  public org.exoplatform.services.wsrp2.type.RegistrationContext register(org.exoplatform.services.wsrp2.type.Register register) throws OperationNotSupported,
                                                                                                                                MissingParameters,
                                                                                                                                OperationFailed {
    LOG.info("Executing operation register");
    System.out.println(register);
    try {
      RegistrationContext response = registrationOperationsInterface.register(register.getRegistrationData(),
                                                                              register.getUserContext(),
                                                                              register.getLifetime());
      org.exoplatform.services.wsrp2.type.RegistrationContext _return = response;
      return _return;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
    //throw new OperationNotSupported("OperationNotSupported...");
    //throw new MissingParameters("MissingParameters...");
    //throw new OperationFailed("OperationFailed...");
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType#deregister(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext )*
   */
  public org.exoplatform.services.wsrp2.type.Extension deregister(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                                                  org.exoplatform.services.wsrp2.type.UserContext userContext) throws OperationNotSupported,
                                                                                                                              ResourceSuspended,
                                                                                                                              InvalidRegistration,
                                                                                                                              OperationFailed {
    LOG.info("Executing operation deregister");
    System.out.println(registrationContext);
    System.out.println(userContext);
    try {
      ReturnAny response = registrationOperationsInterface.deregister(registrationContext,
                                                                      userContext);
      org.exoplatform.services.wsrp2.type.Extension _return = response.getExtensions();
      return _return;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
    //throw new OperationNotSupported("OperationNotSupported...");
    //throw new ResourceSuspended("ResourceSuspended...");
    //throw new InvalidRegistration("InvalidRegistration...");
    //throw new OperationFailed("OperationFailed...");
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType#getRegistrationLifetime(org.exoplatform.services.wsrp2.type.GetRegistrationLifetime  getRegistrationLifetime )*
   */
  public org.exoplatform.services.wsrp2.type.Lifetime getRegistrationLifetime(org.exoplatform.services.wsrp2.type.GetRegistrationLifetime getRegistrationLifetime) throws OperationNotSupported,
                                                                                                                                                                  AccessDenied,
                                                                                                                                                                  ResourceSuspended,
                                                                                                                                                                  InvalidRegistration,
                                                                                                                                                                  InvalidHandle,
                                                                                                                                                                  ModifyRegistrationRequired,
                                                                                                                                                                  OperationFailed {
    LOG.info("Executing operation getRegistrationLifetime");
    System.out.println(getRegistrationLifetime);
    try {
      Lifetime response = registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime.getRegistrationContext(),
                                                                                  getRegistrationLifetime.getUserContext());
      org.exoplatform.services.wsrp2.type.Lifetime _return = response;
      return _return;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
    //throw new OperationNotSupported("OperationNotSupported...");
    //throw new AccessDenied("AccessDenied...");
    //throw new ResourceSuspended("ResourceSuspended...");
    //throw new InvalidRegistration("InvalidRegistration...");
    //throw new InvalidHandle("InvalidHandle...");
    //throw new ModifyRegistrationRequired("ModifyRegistrationRequired...");
    //throw new OperationFailed("OperationFailed...");
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType#setRegistrationLifetime(org.exoplatform.services.wsrp2.type.SetRegistrationLifetime  setRegistrationLifetime )*
   */
  public org.exoplatform.services.wsrp2.type.Lifetime setRegistrationLifetime(org.exoplatform.services.wsrp2.type.SetRegistrationLifetime setRegistrationLifetime) throws OperationNotSupported,
                                                                                                                                                                  AccessDenied,
                                                                                                                                                                  ResourceSuspended,
                                                                                                                                                                  InvalidRegistration,
                                                                                                                                                                  InvalidHandle,
                                                                                                                                                                  ModifyRegistrationRequired,
                                                                                                                                                                  OperationFailed {
    LOG.info("Executing operation setRegistrationLifetime");
    System.out.println(setRegistrationLifetime);
    try {
      Lifetime response = registrationOperationsInterface.setRegistrationLifetime(setRegistrationLifetime.getRegistrationContext(),
                                                                                  setRegistrationLifetime.getUserContext(),
                                                                                  setRegistrationLifetime.getLifetime());
      org.exoplatform.services.wsrp2.type.Lifetime _return = response;
      return _return;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
    //throw new OperationNotSupported("OperationNotSupported...");
    //throw new AccessDenied("AccessDenied...");
    //throw new ResourceSuspended("ResourceSuspended...");
    //throw new InvalidRegistration("InvalidRegistration...");
    //throw new InvalidHandle("InvalidHandle...");
    //throw new ModifyRegistrationRequired("ModifyRegistrationRequired...");
    //throw new OperationFailed("OperationFailed...");
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType#modifyRegistration(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.RegistrationData  registrationData ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)byte[]  registrationState ,)org.exoplatform.services.wsrp2.type.Lifetime  scheduledDestruction ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void modifyRegistration(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                 org.exoplatform.services.wsrp2.type.RegistrationData registrationData,
                                 org.exoplatform.services.wsrp2.type.UserContext userContext,
                                 javax.xml.ws.Holder<byte[]> registrationState,
                                 javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> scheduledDestruction,
                                 javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                               ResourceSuspended,
                                                                                                                               InvalidRegistration,
                                                                                                                               MissingParameters,
                                                                                                                               OperationFailed {
    LOG.info("Executing operation modifyRegistration");
    System.out.println(registrationContext);
    System.out.println(registrationData);
    System.out.println(userContext);
    try {
      RegistrationState response = registrationOperationsInterface.modifyRegistration(registrationContext,
                                                                                      registrationData,
                                                                                      userContext);
      byte[] registrationStateValue = response.getRegistrationState();
      registrationState.value = registrationStateValue;
      org.exoplatform.services.wsrp2.type.Lifetime scheduledDestructionValue = response.getScheduledDestruction();
      scheduledDestruction.value = scheduledDestructionValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
    //throw new OperationNotSupported("OperationNotSupported...");
    //throw new ResourceSuspended("ResourceSuspended...");
    //throw new InvalidRegistration("InvalidRegistration...");
    //throw new MissingParameters("MissingParameters...");
    //throw new OperationFailed("OperationFailed...");
  }
}
