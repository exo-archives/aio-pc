/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.impl;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.exoplatform.services.wsrp.consumer.PortletKey;
import org.exoplatform.services.wsrp.consumer.PortletRegistry;
import org.exoplatform.services.wsrp.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp.exceptions.WSRPException;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 20:40:23
 */

public class PortletRegistryImpl implements PortletRegistry{

  private Map portlets = new HashMap();

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

  public Iterator getAllPortlets() {
    return portlets.values().iterator();
  }

  public void removeAllPortlets() {
    portlets.clear();
  }

}