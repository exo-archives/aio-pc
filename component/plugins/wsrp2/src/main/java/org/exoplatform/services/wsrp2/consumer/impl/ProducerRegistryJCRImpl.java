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

package org.exoplatform.services.wsrp2.consumer.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.configuration.ConfigurationException;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.core.nodetype.ExtendedNodeTypeManager;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
import org.picocontainer.Startable;
import org.w3c.dom.Element;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 2
 *         févr. 2004 Time: 23:04:48
 */
public class ProducerRegistryJCRImpl implements ProducerRegistry, Startable {
  private static final String   queryAllProducer = "from pd in class org.exoplatform.services.wsrp2.consumer.impl.WSRP2ProducerData";

  private static final String   queryProducer    = "from pd in class org.exoplatform.services.wsrp2.consumer.impl.WSRP2ProducerData "
                                                     + "where pd.id = ?";

  private long                  lastModifiedTime_;

  private Map<String, Producer> producers;

//  private Map<String, WSRPService> services;

  private HibernateService      hservice_;

  private Log                   log_;

  protected ExoContainer        cont;

  private RepositoryService     repositoryService;

  public ProducerRegistryJCRImpl(ExoContainerContext ctx,
                                 HibernateService dbService,
                                 RepositoryService repositoryService) throws ConfigurationException {
    hservice_ = dbService;
    log_ = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    cont = ctx.getContainer();
    producers = null;//loadProducers();
//    services = new HashMap<String, WSRPService>();
    lastModifiedTime_ = System.currentTimeMillis();
    this.repositoryService = repositoryService;
    // use jcr

  }

  private String getAttributeSmart(Element element, String attr) {
    return element.hasAttribute(attr) ? element.getAttribute(attr) : null;
  }

  private Map<String, Producer> loadProducers() {

    Map<String, Producer> map = new HashMap<String, Producer>();
    try {
      Collection<WSRP2ProducerData> c = loadAll();
      for (Iterator<WSRP2ProducerData> iterator = c.iterator(); iterator.hasNext();) {
        WSRP2ProducerData wsrp2ProducerData = (WSRP2ProducerData) iterator.next();
        String producerUrl = wsrp2ProducerData.getProducer().getUrl().toExternalForm();
        ((ProducerImpl) wsrp2ProducerData.getProducer()).init(cont, producerUrl);
        map.put(wsrp2ProducerData.getId(), wsrp2ProducerData.getProducer());
      }
    } catch (Exception e) {
      log_.error("Error", e);
    }
    return map;
  }

  public void addProducer(Producer producer) {
    try {
      save(producer);
      producers.put(producer.getID(), producer);
//      services.put(producer.getID(), new WSRPService(producer.getUrl()));
      lastModifiedTime_ = System.currentTimeMillis();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Producer getProducer(String id) {
    return (Producer) producers.get(id);
  }

  public Iterator<Producer> getAllProducers() {
    return producers.values().iterator();
  }

  public Producer removeProducer(String id) {
    try {
      remove(id);
      producers.remove(id);
      if (cont.getComponentInstance(id) == null)
        cont.unregisterComponent(id);
//      services.remove(id);
      lastModifiedTime_ = System.currentTimeMillis();
      Producer producer = (Producer) producers.get(id);
      return producer;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void removeAllProducers() throws Exception {
    removeAll();
    producers.clear();
//    services.clear();
    lastModifiedTime_ = System.currentTimeMillis();
  }

  public boolean existsProducer(String id) {
    return producers.containsKey(id);
  }

  public Producer createProducerInstance(String producerURL, int version) {
    return new ProducerImpl(cont, producerURL, version);
  }

  public long getLastModifiedTime() {
    return lastModifiedTime_;
  }

  final private void save(Producer p) throws Exception {
//    Session session = hservice_.openSession();
//    WSRP2ProducerData data = load(p.getID());
//    if (data == null) {
//      data = new WSRP2ProducerData();
//      data.setId(p.getID());
//      data.setProducer(p);
//      session.save(data);
//    } else {
//      data.setProducer(p);
//      session.update(data);
//    }
//    session.flush();
  }

  final private Collection<WSRP2ProducerData> loadAll() throws Exception {
//    Session session = hservice_.openSession();
//    return session.createQuery(queryAllProducer).list();
    return null;
  }

  final private WSRP2ProducerData load(String id) throws Exception {
//    Session session = hservice_.openSession();
//    WSRP2ProducerData data = load(id, session);
//    return data;
    return null;
  }

  final public WSRP2ProducerData load(String id, Session session) throws Exception {
    WSRP2ProducerData data = null;
//    List<WSRP2ProducerData> l = session.createQuery(queryProducer).setString(0, id).list();
//    if (l.size() > 1) {
//      throw new Exception("Expect only one configuration but found" + l.size());
//    } else if (l.size() == 1) {
//      data = (WSRP2ProducerData) l.get(0);
//    }
    return data;
  }

  final private void remove(String id) throws Exception {
//    Session session = hservice_.openSession();
//    Object obj = session.createQuery(queryProducer).setString(0, id).uniqueResult();
//    session.delete(obj);
//    session.flush();
  }

  final private void removeAll() throws Exception {
//    Session session = hservice_.openSession();
//    Collection<Object> c = session.createQuery(queryAllProducer).list();
//    for (Iterator<Object> iterator = c.iterator(); iterator.hasNext();) {
//      session.delete(iterator.next());
//    }
//    session.flush();
  }

  public void start() {

    try {
      System.out.println(">>> EXOMAN = ");
      System.out.println(">>> EXOMAN = ");
      System.out.println(">>> EXOMAN = ");

      ManageableRepository repository = this.repositoryService.getRepository("lightrep");
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.ProducerRegistryJCRImpl() repository = "
          + repository);

      SessionProvider sessionProvider = SessionProvider.createSystemProvider();
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.ProducerRegistryJCRImpl() sessionProvider = "
          + sessionProvider);

      Session session = sessionProvider.getSession("production", repository);
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.ProducerRegistryJCRImpl() session = "
          + session);

      InputStream xml = getClass().getResourceAsStream("ext-nodetypes-config.xml");
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() xml = " + xml);

      repository.getNodeTypeManager().registerNodeTypes(xml,
                                                        ExtendedNodeTypeManager.IGNORE_IF_EXISTS);
      xml.close();

      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() session.getRootNode().getName() = "
          + session.getRootNode().getName());
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() session.getRootNode().getPath() = "
          + session.getRootNode().getPath());


      Node n = session.getRootNode().addNode("123", "exo:wsrp2ProducerData");
      n.setProperty("content", new ByteArrayInputStream("demo bytes array for test".getBytes()));
      session.save();

      Node root = session.getRootNode().getNode("123");//root_node + "/" + "exo:skins");
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() root = " + root);
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() root.getName() = "
          + root.getName());
      Property prop = root.getProperty("content");
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() prop = " + prop);
      ByteArrayInputStream bariStream = (ByteArrayInputStream) prop.getStream();
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() bariStream = " + bariStream);
      int length = bariStream.available();
      byte[] buff = new byte[length];
      bariStream.read(buff);
      String test = new String(buff);
      System.out.println(">>> EXOMAN ProducerRegistryJCRImpl.start() test = " + test);

      session.save();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void stop() {
  }

}
