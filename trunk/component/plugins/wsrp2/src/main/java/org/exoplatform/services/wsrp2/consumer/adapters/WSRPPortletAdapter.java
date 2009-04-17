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
  }

  public PortletKey getPortletKey() {
    return this.portletKey;
  }

  public void setPortletKey(PortletKey portletKey) {
    if (portletKey != null) {
      this.portletKey = portletKey;
    }
  }

  public void setPortletContext(PortletContext portletContext) {
    if (portletContext != null) {
      this.portletContext = portletContext;
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
    // if (parentHandle == PortletContext.handle) than 'false'
    return !getParent().equals(getPortletHandle());
  }

  public String getPortletHandle() {
    if (getPortletContext() == null)
      return null;
    return getPortletContext().getPortletHandle();
  }

  public void setPortletHandle(String portletHandle) {
    if (getPortletContext() == null)
      setPortletContext(new PortletContext());
    getPortletContext().setPortletHandle(portletHandle);
  }

}
