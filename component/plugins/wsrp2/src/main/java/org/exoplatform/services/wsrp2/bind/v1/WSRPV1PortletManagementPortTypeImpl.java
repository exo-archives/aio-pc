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

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
import org.exoplatform.services.wsrp1.intf.WS1AccessDenied;
import org.exoplatform.services.wsrp1.intf.WS1InconsistentParameters;
import org.exoplatform.services.wsrp1.intf.WS1InvalidHandle;
import org.exoplatform.services.wsrp1.intf.WS1InvalidRegistration;
import org.exoplatform.services.wsrp1.intf.WS1InvalidUserCategory;
import org.exoplatform.services.wsrp1.intf.WS1MissingParameters;
import org.exoplatform.services.wsrp1.intf.WS1OperationFailed;
import org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType;
import org.exoplatform.services.wsrp1.type.WS1AccessDeniedFault;
import org.exoplatform.services.wsrp1.type.WS1InconsistentParametersFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidHandleFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidRegistrationFault;
import org.exoplatform.services.wsrp1.type.WS1InvalidUserCategoryFault;
import org.exoplatform.services.wsrp1.type.WS1MissingParametersFault;
import org.exoplatform.services.wsrp1.type.WS1OperationFailedFault;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InconsistentParameters;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.InvalidUserCategory;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 */

