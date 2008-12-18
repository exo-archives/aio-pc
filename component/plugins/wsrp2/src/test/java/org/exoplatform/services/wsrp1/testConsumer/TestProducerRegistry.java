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

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 17:08:46
 */

public class TestProducerRegistry extends BaseTest {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestProducerRegistry.setUp()");
  }

  public void testAddProducer() {
    producerRegistry.addProducer(producer);
    assertTrue(producerRegistry.existsProducer(producer.getID()));
    assertEquals(producer, producerRegistry.getAllProducers().next());
  }

  public void testRemoveProducer() throws Exception {
    producerRegistry.removeAllProducers();

    producerRegistry.addProducer(producer);
    producerRegistry.removeAllProducers();
    assertTrue(!producerRegistry.getAllProducers().hasNext());

    producerRegistry.addProducer(producer);
    producerRegistry.removeProducer(producer.getID());
    assertTrue(!producerRegistry.getAllProducers().hasNext());
  }
}
