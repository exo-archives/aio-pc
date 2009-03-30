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

package org.exoplatform.services.wsrp2.bind.v2;

import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InconsistentParameters;
import org.exoplatform.services.wsrp2.intf.InvalidCookie;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.InvalidSession;
import org.exoplatform.services.wsrp2.intf.InvalidUserCategory;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.intf.UnsupportedLocale;
import org.exoplatform.services.wsrp2.intf.UnsupportedMimeType;
import org.exoplatform.services.wsrp2.intf.UnsupportedMode;
import org.exoplatform.services.wsrp2.intf.UnsupportedWindowState;
import org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType;
import org.exoplatform.services.wsrp2.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;

/**
 */

@javax.jws.WebService(name = "WSRPV2RegistrationPortType", serviceName = "WSRPService2", portName = "WSRP_v2_Registration_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v2:wsdl", wsdlLocation = "/WEB-INF/wsdl2/wsrp-service.wsdl", endpointInterface = "org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType")
public class WSRPV2RegistrationPortTypeImpl implements WSRPV2RegistrationPortType,
    AbstractSingletonWebService {

  private static final Log                LOG = ExoLogger.getLogger(WSRPV2RegistrationPortTypeImpl.class.getName());

  private RegistrationOperationsInterface registrationOperationsInterface;

  public WSRPV2RegistrationPortTypeImpl(RegistrationOperationsInterface registrationOperationsInterface) {
    this.registrationOperationsInterface = registrationOperationsInterface;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType#register(org.exoplatform.services.wsrp2.type.Register  register )*
   */
  public org.exoplatform.services.wsrp2.type.RegistrationContext register(org.exoplatform.services.wsrp2.type.Register register) throws OperationNotSupported,
                                                                                                                                MissingParameters,
                                                                                                                                OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation register");
    if (LOG.isDebugEnabled())
      LOG.debug(register);
    try {
      RegistrationContext response = registrationOperationsInterface.register(register.getRegistrationData(),
                                                                              register.getUserContext(),
                                                                              register.getLifetime());
      org.exoplatform.services.wsrp2.type.RegistrationContext _return = response;
      return _return;
   } catch (MissingParameters mp) {
     throw new MissingParameters(mp.getMessage());
   } catch (OperationFailed of) {
     throw new OperationFailed(of.getMessage());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                + "'on a PRODUCER side with exception at '"
                                + wsrpe.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
         e.printStackTrace();
      throw new OperationFailed("Error '" + e.toString()
                                + "'on a PRODUCER side with exception at '"
                                +  e.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2RegistrationPortType#deregister(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext )*
   */
  public List<org.exoplatform.services.wsrp2.type.Extension> deregister(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                                                        org.exoplatform.services.wsrp2.type.UserContext userContext) throws OperationNotSupported,
                                                                                                                                    ResourceSuspended,
                                                                                                                                    InvalidRegistration,
                                                                                                                                    OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation deregister");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
    try {
      ReturnAny response = registrationOperationsInterface.deregister(registrationContext,
                                                                      userContext);
      List<org.exoplatform.services.wsrp2.type.Extension> _return = response.getExtensions();
      return _return;
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage());
   } catch (InvalidRegistration ir) {
     throw new InvalidRegistration(ir.getMessage());
   } catch (OperationFailed of) {
     throw new OperationFailed(of.getMessage());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                + "'on a PRODUCER side with exception at '"
                                + wsrpe.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
         e.printStackTrace();
      throw new OperationFailed("Error '" +  e.toString()
                                + "'on a PRODUCER side with exception at '"
                                +  e.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    }
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
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation getRegistrationLifetime");
    if (LOG.isDebugEnabled())
      LOG.debug(getRegistrationLifetime);
    try {
      Lifetime response = registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime.getRegistrationContext(),
                                                                                  getRegistrationLifetime.getUserContext());
      org.exoplatform.services.wsrp2.type.Lifetime _return = response;
      return _return;
    } catch (AccessDenied ad){
      throw new AccessDenied(ad.getMessage());
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage());
   } catch (InvalidRegistration ir) {
     throw new InvalidRegistration(ir.getMessage());
   } catch (InvalidHandle ih) {
     throw new InvalidHandle(ih.getMessage());
   } catch (ModifyRegistrationRequired mrr) {
     throw new ModifyRegistrationRequired(mrr.getMessage());
   } catch (OperationFailed of) {
     throw new OperationFailed(of.getMessage());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                + "'on a PRODUCER side with exception at '"
                                + wsrpe.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
         e.printStackTrace();
      throw new OperationFailed("Error '" +  e.toString()
                                + "'on a PRODUCER side with exception at '"
                                +  e.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    }
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
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation setRegistrationLifetime");
    if (LOG.isDebugEnabled())
      LOG.debug(setRegistrationLifetime);
    try {
      Lifetime response = registrationOperationsInterface.setRegistrationLifetime(setRegistrationLifetime.getRegistrationContext(),
                                                                                  setRegistrationLifetime.getUserContext(),
                                                                                  setRegistrationLifetime.getLifetime());
      org.exoplatform.services.wsrp2.type.Lifetime _return = response;
      return _return;
    } catch (AccessDenied ad){
      throw new AccessDenied(ad.getMessage());
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage());
   } catch (InvalidRegistration ir) {
     throw new InvalidRegistration(ir.getMessage());
   } catch (InvalidHandle ih) {
     throw new InvalidHandle(ih.getMessage());
   } catch (ModifyRegistrationRequired mrr) {
     throw new ModifyRegistrationRequired(mrr.getMessage());
   } catch (OperationFailed of) {
     throw new OperationFailed(of.getMessage());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                + "'on a PRODUCER side with exception at '"
                                + wsrpe.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
         e.printStackTrace();
      throw new OperationFailed("Error '" +  e.toString()
                                + "'on a PRODUCER side with exception at '"
                                +  e.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    }
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
    if (LOG.isDebugEnabled())
      LOG.debug("Executing operation modifyRegistration");
    if (LOG.isDebugEnabled())
      LOG.debug(registrationContext);
    if (LOG.isDebugEnabled())
      LOG.debug(registrationData);
    if (LOG.isDebugEnabled())
      LOG.debug(userContext);
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
    } catch (ResourceSuspended rs) {
      throw new ResourceSuspended(rs.getMessage());
   } catch (InvalidRegistration ir) {
     throw new InvalidRegistration(ir.getMessage());
   } catch (MissingParameters mp) {
     throw new MissingParameters(mp.getMessage());
   } catch (OperationFailed of) {
     throw new OperationFailed(of.getMessage());
    } catch (WSRPException wsrpe) {
      if (LOG.isDebugEnabled())
        wsrpe.printStackTrace();
      throw new OperationFailed("Error '" + wsrpe.toString()
                                + "'on a PRODUCER side with exception at '"
                                + wsrpe.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    } catch (Exception e) {
      if (LOG.isDebugEnabled())
         e.printStackTrace();
      throw new OperationFailed("Error '" +  e.toString()
                                + "'on a PRODUCER side with exception at '"
                                +  e.getStackTrace()[0].toString() + "'",
                            new OperationFailed());
    }
  }
}
