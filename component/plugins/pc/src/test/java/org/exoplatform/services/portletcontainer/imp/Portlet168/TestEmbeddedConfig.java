/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.imp.Portlet168;

import java.util.List;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.pci.CustomModeWithDescription;
import org.exoplatform.services.portletcontainer.pci.LocalisedDescription;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */
public class TestEmbeddedConfig extends BasicTestCase {

  public TestEmbeddedConfig(String name) {
    super(name);
  }

  public void setUp() throws Exception {  }


  public void testPortletContainerEmbeddedConfig() throws Exception {
    PortletContainerConf conf = ((PortletContainerConf) ExoContainerContext.getTopContainer().
      getComponentInstanceOfType(PortletContainerConf.class));
    assertEquals(conf.getName(), "ExoPortletContainer");
    assertEquals(conf.getMajorVersion(), 2);
    assertEquals(conf.getMinorVersion(), 0);
    assertTrue(conf.isCacheEnable());
    assertTrue(conf.isBundleLookupDelegated());
// commented out because of removing pooling at all
//    assertEquals(conf.getNbOfInstancesInPool(), 200);
    assertEquals(conf.getProperties().get("test"), "test_value");
    boolean descChecked = false;
    Object[] cms = conf.getSupportedPortletModesWithDescriptions().toArray();
    for (Object cm0 : cms) {
      CustomModeWithDescription cm = (CustomModeWithDescription) cm0;
      if (!cm.getPortletMode().toString().equals("config"))
        continue;
      List descs = cm.getDescriptions();
      for (int j = 1; j <= descs.size(); j++) {
        LocalisedDescription ld = (LocalisedDescription) descs.get(j - 1);
        if (!ld.getLocale().getLanguage().equals("fr"))
          continue;
        assertEquals(ld.getDescription(), "permet de configurer les portlets");
        descChecked = true;
      }
    }
    assertTrue(descChecked);
  }

  protected String getDescription() {
    return "Test Converter";
  }
}