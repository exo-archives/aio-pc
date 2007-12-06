/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.portletcontainer.test.listeners;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//import org.exoplatform.container.PortalContainer;
//import org.exoplatform.container.RootContainer;
import org.exoplatform.container.StandaloneContainer;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id$
 */

public class AppListener implements ServletContextListener {
  
  public void contextInitialized(ServletContextEvent sce) {
    try {
      ServletContext ctx = sce.getServletContext();
      Object[][] components = {{ServletContext.class.getName(), ctx}};
      StandaloneContainer standaloneContainer = StandaloneContainer.getInstance(this.getClass().getClassLoader(),
        components);
    } catch (Exception e) {
      System.out.println(" !!! AppListener.contextInitialized exception: " + e);
      e.printStackTrace();
    }
  }
  
  public void contextDestroyed(ServletContextEvent sce) {
  }
  
}
