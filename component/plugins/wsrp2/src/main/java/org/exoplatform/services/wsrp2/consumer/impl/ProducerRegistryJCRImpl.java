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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.configuration.ConfigurationException;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.core.nodetype.ExtendedNodeTypeManager;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
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

  private long                  lastModifiedTime_;

  private Map<String, Producer> producers;

  private Log                   log_;

  protected ExoContainer        cont;

  private RepositoryService     repositoryService;

  private Session               session           = null;

  private Node                  nodeForStorage;

  private static final String   NODE_STORAGE_NAME = "exo:wsrp";

  public ProducerRegistryJCRImpl(ExoContainerContext ctx, RepositoryService repositoryService) throws ConfigurationException {
    log_ = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    cont = ctx.getContainer();
    lastModifiedTime_ = System.currentTimeMillis();
    this.repositoryService = repositoryService;
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
        ProducerImpl prodImpl = (ProducerImpl)wsrp2ProducerData.getProducer();
        prodImpl.setID(wsrp2ProducerData.getId());
        prodImpl.init(cont, producerUrl);
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

  final private void save(Producer p) throws Exception {
    WSRP2ProducerData data = load(p.getID());
    if (data == null) {
      data = new WSRP2ProducerData();
      data.setId(p.getID());
      data.setProducer(p);
      Node n = getNodeForStorage().addNode(p.getID(), "exo:wsrp2ProducerData");
      n.setProperty("content", new ByteArrayInputStream(data.getData()));
      session.save();
    } else {
      data.setProducer(p);
      Node n = getNodeForStorage().getNode(p.getID());
      n.setProperty("content", new ByteArrayInputStream(data.getData()));
      session.save();
    }
  }

  final private Collection<WSRP2ProducerData> loadAll() throws Exception {
    Collection<WSRP2ProducerData> loadAll = null;
    if (session != null && getNodeForStorage() != null && getNodeForStorage().hasNodes()) {
      NodeIterator nodeIter = getNodeForStorage().getNodes();
      while (nodeIter.hasNext()) {
        Node elem = (Node) nodeIter.nextNode();
        if (loadAll == null)
          loadAll = new ArrayList<WSRP2ProducerData>();
        loadAll.add(getDataFromNode(elem));
      }
    }
    return loadAll;
  }

  final private WSRP2ProducerData load(String id) throws Exception {
    WSRP2ProducerData data = load(id, session);
    return data;
  }

  final private WSRP2ProducerData load(String id, Session session1) throws Exception {
    if (getNodeForStorage().hasNode(id)) {
      Node root = getNodeForStorage().getNode(id);
      return getDataFromNode(root);
    } else {
      return null;
    }
  }

  private WSRP2ProducerData getDataFromNode(Node node) throws PathNotFoundException,
                                                      RepositoryException {

    if (!node.hasProperty("content"))
      return null;
    Property prop = node.getProperty("content");
    ByteArrayInputStream bariStream = (ByteArrayInputStream) prop.getStream();
    int length = bariStream.available();
    byte[] buff = new byte[length];
    try {
      bariStream.read(buff);
    } catch (IOException e) {
      log_.error(e.getMessage(), e);
    }

    WSRP2ProducerData data = null;
    data = new WSRP2ProducerData();
    data.setId(node.getName());
    try {
      data.setData(buff);
    } catch (Exception e) {
      log_.error(e.getMessage(), e);
    }

    return data;
  }

  final private void remove(String id) throws Exception {
    if (getNodeForStorage().hasNode(id)) {
      Node n = getNodeForStorage().getNode(id);
      n.remove();
      session.save();
    }
  }

  final private void removeAll() throws Exception {

    if (getNodeForStorage().hasNodes()) {
      NodeIterator nodeIter = getNodeForStorage().getNodes();
      while (nodeIter.hasNext()) {
        Node elem = (Node) nodeIter.nextNode();
        elem.remove();
      }
    }
  }

  public void start() {

    try {

      ManageableRepository repository = this.repositoryService.getRepository("lightrep");

      SessionProvider sessionProvider = SessionProvider.createSystemProvider();

      session = sessionProvider.getSession("production", repository);

      InputStream xml = getClass().getResourceAsStream("ext-nodetypes-config.xml");

      repository.getNodeTypeManager().registerNodeTypes(xml,
                                                        ExtendedNodeTypeManager.IGNORE_IF_EXISTS);
      xml.close();

      getNodeForStorage();

      producers = loadProducers();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private Node getNodeForStorage() {
    if (nodeForStorage != null)
      return nodeForStorage;
    try {
      nodeForStorage = session.getRootNode().getNode(NODE_STORAGE_NAME);
      // if node exo:wsrp exist
      return nodeForStorage;
    } catch (RepositoryException e1) {
      // if node exo:wsrp doesn't exist
      try {
        nodeForStorage = session.getRootNode().addNode(NODE_STORAGE_NAME);
        session.save();
        return nodeForStorage;
      } catch (Exception e) {
        log_.error(e.getMessage(), e);
      }
      return null;
    }
  }

  public void stop() {
  }

}
