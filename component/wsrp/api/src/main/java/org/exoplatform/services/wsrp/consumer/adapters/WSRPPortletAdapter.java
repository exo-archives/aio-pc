/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.adapters;

import org.exoplatform.services.wsrp.consumer.PortletKey;
import org.exoplatform.services.wsrp.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp.type.PortletContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 8 févr. 2004
 * Time: 23:11:40
 */

public class WSRPPortletAdapter implements WSRPPortlet {

  private PortletKey portletKey = null;
  private PortletContext portletContext = null;
  private String parentHandle = null;

  public WSRPPortletAdapter(PortletKey portletKey) {
    this.portletKey = portletKey;
    this.parentHandle = portletKey.getPortletHandle();
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
    return !getParent().equals(portletKey.getPortletHandle());
  }

}