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
package org.exoplatform.services.portletcontainer;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;

/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com
 */
public interface PortletApplicationRegister {

  public void registerPortletApplication(ServletContext servletContext, PortletApp portletApp_, Collection<String> roles, String portletAppName) throws PortletContainerException;

  public void removePortletApplication(ServletContext servletContext, String portletAppName) throws PortletContainerException;

  public void addListenerPlugin(ComponentPlugin listener);
}
