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

package org.exoplatform.services.wsrp2.producer.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.axis.utils.Options;
import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.AdminClient;
import org.exoplatform.services.wsrp2.WSRPConstants;

/**
 * Author : Roman Pedchenko roman.pedchenko@exoplatform.com.ua Author : Alexey
 * Zavizionov alexey.zavizionov@exoplatform.com.ua Date: 23 may 2006 Time: 11:43
 * am
 */

public class WSRPStarter extends HttpServlet {

  private AxisThread   axis;

  private ExoContainer container;

  private String       base                = "http://localhost:8080/";

  private String       path                = "portal/services2";

  protected int        delayBeforeStartSec = 0;

  protected int        retries             = 1;

  protected int        delayRetrySec       = 3;

  private String       deployWSDD          = "org/exoplatform/services/wsrp2/wsdl/deploy.wsdd";

  private final Log    log                 = ExoLogger.getLogger(getClass().getName());

  public WSRPStarter() {
    logDebug("org.exoplatform.services.wsrp2.producer.impl.WSRPStarter.WSRPStarter() entered");
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    logDebug("org.exoplatform.services.wsrp2.producer.impl.WSRPStarter.init() entered");
    container = ExoContainerContext.getCurrentContainer();
    if (config.getInitParameter("base") != null) {
      base = config.getInitParameter("base");
      logDebug("WSRPStarter.init() base = " + base);
    }
    if (config.getInitParameter("path") != null) {
      path = config.getInitParameter("path");
      logDebug("WSRPStarter.init() path = " + path);
    }
    if (config.getInitParameter("deploy-wsdd") != null) {
      deployWSDD = config.getInitParameter("deploy-wsdd");
      logDebug("WSRPStarter.init() deployWSDD = " + deployWSDD);
    }
    try {
      delayBeforeStartSec = Integer.parseInt(config.getInitParameter("delay-before-start-sec"));
      logDebug("WSRPStarter.init() delayBeforeStartSec = " + delayBeforeStartSec);
    } catch (Exception e) {
    }
    try {
      retries = Integer.parseInt(config.getInitParameter("retries"));
      logDebug("WSRPStarter.init() retries = " + retries);
    } catch (Exception e) {
    }
    try {
      delayRetrySec = Integer.parseInt(config.getInitParameter("delay-retry-sec"));
      logDebug("WSRPStarter.init() delayRetrySec = " + delayRetrySec);
    } catch (Exception e) {
    }
    run();
  }

  private void run() {
    logDebug("org.exoplatform.services.wsrp2.producer.impl.WSRPStarter.run() entered");
    try {
      String wurl = base + path;
      String[] args = { "-l" + wurl };
      System.out.println(" --- " + WSRPConstants.WSRP_ID + ": url opt: " + wurl);
      Options opts = new Options(args);
      URL wsdd = Thread.currentThread().getContextClassLoader().getResource(deployWSDD);
      System.out.println(" --- " + WSRPConstants.WSRP_ID + ": WSDD url: " + wsdd);

      axis = new AxisThread(opts, wsdd);
      axis.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void logDebug(Object message) {
    if (log.isDebugEnabled()) {
      log.debug(message);
    }
  }

  class AxisThread extends Thread {
    Options opts;

    URL     wsdd;

    AxisThread(Options opts, URL wsdd) {
      this.opts = opts;
      this.wsdd = wsdd;
    }

    public void run() {
      try {
        System.out.println(" --- " + WSRPConstants.WSRP_ID + ": axis thread started");
        try {
          Thread.currentThread().sleep(delayBeforeStartSec * 1000, 0);
        } catch (InterruptedException ie) {
        }
        for (int i = 1; i <= retries; i++) {
          InputStream is = wsdd.openStream();
          System.out.println(" --- " + WSRPConstants.WSRP_ID + ": axis thread: attempt: " + i);
          logDebug(" --- " + WSRPConstants.WSRP_ID + ": axis thread: attempt: " + i + " at: "
              + new Date(System.currentTimeMillis()));
          try {
            AdminClient admin = new AdminClient();
            String result = admin.process(opts, is);
            if (result != null) {
              System.out.println(" --- " + WSRPConstants.WSRP_ID + ": axis: " + result);
              break;
            }
          } catch (Exception e) {
            System.out.println(" --- " + WSRPConstants.WSRP_ID + ": axis thread got an exception: "
                + e);
            e.printStackTrace();
            System.out.println(" --- " + WSRPConstants.WSRP_ID
                + ": axis thread got an exception: cause: " + e.getCause());
            if (e.getCause() != null)
              e.getCause().printStackTrace();
            if (i < retries) {
              try {
                Thread.sleep(delayRetrySec * 1000, 0);
              } catch (InterruptedException ie) {
              }
            }
          }
        }
        System.out.println(" --- " + WSRPConstants.WSRP_ID + ": axis thread finished");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

}
