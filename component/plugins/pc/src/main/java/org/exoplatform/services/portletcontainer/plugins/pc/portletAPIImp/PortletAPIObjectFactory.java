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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import javax.portlet.PortletContext;
import javax.servlet.ServletContext;
import java.net.URLClassLoader;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: 11 nov. 2003
 * Time: 19:52:26
 */
public class PortletAPIObjectFactory {

  /**
   * Instance.
   */
  private static PortletAPIObjectFactory ourInstance;

  /**
   * @return class instance
   */
  public synchronized static PortletAPIObjectFactory getInstance() {
    if (ourInstance == null)
      ourInstance = new PortletAPIObjectFactory();
    return ourInstance;
  }

  /**
   * Private constructor.
   */
  private PortletAPIObjectFactory() {
  }

  /**
   * @param cont exo container
   * @param scontext servlet context
   * @param portlet portlet
   * @return portlet context
   */
  public final PortletContext createPortletContext(final ExoContainer cont,
      final ServletContext scontext,
      final Portlet portlet) {
    return new PortletContextImpl(cont, scontext, portlet);
  }

  /**
   * @param cont exo container
   * @param scontext servlet context
   * @param portlet portlet
   * @param cl class loader
   * @return portlet context
   */
  public final PortletContext createPortletContext(final ExoContainer cont,
      final ServletContext scontext,
      final Portlet portlet,
      final URLClassLoader cl) {
    return new PortletContextImpl(cont, scontext, portlet);
  }

}