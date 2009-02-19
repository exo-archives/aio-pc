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
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Roman Pedchenko
 * @email: roman.pedchenko@exoplatform.com.ua
 * @version: $Id$
 */
public class FilterMapping {

  /**
   * Filter name.
   */
  private String filterName;

  /**
   * Portlet names.
   */
  private List portletName = new ArrayList<String>();

  /**
   * @return filter name
   */
  public final String getFilterName() {
    return filterName;
  }

  /**
   * @param filterName filter name
   */
  public final void setFilterName(final String filterName) {
    this.filterName = filterName;
  }

  /**
   * @return portlet names
   */
  public final List<String> getPortletName() {
    return portletName;
  }

  /**
   * @param portletName portlet names
   */
  public final void setPortletName(final List<String> portletName) {
    this.portletName = portletName;
  }

  /**
   * @param name portlet name
   */
  public final void addPortletName(final String name) {
    this.portletName.add(name);
  }

}
