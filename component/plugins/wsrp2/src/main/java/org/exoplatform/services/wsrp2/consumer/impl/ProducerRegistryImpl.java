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

package org.exoplatform.services.wsrp2.consumer.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.configuration.ConfigurationException;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 2
 *         févr. 2004 Time: 23:04:48
 */
public class ProducerRegistryImpl implements ProducerRegistry {
  
  /**
   *  Query all producer.  
   */
  private static final String   queryAllProducer = "from pd in class org.exoplatform.services.wsrp2.consumer.impl.WSRP2ProducerData";

  /**
   *  Query V2 producer.  
   */
  private static final String   queryProducer    = "from pd in class org.exoplatform.services.wsrp2.consumer.impl.WSRP2ProducerData "
                                                     + "where pd.id = ?";

  /**
   *  Last modified time.  
   */
  private long                  lastModifiedTime_;

  /**
   *  Producers map.  
   */
  private Map<String, Producer> producers;

  /**
   *  Hibernate service.  
   */
  private HibernateService      hservice_;

  /**
   *  Logger.  
   */
  private Log                   log_;

  /**
   *  Container.  
   */
  protected ExoContainer        cont;

  public ProducerRegistryImpl(ExoContainerContext ctx, HibernateService dbService) throws ConfigurationException {
    hservice_ = dbService;
    log_ = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    cont = ctx.getContainer();
    producers = loadProducers();
    lastModifiedTime_ = System.currentTimeMillis();
  }

  private Map<String, Producer> loadProducers() {

    Map<String, Producer> map = new HashMap<String, Producer>();
    Collection<WSRP2ProducerData> c = null;
    try {
      c = loadAll();
    } catch (Exception e) {
      log_.error("Cannot load all producers with", e);
      return map;
    }

    for (Iterator<WSRP2ProducerData> iterator = c.iterator(); iterator.hasNext();) {
      WSRP2ProducerData wsrp2ProducerData = (WSRP2ProducerData) iterator.next();
      String producerUrl = wsrp2ProducerData.getProducer().getUrl().toExternalForm();
      ((ProducerImpl) wsrp2ProducerData.getProducer()).init(cont, producerUrl);
      map.put(wsrp2ProducerData.getId(), wsrp2ProducerData.getProducer());
      log_.info("Loaded producer with id = '" + wsrp2ProducerData.getId() + "' SUCCESSFUL");
    }

    return map;
  }

  public void addProducer(Producer producer) {
    try {
      save(producer);
      producers.put(producer.getID(), producer);
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

  final public void save(Producer p) throws Exception {
    WSRP2ProducerData data = load(p.getID());
    Session session = hservice_.openSession();
    if (data == null) {
      data = new WSRP2ProducerData();
      data.setId(p.getID());
      data.setProducer(p);
      session.save(data);
    } else {
      data.setProducer(p);
      session.update(data);
    }
    session.flush();
  }

  final public Collection<WSRP2ProducerData> loadAll() throws Exception {
    Session session = hservice_.openSession();
    List l = null;
    l = session.createQuery(queryAllProducer).list();
    return l;
  }

  final public WSRP2ProducerData load(String id) throws Exception {
    WSRP2ProducerData data = null;
    Session session = hservice_.openSession();
    List<WSRP2ProducerData> l = session.createQuery(queryProducer).setString(0, id).list();
    if (l.size() > 1) {
      throw new Exception("Expect only one configuration but found" + l.size());
    } else if (l.size() == 1) {
      data = (WSRP2ProducerData) l.get(0);
    }
    return data;
  }

  final public void remove(String id) throws Exception {
    Session session = hservice_.openSession();
    Object obj = session.createQuery(queryProducer).setString(0, id).uniqueResult();
    session.delete(obj);
    session.flush();
  }

  final public void removeAll() throws Exception {
    Session session = hservice_.openSession();
    Collection<Object> c = session.createQuery(queryAllProducer).list();
    for (Iterator<Object> iterator = c.iterator(); iterator.hasNext();) {
      session.delete(iterator.next());
    }
    session.flush();
  }
}
