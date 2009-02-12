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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.configuration.ConfigurationException;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.peristence.WSRPPersister;
import org.picocontainer.Startable;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 2
 *         févr. 2004 Time: 23:04:48
 */
public class ProducerRegistryJCRImpl implements ProducerRegistry, Startable {

  private long                  lastModifiedTime_;

  private Map<String, Producer> producers;

  private Log                   LOG;

  private ExoContainer          cont;

  private WSRPPersister         persister;

  private String                path;

  /**
   * The service name.
   */
  private static final String   SERVICE_NAME = "ProducerRegistryJCRImpl";

  public ProducerRegistryJCRImpl(ExoContainerContext ctx, WSRPPersister persister, InitParams params) throws ConfigurationException {
    this.LOG = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    this.cont = ctx.getContainer();
    this.lastModifiedTime_ = System.currentTimeMillis();
    this.persister = persister;
    this.path = params.getValueParam("path").getValue();
  }

  private Map<String, Producer> loadProducers() {

    Map<String, Producer> map = new HashMap<String, Producer>();
    try {
      Collection<WSRP2ProducerData> c = loadAll();
      // if this is the first start of WSRP service
      if (c == null)
        return map;
      for (Iterator<WSRP2ProducerData> iterator = c.iterator(); iterator.hasNext();) {
        WSRP2ProducerData wsrp2ProducerData = (WSRP2ProducerData) iterator.next();
        String producerUrl = wsrp2ProducerData.getProducer().getUrl().toExternalForm();
        ProducerImpl prodImpl = (ProducerImpl) wsrp2ProducerData.getProducer();
        prodImpl.setID(wsrp2ProducerData.getId());
        prodImpl.init(cont, producerUrl);
        map.put(wsrp2ProducerData.getId(), wsrp2ProducerData.getProducer());
      }
    } catch (Exception e) {
      LOG.error("Error", e);
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
      persister.putValue(path, id, null);
      producers.remove(id);
//      if (cont.getComponentInstance(id) == null)
//        cont.unregisterComponent(id);
      lastModifiedTime_ = System.currentTimeMillis();
      return getProducer(id);
    } catch (RepositoryException e) {
      LOG.error(e.getMessage(), e.getCause());
    }
    return null;
  }

  public void removeAllProducers() throws Exception {
    persister.removeAll(path);
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

  final private Collection<WSRP2ProducerData> loadAll() throws WSRPException {
    // load parent node, where are placed producer's registration

    Map<String, String> all = null;
    try {
      all = persister.loadAll(path);
    } catch (RepositoryException e) {
      throw new WSRPException(e.getMessage(), e);
    }

    if (all == null)
      return null;
    Collection<WSRP2ProducerData> loadAll = null;

    Iterator<String> keys = all.keySet().iterator();
    while (keys.hasNext()) {
      String key = (String) keys.next();
      if (loadAll == null)
        loadAll = new ArrayList<WSRP2ProducerData>();
      WSRP2ProducerData data = new WSRP2ProducerData();
      data.setId(key);
      try {
        data.setData(all.get(key).getBytes());
        LOG.info("Loaded producer with id = '" + key + "' SUCCESSFUL");
      } catch (Exception e) {
        LOG.info("Cannot load producer with id = '" + key + "'");
        throw new WSRPException(e.getMessage(), e);
      }
      loadAll.add(data);
    }
    return loadAll;
  }

  public void start() {
    try {

      producers = loadProducers();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void stop() {
  }

  final private void save(Producer p) throws Exception {
    WSRP2ProducerData data = new WSRP2ProducerData();
    data.setId(p.getID());
    data.setProducer(p);
    String value = new String(data.getData());
    persister.putValue(path, p.getID(), value);
  }

  final private WSRP2ProducerData load(String id) throws WSRPException {
    String el = null;
    try {
      persister.getValue(path, id);
    } catch (RepositoryException e) {
      throw new WSRPException(e.getMessage(), e);
    }

    if (el == null)
      return null;
    WSRP2ProducerData data = new WSRP2ProducerData();
    data.setId(id);
    try {
      data.setData(el.getBytes());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return data;
  }

}
