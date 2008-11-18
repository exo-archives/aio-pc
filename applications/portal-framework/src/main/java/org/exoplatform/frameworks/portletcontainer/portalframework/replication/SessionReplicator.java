/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.frameworks.portletcontainer.portalframework.replication;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.plugins.pc.replication.FakeHttpRequest;
import org.exoplatform.services.portletcontainer.plugins.pc.replication.FakeHttpResponse;
import org.exoplatform.services.portletcontainer.plugins.pc.replication.FakeHttpSession;
import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.blocks.GroupRequest;
import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestHandler;
import org.picocontainer.Startable;

/**
 * Session replicator class.
 */
public class SessionReplicator implements RequestHandler, Startable {

  /**
   * Session identifier.
   */
  public static final String       SESSION_IDENTIFIER    = "SID";

  /**
   * Portal identifier.
   */
  public static final String       PORTAL_IDENTIFIER     = "PID";

  /**
   * Portal identifier.
   */
  public static final String       REPLICATOR_IDENTIFIER = "RID";

  /**
   * Channel.
   */
  private static Channel           channel;

  /**
   * Message dispatcher.
   */
  private static MessageDispatcher disp;

  /**
   * Logger.
   */
  private static final Log         LOG                   = ExoLogger.getLogger(SessionReplicator.class);

  /**
   * Sends session info to other nodes.
   * 
   * @param sessionId http session id
   * @param portalContainerName portal container name
   * @param sessionInfo session info
   * @throws Exception something may go wrong
   */

  public SessionReplicator() {
    init();
  }

  public final void send(String sessionId,
                         String portalContainerName,
                         final Map<String, Object> sessionInfo) throws Exception {
    if (sessionInfo == null)
      return;
    if (LOG.isDebugEnabled())
      LOG.debug("SessionReplicator.send() sessionId = " + sessionId);
    sessionInfo.put(SESSION_IDENTIFIER, sessionId);
    sessionInfo.put(PORTAL_IDENTIFIER, portalContainerName);
    sessionInfo.put(REPLICATOR_IDENTIFIER, this.toString());
    try {
      org.jgroups.Message mess = new org.jgroups.Message(null,
                                                         null,
                                                         (HashMap<String, Object>) sessionInfo);
      disp.castMessage(null, mess, GroupRequest.GET_ALL, 0);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Receives remote session infos and updates local data.
   * 
   * @param msg message received
   * @return reply
   */
  public final Object handle(final Message msg) {
    if (msg.getObject() instanceof HashMap) {
      HashMap<String, Object> sessionInfo = (HashMap<String, Object>) msg.getObject();
      String sid = (String) sessionInfo.get(SESSION_IDENTIFIER);
      String pid = (String) sessionInfo.get(PORTAL_IDENTIFIER);
      String rid = (String) sessionInfo.get(REPLICATOR_IDENTIFIER);
      if (rid.equals(this.toString()))
        return null;
      sessionInfo.remove(SESSION_IDENTIFIER);
      sessionInfo.remove(PORTAL_IDENTIFIER);
      sessionInfo.remove(REPLICATOR_IDENTIFIER);

      ExoContainer container = ExoContainerContext.getContainerByName(pid);

      PortletContainerService service = (PortletContainerService) container.getComponentInstanceOfType(PortletContainerService.class);

      ServletContext ctx = (ServletContext) container.getComponentInstanceOfType(ServletContext.class);

      FakeHttpSession httpSession = new FakeHttpSession(sid, ctx);
      FakeHttpRequest httpRequest = new FakeHttpRequest(httpSession);
      FakeHttpResponse httpResponse = new FakeHttpResponse();

      try {
        for (Iterator<String> i = sessionInfo.keySet().iterator(); i.hasNext();) {
          String appName = i.next();
          if (sessionInfo.get(appName) != null) {
            service.sendAttrs((HttpServletRequest) httpRequest,
                              (HttpServletResponse) httpResponse,
                              (Map<String, Object>) sessionInfo.get(appName),
                              appName.split("/")[0]);
          }
        }
      } catch (Exception exc) {
        exc.printStackTrace();
      }
    }
    return new String("Ok");
  }

  private static JChannel createJChannel() throws Exception {
    InputStream stream = (SessionReplicator.class.getClassLoader().getResourceAsStream("jgroups-configuration.conf"));
    byte[] b;
    b = new byte[stream.available()];
    stream.read(b, 0, stream.available());
    String props = new String(b);
    return new JChannel(props);
  }

  public void init() {
    try {
      if (channel == null || disp == null) {
        channel = createJChannel();
        disp = new MessageDispatcher(channel, null, null, this);
        channel.connect("TestGroup");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void start() {
  }

  public void stop() {
  }

}
