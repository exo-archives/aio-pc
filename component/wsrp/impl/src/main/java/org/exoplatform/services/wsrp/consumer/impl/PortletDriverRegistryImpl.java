/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.impl;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp.consumer.PortletDriver;
import org.exoplatform.services.wsrp.consumer.PortletDriverRegistry;
import org.exoplatform.services.wsrp.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp.exceptions.WSRPException;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 22:49:30
 */

public class PortletDriverRegistryImpl implements PortletDriverRegistry {

  private Map portletDrivers = new HashMap();  
  protected ExoContainer cont;

  public PortletDriverRegistryImpl(ExoContainerContext ctx) {
    cont = ctx.getContainer();
  }

  public PortletDriver getPortletDriver(WSRPPortlet portlet) throws WSRPException {
    PortletDriver driver = null;
    if ((driver = (PortletDriver) portletDrivers.get(portlet.getPortletKey().toString())) == null) {      
      driver = new PortletDriverImpl(cont, portlet);
      portletDrivers.put(portlet.getPortletKey().toString(), driver);
    }
    return driver;
  }

  public Iterator getAllPortletDrivers() {
    return portletDrivers.values().iterator();
  }

}