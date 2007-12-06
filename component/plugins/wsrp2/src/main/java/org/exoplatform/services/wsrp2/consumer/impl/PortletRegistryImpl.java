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

import org.exoplatform.services.wsrp2.consumer.PortletKey;
import org.exoplatform.services.wsrp2.consumer.PortletRegistry;
import org.exoplatform.services.wsrp2.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;

/*
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 2
 * févr. 2004 Time: 20:40:23
 */

public class PortletRegistryImpl implements PortletRegistry {

  private Map<PortletKey, WSRPPortlet> portlets = new HashMap<PortletKey, WSRPPortlet>();

  public void addPortlet(WSRPPortlet portlet) throws WSRPException {
    portlets.put(portlet.getPortletKey(), portlet);
  }

  public WSRPPortlet getPortlet(PortletKey portletKey) {
    return (WSRPPortlet) portlets.get(portletKey);
  }

  public WSRPPortlet removePortlet(PortletKey portletKey) {
    WSRPPortlet p = (WSRPPortlet) portlets.get(portletKey);
    portlets.remove(portletKey);
    return p;
  }

  public boolean existsPortlet(PortletKey portletKey) {
    return portlets.containsKey(portletKey);
  }

  public Iterator<WSRPPortlet> getAllPortlets() {
    return portlets.values().iterator();
  }

  public void removeAllPortlets() {
    portlets.clear();
  }

}
