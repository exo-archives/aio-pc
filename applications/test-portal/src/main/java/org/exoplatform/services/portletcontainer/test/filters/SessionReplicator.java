/**
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.portletcontainer.test.filters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jgroups.*;
import org.jgroups.blocks.*;
import org.jgroups.util.*;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import javax.servlet.ServletContext;
import org.exoplatform.services.portletcontainer.impl.replication.*;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.exoplatform.Constants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SessionReplicator implements RequestHandler{

  private static Channel channel;
  private static MessageDispatcher disp;
  RspList rsp_list;
  
  private static final String props = "UDP(mcast_addr=228.8.8.8;mcast_port=45566;ip_ttl=32;mcast_send_buf_size=64000;mcast_recv_buf_size=64000):" +
  "PING(timeout=2000;num_initial_members=3):" +
  "MERGE2(min_interval=5000;max_interval=10000):" +
  "FD(timeout=5000):" +
  "VERIFY_SUSPECT(timeout=1500):" +
  "pbcast.NAKACK(max_xmit_size=8096;gc_lag=50;retransmit_timeout=600,1200,2400,4800):" +
  "UNICAST(timeout=600,1200,2400,4800):" +
  "pbcast.STABLE(desired_avg_gossip=20000):" +
  "FRAG(frag_size=8096;down_thread=false;up_thread=false):" +
  "pbcast.GMS(join_timeout=5000;" +
  "join_retry_timeout=2000;shun=false;print_local_addr=true)"; 

  
  
  public void send(HashMap session_info) throws Exception {
    
    if (channel == null || disp == null){
    channel = new JChannel(props);
    disp = new MessageDispatcher(channel,null,null,this);
    channel.connect("TestGroup");
    }
    org.jgroups.Message  mess = new org.jgroups.Message(null,null,session_info);
    rsp_list = disp.castMessage(null, mess, GroupRequest.GET_ALL, 0);
      
    //channel.close();
    //disp.stop();
  }

  public Object handle(Message msg) {
    //System.out.println("handle(): " + msg.getObject());
    HashMap session_info = (HashMap)msg.getObject();
    String sid = (String)session_info.get(PortletFilter.session_identifier);
    String pid = (String)session_info.get(PortletFilter.portal_identifier);
    session_info.remove(PortletFilter.session_identifier);
    session_info.remove(PortletFilter.portal_identifier);
    
    ExoContainer container  = ExoContainerContext.getContainerByName(pid);
    //System.out.println("Container2 : "+container);
    
    PortletContainerService service = (PortletContainerService) container.getComponentInstanceOfType(PortletContainerService.class);
    //System.out.println("Service : "+service);
    
    ServletContext ctx = (ServletContext) container.getComponentInstanceOfType(ServletContext.class);
    //System.out.println("ServletContext : "+ctx);
    
    FakeHttpSession httpSession = new FakeHttpSession(sid, ctx);
    FakeHttpRequest httpRequest = new FakeHttpRequest(httpSession);
    FakeHttpResponse httpResponse = new FakeHttpResponse();
    
    
    try {
      for (Iterator i = session_info.keySet().iterator(); i.hasNext(); ) {
        String appName = (String) i.next();
        service.sendAttrs((HttpServletRequest)httpRequest, (HttpServletResponse)httpResponse, (Map)session_info.get(appName), appName);
      }
    }catch(Exception exc){
      //exc.printStackTrace();
    }
    return new String("Ok");
  }
}
