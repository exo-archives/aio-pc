/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.testConsumer;

import org.exoplatform.services.wsrp.consumer.WSRPPortlet;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.type.PortletContext;

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