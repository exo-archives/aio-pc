/*
* Copyright 2001-2007 The eXo platform SAS  All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp2.testConsumer;

import org.exoplatform.services.wsrp2.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.type.PortletContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 5 févr. 2004
 * Time: 11:07:10
 */

public class TestPortletRegistry extends BaseTest{

  public void testAddPortlet() throws WSRPException {
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/AppletClient");
    WSRPPortlet p = createPortlet("hello/AppletClient", null, portletContext);
    portletRegistry.addPortlet(p);
    assertTrue(portletRegistry.existsPortlet(p.getPortletKey()));
    assertTrue(portletRegistry.getAllPortlets().hasNext());
    portletRegistry.removePortlet(p.getPortletKey());
    assertFalse(portletRegistry.getAllPortlets().hasNext());
  }

  public void testRemoveAll() throws WSRPException {
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/AppletClient");
    WSRPPortlet p = createPortlet("hello/AppletClient", null, portletContext);
    portletRegistry.addPortlet(p);
    portletRegistry.removeAllPortlets();
    assertFalse(portletRegistry.getAllPortlets().hasNext());
  }

}