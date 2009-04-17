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

import org.exoplatform.management.annotations.Managed;
import org.exoplatform.management.jmx.annotations.NameTemplate;
import org.exoplatform.management.jmx.annotations.Property;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;

import javax.servlet.ServletContext;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
@Managed
@NameTemplate({
  @Property(key="service",value="portletcontainer"),
  @Property(key="type",value="application"),
  @Property(key="name",value="{Name}")})
public class PortletApplicationManaged {

  /** . */
  final PortletContainerManaged container;

  /** . */
  private final String name;

  /** . */
  private final PortletApp application;

  /** . */
  private final ServletContext servletContext;

  public PortletApplicationManaged(
    PortletContainerManaged container,
    String name,
    PortletApp application,
    ServletContext servletContext) {
    this.container = container;
    this.name = name;
    this.application = application;
    this.servletContext = servletContext;
  }

  @Managed
  public String getName() {
    return name;
  }

}
