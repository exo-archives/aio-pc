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

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Feb 2, 2009
 */
public class WSRPPersisterJCRrepository implements WSRPPersister {
  private static final Log  LOG = ExoLogger.getLogger(WSRPPersisterJCRrepository.class);

  /**
   * Example "exo:services/" + SERVICE_NAME. SERVICE_NAME =
   * "PersistentStateManagerJCRImpl".
   */

  private RepositoryService repositoryService;

  public WSRPPersisterJCRrepository(RepositoryService repositoryService) {
    this.repositoryService = repositoryService;
  }

  public String getValue(String path, String id) throws RepositoryException {
    Session session = null;
    try {
      SessionProvider sessionProvider2 = SessionProvider.createSystemProvider();
      session = sessionProvider2.getSession("production", repositoryService.getCurrentRepository());
      javax.jcr.Node rootNode = session.getRootNode();
      init(rootNode, path);
      javax.jcr.Node customNode = rootNode.getNode(path);
      if (customNode.hasNode(id))
        return customNode.getNode(id).getProperty("value").getValue().getString();
      session.save();
      return null;
    } finally {
      if (session != null) {
        session.logout();
      }
    }
  }

  public void putValue(String path, String id, String value) throws RepositoryException {
    Session session = null;
    try {
      SessionProvider sessionProvider2 = SessionProvider.createSystemProvider();
      session = sessionProvider2.getSession("production", repositoryService.getCurrentRepository());
      javax.jcr.Node rootNode = session.getRootNode();
      init(rootNode, path);
      javax.jcr.Node customNode = rootNode.getNode(path);
      if (value == null) {
        // to REMOVE: value is null
        if (customNode.hasNode(id)) {
          customNode = customNode.getNode(id);
          Node parent = customNode.getParent();
          customNode.remove();
          parent.save();
        }
      } else {
        // to SAVE
        if (customNode.hasNode(id)) {
          // to REMOVE: before write
          customNode = customNode.getNode(id);
          Node parent = customNode.getParent();
          customNode.remove();
          parent.save();
        }
        //WRITE
        customNode = rootNode.getNode(path);
        customNode = customNode.addNode(id);
        if (LOG.isDebugEnabled()) {
          LOG.debug("add Node with id =" + id);
        }
        customNode.setProperty("value", value);

        if (LOG.isDebugEnabled()) {
          LOG.debug("setProperty '" + value + "' for id = " + id);
        }

      }

      session.save();

    } finally {
      if (session != null) {
        session.logout();
      }
    }
  }

  public Map<String, String> loadAll(String path) throws RepositoryException {
    Session session = null;
    try {
      SessionProvider sessionProvider2 = SessionProvider.createSystemProvider();
      session = sessionProvider2.getSession("production", repositoryService.getCurrentRepository());
      javax.jcr.Node rootNode = session.getRootNode();
      init(rootNode, path);
      javax.jcr.Node customNode = rootNode.getNode(path);

      Map<String, String> loadAll = null;

      NodeIterator childNodes = customNode.getNodes();
      while (childNodes.hasNext()) {
        Node item = (Node) childNodes.next();
        String key = item.getName();
        String value = item.getProperty("value").getValue().getString();
        if (loadAll == null)
          loadAll = new HashMap<String, String>();
        loadAll.put(key, value);
      }
      session.save();
      return loadAll;

    } finally {
      if (session != null) {
        session.logout();
      }
    }
  }

  public void removeAll(String path) throws RepositoryException {
    Session session = null;
    try {
      SessionProvider sessionProvider2 = SessionProvider.createSystemProvider();
      session = sessionProvider2.getSession("production", repositoryService.getCurrentRepository());
      javax.jcr.Node rootNode = session.getRootNode();
      init(rootNode, path);
      
      javax.jcr.Node customNode = rootNode;
      // to REMOVE
      if (customNode.hasNode(path)) {
        customNode = customNode.getNode(path);
        Node parent = customNode.getParent();
        customNode.remove();
        parent.save();
      }
      session.save();
    } finally {
      if (session != null) {
        session.logout();
      }
    }
  }

  private void init(Node rootNode, String path) throws RepositoryException {
    if (!rootNode.hasNode(path)) {
      rootNode.addNode(path);
      rootNode.save();
      if (LOG.isDebugEnabled()) {
        LOG.debug("add Node = path = " + path);
      }
    }
  }

}
