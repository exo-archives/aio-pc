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

import java.util.Collection;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.registry.RegistryEntry;
import org.exoplatform.services.jcr.ext.registry.RegistryService;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.impl.ProducerImpl;
import org.exoplatform.services.wsrp2.consumer.impl.ProducerRegistryJCRImpl;
import org.exoplatform.services.wsrp2.consumer.impl.WSRP2ProducerData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Jan 23, 2009
 */
public class TestProducerRegistryJCRImpl2 extends BaseTest {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testJCRConf() throws Exception {

//    StandaloneContainer.addConfigurationPath("src/main/resources/conf/portal/jcr-exo-configuration.xml");
//    container = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader());
//    System.out.println(">>> EXOMAN BaseTest.setUp() container = " + container);

//    ProducerRegistryJCRImpl producerRegistry = (ProducerRegistryJCRImpl) container.getComponentInstanceOfType(ProducerRegistryJCRImpl.class);
//    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.testJCRConf() producerRegistry = "
//        + producerRegistry);

    RegistryService registryService = (RegistryService) container.getComponentInstanceOfType(RegistryService.class);
    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.testJCRConf() registryService = "
        + registryService);

    String SERVICE_NAME = "ProducerRegistryJCRImpl";
    String id = "a123456a";
//    SessionProvider sessionProvider = SessionProvider.createSystemProvider();
    Document doc = null;
    Element element = null;
    String entryPath = null;

    //WRITE
    SessionProvider sessionProvider2 = SessionProvider.createSystemProvider();
    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.write() sessionProvider = "
        + sessionProvider2);
    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//    Element root = doc.createElement(SERVICE_NAME);
//    doc.appendChild(root);

    element = doc.createElement(id);
    setAttributeSmart(element, "value", "abcdef");
    doc.appendChild(element);

    RegistryEntry serviceEntry = new RegistryEntry(doc);
    registryService.createEntry(sessionProvider2,
                                RegistryService.EXO_SERVICES + "/" + SERVICE_NAME,
                                serviceEntry);
    sessionProvider2.close();

    //LOAD
    load(registryService);

    //REMOVE
    SessionProvider sessionProvider4 = SessionProvider.createSystemProvider();
    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.REMOVE() sessionProvider4 = "
        + sessionProvider4);
    String entryPath2 = RegistryService.EXO_SERVICES + "/" + SERVICE_NAME + "/" + id;
    registryService.removeEntry(sessionProvider4, entryPath2);
    sessionProvider4.close();
    /////load
    try {
      sessionProvider4 = SessionProvider.createSystemProvider();
      RegistryEntry registryEntry = registryService.getEntry(sessionProvider4, entryPath2);
      sessionProvider4.close();
      doc = registryEntry.getDocument();
      element = doc.getDocumentElement();
    } catch (PathNotFoundException e) {
      element = null;
    } catch (RepositoryException e) {
      element = null;
    }
    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.load() +++++++ element = " + element);

//    //LOAD
//    load(registryService);
//
//    // LOADALL
//    for (int j = 0; j < 1; j++) {
//      loadAll(registryService);
//    }
//
//    //LOAD
//    load(registryService);

    
////  "http://localhost:8080/wsrp/soap/services/WSRP_v2_Markup_Service?wsdl"
//    Producer p = new ProducerImpl(container, "123", 2);
//    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.start() p = " + p);
//    p.setName("producerName");
//    p.setID("2307");
//    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.start() p = " + p);
//    WSRP2ProducerData data = new WSRP2ProducerData();
//    data.setId(p.getID());
//    data.setProducer(p);
//    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.start() data.getId() = "
//        + data.getId());
//    
//    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.start() data.getData() = "
//        + data.getData());
//    
//    
//    WSRP2ProducerData data2 = new WSRP2ProducerData();
//    data2.setData(data.getData());
//    
//    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.start() data2.getProducer().getID() = "
//        + data2.getProducer().getID());
//    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.start() data2.getProducer().getName() = "
//        + data2.getProducer().getName());

  }

  private void load(RegistryService registryService) {
    String SERVICE_NAME = "ProducerRegistryJCRImpl";
    String id = "a123456a";
//    SessionProvider sessionProvider = SessionProvider.createSystemProvider();
    Document doc = null;
    Element element = null;
    String entryPath = null;
    SessionProvider sessionProvider5 = SessionProvider.createSystemProvider();
    entryPath = RegistryService.EXO_SERVICES + "/" + SERVICE_NAME + "/" + id;
    try {
      RegistryEntry registryEntry = registryService.getEntry(sessionProvider5, entryPath);
      sessionProvider5.close();
      doc = registryEntry.getDocument();
      element = doc.getDocumentElement();
    } catch (PathNotFoundException e) {
      element = null;
    } catch (RepositoryException e) {
      element = null;
    }
    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.load() element = " + element);
//    if (element != null) {
//      String el = getAttributeSmart(element, "value");
//      System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.load() el !!! = " + el);
//    }
  }

  private void loadAll(RegistryService registryService) throws Exception {
    String SERVICE_NAME = "ProducerRegistryJCRImpl";
    String id = "a123456a";
//    SessionProvider sessionProvider = SessionProvider.createSystemProvider();
    Document doc = null;
    Element element = null;
    String entryPath = null;

    //LOADALL
    SessionProvider sessionProvider6 = SessionProvider.createSystemProvider();
    String entryPath3 = RegistryService.EXO_SERVICES + "/" + SERVICE_NAME;

    try {
      RegistryEntry registryEntry = registryService.getEntry(sessionProvider6, entryPath3);
      sessionProvider6.close();
      doc = registryEntry.getDocument();
      element = doc.getDocumentElement();
    } catch (PathNotFoundException e) {
      element = null;
    } catch (RepositoryException e) {
      element = null;
    }
    System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.loadAll() element = " + element);
    if (element != null) {

      System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.loadAll() element = " + element);

      NodeList childNodes = element.getChildNodes();
      System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.loadAll() childNodes = "
          + childNodes);
      System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.loadAll() childNodes.getLength() !!!!! = "
          + childNodes.getLength());
      if (childNodes.getLength() != 0) {
        throw new Exception(String.valueOf(childNodes.getLength()));
      }

      for (int i = 0; i < childNodes.getLength(); i++) {
        Node item = childNodes.item(i);
        System.out.println(">>> EXOMAN TestProducerRegistryJCRImpl.loadAll() item.getNodeName() = "
            + item.getNodeName());
      }
    }
  }

  private String getAttributeSmart(Element element, String attr) {
    return element.hasAttribute(attr) ? element.getAttribute(attr) : null;
  }

  private void setAttributeSmart(Element element, String attr, String value) {
    if (value == null) {
      element.removeAttribute(attr);
    } else {
      element.setAttribute(attr, value);
    }
  }

}
