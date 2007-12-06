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
package org.exoplatform.services.portletcontainer.pci;

import java.util.List;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class CustomModeWithDescription {

  private javax.portlet.PortletMode portletMode;
  private List descriptions;

  public CustomModeWithDescription(javax.portlet.PortletMode portletMode,
                                   List descriptions){
    this.portletMode = portletMode;
    this.descriptions = descriptions;
  }

  /**
   * @return a List of LocalisedDescription objects
   */
  public List getDescriptions() {
    return descriptions;
  }

  public javax.portlet.PortletMode getPortletMode() {
    return portletMode;
  }



}
