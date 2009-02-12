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
package org.exoplatform.services.wsrp2.peristence;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.registry.RegistryEntry;
import org.exoplatform.services.jcr.ext.registry.RegistryService;
import org.exoplatform.services.log.ExoLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Feb 2, 2009
 */
public class WSRPPersisterJCRregistry implements WSRPPersister {
  private static final Log LOG = ExoLogger.getLogger(WSRPPersisterJCRregistry.class);

  /**
   * Path = RegistryService.EXO_SERVICES + "/" + SERVICE_NAME + "/" + id.
   * SERVICE_NAME = "ProducerRegistryJCRImpl".
   */
  private String           pathPrefix = RegistryService.EXO_SERVICES + "/";

  private RegistryService  registryService;

  public WSRPPersisterJCRregistry(RegistryService registryService) {
    this.registryService = registryService;
  }

  public String getValue(String path, String id) throws RepositoryException {
    if (LOG.isDebugEnabled())
      LOG.debug("id = " + id);
    String entryPath = pathPrefix + path + "/" + id;
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
    }
    if (element == null)
      return null;
    String el = getAttributeSmart(element, "value");
    if (LOG.isDebugEnabled())
      LOG.debug(" el = " + el);
    return el;

  }

  public void putValue(String path, String id, String value) throws RepositoryException {
    if (LOG.isDebugEnabled())
      LOG.debug(" id = " + id);
    String entryPath = pathPrefix + path + "/" + id;
    if (LOG.isDebugEnabled())
      LOG.debug(" entryPath = " + entryPath);
    // if value = null and present than REMOVE it
    if (value == null) {
      String el = getValue(path, id);
      if (el != null) {
        // remove
        if (LOG.isDebugEnabled())
          LOG.debug(" +++  if value = null and present than remove = ");
        SessionProvider sessionProvider = SessionProvider.createSystemProvider();
        registryService.removeEntry(sessionProvider, entryPath);
      }
    } else {
      // value != null
      String el = getValue(path, id);
      if (LOG.isDebugEnabled())
        LOG.debug(" el = " + el);
      if (el == null) {
        // if not present than write
        if (LOG.isDebugEnabled())
          LOG.debug(" +++  if not present than write =");
        SessionProvider sessionProvider = SessionProvider.createSystemProvider();
        Document doc = null;
        try {
          doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
          throw new RepositoryException(e.getMessage(), e.getCause());
        }
        Element element = doc.createElement(id);
        setAttributeSmart(element, "value", value);
        doc.appendChild(element);
        RegistryEntry serviceEntry = new RegistryEntry(doc);
        registryService.createEntry(sessionProvider,
                                    pathPrefix + path,
                                    serviceEntry);
      } else {
        // if present than update
        if (LOG.isDebugEnabled())
          LOG.debug("if present than update =");
        SessionProvider sessionProvider = SessionProvider.createSystemProvider();
        Document doc = null;
        try {
          doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
          throw new RepositoryException(e.getMessage(), e.getCause());
        }
        if (LOG.isDebugEnabled())
          LOG.debug(" id = " + id);
        Element element = doc.createElement(id);
        setAttributeSmart(element, "value", value);
        doc.appendChild(element);
        RegistryEntry serviceEntry = new RegistryEntry(doc);
        registryService.recreateEntry(sessionProvider,
                                      pathPrefix + path,
                                      serviceEntry);
      }
    }

  }

  public Map<String, String> loadAll(String path) throws RepositoryException {
    if (LOG.isDebugEnabled())
      LOG.debug("entered");
    String entryPath = pathPrefix + path;
    // load parent node, where are placed producer's registration
    Element element = null;
    try {
      SessionProvider sessionProvider = SessionProvider.createSystemProvider();
      RegistryEntry registryEntry = registryService.getEntry(sessionProvider, entryPath);
      Document doc = registryEntry.getDocument();
      element = doc.getDocumentElement();
    } catch (PathNotFoundException e) {
      return null;
//    } catch (RepositoryException e) {
//      return null;
    }
    if (element == null)
      return null;
    Map<String, String> loadAll = null;
    NodeList childNodes = element.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node item = childNodes.item(i);
      String key = item.getNodeName();
      String value = getValue(path, item.getNodeName());
      if (loadAll == null)
        loadAll = new HashMap<String, String>();
      loadAll.put(key, value);
    }
    return loadAll;
  }

  public void removeAll(String path) throws RepositoryException {
    if (LOG.isDebugEnabled())
      LOG.debug("entered");
    String entryPath = pathPrefix + path;
    try {
      SessionProvider sessionProvider = SessionProvider.createSystemProvider();
      registryService.removeEntry(sessionProvider, entryPath);
    } catch (RepositoryException e) {
      // there is no data in JCR path yet - to do nothing
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
