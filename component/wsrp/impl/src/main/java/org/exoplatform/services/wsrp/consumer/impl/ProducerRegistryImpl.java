/*
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 */

package org.exoplatform.services.wsrp.consumer.impl;

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
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.consumer.ProducerRegistry;
import org.hibernate.Session;

 
 
/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 23:04:48
 */
public class ProducerRegistryImpl implements ProducerRegistry {
  private static final String queryAllProducer =
      "from pd in class org.exoplatform.services.wsrp.consumer.impl.ProducerData";
  private static final String queryProducer =
      "from pd in class org.exoplatform.services.wsrp.consumer.impl.ProducerData " +
      "where pd.id = ?";

  private long lastModifiedTime_;
  private Map producers;
  private HibernateService hservice_;
  private Log log_;
  protected ExoContainer cont;

  public ProducerRegistryImpl(ExoContainerContext ctx, HibernateService dbService) throws ConfigurationException {
    hservice_ = dbService ;
    log_ = ExoLogger.getLogger("org.exoplatform.services.wsrp");
    cont = ctx.getContainer();
    producers = loadProducers();
    lastModifiedTime_ = System.currentTimeMillis();
  }

  private Map loadProducers() {
    Map map = new HashMap();
    try {
      Collection c = loadAll();
      for (Iterator iterator = c.iterator(); iterator.hasNext();) {
        ProducerData producerData = (ProducerData) iterator.next();
        ((ProducerImpl)producerData.getProducer()).init(cont);
        map.put(producerData.getId(), producerData.getProducer());
      }
    } catch (Exception e) {
      log_.error("Error", e) ;
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

  public Iterator getAllProducers() {
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
    removeAll() ;
    producers.clear();
    lastModifiedTime_ = System.currentTimeMillis();
  }

  public boolean existsProducer(String id) {
    return producers.containsKey(id);
  }

  public Producer createProducerInstance() {
    return new ProducerImpl(cont);
  }

  public long getLastModifiedTime() {
    return lastModifiedTime_;
  }

  final public void save(Producer p) throws Exception {
  	Session session = hservice_.openSession();
  	ProducerData data = load(p.getID());
  	if (data == null) {
  		data = new ProducerData();
  		data.setId(p.getID());
  		data.setProducer(p);
  		session.save(data);
  	} else {
  		data.setProducer(p);
  		session.update(data);
  	}
  	session.flush();
  }

  final public Collection loadAll() throws Exception {
  	Session session = hservice_.openSession();
  	return session.createQuery(queryAllProducer).list();
  }


  final public ProducerData load(String id) throws Exception {
  	Session session = hservice_.openSession();
  	ProducerData data = load(id, session);
  	return data;
  }

  final public ProducerData load(String id, Session session) throws Exception {
    ProducerData data = null;
    List l = session.createQuery(queryProducer).setString(0, id).list();
    if (l.size() > 1) {
      throw new Exception("Expect only one configuration but found" + l.size());
    } else if (l.size() == 1) {
      data = (ProducerData) l.get(0);
    }
    return data;
  }
  
  final public void remove(String id) throws Exception {
  	Session session = hservice_.openSession();
  	Object obj = session.createQuery(queryProducer).setString(0, id).uniqueResult();
  	session.delete(obj);
  	session.flush();
  }

  final public void removeAll()  throws Exception {
  	Session session = hservice_.openSession();
  	Collection c = session.createQuery(queryAllProducer).list();
    for (Iterator iterator = c.iterator(); iterator.hasNext();) {
      session.delete(iterator.next());
    }
  	session.flush();
  }
}
