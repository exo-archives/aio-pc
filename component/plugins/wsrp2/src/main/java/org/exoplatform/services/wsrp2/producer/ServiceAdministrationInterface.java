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

package org.exoplatform.services.wsrp2.producer;

import java.util.Map;

/**
 * The Interface ServiceAdministrationInterface.
 */
public interface ServiceAdministrationInterface {

  /**
   * Gets the service administration.
   * 
   * @param properties the properties
   * @return the service administration
   * @throws RemoteException the remote exception
   */
  public ServiceAdministration getServiceAdministration(Map<String, String> properties) throws java.rmi.RemoteException;
}
