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
package org.exoplatform.services.wsrp2.testConsumer;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.impl.ProducerImpl;
import org.exoplatform.services.wsrp2.consumer.impl.ProducerRegistryJCRImpl;
import org.exoplatform.services.wsrp2.consumer.impl.WSRP2ProducerData;


/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey Zavizionov</a>
 * @version $Id: $
 *
 * Jan 23, 2009  
 */
public class TestProducerRegistryJCRImpl extends BaseTest {
  
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testJCRConf() throws Exception {
    
//    StandaloneContainer.addConfigurationPath("src/main/resources/conf/portal/jcr-exo-configuration.xml");
//    container = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader());
//    System.out.println(">>> EXOMAN BaseTest.setUp() container = " + container);
    
    ProducerRegistryJCRImpl producerRegistry = (ProducerRegistryJCRImpl) container.getComponentInstanceOfType(ProducerRegistryJCRImpl.class);
    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.testJCRConf() producerRegistry = "
        + producerRegistry);
    
    
    
//  "http://localhost:8080/wsrp/soap/services/WSRP_v2_Markup_Service?wsdl"
    Producer p = new ProducerImpl(container, "123", 2);
    System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() p = " + p);
    p.setName("producerName");
    p.setID("2307");
    System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() p = " + p);
    WSRP2ProducerData data = new WSRP2ProducerData();
    data.setId(p.getID());
    data.setProducer(p);
    System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() data.getId() = "
        + data.getId());
    
    System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() data.getData() = "
        + data.getData());
    
    
    WSRP2ProducerData data2 = new WSRP2ProducerData();
    data2.setData(data.getData());
    
    System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() data2.getProducer().getID() = "
        + data2.getProducer().getID());
    System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() data2.getProducer().getName() = "
        + data2.getProducer().getName());
    
    
    
  }
  
}