@javax.jws.WebService(name = "WSRPV1PortletManagementPortType", serviceName = "WSRPService1", portName = "WSRP_v1_PortletManagement_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v1:wsdl", wsdlLocation = "/WEB-INF/wsdl1/wsrp_service.wsdl", endpointInterface = "org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType")
public class WSRPV1PortletManagementPortTypeImpl implements WSRPV1PortletManagementPortType,
    AbstractSingletonWebService {

  private static final Log                     LOG = ExoLogger.getLogger(WSRPV1PortletManagementPortTypeImpl.class.getName());

  private PortletManagementOperationsInterface portletManagementOperationsInterface;

  public WSRPV1PortletManagementPortTypeImpl(PortletManagementOperationsInterface portletManagementOperationsInterface) {
    this.portletManagementOperationsInterface = portletManagementOperationsInterface;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType#destroyPortlets(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)java.util.List<java.lang.String>  portletHandles ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1DestroyFailed>  destroyFailed ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void destroyPortlets(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                              java.util.List<java.lang.String> portletHandles,
                              javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1DestroyFailed>> destroyFailed,
                              javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1InvalidRegistration,
                                                                                                                               WS1MissingParameters,
                                                                                                                               WS1InconsistentParameters,
                                                                                                                               WS1OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation destroyPortlets");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(portletHandles);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);

      DestroyPortletsResponse response = portletManagementOperationsInterface.destroyPortlets(ws2registrationContext,
                                                                                              portletHandles,
                                                                                              null);

      destroyFailed.value = WSRPTypesTransformer.getWS1DestroyFailed(response.getFailedPortlets());
      extensions.value = WSRPTypesTransformer.getWS1Extensions(response.getExtensions());

    } catch (InvalidRegistration ir) {
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (MissingParameters ir) {
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (InconsistentParameters ir) {
      throw new WS1InconsistentParameters(ir.getMessage(), new WS1InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + wsrpe.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }

  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType#setPortletProperties(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp1.type.WS1PortletContext  portletContext ,)org.exoplatform.services.wsrp1.type.WS1UserContext  userContext ,)org.exoplatform.services.wsrp1.type.WS1PropertyList  propertyList ,)java.lang.String  portletHandle ,)byte[]  portletState ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void setPortletProperties(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                                   org.exoplatform.services.wsrp1.type.WS1PortletContext portletContext,
                                   org.exoplatform.services.wsrp1.type.WS1UserContext userContext,
                                   org.exoplatform.services.wsrp1.type.WS1PropertyList propertyList,
                                   javax.xml.ws.Holder<java.lang.String> portletHandle,
                                   javax.xml.ws.Holder<byte[]> portletState,
                                   javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1InvalidRegistration,
                                                                                                                                    WS1MissingParameters,
                                                                                                                                    WS1InvalidUserCategory,
                                                                                                                                    WS1AccessDenied,
                                                                                                                                    WS1InvalidHandle,
                                                                                                                                    WS1InconsistentParameters,
                                                                                                                                    WS1OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation setPortletProperties");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(portletContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    if (LOG.isDebugEnabled())
      LOG.debug(propertyList);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);
      PortletContext ws2portletContext = WSRPTypesTransformer.getWS2PortletContext(portletContext);
      UserContext ws2userContext = WSRPTypesTransformer.getWS2UserContext(userContext);
      PropertyList ws2propertyList = WSRPTypesTransformer.getWS2PropertyList(propertyList);

      PortletContext response = portletManagementOperationsInterface.setPortletProperties(ws2registrationContext,
                                                                                          ws2portletContext,
                                                                                          ws2userContext,
                                                                                          ws2propertyList);

      portletHandle.value = response.getPortletHandle();
      portletState.value = response.getPortletState();
      extensions.value = WSRPTypesTransformer.getWS1Extensions(response.getExtensions());

    } catch (InvalidRegistration ir) {
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (MissingParameters ir) {
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (InvalidUserCategory ir) {
      throw new WS1InvalidUserCategory(ir.getMessage(), new WS1InvalidUserCategoryFault());
    } catch (AccessDenied ir) {
      throw new WS1AccessDenied(ir.getMessage(), new WS1AccessDeniedFault());
    } catch (InvalidHandle ir) {
      throw new WS1InvalidHandle(ir.getMessage(), new WS1InvalidHandleFault());
    } catch (InconsistentParameters ad) {
      throw new WS1InconsistentParameters(ad.getMessage(), new WS1InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + wsrpe.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }

  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType#clonePortlet(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp1.type.WS1PortletContext  portletContext ,)org.exoplatform.services.wsrp1.type.WS1UserContext  userContext ,)java.lang.String  portletHandle ,)byte[]  portletState ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void clonePortlet(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                           org.exoplatform.services.wsrp1.type.WS1PortletContext portletContext,
                           org.exoplatform.services.wsrp1.type.WS1UserContext userContext,
                           javax.xml.ws.Holder<java.lang.String> portletHandle,
                           javax.xml.ws.Holder<byte[]> portletState,
                           javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1InvalidRegistration,
                                                                                                                            WS1MissingParameters,
                                                                                                                            WS1InvalidUserCategory,
                                                                                                                            WS1AccessDenied,
                                                                                                                            WS1InvalidHandle,
                                                                                                                            WS1InconsistentParameters,
                                                                                                                            WS1OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation clonePortlet");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(portletContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);
      PortletContext ws2portletContext = WSRPTypesTransformer.getWS2PortletContext(portletContext);
      UserContext ws2userContext = WSRPTypesTransformer.getWS2UserContext(userContext);

      PortletContext response = portletManagementOperationsInterface.clonePortlet(ws2registrationContext,
                                                                                  ws2portletContext,
                                                                                  ws2userContext,
                                                                                  null);

      portletHandle.value = response.getPortletHandle();
      portletState.value = response.getPortletState();
      extensions.value = WSRPTypesTransformer.getWS1Extensions(response.getExtensions());

    } catch (InvalidRegistration ir) {
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (MissingParameters ir) {
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (InvalidUserCategory ir) {
      throw new WS1InvalidUserCategory(ir.getMessage(), new WS1InvalidUserCategoryFault());
    } catch (AccessDenied ir) {
      throw new WS1AccessDenied(ir.getMessage(), new WS1AccessDeniedFault());
    } catch (InvalidHandle ir) {
      throw new WS1InvalidHandle(ir.getMessage(), new WS1InvalidHandleFault());
    } catch (InconsistentParameters ad) {
      throw new WS1InconsistentParameters(ad.getMessage(), new WS1InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault(), of);
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + wsrpe.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType#getPortletDescription(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp1.type.WS1PortletContext  portletContext ,)org.exoplatform.services.wsrp1.type.WS1UserContext  userContext ,)java.util.List<java.lang.String>  desiredLocales ,)org.exoplatform.services.wsrp1.type.WS1PortletDescription  portletDescription ,)org.exoplatform.services.wsrp1.type.WS1ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void getPortletDescription(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                                    org.exoplatform.services.wsrp1.type.WS1PortletContext portletContext,
                                    org.exoplatform.services.wsrp1.type.WS1UserContext userContext,
                                    java.util.List<java.lang.String> desiredLocales,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1PortletDescription> portletDescription,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ResourceList> resourceList,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1InvalidRegistration,
                                                                                                                                     WS1MissingParameters,
                                                                                                                                     WS1InvalidUserCategory,
                                                                                                                                     WS1AccessDenied,
                                                                                                                                     WS1InvalidHandle,
                                                                                                                                     WS1InconsistentParameters,
                                                                                                                                     WS1OperationFailed {

    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation getPortletDescription");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(portletContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    if (LOG.isDebugEnabled())
      LOG.debug(desiredLocales);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);
      PortletContext ws2portletContext = WSRPTypesTransformer.getWS2PortletContext(portletContext);
      UserContext ws2userContext = WSRPTypesTransformer.getWS2UserContext(userContext);

      PortletDescriptionResponse response = portletManagementOperationsInterface.getPortletDescription(ws2registrationContext,
                                                                                                       ws2portletContext,
                                                                                                       ws2userContext,
                                                                                                       desiredLocales);

      portletDescription.value = WSRPTypesTransformer.getWS1PortletDescription(response.getPortletDescription());
      resourceList.value = WSRPTypesTransformer.getWS1ResourceList(response.getResourceList());
      extensions.value = WSRPTypesTransformer.getWS1Extensions(response.getExtensions());

    } catch (InvalidRegistration ir) {
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (MissingParameters ir) {
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (InvalidUserCategory ir) {
      throw new WS1InvalidUserCategory(ir.getMessage(), new WS1InvalidUserCategoryFault());
    } catch (AccessDenied ir) {
      throw new WS1AccessDenied(ir.getMessage(), new WS1AccessDeniedFault());
    } catch (InvalidHandle ir) {
      throw new WS1InvalidHandle(ir.getMessage(), new WS1InvalidHandleFault());
    } catch (InconsistentParameters ad) {
      throw new WS1InconsistentParameters(ad.getMessage(), new WS1InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + wsrpe.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType#getPortletPropertyDescription(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp1.type.WS1PortletContext  portletContext ,)org.exoplatform.services.wsrp1.type.WS1UserContext  userContext ,)java.util.List<java.lang.String>  desiredLocales ,)org.exoplatform.services.wsrp1.type.WS1ModelDescription  modelDescription ,)org.exoplatform.services.wsrp1.type.WS1ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void getPortletPropertyDescription(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                                            org.exoplatform.services.wsrp1.type.WS1PortletContext portletContext,
                                            org.exoplatform.services.wsrp1.type.WS1UserContext userContext,
                                            java.util.List<java.lang.String> desiredLocales,
                                            javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ModelDescription> modelDescription,
                                            javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ResourceList> resourceList,
                                            javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1InvalidRegistration,
                                                                                                                                             WS1MissingParameters,
                                                                                                                                             WS1InvalidUserCategory,
                                                                                                                                             WS1AccessDenied,
                                                                                                                                             WS1InvalidHandle,
                                                                                                                                             WS1InconsistentParameters,
                                                                                                                                             WS1OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation getPortletPropertyDescription");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(portletContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    if (LOG.isDebugEnabled())
      LOG.debug(desiredLocales);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);
      PortletContext ws2portletContext = WSRPTypesTransformer.getWS2PortletContext(portletContext);
      UserContext ws2userContext = WSRPTypesTransformer.getWS2UserContext(userContext);

      PortletPropertyDescriptionResponse response = portletManagementOperationsInterface.getPortletPropertyDescription(ws2registrationContext,
                                                                                                                       ws2portletContext,
                                                                                                                       ws2userContext,
                                                                                                                       desiredLocales);

      modelDescription.value = WSRPTypesTransformer.getWS1ModelDescription(response.getModelDescription());
      resourceList.value = WSRPTypesTransformer.getWS1ResourceList(response.getResourceList());
      extensions.value = WSRPTypesTransformer.getWS1Extensions(response.getExtensions());

    } catch (InvalidRegistration ir) {
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (MissingParameters ir) {
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (InvalidUserCategory ir) {
      throw new WS1InvalidUserCategory(ir.getMessage(), new WS1InvalidUserCategoryFault());
    } catch (AccessDenied ir) {
      throw new WS1AccessDenied(ir.getMessage(), new WS1AccessDeniedFault());
    } catch (InvalidHandle ir) {
      throw new WS1InvalidHandle(ir.getMessage(), new WS1InvalidHandleFault());
    } catch (InconsistentParameters ad) {
      throw new WS1InconsistentParameters(ad.getMessage(), new WS1InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + wsrpe.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType#getPortletProperties(org.exoplatform.services.wsrp1.type.WS1RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp1.type.WS1PortletContext  portletContext ,)org.exoplatform.services.wsrp1.type.WS1UserContext  userContext ,)java.util.List<java.lang.String>  names ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Property>  properties ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1ResetProperty>  resetProperties ,)java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>  extensions )*
   */
  public void getPortletProperties(org.exoplatform.services.wsrp1.type.WS1RegistrationContext registrationContext,
                                   org.exoplatform.services.wsrp1.type.WS1PortletContext portletContext,
                                   org.exoplatform.services.wsrp1.type.WS1UserContext userContext,
                                   java.util.List<java.lang.String> names,
                                   javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Property>> properties,
                                   javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1ResetProperty>> resetProperties,
                                   javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> extensions) throws WS1InvalidRegistration,
                                                                                                                                    WS1MissingParameters,
                                                                                                                                    WS1InvalidUserCategory,
                                                                                                                                    WS1AccessDenied,
                                                                                                                                    WS1InvalidHandle,
                                                                                                                                    WS1InconsistentParameters,
                                                                                                                                    WS1OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation getPortletProperties");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(portletContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    if (LOG.isDebugEnabled())
      LOG.debug(names);
    try {

      RegistrationContext ws2registrationContext = WSRPTypesTransformer.getWS2RegistrationContext(registrationContext);
      PortletContext ws2portletContext = WSRPTypesTransformer.getWS2PortletContext(portletContext);
      UserContext ws2userContext = WSRPTypesTransformer.getWS2UserContext(userContext);

      PropertyList response = portletManagementOperationsInterface.getPortletProperties(ws2registrationContext,
                                                                                        ws2portletContext,
                                                                                        ws2userContext,
                                                                                        names);

      properties.value = WSRPTypesTransformer.getWS1Properties(response.getProperties());
      resetProperties.value = WSRPTypesTransformer.getWS1ResetProperties(response.getResetProperties());
      extensions.value = WSRPTypesTransformer.getWS1Extensions(response.getExtensions());

    } catch (InvalidRegistration ir) {
      throw new WS1InvalidRegistration(ir.getMessage(), new WS1InvalidRegistrationFault());
    } catch (MissingParameters ir) {
      throw new WS1MissingParameters(ir.getMessage(), new WS1MissingParametersFault());
    } catch (InvalidUserCategory ir) {
      throw new WS1InvalidUserCategory(ir.getMessage(), new WS1InvalidUserCategoryFault());
    } catch (AccessDenied ir) {
      throw new WS1AccessDenied(ir.getMessage(), new WS1AccessDeniedFault());
    } catch (InvalidHandle ir) {
      throw new WS1InvalidHandle(ir.getMessage(), new WS1InvalidHandleFault());
    } catch (InconsistentParameters ad) {
      throw new WS1InconsistentParameters(ad.getMessage(), new WS1InconsistentParametersFault());
    } catch (OperationFailed of) {
      throw new WS1OperationFailed(of.getMessage(), new WS1OperationFailedFault());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new WS1OperationFailed("Error '" + wsrpe.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + wsrpe.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   wsrpe);
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      throw new WS1OperationFailed("Error '" + e.toString()
                                       + "'on a PRODUCER side with exception at '"
                                       + e.getStackTrace()[0].toString() + "'",
                                   new WS1OperationFailedFault(),
                                   e);
    }

  }
}
