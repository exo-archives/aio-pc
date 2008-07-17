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
package org.exoplatform.services.portletcontainer.test.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.exoplatform.container.StandaloneContainer;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */

/**
 * Servlet context listener class is intended to catch context initialization and to set up
 * StandaloneContainer.
 */
public class AppListener implements ServletContextListener {

  /**
   * Creates stand-alone container when context have just been initialized.
   *
   * @param sce servlet context event
   */
  public final void contextInitialized(final ServletContextEvent sce) {
    try {
      ServletContext ctx = sce.getServletContext();
      Object[][] components = {{ServletContext.class.getName(), ctx}};
      // it's needed to switch off default behavior -- searching for /conf/portal/configuration.xml in all
      // available jars
      // StandaloneContainer.setConfigurationURL(null);
      StandaloneContainer standaloneContainer = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader()/*sce.getClass().getClassLoader()*/,
        components);
    } catch (Exception e) {
      System.out.println(" !!! AppListener.contextInitialized exception: " + e);
      e.printStackTrace();
    }
  }

  /**
   * Does nothing. Just an empty interface implementation.
   *
   * @param sce servlet context event
   */
  public final void contextDestroyed(final ServletContextEvent sce) { }

}
