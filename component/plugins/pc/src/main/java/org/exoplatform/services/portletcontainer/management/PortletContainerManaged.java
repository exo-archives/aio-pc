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
package org.exoplatform.services.portletcontainer.management;

import org.exoplatform.services.portletcontainer.PortletLifecycleListener;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationRegisterImpl;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.management.annotations.Managed;
import org.exoplatform.management.ManagementContext;
import org.exoplatform.management.ManagementAware;
import org.exoplatform.management.jmx.annotations.NameTemplate;
import org.exoplatform.management.jmx.annotations.Property;

import javax.servlet.ServletContext;
import javax.portlet.PortletConfig;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
@Managed
@NameTemplate(@Property(key="service", value="portletcontainer"))
public class PortletContainerManaged implements PortletLifecycleListener, ManagementAware {

  /** . */
  private String name;

  /** . */
  private String description;

  /** . */
  private ManagementContext context;

  /** . */
//  private PortletApplicationManaged portletApplicationManaged;

  /** . */
  private List<PortletManaged> portletManageds = new ArrayList<PortletManaged>();

  public PortletContainerManaged(PortletApplicationRegisterImpl register) {
    register.addListenerPlugin(this);
  }

  public void preUndeploy(String portletApplicationName, PortletApp portletApplication, ServletContext servletContext) {
    for (Iterator<PortletManaged> i = portletManageds.iterator();i.hasNext();) {
      PortletManaged portletManaged = i.next();
      context.unregister(portletManaged);
      i.remove();
    }
//    context.unregister(portletApplicationManaged);
  }

  public void postUndeploy(String portletApplicationName, PortletApp portletApplication, ServletContext servletContext) {

  }

  public void preDeploy(String portletApplicationName, PortletApp portletApplication, ServletContext servletContext) {
  }

  public void postDeploy(String portletApplicationName, PortletApp portletApplication, ServletContext servletContext) {
    PortletApplicationManaged portletApplicationManaged = new PortletApplicationManaged(
      this,
      portletApplicationName,
      portletApplication,
      servletContext);
//    context.register(portletApplicationManaged);
//    this.portletApplicationManaged = portletApplicationManaged;

    //
    for (Portlet portlet : portletApplication.getPortlet()) {
      PortletManaged portletManaged = new PortletManaged(
        portletApplicationManaged,
        portlet
      );
      context.register(portletManaged);
      portletManageds.add(portletManaged);
    }
  }

  public void preInit(PortletConfig portletConfig) {

  }

  public void postInit(PortletConfig portletConfig) {

  }

  public void preDestroy() {

  }

  public void postDestroy() {

  }

  // *****
  public void setContext(ManagementContext context) {
    this.context = context;
  }

  // *****

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
