/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.services.wsrp2.bind.v1;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
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

@javax.jws.WebService(name = "WSRPV1RegistrationPortType", serviceName = "WSRPService1", portName = "WSRP_v1_Registration_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v1:wsdl", wsdlLocation = "/WEB-INF/wsdl1/wsrp_service.wsdl", endpointInterface = "org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType")
public class WSRPV1RegistrationPortTypeImpl implements WSRPV1RegistrationPortType,
    AbstractSingletonWebService {

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
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(registrationData);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);
      RegistrationData ws2registrationData = WSRPTypesTransformer.getWS2RegistrationData(registrationData);

      RegistrationState response = registrationOperationsInterface.modifyRegistration(ws2registrationContext,
                                                                                      ws2registrationData,
                                                                                      null);

      registrationState.value = response.getRegistrationState();
      extensions.value = WSRPTypesTransformer.getWS1Extensions(response.getExtensions());

    } catch (InvalidRegistration ir) {
      //LOG.errorir.getMessage(), ir);
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (MissingParameters ir) {
      //LOG.errorir.getMessage(), ir);
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (OperationFailed of) {
      //LOG.errorof.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      //LOG.errorwsrpe.getMessage(), wsrpe);
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                   + "'on a PRODUCER side with exception at '"
                                   + wsrpe.getStackTrace()[0].toString() + "'",
                               new WS1OperationFailedFault(),
                               wsrpe);
    } catch (Exception e) {
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
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
    if (LOG.isDebugEnabled())
      LOG.debug(registrationHandle);
    if (LOG.isDebugEnabled())
      LOG.debug(registrationState);
    if (LOG.isDebugEnabled())
      LOG.debug(extensions.value);
    try {

      RegistrationContext ws2registrationContext = new RegistrationContext();
      ws2registrationContext.setRegistrationHandle(registrationHandle);
      ws2registrationContext.setRegistrationState(registrationState);

      ReturnAny response = registrationOperationsInterface.deregister(ws2registrationContext, null);

      List<Extension> ws2Extensions = new ArrayList<Extension>();
      if (response.getExtensions() != null)
        ws2Extensions.addAll(response.getExtensions());
      extensions.value = WSRPTypesTransformer.getWS1Extensions(ws2Extensions);

    } catch (InvalidRegistration ir) {
      //LOG.errorir.getMessage(), ir);
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (OperationFailed of) {
      //LOG.errorof.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      //LOG.errorwsrpe.getMessage(), wsrpe);
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                   + "'on a PRODUCER side with exception at '"
                                   + wsrpe.getStackTrace()[0].toString() + "'",
                               new WS1OperationFailedFault(),
                               wsrpe);
    } catch (Exception e) {
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
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
    if (LOG.isDebugEnabled())
      LOG.debug(consumerName);
    if (LOG.isDebugEnabled())
      LOG.debug(consumerAgent);
    if (LOG.isDebugEnabled())
      LOG.debug(methodGetSupported);
    if (LOG.isDebugEnabled())
      LOG.debug(consumerModes);
    if (LOG.isDebugEnabled())
      LOG.debug(consumerWindowStates);
    if (LOG.isDebugEnabled())
      LOG.debug(consumerUserScopes);
    if (LOG.isDebugEnabled())
      LOG.debug(customUserProfileData);
    if (LOG.isDebugEnabled())
      LOG.debug(registrationProperties);
    if (LOG.isDebugEnabled())
      LOG.debug(extensions.value);
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
      //LOG.errorir.getMessage(), ir);
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (OperationFailed of) {
      //LOG.errorof.getMessage(), of);
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      //LOG.errorwsrpe.getMessage(), wsrpe);
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                   + "'on a PRODUCER side with exception at '"
                                   + wsrpe.getStackTrace()[0].toString() + "'",
                               new WS1OperationFailedFault(),
                               wsrpe);
    } catch (Exception e) {
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }

  }

}
