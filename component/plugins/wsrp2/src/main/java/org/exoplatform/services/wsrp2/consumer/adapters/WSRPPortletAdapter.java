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

package org.exoplatform.services.wsrp2.consumer.adapters;

import org.exoplatform.services.wsrp2.consumer.PortletKey;
import org.exoplatform.services.wsrp2.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp2.type.PortletContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 8
 *         févr. 2004 Time: 23:11:40
 */

public class WSRPPortletAdapter implements WSRPPortlet {

  private PortletKey     portletKey     = null;

  private PortletContext portletContext = null;

  private String         parentHandle   = null;

  public WSRPPortletAdapter(PortletKey portletKey) {
    this.portletKey = portletKey;
    System.out.println(">>>alexey:WSRPPortletAdapter.WSRPPortletAdapter portletKey.getPortletHandle() = "
        + portletKey.getPortletHandle());
    if (portletKey.getPortletHandle().contains("/"))
      this.parentHandle = portletKey.getPortletHandle().substring(portletKey.getPortletHandle().indexOf("/")+1);
    System.out.println(">>>alexey:WSRPPortletAdapter.WSRPPortletAdapter parentHandle = " + parentHandle);
    
  }

  public PortletKey getPortletKey() {
    return this.portletKey;
  }

  public void setPortletKey(PortletKey portletKey) {
    if (portletKey != null) {
      this.portletKey = portletKey;
      if (this.portletContext != null) {
        this.portletContext.setPortletHandle(portletKey.getPortletHandle());
      }
      if (parentHandle == null) {
        parentHandle = portletKey.getPortletHandle();
      }
    }
  }

  public void setPortletContext(PortletContext portletContext) {
    if (portletContext != null) {
      this.portletContext = portletContext;
      this.portletKey.setPortletHandle(portletContext.getPortletHandle());
    }
  }

  public PortletContext getPortletContext() {
    return this.portletContext;
  }

  public String getParent() {
    return this.parentHandle;
  }

  public void setParent(String portletHandle) {
    this.parentHandle = portletHandle;
  }

  public boolean isConsumerConfigured() {
    System.out.println(">>>alexey:WSRPPortletAdapter.isConsumerConfigured parentHandle = " + parentHandle);
    System.out.println(">>>alexey:WSRPPortletAdapter.isConsumerConfigured portletKey.getPortletHandle() = "
        + portletKey.getPortletHandle());
    return !getParent().equals(portletKey.getPortletHandle());
  }

}
