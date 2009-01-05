
package org.exoplatform.services.wsrp2.bind.v1;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp1.intf.WS1InvalidRegistration;
import org.exoplatform.services.wsrp1.intf.WS1MissingParameters;
import org.exoplatform.services.wsrp1.intf.WS1OperationFailed;
import org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType;
import org.exoplatform.services.wsrp1.type.WS1InvalidRegistrationFault;
import org.exoplatform.services.wsrp1.type.WS1MissingParametersFault;
import org.exoplatform.services.wsrp1.type.WS1OperationFailedFault;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 */

@javax.jws.WebService(name = "WSRPV1RegistrationPortType", serviceName = "WSRPService", portName = "WSRPMarkupService", targetNamespace = "urn:oasis:names:tc:wsrp:v1:wsdl", wsdlLocation = "file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl1/wsrp_service.wsdl", endpointInterface = "org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType")
public class WSRPV1RegistrationPortTypeImpl implements WSRPV1RegistrationPortType {

  private final Log                       LOG = ExoLogger.getLogger(WSRPV1RegistrationPortTypeImpl.class);

  private RegistrationOperationsInterface registrationOperationsInterface;

  public WSRPV1RegistrationPortTypeImpl(RegistrationOperationsInterface registrationOperationsInterface) {
    this.registrationOperationsInterface = registrationOperationsInterface;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType#modifyRegistration(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp1.type.WS1RegistrationData  registrationData ,)byte[]  registrationState ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void modifyRegistration(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                                 org.exoplatform.services.wsrp1.type.WS1RegistrationData registrationData,
                                 javax.xml.ws.Holder<byte[]> registrationState,
                                 javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1InvalidRegistration,
                                                                                                                                  WS1MissingParameters,
                                                                                                                                  WS1OperationFailed {
    LOG.info("Executing operation modifyRegistration");
    System.out.println(registrationContext);
    System.out.println(registrationData);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);
      RegistrationData ws2registrationData = WSRPTypesTransformer.getWS2RegistrationData(registrationData);

      RegistrationState response = registrationOperationsInterface.modifyRegistration(ws2registrationContext,
                                                                                      ws2registrationData,
                                                                                      null);

      registrationState.value = response.getRegistrationState();
      extensions.value = WSRPTypesTransformer.getWS1Extensions(response.getExtensions());

    } catch (InvalidRegistration ir) {
      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (MissingParameters ir) {
      LOG.error(ir.getMessage(), ir);
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (OperationFailed of) {
      LOG.error(of.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new WS1OperationFailed(wsrpe.getMessage(), new WS1OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new WS1OperationFailed(e.getMessage(), new WS1OperationFailedFault());
    }

  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType#deregister(java.lang.String  registrationHandle ,)byte[]  registrationState ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void deregister(java.lang.String registrationHandle,
                         byte[] registrationState,
                         javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1InvalidRegistration,
                                                                                                                          WS1OperationFailed {
    LOG.info("Executing operation deregister");
    System.out.println(registrationHandle);
    System.out.println(registrationState);
    System.out.println(extensions.value);
    try {

      RegistrationContext ws2registrationContext = new RegistrationContext();
      ws2registrationContext.setRegistrationHandle(registrationHandle);
      ws2registrationContext.setRegistrationState(registrationState);

      ReturnAny response = registrationOperationsInterface.deregister(ws2registrationContext, null);

      List<Extension> ws2Extensions = new ArrayList<Extension>();
      ws2Extensions.add(response.getExtensions());
      extensions.value = WSRPTypesTransformer.getWS1Extensions(ws2Extensions);

    } catch (InvalidRegistration ir) {
      LOG.error(ir.getMessage(), ir);
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (OperationFailed of) {
      LOG.error(of.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new WS1OperationFailed(wsrpe.getMessage(), new WS1OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new WS1OperationFailed(e.getMessage(), new WS1OperationFailedFault());
    }

  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType#register(java.lang.String  consumerName ,)java.lang.String  consumerAgent ,)boolean  methodGetSupported ,)java.util.List<java.lang.String>  consumerModes ,)java.util.List<java.lang.String>  consumerWindowStates ,)java.util.List<java.lang.String>  consumerUserScopes ,)java.util.List<java.lang.String>  customUserProfileData ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Property>  registrationProperties ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions ,)java.lang.String  registrationHandle ,)byte[]  registrationState )*
   */
  public void register(java.lang.String consumerName,
                       java.lang.String consumerAgent,
                       boolean methodGetSupported,
                       java.util.List<java.lang.String> consumerModes,
                       java.util.List<java.lang.String> consumerWindowStates,
                       java.util.List<java.lang.String> consumerUserScopes,
                       java.util.List<java.lang.String> customUserProfileData, //TODO no exist in the 2nd spec
                       java.util.List<org.exoplatform.services.wsrp1.type.WS1Property> registrationProperties,
                       javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions,
                       javax.xml.ws.Holder<java.lang.String> registrationHandle,
                       javax.xml.ws.Holder<byte[]> registrationState) throws WS1MissingParameters,
                                                                     WS1OperationFailed {
    LOG.info("Executing operation register");
    System.out.println(consumerName);
    System.out.println(consumerAgent);
    System.out.println(methodGetSupported);
    System.out.println(consumerModes);
    System.out.println(consumerWindowStates);
    System.out.println(consumerUserScopes);
    System.out.println(customUserProfileData);
    System.out.println(registrationProperties);
    System.out.println(extensions.value);
    try {

      RegistrationData registrationData = new RegistrationData();
      registrationData.setConsumerName(consumerName);
      registrationData.setConsumerAgent(consumerAgent);
      registrationData.setMethodGetSupported(methodGetSupported);
      registrationData.setResourceList(null);
      registrationData.getConsumerModes().addAll(consumerModes);
      registrationData.getConsumerWindowStates().addAll(consumerWindowStates);
      registrationData.getConsumerUserScopes().addAll(consumerUserScopes);
      registrationData.getRegistrationProperties()
                      .addAll(WSRPTypesTransformer.getWS2Properties(registrationProperties));
      registrationData.getExtensions()
                      .addAll(WSRPTypesTransformer.getWS2Extensions(extensions.value));

      RegistrationContext response = registrationOperationsInterface.register(registrationData,
                                                                              null,
                                                                              null);

      extensions.value = WSRPTypesTransformer.getWS1Extensions(response.getExtensions());
      registrationHandle.value = response.getRegistrationHandle();
      registrationState.value = response.getRegistrationState();

    } catch (MissingParameters ir) {
      LOG.error(ir.getMessage(), ir);
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (OperationFailed of) {
      LOG.error(of.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new WS1OperationFailed(wsrpe.getMessage(), new WS1OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new WS1OperationFailed(e.getMessage(), new WS1OperationFailedFault());
    }

  }

}
