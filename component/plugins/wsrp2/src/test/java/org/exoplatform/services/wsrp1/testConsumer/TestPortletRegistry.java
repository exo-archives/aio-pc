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

package org.exoplatform.services.wsrp1.testConsumer;

import org.exoplatform.services.wsrp.BaseTest;
import org.exoplatform.services.wsrp.WSRPException;
import org.exoplatform.services.wsrp.WSRPPortlet;
import org.exoplatform.services.wsrp1.type.PortletContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 5 févr. 2004
 * Time: 11:07:10
 */

public class TestPortletRegistry extends BaseTest {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestPortletRegistry.setUp()");
  }

  public void testAddPortlet() throws WSRPException {
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH.substring(1) + "/AppletClient");
    WSRPPortlet p = createPortlet(CONTEXT_PATH.substring(1) + "/AppletClient", null, portletContext);
    portletRegistry.addPortlet(p);
    assertTrue(portletRegistry.existsPortlet(p.getPortletKey()));
    assertTrue(portletRegistry.getAllPortlets().hasNext());
    portletRegistry.removePortlet(p.getPortletKey());
    assertFalse(portletRegistry.getAllPortlets().hasNext());
  }

  public void testRemoveAll() throws WSRPException {
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH.substring(1) + "/AppletClient");
    WSRPPortlet p = createPortlet(CONTEXT_PATH.substring(1) + "/AppletClient", null, portletContext);
    portletRegistry.addPortlet(p);
    portletRegistry.removeAllPortlets();
    assertFalse(portletRegistry.getAllPortlets().hasNext());
  }

}
