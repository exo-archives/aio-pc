/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.imp;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import org.exoplatform.services.portletcontainer.impl.config.*;
import org.exoplatform.services.portletcontainer.pci.CustomModeWithDescription;
import org.exoplatform.services.portletcontainer.pci.LocalisedDescription;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
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
    assertEquals(conf.getPortletContainerName(), "ExoPortletContainer");
    assertEquals(conf.getMajorVersion(), 1);
    assertEquals(conf.getMinorVersion(), 0);
    assertTrue(conf.isCacheEnable());
    assertFalse(conf.isSharedSessionEnable());
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