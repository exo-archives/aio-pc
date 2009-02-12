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
package org.exoplatform.services.wsrp2.test;

import java.util.Map;

import junit.framework.TestCase;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.wsrp2.peristence.WSRPPersister;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Feb 3, 2009
 */
public class WSRPPersisterTestBase extends TestCase {

  protected WSRPPersister wsrpPersister = null;

  protected ExoContainer  container;

  private String          path          = "TestWSRPPersisterPath";

  private String          id            = "id";

  private String          value         = "value";

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    StandaloneContainer.addConfigurationPath("src/test/resources/jcr-exo-configuration.xml");
    container = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader());

  }

  public void testPut() throws Exception {

    wsrpPersister.putValue(path, id, value);
    String valueResult = wsrpPersister.getValue(path, id);
    assertNotNull(valueResult);
    assertEquals(value, valueResult);
  }

  public void testRemove() throws Exception {

    wsrpPersister.putValue(path, id, value);
    String valueResult = wsrpPersister.getValue(path, id);
    assertNotNull(valueResult);
    assertEquals(value, valueResult);
    wsrpPersister.putValue(path, id, null);
    valueResult = wsrpPersister.getValue(path, id);
    assertNull(valueResult);
  }

  public void testRemoveEmpty() throws Exception {

    wsrpPersister.putValue(path, id, null);
    String valueResult = wsrpPersister.getValue(path, id);
    assertNull(valueResult);
  }

  public void testPutAnother() throws Exception {
    String emptyValue = "value";

    wsrpPersister.putValue(path, id, value);
    String valueResult = wsrpPersister.getValue(path, id);
    assertNotNull(valueResult);
    assertEquals(value, valueResult);
    wsrpPersister.putValue(path, id, emptyValue);
    valueResult = wsrpPersister.getValue(path, id);
    assertNotSame(value, valueResult);
    assertEquals(emptyValue, valueResult);
  }

  public void testRemoveAllEmpty() throws Exception {

    wsrpPersister.removeAll(path);
  }

  public void testRemoveAll() throws Exception {

    wsrpPersister.putValue(path, id + "1", value + "1");
    String valueResult = wsrpPersister.getValue(path, id + "1");
    assertNotNull(valueResult);
    assertEquals(value + "1", valueResult);
    wsrpPersister.putValue(path, id + "2", value + "2");
    valueResult = wsrpPersister.getValue(path, id + "2");
    assertNotNull(valueResult);
    assertEquals(value + "2", valueResult);

    wsrpPersister.removeAll(path);

    valueResult = wsrpPersister.getValue(path, id + "1");
    assertNull(valueResult);

    valueResult = wsrpPersister.getValue(path, id + "2");
    assertNull(valueResult);

  }

  public void testLoadAll() throws Exception {

    wsrpPersister.putValue(path, id + "1", value + "1");
    String valueResult = wsrpPersister.getValue(path, id + "1");
    assertNotNull(valueResult);
    assertEquals(value + "1", valueResult);
    wsrpPersister.putValue(path, id + "2", value + "2");
    valueResult = wsrpPersister.getValue(path, id + "2");
    assertNotNull(valueResult);
    assertEquals(value + "2", valueResult);

    Map<String, String> loadAll = wsrpPersister.loadAll(path);

    assertEquals(2, loadAll.keySet().size());

    valueResult = loadAll.get(id + "1");
    assertNotNull(valueResult);
    assertEquals(value + "1", valueResult);

    valueResult = loadAll.get(id + "2");
    assertNotNull(valueResult);
    assertEquals(value + "2", valueResult);

  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

}
