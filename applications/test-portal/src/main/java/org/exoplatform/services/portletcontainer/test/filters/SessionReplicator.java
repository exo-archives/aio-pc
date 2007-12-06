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
package org.exoplatform.services.portletcontainer.test.filters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
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

/**
 * Session replicator class.
 */
public class SessionReplicator implements RequestHandler {

  /**
   * Channel.
   */
  private static Channel           channel;

  /**
   * Message dispatcher.
   */
  private static MessageDispatcher disp;

  /**
   * Properties.
   */
  private static final String      PROPS = "UDP(mcast_addr=228.8.8.8;mcast_port=45566;"
                                             + "ip_ttl=32;mcast_send_buf_size=64000;mcast_recv_buf_size=64000):"
                                             + "PING(timeout=2000;num_initial_members=3):" + "MERGE2(min_interval=5000;max_interval=10000):"
                                             + "FD(timeout=5000):" + "VERIFY_SUSPECT(timeout=1500):"
                                             + "pbcast.NAKACK(max_xmit_size=8096;gc_lag=50;retransmit_timeout=600,1200,2400,4800):"
                                             + "UNICAST(timeout=600,1200,2400,4800):" + "pbcast.STABLE(desired_avg_gossip=20000):"
                                             + "FRAG(frag_size=8096;down_thread=false;up_thread=false):" + "pbcast.GMS(join_timeout=5000;"
                                             + "join_retry_timeout=2000;shun=false;print_local_addr=true)";

  /**
   * Sends session info to other nodes.
   *
   * @param sessionInfo session info
   * @throws Exception something may go wrong
   */
  public final void send(final HashMap<String, Serializable> sessionInfo) throws Exception {
    if (channel == null || disp == null) {
      channel = new JChannel(PROPS);
      disp = new MessageDispatcher(channel, null, null, this);
      channel.connect("TestGroup");
    }
    org.jgroups.Message mess = new org.jgroups.Message(null, null, sessionInfo);
    disp.castMessage(null, mess, GroupRequest.GET_ALL, 0);
  }

  /**
   * Receives remote session infos and updates local data.
   *
   * @param msg message received
   * @return reply
   */
  public final Object handle(final Message msg) {
    HashMap<String, Serializable> sessionInfo = (HashMap<String, Serializable>) msg.getObject();
    String sid = (String) sessionInfo.get(PortletFilter.SESSION_IDENTIFIER);
    String pid = (String) sessionInfo.get(PortletFilter.PORTAL_IDENTIFIER);
    sessionInfo.remove(PortletFilter.SESSION_IDENTIFIER);
    sessionInfo.remove(PortletFilter.PORTAL_IDENTIFIER);

    ExoContainer container = ExoContainerContext.getContainerByName(pid);

    PortletContainerService service = (PortletContainerService) container.getComponentInstanceOfType(PortletContainerService.class);

    ServletContext ctx = (ServletContext) container.getComponentInstanceOfType(ServletContext.class);

    FakeHttpSession httpSession = new FakeHttpSession(sid, ctx);
    FakeHttpRequest httpRequest = new FakeHttpRequest(httpSession);
    FakeHttpResponse httpResponse = new FakeHttpResponse();

    try {
      for (Iterator<String> i = sessionInfo.keySet().iterator(); i.hasNext();) {
        String appName = i.next();
        service.sendAttrs((HttpServletRequest) httpRequest,
                          (HttpServletResponse) httpResponse,
                          (Map<String, Object>) sessionInfo.get(appName),
                          appName);
      }
    } catch (Exception exc) {
    }
    return new String("Ok");
  }
}
