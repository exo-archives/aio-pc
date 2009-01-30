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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.configuration.ConfigurationException;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.registry.RegistryEntry;
import org.exoplatform.services.jcr.ext.registry.RegistryService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
import org.picocontainer.Startable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 2
 *         févr. 2004 Time: 23:04:48
 */
public class ProducerRegistryJCRImpl implements ProducerRegistry, Startable {

  private long                  lastModifiedTime_;

  private Map<String, Producer> producers;

  private Log                   LOG;

  protected ExoContainer        cont;

  /**
   * Registry service.
   */
  private RegistryService       registryService;

  /**
   * The service name.
   */
  private static final String   SERVICE_NAME = "ProducerRegistryJCRImpl";

  public ProducerRegistryJCRImpl(ExoContainerContext ctx, RegistryService registryService) throws ConfigurationException {
    this.LOG = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    this.cont = ctx.getContainer();
    this.registryService = registryService;
    this.lastModifiedTime_ = System.currentTimeMillis();
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
      writeValueRegistryService(id, null);
      producers.remove(id);
      if (cont.getComponentInstance(id) == null)
        cont.unregisterComponent(id);
      lastModifiedTime_ = System.currentTimeMillis();
      return getProducer(id);
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

  final private Collection<WSRP2ProducerData> loadAll() throws Exception {
    // load parent node, where are placed producer's registration
    String entryPath = RegistryService.EXO_SERVICES + "/" + SERVICE_NAME;
    Element element = null;
    try {
      SessionProvider sessionProvider = SessionProvider.createSystemProvider();
      RegistryEntry registryEntry = registryService.getEntry(sessionProvider, entryPath);
      Document doc = registryEntry.getDocument();
      element = doc.getDocumentElement();
    } catch (PathNotFoundException e) {
      return null;
    } catch (RepositoryException e) {
      return null;
    }
    if (element == null)
      return null;
    Collection<WSRP2ProducerData> loadAll = null;
    NodeList childNodes = element.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node item = childNodes.item(i);
      String value = loadValueRegistryService(item.getNodeName());
      if (loadAll == null)
        loadAll = new ArrayList<WSRP2ProducerData>();
      WSRP2ProducerData data = new WSRP2ProducerData();
      data.setId(item.getNodeName());
      data.setData(value.getBytes());
      loadAll.add(data);
    }
    return loadAll;
  }

  final private void removeAll() throws Exception {
    try {
      String entryPath = RegistryService.EXO_SERVICES + "/" + SERVICE_NAME;
      SessionProvider sessionProvider = SessionProvider.createSystemProvider();
      registryService.removeEntry(sessionProvider, entryPath);
    } catch (RepositoryException e) {
      // if there are no producer's registration yet - to do nothing
    }
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
    writeValueRegistryService(p.getID(), value);
  }

  private void writeValueRegistryService(String id, String value) throws IOException,
                                                                 SAXException,
                                                                 ParserConfigurationException,
                                                                 RepositoryException {

    String entryPath = RegistryService.EXO_SERVICES + "/" + SERVICE_NAME + "/" + id;
    if (LOG.isDebugEnabled())
      LOG.debug(" entryPath = " + entryPath);
    // if value = null and present than remove
    if (value == null) {
      String el = loadValueRegistryService(id);
      if (el != null) {
        // remove
        if (LOG.isDebugEnabled())
          LOG.debug(" +++  if value = null and present than remove = ");
        SessionProvider sessionProvider = SessionProvider.createSystemProvider();
        registryService.removeEntry(sessionProvider, entryPath);
      }
    } else {
      // value != null
      String el = loadValueRegistryService(id);
      if (LOG.isDebugEnabled())
        LOG.debug(" el = " + el);
      if (el == null) {
        // if not present than write
        if (LOG.isDebugEnabled())
          LOG.debug(" +++  if not present than write =");
        SessionProvider sessionProvider = SessionProvider.createSystemProvider();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element = doc.createElement(id);
        setAttributeSmart(element, "value", value);
        doc.appendChild(element);
        RegistryEntry serviceEntry = new RegistryEntry(doc);
        registryService.createEntry(sessionProvider, RegistryService.EXO_SERVICES + "/"
            + SERVICE_NAME, serviceEntry);
      } else {
        // if present than update
        if (LOG.isDebugEnabled())
          LOG.debug("if present than update =");
        SessionProvider sessionProvider = SessionProvider.createSystemProvider();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        if (LOG.isDebugEnabled())
          LOG.debug(" id = " + id);
        Element element = doc.createElement(id);
        setAttributeSmart(element, "value", value);
        doc.appendChild(element);
        RegistryEntry serviceEntry = new RegistryEntry(doc);
        registryService.recreateEntry(sessionProvider, RegistryService.EXO_SERVICES + "/"
            + SERVICE_NAME, serviceEntry);
      }
    }

  }

  final private WSRP2ProducerData load(String id) throws PathNotFoundException, RepositoryException {
    String el = loadValueRegistryService(id);
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

  private String loadValueRegistryService(String id) {

    String entryPath = RegistryService.EXO_SERVICES + "/" + SERVICE_NAME + "/" + id;
    if (LOG.isDebugEnabled())
      LOG.debug(" entryPath = " + entryPath);
    Element element = null;
    try {
      SessionProvider sessionProvider = SessionProvider.createSystemProvider();
      RegistryEntry registryEntry = registryService.getEntry(sessionProvider, entryPath);
      Document doc = registryEntry.getDocument();
      element = doc.getDocumentElement();
      if (LOG.isDebugEnabled())
        LOG.debug(" element = " + element);
    } catch (PathNotFoundException e) {
      if (LOG.isDebugEnabled())
        LOG.debug(" e.getCause() = " + e.getCause());
      return null;
    } catch (RepositoryException e) {
      if (LOG.isDebugEnabled())
        LOG.debug(" e.getCause() = " + e.getCause());
      return null;
    }
    if (element == null)
      return null;
    String el = getAttributeSmart(element, "value");
    if (LOG.isDebugEnabled())
      LOG.debug(" el = " + el);
    return el;

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
