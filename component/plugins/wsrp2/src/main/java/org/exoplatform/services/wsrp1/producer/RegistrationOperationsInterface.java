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

package org.exoplatform.services.wsrp1.producer;

import org.exoplatform.services.wsrp1.type.RegistrationContext;
import org.exoplatform.services.wsrp1.type.RegistrationData;
import org.exoplatform.services.wsrp1.type.RegistrationState;
import org.exoplatform.services.wsrp1.type.ReturnAny;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface RegistrationOperationsInterface {
  public RegistrationContext register(RegistrationData data) throws java.rmi.RemoteException;

  public RegistrationState modifyRegistration(RegistrationContext context, RegistrationData data) throws java.rmi.RemoteException;

  public ReturnAny deregister(RegistrationContext context) throws java.rmi.RemoteException;
}
