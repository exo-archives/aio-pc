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
package org.exoplatform.services.wsrp2.consumer.adapters.ports.v1;

import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp1.intf.WS1InvalidRegistration;
import org.exoplatform.services.wsrp1.intf.WS1MissingParameters;
import org.exoplatform.services.wsrp1.intf.WS1OperationFailed;
import org.exoplatform.services.wsrp1.intf.WSRPV1RegistrationPortType;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPRegistrationPortTypeAdapterAPI;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.GetRegistrationLifetime;
import org.exoplatform.services.wsrp2.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.MissingParametersFault;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.SetRegistrationLifetime;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public class WSRPV1RegistrationPortTypeAdapter implements WSRPRegistrationPortTypeAdapterAPI {

  private WSRPV1RegistrationPortType registrationPort;

  private static final Log           LOG = ExoLogger.getLogger(WSRPV1RegistrationPortTypeAdapter.class);

  public WSRPV1RegistrationPortTypeAdapter(WSRPV1RegistrationPortType registrationPort) {
    this.registrationPort = registrationPort;
  }

  public RegistrationContext register(Register register) throws OperationNotSupported,
                                                        MissingParameters,
                                                        OperationFailed {

    if (LOG.isDebugEnabled())
      LOG.debug("Invoking register...");
    java.lang.String _register_consumerName = register.getRegistrationData().getConsumerName();
    java.lang.String _register_consumerAgent = register.getRegistrationData().getConsumerAgent();
    boolean _register_methodGetSupported = register.getRegistrationData().isMethodGetSupported();
    java.util.List<java.lang.String> _register_consumerModes = register.getRegistrationData()
                                                                       .getConsumerModes();
    java.util.List<java.lang.String> _register_consumerWindowStates = register.getRegistrationData()
                                                                              .getConsumerWindowStates();
    java.util.List<java.lang.String> _register_consumerUserScopes = register.getRegistrationData()
                                                                            .getConsumerUserScopes();
    java.util.List<java.lang.String> _register_customUserProfileData = null;
    java.util.List<org.exoplatform.services.wsrp1.type.WS1Property> _register_registrationProperties = WSRPTypesTransformer.getWS1Properties(register.getRegistrationData()
                                                                                                                                                     .getRegistrationProperties());
    java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> _register_extensionsVal = WSRPTypesTransformer.getWS1Extensions(register.getRegistrationData()
                                                                                                                                             .getExtensions());
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> _register_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>>(_register_extensionsVal);
    javax.xml.ws.Holder<java.lang.String> _register_registrationHandle = new javax.xml.ws.Holder<java.lang.String>();
    javax.xml.ws.Holder<byte[]> _register_registrationState = new javax.xml.ws.Holder<byte[]>();
    try {
      registrationPort.register(_register_consumerName,
                                _register_consumerAgent,
                                _register_methodGetSupported,
                                _register_consumerModes,
                                _register_consumerWindowStates,
                                _register_consumerUserScopes,
                                _register_customUserProfileData,
                                _register_registrationProperties,
                                _register_extensions,
                                _register_registrationHandle,
                                _register_registrationState);

      if (LOG.isDebugEnabled())
        LOG.debug("register._register_extensions=" + _register_extensions.value);
      if (LOG.isDebugEnabled())
        LOG.debug("register._register_registrationHandle=" + _register_registrationHandle.value);
      if (LOG.isDebugEnabled())
        LOG.debug("register._register_registrationState=" + _register_registrationState.value);

    } catch (WS1MissingParameters ir) {
      throw new MissingParameters(ir.getMessage(), new MissingParametersFault());
    } catch (WS1OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    }

    RegistrationContext registrationContext = new RegistrationContext();
    registrationContext.setRegistrationHandle(_register_registrationHandle.value);
    registrationContext.setRegistrationState(_register_registrationState.value);
    return registrationContext;
  }

  public List<Extension> deregister(Deregister deregister) throws OperationNotSupported,
                                                          ResourceSuspended,
                                                          InvalidRegistration,
                                                          OperationFailed {

    if (LOG.isDebugEnabled())
      LOG.debug("Invoking deregister...");
    java.lang.String _deregister_registrationHandle = deregister.getRegistrationContext()
                                                                .getRegistrationHandle();
    byte[] _deregister_registrationState = deregister.getRegistrationContext()
                                                     .getRegistrationState();
    java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension> _deregister_extensionsVal = WSRPTypesTransformer.getWS1Extensions(deregister.getRegistrationContext()
                                                                                                                                                 .getExtensions());
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> _deregister_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>>(_deregister_extensionsVal);
    try {
      registrationPort.deregister(_deregister_registrationHandle,
                                  _deregister_registrationState,
                                  _deregister_extensions);

      if (LOG.isDebugEnabled())
        LOG.debug("deregister._deregister_extensions=" + _deregister_extensions.value);

    } catch (WS1InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (WS1OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    }

    List<Extension> extension = null;
    if (!_deregister_extensions.value.isEmpty()) {
      extension = WSRPTypesTransformer.getWS2Extensions(_deregister_extensions.value); // in a WSRP2 we have to return just one Extension
    }
    return extension;
  }

  public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws OperationNotSupported,
                                                                                    ResourceSuspended,
                                                                                    InvalidRegistration,
                                                                                    MissingParameters,
                                                                                    OperationFailed {

    if (LOG.isDebugEnabled())
      LOG.debug("Invoking modifyRegistration...");
    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _modifyRegistration_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(modifyRegistration.getRegistrationContext());
    org.exoplatform.services.wsrp1.type.WS1RegistrationData _modifyRegistration_registrationData = WSRPTypesTransformer.getWS1RegistrationData(modifyRegistration.getRegistrationData());
    javax.xml.ws.Holder<byte[]> _modifyRegistration_registrationState = new javax.xml.ws.Holder<byte[]>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> _modifyRegistration_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>>();
    try {
      registrationPort.modifyRegistration(_modifyRegistration_registrationContext,
                                          _modifyRegistration_registrationData,
                                          _modifyRegistration_registrationState,
                                          _modifyRegistration_extensions);

      if (LOG.isDebugEnabled())
        LOG.debug("modifyRegistration._modifyRegistration_registrationState="
            + _modifyRegistration_registrationState.value);
      if (LOG.isDebugEnabled())
        LOG.debug("modifyRegistration._modifyRegistration_extensions="
            + _modifyRegistration_extensions.value);

    } catch (WS1InvalidRegistration ir) {
      throw new InvalidRegistration(ir.getMessage(), new InvalidRegistrationFault());
    } catch (WS1MissingParameters ir) {
      throw new MissingParameters(ir.getMessage(), new MissingParametersFault());
    } catch (WS1OperationFailed of) {
      throw new OperationFailed(of.getMessage(), new OperationFailedFault());
    }

    RegistrationState registrationState = new RegistrationState();
    registrationState.setRegistrationState(_modifyRegistration_registrationState.value);
    if (_modifyRegistration_extensions.value != null)
      registrationState.getExtensions()
                       .addAll(WSRPTypesTransformer.getWS2Extensions(_modifyRegistration_extensions.value));
    return registrationState;
  }

  public Lifetime getRegistrationLifetime(GetRegistrationLifetime getRegistrationLifetime) throws OperationNotSupported,
                                                                                          AccessDenied,
                                                                                          ResourceSuspended,
                                                                                          InvalidRegistration,
                                                                                          InvalidHandle,
                                                                                          ModifyRegistrationRequired,
                                                                                          OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Invoking getRegistrationLifetime...");
    // wsrp1 doesn't have this operation
    return null;
  }

  public Lifetime setRegistrationLifetime(SetRegistrationLifetime setRegistrationLifetime) throws OperationNotSupported,
                                                                                          AccessDenied,
                                                                                          ResourceSuspended,
                                                                                          InvalidRegistration,
                                                                                          InvalidHandle,
                                                                                          ModifyRegistrationRequired,
                                                                                          OperationFailed {
    if (LOG.isDebugEnabled())
      LOG.debug("Invoking setRegistrationLifetime...");
    // wsrp1 doesn't have this operation
    return null;
  }

}
