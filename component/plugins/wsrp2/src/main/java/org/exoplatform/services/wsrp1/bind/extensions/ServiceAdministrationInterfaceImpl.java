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

package org.exoplatform.services.wsrp1.bind.extensions;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp2.producer.impl.WSRPConfiguration;

/**
 */
public class ServiceAdministrationInterfaceImpl implements ServiceAdministrationInterface {

  private WSRPConfiguration conf;

  private Log               log;

  private ExoContainer      container;

  public ServiceAdministrationInterfaceImpl(PortletContainerProxy cont,
                                            WSRPConfiguration conf,
                                            ExoContainerContext context) {
    this.conf = conf;
    this.log = ExoLogger.getLogger(ServiceAdministrationInterfaceImpl.class);
    this.container = context.getContainer();
  }

  public ServiceAdministration getServiceAdministration(Map<String, String> properties) throws RemoteException {

    java.util.HashMap<java.lang.String, java.lang.String> realProps = conf.getProperties();

    Set<String> set = properties.keySet();
    for (String key : set) {
      if (realProps.containsKey(key)) {
        realProps.put(key, properties.get(key));
      }
    }

    ServiceAdministration sA = new ServiceAdministration();
    sA.getProperties().putAll(realProps);
    return sA;
  }
}
