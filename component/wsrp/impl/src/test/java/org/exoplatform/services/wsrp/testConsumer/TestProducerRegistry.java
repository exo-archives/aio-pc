/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.testConsumer;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 17:08:46
 */

public class TestProducerRegistry extends BaseTest{

  public void testAddProducer(){
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
