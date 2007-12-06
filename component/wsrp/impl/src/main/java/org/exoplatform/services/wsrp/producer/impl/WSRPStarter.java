package org.exoplatform.services.wsrp.producer.impl;

import java.net.URL;
import java.io.InputStream;
import javax.servlet.ServletContext;
import org.picocontainer.Startable;
import org.apache.axis.utils.Options;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.wsrp.AdminClient;
import org.exoplatform.container.xml.ValueParam;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 * Date: 23 may 2006
 * Time: 11:43 am
 */

public class WSRPStarter implements Startable {

  protected int retries = 0;
  protected int retry_after = 0;

  class AxisThread extends Thread {
    Options opts;
    InputStream is;

    AxisThread(Options opts, InputStream is) {
      this.opts = opts;
      this.is = is;
    }

    public void run() {
System.out.println(" --- WSRP: axis thread started");
      try { Thread.currentThread().sleep(2 * 1000, 0); } catch (InterruptedException ie) {}
      for (int i = 0; i <= retries; i++) {
System.out.println(" --- WSRP: axis thread: attempt: " + i);
        try {
          AdminClient admin = new AdminClient();
          String result = admin.process(opts, is);
          if(result != null) {
            System.out.println(" --- WSRP axis: " + result);
            break;
          }
        } catch (Exception e) {
          System.out.println(" --- WSRP: axis thread got an exception: " + e);
          e.printStackTrace();
          System.out.println(" --- WSRP: axis thread got an exception: cause: " + e.getCause());
          if (e.getCause() != null)
            e.getCause().printStackTrace();
          if (i < retries)
            try { Thread.sleep(retry_after * 1000, 0); } catch (InterruptedException ie) {}
        }
      }
System.out.println(" --- WSRP: axis thread finished");
    }
  }

  private AxisThread axis;
  private ExoContainer cont;
  private String base = "";
  private String path = "";
  
  public WSRPStarter(ExoContainerContext ctx, WSRPConfiguration conf, InitParams params) {
    cont = ctx.getContainer();
    try {                                                
      base = params.getValueParam("base").getValue();
      path = params.getValueParam("path").getValue();
      retries = (new Integer(params.getValueParam("retries").getValue())).intValue();
      retry_after = (new Integer(params.getValueParam("retry_after").getValue())).intValue();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void start() {
    try {                                                
      ServletContext ctx = (ServletContext) cont.getComponentInstanceOfType(ServletContext.class);
      String wurl = base + path;
      //String wurl = base + ctx.getServletContextName() + path;
      String[] args = {"-l" + wurl};
System.out.println(" --- WSRP url opt: " + wurl);
      Options opts = new Options(args);
      URL wsdd = Thread.currentThread().getContextClassLoader().getResource("org/exoplatform/services/wsrp/wsdl/deploy.wsdd");
System.out.println(" --- WSDD url: " + wsdd);
      InputStream is = wsdd.openStream();
System.out.println(" --- WSDD stream: " + is);
      axis = new AxisThread(opts, is);
      axis.start();
//      axis.join(10000);
//System.out.println(" --- WSRP: axis thread is alive: " + axis.isAlive());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() {}
}