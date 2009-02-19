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

package org.exoplatform.services.wsrp2.consumer.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp2.consumer.PortletDriver;
import org.exoplatform.services.wsrp2.consumer.PortletDriverRegistry;
import org.exoplatform.services.wsrp2.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 22:49:30
 */

public class PortletDriverRegistryImpl implements PortletDriverRegistry {

  private Map<String, PortletDriver> portletDrivers = new HashMap<String, PortletDriver>();

  protected ExoContainer             cont;

  public PortletDriverRegistryImpl(ExoContainerContext ctx) {
    cont = ctx.getContainer();
  }

  public PortletDriver getPortletDriver(WSRPPortlet portlet) throws WSRPException {
    PortletDriver driver = (PortletDriver) portletDrivers.get(portlet.getPortletKey().toString());
    if (driver == null) {
      driver = new PortletDriverImpl(cont, portlet);
      portletDrivers.put(portlet.getPortletKey().toString(), driver);
    }
    return driver;
  }

  public Iterator<PortletDriver> getAllPortletDrivers() {
    return portletDrivers.values().iterator();
  }

}
