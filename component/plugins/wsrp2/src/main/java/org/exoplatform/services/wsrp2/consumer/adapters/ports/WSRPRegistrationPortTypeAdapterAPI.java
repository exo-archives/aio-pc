/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.services.wsrp2.consumer.adapters.ports;

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
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.SetRegistrationLifetime;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public interface WSRPRegistrationPortTypeAdapterAPI {

  public RegistrationContext register(Register register) throws OperationNotSupported,
                                                        MissingParameters,
                                                        OperationFailed;

  public Extension deregister(Deregister deregister) throws OperationNotSupported,
                                                    ResourceSuspended,
                                                    InvalidRegistration,
                                                    OperationFailed;

  public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws OperationNotSupported,
                                                                                    ResourceSuspended,
                                                                                    InvalidRegistration,
                                                                                    MissingParameters,
                                                                                    OperationFailed;

  public Lifetime getRegistrationLifetime(GetRegistrationLifetime getRegistrationLifetime) throws OperationNotSupported,
                                                                                          AccessDenied,
                                                                                          ResourceSuspended,
                                                                                          InvalidRegistration,
                                                                                          InvalidHandle,
                                                                                          ModifyRegistrationRequired,
                                                                                          OperationFailed;

  public Lifetime setRegistrationLifetime(SetRegistrationLifetime setRegistrationLifetime) throws OperationNotSupported,
                                                                                          AccessDenied,
                                                                                          ResourceSuspended,
                                                                                          InvalidRegistration,
                                                                                          InvalidHandle,
                                                                                          ModifyRegistrationRequired,
                                                                                          OperationFailed;
}
