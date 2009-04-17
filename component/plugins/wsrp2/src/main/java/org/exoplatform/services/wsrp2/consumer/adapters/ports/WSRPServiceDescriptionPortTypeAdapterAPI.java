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
package org.exoplatform.services.wsrp2.consumer.adapters.ports;

import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.ServiceDescription;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public interface WSRPServiceDescriptionPortTypeAdapterAPI {

  /**
   * Gets the service description.
   * 
   * @param serviceDescription the service description
   * 
   * @return the service description
   * 
   * @throws ResourceSuspended the resource suspended
   * @throws InvalidRegistration the invalid registration
   * @throws ModifyRegistrationRequired the modify registration required
   * @throws OperationFailed the operation failed
   */
  public ServiceDescription getServiceDescription(GetServiceDescription serviceDescription) throws ResourceSuspended,
                                                                                           InvalidRegistration,
                                                                                           ModifyRegistrationRequired,
                                                                                           OperationFailed;
}
