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
package org.exoplatform.services.wsrp2.test;

import junit.framework.TestCase;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.jcr.ext.registry.RegistryService;
import org.exoplatform.services.wsrp2.peristence.WSRPPersister;
import org.exoplatform.services.wsrp2.peristence.WSRPPersisterJCRregistry;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Feb 3, 2009
 */
public class TestWSRPPersisterJCRregistry extends TestCase {

  WSRPPersister          wsrpPersisterJCRregistry = null;

  protected ExoContainer container;

  RegistryService        registryService          = null;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    StandaloneContainer.addConfigurationPath("src/test/resources/jcr-exo-configuration.xml");
    container = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader());
    System.out.println(">>> EXOMAN TestWSRPPersisterJCRregistry.setUp() container = " + container);

    registryService = (RegistryService) container.getComponentInstanceOfType(RegistryService.class);
    System.out.println(">>> EXOMAN TestWSRPPersisterJCRregistry.setUp() registryService = "
        + registryService);

    wsrpPersisterJCRregistry = new WSRPPersisterJCRregistry(registryService);
    System.out.println(">>> EXOMAN TestWSRPPersisterJCRregistry.setUp() wsrpPersisterJCRregistry = "
        + wsrpPersisterJCRregistry);
  }

  public void testPut() throws Exception {
    String path = "TestWSRPPersisterJCRregistry";
    String id = "id";
    String value = "value";

    wsrpPersisterJCRregistry.putValue(path, id, value);
    String value2 = wsrpPersisterJCRregistry.getValue(path, id);
    assertNotNull(value2);
    assertEquals(value, value2);
  }
  
  public void testRemove() throws Exception {
    String path = "TestWSRPPersisterJCRregistry";
    String id = "id";
    String value = "value";

    wsrpPersisterJCRregistry.putValue(path, id, value);
    String value2 = wsrpPersisterJCRregistry.getValue(path, id);
    assertNotNull(value2);
    assertEquals(value, value2);
    wsrpPersisterJCRregistry.putValue(path, id, null);
    value2 = wsrpPersisterJCRregistry.getValue(path, id);
    assertNull(value2);
  }
  
  public void testRemoveUnsaved() throws Exception {
    String path = "TestWSRPPersisterJCRregistry";
    String id = "id";

    wsrpPersisterJCRregistry.putValue(path, id, null);
    String value2 = wsrpPersisterJCRregistry.getValue(path, id);
    assertNull(value2);
  }
  
  public void testPutAnother() throws Exception {
    String path = "TestWSRPPersisterJCRregistry";
    String id = "id";
    String value = "value";

    wsrpPersisterJCRregistry.putValue(path, id, value);
    String value2 = wsrpPersisterJCRregistry.getValue(path, id);
    assertNotNull(value2);
    assertEquals(value, value2);
    wsrpPersisterJCRregistry.putValue(path, id, "");
    value2 = wsrpPersisterJCRregistry.getValue(path, id);
    assertNotSame(value, value2);
  }
  

  @Override
  public void tearDown() throws Exception {
    System.out.println(">>> EXOMAN TestWSRPPersisterJCRregistry.tearDown() 1 = " + 1);
    super.tearDown();
  }

}
